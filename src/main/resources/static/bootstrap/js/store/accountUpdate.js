$(document).ready(function () {
    let duplication = false;

    // 연락처 중복 확인
    $("#phoneCheck").off().on("click", function () {
        let phoneNumber = $("#phoneNumber").val();
        let error = $(".phoneNumber-error");

        if (phoneNumber === "" || phoneNumber === "undefined") {
            error.html("전화번호를 입력해 주세요");
            error.css("color", "#dc3545")
        } else {
            $.ajax({
                url: "/zerogreen/members/phoneNumber",
                method: "post",
                dataType: "json",
                data: {
                    phoneNumber: phoneNumber
                }
            })
                .done(function (data) {
                    if (data.result === 1) {
                        error.html("이미 존재하는 연락처입니다");
                        error.css("color", "#dc3545");
                    } else {
                        duplication = true;
                        error.html("사용 가능한 연락처입니다");
                        error.css("color", "blue");
                    }
                });
        }
    });

// 회원정보 수정 버튼
    $("#btnUpdate").on("click", function () {
        if (duplication === true) {
            $("#storeAccountUpdate").submit();
        } else {
            $(".phoneNumber-error").html("중복 검사를 실시해 주세요");
            $(".phoneNumber-error").css("color", "#dc3545");
        }
    });
});

//비밀번호 변경
function pwdChange() {
    let password = $("#storePassword");
    let newPassword = $("#newPassword");
    let checkNewPassword = $("#checkNewPassword");
    let error = $(".pwd-error");
    let re_error = $(".rePwd-error");
    let check = $(".check-error");
    console.log(password.val());
    if (password.val() === "" || newPassword.val() === "" || checkNewPassword.val() === "") {
        check.html("비밀번호를 입력해 주세요");
        check.css("color", "#dc3545");
        return false;
    } else if (password.val() === newPassword.val()) {
        re_error.html("현재 비밀번호로 변경할 수 없어요");
        re_error.css("color", "#dc3545");
        return false;
    } else if (!passwordRegCheck(newPassword.val())) {
        re_error.html("숫자/대,소문자/특수기호를 포함한<br/>8~12자리로 변경해 주세요");
        re_error.css("color", "#dc3545");
        return false;
    }

    if (newPassword.val() !== checkNewPassword.val()) {
        re_error.html("");
        check.html("비밀번호가 일치하지 않아요");
        check.css("color", "#dc3545");
        checkNewPassword.val("");
        checkNewPassword.focus();
    } else {
        $.ajax({
            url: "/zerogreen/members/account/pwdChange",
            type: "patch",
            data: {
                password: password.val(),
                newPassword: newPassword.val()
            },
            dataType: "json"
        })
            .done(function (data) {
                if (data.result === "success") {
                    alert("비밀번호가 변경되었어요\n다시 로그인해 주세요");
                    location.replace("/zerogreen/login");
                } else if (data.result === "fail") {
                    re_error.html("");
                    check.html("");
                    error.html("비밀번호가 일치하지 않아요");
                    error.css("color", "#dc3545");
                    password.val("");
                    password.focus();
                }
            });
    }
}

// 비밀번호 정규식 확인 (숫자, 소문자, 대문자, 특수문자 1개 이상 8~12자리)
function passwordRegCheck(password) {
    let regExp = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[$@$!%*?&])[A-Za-z\d$@$!%*?&]{8,12}/;
    return regExp.test(password);
}

//회원 탈퇴
function storeMemberDel() {
    let password = $("#password").val();
    let error = $(".del-error");
    if (password === "") {
        error.html("비밀번호를 입력해 주세요");
        error.css("color", "#dc3545");
        error.css("margin-bottom", "0.5rem");
    } else{
        $.ajax({
            url: "/zerogreen/members/account/deleteMember",
            type: "delete",
            dataType: "json",
            data: {
                password: password
            }
        }).done(function (data) {
            if (data.result === "success") {
                location.replace("/zerogreen");
            } else if (data.result === "fail") {
                error.html("비밀번호가 일치하지 않아요");
                error.css("color", "#dc3545");
                error.css("margin-bottom", "0.5rem");
            }
        });
    }
}