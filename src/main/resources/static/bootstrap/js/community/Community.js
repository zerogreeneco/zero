// 커뮤니티 상세보기 JS
$(function () {

    getReplyData();

    let boardId = $("#id").val();
    let actionForm = $("form");

    /* 좋아요 버튼 */
    $("#like-btn").click(function () {

        $.ajax({
            url: "/community/like/" + boardId,
            method: "post",
            dataType: "json",
            data: {
                boardId: boardId
            }
        }).done(function (data) {
            if (data.count === 1) {
                $(".like-img").attr("src", "/zerogreen/bootstrap/images/like/disLike.png")
                $(".test-count").text(data.totalCount);
            } else if (data.count === 0) {
                $(".like-img").attr("src", "/zerogreen/bootstrap/images/like/like.png")
                $(".test-count").text(data.totalCount);
            }
        });
    }); // like-btn end.

    /* 댓글 전송 */
    $("#review-send-btn").click(function () {
        let text = $("#text").val();

        let item = sessionStorage.getItem('LoginUser');
        console.log(item);
        $.ajax({
            url: "/community/" + boardId + "/reply",
            method: "post",
            dataType: "json",
            contentType: "application/x-www-form-urlencoded; charset=UTF-8",
            data: {
                boardId: boardId,
                text: text
            }
        }).done(function (data) {
            // $("#review-table").replaceWith(fragment);
            // getReplyData(boardId, table);
            if (data.result === "success") {
                alert("댓글이 등록되었습니다.");
                $("#text").val("");
                $("#text-count").text("0 / 100");
                // $("#review-table").html("");
                getReplyData();
                return false;
            }
        });
    }); // review send end.

    // 게시물 삭제
    $("#delete-board").click(function () {
        actionForm
            .attr("action", "/community/" + boardId + "/delete")
            .attr("method", "post");
        if (confirm("정말 삭제하시겠습니까?") === true) {
            actionForm.submit();
        } else {
            return;
        }

    });

    $("#text").on("keyup", function () {
        let text = $(this);
        limitTextInput(text);
    });


}); // onload end.

/* 댓글 글자 수 제한 */
function limitTextInput(event) {
    let countText = $(event).prev().children(".cm-text-count");
    countText.html($(event).val().length + " / 100");

    if ($(event).val().length > 100) {
        $(event).val($(event).val().substring(0, 100));
        alert("100자까지 입력이 가능합니다.");
        countText.html("100 / 100");
    }
}

let boardId = $("#id").val();

function textareaResize(obj) {
    obj.style.height = "1px";
    obj.style.height = (12 + obj.scrollHeight) + "px";
}

function getReplyData() {
    let $id = $("#id").val();
    let $username = $("#login-user").val();

    $.getJSON("/community/api/replyList/" + $id, function (list) {
        let html = "";

        console.log(list.data);
        $.each(list.data, function (key, value) {
            html += "<div>";
            if (value.depth === 1) {
                html += "<div class='comment-wrapper'>";
            } else if (value.depth === 2) {
                html += "<div class='comment-wrapper' style='padding-left: 30px;'>";
            }
            html += "<div class='nest-list'>";
            html += "<input type='hidden' class='replyId' value='" + value.replyId + "'>";
            html += "<span>";
            html += "<img src='/bootstrap/images/profile/" + value.vegetarianGrade + ".png' width='15px' height='15px'>";
            html += "</span>";
            html += "<span>" + value.nickname + "</span>";
            html += "<span style='font-size: 0.8em; float: right; color: #969696; margin-right: 4px;'>" + value.createdTime + "</span>";
            html += "<div id='text-count-wrapper'>";
            html += "<span class='cm-text-count' style='display: none; float:right; margin-bottom: 5px;'>0 / 100</span>";
            html += "</div>";
            html += "<p class='rp-text' style='margin-bottom: 10px;'>" + value.text + "</p>";
            html += "<a class='modify-test-btn' style='display: none;' onclick='modifyReply(this)'>수정!</a>";
            html += "<a class='cancel-test-btn' style='display: none;' onclick='modifyCancel(this)'>취소</a>";
            html += "<div class='reply-btn-wrapper' style='margin-bottom: 60px;'>";
            if (value.depth === 1) {
                html += "<a class='nested-reply-btn' onclick='nestedReplyInput(this)'>답글</a>";
            }
            if (value.username === $username) {
                html += "<a class='rp-cancel-btn' onclick='deleteReply(" + value.replyId + ")'>삭제</a>";
                html += "<a class='rp-modify-btn' onclick='replaceTag(this)'>수정</a>";
            }
            html += "</div>";
            html += "<div class='nested-reply-wrapper' style='display: none;'>";
            html += "<textarea class='nested-reply-input' onkeyup='limitTextInput(this)'></textarea>";
            html += "<div style='display: inline-block; width: 15%;'>";
            html += "<span class='cm-text-count' style='display: block; margin-bottom: 10px;'>0 / 100</span>";
            html += "<a onclick='nestedReplySend(this)'>등록</a>";
            html += "<a>취소</a>";
            html += "</div>";
            html += "</div>";
            html += "</div>";
            html += "</div>";
            html += "</div>";
        })
        $("#review-table").html(html);
    });
} // getReplyData end

/* 댓글 수정용 태그 변경 */
function replaceTag(event) {
    let thisBtn = $(event);
    let parent = thisBtn.parent().parent();
    let rpText = parent.find(".rp-text:first");
    let text = rpText.text();

    rpText.replaceWith("<textarea class='modify-test' style='margin-bottom: 5px;' name='text' onkeyup='limitTextInput(this)'>" + text + "</textarea>");
    parent.find(".modify-test-btn:first").show();
    parent.find(".cancel-test-btn:first").show();
    parent.find(".cm-text-count").show();
    thisBtn.parent().find(".rp-cancel-btn").hide();
    thisBtn.parent().find(".nested-reply-btn").hide();
    thisBtn.hide();
}
// 댓글 수정 취소 버튼
function modifyCancel(event) {
    let thisBtn = $(event);
    let parent = thisBtn.parent().parent();
    let $textArea = parent.find(".modify-test");
    let text = $textArea.text();

    $textArea.replaceWith("<p class='rp-text' style='margin-bottom: 10px;'>" + text + "</p>");
    parent.find(".modify-test-btn:first").show();
    parent.find(".cm-text-count").hide();
    parent.find(".modify-test-btn").hide();
    thisBtn.parent().find(".rp-cancel-btn").show();
    thisBtn.parent().find(".rp-modify-btn").show();
    thisBtn.parent().find(".nested-reply-btn").show();
    thisBtn.hide();
}

function nestedReplyInput(event) {
    let inputAdd = $(event);
    inputAdd.parent().parent().find(".nested-reply-wrapper").show();
}

/* 댓글 수정 */
function modifyReply(event) {
    let modifyBtn = $(event);
    let replyId = modifyBtn.parent().find(".replyId").val();
    let text = modifyBtn.parent().find(".modify-test");
    let data = {
        boardId: boardId,
        replyId: replyId,
        text: text.val()
    }

    $.ajax({
        url: "/community/replyModify/" + replyId,
        method: "put",
        dataType: "json",
        contentType: 'application/json; charset=utf-8',
        data: JSON.stringify(data)
    })
        .done(function (data) {
            if (data.result === "success") {
                alert("수정되었습니다.");
                text.replaceWith("<p class='rp-text'>" + text.val() + "</p>")
                // $(".rp-text:eq(1)").remove();
                $(".replyId").next(".modify-test").hide();
                $(".replyId").next().next().children(".cm-text-count").hide();
                modifyBtn.hide();
                modifyBtn.prevAll("#text-count-wrapper").children().hide()
                modifyBtn.next().children(".rp-modify-btn").show();
                modifyBtn.parent().children(".reply-btn-wrapper").children(".rp-cancel-btn").show();
                modifyBtn.parent().children(".reply-btn-wrapper").children(".nested-reply-btn").show();
            } else {
                alert("실패");
            }
        })
}

// 대댓글
function nestedReplySend(event) {
    let boardId = $("#id").val();
    let replyBtn = $(event);
    let replyId = replyBtn.parents(".comment-wrapper").find(".replyId").val();
    let text = replyBtn.parent().parent().children(".nested-reply-input").val();

    let data = {
        boardId: boardId,
        replyId: replyId,
        text: text
    }
    console.log(text);
    $.ajax({
        url: "/community/" + boardId + "/" + replyId + "/nestedReply",
        method: "post",
        dataType: 'json',
        contentType: 'application/json; charset=utf-8',
        data: JSON.stringify(data),
    }).done(function (data) {
        if (data.result === "success") {
            alert("대댓글");
            getReplyData();
        }
    });
    return false;
}

/* 댓글 삭제 */
function deleteReply(replyId) {

    let boardId = $("#id").val();
    let data = {
        replyId: replyId,
        boardId: boardId
    }
    if (confirm("댓글을 삭제하시겠습니까?") === true) {
        $.ajax({
            url: "/community/" + replyId + "/delete",
            method: "delete",
            dataType: "json",
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify(data),
        })
            .done(function (data) {
                if (data.result === "success") {
                    $("#review-table").html("");
                    getReplyData();
                }
            });
    } else {
        return;
    }
}

// 커뮤니티 메인 JS
$(function () {
    let page = 1;

    // 더보기
    $("#more-list").click(function () {
        ++page;
        let keyword = getParameterByName("keyword");
        let category = getParameterByName("category");
        let searchType = getParameterByName("searchType");

        $.ajax({
            url: "/community",
            method: "post",
            data: {
                page: page,
                category: category,
                keyword: keyword,
                searchType: searchType
            }
        })
            .done(function (fragment) {
                $("#more-wrapper").append(fragment);
            })
            .fail(function (error) {
                alert(error);
            });
    });

    // 좋아요
    $(".main-like-btn").click(function (e) {

        let likeBtn = $(this);
        let boardId = likeBtn.closest("#list-wrapper").children(".list-board-id").val();

        $.ajax({
            url: "/community/like/" + boardId,
            method: "post",
            dataType: "json",
            data: {
                boardId: boardId
            }
        })
            .done(function (data) {
                if (data.count === 1) {
                    likeBtn.closest(".like-count").find(".like-img").attr("src", "/zerogreen/bootstrap/images/like/disLike.png")
                    likeBtn.closest(".like-count").children(".test-count").text(data.totalCount);
                } else if (data.count === 0) {
                    likeBtn.closest(".like-count").find(".like-img").attr("src", "/zerogreen/bootstrap/images/like/like.png")
                    likeBtn.closest(".like-count").children(".test-count").text(data.totalCount);
                }
            });
    }); // like-btn end.

    // 페이지 상단으로 이동
    $("#top-btn").click(function () {
        $('html, body').animate({scrollTop: 0}, '400');
    });

    // 카테고리 태그 색상 변경
    if (window.location.href == "http://localhost:8080/zerogreen/community") {
        $(".category-list li:first").css('background', '#16a085');
        $(".category-list a:first").css('color', '#fff');
    }
    if (window.location.href.indexOf("QNA") > -1) {
        $(".category-list li").eq(1).css('background', '#16a085');
        $(".category-list a").eq(1).css('color', '#fff');
    }
    if (window.location.href.indexOf("VEGAN") > -1) {
        $(".category-list li").eq(2).css('background', '#16a085');
        $(".category-list a").eq(2).css('color', '#fff');
    }
    if (window.location.href.indexOf("NEWS") > -1) {
        $(".category-list li").eq(3).css('background', '#16a085');
        $(".category-list a").eq(3).css('color', '#fff');
    }
    if (window.location.href.indexOf("PLOGGING") > -1) {
        $(".category-list li").eq(4).css('background', '#16a085');
        $(".category-list a").eq(4).css('color', '#fff');
    }
    if (window.location.href.indexOf("PLOVING") > -1) {
        $(".category-list li").eq(5).css('background', '#16a085');
        $(".category-list a").eq(5).css('color', '#fff');
    }

    // 게시물이 없을 경우
    if ($("div").hasClass("list-wrapper")) {
        $(".list-none").css('display', 'none');
        $("#more-list").css('display', 'block');
    } else {
        $(".list-none").css('display', 'block');
    }
});

// parameter 값 불러오기
function getParameterByName(name) {
    name = name.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
    let regex = new RegExp("[\\?&]" + name + "=([^&#]*)"), results = regex.exec(location.search);
    return results == null ? "" : decodeURIComponent(results[1].replace(/\+/g, " "));
}