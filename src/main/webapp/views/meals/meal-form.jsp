<%@ page import="ru.mikehalko.kbju.util.DateTimeUtil" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, height=device-height, initial-scale=0">
  <title>Some Title</title>
  <link rel="stylesheet" href="../../css/site.css">
  <link rel="stylesheet" href="../../css/miniprofile.css">
  <link rel="stylesheet" href="../../css/create.css">
  <link rel="stylesheet" href="../../css/meal-form.css">
  <script type='text/javascript' src='http://ajax.googleapis.com/ajax/libs/jquery/1.9.0/jquery.min.js'></script>
  <script src="../../scripts/meal-form.js"></script>
</head>
<body>
<jsp:useBean id="meal" type="ru.mikehalko.kbju.to.MealTo" scope="request"/>

<div id="block_top_panel">
  <div id="location_path">
    <a class="link" href="../../index.html">main page</a> >> <a class="link" href="meals">list of meals</a>
    <a style="display: none" class="create"> >> </a><a href="" class="create link" style="display: none">create new meal</a>
    <a style="display: none" class="edit"> >> </a><a style="display: none" class="edit link" href="?action=get&id=${meal.id}">the meal</a><a style="display: none" class="edit"> >> </a><a href="" class="edit link" style="display: none">edit meal</a>
  </div>
</div>

<div id="main_and_right_panel">
  <div id="block_main">
    <div class="content">
      <section class="meal_form">
        <form id="create_meal" method="post" action="">
          <h3 class="create">Meal Create Form</h3>
          <h3 class="edit">Meal Edit Form</h3>
          <input id="meal_id" type="hidden" name="id" value="${meal.id}">
          <section id="sections">
            <section class="section">
              <div class="input_container">
                <input id="timestamp" type="datetime-local" name="dateTime" value="<%=DateTimeUtil.toString(meal.getDateTime())%>" required autofocus/>
              </div>
              <div class="input_container">
                <textarea id="description" placeholder=" " name="description" maxlength="76" required>${meal.description}</textarea>
                <label>description</label>
              </div>
            </section>

            <section class="section">
              <div class="input_container">
                <input id="proteins" type="number" name="proteins" placeholder=" " value="${meal.proteins}" required/>
                <label>proteins</label>
              </div>
              <div class="input_container">
                <input id="fats" type="number" name="fats" placeholder=" " value="${meal.fats}" required/>
                <label>fats</label>
              </div>
              <div class="input_container">
                <input id="carbohydrates" type="number" name="carbohydrates" placeholder=" " value="${meal.carbohydrates}" required/>
                <label>carbohydrates</label>
              </div>
            </section>

            <section class="section">
              <div class="input_container">
                <input id="mass" type="number" name="mass" placeholder=" " value="${meal.mass}" required/>
                <label>mass</label>
              </div>
              <div class="input_container">
                <input id="calories" type="number" name="calories" placeholder=" " value="${meal.calories}" required/>
                <label>calories*</label>
              </div>
            </section>
          </section>

          <section id="under_form">
            <section id="buttons">
              <button name="submit" type="submit" id="meal-submit">save</button>
              <button onclick="window.history.back()" type="button">cancel</button>
            </section>
            <p id="note">* - enter "0" calories, then the calculation will be made based on the amount of proteins, fats and carbohydrates.</p>
          </section>

        </form>
      </section>
    </div>
  </div>


  <div id="block_right_panel">
    <section class="mini_profile">
      <jsp:useBean id="user" class="ru.mikehalko.kbju.model.user.User" scope="session"/>
      <a id="user_login">USER_LOGIN</a>
      <p class="profile_info_line"><label for="user_name">name:</label><a id="user_name">${user.name}</a></p>
      <p class="profile_info_line"><label for="user_calories_min">calories (min):</label><a id="user_calories_min">${user.caloriesMin}</a></p>
      <p class="profile_info_line"><label for="user_calories_max">calories (max):</label><a id="user_calories_max">${user.caloriesMax}</a></p>
    </section>
    <menu>
      <li><a class="menu_button" href="meals">meals</a></li>
      <li><a class="menu_button" href="user">profile</a></li>
      <li><a class="menu_button" href="login?action=out">logout</a></li>
    </menu>
  </div>

</div>

<footer>
  <div>
    <a>defezis 2023</a>
  </div>
</footer>

</body>
</html>