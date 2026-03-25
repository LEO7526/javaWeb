<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.util.ArrayList, ict.bean.Phone" %>
<html>
<head><title>Phone List</title></head>
<body>
  <%
    ArrayList<Phone> phones = (ArrayList<Phone>)request.getAttribute("phoneList");
    for(Phone p : phones){
  %>
    <p><%= p.getName() %> - $<%= p.getPrice() %></p>
    <img src="<%= p.getImg() %>"/>
  <% } %>
  <hr/>
  <a href="brandController?action=list">Back to Brands</a>
</body>
</html>
