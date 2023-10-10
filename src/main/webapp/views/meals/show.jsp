<%@ page import="ru.mikehalko.kbju.util.DateTimeUtil" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>KBJU | Meal</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, height=device-height, initial-scale=0">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/site.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/other.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/show.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/miniprofile.css">
</head>
<body>
<div id="block_top_panel">
    <div id="location_path">
        <a class="link" href="${pageContext.request.contextPath}/">main page</a> >> <a class="link" href="meals?action=get_all">list of meals</a> >> <a class="link" href="">meal</a>
    </div>
</div>

<jsp:useBean id="meal" type="ru.mikehalko.kbju.to.MealTo" scope="request"/>

<div id="main_and_right_panel">
    <div id="block_main">
        <div class="content">

            <h1>The Meal № ${meal.id}</h1>
            <section id="desc">
                <label for="dateTime">Timestamp:</label>
                <a id="dateTime"><%=DateTimeUtil.toString(meal.getDateTime())%></a>
                <br>
                <label for="description">Description:</label>
                <a id="description">${meal.description}</a>
                <br>
            </section>

            <section id="params">
                <h2>parameters</h2>
                <div>
                    <table>
                        <thead><tr>
                            <th>
                                Mass
                            </th>
                            <th>
                                Calories
                            </th>
                        <tr></thead>
                        <tbody>
                        <tr>
                            <td>${meal.mass} g</td>
                            <td>${meal.calories} kk</td>
                        </tr>
                        </tbody>
                    </table>

                    <table>
                        <thead><tr>
                            <th>
                                Proteins
                            </th>
                            <th>
                                Fats
                            </th>
                            <th>
                                Carbohydrates
                            </th>
                        <tr></thead>
                        <tbody>
                        <tr>
                            <td>${meal.proteins}</td>
                            <td>${meal.fats}</td>
                            <td>${meal.carbohydrates}</td>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </section>

            <section id="meal_buttons">
                <a href="?action=update&id=${meal.id}"><button>edit</button></a>
                <a href="?action=delete&id=${meal.id}"><button>delete</button></a>
            </section>
        </div>
    </div>

    <div id="block_right_panel">
        <section class="mini_profile">
            <jsp:useBean id="user" class="ru.mikehalko.kbju.model.user.User" scope="session"/>
            <jsp:useBean id="login" class="java.lang.String" scope="session"/>
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
    </div>
</footer>

</body>
</html>