//password script
var myInput = document.getElementById("user_password");
var letter = document.getElementById("letter");
var capital = document.getElementById("capital");
var number = document.getElementById("number");
var length = document.getElementById("length");

// When the user clicks on the password field, show the message box
myInput.onfocus = function () {
    document.getElementById("message").style.display = "block";
}

// When the user clicks outside of the password field, hide the message box
myInput.onblur = function () {
    document.getElementById("message").style.display = "none";
}

// When the user starts to type something inside the password field
myInput.onkeyup = function () {
    // Validate lowercase letters
    var lowerCaseLetters = /[a-z]/g;
    if (myInput.value.match(lowerCaseLetters)) {
        letter.classList.remove("invalid");
        letter.classList.add("valid");
    } else {
        letter.classList.remove("valid");
        letter.classList.add("invalid");
    }

    // Validate capital letters
    var upperCaseLetters = /[A-Z]/g;
    if (myInput.value.match(upperCaseLetters)) {
        capital.classList.remove("invalid");
        capital.classList.add("valid");
    } else {
        capital.classList.remove("valid");
        capital.classList.add("invalid");
    }

    // Validate numbers
    var numbers = /[0-9]/g;
    if (myInput.value.match(numbers)) {
        number.classList.remove("invalid");
        number.classList.add("valid");
    } else {
        number.classList.remove("valid");
        number.classList.add("invalid");
    }

    // Validate length
    if (myInput.value.length >= 8) {
        length.classList.remove("invalid");
        length.classList.add("valid");
    } else {
        length.classList.remove("valid");
        length.classList.add("invalid");
    }
}
// 비밀번호 일치 검사
var repeatPsw = document.getElementById("user_password_repeat");
var chkRepeatPsw = false;

repeatPsw.onfocus = () => {
    document.getElementById("chkPsw").style.display = "block";
}

repeatPsw.onblur = () => {
    document.getElementById("chkPsw").style.display = "none";
}

repeatPsw.onkeyup = () => {
    if (repeatPsw.value === null) {
        document.getElementById("pswOk").style.display = "none";
        document.getElementById("pswWrong").style.display = "none";
    } else {
        if (myInput.value === repeatPsw.value) {
            document.getElementById("pswWrong").style.display = "none";
            document.getElementById("pswOk").style.display = "inline";
            chkRepeatPsw = true;
        } else {
            document.getElementById("pswOk").style.display = "none";
            document.getElementById("pswWrong").style.display = "inline";
            chkRepeatPsw = false;
        }
    }
}

// 회원가입 유효성
var checkDupId = false;
var checkDupEmail = false;

function checkId() {
    const val = $("#signup_id").val();
    if (val === '') {
        return alert('아이디를 입력해 주세요');
    }
    const type = 'user_id';
    return isExist(val, type);
}

function checkEmail() {
    const val = $("#signup_email").val();
    if (val === "") {
        return alert('이메일을 입력해 주세요');
    }
    const type = 'user_email';
    return isExist(val, type);
}

function isExist(val, type) {
    $.ajax({
        url: '/user/isExist',
        data: {
            value: val,
            type: type
        },
        type: 'POST',
        dataType: 'json',
        async: false,
        success: function (data) {
            if (data.isExist === '1') {
                if (type === 'user_id') {
                    alert('중복된 아이디 입니다.')
                    checkDupId = false;
                }
                if (type === 'user_email') {
                    alert('중복된 이메일 입니다.')
                    checkDupEmail = false;
                }
            } else {
                if (type === 'user_id') {
                    alert('이용가능한 아이디 입니다.')
                    checkDupId = true;
                }
                if (type === 'user_email') {
                    alert('이용가능한 이메일 입니다.')
                    checkDupEmail = true;
                }
            }
        }
    });
}

function validateForm() {
    if (chkRepeatPsw) {
        if (checkDupId && checkDupEmail) {
            return true;
        } else {
            alert('아이디 또는 이메일 중복확인을 해주세요.');
            return false;
        }
    } else {
        alert('비밀번호를 확인해주세요.')
        return false;
    }
}