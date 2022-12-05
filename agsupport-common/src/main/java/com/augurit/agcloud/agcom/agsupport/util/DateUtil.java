package com.augurit.agcloud.agcom.agsupport.util;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 时间格式转换类
 *
 * @autherAdmin Deram Zhao
 * @creat 2017/8/25
 */
public class DateUtil {
    //时间格式常量
    public static final DateFormat FORMAT1 = new SimpleDateFormat("yyyy-MM-dd");
    public static final DateFormat FORMAT2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final DateFormat FORMAT3 = new SimpleDateFormat("yyyyMMddHHmmss");
    public static final DateFormat FORMAT4 = new SimpleDateFormat("yyyy-MM");
    public static final DateFormat FORMAT5 = new SimpleDateFormat("HH:mm:ss");

    /**
     * 将字符串类型转换为日期类型
     * @param format
     * @param dateStr
     * @return Date
     * @throws ParseException
     */
    public static Date parse(DateFormat format,String dateStr) throws ParseException {
        if ("".equals(dateStr)|| null==dateStr)
            return null;
        return format.parse(dateStr);
    }

    /**
     * 将日期类型转化为字符串类型
     * @param format
     * @param date
     * @return String
     */
    public static String formatDate(DateFormat format,Date date){
        if (null==date)
            return null;
        return format.format(date);
    }
}
