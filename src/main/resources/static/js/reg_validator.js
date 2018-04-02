$(document).ready(function () {
    $("#reg_username").change(function () {
        var username = $(this).val();
        $.post("/register/validation/username", {
            "username": username
        }, function (data, status) {
            alert("got response " + status.toString())
            var error = $("#username-error");
            if (data) {
                error.text("Username has already been used");
                error.addClass("text-danger");
            } else {
                error.text("Available");
                error.addClass("text-success");
            }
        });
    });
});