package org.apache.jsp;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.jsp.*;

public final class AttributeExample_jsp extends org.glassfish.wasp.runtime.HttpJspBase
    implements org.glassfish.wasp.runtime.JspSourceDependent {

  private static final JspFactory _jspxFactory = JspFactory.getDefaultFactory();

  private static java.util.List<String> _jspx_dependants;

  static {
    _jspx_dependants = new java.util.ArrayList<String>(1);
    _jspx_dependants.add("/WEB-INF/tlds/ict-taglib.tld");
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
      out.write("    <title>Attribute Example</title>\n");
      out.write("</head>\n");
      out.write("<body>\n");
      out.write("    <h1>");
      if (_jspx_meth_ict_attribute_0(_jspx_page_context))
        return;
      out.write("</h1>\n");
      out.write("    <h1>");
      if (_jspx_meth_ict_attribute_1(_jspx_page_context))
        return;
      out.write("</h1>\n");
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

  private boolean _jspx_meth_ict_attribute_0(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ict:attribute
    ict.tags.AttributeTag _jspx_th_ict_attribute_0 = (_jspx_resourceInjector != null)? _jspx_resourceInjector.createTagHandlerInstance(ict.tags.AttributeTag.class) : new ict.tags.AttributeTag();
    _jspx_th_ict_attribute_0.setJspContext(_jspx_page_context);
    _jspx_th_ict_attribute_0.doTag();
    if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_ict_attribute_0);
    return false;
  }

  private boolean _jspx_meth_ict_attribute_1(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ict:attribute
    ict.tags.AttributeTag _jspx_th_ict_attribute_1 = (_jspx_resourceInjector != null)? _jspx_resourceInjector.createTagHandlerInstance(ict.tags.AttributeTag.class) : new ict.tags.AttributeTag();
    _jspx_th_ict_attribute_1.setJspContext(_jspx_page_context);
    _jspx_th_ict_attribute_1.setMessage("Welcome to ICT");
    _jspx_th_ict_attribute_1.doTag();
    if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_ict_attribute_1);
    return false;
  }
}
