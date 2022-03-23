// 게시글 등록
let selFiles = [];

$(function () {
    $("#imageFiles").on('change', handleImageFileSelect);
});

function fileUploadAction() {
    console.log("fileUploadAction");
    $("#imageFiles").trigger('click');
}

function handleImageFileSelect(e) {
    selFiles = [];
    $("#select-img").empty();

    let files = e.target.files;
    let filesArr = Array.prototype.slice.call(files);

    let index = 0;
    filesArr.forEach(function (file) {
        if (!file.type.match("image.*")) {
            alert("이미지만 업로드 가능합니다.");
            file.val("");
            return;
        }

        selFiles.push(file);

        let reader = new FileReader();
        reader.onload = function (e) {
            let html = "<a href='javascript:void(0);' onclick=\"deleteImage("+index+")\" id=\"img_id_"+index+"\"><img src='"+e.target.result+"' data-file='"+file.name+"' width='150px' height='auto'></a>";
            $("#select-img").append(html);
            index++;
        };
        reader.readAsDataURL(file);
    });
}

function checkExt() {
    
}

function deleteImage(idx) {
    console.log("INDEX : " + idx);
    selFiles.splice(idx, 1);

    let img_id = "img_id_" + idx;
    $(img_id).remove();

    console.log(selFiles);
}

// DB 이미지 삭제
function deleteImageDB(event) {
    let thisBtn = $(event);
    let imageId = thisBtn.parent().find(".image-id").val();
    let filePath = thisBtn.parent().find(".file-path").val();

    $.ajax({
        url: "/community/" + imageId + "/imageDelete",
        method: "delete",
        dataType: "json",
        data: {
            imageId: imageId,
            filePath: filePath
        }
    })
        .done(function (data) {
            if (data.key === "success") {
                thisBtn.parent().find('img').remove();
                thisBtn.remove();
            }
        });
}

// 이전 페이지로 이동
function goBack() {
    window.history.back();
}