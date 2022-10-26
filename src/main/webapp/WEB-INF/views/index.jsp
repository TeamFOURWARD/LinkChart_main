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
<h1>테스트</h1>
<div style="text-align: right">
    <a href="chart/searchStockData" target="_blank">종목 가져오기</a>
    <br>
    <a href="chart/viewStockChart" target="_self">차트 페이지</a>
</div>
<div style="text-align: center">
    <h2>차트 부분</h2>
    <iframe src="chart/viewStockChart" width="80%" height="600px"></iframe>
</div>
<div style="text-align: center">
    <h2>뉴스 부분</h2>
    <form action="news/getNewsData" method="get" target="fNews">
        키워드 : <input type="text" name="name">
        날짜 : <input type="text" name="date">
        <input type="submit">
    </form>
    <%--    기본값 : 현재 날짜와 증시로 뉴스를 검색--%>
    <iframe name="fNews" src="news/getNewsData?name=증시&date=<%=request.getAttribute("nowDate")%>" width="90%"
            height="1200px"></iframe>
</div>
</body>
</html>