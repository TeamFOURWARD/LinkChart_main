function updateUserPsw() {
    const id = $("#profile_user_id").val();
    const psw = $("#profile_user_psw").val();

    $.ajax({
        url: 'user/updatePsw',
        data: {
            "user_id": id,
            "user_password": psw
        },
        dataType: "json",
        type: 'POST',
        async: false,
        success: function () {
            alert("비밀번호가 변경되었습니다.");
        },
        error: function () {
            // TODO
            // 서버에서 실패시 에러코드를 반환하여 에러를 인식시키도록 처리.
        }
    })
}