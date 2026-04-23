package ict.servlet;

import ict.bean.QueueEntry;
import ict.bean.User;
import ict.db.QueueDB;
import ict.util.AuthUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/patient/queue")
public class PatientQueueServlet extends HttpServlet {
    private QueueDB queueDB;

    @Override
    public void init() {
        String url = getServletContext().getInitParameter("dbUrl");
        String user = getServletContext().getInitParameter("dbUser");
        String pass = getServletContext().getInitParameter("dbPassword");
        queueDB = new QueueDB(url, user, pass);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = AuthUtil.requireRole(request, response, "PATIENT");
        if (user == null) return;

        List<QueueEntry> entries = queueDB.findTodayByPatient(user.getId());
        if (!entries.isEmpty()) {
            QueueEntry entry = entries.get(0);
            request.setAttribute("queueEntry", entry);
            request.setAttribute("waitTime", queueDB.estimateWaitMinutes(entry.getClinicId(), entry.getQueueNumber()));
        }

        request.getRequestDispatcher("/WEB-INF/jsp/patient/queue.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = AuthUtil.requireRole(request, response, "PATIENT");
        if (user == null) return;

        String action = request.getParameter("action");
        if ("join".equals(action)) {
            int serviceId = Integer.parseInt(request.getParameter("serviceId"));
            queueDB.joinTodayQueue(user.getId(), serviceId);
        }

        response.sendRedirect("queue");
    }
}