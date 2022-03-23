$(function () {

    let id = $("#username");
    let password = $("#password");
    let rePassword = $("#re-password")
    let idCheck = false;
    let check1 = false;
    let check2 = false;
    let authKey = "";
    // 이메일 확인
    $("#send-mail").off().on("click", function () {

        if (id === "") {
            alert("메일 주소를 입력해주세요.");
        } else if (!emailRegCheck(id.val())) {
            alert("이메일 양식이 아닙니다.");
        } else {
            $.ajax({
                url: "/members/checkMail",
                type: "post",
                dataType: "json",
                contentType: "application/x-www-form-urlencoded; charset=UTF-8",
                data: {
                    mail: id.val(),
                },
                success: function (key) {
                    if (key.fail) {
                        alert(key.fail);
                        id.focus();
                    } else {
                        alert("인증 번호가 전송되었습니다.");
                        authKey = key.key;
                        $(".auth-box").show();
                        $(".email-msg").text("인증번호를 입력해주세요.");
                        $(".email-msg").css({"color": "blue", "margin-bottom": "10px"});
                    }
                },
                error: function (xhr, status, error) {
                    alert("Error" + status + ">>>> " + error);
                }
            }); // ajax end.
        }
    });

    // 이메일 인증
    $('#authConfirm').off().on("click", function () {
        let $key = $(".authKey");
        let $auth = $(".auth-msg");

        if ($key.val() === authKey) {
            $auth.text("이메일이 인증되었습니다.");
            $auth.css("color", "blue");
            idCheck = true;
            if (idCheck === true) {
                $("#joinBtn").removeAttr("disabled");
                $("#joinBtn").css("background", "#f1c40f");
                $key.attr("disabled", true);
                id.attr("readonly", true);
                // disabled -> Form 전송 불가
                // readOnly -> Form 전송 가능
            }
        } else {
            $auth.text("인증번호를 다시 확인해주세요.");
            $auth.css("color", "red");
        }
    });

    // 닉네임 중복 확인
    $("#nickname-check").off().on("click", function () {
        let nickname = $("#nickname");

        if (nickname.val() === "" || nickname.val() === "undefined") {
            alert("닉네임을 입력해주세요.");
        } else {
            $.ajax({
                url: "/members/nickname",
                method: "post",
                dataType: "json",
                data: {
                    nickname: nickname.val()
                }
            })
                .done(function (data) {
                    let $nickname = $(".nickname-error");
                    if (data.result === 1) {
                        $nickname.text("이미 존재하는 닉네임입니다.");
                        $nickname.css("color", "red");
                        nickname.focus();
                    } else {
                        check1 = true;
                        $nickname.text("사용 가능한 닉네임입니다.");
                        $nickname.css("color", "blue");
                    }
                });
        }
    });

    $("#attachFile").click(function () {

        console.log(attachFile);
    });
    let attachFile = $("#attachFile").val();
    if (attachFile !== null) {
        console.log(attachFile);
    }

    // 연락처 중복 확인
    $("#phoneNumber-check").off().on("click", function () {
        let phoneNumber = $("#phoneNumber").val();

        if (phoneNumber === "" || phoneNumber === "undefined") {
            alert("연락처를 입력해주세요.");
        } else {
            $.ajax({
                url: "/members/phoneNumber",
                method: "post",
                dataType: "json",
                data: {
                    phoneNumber: phoneNumber
                }
            })
                .done(function (data) {
                    let $phoneNumber = $(".phoneNumber-error");
                    if (data.result === 1) {
                        $phoneNumber.text("이미 존재하는 연락처입니다.");
                        $phoneNumber.css("color", "red");
                    } else {
                        check2 = true;
                        $phoneNumber.text("사용 가능한 연락처입니다.");
                        $phoneNumber.css("color", "blue");
                    }
                });
        }
    });

    // 사업자등록번호 중복 확인
    $("#storeRegNum-check").off().on("click", function () {
        let storeRegNum = $("#storeRegNum").val();

        if (storeRegNum === "" || storeRegNum === "undefined") {
            alert("사업자 등록 번호를 입력해주세요.");
        } else {
            $.ajax({
                url: "/members/storeRegNum",
                method: "post",
                dataType: "json",
                data: {
                    storeRegNum: storeRegNum
                }
            })
                .done(function (data) {
                    let $storeRegNum = $(".regNum-error");
                    if (data.result === 1) {
                        $storeRegNum.text("이미 존재하는 업체입니다.");
                        $storeRegNum.css("color", "red");
                    } else {
                        check1 = true;
                        $storeRegNum.text("중복되는 사업자가 없습니다.");
                        $storeRegNum.css("color", "blue");
                    }
                });
        }
    });
    /*
    * 유효성 검사 (Blur)
    * */
    password.blur(function () {

        let $password = $(".password-error");
        if (!passwordRegCheck(password.val())) {
            $password.text("숫자/대,소문자/특수기호를 포함한 8~12자리");
            $password.css("color", "red");
            this.focus();
        } else {
            $password.text("사용 가능한 비밀번호입니다.");
            $password.css("color", "blue");
        }
    });

    // 비밀번호 중복 확인
    rePassword.blur(function () {
        let $re = $(".re-password-error");
        if (password.val() !== rePassword.val()) {
            $re.text("비밀번호가 일치하지 않습니다.");
            $re.css("color", "red");
            this.focus();
        } else {
            $re.text("비밀번호가 일치합니다.");
            $re.css("color", "blue");
        }
    });

    // 회원가입 버튼
    $("#joinBtn").on("click", function () {
        if (check2 === true && check1 === true
            && (password.val() === rePassword.val())) {
            $("#reg-form").submit();
        }
    });
});

// 이메일 정규식 확인
function emailRegCheck(email) {
    let regExp =
        /^([\w\.\_\-])*[a-zA-Z0-9]+([\w\.\_\-])*([a-zA-Z0-9])+([\w\.\_\-])+@([a-zA-Z0-9]+\.)+[a-zA-Z0-9]{2,8}$/;
    return (email !== "" && email !== "undefined" && regExp.test(email));
}

// 비밀번호 정규식 확인 (숫자, 소문자, 대문자, 특수문자 1개 이상 8~12자리)
function passwordRegCheck(password) {
    let regExp = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[$@$!%*?&])[A-Za-z\d$@$!%*?&]{8,12}/;
    return regExp.test(password);
}
