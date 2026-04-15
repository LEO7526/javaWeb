<%@ page import="java.util.List" %>
<%@ page import="ict.bean.User" %>
<%@ page import="ict.bean.Appointment" %>
<%@ page import="ict.bean.QueueEntry" %>
<%@ page import="ict.bean.Notification" %>
<%@ page import="ict.util.DateTimeUtil" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    User user = (User) request.getAttribute("currentUser");
    List<Appointment> upcomingAppointments = (List<Appointment>) request.getAttribute("upcomingAppointments");
    List<QueueEntry> todayQueue = (List<QueueEntry>) request.getAttribute("todayQueue");
    List<Notification> notifications = (List<Notification>) request.getAttribute("notifications");
%>
<!DOCTYPE html>
<html>
<head>
    <title>Patient Dashboard</title>
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
        <h2>Welcome, <%= user.getFullName() %></h2>
        <p class="muted">Patient portal for appointment booking, queue tracking, and notifications.</p>
    </div>

    <div class="grid-2">
        <div class="card">
            <h3>Upcoming Appointments</h3>
            <table>
                <tr><th>ID</th><th>Date/Time</th><th>Status</th></tr>
                <% if (upcomingAppointments.isEmpty()) { %>
                <tr><td colspan="3" class="muted">No upcoming appointment.</td></tr>
                <% } %>
                <% for (Appointment appointment : upcomingAppointments) { %>
                <tr>
                    <td>#<%= appointment.getId() %></td>
                    <td><%= DateTimeUtil.format(appointment.getSlotTime()) %></td>
                    <td><span class="badge"><%= appointment.getStatus() %></span></td>
                </tr>
                <% } %>
            </table>
        </div>

        <div class="card">
            <h3>Today Queue Status</h3>
            <table>
                <tr><th>Queue #</th><th>Status</th><th>Joined</th></tr>
                <% if (todayQueue.isEmpty()) { %>
                <tr><td colspan="3" class="muted">Not in any queue today.</td></tr>
                <% } %>
                <% for (QueueEntry entry : todayQueue) { %>
                <tr>
                    <td><%= entry.getQueueNumber() %></td>
                    <td><span class="badge"><%= entry.getStatus() %></span></td>
                    <td><%= DateTimeUtil.format(entry.getJoinedAt()) %></td>
                </tr>
                <% } %>
            </table>
        </div>
    </div>

    <div class="card">
        <h3>Latest Notifications</h3>
        <table>
            <tr><th>Time</th><th>Type</th><th>Message</th></tr>
            <% if (notifications.isEmpty()) { %>
            <tr><td colspan="3" class="muted">No notifications yet.</td></tr>
            <% } %>
            <% for (int i = 0; i < notifications.size() && i < 5; i++) {
                Notification notification = notifications.get(i);
            %>
            <tr>
                <td><%= DateTimeUtil.format(notification.getCreatedAt()) %></td>
                <td><%= notification.getType() %></td>
                <td><%= notification.getMessage() %></td>
            </tr>
            <% } %>
        </table>
    </div>
</div>
</body>
</html>

