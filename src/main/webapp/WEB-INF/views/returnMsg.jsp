<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>결과 메시지</title>
    <script type="text/javascript">
        <%
        String msg =(String)request.getAttribute("msg");
        %>

        alert("<%=msg%>")
        top.location.href = "${pageContext.request.contextPath}/index"
    </script>
</head>
<body>

</body>
</html>