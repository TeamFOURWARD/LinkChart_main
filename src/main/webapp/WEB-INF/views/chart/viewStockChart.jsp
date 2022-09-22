<%@ page contentType="text/html; charset=utf-8" %>
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

        var list = [];

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

        %>
        var tmpList = [<%=pDTO.getDate()%>, <%=pDTO.getLow()%>, <%=pDTO.getHigh()%>, <%=pDTO.getOpen()%>, <%=pDTO.getClose()%>]
        list.push(tmpList)

        <%
        }else{
        for (int i = 0; i < rList.size(); i++){
            String date = rList.get(i).getDate();
            int low = Integer.parseInt(rList.get(i).getLow());
            int high = Integer.parseInt(rList.get(i).getHigh());
            int open = Integer.parseInt(rList.get(i).getOpen());
            int close = Integer.parseInt(rList.get(i).getClose());
        %>
        var tmpList = [new Date(<%=date%>), <%=low%>, <%=high%>, <%=open%>, <%=close%>]
        list.push(tmpList)
        <%
            }
        }
        %>

        function drawChart() {
            var data = google.visualization.arrayToDataTable(list, true);

            var options = {legend: 'none'};

            var chart = new google.visualization.CandlestickChart(document.getElementById('chart_div'));
            chart.draw(data, options);

            console.log("data : " + data);
        }
    </script>

</head>
<body>
<div id="chart_div" style="width: 900px; height: 500px;"></div>
<form method="get" action="chart/getStockData">
    종목명<input type="text" name="name"/>
    <input type="submit"/>
    <input type="reset"/>
</form>
<a href="searchStockData" target="_blank">데이터 가져오기</a>
</body>
</html>
