package com.xcdh.target.util;

public class Constant {

    public static String SYSTEM_NAME = "ark";
    public static String SYSTEM_DOMAIN = SYSTEM_NAME + ".biyao.com";

    // redis前缀
    public static String REDIS_PREFIX = "ark:";

    // 时间常量
    public static final int DAY_SECONDS = 3600 * 24;
    public static final int WEEK_SECONDS = DAY_SECONDS * 7;
    public static final int MONTH_SECONDS = DAY_SECONDS * 30;
    public static final int DAY_3_SECONDS = DAY_SECONDS * 3;
    public static final int DAY_7_SECONDS = WEEK_SECONDS;
    public static final int DAY_15_SECONDS = DAY_SECONDS * 15;
    public static final int DAY_2_SECONDS = DAY_SECONDS * 2;

    public static final int SECONDS_10 = 10;
    public static final int MINUTES_SECONDS = 60;
    public static final int MINUTES_5_SECONDS = MINUTES_SECONDS * 5;
    public static final int MINUTES_30_SECONDS = MINUTES_SECONDS * 30;
    public static final int SECOND_MILLISECOND = 1000;
    public static final int YEAR_DAYS = 365;


}
