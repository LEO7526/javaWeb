package org.apache.jsp;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.jsp.*;

public final class prime_jsp extends org.glassfish.wasp.runtime.HttpJspBase
    implements org.glassfish.wasp.runtime.JspSourceDependent {

  private static final JspFactory _jspxFactory = JspFactory.getDefaultFactory();

  private static java.util.List<String> _jspx_dependants;

  static {
    _jspx_dependants = new java.util.ArrayList<String>(1);
    _jspx_dependants.add("/WEB-INF/tlds/primes.tld");
  }

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
      out.write("\n");
      out.write("<!DOCTYPE html>\n");
      out.write("<html>\n");
      out.write("<head>\n");
      out.write("    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n");
      out.write("    <title>Prime Result</title>\n");
      out.write("</head>\n");
      out.write("<body>\n");

    int min = 2;
    int max = 50;
    String tagType = "simple";

    try {
        if (request.getParameter("min") != null) {
            min = Integer.parseInt(request.getParameter("min"));
        }
        if (request.getParameter("max") != null) {
            max = Integer.parseInt(request.getParameter("max"));
        }
        if (request.getParameter("tagType") != null) {
            tagType = request.getParameter("tagType");
        }
    } catch (NumberFormatException ignored) {
    }

      out.write("\n");
      out.write("\n");
      out.write("<h2>Prime Numbers (");
      out.print(min);
      out.write(" to ");
      out.print(max);
      out.write(")</h2>\n");
      //  ict:prime
      ict.tags.PrimeTag _jspx_th_ict_prime_0 = (_jspx_resourceInjector != null)? _jspx_resourceInjector.createTagHandlerInstance(ict.tags.PrimeTag.class) : new ict.tags.PrimeTag();
      _jspx_th_ict_prime_0.setJspContext(_jspx_page_context);
      _jspx_th_ict_prime_0.setMin(min);
      _jspx_th_ict_prime_0.setMax(max);
      _jspx_th_ict_prime_0.setTagType(tagType);
      _jspx_th_ict_prime_0.doTag();
      if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_ict_prime_0);
      out.write("\n");
      out.write("<p><a href=\"primeInput.jsp\">Back</a></p>\n");
      out.write("</body>\n");
      out.write("</html>\r\n");
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
