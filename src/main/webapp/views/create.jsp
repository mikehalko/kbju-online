<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Create meal</title>
</head>
<body>
<h3><a href="../index.html">Главная</a> | <a href="../meals">Список приёмов пищи</a> | Внести новую запись приёма пищи</h3>

<section>
  <hr>
  <form method="post" action="meals">
    <input type="hidden" name="id">
    <dl>
      <dt>Дата и время:</dt>
      <dd><input type="datetime-local" name="dateTime" required></dd>
    </dl>
    <dl>
      <dt>Описание:</dt>
      <dd><input type="text" size=40 name="description" required></dd>
    </dl>
    <dl>
      <dt>Масса:</dt>
      <dd><input type="number" name="mass" required></dd>
    </dl>
    <dl>
      <dt>Белки:</dt>
      <dd><input type="number" name="proteins" required></dd>
    </dl>
    <dl>
      <dt>Жиры:</dt>
      <dd><input type="number" name="fats" required></dd>
    </dl>
    <dl>
      <dt>Углеводы:</dt>
      <dd><input type="number" name="carbohydrates" required></dd>
    </dl>
    <dl>
      <dt>Калории:</dt>
      <dd><input type="number" name="calories" value="0"></dd>
      <dt>"0" - подсчитает на основе БЖУ</dt>
    </dl>
    <button type="submit">Сохранить</button>
    <button onclick="window.history.back()" type="button">Отменить</button>
  </form>
</section>
</body>
</html>
