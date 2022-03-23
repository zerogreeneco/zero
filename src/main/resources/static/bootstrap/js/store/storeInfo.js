$(document).ready(function () {
    // 영업시간
    for (var i = 0; i <= 47; i++) {
        var hour = "";
        var min = ":00";

        if ((Math.ceil(i / 2)) < 13) {
            hour = Math.floor(i / 2);
        } else {
            hour = Math.floor(i / 2);
        }
        if ((Math.ceil(i % 2)) != 0) { <!--나머지 0 또는 1-->
            var min = ":30";
        }

        $(".startTime").append("<option value="
            + ((hour >= 10) ? hour : ('0' + hour)) + min + ">" + ((hour >= 10) ? hour : ("0" + hour)) + min + "</option>");
    }

    for (var i = 47; i >= 0; i--) {
        var hour = "";
        var min = ":00";

        if ((Math.ceil(i / 2)) < 13) {
            hour = Math.floor(i / 2);
        } else {
            hour = Math.floor(i / 2);
        }
        if ((Math.ceil(i % 2)) != 0) { <!--나머지 0 또는 1-->
            var min = ":30";
        }

        $(".closeTime").append("<option value="
            + ((hour >= 10) ? hour : ('0' + hour)) + min + ">" + ((hour >= 10) ? hour : ("0" + hour)) + min + "</option>");
    }
    // 이미지 슬라이더
    $("#img-right").click(function() {
        $(".img-slider").animate({marginLeft: "-150px"
        }, function() {
            $(".img-slider .store-img:first").appendTo(".img-slider");
            $(".img-slider").css({marginLeft: 0});
        });
    });
    $("#img-left").click(function() {
        $(".img-slider .store-img:last").prependTo(".img-slider");
        $(".img-slider").css({ "marginLeft": "-150px"});
        $(".img-slider").animate({ marginLeft: 0 });
    });

    // 업로드 미리보기
    $(".input-box").on('change', selectedImageFile);
}); //end Update

// 글자수 제한
function limitTextInput(event) {
    let countText = $(event).prev().children(".cm-text-count");
    let check=$("#textCheck");
    countText.html($(event).val().length + " / 300");

    if ($(event).val().length > 300) {
        $(event).val($(event).val().substring(0, 300));
        check.html("300자까지 입력 가능합니다");
        check.css("color","#dc3545");
        countText.html("300 / 300");
    }
}

// Social 주소 추가
function addSocial() {
    $("#socialAddress2").removeAttr("hidden");
    $("#socialAdd").attr("hidden", true);
}

// Price 회계단위 추가
function inputPriceFormat(event){
    event.value = comma(uncomma(event.value));
}
function comma(str) {
    str = String(str);
    return str.replace(/(\d)(?=(?:\d{3})+(?!\d))/g, '$1,');
}

function uncomma(str) {
    str = String(str);
    return str.replace(/[^\d]+/g, '');
}


// 테이블 Grade 추가
function menuAdd() {
    let name = $("#menuName").val();
    let price = $("#menuPrice").val();
    let grade = $(":input:radio[name=vegetarianGrade]:checked").val();
    let inputCheck = $("#inputCheck");
    let gradeCheck = $("#gradeCheck");

    if(name == "" || price == ""){
        inputCheck.html("메뉴의 이름과 가격을 입력해 주세요");
        inputCheck.css("color","#dc3545");

        return null;
    } else if(grade == null) {
        gradeCheck.html("해당 메뉴의 비건 등급을 입력해 주세요");
        gradeCheck.css("color", "#dc3545");
    }

    $.ajax({
        url: "/zerogreen/stores/update/gradeTable",
        method: "post",
        contentType: "application/x-www-form-urlencoded; charset=UTF-8",
        data: {
            name: name,
            price: price,
            grade: grade
        }
    })
        .done(function (fragment) {
            $("#grade-table").replaceWith(fragment);
            $("#menuName").val("");
            $("#menuPrice").val("");
            $(":input:radio[name=vegetarianGrade]:checked").prop('checked', false);
            inputCheck.html("");
            gradeCheck.html("");
        });
} //menuAdd ajax end

//테이블 추가
function tableAdd() {
    let name = $("#menuName").val();
    let price = $("#menuPrice").val();
    let inputCheck = $("#inputCheck");

    if(name == "" || price == "") {
        inputCheck.html("메뉴의 이름과 가격을 입력해 주세요");
        inputCheck.css("color", "#dc3545");

        return null;
    }

    $.ajax({
        url: "/zerogreen/stores/update/table",
        method: "post",
        contentType: "application/x-www-form-urlencoded; charset=UTF-8",
        data: {
            name: name,
            price: price
        }
    })
        .done(function (fragment) {
            $("#list-table").replaceWith(fragment);
            $("#menuName").val("");
            $("#menuPrice").val("");
            inputCheck.html("");
        });
} //menuAdd ajax end

// 테이블 Grade 삭제
function gradeDel(event) {
    let tableDel = $(event);
    let id = tableDel.parent().parent().find(".tableId").val();
    $.ajax({
        url: "/zerogreen/stores/update/grade/delete",
        method: "delete",
        data: {
            id: id
        }
    })
        .done(function (fragment) {
            $("#grade-table").replaceWith(fragment);
        });
}

// 테이블 삭제
function tableDel(event) {
    let tableDel = $(event);
    let id = tableDel.parent().parent().find(".tableId").val();
    $.ajax({
        url: "/zerogreen/stores/update/table/delete",
        method: "delete",
        data: {
            id: id
        }
    })
        .done(function (fragment) {
            $("#list-table").replaceWith(fragment);
        });
}


// 이미지 삭제
function imgDel(event) {
    let imgDel = $(event);
    let id = imgDel.parent().find(".imgId").val();
    let filePath = imgDel.parent().find(".filePath").val();
    let thumb = imgDel.parent().find(".thumb").val();
    $.ajax({
        url: "/zerogreen/stores/update/img/delete",
        method: "delete",
        dataType:"json",
        data: {
            id: id,
            filePath:filePath,
            thumb:thumb
        }
    })
        .done(function (data) {
            if (data.key === "success") {
                imgDel.parent().find('img').remove();
                imgDel.remove();
            }
        });
}
// 크기, 확장자 확인
function selectedImageFile(e) {
    selFiles = [];
    $("#selectedImg").empty();

    let files = e.target.files;
    let filesArr = Array.prototype.slice.call(files);
    let totalSize = 0;
    let index = 0;

    let check = $("#imgCheck");

    filesArr.forEach(function (file){
        totalSize = totalSize + file.size;
    });

    filesArr.forEach(function (file) {

        if (!file.type.match("image.*")) {
            check.html("사진을 첨부해주세요");
            check.css("color","#dc3545");

            return;
        }else if(totalSize > 10485760){
            check.html("최대 10MB까지 첨부할 수 있습니다");
            check.css("color","#dc3545");
            $(".input-box").val("");

            return;
        } else{
            selFiles.push(file);
            let reader = new FileReader();

            reader.onload = function (e) {
                let html = "<img class='selectedImg' src='"+e.target.result+"' data-file='"+file.name+"' width='100px' height='100px'>";
                $("#selectedImg").append(html);
                index++;
            };
            reader.readAsDataURL(file);
        }
    });
}
