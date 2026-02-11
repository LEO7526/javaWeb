<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <title>Radio Button</title>
    </head>
    <body>
        <form method="get" action="radio" >
            <br/>How many employees in your company?<br/>
            <br/>1-100<input type="radio" name="employee" value="1-100" />
            <br/>101-200<input type="radio" name="employee" value="101-200" />
            <br/>201-300<input type="radio" name="employee" value="201-300" />
            <br/>301-400<input type="radio" name="employee" value="301-400" />
            <br/>401-more<input type="radio" name="employee" value="401-more" />
            <br/><input type="submit" value="submit">
        </form>
        

    </body>
</html>
