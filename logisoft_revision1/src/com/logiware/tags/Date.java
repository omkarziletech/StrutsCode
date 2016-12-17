package com.logiware.tags;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 *
 * @author Thamizh
 */
public class Date {

    public static java.util.Date currentDate() {
        return new java.util.Date();
    }

    public static java.util.Date addDays(java.util.Date date, Integer days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days);
        return cal.getTime();
    }

    public static java.util.Date subtractDays(java.util.Date date, Integer days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, -days);
        return cal.getTime();
    }

    public static java.lang.String formatDate(java.util.Date date, java.lang.String pattern) {
        return new SimpleDateFormat(pattern).format(date);
    }
}
