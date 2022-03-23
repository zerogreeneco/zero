$(function () {
    $("#find-id-btn").click(function () {
        // $(".modal").fadeIn();
        var a = $("#phoneNumber").val();
        alert(a);
    });

   $(".modal-content").click(function () {
       $(".modal").fadeOut();
   });
});