<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, height=device-height, initial-scale=0">
    <title>Show Meal</title>
    <link rel="stylesheet" href="../../css/site.css">
    <link rel="stylesheet" href="../../css/show.css">
    <link rel="stylesheet" href="../../css/miniprofile.css">
</head>
<body>
<div id="block_top_panel">
    <div id="location_path">
        <a class="link" href="../../index.html">main page</a> >> <a class="link" href="">profile</a>
    </div>
</div>

<div id="main_and_right_panel">
    <div id="block_main">
        <div class="content">

            <jsp:useBean id="user" type="ru.mikehalko.kbju.model.user.User" scope="session"/>

            <h1>Profile $USER_LOGIN</h1>
            <section id="desc">
                <label for="dateTime">Name:</label>
                <a id="dateTime">${user.name}</a>
                <br>
            </section>

            <section id="params">
                <h2>parameters</h2>
                <div>
                    <table>
                        <thead><tr>
                            <th>
                                Minimum Cal.
                            </th>
                            <th>
                                Maximum Cal.
                            </th>
                        <tr></thead>
                        <tbody>
                        <tr>
                            <td>${user.caloriesMin} kk</td>
                            <td>${user.caloriesMax} kk</td>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </section>

            <section id="meal_buttons">
                <a href="${pageContext.request.contextPath}user?action=update"><button>edit</button></a>
            </section>
        </div>
    </div>

    <div id="block_right_panel">
        <section class="mini_profile">
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