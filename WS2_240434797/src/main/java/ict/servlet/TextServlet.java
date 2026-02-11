package ict.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet(name = "TextServlet", urlPatterns = {"/hello"})
public class TextServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse res)
        throws ServletException, IOException {
        res.setContentType("text/html");
        PrintWriter out = res.getWriter();
        String name = req.getParameter("name");
        
        if(name == null || name.isEmpty()){
            out.println("<html>");
            out.println("<head><title>Hello World</title></head>");
            out.println("<body>");
            out.println("Hello World");
            out.println("</body></html>");
        }else{
            out.println("<html>");
        out.println("<head><title>hello, " + name + "</title></head>");
        out.println("<body>");
        out.println("Hello, " + name);
        out.println("</body></html>");
        }
        
    }
}
