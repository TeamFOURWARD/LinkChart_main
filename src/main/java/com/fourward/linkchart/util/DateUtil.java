package com.fourward.linkchart.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
}