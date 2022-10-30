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
    <a href="chart/searchStockData" target="_self">종목 가져오기 페이지</a>
    <br>
    <a href="chart/viewStockChart" target="_self">차트 페이지</a>
</div>


<div style="text-align: center;">

    <div style="display:inline-flex;text-align: center;width: 47%;">
        <div style="width: 100%;">
            <div>
                <h2>차트 부분</h2>
            </div>
            <div>
                <iframe src="chart/viewStockChart" width="100%" height="600px"></iframe>
            </div>
        </div>
    </div>


    <div style="display:inline-flex;text-align: center;width: 47%;">
        <div style="width: 100%;">
            <div>
                <h2>뉴스 부분</h2>
            </div>
            <div>
                <form action="news/getNewsData" method="get" target="fNews">
                    키워드 : <input type="text" name="name">
                    날짜 : <input type="text" name="date">
                    <input type="submit">
                </form>
                <%--    기본값 : 현재 날짜와 증시로 뉴스를 검색--%>
                <iframe name="fNews" src="news/getNewsData?name=증시&date=<%=request.getAttribute("nowDate")%>"
                        width="100%"
                        height="1200px"></iframe>
            </div>
        </div>
    </div>

</div>


</body>
</html>