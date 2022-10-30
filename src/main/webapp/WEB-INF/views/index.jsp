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

<div class="section_01_wrap">

    <div class="container section_01">

        <div class="lc_header">
            <a href="#" class="logo">
                Link Chart
            </a>
            <ul>
                <li class="lc_list active">
                    <a href="#">Home</a>
                </li>
                <li class="lc_list">
                    <a href="#">Profile</a>
                </li>
                <li class="lc_list">
                    <a href="#">Like</a>
                </li>
                <li class="lc_list">
                    <a href="#">Share</a>
                </li>
            </ul>
        </div>

        <div class="row">
            <div class="col-md-5 intro_content">
                <h2>
                    about Link Chart
                </h2>
                <p>
                    Always wonder how to get the right information.<br>
                    We provide the THINGS you want exactly.<br>
                    and provide objectively verified information.<br>
                    Experience it.
                </p>
            </div>
            <div class="col-md-7 card_wrap">
                <div class="row">
                    <div class="card_content_wrap">
                        <div class="card_content">
                            <h2>01</h2>
                            <h3>CHART</h3>
                            <p>
                                정확한 정보를, 검증된 정보를<br>
                                당신이 원하는대로<br>
                                정확히 제공합니다.<br>
                                경험하십시오.
                            </p>
                        </div>
                    </div>
                    <div class="card_content_wrap">
                        <div class="card_content">
                            <h2>02</h2>
                            <h3>NEWS</h3>
                            <p>
                                Always wonder how to get the right information.<br>
                                We provide the THINGS you want exactly.<br>
                                and provide objectively verified information.<br>
                                Experience it.
                            </p>
                        </div>
                    </div>
                    <div class="card_content_wrap">
                        <div class="card_content">
                            <h2>03</h2>
                            <h3>card one</h3>
                            <p>
                                Always wonder how to get the right information.<br>
                                We provide the THINGS you want exactly.<br>
                                and provide objectively verified information.<br>
                                Experience it.
                            </p>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <p class="copyrightText">
            Copyright &copy; 2022 FOURWARD All Right Reserved.
        </p>

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
            <iframe src="chart/viewStockChart"></iframe>
        </div>

        <div class="news_part">
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

<script type="text/javascript" src="../../resources/js/vanilla-tilt.js"></script>
<script>

    VanillaTilt.init(document.querySelectorAll(".card_content_wrap"), {
        max: 25,
        speed: 400,
        glare: true,
        "max-glare" : 1
    });

    const lc_list = document.querySelectorAll('.lc_list');
    function activeLink(){
        lc_list.forEach((item) => item.classList.remove('active'));
        this.classList.add('active');
    }
    list.forEach((item) => item.addEventListener('click', activeLink));

    /*for(i = 1; i < 100; i++){
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
    }*/

    /*function myInterval(){
        for(i = 1; i < 100; i++){
            document.getElementById("line" + i).style.height = Math.floor(Math.random() * 40) + 1 + "vh";
            document.getElementById("line" + i).style.backgroundColor = "#" +  Math.floor(Math.random() * 900 + 1);
        }
    }*/

    /*setInterval("myInterval()", 1500);

    const text = document.querySelector('.text');
    text.innerHTML = text.textContent.replace(/\S/g, "<span>$&</span>");

    const  wavyelement = document.querySelectorAll('span');
    for(let i = 0; i < wavyelement.length; i++){
        wavyelement[i].style.animationDelay = i * 0.05 + 's';
    }*/
</script>

</body>
</html>