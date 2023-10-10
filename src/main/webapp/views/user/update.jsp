<%@ page import="ru.mikehalko.kbju.util.web.validation.UserValidator" %>
<%@ page import="static ru.mikehalko.kbju.web.constant.attribute.UserField.*" %>
<%@ page import="static ru.mikehalko.kbju.web.constant.OtherConstant.*" %>
<%@ page import="ru.mikehalko.kbju.util.web.validation.UserCredentialValidator" %>
<%@ page import="static ru.mikehalko.kbju.web.constant.attribute.UserCredentialField.PARAM_PASSWORD_NEW" %>
<%@ page import="static ru.mikehalko.kbju.web.constant.attribute.UserCredentialField.*" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, height=device-height, initial-scale=0">
    <title>KBJU | Edit Profile</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/site.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/other.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/miniprofile.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/create.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/user-form.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/validation_form.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/validation_form_user_update.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/inactive.css">
    <script type='text/javascript' src='http://ajax.googleapis.com/ajax/libs/jquery/1.9.0/jquery.min.js'></script>
    <script src="${pageContext.request.contextPath}/scripts/update-user.js"></script>
    <script src="${pageContext.request.contextPath}/scripts/fail-message.js"></script>
</head>
<body>

<div id="block_top_panel">
    <div id="location_path">
        <a class="link" href="${pageContext.request.contextPath}">main page</a> >> <a class="link" href="">user edit</a>
    </div>
</div>



<div id="main_and_right_panel">
    <div id="block_main">
        <div class="content">

            <section class="user_form">
                <jsp:useBean id="user_edit" type="ru.mikehalko.kbju.model.user.User" scope="request"/>
                <form id="update_user" method="post" action="user?action=update">
                    <h3>User Edit Form</h3>
                    <%
                        UserValidator userValid = (UserValidator) request.getAttribute(VALIDATOR_USER.value());
                        UserCredentialValidator credValid = (UserCredentialValidator) request.getAttribute(VALIDATOR_USER_CREDENTIAL.value());
                        boolean userInvalid = userValid != null;
                        boolean credentialInvalid = credValid != null;
                        String messageUser = !userInvalid ? null : userValid.resultMessage();
                        String messageCredential = !credentialInvalid ? null : credValid.resultMessage();

                        String nicknameClass = userInvalid && userValid.isNoValid(PARAM_NAME) ? "fail_field" : "";
                        String caloriesMinClass = userInvalid && userValid.isNoValid(PARAM_CALORIES_MIN) ? "fail_field" : "";
                        String caloriesMaxClass = userInvalid && userValid.isNoValid(PARAM_CALORIES_MAX) ? "fail_field" : "";

                        String passwordOld = credentialInvalid && credValid.isNoValid(PARAM_PASSWORD_OLD) ? "fail_field" : "";
                        String passwordNew = credentialInvalid && credValid.isNoValid(PARAM_PASSWORD_NEW) ? "fail_field" : "";
                        String passwordRepeat = credentialInvalid && credValid.isNoValid(PARAM_PASSWORD_REPEAT) ? "fail_field" : "";
                    %>
                    <input type="hidden" name="user_id" value="${user_edit.id}">

                    <section id="sections">
                        <section class="section">
                            <div class="input_container">
                                <%--                                TODO "name" значения констант --%>
                                <input id="nickname" class="user_update <%= nicknameClass%>" type="text" name="name" placeholder=" " value="${user_edit.name}" required/>
                                <label class="user_update">nickname</label>
                            </div>
                            <div class="input_container">
                                <input id="calories_min" class="user_update <%= caloriesMinClass%>" type="number" name="calories_min" placeholder=" " value="${user_edit.caloriesMin}"/>
                                <label class="user_update">calories min</label>
                            </div>
                            <div class="input_container">
                                <input id="calories_max" class="user_update <%= caloriesMaxClass%>" type="number" name="calories_max" placeholder=" " value="${user_edit.caloriesMax}"/>
                                <label class="user_update">calories max</label>
                            </div>
                        </section>

                        <section class="section">
                            <div class="input_container">
                                <input id="password-old" class="credential_update <%= passwordOld%>" type="password" name="password_old" placeholder=" "/>
                                <label class="credential_update">old password</label>
                            </div>
                            <div class="input_container">
                                <input id="password-new" class="credential_update <%= passwordNew%>" type="password" name="password_new" placeholder=" "/>
                                <label class="credential_update">new password</label>
                            </div>
                            <div class="input_container">
                                <input id="password-repeat" class="credential_update <%= passwordRepeat%>" type="password" name="password_repeat" placeholder=" "/>
                                <label class="credential_update">repeat password</label>
                            </div>
                        </section>

                    </section>

                    <section id="under_form">
                        <section id="fail_message_section">
                            <div id="fail_area" style="display: none">
                                <%= messageUser != null ? "<p class=\"fail_message_text\">" + messageUser + "</p>" : "" %>
                                <%= messageCredential != null ? "<p class=\"fail_message_text\">" + messageCredential + "</p>" : "" %>
                            </div>
                        </section>
                        <section id="buttons">
                            <button name="submit" type="submit">save</button>
                            <button onclick="location.href = 'user?action=get'" type="button">cancel</button>
                            <input id="switch_checkbox" type="checkbox" name="switch_checkbox" <%= credentialInvalid ? "checked" : ""%>/>
                            <label for="switch_checkbox">change password (only)</label>
                        </section>
                    </section>
                </form>
            </section>

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
            <li><a class="menu_button" href="meals?action=get-all">meals</a></li>
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