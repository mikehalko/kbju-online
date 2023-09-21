<%@ page import="ru.mikehalko.kbju.util.DateTimeUtil" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Update meal</title>
  <link rel="stylesheet" href="../../css/main.css">
</head>
<body>
<jsp:useBean id="meal" type="ru.mikehalko.kbju.to.MealTo" scope="request"/>
<h3><a class="href" href="../../index.html">Главная</a> | <a class="href" href="../meals">Список приёмов пищи</a> | <a class="href" href="?action=get&id=${meal.id}">Приём пищи</a> | <a class="href_opened">Править</a></h3>
<hr>

<section>
  <%--    `meal.new` cause javax.el.ELException - bug tomcat --%>
  <form method="post" action="">
    <input type="hidden" name="id" value="${meal.id}">
      <p>Дата и время:<input type="datetime-local" value="<%=DateTimeUtil.toString(meal.getDateTime())%>" name="dateTime" required></p>
      <p>Описание: <input type="text" size=40 value="${meal.description}" name="description" required></p>
      <p>Масса:<input type="number" value="${meal.mass}" name="mass" required></p>
      <p>Белки:<input type="number" value="${meal.proteins}" name="proteins" required></p>
      <p>Жиры:<input type="number" value="${meal.fats}" name="fats" required></p>
      <p>Углеводы:<input type="number" value="${meal.carbohydrates}" name="carbohydrates" required></p>
      <p>Калории:<input type="number" value="${meal.calories}" name="calories"></p>
      <p>* "0" - подсчитает на основе БЖУ</p>
    <button type="submit">Сохранить</button>
    <button onclick="window.history.back()" type="button">Отменить</button>
  </form>
</section>
</body>
</html>
