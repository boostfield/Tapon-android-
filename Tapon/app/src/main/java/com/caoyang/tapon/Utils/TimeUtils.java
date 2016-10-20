package com.caoyang.tapon.Utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Administrator on 2016/06/13.
 */
public class TimeUtils {
    /**
     * 获取当前时间
     */
    public static String getCurrentTime() {
        SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date t = new Date();
        return df1.format(t);
    }


    /**
     * 获取当前时间-没有横线
     */
    public static String getCurrentTimeWithoutLine() {
        SimpleDateFormat df1 = new SimpleDateFormat("yyyyMMddHHmmss");
        Date t = new Date();
        return df1.format(t);
    }

    /**
     * 当前时间转成UTC格式
     *
     * @return
     */
    public static String getCurrentUTCTime() {
        SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        Date t = new Date();
        df1.setTimeZone(TimeZone.getTimeZone("UTC"));
        return df1.format(t);
    }

    /**
     * 进入时间 UTC时间 2016-09-19T07:13:56
     *
     * @param time
     * @return
     */
    public static String getTimeFormatNO1(String time) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DateFormat wantdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date current = new Date();
            Date tiemBefore = df.parse(time.substring(0, 19).replace("T", " "));
            String str = wantdf.format(tiemBefore);
            return str;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "null";
    }

    /**
     * 获取相差时间
     */
    public static String getTimeDifference(String time) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date current = new Date();
            Date tiemBefore = df.parse(time.substring(0, 19).replace("T", " "));
            long diff = (current.getTime() - (1000 * 8 * 60 * 60)) - tiemBefore.getTime();
            long days = diff / (1000 * 60 * 60 * 24);
            long hours = (diff - days * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
            long minutes = (diff - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60)) / (1000 * 60);
            long second = (diff - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60) - minutes * (1000 * 60)) / (60 * 60);
            if (days != 0) {
                return "" + days + "天前";
            } else if (hours != 0) {
                return "" + hours + "小时前";
            } else if (minutes != 0) {
                return "" + minutes + "分钟前";
            } else {
                return "刚刚";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getTImeStamp() {
        return System.currentTimeMillis() + "";

    }
}
