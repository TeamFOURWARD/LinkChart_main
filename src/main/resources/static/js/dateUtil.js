/**
 *
 * @param date 현재날짜
 * @returns {string} 현재 날짜의 문자열 표시
 */
function dateToString(date = new Date()) {
    const Y = date.getFullYear();
    const m = date.getMonth() + 1;
    const M = m > 9 ? m : '0' + m;
    const d = date.getDate();
    const D = d > 9 ? d : '0' + d;

    return Y + '' + M + '' + D;
}

/**
 *
 * @param s 입력받은 날짜 String
 * @param t day 생략
 * @returns {Date}
 */
function stringToDate(s, t) {
    const y = s.substring(0, 4);
    const m = s.substring(4, 6);
    const d = s.substring(6);
    if (t) {
        return new Date(y, m, null);
    }
    return new Date(y, m - 1, d);
}