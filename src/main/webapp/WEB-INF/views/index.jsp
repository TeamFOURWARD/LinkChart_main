<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>테스트 모델</title>
</head>
<body>
<h1>테스트 모델</h1>
<div style="text-align: right">
    <a href="chart/searchStockData" target="_self">종목 가져오기</a>
    <br>
    <a href="chart/viewStockChart" target="_self">차트 페이지</a>
</div>
<div style="text-align: center">
    <h2>차트 부분</h2>
    <iframe src="chart/viewStockChart" width="80%" height="600px"></iframe>
</div>
<div style="text-align: center">
    <h2>뉴스 부분</h2>
</div>
</body>
</html>