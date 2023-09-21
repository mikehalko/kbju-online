<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="ru">
<head>
    <title>Meal show</title>
    <link rel="stylesheet" href="../../css/main.css">
    <link rel="stylesheet" href="../../css/meals.css">
</head>
<hr>
<h3><a class="href" href="../../index.html">Главная</a> | <a class="href_opened">Профиль</a> | <a class="href" href="../meals">Список приёмов пищи</a></h3>
<hr>
<c:set var="user" value="${requestScope.user}"/>
<jsp:useBean id="user" type="ru.mikehalko.kbju.model.user.User"/>
<section>
    <p>Отображаемое имя: ${user.name}</p>
    <p>Порог калорий (min): <a class="shortages">${user.caloriesMin}</a></p>
    <p>Порог калорий (max): <a class="excess">${user.caloriesMax}</a></p>
    <a href="?action=update&id=${user.id}"><button>Править</button></a>
</section>
</body>
</html>