<%@ page import="ru.mikehalko.kbju.util.DateTimeUtil" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="ru">
<head>
    <title>Meals</title>
    <style>
        .normal {
            color: green;
        }

        .excess {
            color: red;
        }
    </style>
</head>
<body>
<hr>
<h3><a href="../index.html">Главная</a> | Список приёмов пищи</h3>
<hr>
<section>
    <c:set var="user" value="${requestScope.user}"/>
    <jsp:useBean id="user" type="ru.mikehalko.kbju.model.User"/>
    <nobr>ИМЯ ПОЛЬЗОВАТЕЛЯ: <p style="font-size: large; color: deepskyblue">${user.name}</p></nobr>
    <nobr>НОРМА КАЛОРИЙ: <p style="font-size: large; color: red">${user.nutritionallyNorm.calories}</p></nobr>
    <h4><a href="../meals?action=create"><button>Новый приём пищи</button></a></h4>
</section>
<hr>
<section>
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
            <tr class="${meal.excess ? 'excess' : 'normal'}">
                <td><%=DateTimeUtil.toString(meal.getDateTime())%></td>
                <td>${meal.description}</td>
                <td>${meal.calories}</td>
                <td><a href="meals?action=get&id=${meal.id}"><button>открыть</button></a></td>
            </tr>
        </c:forEach>
    </table>

</section>

</body>
</html>