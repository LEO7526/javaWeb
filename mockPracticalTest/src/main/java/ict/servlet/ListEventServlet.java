package ict.servlet;

import ict.bean.EventBean;
import ict.db.EventDB;
import java.io.IOException;
import java.util.ArrayList;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ListEventServlet extends HttpServlet {
    private EventDB eventDB;

    @Override
    public void init() throws ServletException {
        String dbUrl = getServletContext().getInitParameter("dbUrl");
        String dbUser = getServletContext().getInitParameter("dbUser");
        String dbPassword = getServletContext().getInitParameter("dbPassword");
        eventDB = new EventDB(dbUrl, dbUser, dbPassword);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String venue = request.getParameter("venue");

        if (venue == null || venue.trim().isEmpty()) {
            throw new ServletException("Venue is required.");
        }

        try {
            ArrayList<EventBean> events = eventDB.getEvents(venue);

            request.setAttribute("venue", venue);
            request.setAttribute("eventList", events);
            request.getRequestDispatcher("/list.jsp").forward(request, response);
        } catch (Exception ex) {
            throw new ServletException(ex);
        }
    }
}
