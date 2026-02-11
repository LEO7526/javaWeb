package ict.servlet;

import ict.util.Converter;
import ict.util.HtmlUtil;
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
        out.println("<head><title>Temperature Converter Result</title></head>");
        out.println("<body>");
        out.println("<h1>Temperature Converter Result</h1>");
        
        if (tempStr == null || tempStr.isEmpty() || conversionType == null) {
            out.println("<p>Please provide temperature value and conversion type.</p>");
            out.println("<br/><a href=\"converter.jsp\">Try again</a>");
        } else {
            try {
                double temp = Double.parseDouble(tempStr);
                Converter converter = new Converter();
                double result;
                String resultMsg;
                
                if ("toFahrenheit".equals(conversionType)) {
                    result = converter.toFahrenheit(temp);
                    resultMsg = String.format("%.2f°C = %.2f°F", temp, result);
                } else if ("toCelsius".equals(conversionType)) {
                    result = converter.toCelsius(temp);
                    resultMsg = String.format("%.2f°F = %.2f°C", temp, result);
                } else {
                    resultMsg = "Invalid conversion type";
                }
                
                out.println("<p><strong>" + HtmlUtil.escapeHtml(resultMsg) + "</strong></p>");
                out.println("<br/><a href=\"converter.jsp\">Convert another temperature</a>");
            } catch (NumberFormatException e) {
                out.println("<p style=\"color: red;\">Invalid temperature value. Please enter a valid number.</p>");
                out.println("<br/><a href=\"converter.jsp\">Try again</a>");
            }
        }
        
        out.println("</body></html>");
    }
}