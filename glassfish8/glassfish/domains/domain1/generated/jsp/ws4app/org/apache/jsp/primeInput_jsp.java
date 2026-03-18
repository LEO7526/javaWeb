package org.apache.jsp;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.jsp.*;

public final class primeInput_jsp extends org.glassfish.wasp.runtime.HttpJspBase
    implements org.glassfish.wasp.runtime.JspSourceDependent {

  private static final JspFactory _jspxFactory = JspFactory.getDefaultFactory();

  private static java.util.List<String> _jspx_dependants;

  private org.glassfish.jsp.api.ResourceInjector _jspx_resourceInjector;

  public java.util.List<String> getDependants() {
    return _jspx_dependants;
  }

  public boolean getErrorOnELNotFound() {
    return false;
  }

  public void _jspService(HttpServletRequest request, HttpServletResponse response)
        throws java.io.IOException, ServletException {

    PageContext pageContext = null;
    HttpSession session = null;
    ServletContext application = null;
    ServletConfig config = null;
    JspWriter out = null;
    Object page = this;
    JspWriter _jspx_out = null;
    PageContext _jspx_page_context = null;

    try {
      response.setContentType("text/html;charset=UTF-8");
      response.setHeader("X-Powered-By", "JSP/3.0");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			null, true, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;
      _jspx_resourceInjector = (org.glassfish.jsp.api.ResourceInjector) application.getAttribute("com.sun.appserv.jsp.resource.injector");

      out.write("\n");
      out.write("<!DOCTYPE html>\n");
      out.write("<html>\n");
      out.write("<head>\n");
      out.write("    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n");
      out.write("    <title>Prime number generator</title>\n");
      out.write("</head>\n");
      out.write("<body>\n");
      out.write("    <h1>Prime number generator</h1>\n");
      out.write("    <form action=\"prime.jsp\" method=\"get\">\n");
      out.write("        <label for=\"min\">Minimum:</label>\n");
      out.write("        <input type=\"text\" id=\"min\" name=\"min\" value=\"5\" /><br/>\n");
      out.write("        <label for=\"max\">Maximum:</label>\n");
      out.write("        <input type=\"text\" id=\"max\" name=\"max\" value=\"50\" /><br/>\n");
      out.write("\n");
      out.write("        <input type=\"radio\" id=\"simple\" name=\"tagType\" value=\"simple\" checked=\"checked\" />\n");
      out.write("        <label for=\"simple\">Simple</label>\n");
      out.write("        <input type=\"radio\" id=\"table\" name=\"tagType\" value=\"table\" />\n");
      out.write("        <label for=\"table\">Table</label>\n");
      out.write("        <br/>\n");
      out.write("\n");
      out.write("        <input type=\"submit\" value=\"generate\" />\n");
      out.write("    </form>\n");
      out.write("</body>\n");
      out.write("</html>\n");
    } catch (Throwable t) {
      if (!(t instanceof SkipPageException)){
        out = _jspx_out;
        if (out != null && out.getBufferSize() != 0)
          out.clearBuffer();
        if (_jspx_page_context != null) _jspx_page_context.handlePageException(t);
        else throw new ServletException(t);
      }
    } finally {
      _jspxFactory.releasePageContext(_jspx_page_context);
    }
  }
}
