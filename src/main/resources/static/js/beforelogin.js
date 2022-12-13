//password script
var myInput = document.getElementById("inputPwd");
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
var repeatPsw = document.getElementById("inputPwdRepeat");
var chkRepeatPsw = false;

repeatPsw.onfocus = () => {
    document.getElementById("chkPwd").style.display = "block";
}

repeatPsw.onblur = () => {
    document.getElementById("chkPwd").style.display = "none";
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

// 회원가입 유효성 ========================================================

const inputAddr = document.getElementById("inputAddr");
const inputName = document.getElementById("inputName");
const inputId = document.getElementById("inputId");
const inputEmail = document.getElementById("inputEmail");
const inputMobilePin = document.getElementById("inputMobilePin");
const inputEmailPin = document.getElementById("inputEmailPin");
const n1 = document.getElementById("inputMobile_n1");
const n2 = document.getElementById("inputMobile_n2");
const n3 = document.getElementById("inputMobile_n3");
const btnSubmitId = document.getElementById("btnSubmitId");
const btnSubmitMobile = document.getElementById("btnSubmitMobile");
const btnSubmitMobilePin = document.getElementById("btnSubmitMobilePin");
const btnResetMobilePin = document.getElementById("btnResetMobilePin");
const btnSubmitEmail = document.getElementById("btnSubmitEmail")
const btnSubmitEmailPin = document.getElementById("btnSubmitEmailPin");
const btnResetEmailPin = document.getElementById("btnResetEmailPin");
const btnSubmitSignup = document.getElementById("btnSubmitSignup");

let valId = false
const valName = () => inputName.value !== "";
const valAddr = () => inputAddr.value !== "";
let checkCertEmail = false;
let checkCertMobile = false;

btnSubmitSignup.addEventListener("click", ev => {
    if (validateForm()) {
        return submitSignupForm();
    }
    alert("입력사항을 확인해주세요.");
})
btnSubmitMobile.addEventListener("click", ev => {
    checkMobile();
})
btnResetMobilePin.addEventListener("click", ev => invalidateSession())
btnResetEmailPin.addEventListener("click", ev => resetEmail())
btnSubmitMobilePin.addEventListener("click", ev => checkMobilePin());
btnSubmitEmailPin.addEventListener("click", ev => checkEmailPin());
btnSubmitEmail.addEventListener("click", ev => checkEmail());
btnSubmitId.addEventListener("click", ev => checkId());

function checkId() {
    if (inputId.value === '') {
        return alert('아이디를 입력해 주세요');
    }
    // 정규식 아이디 검증 TODO
    $.ajax({
        url: "/user/validate/id",
        type: "post",
        data: JSON.stringify({
            "user_id": inputId.value
        }),
        contentType: "application/json; charset=UTF-8",
        dataType: 'json',
        statusCode: {
            200: () => {
                valId = true;
                if (!confirm("사용할 수 있는 아이디 입니다.\n사용하시겠습니까?")) {
                    inputId.value = "";
                }
            },
            400: () => {
                valId = false;
                alert("사용할 수 없는 아이디 입니다.")
            }
        }
    })
}

function checkMobile() {
    const m = n1.value.concat(n2.value, n3.value);
    console.log('전화번호 : ' + m);
    $.ajax({
        url: "/user/validate/Mobile",
        type: "post",
        data: JSON.stringify({
            "mobile": m
        }),
        contentType: "application/json; charset=UTF-8",
        statusCode: {
            200: () => {
                alert("인증번호를 5분 안으로 입력해주세요.");
            },
            400: ss => {
                if (
                    confirm("이전 인증 작업이 존재합니다." + ss.responseText + " 초 후에 다시 시도해 주세요.\n"
                        + "인증작업을 처음부터 다시 하겠습니까?")) {
                    invalidateSession();
                }
            },
            406: () => {
                alert("중복된 전화번호 입니다.");
            }
        },
    })
}

function checkMobilePin() {
    $.ajax({
        url: "/user/validate/Mobile/pin",
        type: "post",
        data: JSON.stringify({
            "pin": inputMobilePin.value
        }),
        contentType: "application/json; charset=UTF-8",
        dataType: 'json',
        statusCode: {
            200: () => {
                n2.disabled = true
                n3.disabled = true
                inputMobilePin.value = "";
                inputMobilePin.disabled = true
                inputMobilePin.readOnly = true
                btnSubmitMobile.style.display = "none";
                btnSubmitMobilePin.style.display = "none";
                checkCertMobile = true;
                alert("인증에 성공하였습니다.");
            },
            400: () => {
                alert("핀 번호를 확인해주세요.");
            },
            408: () => {
                alert("요청이 만료되었습니다.");
                invalidateSession();
            }
        }
    })
}

function checkEmail() {
    if (inputEmail.value === "") {
        return alert('이메일을 입력해 주세요');
    }
    // 정규식 이메일 검증 TODO
    inputEmail.disabled = true;
    $.ajax({
        url: "/user/validate/email",
        type: "post",
        data: JSON.stringify({
            "user_email": inputEmail.value
        }),
        contentType: "application/json; charset=UTF-8",
        dataType: 'json',
        statusCode: {
            200: () => {
                alert("사용할 수 있는 이메일 입니다.\n발송된 코드를 입력해주세요.");
                console.log('mail : ' + inputEmail.value);
            },
            400: () => {
                inputEmail.disabled = false;
                alert("사용할 수 없는 이메일 입니다.");
            },
            408: () => {
                inputEmail.disabled = false;
                alert("요청 시간이 만료되었습니다.");
                window.location.reload();
            },
            500: () => {
                inputEmail.disabled = false;
                alert("서버에 문제가 발생하였습니다. 잠시후 다시 시도해주시기 바랍니다.");
            }
        }
    })
}

function checkEmailPin() {
    $.ajax({
        url: "/user/validate/email/pin",
        type: "post",
        contentType: "application/json; charset=UTF-8",
        dataType: 'json',
        data: JSON.stringify({
            "pin": inputEmailPin.value
        }),
        statusCode: {
            200: () => {
                alert("인증되었습니다.");
                checkCertEmail = true;
                inputEmailPin.value = "";
                inputEmailPin.disabled = true;
                inputEmailPin.readOnly = true;
                btnSubmitEmail.style.display = "none";
                btnSubmitEmailPin.style.display = "none";
            },
            400: () => {
                alert("이메일 핀 번호를 다시 확인해주세요.");
            },
            408: () => {
                alert("요청이 만료되었습니다.\n인증을 다시 시작해주세요.");
            }
        }, error: () => checkCertEmail = false
    })
}

function invalidateSession() {
    $.ajax({
        url: "/user/invalidate",
        type: "get",
        statusCode: {
            200: () => {
                alert("인증을 처음부터 다시 시작해 주세요.");
                n2.value = "";
                n3.value = "";
                inputMobilePin.value = "";
                inputMobilePin.disabled = false;
                inputMobilePin.readOnly = false;
                inputId.value = "";
                inputId.readOnly = false;
                inputId.disabled = false;
                inputEmail.value = "";
                inputEmail.disabled = false;
                inputEmail.readOnly = false;
                inputEmailPin.value = "";
                inputEmailPin.disabled = false;
                inputEmailPin.readOnly = false;
                btnSubmitMobile.style.display = "block";
                btnSubmitMobilePin.style.display = "block";
                btnSubmitEmail.style.display = "block";
                btnSubmitEmailPin.style.display = "block";
            }
        }
    })
}

const validateForm = () => {
    if (valId && valName() && valAddr() && chkRepeatPsw) {
        if (checkCertMobile && checkCertEmail) {
            return true;
        } else {
            alert("인증상태를 확인해주세요.")
            return false;
        }
    } else {
        return false;
    }
}

function submitSignupForm() {
    console.log("form data : " + jsonSignupForm());
    $.ajax({
        url: "/user/doSignUp",
        type: "post",
        data: jsonSignupForm(),
        contentType: "application/json; charset=UTF-8",
        statusCode: {
            200: () => {
                alert("가입에 성공하였습니다.");
                window.location.reload();
            },
            400: () => {
                alert("요청이 잘못되었습니다. 입력사항을 확인해주세요.")
            },
            408: () => {
                alert("요청이 만료되었습니다.");
            },
            500: () => {
                alert("서버에 문제가 발생하였습니다. 잠시후 다시 시도해주세요.");
            }
        }
    })
}

function resetEmail() {
    inputEmail.value = "";
    inputEmail.disabled = false;
    inputEmail.readOnly = false;
    inputEmailPin.value = "";
    inputEmailPin.readOnly = false;
    inputEmailPin.disabled = false;
    btnSubmitEmail.style.display = "block";
    btnSubmitEmailPin.style.display = "block";
}

const jsonSignupForm = () =>
    JSON.stringify({
        "user_id": inputId.value,
        "user_name": document.getElementById("inputName").value,
        "mobile": (n1.value).concat(n2.value, n3.value),
        "user_email": inputEmail.value,
        "user_password": document.getElementById("inputPwd").value,
        "user_addr": document.getElementById("inputAddr").value
    })

// 로그인

const inputLoginId = document.getElementById("inputLoginId");
const inputLoginPwd = document.getElementById("inputLoginPwd");
const btnSubmitLoginForm = document.getElementById("btnSubmitLoginForm");

btnSubmitLoginForm.addEventListener("click", () => submitLoginForm());
const jsonLoginForm = () =>
    JSON.stringify({
        "user_id": inputLoginId.value,
        "user_password": inputLoginPwd.value
    })

function submitLoginForm() {
    console.log("form data : " + jsonLoginForm());
    $.ajax({
        url: "/user/login",
        type: "post",
        data: jsonLoginForm(),
        contentType: "application/json; charset=UTF-8",
        dataType: "json",
        statusCode: {
            200: () => {
                window.location.reload();
            },
            400: () => {
                alert("잘못된 회원정보 입니다.")
            },
            408: () => {
                alert("요청이 만료되었습니다.");
            },
            500: () => {
                alert("서버에 문제가 발생하였습니다. 잠시후 다시 시도해주세요.");
            }
        }
    })
}