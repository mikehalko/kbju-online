let marginLeftValueLogin = "27%";
let marginLeftValueRegister = "1.7vw";

let actionRegisterRelativePath = 'login?action=register';
let actionLoginRelativePath = 'login?action=login';

let lockRadioButtonMillis = 800;
let animationMillis = 200;
let timeoutBeforeAnimationAfterMarginMillis = 300;

function tempLock(checkable, millis) {
    checkable.attr('disabled', true);
    setTimeout(function () {
        checkable.attr('disabled', false);
    }, millis);
}

$(function() {
    loginStart();
    $("#reg_checkbox").click(function(){
        tempLock($("#reg_checkbox"), lockRadioButtonMillis);
        switchForm($('#user_form'), this, timeoutBeforeAnimationAfterMarginMillis);
    });
});

function switchForm(form, checkable, timeout_milli) {
    if($(checkable).is(":checked")) {
        // register form
        form.attr('action', actionRegisterRelativePath);
        marginLeft($(".section"), marginLeftValueRegister);
        setTimeout(register, timeout_milli)
        console.log("form switched to \"register\"");
    } else {
        // login form
        form.attr('action', actionLoginRelativePath);
        login();
        marginLeft($(".section"), marginLeftValueLogin);
        console.log("form switched to \"login\"");
    }
}

function loginStart() {
    $(".section").css("margin-left", marginLeftValueLogin);
    $("#h3_log").show()
    $(".log").show();
    $(".reg").hide();
    $("#h3_reg").hide()
}

function login() {
    $("#h3_reg").hide()
    $("#h3_log").show()
    $(".reg").hide(animationMillis);
    $(".log").show(animationMillis);
}

function register() {
    $("#h3_reg").show()
    $("#h3_log").hide()
    $(".reg").show(animationMillis);
    $(".log").hide(animationMillis);
}

function marginLeft(object, value) {
    object.animate({marginLeft: value});
}