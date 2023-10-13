<%@ page import="ru.mikehalko.kbju.web.validation.UserValidator" %>
<%@ page import="static ru.mikehalko.kbju.web.constant.attribute.UserCredentialField.*" %>
<%@ page import="static ru.mikehalko.kbju.web.constant.attribute.UserField.*" %>
<%@ page import="static ru.mikehalko.kbju.web.constant.OtherConstant.*" %>
<%@ page import="ru.mikehalko.kbju.web.validation.UserCredentialValidator" %>
<%@ page import="ru.mikehalko.kbju.model.user.User" %>
<%@ page import="ru.mikehalko.kbju.web.util.WebUtil" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, height=device-height, initial-scale=0">
    <title>KBJU | Auth</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/site.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/create.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/user-form.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/user-form-login.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/validation_form.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/validation_form_user_update.css">
    <script type='text/javascript' src='http://ajax.googleapis.com/ajax/libs/jquery/1.9.0/jquery.min.js'></script>
    <script src="${pageContext.request.contextPath}/scripts/auth.js"></script>
    <script src="${pageContext.request.contextPath}/scripts/fail-message.js"></script>
</head>
<body>
<%
    UserValidator userValid = (UserValidator) request.getAttribute(VALIDATOR_USER.value());
    UserCredentialValidator credValid = (UserCredentialValidator) request.getAttribute(VALIDATOR_USER_CREDENTIAL.value());
    boolean userInvalid = userValid != null;
    boolean credentialInvalid = credValid != null;
    boolean formLoginType = (!userInvalid && !credentialInvalid) || (credentialInvalid && !userInvalid);

    String userName = "";
    int calMin = 0;
    int calMax = 0;
    if (userInvalid) {
        User user = (User) WebUtil.getAttribute(request, USER);
        userName = user.getName();
        calMin = user.getCaloriesMin();
        calMax = user.getCaloriesMax();
    }
    String login = "";
    if (credentialInvalid) {
        login = WebUtil.getAttributeString(request, PARAM_LOGIN);
    }
%>

<div id="block_top_panel">
    <div id="location_path">
        <a class="link" href="${pageContext.request.contextPath}/">main page</a> >> <a class="link" href="">auth page</a>
    </div>
</div>



<div id="main_and_right_panel">
    <div id="block_main">
        <div class="content">
            <section class="user_form">
                <form id="user_form" method="post" action="login?action=login">
                    <h3 id="h3_log">User Login Form</h3>
                    <h3 id="h3_reg" style="display: none">User Register Form</h3>

                    <section id="sections">
                        <section class="section">
                            <div class="input_container">
                                <input id="id" class="" type="text" name="<%=PARAM_USER_ID%>" placeholder=" " value="0" hidden="hidden"/>
                                <input id="login" class="<%= credValid == null ? "" : credValid.isValid(PARAM_LOGIN) ? "" : "fail_field"%>" type="text" name="login" placeholder=" " <%= login != null ? "value=\""+ login + "\"" : ""%>/>
                                <label>login</label>
                            </div>
                            <div class="input_container log">
                                <input id="password" class="<%= credValid == null ? "" : credValid.isValid(PARAM_PASSWORD) ? "" : "fail_field"%>" type="password" name="password" placeholder=" "/>
                                <label>password</label>
                            </div>
                            <div class="input_container reg">
                                <input id="password_new" class="<%= credValid == null ? "" : credValid.isValid(PARAM_PASSWORD_NEW) ? "" : "fail_field"%>" type="password" name="password_new" placeholder=" "/>
                                <label>password</label>
                            </div>
                            <div class="input_container reg">
                                <input id="password_repeat" class="<%= credValid == null ? "" : credValid.isValid(PARAM_PASSWORD_REPEAT) ? "" : "fail_field"%>" type="password" name="password_repeat" placeholder=" "/>
                                <label>repeat password</label>
                            </div>
                        </section>

                        <section class="section reg" style="display: none">
                            <div class="input_container">
                                <input id="nickname" class="<%= userValid == null ? "" : userValid.isValid(PARAM_NAME) ? "" : "fail_field"%>" type="text" name="name" placeholder=" " <%= userInvalid ? "value=\""+ userName + "\"" : ""%>/>
                                <label>nickname</label>
                            </div>
                            <div class="input_container">
                                <input id="calories_min" class="<%= userValid == null ? "" : userValid.isValid(PARAM_CALORIES_MIN) ? "" : "fail_field"%>" type="number" name="calories_min" placeholder=" " <%= userInvalid ? "value=\""+ calMin + "\"" : ""%>/>
                                <label>calories min</label>
                            </div>
                            <div class="input_container">
                                <input id="calories_max" class="<%= userValid == null ? "" : userValid.isValid(PARAM_CALORIES_MAX) ? "" : "fail_field"%>" type="number" name="calories_max" placeholder=" " <%= userInvalid ? "value=\""+ calMax + "\"" : ""%>/>
                                <label>calories max</label>
                            </div>
                        </section>

                    </section>
                </form>
                <section id="under_form">
                    <section id="fail_message_section">
                        <div id="fail_area" style="display: none">
                            <% String message_user = userValid == null ? null : userValid.resultMessage();%>
                            <%= message_user != null ? "<p class=\"fail_message_text\">" + message_user + "</p>" : "" %>
                            <% String message_cred = credValid == null ? null : credValid.resultMessage();%>
                            <%= message_cred != null ? "<p class=\"fail_message_text\">" + message_cred + "</p>" : "" %>
                        </div>
                    </section>
                    <section id="buttons">
                        <button form="user_form" class="reg" style="display: none" name="submit" type="submit">register</button>
                        <button form="user_form" class="log" name="submit" type="submit">login</button>
                        <button onclick="location.href = 'index.html'" type="button">cancel</button>
                        <form id="test_form" method="post" action="login?action=login">
                            <input type="text" name="login" value="USER_1" hidden="hidden">
                            <input type="password" name="password" value="111" hidden="hidden">
                            <button form="test_form" type="submit">тест аккаунт</button>
                        </form>
                    </section>
                    <p id="note">
                        <input id="reg_checkbox" type="checkbox" name="checkboxname" <%= formLoginType ? "" : "checked"%>/>
                        <label for="reg_checkbox">SIGN UP</label>
                    </p>
                </section>
            </section>
        </div>
    </div>

</div>

<footer>
    <div>
        <a>defezis 2023</a> |
        <a href="https://github.com/mikehalko/kbju-online"  class="link">code on github</a>
    </div>
</footer>

</body>
</html>
