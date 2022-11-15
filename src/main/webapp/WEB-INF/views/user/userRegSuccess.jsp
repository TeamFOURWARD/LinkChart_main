<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>회원가입 완료</title>
    <script type="text/javascript">
        setTimeout(() => location.href = "/", 3000);

        function redirect() {
            location.href = '/'
        }
    </script>
</head>
<body>
<div style="text-align: center;">
    <div>
        <h2>${user_id} 님 회원가입을 축하합니다.</h2>
        <br/>
        <p>잠시후 홈페이지로 돌아갑니다.</p>
        <br/>
        <p>로그인 해주세요.</p>
        <button onclick="redirect()">되돌아가기</button>
    </div>
</div>
</body>
</html>