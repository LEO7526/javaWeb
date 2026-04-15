<%@ page import="ict.bean.User" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    User user = (User) request.getAttribute("currentUser");
%>
<!DOCTYPE html>
<html>
<head>
    <title>Staff Dashboard Template</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/app.css"/>
</head>
<body>
<div class="topbar">
    <a href="<%= request.getContextPath() %>/staff/template">Staff Dashboard</a>
    <a href="<%= request.getContextPath() %>/auth/logout">Logout</a>
</div>
<div class="container">
    <div class="card">
        <h2>Staff Dashboard Template</h2>
        <p class="muted">Hello <%= user.getFullName() %>. This page contains only structure templates for staff features.</p>
    </div>

    <div class="grid-2">
        <div class="card">
            <h3>Daily Appointment and Queue View</h3>
            <p class="muted">Template placeholders:</p>
            <ul>
                <li>Filter by clinic and service</li>
                <li>Real-time appointment list</li>
                <li>Queue panel with patient flow</li>
            </ul>
            <button type="button">Call Next (Template)</button>
            <button type="button">Skip (Template)</button>
            <button type="button">Complete (Template)</button>
        </div>

        <div class="card">
            <h3>Check-in and Visit Outcome</h3>
            <p class="muted">Template placeholders:</p>
            <ul>
                <li>Mark Arrived / Completed / No-show</li>
                <li>Record cancellation by clinic</li>
                <li>Trigger patient notifications</li>
            </ul>
            <button type="button">Mark Arrived (Template)</button>
            <button type="button">Mark No-show (Template)</button>
        </div>
    </div>

    <div class="card">
        <h3>Incident Reporting and Audit Trail</h3>
        <p class="muted">Basic structure only. Backend implementation pending.</p>
        <form>
            <label>Incident Summary</label>
            <input type="text" placeholder="Doctor unavailable / service suspended"/>
            <label>Detail</label>
            <textarea rows="4" placeholder="Template only"></textarea>
            <button type="button">Submit Incident (Template)</button>
        </form>
    </div>
</div>
</body>
</html>

