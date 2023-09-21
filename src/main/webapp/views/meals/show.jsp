<%@ page import="ru.mikehalko.kbju.util.DateTimeUtil" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="ru">
<head>
    <title>Meal show</title>
    <link rel="stylesheet" href="../../css/main.css">
</head>
<hr>
<h3><a class="href" href="../../index.html">Главная</a> | <a class="href" href="../meals">Список приёмов пищи</a> | <a class="href_opened">Приём пищи</a></h3>
<hr>
<c:set var="meal" value="${requestScope.meal}"/>
<jsp:useBean id="meal" type="ru.mikehalko.kbju.to.MealTo"/>
<section>
    <p>Дата и время: <%=DateTimeUtil.toString(meal.getDateTime())%></p>
    <p>Описание: ${meal.description}</p>
    <p>Масса: ${meal.mass}</p>
    <p>Белки: ${meal.proteins}</p>
    <p>Жиры: ${meal.fats}</p>
    <p>Углеводы: ${meal.carbohydrates}</p>
    <p>Калории: ${meal.calories}</p>
    <a href="?action=update&id=${meal.id}"><button>Править</button></a>
    <a href="?action=delete&id=${meal.id}"><button>Удалить</button></a>
</section>
</body>
</html>