$(function (event) {
    // 회원 정보 변경
    $("#info-submit").click(function () {

        const nickname = $("#nickname").val();
        const phoneNum = $("#phoneNumber").val();
        const vegetarianGrade = $("#vegetarianGrade").val();

        let data = {
            nickname: nickname,
            phoneNumber: phoneNum,
            vegetarianGrade: vegetarianGrade
        }

        $.ajax({
            url: "/members/account",
            type: 'patch',
            dataType: 'json',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(data)
        })
            .done(function (data) {
                if (data.result === "success") {
                    alert("회원정보를 수정했습니다.");
                } else if (data.result === "fail") {
                    alert("닉네임 또는 연락처를 확인해주세요.");
                }
            });
    });

    $("#auth-info-submit").click(function () {

        let vegetarianGrade = $("#vegetarianGrade").val();
        let data = {vegetarianGrade: vegetarianGrade};

        $.ajax({
            url: "/members/account",
            type: 'patch',
            dataType: 'json',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(data)
        })
            .done(function (data) {
                if (data.result === "success") {
                    alert("회원정보를 수정했습니다.");
                }
            });
    });

    // 비밀번호 변경
    $("#pwd-change").click(function () {
        const password = $("#password");
        const newPassword = $("#newPassword");
        const reNewPassword = $("#re-newPassword");

        if (password.val() === "" || newPassword.val() === "") {
            alert("비밀번호를 입력해주세요.");
            return false;
        } else if (password.val() === newPassword.val()) {
            alert("현재 비밀번호로 변경할 수 없습니다.");
            return false;
        } else if (!passwordRegCheck(newPassword.val())) {
            alert("숫자/대,소문자/특수기호를 포함한 8~12자리");
            return false;
        }

        if (newPassword.val() !== reNewPassword.val()) {
            alert("변경하려는 비밀번호가 일치하지 않습니다.\n변경할 비밀번호를 다시 확인해주세요.");
            reNewPassword.val("");
            reNewPassword.focus();
        } else {
            $.ajax({
                url: "/members/account/pwdChange",
                type: "patch",
                data: {
                    password: password.val(),
                    newPassword: newPassword.val()
                },
                dataType: "json"
            })
                .done(function (data) {
                    if (data.result === "success") {
                        alert("비밀번호가 변경되었습니다.\n다시 로그인해주십시오.");
                        location.replace("/zerogreen/login");
                    } else if (data.result === "fail") {
                        alert("현재 비밀번호가 일치하지 않습니다.\n다시 확인해주십시오.");
                    }
                });
        }
    });

    // 회원 탈퇴
    $("#delete_id_btn").click(function () {
        const password = $("#delete_id").val();

        $.ajax({
            url: "/members/account/deleteMember",
            type: "delete",
            dataType: "json",
            data: {
                password: password
            }
        }).done(function (data) {
            if (data.result === "success") {
                alert("꼭 다시 돌아와주세요..😥");
                location.replace("/zerogreen");
            } else if (data.result === "fail") {
                alert("비밀번호가 일치하지 않습니다.");
            }
        });
    });
});

function passwordRegCheck(password) {
    let regExp = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[$@$!%*?&])[A-Za-z\d$@$!%*?&]{8,12}/;
    return regExp.test(password);
}