<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Update meal</title>
    <link rel="stylesheet" href="../../css/main.css">
</head>
<body>
<jsp:useBean id="user" type="ru.mikehalko.kbju.model.user.User" scope="request"/>
<h3><a class="href" href="../../index.html">Главная</a> | <a class="href" href="${pageContext.request.contextPath}/user">Профиль</a> | <a class="href_opened">Править</a></h3>
<hr>

<section>
    <form method="post" action="">
        <input type="hidden" name="user_id" value="${user.id}">
        <p>Отображаемое имя: <input type="text" size=40 value="${user.name}" name="name" required></p>
        <p>Порог калорий (min):<input type="number" value="${user.caloriesMin}" name="calories_min" required></p>
        <p>Порог калорий (max):<input type="number" value="${user.caloriesMax}" name="calories_max" required></p>
        <button type="submit">Сохранить</button>
        <button onclick="window.history.back()" type="button">Отменить</button>
    </form>
</section>
</body>
</html>