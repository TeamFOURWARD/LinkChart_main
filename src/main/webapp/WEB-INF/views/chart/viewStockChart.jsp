<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0"/>
    <meta http-equiv="X-UA-Compatible" content="ie=edge"/>
    <title>차트 페이지</title>

    <script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
    <script type="text/javascript">
        google.charts.load('current', {'packages':['corechart']});
        google.charts.setOnLoadCallback(drawChart);

        function drawChart() {
            var jsonData = $.ajax({
                url:"/chart/getStockData",
                dataType:"json",
                async:false
            }).responseText;

            console.log("jsonData : " + jsonData);

            var options = {
                legend:'none'
            };

            var chart = new google.visualization.CandlestickChart(document.getElementById('chart_div'));

            chart.draw(jsonData, options);
        }
    </script>

</head>
<body>
<div id="chart_div" style="width: 900px; height: 500px;"></div>
<form method="get" action="char/getStockData" target="ifr">
    종목명<input type="text" name="name"/>
    <input type="submit"/>
    <input type="reset"/>
</form>
<a href="searchStockData" target="_blank">데이터 가져오기</a>
<iframe name="ifr" style="display: none"></iframe>
</body>
</html>
