let paramName = "action";
let editAction = "update";
let newAction = "create";

$(
    function () {
        setInputZeroToEmpty()

        let action = getUrlParameter(paramName);
        console.log(paramName + " = " + action);

        if (action === editAction) {
            console.log(editAction);
            $(".create").hide();
            $(".edit").show();
        } else if (action === newAction) {
            console.log(newAction);
            $(".edit").hide();
            $(".create").show();
        } else {
            console.log("no one");
        }
        console.log("script end");
    }
);



function getUrlParameter(sParam) {
    let sPageURL = decodeURIComponent(window.location.search.substring(1)),
        sURLVariables = sPageURL.split('&'),
        sParameterName,
        i;

    for (i = 0; i < sURLVariables.length; i++) {
        sParameterName = sURLVariables[i].split('=');

        if (sParameterName[0] === sParam) {
            return sParameterName[1] === undefined ? true : sParameterName[1];
        }
    }
}

function setInputZeroToEmpty() {
    $('input').each(function () {
        let someInput = $(this);
        if (someInput.prop('id') !== "meal_id" && someInput.context.valueAsNumber === 0) {
            someInput.context.valueAsNumber = NaN;
        }
    });
}
