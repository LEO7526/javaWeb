package ict.servlet;

import ict.util.Converter;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "TemperatureServlet", urlPatterns = {"/temperature"})
public class TemperatureServlet extends HttpServlet {

    public void doGet(HttpServletRequest req, HttpServletResponse res)
        throws ServletException, IOException {

        res.setContentType("text/html");
        PrintWriter out = res.getWriter();
        
        String tempStr = req.getParameter("temperature");
        String conversionType = req.getParameter("conversionType");
        
        out.println("<html>");
        out.println("<head><title>Temperature Converter</title></head>");
        out.println("<body>");
        
        if (tempStr == null || tempStr.isEmpty() || conversionType == null) {
            out.println("<h3>Please provide temperature value and conversion type</h3>");
            out.println("<a href=\"converter.jsp\">Try again</a>");
        } else {
            try {
                double temp = Double.parseDouble(tempStr);
                Converter converter = new Converter();
                double result;
                String resultMsg;
                
                if ("toFahrenheit".equals(conversionType)) {
                    result = converter.toFahrenheit(temp);
                    resultMsg = temp + "°C = " + String.format("%.2f", result) + "°F";
                } else if ("toCelsius".equals(conversionType)) {
                    result = converter.toCelsius(temp);
                    resultMsg = temp + "°F = " + String.format("%.2f", result) + "°C";
                } else {
                    resultMsg = "Invalid conversion type";
                }
                
                out.println("<h3>" + resultMsg + "</h3>");
                out.println("<br/><a href=\"converter.jsp\">Convert another temperature</a>");
            } catch (NumberFormatException e) {
                out.println("<h3>Invalid temperature value. Please enter a valid number.</h3>");
                out.println("<a href=\"converter.jsp\">Try again</a>");
            }
        }
        
        out.println("</body></html>");
    }
}