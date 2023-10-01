<%@ page import="ru.mikehalko.kbju.util.web.validation.UserValidation" %>
<%@ page import="static ru.mikehalko.kbju.web.constant.attribute.UserCredentialAttribute.*" %>
<%@ page import="static ru.mikehalko.kbju.web.constant.attribute.UserAttribute.*" %>
<%@ page import="static ru.mikehalko.kbju.web.constant.attribute.OtherAttribute.*" %>
<%@ page import="ru.mikehalko.kbju.util.web.validation.UserCredentialValidation" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, height=device-height, initial-scale=0">
    <title>Some Title</title>
    <link rel="stylesheet" href="../../css/site.css">
    <link rel="stylesheet" href="../../css/create.css">
    <link rel="stylesheet" href="../../css/user-form.css">
    <link rel="stylesheet" href="../../css/user-form-login.css">
    <link rel="stylesheet" href="../../css/validation_form.css">
    <link rel="stylesheet" href="../../css/validation_form_user_update.css">
    <script type='text/javascript' src='http://ajax.googleapis.com/ajax/libs/jquery/1.9.0/jquery.min.js'></script>
    <script src="../../scripts/auth.js"></script>
    <script src="../../scripts/fail-message.js"></script>
</head>
<body>


<div id="block_top_panel">
    <div id="location_path">
        <a class="link" href="../../index.html">main page</a> >> <a class="link" href="">auth page</a>
    </div>
</div>



<div id="main_and_right_panel">
    <div id="block_main">
        <div class="content">
            <section class="user_form">
                <form id="user_form" method="post" action="login?action=login">
                    <h3 id="h3_log">User Login Form</h3>
                    <h3 id="h3_reg" style="display: none">User Register Form</h3>
                    <% UserValidation userValid = (UserValidation) request.getAttribute(VALIDATOR_USER.value());%>
                    <% UserCredentialValidation credValid = (UserCredentialValidation) request.getAttribute(VALIDATOR_USER_CREDENTIAL.value());%>
                    <section id="sections">
                        <section class="section">
                            <div class="input_container">
                                <input id="login" class="<%= credValid == null ? "" : credValid.isValid(PARAM_LOGIN) ? "" : "fail_field"%>" type="text" name="login" placeholder=" " value="USER_1"/>
                                <label>login</label>
                            </div>
                            <div class="input_container log">
                                <input id="password" class="<%= credValid == null ? "" : credValid.isValid(PARAM_PASSWORD) ? "" : "fail_field"%>" type="password" name="password" placeholder=" " value="111"/>
                                <label>password</label>
                            </div>
                            <div class="input_container reg">
                                <input id="password_new" class="<%= credValid == null ? "" : credValid.isValid(PARAM_PASSWORD_NEW) ? "" : "fail_field"%>" type="password" name="password" placeholder=" "/>
                                <label>password</label>
                            </div>
                            <div class="input_container reg">
                                <input id="password_repeat" class="<%= credValid == null ? "" : credValid.isValid(PARAM_PASSWORD_REPEAT) ? "" : "fail_field"%>" type="password" name="password_repeat" placeholder=" "/>
                                <label>repeat password</label>
                            </div>
                        </section>

                        <section class="section reg" style="display: none">
                            <div class="input_container">
                                <input id="nickname" class="<%= userValid == null ? "" : userValid.isValid(PARAM_NAME) ? "" : "fail_field"%>" type="text" name="name" placeholder=" "/>
                                <label>nickname</label>
                            </div>
                            <div class="input_container">
                                <input id="calories_min" class="<%= userValid == null ? "" : userValid.isValid(PARAM_CALORIES_MIN) ? "" : "fail_field"%>" type="number" name="calories_min" placeholder=" "/>
                                <label>calories min</label>
                            </div>
                            <div class="input_container">
                                <input id="calories_max" class="<%= userValid == null ? "" : userValid.isValid(PARAM_CALORIES_MAX) ? "" : "fail_field"%>" type="number" name="calories_max" placeholder=" "/>
                                <label>calories max</label>
                            </div>
                        </section>

                    </section>

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
                            <button class="reg" style="display: none" name="submit" type="submit">register</button>
                            <button class="log" name="submit" type="submit">login</button>
                            <button onclick="window.history.back()" type="button">cancel</button>
                        </section>
                        <p id="note"><input id="reg_checkbox" type="checkbox" name="checkboxname"/>
                            <label for="reg_checkbox">SIGN UP</label></p>
                    </section>

                </form>
            </section>

        </div>
    </div>

</div>

<footer>
    <div>
        <a>defezis 2023</a>
    </div>
</footer>

</body>
</html>
