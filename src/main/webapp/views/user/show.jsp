<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, height=device-height, initial-scale=0">
    <title>KBJU | Profile</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/site.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/other.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/show.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/miniprofile.css">
</head>
<body>
<div id="block_top_panel">
    <div id="location_path">
        <a class="link" href="${pageContext.request.contextPath}/">main page</a> >> <a class="link" href="">profile</a>
    </div>
</div>

<div id="main_and_right_panel">
    <div id="block_main">
        <div class="content">

            <jsp:useBean id="user" type="ru.mikehalko.kbju.model.user.User" scope="session"/>
            <jsp:useBean id="login" class="java.lang.String" scope="session"/>
            <h1>Profile <a class="user_login_class">${login}</a></h1>
            <section id="desc">
                <div class="section_content">
                <label for="name">Name:</label>
                <a id="name">${user.name}</a>
                <br>
                </div>
            </section>

            <section id="params">
                <div class="section_content">
                    <h2>parameters</h2>
                    <div id="parameter_table">
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
                </div>
            </section>

            <section id="meal_buttons">
                <a href="${pageContext.request.contextPath}/user?action=update"><button>edit</button></a>
            </section>
        </div>
    </div>

    <div id="block_right_panel">
        <section class="mini_profile">
            <p id="user_login"><a class="user_login_class">${login}</a></p>
            <p class="profile_info_line"><label for="user_name">name:</label><a id="user_name">${user.name}</a></p>
            <p class="profile_info_line"><label for="user_calories_min">calories (min):</label><a id="user_calories_min">${user.caloriesMin}</a></p>
            <p class="profile_info_line"><label for="user_calories_max">calories (max):</label><a id="user_calories_max">${user.caloriesMax}</a></p>
        </section>
        <menu>
            <li><a class="menu_button" href="meals?action=get_all">meals</a></li>
            <li><a class="menu_button" href="user?action=get">profile</a></li>
            <li><a class="menu_button" href="login?action=out">logout</a></li>
        </menu>
    </div>

</div>

<footer>
    <div>
        <a>defezis 2023</a>
        <a href="https://github.com/mikehalko/kbju-online">github</a>
    </div>
</footer>

</body>
</html>