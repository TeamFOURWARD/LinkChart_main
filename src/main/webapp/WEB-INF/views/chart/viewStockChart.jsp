<%@ page contentType="text/html; charset=utf-8" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.fourward.linkchart.dto.StockDTO" %>
<!doctype html>
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>차트 페이지</title>
</head>
<body>



<form method="get" action="char/getStockData" target="ifr">
    종목명<input type="text" name="name"/>
    <input type="submit"/>
    <input type="reset"/>
</form>
<a href="searchStockData.jsp" target="_blank">데이터 가져오기</a>
<iframe name="ifr" style="display: none"></iframe>
</body>
</html>