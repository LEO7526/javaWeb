package ict.servlet;

import ict.db.AppointmentDB;
import ict.db.ClinicServiceDB;
import ict.db.NotificationDB;
import ict.bean.Appointment;
import ict.bean.ClinicService;
import ict.bean.User;
import ict.util.AuthUtil;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@WebServlet("/patient/appointments")
public class PatientAppointmentServlet extends HttpServlet {
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final LocalTime CLINIC_OPEN = LocalTime.of(9, 0);
    private static final LocalTime CLINIC_CLOSE = LocalTime.of(17, 0);
    private static final int SLOT_MINUTES = 30;

    private AppointmentDB appointmentDB;
    private ClinicServiceDB serviceDB;
    private NotificationDB notificationDB;

    @Override
    public void init() throws ServletException {
        String dbUrl = getServletContext().getInitParameter("dbUrl");
        String dbUser = getServletContext().getInitParameter("dbUser");
        String dbPassword = getServletContext().getInitParameter("dbPassword");
        if (isBlank(dbUrl) || isBlank(dbUser) || dbPassword == null) {
            throw new ServletException("Database parameters are missing in web.xml");
        }

        appointmentDB = new AppointmentDB(dbUrl, dbUser, dbPassword);
        serviceDB = new ClinicServiceDB(dbUrl, dbUser, dbPassword);
        notificationDB = new NotificationDB(dbUrl, dbUser, dbPassword);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        processRequest(request, response);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        User user = AuthUtil.requireRole(request, response, "PATIENT");
        if (user == null) {
            return;
        }

        String action = request.getParameter("action");
        if (isBlank(action)) {
            action = "view";
        }
        String message;
        String messageType;

        try {
            switch (action) {
                case "book":
                    int serviceId = Integer.parseInt(request.getParameter("serviceId"));
                    LocalDateTime slotTime = LocalDateTime.parse(request.getParameter("slotTime"));
                    Appointment appointment = appointmentDB.createAppointment(user.getId(), serviceId, slotTime);
                    if (appointment == null) {
                        message = "Booking failed: duplicated appointment timeslot.";
                        messageType = "error";
                    } else {
                        ClinicService service = serviceDB.findById(serviceId);
                        notificationDB.create(user.getId(), "APPOINTMENT", "Appointment booked: " + service.getServiceName());
                        message = "Appointment booked successfully.";
                        messageType = "success";
                    }
                    break;
                case "reschedule":
                    int appointmentId = Integer.parseInt(request.getParameter("appointmentId"));
                    LocalDateTime newSlot = LocalDateTime.parse(request.getParameter("newSlot"));
                    boolean rescheduled = appointmentDB.rescheduleAppointment(appointmentId, user.getId(), newSlot);
                    if (rescheduled) {
                        notificationDB.create(user.getId(), "APPOINTMENT", "Your appointment was rescheduled.");
                        message = "Appointment rescheduled.";
                        messageType = "success";
                    } else {
                        message = "Reschedule failed.";
                        messageType = "warning";
                    }
                    break;
                case "cancel":
                    int cancelId = Integer.parseInt(request.getParameter("appointmentId"));
                    boolean cancelled = appointmentDB.cancelAppointment(cancelId, user.getId());
                    if (cancelled) {
                        notificationDB.create(user.getId(), "APPOINTMENT", "Your appointment has been cancelled.");
                        message = "Appointment cancelled.";
                        messageType = "success";
                    } else {
                        message = "Cancellation failed.";
                        messageType = "warning";
                    }
                    break;
                case "list":
                case "view":
                    message = null;
                    messageType = null;
                    break;
                default:
                    message = "Unsupported action.";
                    messageType = "error";
                    break;
            }
        } catch (Exception ex) {
            message = "Request failed. Please check input format.";
            messageType = "error";
        }

        if (message != null) {
            request.setAttribute("message", message);
            request.setAttribute("messageType", messageType == null ? "success" : messageType);
        }
        loadPageData(request, user);
        RequestDispatcher rd = getServletContext().getRequestDispatcher("/WEB-INF/jsp/patient/appointments.jsp");
        rd.forward(request, response);
    }

    private void loadPageData(HttpServletRequest request, User user) {
        String selectedClinic = normalize(request.getParameter("clinic"));
        String selectedServiceId = normalize(request.getParameter("serviceId"));
        String selectedDate = normalize(request.getParameter("date"));

        List<ClinicService> allServices = serviceDB.findAll();
        List<String> clinicOptions = allServices.stream()
                .map(ClinicService::getClinicName)
                .distinct()
                .collect(Collectors.toList());

        List<ClinicService> services = allServices;
        if (!isBlank(selectedClinic)) {
            final String clinicFilter = selectedClinic;
            services = allServices.stream()
                .filter(service -> clinicFilter.equals(service.getClinicName()))
                    .collect(Collectors.toList());
        }

        LocalDate selectedLocalDate = null;
        if (!isBlank(selectedDate)) {
            selectedLocalDate = LocalDate.parse(selectedDate);
        }

        ClinicService selectedService = null;
        if (!isBlank(selectedServiceId)) {
            int serviceId = Integer.parseInt(selectedServiceId);
            selectedService = serviceDB.findById(serviceId);
            if (selectedService != null && isBlank(selectedClinic)) {
                selectedClinic = selectedService.getClinicName();
            }
        }

        List<LocalDateTime> availableSlots = buildAvailableSlots(selectedService, selectedLocalDate);
        List<Appointment> appointments = appointmentDB.findByPatient(user.getId());

        request.setAttribute("clinicOptions", clinicOptions);
        request.setAttribute("appointments", appointments);
        request.setAttribute("availableSlots", availableSlots);
        request.setAttribute("selectedClinic", selectedClinic);
        request.setAttribute("selectedServiceId", selectedServiceId);
        request.setAttribute("selectedDate", selectedLocalDate == null ? LocalDate.now().format(DATE_FORMAT) : selectedLocalDate.format(DATE_FORMAT));
        request.setAttribute("selectedService", selectedService);
        request.setAttribute("currentUser", user);
        request.setAttribute("services", services);
    }

    private List<LocalDateTime> buildAvailableSlots(ClinicService selectedService, LocalDate selectedDate) {
        List<LocalDateTime> slots = new ArrayList<>();
        if (selectedService == null || selectedDate == null) {
            return slots;
        }

        Set<LocalDateTime> bookedSlots = appointmentDB.findBookedSlotTimesByServiceAndDate(selectedService.getId(), selectedDate);
        LocalDateTime cursor = selectedDate.atTime(CLINIC_OPEN);
        LocalDateTime end = selectedDate.atTime(CLINIC_CLOSE);

        while (cursor.isBefore(end)) {
            if (!bookedSlots.contains(cursor)) {
                slots.add(cursor);
            }
            cursor = cursor.plusMinutes(SLOT_MINUTES);
        }
        return slots;
    }

    private String normalize(String value) {
        return isBlank(value) ? null : value.trim();
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}


