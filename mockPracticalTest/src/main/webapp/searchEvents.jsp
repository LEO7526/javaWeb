<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>EventSphere - Search Events</title>
    <style>
        body {
            font-family: Georgia, "Times New Roman", serif;
            margin: 24px 32px;
        }
        .panel {
            width: 900px;
            min-height: 260px;
            border: 1px solid #333;
            padding: 18px 16px 24px;
        }
        h1 {
            font-size: 30px;
            margin: 0 0 26px;
        }
        .row {
            margin-bottom: 18px;
            font-size: 20px;
        }
        label {
            display: inline-block;
            min-width: 150px;
        }
        select, input[type="text"] {
            font-size: 18px;
        }
        .button-row {
            margin-top: 28px;
        }
    </style>
</head>
<body>
    <div class="panel">
        <h1>Find Your Event</h1>

        <form action="searchEventServlet" method="get">

            <div class="row">
                <label for="venue">Select Venue:</label>
                <select id="venue" name="venue">
                    <option value="">-- Select Venue --</option>
                    <option value="Convention Center">Convention Center</option>
                    <option value="Harbour Park">Harbour Park</option>
                    <option value="City Gallery">City Gallery</option>
                </select>
            </div>

            <div class="button-row">
                <input type="submit" value="FIND EVENTS" />
            </div>
        </form>
    </div>
</body>
</html>
