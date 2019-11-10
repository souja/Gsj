package com.wangxiaobao.gsj.base;

import android.text.format.Time;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by candy on 18-3-12.
 */

public class TimeUtils {
    public static String getTime(long time) {

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);

        Calendar responseCalendar = Calendar.getInstance();
        responseCalendar.setTimeInMillis(time);

        int reponseYear = responseCalendar.get(Calendar.YEAR);


        SimpleDateFormat sp = new SimpleDateFormat("MM月dd日");

        if (year == reponseYear) {
            return sp.format(new Date(time));
        } else {
            return new SimpleDateFormat("yyyy年MM月dd日").format(new Date(time));
        }
    }

    public static String getDateString(Date date, String pattern) {
        return new SimpleDateFormat(pattern).format(date);
    }

    public static String getDateToString(long milSecond, String pattern) {
        Date date = new Date(milSecond);
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(date);
    }
    public static long getTimeDifference(String starTime, String endTime) {
        long days = 0;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        try {
            Date parse = dateFormat.parse(starTime);
            Date parse1 = dateFormat.parse(endTime);

            long diff = parse1.getTime() - parse.getTime();

            long day = diff / (24 * 60 * 60 * 1000);
            days = day;

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return days;

    }

    public static String getNowTime() {
        String timeString = null;
        Time time = new Time();
        time.setToNow();
        String year = thanTen(time.year);
        String month = thanTen(time.month + 1);
        String monthDay = thanTen(time.monthDay);

        timeString = year + "-" + month + "-" + monthDay + " ";
        // System.out.println("-------timeString----------" + timeString);
        return timeString;
    }

    /**
     * 十一下加零
     *
     * @param str
     * @return
     */
    public static String thanTen(int str) {

        String string = null;

        if (str < 10) {
            string = "0" + str;
        } else {

            string = "" + str;

        }
        return string;
    }


}
