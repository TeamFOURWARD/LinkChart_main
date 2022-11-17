<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>요청 에러</title>
</head>
<body>
<div style="text-align: center">
    <div>
        <h1>처리중 에러가 발생하였습니다.</h1>
        <br/>
        <p>잠시후 홈페이지로 되돌아갑니다.</p>
        ${error_type}
    </div>
</div>
<script type="text/javascript">
    setTimeout(() => location.href = "/", 3000);
</script>
</body>
</html>