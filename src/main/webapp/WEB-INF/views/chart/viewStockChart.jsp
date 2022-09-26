<%@ page contentType="text/html; charset=utf-8" %>
<%@ page import="com.fourward.linkchart.dto.StockDTO" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0"/>
    <meta http-equiv="X-UA-Compatible" content="ie=edge"/>
    <title>차트 페이지</title>

    <script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
    <script type="text/javascript">
        google.charts.load('current', {'packages': ['corechart']});
        google.charts.setOnLoadCallback(drawChart);

        let list = [];
        let tmpList = [0, 0, 0, 0, 0]
        <%
        List<StockDTO> rList = (List<StockDTO>) request.getAttribute("rList");
        if (rList == null) {
        %>
        list.push(tmpList)
        <%
        }else{
        for(StockDTO stockDTO: rList){
            String y = stockDTO.getDate().substring(0,4);
            String m = stockDTO.getDate().substring(4,6);
            String d = stockDTO.getDate().substring(6);
            int low = Integer.parseInt(stockDTO.getLow());
            int high = Integer.parseInt(stockDTO.getHigh());
            int open = Integer.parseInt(stockDTO.getOpen());
            int close = Integer.parseInt(stockDTO.getClose());
        %>
        tmpList = [new Date(<%=y%>, <%=m%>-1, <%=d%>), <%=low%>, <%=high%>, <%=open%>, <%=close%>]
        list.push(tmpList)
        <%
            }
        }
        %>

        function drawChart() {
            var data = google.visualization.arrayToDataTable(list, true);

            var options = {
                legend: 'none',
                bar: {groupWidth: '100%'}, // Remove space between bars.
                candlestick: {
                    fallingColor: {strokeWidth: 0, fill: '#a52714'}, // red
                    risingColor: {strokeWidth: 0, fill: '#0f9d58'}   // green
                }
            };

            var chart = new google.visualization.CandlestickChart(document.getElementById('chart_div'));
            chart.draw(data, options);
        }
    </script>

</head>
<body>
<div id="chart_div" style="width: 900px; height: 500px;"></div>
<form method="get" action="${pageContext.request.contextPath}/chart/getStockData">
    종목명<input type="text" name="name"/>
    <input type="submit"/>
    <input type="reset"/>
</form>
<a href="${pageContext.request.contextPath}/chart/searchStockData" target="_blank">데이터 가져오기</a>
<div id="news_div" style="width: 900px; height: 500px;"</div>
</body>
</html>
