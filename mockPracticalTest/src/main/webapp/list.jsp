<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ page import="java.util.ArrayList, ict.bean.EventBean" %>
<!DOCTYPE html>
<html>
<head>
    <title>EventSphere - Book Your Experience</title>
    <style>
        body {
            font-family: Georgia, "Times New Roman", serif;
            margin: 24px 32px;
        }
        .panel {
            width: 930px;
            min-height: 340px;
            border: 1px solid #333;
            padding: 12px 16px 24px;
        }
        h1 {
            font-size: 34px;
            margin: 0 0 24px;
        }
        .venue-line, .total-line {
            font-size: 20px;
            margin: 18px 0;
        }
        .venue-line {
            margin-top: 10px;
        }
        table {
            border-collapse: collapse;
            margin-top: 12px;
            font-size: 18px;
        }
        th, td {
            border: 1px solid #999;
            padding: 2px 4px;
        }
        .back-link {
            display: inline-block;
            margin-top: 36px;
            font-size: 18px;
        }
    </style>
</head>
<body>
    <div class="panel">
        <h1>EventSphere - Book Your Experience</h1>
        <jsp:useBean id="eventList" class="java.util.ArrayList" scope="request" />
        <%
            ArrayList<EventBean> events = (ArrayList<EventBean>) request.getAttribute("eventList");
            if (events == null) {
                events = new ArrayList<EventBean>();
            }
            int totalSeats = 0;
            String venue = (String) request.getAttribute("venue");
        %>

        <div class="venue-line">
            Venue: <%= venue == null ? "" : venue %>
        </div>

        <table>
            <tr>
                <th>Event Name</th>
                <th>Date</th>
                <th>Price</th>
                <th>Seat</th>
            </tr>
            <%
                for (EventBean event : events) {
                    totalSeats += event.getSeatsAvailable();
            %>
            <tr>
                <td><%= event.getEventName() %></td>
                <td><%= event.getEventDate() %></td>
                <td><%= event.getTicketPrice() %></td>
                <td><%= event.getSeatsAvailable() %></td>
            </tr>
            <%
                }
            %>
        </table>

        <div class="total-line">
            Total seats: <%= totalSeats %>
        </div>

        <a class="back-link" href="searchEvents.jsp">Back to Search</a>
    </div>
</body>
</html>
