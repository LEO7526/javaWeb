package ict.tags;

import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import java.io.IOException;

// import required lib
public class CustomerTag  /* extends the required class*/{
  public CustomerTag() {
  }

  // define a variable customers as ArrayList type
// define the tagType as String
// define the settter method for customers and tagType
  @Override
  public void doTag() {
    try {
      PageContext pageContext = (PageContext) getJspContext();
      JspWriter out = pageContext.getOut();
      if ("simple".equalsIgnoreCase( tagType)) {
  // display the simple format
      } else if ("list".equalsIgnoreCase( tagType)) {
      // display the list format
    } else {
      out.println("No such type");
    }
  } catch (IOException ioe) {
    out.println("Error generating prime: " + ioe);
  }}}
