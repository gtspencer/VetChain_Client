$(".sidebar-option").click(function () {
    $(".sidebar-option.active").removeClass("active");
    $(this).addClass("active");
});