$(document).ready(function(e){
    let contextPath = $('#contextPathHolder').attr('data-contextPath') ? $('#contextPathHolder').attr('data-contextPath') : '';
    let sno = $(".js-storeId").text();
    let liking = $(".liking");
    let cnt = $(".like-cnt");

    //스토어 이미지 슬라이드
    $("#image-right-top").click(function() {
        $(".image-slider-top").animate({marginLeft: "-200px"},
        function() {
            $(".image-slider-top .card:first").appendTo(".image-slider-top");
            $(".image-slider-top").css({marginLeft: 0 });
        });
    });
    $("#image-left-top").click(function() {
        $(".image-slider-top .card:last").prependTo(".image-slider-top");
        $(".image-slider-top").css({ "marginLeft": "-200px"});
        $(".image-slider-top").animate({ marginLeft: 0 });
    });


    //좋아요 추가+취소
    $(".liking").on("click", function(){
        $.ajax({
           url: contextPath+'/detailLikes/'+sno,
           method: "POST",
           dataType: "json",
           data: {
                sno: sno
           }
        })

        .done(function (data) {
                //값을 똑바로 맞춰주자 ^.ㅠ
            if (data.memberCnt === 1) {
                $(".liking").attr("src", contextPath + "/bootstrap/images/like/disLike.png");
                //$(".countLikes").text(data.totalCount);
                $(".like-cnt").html(Number(cnt.text())-1);

            } else if (data.memberCnt === 0) {
                $(".liking").attr("src", "/zerogreen/bootstrap/images/like/like.png");
                //$(".countLikes").text(data.totalCount);
                $(".like-cnt").html(Number(cnt.text())+1);
            }
        }) //end of done
    }); //end of function


}); //end script
