<%@ page import="ru.mikehalko.kbju.util.web.validation.UserValidation" %>
<%@ page import="ru.mikehalko.kbju.web.constant.UserParams" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, height=device-height, initial-scale=0">
    <title>Update meal</title>
    <link rel="stylesheet" href="/css/site.css">
    <link rel="stylesheet" href="/css/miniprofile.css">
    <link rel="stylesheet" href="/css/create.css">
    <link rel="stylesheet" href="/css/user-form.css">
    <link rel="stylesheet" href="/css/validation_form.css">
</head>
<body>

<div id="block_top_panel">
    <div id="location_path">
        <a class="link" href="../../index.html">main page</a> >> <a class="link" href="">user edit</a>
    </div>
</div>



<div id="main_and_right_panel">
    <div id="block_main">
        <div class="content">

            <section class="user_form">
                <jsp:useBean id="user_edit" type="ru.mikehalko.kbju.model.user.User" scope="request"/>
                <form id="create_user" method="post" action="">
                    <h3>User Edit Form</h3>
                    <% UserValidation valid = (UserValidation) request.getAttribute("validator");%>
                    <input type="hidden" name="user_id" value="${user_edit.id}">

                    <section id="sections">
                        <section class="section">
                            <div class="input_container">
                                <input id="nickname" class="<%= valid == null ? "" : valid.isValid(UserParams.PARAM_NAME) ? "" : "fail_field"%>" type="text" name="name" placeholder=" " value="${user_edit.name}" required/>
                                <label>nickname</label>
                            </div>
                            <div class="input_container">
                                <input id="calories_min" class="<%= valid == null ? "" : valid.isValid(UserParams.PARAM_CALORIES_MIN) ? "" : "fail_field"%>" type="number" name="calories_min" placeholder=" " value="${user_edit.caloriesMin}"/>
                                <label>calories min</label>
                            </div>
                            <div class="input_container">
                                <input id="calories_max" class="<%= valid == null ? "" : valid.isValid(UserParams.PARAM_CALORIES_MAX) ? "" : "fail_field"%>" type="number" name="calories_max" placeholder=" " value="${user_edit.caloriesMax}"/>
                                <label>calories max</label>
                            </div>
                        </section>

<%--                        TODO не работает    --%>
                        <section class="section">
                            <div class="input_container">
                                <input id="password-old" type="password" name="password" placeholder=" "/>
                                <label>old password</label>
                            </div>
                            <div class="input_container">
                                <input id="password-new" type="password" name="password_new" placeholder=" "/>
                                <label>new password</label>
                            </div>
                            <div class="input_container">
                                <input id="password-repeat" type="password" name="password_repeat" placeholder=" "/>
                                <label>repeat password</label>
                            </div>
                        </section>

                    </section>

                    <section id="under_form">
                        <section id="buttons">
                            <button name="submit" type="submit">save</button>
                            <button onclick="window.history.back()" type="button">cancel</button> <!-- // TODO если ошибка - вернет не туда -->
                        </section>
                        <% String message = valid != null ? valid.resultMessage() : null;%>
                        <%= message != null ? "<p class=\"fail_message\">" + message + "</p>" : "" %>
                    </section>
                </form>
            </section>

        </div>
    </div>


    <div id="block_right_panel">
        <section class="mini_profile">
            <jsp:useBean id="user" type="ru.mikehalko.kbju.model.user.User" scope="session"/>
            <a id="user_login">USER_LOGIN</a>
            <p class="profile_info_line"><label for="user_name">name:</label><a id="user_name">${user.name}</a></p>
            <p class="profile_info_line"><label for="user_calories_min">calories (min):</label><a id="user_calories_min">${user.caloriesMin}</a></p>
            <p class="profile_info_line"><label for="user_calories_max">calories (max):</label><a id="user_calories_max">${user.caloriesMax}</a></p>
        </section>
        <menu>
            <li><a class="menu_button" href="meals">meals</a></li>
            <li><a class="menu_button" href="user?action=get">profile</a></li>
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