$(document).ready(function(e){

    //리뷰 슬라이드
    $("#review-right").click(function() {
        $(".review-slider").animate({marginLeft: "-180px"},
        function() {
            $(".review-slider .card:first").appendTo(".review-slider");
            $(".review-slider").css({marginLeft: 0});
        });
    });
    $("#review-left").click(function() {
        $(".review-slider .card:last").prependTo(".review-slider");
        $(".review-slider").css({ "marginLeft": "-180px"});
        $(".review-slider").animate({ marginLeft: 0 });
    });

    //좋아요 슬라이드
    $("#like-right").click(function() {
        $(".like-slider").animate({marginLeft: "-180px"},
        function() {
            $(".like-slider .card:first").appendTo(".like-slider");
            $(".like-slider").css({marginLeft: 0});
        });
    });
    $("#like-left").click(function() {
        $(".like-slider .card:last").prependTo(".review-slider");
        $(".like-slider").css({ "marginLeft": "-180px"});
        $(".like-slider").animate({ marginLeft: 0 });
    });
}); //end script

