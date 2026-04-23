package ict.servlet;

import ict.bean.*;
import ict.db.*;
import ict.util.AuthUtil;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/staff/template")
public class StaffTemplateServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = AuthUtil.requireRole(request, response, "STAFF");
        if (user == null) return;

        String url = getServletContext().getInitParameter("dbUrl");
        String userDb = getServletContext().getInitParameter("dbUser");
        String passDb = getServletContext().getInitParameter("dbPassword");

      
        AppointmentDB appDb = new AppointmentDB(url, userDb, passDb);
        QueueDB qDb = new QueueDB(url, userDb, passDb);

        request.setAttribute("appointments", appDb.findTodayAppointments());
        request.setAttribute("queues", qDb.findWaitingQueue());
        request.setAttribute("currentUser", user);

        request.getRequestDispatcher("/WEB-INF/jsp/staff/dashboard.jsp").forward(request, response);
    }
}