package com.fourward.linkchart.util;

import lombok.SneakyThrows;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
    public static String date(String date, int x, int k) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Calendar c1 = Calendar.getInstance();
        Date date1 = sdf.parse(date);
        c1.setTime(date1);
        c1.add(x, k);

        return sdf.format(c1.getTime());
    }

    public static long compare(String date1, String date2) throws ParseException {

        Date format1 = new SimpleDateFormat("yyyyMMdd").parse(date1);
        Date format2 = new SimpleDateFormat("yyyyMMdd").parse(date2);

        return format1.getTime() - format2.getTime();
    }

    public static String getNowDate() {
        LocalDate now = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

        return now.format(formatter);
    }

    /**
     * 날짜, 시간 출력하기
     *
     * @param fm 날짜 출력 형식
     * @return date
     */
    public static String getDateTime(String fm) {

        Date today = new Date();
        SimpleDateFormat date = new SimpleDateFormat(fm);

        return date.format(today);
    }

    /**
     * 날짜, 시간 출력하기
     *
     * @return 기본값은 년.월.일
     */
    public static String getDateTime() {

        return getDateTime("yyyy.MM.dd");
    }

    /**
     * Unix UTC 타입의 날짜, 시간 출력하기
     *
     * @param time 시간
     * @return date
     */
    public static String getLongDateTime(Object time) {

        return getLongDateTime(time, "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * Unix UTC 타입의 날짜, 시간 출력하기
     *
     * @param time 시간
     * @return date
     */
    public static String getLongDateTime(Integer time) {

        return getLongDateTime(time, "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * Unix UTC 타입의 날짜, 시간 출력하기
     *
     * @param time 시간
     * @param fm   날짜 출력 형식
     * @return date
     */
    public static String getLongDateTime(Object time, String fm) {

        return getLongDateTime((Integer) time, fm);
    }

    /**
     * Unix UTC 타입의 날짜, 시간 출력하기
     *
     * @param time 시간
     * @param fm   날짜 출력 형식
     * @return date
     */
    public static String getLongDateTime(Integer time, String fm) {
        Instant instant = Instant.ofEpochSecond(time);

        return DateTimeFormatter.ofPattern(fm).withZone(ZoneId.systemDefault()).format(instant);
    }
}