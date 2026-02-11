package ict.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet(name = "Radio", urlPatterns = {"/radio"})
public class Radio extends HttpServlet {

    public void doGet(HttpServletRequest req, HttpServletResponse res)
        throws ServletException, IOException {

        res.setContentType("text/html");
        PrintWriter out = res.getWriter();
        
        String size = req.getParameter("employee");
        
        if(size==null){
            out.println("<html>");
        out.println("<head><title>radio button</title></head>");
        out.println("<body>");
        out.println("your company size :  " );
                out.println("</br>");
        out.println("<a href=\"radio.jsp\">Try again!!</a>");
        out.println("</body></html>");
        
        }else{
            out.println("<html>");
        out.println("<head><title>radio button</title></head>");
        out.println("<body>");
        out.println("your company size : " + size);
                        out.println("</br>");
                out.println("<a href=\"radio.jsp\">Try again!!</a>");
        out.println("</body></html>");
        }

    }
}
