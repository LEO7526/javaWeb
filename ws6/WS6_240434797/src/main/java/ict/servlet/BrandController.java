package ict.servlet;

import ict.bean.Brand;
import ict.db.BrandsDB;
import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "BrandController", urlPatterns = {"/brandController"})
public class BrandController extends HttpServlet {

    private BrandsDB db;

    @Override
    public void init() throws ServletException {
        String dbUrl = getServletContext().getInitParameter("dbUrl");
        String dbUser = getServletContext().getInitParameter("dbUser");
        String dbPassword = getServletContext().getInitParameter("dbPassword");
        db = new BrandsDB(dbUrl, dbUser, dbPassword);
        db.createPhoneTable();
        db.seedSampleData();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!isAuthenticated(request)) {
            redirectToLogin(request, response);
            return;
        }
        String action = request.getParameter("action");
        if ("list".equals(action)) {
            ArrayList<Brand> brands = db.getAllBrands();
            request.setAttribute("brands", brands);
            RequestDispatcher rd = getServletContext().getRequestDispatcher("/brands.jsp");
            rd.forward(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_IMPLEMENTED);
        }
    }

    private boolean isAuthenticated(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session != null && session.getAttribute("userInfo") != null;
    }

    private void redirectToLogin(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher rd = getServletContext().getRequestDispatcher("/login.jsp");
        rd.forward(request, response);
    }
}
