let marginLeftValueLogin = "27%";
let marginLeftValueRegister = "1.7vw";

let actionRegisterRelativePath = 'login?action=register';
let actionLoginRelativePath = 'login?action=login';

let lockRadioButtonMillis = 800;
let animationMillis = 200;
let timeoutBeforeAnimationAfterMarginMillis = 300;

let checkBoxSelector = "#reg_checkbox";
let userFormSelector = "#user_form";

function tempLock(checkable, millis) {
    checkable.attr('disabled', true);
    setTimeout(function () {
        checkable.attr('disabled', false);
    }, millis);
}

$(function() {
    loginStart($(userFormSelector));

    $(checkBoxSelector).click(function(){
        tempLock($(checkBoxSelector), lockRadioButtonMillis);
        switchForm($(userFormSelector), this, timeoutBeforeAnimationAfterMarginMillis);
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

function loginStart(form) {
    if ($(checkBoxSelector).is(":checked")) {
        console.log("is checked");
        $(".section").css("margin-left", marginLeftValueRegister);
        $("#h3_log").hide();
        $(".log").hide();
        $(".reg").show();
        $("#h3_reg").show();
        form.attr('action', actionRegisterRelativePath);
        console.log("form switched to \"register\"");
    } else if (!$(checkBoxSelector).is(":checked")) {
        console.log("isn't checked");
        $(".section").css("margin-left", marginLeftValueLogin);
        $("#h3_log").show();
        $(".log").show();
        $(".reg").hide();
        $("#h3_reg").hide();
        form.attr('action', actionLoginRelativePath);
        console.log("form switched to \"login\"");
    } else {
        console.log("some bug")
    }
}

function login() {
    $("#h3_reg").hide()
    $("#h3_log").show()
    $(".reg").hide(animationMillis);
    $(".log").show(animationMillis);
}

function register() {
    $("#h3_reg").show();
    $("#h3_log").hide();
    $(".reg").show(animationMillis);
    $(".log").hide(animationMillis);
}

function marginLeft(object, value) {
    object.animate({marginLeft: value});
}