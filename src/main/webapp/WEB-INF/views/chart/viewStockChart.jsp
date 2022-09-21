<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.fourward.linkchart.dto.StockDTO" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
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
        <%
            List<StockDTO> rList = (List<StockDTO>) request.getAttribute("rList");
            if (rList == null) {
                rList = new ArrayList<StockDTO>();
                StockDTO pDTO = new StockDTO();
                pDTO.setDate("0");
                pDTO.setLow("0");
                pDTO.setHigh("0");
                pDTO.setOpen("0");
                pDTO.setClose("0");
                rList.add(pDTO);
            }
        %>


        var list = [];
        //for (var i = 0; i <<%=rList.size()%>; i++) {
        var tmpList = [
            <%=Integer.parseInt(rList.get(0).getDate())%>,
            <%=Integer.parseInt(rList.get(0).getLow())%>,
            <%=Integer.parseInt(rList.get(0).getHigh())%>,
            <%=Integer.parseInt(rList.get(0).getOpen())%>,
            <%=Integer.parseInt(rList.get(0).getClose())%>
        ]

        list[0] = tmpList;

        // }
        function drawChart() {
            var data = google.visualization.arrayToDataTable(
                list
                , true);


            console.log("data : " + data);

            var options = {
                legend: 'none'
            };

            var chart = new google.visualization.CandlestickChart(document.getElementById('chart_div'));

            chart.draw(data, options);
        }
    </script>

</head>
<body>
<div id="chart_div" style="width: 900px; height: 500px;"></div>
<form method="get" action="chart/getStockData" >
    종목명<input type="text" name="name"/>
    <input type="submit"/>
    <input type="reset"/>
</form>
<a href="searchStockData" target="_blank">데이터 가져오기</a>
</body>
</html>
