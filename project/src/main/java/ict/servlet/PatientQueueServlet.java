package ict.servlet;

import ict.db.ClinicServiceDB;
import ict.db.NotificationDB;
import ict.db.QueueDB;
import ict.bean.ClinicService;
import ict.bean.QueueEntry;
import ict.bean.User;
import ict.util.AuthUtil;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/patient/queue")
public class PatientQueueServlet extends HttpServlet {
    private QueueDB queueDB;
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

        queueDB = new QueueDB(dbUrl, dbUser, dbPassword);
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
        String messageType;

        if ("join".equals(action)) {
            try {
                int serviceId = Integer.parseInt(request.getParameter("serviceId"));
                QueueEntry entry = queueDB.joinTodayQueue(user.getId(), serviceId);
                ClinicService service = serviceDB.findById(serviceId);
                notificationDB.create(user.getId(), "QUEUE", "Queue joined for " + service.getServiceName() + ", number: " + entry.getQueueNumber());
                message = "Joined queue successfully.";
                messageType = "success";
            } catch (Exception ex) {
                message = "Queue join failed.";
                messageType = "error";
            }
        } else if ("list".equals(action)) {
            message = null;
            messageType = null;
        } else {
            message = "Unsupported queue action.";
            messageType = "error";
        }

        if (message != null) {
            request.setAttribute("message", message);
            request.setAttribute("messageType", messageType == null ? "success" : messageType);
        }
        loadPageData(request, user);
        RequestDispatcher rd = getServletContext().getRequestDispatcher("/WEB-INF/jsp/patient/queue.jsp");
        rd.forward(request, response);
    }

    private void loadPageData(HttpServletRequest request, User user) {
        List<ClinicService> services = serviceDB.findAll();
        List<QueueEntry> myQueue = queueDB.findTodayByPatient(user.getId());
        Map<Integer, Integer> waitEstimate = new HashMap<>();
        for (QueueEntry entry : myQueue) {
            waitEstimate.put(entry.getId(), queueDB.estimateWaitMinutes(entry.getServiceId(), entry.getQueueNumber()));
        }
        request.setAttribute("services", services);
        request.setAttribute("myQueue", myQueue);
        request.setAttribute("waitEstimate", waitEstimate);
        request.setAttribute("currentUser", user);
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}


