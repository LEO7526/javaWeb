<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<html>
<head><title>Login</title></head>
<body>
  <form method="post" action="main">
    <input type="hidden" name="action" value="authenticate"/>
    <p>Login name is "abc" and password is "123"</p>
    Username: <input type="text" name="username"/><br/>
    Password: <input type="password" name="password"/><br/>
    <input type="submit" value="Login"/>
  </form>
</body>
</html>
