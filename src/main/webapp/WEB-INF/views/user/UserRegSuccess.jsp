<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.fourward.linkchart.util.CmmUtil" %>
<%@ page import="com.fourward.linkchart.dto.UserInfoDTO" %>
<%
    //Controller로부터 전달받은 데이터
    String msg = CmmUtil.nvl((String) request.getAttribute("msg"));

    //Controller로부터 전달받은 웹(회원정보 입력화면)으로부터 입력받은 데이터(회원아이디, 이름, 이메일, 주소 등)
    UserInfoDTO pDTO = (UserInfoDTO) request.getAttribute("pDTO");

    if (pDTO == null) {
        pDTO = new UserInfoDTO();

    }

%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>회원가입을 축하드립니다.</title>
    <script type="text/javascript">
        alert("<%=msg%>");
    </script>
</head>
<body>
<%=CmmUtil.nvl(pDTO.getUser_name()) %>님의 회원가입을 축하드립니다.
</body>
</html>