let formSelector = '#update_user';
let checkBoxSelector = '#switch_checkbox';
let credentialSelector = '.credential_update';
let userSelector = '.user_update';
let credentialClass = 'credential_update';
let userClass = 'user_update';

let actionUserUpdate = 'user?action=update';
let actionUserCredentialUpdate = 'login?action=update';

let classInactive = "inactive";

$(function() {
    switchForm($(formSelector), checkBoxSelector);
    $(checkBoxSelector).click(function(){
        switchForm($(formSelector), this);
    });
});

function switchForm(form, checkable) {
    if($(checkable).is(":checked")) {
        // credential form
        form.attr('action', actionUserCredentialUpdate);
        credential();
        console.log("form switched to \"credential update\"");
    } else {
        // login form
        form.attr('action', actionUserUpdate);
        user();
        console.log("form switched to \"user update\"");
    }
}

function user() {
    disableByClass(credentialClass, true);
    addClassForAllBySelector(credentialSelector, classInactive);
    disableByClass(userClass, false);
    removeClassForAllBySelector(userSelector, classInactive);
}

function credential() {
    disableByClass(userClass, true);
    addClassForAllBySelector(userSelector, classInactive);
    disableByClass(credentialClass, false);
    removeClassForAllBySelector(credentialSelector, classInactive);
}

function disableByClass(classname, isDisable) {
    const cells = document.getElementsByClassName(classname);
    for (let i = 0; i < cells.length; i++) {
        cells[i].disabled = isDisable;
    }
}

function addClassForAllBySelector(selector, addClass) {
    const elements = document.querySelectorAll(selector);
    for (let i = 0; i < elements.length; i++) {
        elements[i].classList.add(addClass);
    }
}

function removeClassForAllBySelector(selector, removeClass) {
    const elements = document.querySelectorAll(selector);
    for (let i = 0; i < elements.length; i++) {
        elements[i].classList.remove(removeClass);
    }
}