$(document).ready(function(e){
    let contextPath = $('#contextPathHolder').attr('data-contextPath') ? $('#contextPathHolder').attr('data-contextPath') : '';
    let cnt = $(".review-cnt");
    let sno = $(".js-storeId").text();
    let count = 0;

    //** 페이징 작업중 **
    function paging(totalData, dataPerPage, pageCount, currentPage) {
        console.log("currentPage : " + currentPage);

        totalPage = Math.ceil(totalData / dataPerPage); //총 페이지 수

        if(totalPage<pageCount){
            pageCount=totalPage;
        }

        let pageGroup = Math.ceil(currentPage / pageCount); // 페이지 그룹
        let last = pageGroup * pageCount; //화면에 보여질 마지막 페이지 번호

        if (last > totalPage) {
            last = totalPage;
        }

        let first = last - (pageCount - 1); //화면에 보여질 첫번째 페이지 번호
        let next = last + 1;
        let prev = first - 1;

        let pageHtml = "";

        if (prev > 0) {
            pageHtml += "<li><a href='#' id='prev'> 이전 </a></li>";
        }

        //페이징 번호 표시
        for (var i = first; i <= last; i++) {
        if (currentPage == i) {
              pageHtml +=
                "<li class='on'><a href='#' id='" + i + "'>" + i + "</a></li>";
        } else {
              pageHtml += "<li><a href='#' id='" + i + "'>" + i + "</a></li>";
            }
        }

        if (last < totalPage) {
            pageHtml += "<li><a href='#' id='next'> 다음 </a></li>";
        }

        $("#pagingul").html(pageHtml);
        let displayCount = "";
        displayCount = "현재 1 - " + totalPage + " 페이지 / " + totalData + "건";
        $("#displayCount").text(displayCount);


        //페이징 번호 클릭 이벤트
        $("#pagingul li a").click(function () {
        let $id = $(this).attr("id");
        selectedPage = $(this).text();

        if ($id == "next") selectedPage = next;
        if ($id == "prev") selectedPage = prev;

        //전역변수에 선택한 페이지 번호를 담는다...
        globalCurrentPage = selectedPage;
        //페이징 표시 재호출
        paging(totalData, dataPerPage, pageCount, selectedPage);
        //글 목록 표시 재호출
        displayData(selectedPage, dataPerPage);
        });
    }

/*        여기까지               */


    //리뷰 이미지 등록시 div14 css 재설정 ** on working **
   $('.div14').on('change',function (e) {
       let img = $(this).parent().find('.div12').find('.rv-img').val();
       let div13 = $(this).parent('.div13');

        if (img != null) {
            $(this).css('marginLeft','130px');
            div13.css('min-height','8em');
        }
   });
   $('.div14').change();

/*
    function divChange(event) {
       let img = $(event).parent().find('.div12').find('.rv-img').val();
       console.log(">>>>>>>>> "+img);
       let div13 = $(event).parent('.div13');

        if (img != null) {
            $(event).css('marginLeft','130px');
            div13.css('min-height','8em');
        }
    }
*/

    //show 대댓글 입력박스
    $(".srv-toAdd").on("click", function(){
        let inputBox = $(this).parent().parent().children(".srv-input");
        let toAdd = $(this).parent().children(".srv-toAdd");

        inputBox.show();
        toAdd.attr('style',"display:none;");
    });


    // 대댓글 추가
   $(".srv-adding").click(function () {
        let parent = $(this).parent().parent()
        let reviewText = parent.children().children("#storeReviewText");
        let sno2 = parent.children().children(".js-sno").text();
        let rno = parent.parent().children(".rno").text();
        let btn = parent.parent().children().children(".srv-toAdd");
        console.log("dd " + sno2)

        $.ajax({
            url: contextPath+"/page/detail/addReview/"+sno2+"/"+rno,
            method: "post",
            data: {
                sno: sno2,
                rno: rno,
                reviewText: reviewText.val()
            },
        })
            .done(function (fragment) {
//                $("#reviewList").replaceWith(fragment);
                self.location.reload();

            })
            //btn.attr('style',"display:none;");
    }); //end save comment


    //edit member reviews
   $(".mrv-modify").on("click", function(){
        let rno = $(this).parent().parent().children(".rno").text();
        let editText = $(this).parent().parent().children().children().children(".mrv-textarea");
        let deleteBtn = $(this).parent().children(".rv-delete");
        let textCnt = editText.next(".hidden-text-count");
        count++;

       if (count == 1) {
            //console.log("count");
            editText.removeAttr('readonly');
            editText.css('border','solid 1px #3498db');
            editText.css('cursor','text');
            deleteBtn.css('display','none');
            textCnt.removeAttr('style','display:none;');
//            textCnt.css('display','show');
            editText.focus();

       } else if (count == 2) {
           //console.log("countcount");

            $.ajax({
            url: contextPath + "/editReview/"+rno ,
            type:"PUT",
            contentType:"application/json; charset=utf-8",
            dataType:"json",
            data: JSON.stringify({
                 rno: rno,
                 reviewText: editText.val()
            })
            })

            .done(function (fragment) {
                editText.replaceWith("<textarea class='mrv-textarea' name='reviewText' readonly>" + editText.val() + "</textarea>");
                textCnt.css('display','none');
                deleteBtn.removeAttr('style','display:none;');
//                deleteBtn.css('display','show');
            });
            count = 0;
        } //end else if
   });// end edit member + store Reviews


    // edit store Reviews
   $(".srv-modify").on("click", function(){
        let rno = $(this).parent().parent().children(".rno").text();
        let editText = $(this).parent().parent().children(".storeReviewText");
        let deleteBtn = $(this).parent().children(".rv-delete");
        let textCnt = $(this).parent().parent().children(".hidden-text-count");
        count++;

       if (count == 1) {
            editText.removeAttr('readonly');
            editText.css('border','solid 1px #3498db');
            editText.css('cursor','text');
            deleteBtn.css('display','none');
            textCnt.removeAttr('style');
            editText.focus();

       } else if (count == 2) {

            $.ajax({
            url: contextPath + "/editReview/"+rno ,
            type:"PUT",
            contentType:"application/json; charset=utf-8",
            dataType:"json",
            data: JSON.stringify({
                 rno: rno,
                 reviewText: editText.val()
            })
            })

            .done(function (fragment) {
                editText.replaceWith("<textarea class='storeReviewText' name='storeReviewText' readonly>" + editText.val() + "</textarea>");
                textCnt.css('display','none');
                deleteBtn.removeAttr('style');
            });
            count = 0;
        } //end else if
   });// end edit member + store Reviews


    //delete member review
   $(".mrv-delete").on("click", function(){
       let rno = $(this).parent().parent().children(".rno").text();

       $.ajax({
            url: "/deleteReview/" + sno + "/" + rno ,
            method:"DELETE",
            contentType:"application/json; charset=utf-8",
//            dataType:"json",
            data: JSON.stringify({
                rno: rno
            })
            })
            .done(function (fragment) {
//                $(this).parent().parent().parent(".div17").html("");
//                $("#reviewList").replaceWith(fragment);
                $(".review-cnt").html(Number(cnt.text())-1);
                self.location.reload();
            });
   }); //delete member Review end


    //delete store review
   $(".srv-delete").on("click", function(){
       let srno = $(this).parent().parent().children(".srno").text();

       $.ajax({
            url: contextPath + "/deleteReview/" + sno + "/" + srno ,
            method:"DELETE",
            contentType:"application/json; charset=utf-8",
            data: JSON.stringify({
                rno: srno
            })
            })
            .done(function (fragment) {
                self.location.reload();
                //$("#reviewList").replaceWith(fragment);
            });
   }); //delete store Review end


    //textarea 자동 늘이기
   $('textarea').on('keyup',function (e) {
        $(this).css('height', 'auto');
        $(this).height(this.scrollHeight + 10);
   });
   $('textarea').keyup();


    //리뷰이미지 슬라이드
    $("#image-right").click(function() {
        $(".image-slider").animate({marginLeft: "-130px"},
        function() {
            $(".image-slider .card:first").appendTo(".image-slider");
            $(".image-slider").css({marginLeft: 0 });
        });
    });
    $("#image-left").click(function() {
        $(".image-slider .card:last").prependTo(".image-slider");
        $(".image-slider").css({ "marginLeft": "-130px"});
        $(".image-slider").animate({ marginLeft: 0 });
    });


    //탑 버튼
    $("#top-btn").click(function () {
        $('html, body').animate({scrollTop: 0}, '400');
    });


    //이미지 모달
    let modalImage = $(".modal_content");
    let imageList = $(".rv-imageOnly");
    let mImage = $(".mImage-fr");

    //이미지(list) 모달 띄우기 + 모달 슬라이드 ** on working **
    $(".rv-img-list").click(function () {
        let i = $(this).index();
        let imgSrc = $(this).find(".rv-imageOnlyOrigin").attr("src");
        let length = $(".rv-img-list").length;

        $(".modal_content").attr("src", imgSrc);
        $(".modal1").show();

        //모달 슬라이드
        $("#modal-right").click(function() {
            if (i == length-1) {
                i = length-1;
            } else {
                i++;
                let nextImg = $(".rv-img-list").eq(i).find(".rv-imageOnlyOrigin").attr("src");
                $(".modal_content").attr("src", nextImg);
//                console.log("1++ " + i)
            }
        }); //end click modal right

        $("#modal-left").click(function() {
            if (i == 0) {
                i = 0;
            } else {
                i--;
                let prevImg = $(".rv-img-list").eq(i).find(".rv-imageOnlyOrigin").attr("src");
                $(".modal_content").attr("src", prevImg);
//                console.log("1-- " + i)
            }
        }); //end click modal left
    }); //end 이미지 리스트 모달 띄우기 + 모달 슬라이드


    //이미지(withReview) 모달 띄우기 + 모달 슬라이드 ** on working **
    $(".rv-img").click(function () {
        let i2 = $(this).index();
        let imgSrc2 = $(this).find(".mImageOrigin").attr("src");
        let length2 = $(".rv-img").length;

        $(".modal_content").attr("src", imgSrc2);
        $(".modal2").show();

//        console.log("12?? " + i2)
//        console.log("l22 " + length2);

        //모달 슬라이드
        $("#modal-right2").click(function() {
            if (i2 == 0) {
                i2 = 0;
            } else {
                i2--;
                let nextImg2 = $(".rv-img").eq(i2).find(".mImageOrigin").attr("src");
                $(".modal_content").attr("src", nextImg2);
//                console.log("12-- " + i2)
            }
        }); //end click modal right

        $("#modal-left2").click(function() {
            if (i2 == length2-1) {
                i2 = length2-1;
            } else {
                i2++;
                let prevImg2 = $(".rv-img").eq(i2).find(".mImageOrigin").attr("src");
                $(".modal_content").attr("src", prevImg2);
//                console.log("12++ " + i2)
            }
        }); //end click modal left
    }); //end 이미지 (review) 모달 띄우기 + 모달 슬라이드


    //이미지 모달 닫기 (공통)
    $(".close").click(function () {
        //console.log("closeeeeee");
        $(".modal").hide();
    });


    // Preview for review Images
    $("input[type='file']").change(function(e){
          //div 내용 비워주기
          $('#preview').empty();

          let files = e.target.files;
          let arr = Array.prototype.slice.call(files);

          //업로드 가능 파일인지 체크
          for(i=0; i < files.length; i++){
            if(!checkExtension(files[i].name,files.length)){
            return false;
            }
          }

          preview(arr);
    });//file change


    //프리뷰 삭제 ** on working **
/*
    $("#preview").on("click", "ul button", function() {
        console.log("clickyyyyyyyyyyyy");

        let previewImg = $(this).parent().parent("div");
        let target = $(e.target);
        let data = $(this).attr("value");
//        let idx = $(this).attr("data-idx");
        console.log(previewImg);
        console.log(target);
        console.log(data);
//        console.log(idx);

        previewImg.remove();
        previewImg.empty();

    });
*/


    //리뷰 유효성 검사 ** 작업중 **
/*
    $("#rv-btn").on("click", function () {
    console.log("btn ");
        let review = $("#reviewText");
        let message = $(".errorMessage");
        if (review.val() === "") {
            message.text("내용을 입력해주세요");
        } else {
            $("#replyForm").submit();
        }
    });
*/


}); //end script



//업로드 가능한 파일크기, 파일
function checkExtension(fileName, fileLength){

    let maxLength = 5;
    let regex = new RegExp("(.*?)\.(jpg|png|jpeg|JPG|PNG|JPEG)$");

    if (fileLength > maxLength) {
        alert('최대 5개까지 등록 가능합니다.');
        return false;
    }

    //파일 확장자 유효성 검사
    if(!regex.test(fileName)){
        alert('jpg, jpeg, png 파일만 등록 가능합니다.');
        $("input[type='file']").val("");  //파일 초기화
        return false;
    }
    return true;
}


// Preview for review Images
function preview(arr){
    arr.forEach(function(f){

        //div에 이미지 추가
        let str = '<div style="display: inline-flex; padding: 10px;" class="previewImg"><ul class="preview-ul">';

        //이미지 파일 미리보기
        if(f.type.match('image.*')){
            let reader = new FileReader(); //파일을 읽기 위한 FileReader객체 생성

            reader.onload = function (e) { //파일 읽어들이기를 성공했을때 호출되는 이벤트 핸들러

//                str += '<button type="button" class="previewDel" value="'+f.name+'" style="background: gray">X</button><br>';
                str += '<img src="'+e.target.result+'" width=120>';
                str += '</ul></div>';

                $(str).appendTo('#preview');
            }

            reader.readAsDataURL(f);
            }
    });//arr.forEach
} //end function(preview)


// 글자 수 제한 (왜 온로드 밖에 있어야하지..?)
function limitTextInput(event) {
    let countText = $(event).parent().children().children(".text-count");
    countText.html($(event).val().length + " / 500");

    if ($(event).val().length > 500) {
        $(event).val($(event).val().substring(0, 500));
        countText.html("500 / 500");
    }
}


//리뷰 이미지 로컬 삭제 (왜 온로드 밖에 있어야하지..?)
function deleteImage(event) {

    let parentChildren = $(event).parent().parent().find(".div12").find(".imgInfo");

    let id = parentChildren.children(".imgId").val();
    let rno = parentChildren.children(".rnoRno").val();
    let length = parentChildren.length;

    if (id != null) {

        for (let i = 0; i < parentChildren.length; i++) {
            let filePath = parentChildren.eq(i).children(".imgPath").val();
            let thumbnailName = parentChildren.eq(i).children(".imgThumbPath").val();
            let createdDate = parentChildren.eq(i).children(".imgCreatedDate").val();

            (function(i) {

                $.ajax({
                    url: "/"+ id + "/imageDelete",
                    type: "DELETE",
                    contentType:"application/json; charset=utf-8",
                    dataType: "json",
                    async: false,
                    data: JSON.stringify({
                        filePath: filePath,
                        thumbnailName: thumbnailName,
                        createdDate: createdDate
                    })
                })
                .done(function (data) {
                    if (data.key === "success") {
                        //alert("이미지가 삭제되었습니다.");
                    }
                });
           })(i); //end function(i)
       }//end for statement
    } //end if statement
} //end of function deleteImage