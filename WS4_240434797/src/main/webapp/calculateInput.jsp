<!DOCTYPE html>
<html>
<head>
    <title>Simple Calculator using JSP action</title>
    <style>
        h2 span {
            color: red;
        }
    </style>
</head>
<body>
    <h2>Simple Calculator <span>using jsp action</span></h2>
    
    <form action="calculate.jsp" method="post">
        <label for="firstNumber">First Number:</label>
        <input type="text" id="firstNumber" name="firstNumber"><br/><br/>
        
        <label for="secondNumber">Second Number:</label>
        <input type="text" id="secondNumber" name="secondNumber"><br/><br/>
        
        <label>Operator:</label>
        <input type="radio" name="operator" value="add"> add
        <input type="radio" name="operator" value="subtract"> subtract
        <input type="radio" name="operator" value="multiply"> multiply
        <br/><br/>
        
        <input type="submit" value="submit">
        <input type="reset" value="reset">
    </form>
</body>
</html>
