// Date to string yyyyMMdd
function dateToString(date = new Date()) {
    const Y = date.getFullYear();
    const m = date.getMonth() + 1;
    const M = m > 9 ? m : '0' + m;
    const d = date.getDate();
    const D = d > 9 ? d : '0' + d;

    return Y + '' + M + '' + D;
}

// String yyyyMMdd to Date
function stringToDate(s) {
    const y = s.substring(0, 4);
    const m = s.substring(4, 6);
    const d = s.substring(6);

    return new Date(y, m-1, d);
}