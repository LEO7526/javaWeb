<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ page isErrorPage="true" %>
<!DOCTYPE html>
<html>
<head>
    <title>Error Page</title>
    <style>
        body {
            font-family: Georgia, "Times New Roman", serif;
            margin: 24px 32px;
        }
        .panel {
            width: 760px;
            border: 1px solid #333;
            padding: 18px 16px 24px;
        }
        h1 {
            font-size: 32px;
            margin: 0 0 20px;
        }
        .message {
            font-size: 20px;
            margin: 18px 0 28px;
        }
    </style>
</head>
<body>
    <div class="panel">
        <h1>Ohh! Some error(s).</h1>
        <div class="message">
            <%= (exception != null && exception.getMessage() != null) ? exception.getMessage() : "Invalid input." %>
        </div>
        <a href="searchEvents.jsp">Please try again!!!</a>
    </div>
</body>
</html>
