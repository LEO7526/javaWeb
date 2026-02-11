package ict.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet(name = "ide", urlPatterns = {"/checkbox"})
public class Checkbox extends HttpServlet {

    public void doGet(HttpServletRequest req,
                      HttpServletResponse res)
        throws ServletException, IOException {

        res.setContentType("text/html");
        PrintWriter out = res.getWriter();

        String[] ideName = req.getParameterValues("ide");

        out.println("<html>");
        out.println("<head><title>checkbox button</title></head>");
        out.println("<body>");
        out.println("Your choices are :");
        if (ideName != null) {
            for (int i = 0; i < ideName.length; i++) {
                out.println("<br><b>" + ideName[i] + "</b>");
            }
        }
        out.println("</body></html>");
    }
}
