$(document).ready(function() {
    $("#food-right").click(function() {
        $(".food-slider").animate({
        marginLeft: "-180px"
        }, function() {
            $(".food-slider .card:first").appendTo(".food-slider");
            $(".food-slider").css({
            marginLeft: 0
            });
        });
    });
    $("#food-left").click(function() {
        $(".food-slider .card:last").prependTo(".food-slider");
        $(".food-slider").css({ "marginLeft": "-180px"});
        $(".food-slider").animate({ marginLeft: 0 });
    });

    $("#shop-right").click(function() {
        $(".shop-slider").animate({
        marginLeft: "-180px"
        }, function() {
            $(".shop-slider .card:first").appendTo(".shop-slider");
            $(".shop-slider").css({
            marginLeft: 0
            });
        });
    });
    $("#shop-left").click(function() {
        $(".shop-slider .card:last").prependTo(".shop-slider");
        $(".shop-slider").css({ "marginLeft": "-180px"});
        $(".shop-slider").animate({ marginLeft: 0 });
    });

});