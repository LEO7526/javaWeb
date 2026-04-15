<%@ page import="ict.bean.User" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    User user = (User) request.getAttribute("currentUser");
%>
<!DOCTYPE html>
<html>
<head>
    <title>Profile</title>
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
        <h2>Profile Management</h2>
        <% if (request.getAttribute("message") != null) { %>
            <div class="message"><%= request.getAttribute("message") %></div>
        <% } %>
    </div>

    <div class="grid-2">
        <div class="card">
            <h3>Update Personal Details</h3>
            <form method="post" action="<%= request.getContextPath() %>/patient/profile">
                <input type="hidden" name="action" value="updateProfile"/>
                <label>Full Name</label>
                <input type="text" name="fullName" value="<%= user.getFullName() == null ? "" : user.getFullName() %>" required/>
                <label>Phone</label>
                <input type="text" name="phone" value="<%= user.getPhone() == null ? "" : user.getPhone() %>"/>
                <label>Email</label>
                <input type="email" name="email" value="<%= user.getEmail() == null ? "" : user.getEmail() %>"/>
                <button type="submit">Save Profile</button>
            </form>
        </div>

        <div class="card">
            <h3>Change Password</h3>
            <form method="post" action="<%= request.getContextPath() %>/patient/profile">
                <input type="hidden" name="action" value="changePassword"/>
                <label>Current Password</label>
                <input type="password" name="oldPassword" required/>
                <label>New Password</label>
                <input type="password" name="newPassword" required/>
                <button type="submit">Update Password</button>
            </form>
        </div>
    </div>
</div>
</body>
</html>

