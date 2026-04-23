package ict.servlet;

import ict.bean.User;
import ict.db.AppointmentDB;
import ict.db.NotificationDB;
import ict.db.QueueDB;
import ict.util.AuthUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/patient/dashboard")
public class PatientDashboardServlet extends HttpServlet {
    private AppointmentDB appointmentDB;
    private NotificationDB notificationDB;
    private QueueDB queueDB;

    @Override
    public void init() {
        String url = getServletContext().getInitParameter("dbUrl");
        String user = getServletContext().getInitParameter("dbUser");
        String pass = getServletContext().getInitParameter("dbPassword");
        appointmentDB = new AppointmentDB(url, user, pass);
        notificationDB = new NotificationDB(url, user, pass);
        queueDB = new QueueDB(url, user, pass);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = AuthUtil.requireRole(request, response, "PATIENT");
        if (user == null) return;

        HttpSession session = request.getSession();
        session.setAttribute("user", user);
        request.setAttribute("user", user);

        request.setAttribute("upcomingAppointments", appointmentDB.findUpcomingByPatient(user.getId()));
        request.setAttribute("unreadNotifications", notificationDB.findUnreadByUser(user.getId()));
        request.setAttribute("activeQueues", queueDB.findTodayByPatient(user.getId()));

        request.getRequestDispatcher("/WEB-INF/jsp/patient/dashboard.jsp").forward(request, response);
    }
}