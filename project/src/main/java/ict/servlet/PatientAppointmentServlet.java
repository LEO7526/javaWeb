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
import java.time.LocalDateTime;
import java.util.List;

@WebServlet("/patient/appointments")
public class PatientAppointmentServlet extends HttpServlet {
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
            action = "list";
        }
        String message;

        try {
            switch (action) {
                case "book":
                    int serviceId = Integer.parseInt(request.getParameter("serviceId"));
                    LocalDateTime slotTime = LocalDateTime.parse(request.getParameter("slotTime"));
                    Appointment appointment = appointmentDB.createAppointment(user.getId(), serviceId, slotTime);
                    if (appointment == null) {
                        message = "Booking failed: duplicated appointment timeslot.";
                    } else {
                        ClinicService service = serviceDB.findById(serviceId);
                        notificationDB.create(user.getId(), "APPOINTMENT", "Appointment booked: " + service.getServiceName());
                        message = "Appointment booked successfully.";
                    }
                    break;
                case "reschedule":
                    int appointmentId = Integer.parseInt(request.getParameter("appointmentId"));
                    LocalDateTime newSlot = LocalDateTime.parse(request.getParameter("newSlot"));
                    boolean rescheduled = appointmentDB.rescheduleAppointment(appointmentId, user.getId(), newSlot);
                    if (rescheduled) {
                        notificationDB.create(user.getId(), "APPOINTMENT", "Your appointment was rescheduled.");
                        message = "Appointment rescheduled.";
                    } else {
                        message = "Reschedule failed.";
                    }
                    break;
                case "cancel":
                    int cancelId = Integer.parseInt(request.getParameter("appointmentId"));
                    boolean cancelled = appointmentDB.cancelAppointment(cancelId, user.getId());
                    if (cancelled) {
                        notificationDB.create(user.getId(), "APPOINTMENT", "Your appointment has been cancelled.");
                        message = "Appointment cancelled.";
                    } else {
                        message = "Cancellation failed.";
                    }
                    break;
                case "list":
                    message = null;
                    break;
                default:
                    message = "Unsupported action.";
                    break;
            }
        } catch (Exception ex) {
            message = "Request failed. Please check input format.";
        }

        if (message != null) {
            request.setAttribute("message", message);
        }
        loadPageData(request, user);
        RequestDispatcher rd = getServletContext().getRequestDispatcher("/WEB-INF/jsp/patient/appointments.jsp");
        rd.forward(request, response);
    }

    private void loadPageData(HttpServletRequest request, User user) {
        List<ClinicService> services = serviceDB.findAll();
        List<Appointment> appointments = appointmentDB.findByPatient(user.getId());
        request.setAttribute("services", services);
        request.setAttribute("appointments", appointments);
        request.setAttribute("currentUser", user);
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}


