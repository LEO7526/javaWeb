<%@ page import="java.util.List" %>
<%@ page import="ict.bean.User" %>
<%@ page import="ict.bean.Notification" %>
<%@ page import="ict.util.DateTimeUtil" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    User user = (User) request.getAttribute("currentUser");
    List<Notification> notifications = (List<Notification>) request.getAttribute("notifications");
%>
<!DOCTYPE html>
<html>
<head>
    <title>Notifications</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/app.css"/>
</head>
<body>
<div class="topbar">
    <a href="<%= request.getContextPath() %>/patient/dashboard">Dashboard</a>
    <a href="<%= request.getContextPath() %>/patient/appointments">Appointments</a>
    <a href="<%= request.getContextPath() %>/patient/queue">Queue</a>
    <a href="<%= request.getContextPath() %>/patient/notifications">Notifications</a>
    <a href="<%= request.getContextPath() %>/patient/profile">Profile</a>
    <a href="<%= request.getContextPath() %>/auth/logout">Logout</a>
</div>
<div class="container">
    <div class="card">
        <h2>Notifications</h2>
        <p><a href="<%= request.getContextPath() %>/patient/notifications?markRead=true">Mark all as read</a></p>
        <table>
            <tr><th>Time</th><th>Type</th><th>Status</th><th>Message</th></tr>
            <% if (notifications.isEmpty()) { %>
            <tr><td colspan="4" class="muted">No notifications.</td></tr>
            <% } %>
            <% for (Notification notification : notifications) { %>
            <tr>
                <td><%= DateTimeUtil.format(notification.getCreatedAt()) %></td>
                <td><%= notification.getType() %></td>
                <td><%= notification.isRead() ? "Read" : "Unread" %></td>
                <td><%= notification.getMessage() %></td>
            </tr>
            <% } %>
        </table>
    </div>
</div>
</body>
</html>

