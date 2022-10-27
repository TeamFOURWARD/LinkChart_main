<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <link rel="stylesheet" href="../../../resources/css/reset.css">
    <link rel="stylesheet" href="../../../resources/css/all.min.css">
    <link rel="stylesheet" href="../../../resources/css/newspart.css">
    <!-- Latest compiled and minified CSS -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.1/dist/css/bootstrap.min.css">
    <!-- jQuery library -->
    <script src="https://cdn.jsdelivr.net/npm/jquery@3.6.0/dist/jquery.slim.min.js"></script>
    <!-- Popper JS -->
    <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"></script>
    <!-- Latest compiled JavaScript -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.1/dist/js/bootstrap.bundle.min.js"></script>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
    <title>테스트 모델</title>
</head>
<body>

<div class="container_wrap section_chart">
    <div class="container">
        <div class="sc_header">
            <ul>
                <li>
                    <a href="chart/searchStockData" target="_blank">종목 가져오기</a>
                </li>
                <li>
                    <a href="chart/viewStockChart" target="_self">차트 페이지</a>
                </li>
            </ul>
        </div>

        <div class="sc_chart">
            <h2>차트 부분</h2>
            <iframe src="chart/viewStockChart"></iframe>
        </div>

        <div class="news_part">
            <h2>뉴스 부분</h2>
            <form action="news/getNewsData" method="get" target="fNews">
                키워드 : <input type="text" name="name">
                날짜 : <input type="text" name="date">
                <input type="submit">
            </form>
            <%--    기본값 : 현재 날짜와 증시로 뉴스를 검색--%>
            <iframe name="fNews" src="news/getNewsData?name=증시&date=<%=request.getAttribute("nowDate")%>"></iframe>
        </div>
    </div>
</div>

<%----%>

<%--<div style="text-align: right" class="container">
    <a href="chart/searchStockData" target="_blank">종목 가져오기</a>
    <br>
    <a href="chart/viewStockChart" target="_self">차트 페이지</a>
</div>--%>
<%--<div style="text-align: center; height: 100vh;" class="container">
    <h2>차트 부분</h2>
    <iframe src="chart/viewStockChart" width="100%" height="100%"></iframe>
</div>--%>
<%--<div>
    <h2>뉴스 부분</h2>
    <form action="news/getNewsData" method="get" target="fNews">
        키워드 : <input type="text" name="name">
        날짜 : <input type="text" name="date">
        <input type="submit">
    </form>
    &lt;%&ndash;    기본값 : 현재 날짜와 증시로 뉴스를 검색&ndash;%&gt;
    <iframe name="fNews" src="news/getNewsData?name=증시&date=<%=request.getAttribute("nowDate")%>" width="100%" height="100%"></iframe>
</div>--%>
</body>
</html>