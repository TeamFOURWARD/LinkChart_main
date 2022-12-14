package com.fourward.linkchart.util;

import java.util.Random;

public class RandomUtil {
    public static String getStr(int k) {
        Random random = new Random();
        int createNum = 0;
        String ranNum = "";
        String resultStr = "";
        while (k-- > 0) {
            createNum = random.nextInt(9);
            ranNum = Integer.toString(createNum);
            resultStr += ranNum;
        }
        return resultStr;
    }
}