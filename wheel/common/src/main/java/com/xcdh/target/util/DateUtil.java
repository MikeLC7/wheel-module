package com.xcdh.target.util;

import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 日期工具类
 *
 * @author wangbo
 * @version 1.0 2017/8/22
 */
public class DateUtil {
    public static final String YMD_FORMAT = "yyyyMMdd";
    public static final String MINUTE_FORMAT = "yyyyMMddHHmm";

    public static final String YYYYMM_FORMAT = "yyyy-MM";
    public static final String DATETIME_FORMAT = "yyyy-MM-dd";
    public static final String DAY_FORMAT = "yyyy/MM/dd";

    public static final String ISO_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String ISO_DATETIME_HOUR_FORMAT = "yyyy-MM-dd HH";
    public static final String ISO_DATETIME_MINUTE_FORMAT = "yyyy-MM-dd HH:mm";


    public static Date getNowDate(){
        return new Date();
    }

    public static String format(Date date) {
        return format(date, ISO_DATETIME_FORMAT);
    }

    public static String formatDate(Date date) {
        return format(date, DATETIME_FORMAT);
    }

    public static String format(Date date, String pattern) {
        if (date == null) {
            return null;
        }
        return new SimpleDateFormat(pattern).format(date);
    }

    public static String format(Calendar date, String pattern) {
        return format(date.getTime(), pattern);
    }

    public static Date parse(String dateStr) {
        SimpleDateFormat formatter = new SimpleDateFormat(ISO_DATETIME_FORMAT);
        try {
            return formatter.parse(dateStr);
        } catch (ParseException e) {
            return null;
        }
    }

    public static Date parse(String dateStr, String pattern) {
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        try {
            return formatter.parse(dateStr);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 是否在时间间隔内
     *
     * @param startTime
     * @param stopTime
     * @param compareTime
     * @return
     */
    public static boolean inDateTimeFrame(Date startTime, Date stopTime, Date compareTime) {
        return compareTime.after(startTime) && compareTime.before(stopTime);
    }

    /**
     * 返回时间年份+第几个自然周
     *
     * @param startTime
     * @return
     */
    public static int getYearAndWeekOfYear(Date startTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setTime(startTime);

        int week = calendar.get(Calendar.WEEK_OF_YEAR);
        int year = calendar.get(Calendar.YEAR);
        return Integer.parseInt(year + String.format("%02d", week));
    }

    /**
     * 从年份+第几个自然周获取date 日时分秒按targetDate来
     *
     * @param time
     * @return
     */
    public static Date getDateFromWeekOfYear(int time) {
        int year = time / 100;
        int week = time - year * 100;
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.WEEK_OF_YEAR, week);
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.add(Calendar.WEEK_OF_YEAR, -1);

        return calendar.getTime();
    }

    /**
     * 日期相加，返回输入日期X天后的日期
     */
    public static Date addDays(Date startTime, int day) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(startTime);
        cal.add(Calendar.DATE, day);
        return cal.getTime();
    }

    /**
     * 秒相加
     */
    public static Date addSeconds(Date startTime, int seconds) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(startTime);
        cal.add(Calendar.SECOND, seconds);
        return cal.getTime();
    }

    /**
     * 小时相加
     */
    public static Date addHours(Date startTime, int hours) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(startTime);
        cal.add(Calendar.HOUR, hours);
        return cal.getTime();
    }

    /**
     * 获取日期年月 默认格式 YYYY-MM
     */
    public static String getYearMonth(Date dateTime) {
        return getYearMonth(dateTime, null);
    }

    /**
     * 获取日期年月 带格式
     */
    public static String getYearMonth(Date dateTime, String dateFormat) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateTime);
        if (StringUtils.isBlank(dateFormat)) {
            return new SimpleDateFormat(YYYYMM_FORMAT).format(calendar.getTime());
        }
        return new SimpleDateFormat(dateFormat).format(calendar.getTime());
    }

    /**
     * 获取上年度某天. "20170101"
     */
    public static String getDayOfLastYear(int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -1);
        int year = calendar.get(Calendar.YEAR);
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        calendar.roll(Calendar.DAY_OF_YEAR, day);
        return new SimpleDateFormat(YMD_FORMAT).format(calendar.getTime());
    }

    /**
     * 获取上年度某天. "2017" year - 1
     */
    public static int getYear(int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, year);
        return calendar.get(Calendar.YEAR);
    }

    /**
     * 获取T + n 自然天的日期
     *
     * @param startTime
     * @param days
     * @return
     */
    public static Date getCalendarDays(Date startTime, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startTime);
        calendar.add(Calendar.DATE, days + 1);
        calendar.add(Calendar.SECOND, -1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * 获取上个月最后一天
     *
     * @return
     */
    public static Date getLastMonthLastDay() {
        Calendar cale = Calendar.getInstance();
        cale.set(Calendar.DAY_OF_MONTH, 0);
        return cale.getTime();
    }

    /**
     * 获取指定日期向前推12个自然月 2017-04-01——2018-03-31
     *
     * @return
     */
    public static Date getLastYear() {
        Calendar cale = Calendar.getInstance();
        cale.set(Calendar.MONTH, cale.get(Calendar.MONTH) - 12);
        cale.set(Calendar.DAY_OF_MONTH, 1);
        return cale.getTime();
    }

    /**
     * 获取下一个自然年的起始时间
     *
     * @return
     */
    public static Date getNextCalendarYear() {
        Calendar cale = Calendar.getInstance();
        cale.add(Calendar.MONTH, 12);
        cale.set(Calendar.MONTH, 0);
        cale.set(Calendar.DAY_OF_YEAR, 1);
        cale.set(Calendar.HOUR_OF_DAY, 0);
        cale.set(Calendar.MINUTE, 0);
        cale.set(Calendar.SECOND, 0);
        cale.set(Calendar.MILLISECOND, 0);
        return cale.getTime();
    }

    /**
     * 将传入的日期加1天
     *
     * @param dateStr 只接受格式为'yyyy-MM-dd'的日期类型，不符合格式的返回null
     * @return 日期加1的日期字符串 yyyy-MM-dd
     */
    public static String handleSearchEndDate(String dateStr) {
        if (StringUtils.isBlank(dateStr)) {
            return null;
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        Date date;
        try {
            date = format.parse(dateStr);
        } catch (Exception e) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, 1);
        return format.format(calendar.getTime());
    }


    /**
     * 与当前时间比较
     *
     * @return true：早于今天，false：晚于今天
     */
    public static boolean compareDate(String dateStr) {

        Date nowdate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        Date d = null;
        try {
            d = sdf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        boolean flag = d.before(nowdate);
        return flag;
    }

    /**
     * 将传入的日期加减n天
     *
     * @param dateStr 只接受格式为'yyyy-MM-dd'的日期类型，不符合格式的返回null
     * @return 日期加1的日期字符串 yyyy-MM-dd
     */
    public static String handleSearchEndNDate(String dateStr, int n) {
        if (StringUtils.isBlank(dateStr)) {
            return null;
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date;
        try {
            date = format.parse(dateStr);
        } catch (Exception e) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, n);
        return format.format(calendar.getTime());
    }

}
