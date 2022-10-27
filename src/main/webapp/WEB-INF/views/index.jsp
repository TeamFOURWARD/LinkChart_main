<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <link rel="stylesheet" href="../../resources/css/reset.css">
    <link rel="stylesheet" href="../../resources/css/all.min.css">
    <link rel="stylesheet" href="../../resources/css/newspart.css">
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
    <script type="text/javascript" src="../../resources/js/index.js"></script>
    <link rel="stylesheet" href="../../resources/css/intro.css">
    <style>
        @import url('https://fonts.googleapis.com/css2?family=Handlee&family=Jua&family=Nanum+Gothic:wght@400;700;800&family=Roboto:wght@300;400;700&display=swap');
    </style>
    <title>LINK CHART</title>
</head>
<body>

<div class="intro">
    <div class="intro_background_wrap">
        <ul class="intro_background_cover" id="bpwrap">
        </ul>
    </div>
    <div class="container">
        <div class="row">
            <div class="col-md-12 col-sm-12">
                <div class="text-center">
                    <h2 class="text">
                        Link Chart
                    </h2>
                    <p>
                        by fourward
                    </p>
                </div>
            </div>
        </div>
    </div>
</div>

<div class="container_wrap section_chart">
    <div class="container">
        <div class="sc_header">
            <ul>
                <li>
                    <a href="chart/searchStockData" target="_blank">
                        종목 가져오기
                    </a>
                </li>
                <li>
                    <a href="chart/viewStockChart" target="_self">
                        차트 페이지
                    </a>
                </li>
            </ul>
        </div>

        <div class="sc_chart">
            <h2>
                차트 부분
            </h2>
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

<script>

    for(i = 1; i < 100; i++){
        j = 30;
        const para1 = document.createElement("li");
        const element = document.getElementById("bpwrap");
        para1.setAttribute('id', 'bp' + i);
        element.appendChild(para1);

        const para2 = document.createElement("div");
        para2.setAttribute('id', 'line' + i);
        para1.appendChild(para2);

        para2.setAttribute('class', 'bpf');

        document.getElementById("bp" + i).style.left = j*i - 50 + "px";
    }

    function myInterval(){
        for(i = 1; i < 100; i++){
            document.getElementById("line" + i).style.height = Math.floor(Math.random() * 40) + 1 + "vh";
            document.getElementById("line" + i).style.backgroundColor = "#" +  Math.floor(Math.random() * 900 + 1);
        }
    }

    setInterval("myInterval()", 1500);

    const text = document.querySelector('.text');
    text.innerHTML = text.textContent.replace(/\S/g, "<span>$&</span>");

    const  wavyelement = document.querySelectorAll('span');
    for(let i = 0; i < wavyelement.length; i++){
        wavyelement[i].style.animationDelay = i * 0.05 + 's';
    }
</script>

</body>
</html>