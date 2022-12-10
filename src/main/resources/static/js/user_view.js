// 페이지 로딩후 개인정보 불러옴.
window.addEventListener("load", () => getUserInfo());

function getUserInfo() {
    $.ajax({
        url: 'user/getUserInfo', data: {
            "user_id": SS_USER_ID
        }, dataType: 'json', type: 'POST', async: true, success: function (data) {
            putUserInfo(data);
        }, error: function () {
            // TODO 응답 에러처리, (세션 일치 확인)
        }
    })
}

function putUserInfo(data) {
    document.getElementById("profile_user_id").innerText = SS_USER_ID;
    document.getElementById("profile_user_name").innerText = data.user_name;
    document.getElementById("profile_user_email").innerText = data.user_email;
    document.getElementById("profile_user_addr").innerText = data.user_addr;
}

const psw1 = document.getElementById("profile_update_password");
const psw2 = document.getElementById("profile_update_password_repeat");
const regExPsw = /(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{8,}/;

function updatePsw() {
    if (psw1.value.match(regExPsw)) {
        if (psw1.value === psw2.value) {
            return updatePsw_();
        } else {
            alert("비밀번호가 다릅니다.");
            return false;
        }
    } else {
        alert("비밀번호 양식을 확인해주세요.");
    }
}

function updatePsw_() {
    $.ajax({
        url: 'user/updatePsw', data: {
            "user_id": SS_USER_ID, "user_password": psw1.value
        }, type: 'POST', async: false, statusCode: {
            409: () => {
                alert("이전과 다른 비밀번호를 입력해 주세요.");
            }
        }, success: () => {
            alert("비밀번호가 변경되었습니다.");
            psw1.value = null;
            psw2.value = null;
        }, error: () => {
            alert("비밀번호 변경에 실패하였습니다.");
        }
    });
}

function clearPsw() {
    psw1.value = null;
    psw2.value = null;
}

const lowerCaseLetters = /[a-z]/g;
const upperCaseLetters = /[A-Z]/g;
const numbers = /[0-9]/g;
const msgLowerCaseLetters = document.getElementById("msgLowerCaseLetters");
const msgUpperCaseLetters = document.getElementById("msgUpperCaseLetters");
const msgNumbers = document.getElementById("msgNumbers");
const msgMatchPsw = document.getElementById("msgMatchPsw")
psw1.onkeyup = () => {
    if (psw1.value.match(lowerCaseLetters)) {
        msgLowerCaseLetters.style.display = "none";
    } else {
        msgLowerCaseLetters.style.display = "inline-block";
    }
    if (psw1.value.match(upperCaseLetters)) {
        msgUpperCaseLetters.style.display = "none";
    } else {
        msgUpperCaseLetters.style.display = "inline-block";
    }
    if (psw1.value.match(numbers)) {
        msgNumbers.style.display = "none";
    } else {
        msgNumbers.style.display = "inline-block";
    }
    if (psw1.value.match(regExPsw)) {
        msgMatchPsw.style.display = "inline-block";
    } else {
        msgMatchPsw.style.display = "none";
    }
}

function updateUserEmail() {
    const email_input = document.getElementById("profile_update_email");
    if (email_input.value !== ("" || null || undefined)) {
        $.ajax({
            url: "user/updateEmail", data: {
                "user_id": SS_USER_ID, "user_email": document.getElementById("profile_email").value
            }, type: 'POST', statusCode: {
                409: function () {
                    alert("이전과 다른 이메일을 입력해 주세요.");
                }
            }, success: function () {
                alert("이메일이 변경되었습니다.");
            }, error: function () {
                alert("이메일 변경에 실패하였습니다.");
            }, complete: function () {
                document.getElementById("user_updateEmail").reset();
                getUserInfo();
            }
        })
    }
}

// TODO 주소 유효성 확인

// 로그아웃

const aLogout = document.getElementById("aLogout");
aLogout.addEventListener("click", () => {
    return logout()
});

function logout() {
    $.ajax({
        url: "/user/logout",
        type: "post",
        contentType: "application/json; charset=UTF-8",
        dataType: "json",
        statusCode: {
            200: () => {
                window.location.reload();
            }
        }
    })
}