/**
 *
 * @param arg String. 종목 키워드
 * @param condition boolean. true : 뉴스를 같이 로딩함.
 */
function getStockData(arg, condition) {
    let stockName;
    const startDate_req = document.getElementById("chart_startTime").value;
    const endDate_req = document.getElementById("chart_endTime").value;
    const t = document.getElementById("chart_timeframe");
    const timeframe = t.options[t.selectedIndex].value;
    if (arg == null) {
        stockName = document.getElementById("chart_name").value;
    } else {
        stockName = arg;
    }
    $.ajax({
        url: "/chart/getStockData",
        data: JSON.stringify({
            "name": stockName,
            "startTime": startDate_req,
            "endTime": endDate_req,
            "timeframe": timeframe
        }),
        type: 'POST',
        contentType: "application/json; charset=UTF-8",
        dataType: 'json',
        statusCode: {
            400: () => alert("잘못된 요청 입니다. 종목명을 확인해 주세요."),
            500: () => alert("서버에 오류가 발생 하였습니다. 잠시후 다시 시도해 주세요."),
        },
        success: function (data) {
            if (condition) {
                getNewsData(stockName, null, false);
            }
            if (timeframe === 'month') {
                return loadChart(data, stockName, true);
            } else {
                return loadChart(data, stockName, false);
            }
        }
    });
}

/**
 *
 * @param data 서버에서 받은 데이터
 * @param name 종목명
 * @param t 월별 표시시 '일' 생략
 */
function loadChart(data, name, t) {
    google.charts.load('current', {'packages': ['corechart'], 'language': 'ko'});
    google.charts.setOnLoadCallback(drawChart);

    let chart;
    let dataTable;
    const keyword = name;
    const stockData = data;

    function drawChart() {
        dataTable = new google.visualization.DataTable();
        dataTable.addColumn('date', '날짜');
        dataTable.addColumn('number', '저가');
        dataTable.addColumn('number', '시가');
        dataTable.addColumn('number', '종가');
        dataTable.addColumn('number', '고가');

        let list = []
        stockData.forEach(e => {
            const date = stringToDate(e.date, t);
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
            title: '종목명  :  ' + keyword,
            'height': 700,
            'backgroundColor': '#FCF6F5',
            bar: {
                groupWidth: '100%' // Remove space between bars.
            },
            candlestick: {
                fallingColor: {
                    strokeWidth: 0,
                    fill: '#005cff'
                },
                risingColor: {
                    strokeWidth: 0,
                    fill: '#ff0000'
                }
            }, hAxis: {
                format: 'M/d/yy',
                gridLines: {
                    count: 'none'
                }
            }
        };
        chart.draw(dataTable, options);
    }

    function selectHandler() {
        const selectedItem = chart.getSelection()[0];
        const date = dateToString(dataTable.getValue(selectedItem.row, 0));//yyyyMMdd

        return getNewsData(keyword, date);
    }
}