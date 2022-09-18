<%@ page contentType="text/html; charset=utf-8" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="linkchart.dto.StockDTO" %>
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
/
차트 그려지는 부분
/
<form method="get" action="char/getStockData">
    시작날짜<input type="number" name="start_date" placeholder="20220101 형식"/>
    종료날짜<input type="number" name="end_date" placeholder="20221231 형식"/>
    종목명<input type="text" name="name"/>
    <input type="submit"/>
    <input type="reset"/>
</form>
<a href="searchStockData.jsp" target="_blank">데이터 가져오기</a>
</body>
</html>