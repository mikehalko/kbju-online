<%@ page import="ru.mikehalko.kbju.util.DateTimeUtil" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="ru">
<head>
    <title>Meals</title>
    <link rel="stylesheet" href="../../css/main.css">
    <link rel="stylesheet" href="../../css/meals.css">
</head>
<body>
<hr>
<h3><a class="href" href="../../index.html">Главная</a> | <a class="href_opened">Список приёмов пищи</a></h3>
<hr>
<section class="profile">
    <c:set var="user" value="${requestScope.user}"/>
    <jsp:useBean id="user" type="ru.mikehalko.kbju.model.user.User"/>
    <p>
        ИМЯ ПОЛЬЗОВАТЕЛЯ:
        <a>${user.name}</a>
    </p>
    <p>
        НОРМА КАЛОРИЙ:
        <a class="shortages">min: ${user.caloriesMin}</a>
        <a class="excess">max: ${user.caloriesMax}</a>
    </p>
    <a href="${pageContext.request.contextPath}/user"><button class="button">Профиль</button></a> |
    <a href="${pageContext.request.contextPath}/login?action=out"><button>Выйти из аккаунта</button></a>
</section>
<hr>
<div><a href="../meals?action=create"><button>Новый приём пищи</button></a></div>
<br>
<section class="meal_table">
    <table border="1" cellpadding="8" cellspacing="0">
        <thead>
        <tr>
            <th>ДАТА</th>
            <th>ОПИСАНИЕ</th>
            <th>КАЛОРИИ</th>
            <th>-</th>
        </tr>
        </thead>

        <c:forEach items="${requestScope.list}" var="meal">
            <jsp:useBean id="meal" type="ru.mikehalko.kbju.to.MealTo"/>
            <tr class="${meal.excess ? 'excess' : meal.shortage ? 'shortages' : 'normal'}">
                <td><%=DateTimeUtil.toString(meal.getDateTime())%></td>
                <td>${meal.description}</td>
                <td>${meal.calories}</td>
                <td><a href="?action=get&id=${meal.id}"><button class="button">открыть</button></a></td>
            </tr>
        </c:forEach>
    </table>

</section>

</body>
</html>