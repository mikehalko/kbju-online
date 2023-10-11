<%@ page import="ru.mikehalko.kbju.util.DateTimeUtil" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>KBJU | Your Meals</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/site.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/other.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/miniprofile.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/meals-table.css">
</head>
<body>

<div id="block_top_panel">
    <div id="location_path">
        <a class="link" href="${pageContext.request.contextPath}/">main page</a> >> <a class="link" href="meals?action=get_all">list of meals</a>
    </div>
</div>

<div id="main_and_right_panel">

    <div id="block_main">
        <div class="content">
            <div class="text">
                <p>
                <p>Здесь список приемов пищи. Добавляйте записи, чтобы отслеживать потребление калорий.
                <a class="about-shortage">Превышение</a> калорий или <a class="about-excess">недостаток</a> (в день) будет отображен с помощью выделения цветов:
                <a class="about-shortage">голубым</a> и <a class="about-excess">красным</a>.</p>
                <!--        <br>-->
                <!--        Сайт существует в качестве пет-проекта. Визуальная часть (frontend) служит только для удобства взаимодействия с веб-приложением. Веб-приложение написано на языке Java, работает в контейнере сервлетов Tomcat, который находится внутри контейнера Docker на выделенном VDS линукс-сервере, и взаимодействует с БД Postgres в отдельном контейнере. Данный пет-проект не использует Spring, Hibernate. Использование данных фреймворков будет задействовано в следующих обновлениях, либо в другом пет-проекте.-->
                <!--        </p>-->
            </div>

            <section class="under_table_panel">
                <a href="meals?action=create"><button>create new</button></a>
            </section>

            <div class="scroll-table">

                <table class="scroll-table-head">
                    <thead>
                    <tr>
                        <th class="date">data</th>
                        <th class="description">description</th>
                        <th class="calories">kcal</th>
                        <th class="button">button</th>
                    </tr>
                    </thead>
                </table>

                <div class="scroll-table-body">
                    <table>
                        <tbody>
                        <c:forEach items="${requestScope.list}" var="meal">
                            <jsp:useBean id="meal" type="ru.mikehalko.kbju.to.MealTo"/>
                            <tr class="${meal.excess ? 'excess' : meal.shortage ? 'shortage' : 'normal'}">
                                <td class="date"><a><%=DateTimeUtil.toString(meal.getDateTime())%></a></td>
                                <td class="description"><a>${meal.description}</a></td>
                                <td class="calories"><a>${meal.calories}</a></td>
                                <td class="button"><a href="?action=get&id=${meal.id}"><button>open</button></a></td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>


        </div>
    </div>


    <div id="block_right_panel">
        <section class="mini_profile">
            <jsp:useBean id="user" type="ru.mikehalko.kbju.model.user.User" scope="session"/>
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
        <a href="https://github.com/mikehalko/kbju-online">github</a>
    </div>
</footer>

</body>
</html>