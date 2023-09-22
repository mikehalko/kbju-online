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
    <script type='text/javascript' src='http://ajax.googleapis.com/ajax/libs/jquery/1.9.0/jquery.min.js'></script>
    <script src="../../scripts/auth.js"></script>
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

                    <section id="sections">
                        <section class="section">
                            <div class="input_container">
                                <input id="login" type="text" name="login" placeholder=" " value="USER_1"/>
                                <label>login</label>
                            </div>
                            <div class="input_container">
                                <input id="password" type="password" name="password" placeholder=" " value="1"/>
                                <label>password</label>
                            </div>
                            <div class="input_container reg">
                                <input id="password-repeat" type="password" name="password_repeat" placeholder=" "/>
                                <label>repeat password</label>
                            </div>
                        </section>

                        <section class="section reg" style="display: none">
                            <div class="input_container">
                                <input id="nickname" type="text" name="name" placeholder=" "/>
                                <label>nickname</label>
                            </div>
                            <div class="input_container">
                                <input id="calories_min" type="number" name="calories_min" placeholder=" "/>
                                <label>calories min</label>
                            </div>
                            <div class="input_container">
                                <input id="calories_max" type="number" name="calories_max" placeholder=" "/>
                                <label>calories max</label>
                            </div>
                        </section>

                    </section>

                    <section id="under_form">
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
