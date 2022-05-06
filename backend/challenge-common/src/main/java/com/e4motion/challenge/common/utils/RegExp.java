package com.e4motion.challenge.common.utils;

public class RegExp {

    public static final String dateTime = "(\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2})";

    public static final String date = "(\\d{4}-\\d{2}-\\d{2})";

    public static final String phone = "^[0-9]{2,3}[0-9]{3,4}[0-9]{4}";

    public static final String emptyOrPhone = "^$|"+ phone;

}
