<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="ict.bean.User" %>
<%@ page import="ict.bean.Appointment" %>
<%@ page import="ict.bean.ClinicService" %>
<%@ page import="ict.util.DateTimeUtil" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    User user = (User) request.getAttribute("currentUser");
    List<ClinicService> services = (List<ClinicService>) request.getAttribute("services");
    List<Appointment> appointments = (List<Appointment>) request.getAttribute("appointments");
    Map<Integer, ClinicService> serviceMap = new HashMap<>();
    for (ClinicService service : services) {
        serviceMap.put(service.getId(), service);
    }
%>
<!DOCTYPE html>
<html>
<head>
    <title>Appointment Management</title>
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
        <h2>Appointment Management</h2>
        <p class="muted">Book, reschedule, and cancel appointments. Double booking prevention is enabled.</p>
        <% if (request.getAttribute("message") != null) { %>
            <div class="message"><%= request.getAttribute("message") %></div>
        <% } %>
    </div>

    <div class="grid-2">
        <div class="card">
            <h3>Book Appointment</h3>
            <form method="post" action="<%= request.getContextPath() %>/patient/appointments">
                <input type="hidden" name="action" value="book"/>
                <label>Clinic Service</label>
                <select name="serviceId" required>
                    <% for (ClinicService service : services) { %>
                        <option value="<%= service.getId() %>"><%= service.getClinicName() %> - <%= service.getServiceName() %></option>
                    <% } %>
                </select>
                <label>Date and Time</label>
                <input type="datetime-local" name="slotTime" required/>
                <button type="submit">Book</button>
            </form>
        </div>

        <div class="card">
            <h3>Reschedule Appointment</h3>
            <form method="post" action="<%= request.getContextPath() %>/patient/appointments">
                <input type="hidden" name="action" value="reschedule"/>
                <label>Appointment ID</label>
                <input type="number" name="appointmentId" required/>
                <label>New Date and Time</label>
                <input type="datetime-local" name="newSlot" required/>
                <button type="submit">Reschedule</button>
            </form>
        </div>
    </div>

    <div class="card">
        <h3>Your Appointment History</h3>
        <table>
            <tr><th>ID</th><th>Service</th><th>Slot</th><th>Status</th><th>Action</th></tr>
            <% if (appointments.isEmpty()) { %>
                <tr><td colspan="5" class="muted">No appointment records.</td></tr>
            <% } %>
            <% for (Appointment appointment : appointments) {
                ClinicService service = serviceMap.get(appointment.getServiceId());
            %>
                <tr>
                    <td>#<%= appointment.getId() %></td>
                    <td><%= service == null ? "N/A" : service.getServiceName() %></td>
                    <td><%= DateTimeUtil.format(appointment.getSlotTime()) %></td>
                    <td><span class="badge"><%= appointment.getStatus() %></span></td>
                    <td>
                        <% if (!"CANCELLED".equals(appointment.getStatus())) { %>
                        <form method="post" action="<%= request.getContextPath() %>/patient/appointments">
                            <input type="hidden" name="action" value="cancel"/>
                            <input type="hidden" name="appointmentId" value="<%= appointment.getId() %>"/>
                            <button type="submit">Cancel</button>
                        </form>
                        <% } else { %>
                            <span class="muted">-</span>
                        <% } %>
                    </td>
                </tr>
            <% } %>
        </table>
    </div>
</div>
</body>
</html>

