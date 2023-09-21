<%--
  Created by IntelliJ IDEA.
  User: arti8
  Date: 05.09.2023
  Time: 22:04
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>

<head>
    <title>Title</title>
    <link rel="stylesheet" href="css/main.css">
</head>
<body>

<div>
    <label for="log_form" class="label_form">Login Form</label>
    <form id="log_form" class="form" action="login?action=login" method="post">
        <label for="name_input">login: </label>
        <input id="name_input" type="text" name="name" value="USER_1"/>
            <br/><br/>
        <label for="password_input">password: </label>
        <input id="password_input" type="password" name="password" value="1"/>
            <br/><br/>
      <input class="button" type="submit" value="login"/>
    </form>
</div>

<hr>

<div>
    <label for="reg_form" class="label_form">Registration Form</label>
    <form id="reg_form" class="form" action="login?action=register" method="post">
        <label for="name_reg_input">login: </label>
        <input id="name_reg_input" type="text" name="name"/>
            <br/><br/>
        <label for="password_reg_input">password: </label>
        <input id="password_reg_input" type="password" name="password"/>
            <br/><br/>
        <input class="button" type="submit" value="register"/>
    </form>
</div>
</body>
</html>
