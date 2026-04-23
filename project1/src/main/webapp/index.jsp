<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>CCHC Community Clinic Appointment and Queue System</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/app.css"/>
</head>
<body>
<div class="topbar">
    <strong>CCHC Community Clinic Appointment and Queue System</strong>
</div>

<div class="container">
    <div class="card">
        <h2>Role-based Portal</h2>
        <p>Patient functions are fully implemented. Staff functions are provided as UI templates only.</p>
        <p><a href="<%= request.getContextPath() %>/auth/login">Go to Login</a></p>
        <p class="muted">Demo patient: patient / patient123</p>
        <p class="muted">Demo staff: staff / staff123</p>
    </div>

    <div class="grid-2">
        <div class="card">
            <h3>Patient Scope Implemented</h3>
            <ul>
                <li>Registration and login</li>
                <li>Appointment booking, reschedule, cancel</li>
                <li>Queue joining and live status</li>
                <li>Notifications and profile update</li>
            </ul>
        </div>

        <div class="card">
            <h3>Staff Scope Template</h3>
            <ul>
                <li>Dashboard structure</li>
                <li>Queue control panel placeholders</li>
                <li>Incident form placeholder</li>
            </ul>
            <p><a href="<%= request.getContextPath() %>/staff/template">Open Staff Template</a></p>
        </div>
    </div>
</div>
</body>
</html>