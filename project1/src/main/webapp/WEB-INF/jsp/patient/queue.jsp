<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="ict.bean.User" %>
<%@ page import="ict.bean.QueueEntry" %>
<%@ page import="ict.bean.ClinicService" %>
<%@ page import="ict.util.DateTimeUtil" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    User user = (User) request.getAttribute("currentUser");
    List<ClinicService> services = (List<ClinicService>) request.getAttribute("services");
    List<QueueEntry> myQueue = (List<QueueEntry>) request.getAttribute("myQueue");
    Map<Integer, Integer> waitEstimate = (Map<Integer, Integer>) request.getAttribute("waitEstimate");
    Map<Integer, ClinicService> serviceMap = new HashMap<>();
    for (ClinicService service : services) {
        serviceMap.put(service.getId(), service);
    }
%>
<!DOCTYPE html>
<html>
<head>
    <title>Queue Management</title>
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
        <h2>Same-day Queue</h2>
        <% if (request.getAttribute("message") != null) { %>
            <div class="message"><%= request.getAttribute("message") %></div>
        <% } %>
        <form method="post" action="<%= request.getContextPath() %>/patient/queue">
            <input type="hidden" name="action" value="join"/>
            <label>Select Service to Join Queue</label>
            <select name="serviceId" required>
                <% for (ClinicService service : services) { %>
                    <option value="<%= service.getId() %>"><%= service.getClinicName() %> - <%= service.getServiceName() %></option>
                <% } %>
            </select>
            <button type="submit">Join Queue</button>
        </form>
    </div>

    <div class="card">
        <h3>Live Queue Status (Today)</h3>
        <table>
            <tr><th>Service</th><th>Queue #</th><th>Status</th><th>Est. Wait</th><th>Joined</th></tr>
            <% if (myQueue.isEmpty()) { %>
            <tr><td colspan="5" class="muted">No queue entry today.</td></tr>
            <% } %>
            <% for (QueueEntry entry : myQueue) {
                ClinicService service = serviceMap.get(entry.getServiceId());
                Integer wait = waitEstimate.get(entry.getId());
            %>
            <tr>
                <td><%= service == null ? "N/A" : service.getServiceName() %></td>
                <td><%= entry.getQueueNumber() %></td>
                <td><span class="badge"><%= entry.getStatus() %></span></td>
                <td><%= wait == null ? 0 : wait %> mins</td>
                <td><%= DateTimeUtil.format(entry.getJoinedAt()) %></td>
            </tr>
            <% } %>
        </table>
    </div>
</div>
</body>
</html>

