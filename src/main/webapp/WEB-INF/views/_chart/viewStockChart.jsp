<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> <%@ page
contentType="text/html; charset=utf-8" %> <%@ page
import="com.fourward.linkchart.dto.StockDTO" %> <%@ page import="java.util.List"
%> <%--deprecated--%>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <meta
      name="viewport"
      content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0"
    />
    <meta http-equiv="X-UA-Compatible" content="ie=edge" />
    <title>차트 페이지</title>

    <script
      type="text/javascript"
      src="https://www.gstatic.com/charts/loader.js"
    ></script>
    <script type="text/javascript">
      google.charts.load('current', {'packages': ['corechart'], 'language': 'ko'});
      google.charts.setOnLoadCallback(drawChart);

      let chart;
      let dataTable;
      const keyword = "<c:out value="${name}"/> ";

      function drawChart() {
          dataTable = new google.visualization.DataTable();

          dataTable.addColumn('date', '날짜')
          dataTable.addColumn('number', '저가')
          dataTable.addColumn('number', '시가')
          dataTable.addColumn('number', '종가')
          dataTable.addColumn('number', '고가')

          let list = []
          let tmpList

          <%
          List<StockDTO> rList = (List<StockDTO>) request.getAttribute("rList");
          if (rList == null) {
          %>
          tmpList = [null, 0, 0, 0, 0]
          list.push(tmpList)
          <%
          }else{
          for(StockDTO stockDTO: rList){
              String y = stockDTO.getDate().substring(0,4);
              String m = stockDTO.getDate().substring(4,6);
              String d = stockDTO.getDate().substring(6);
              String low = stockDTO.getLow();
              String high = stockDTO.getHigh();
              String open = stockDTO.getOpen();
              String close = stockDTO.getClose();
          %>
          tmpList = [new Date(<%=y%>, (<%=m%>-1), <%=d%>), parseInt(<%=low%>), parseInt(<%=open%>), parseInt(<%=close%>), parseInt(<%=high%>)]
          //tmpList = [(<%=y%>+'년' + (<%=m%>-1) + '월' + <%=d%>+'일'), parseInt(<%=low%>), parseInt(<%=open%>), parseInt(<%=close%>), parseInt(<%=high%>)]
          list.push(tmpList)
          <%
              }
          }
          %>
          dataTable.addRows(list)

          chart = new google.visualization.CandlestickChart(document.getElementById('chart_div'));
          google.visualization.events.addListener(chart, 'select', selectHandler);

          const options = {
              title: '종목명 : <%=(String) request.getAttribute("name")%>',
              bar: {groupWidth: '100%'}, // Remove space between bars.
              candlestick: {
                  fallingColor: {strokeWidth: 0, fill: '#005cff'},
                  risingColor: {strokeWidth: 0, fill: '#ff0000'}
              },
              hAxis: {
                  format: 'M/d/yy',
                  gridLines: {count: 'none'}
              }
          };
          chart.draw(dataTable, options);
      }

      function selectHandler() {
          let selectedItem = chart.getSelection()[0];
          let value = dataTable.getValue(selectedItem.row, 0);
          const Y = value.getFullYear();
          const m = value.getMonth() + 1;
          const M = m > 9 ? m : '0' + m;
          const d = value.getDate();
          const D = d > 9 ? d : '0' + d;
          const date = Y + '' + M + '' + D;
          alert('selected date : ' + date + '\nselected stockName : ' + keyword);
          console.log('selected date : ' + date + '\nselected stockName : ' + keyword);
      }
    </script>
    <script type="text/javascript"></script>
  </head>
  <body>
    <div style="text-align: center">
      <div
        id="chart_div"
        style="text-align: center; width: 100%; height: 500px"
      ></div>
      <form
        method="get"
        action="${pageContext.request.contextPath}/chart/getStockData"
      >
        종목명<input type="text" name="name" />
        <input type="submit" />
        <input type="reset" />
      </form>
      <a
        href="${pageContext.request.contextPath}/chart/searchStockData"
        target="_blank"
        >데이터 가져오기</a
      >
    </div>
  </body>
</html>
