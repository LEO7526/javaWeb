<%-- 
    Document   : studentInfo
    Created on : Jan 28, 2026, 10:16:34 AM
    Author     : a1
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Student Information Form</h1>
        <form action="StudentInfoServlet" method="get">
            <label for="studentId">Student ID:</label><br/>
            <input type="text" id="studentId" name="studentId" required /><br/><br/>
            
            <label for="studentName">Student Name:</label><br/>
            <input type="text" id="studentName" name="studentName" required /><br/><br/>
            
            <label for="email">Email:</label><br/>
            <input type="email" id="email" name="email" required /><br/><br/>
            
            <label for="major">Major:</label><br/>
            <input type="text" id="major" name="major" required /><br/><br/>
            
            <input type="submit" value="Submit">
        </form>
    </body>
</html>
