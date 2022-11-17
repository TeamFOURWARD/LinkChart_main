function getStockData(arg) {
    let stockName;
    const startDate_req = $("#startDate_req").val();
    const endDate_req = $("#endDate_req").val();
    if (arg == null) {
        stockName = $("#stockName").val();
    } else {
        stockName = arg;
    }
    console.log('keyword : ' + stockName);
    console.log('start_date_req: ' + startDate_req);
    console.log('end_date_req: ' + endDate_req);

    $.ajax({
        url: "/chart/getStockData",
        data: {
            stockName: stockName,
            startDate_req: startDate_req,
            endDate_req: endDate_req
        },
        type: 'POST',
        dataType: 'json',
        async: false,
        success: function (data) {
            if (data.length === 0) {
                alert("종목 가져오기 실패");
                return;
            }
            console.log('getStockData_row[0] : ' + data[0].date.toString() + ' ' + parseInt(data[0].open));

            return loadChart(data, stockName);
        }
    });
}

function loadChart(data, name) {
    google.charts.load('current', {'packages': ['corechart'], 'language': 'ko'});
    google.charts.setOnLoadCallback(drawChart);

    let chart;
    let dataTable;
    const keyword = name;
    const stockData = data;

    console.log('loadChart_keyword : ' + keyword);

    function drawChart() {
        dataTable = new google.visualization.DataTable();

        dataTable.addColumn('date', '날짜');
        dataTable.addColumn('number', '저가');
        dataTable.addColumn('number', '시가');
        dataTable.addColumn('number', '종가');
        dataTable.addColumn('number', '고가');

        let list = []
        stockData.forEach(e => {
            const date = stringToDate(e.date);
            const open = parseInt(e.open);
            const low = parseInt(e.low);
            const high = parseInt(e.high);
            const close = parseInt(e.close);
            const d = [date, low, open, close, high];
            list.push(d);
        });
        dataTable.addRows(list);

        chart = new google.visualization.CandlestickChart(document.getElementById('chart_div'));
        google.visualization.events.addListener(chart, 'select', selectHandler);

        const options = {
            title: '종목명  :  ' + keyword, 'height': 700, 'backgroundColor': '#FCF6F5',
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
        const selectedItem = chart.getSelection()[0];
        const date = dateToString(dataTable.getValue(selectedItem.row, 0));//yyyyMMdd

        //alert('selected date : ' + date + '\nselected stockName : ' + keyword);
        console.log('selected date : ' + date + '\nselected stockName : ' + keyword);

        return getNewsData(keyword, date);
    }
}