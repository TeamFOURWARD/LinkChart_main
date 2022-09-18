<%@ page language="java" contentType="text/html; charset=utf-8" %>

<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>종목 데이터 가져오기</title>
</head>
<body>
<h1>가져올 종목 날짜범위, 이름 입력</h1>
<form name="f" method="get" action="${pageContext.request.contextPath}/chart/insertStockData">
    <input name="code" type="text" width="20"/>
    <input name="start_date" type="number" placeholder="예 : 20200101 형식"/>
    <input name="end_date" type="number" placeholder="예 : 20221231 형식"/>
    <input type="submit" value="전송"/>
    <input type="reset" value="초기화"/>
</form>

</body>
</html>