package com.e4motion.challenge.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeUtils {

    private static final String dateTimeFormat = "yyyy-MM-dd HH:mm:ss";
    private static final String dateFormat = "yyyy-MM-dd";

    public static Date parseDateTime(String dateTime) throws ParseException {
        return new SimpleDateFormat(dateTimeFormat).parse(dateTime);
    }

    public static String formatDateTime(Date dateTime) {
        return new SimpleDateFormat(dateTimeFormat).format(dateTime);
    }

    public static Date parseDate(String date) throws ParseException {
        return new SimpleDateFormat(dateFormat).parse(date);
    }

    public static String formatDate(Date date) {
        return new SimpleDateFormat(dateFormat).format(date);
    }

}
