package com.gp.cong.common;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class DateUtils {

    public static Date parseToDate(String date) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        Date dateFormat = null;
        if (CommonUtils.isNotEmpty(date)) {
            dateFormat = (Date) sdf.parse(date.trim());
        }
        return dateFormat;
    }

    public static Date parseToDateForMonthMMM(String date) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        Date dateFormat = null;
        if (CommonUtils.isNotEmpty(date)) {
            dateFormat = (Date) sdf.parse(date.trim());
        }
        return dateFormat;
    }

    public static Date parseToDateForMonthMMMandTime(String date) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy HH:mm a");
        Date dateFormat = null;
        dateFormat = (Date) sdf.parse(date.trim());
        return dateFormat;
    }

    public static Date parseStringToDateWithTime(String date) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dateFormat = null;
        dateFormat = (Date) sdf.parse(date.trim());
        return dateFormat;
    }

    public static Date parseStringToDate(String date) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date dateFormat = null;
        dateFormat = (Date) sdf.parse(date.trim());
        return dateFormat;
    }

    public static Date parseToDateTime(String date) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm");
        Date dateFormat = null;
        dateFormat = (Date) sdf.parse(date);
        return dateFormat;
    }

    public static Date parseDate(String date, String format) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date parsedDate = null;
        if (CommonUtils.isNotEmpty(date)) {
            parsedDate = sdf.parse(date);
        }
        return parsedDate;
    }

    public static String formatDate(Date date, String format) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String formattedDate = null;
        if (null != date) {
            try {
                formattedDate = sdf.format(date);
            } catch (Exception e) {
                throw e;
            }
        }
        return formattedDate;
    }

    public static String parseDateToString(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        String dateFormat = null;
        if (null != date) {
            dateFormat = sdf.format(date);
        }
        return dateFormat;
    }

    public static String parseDateToDateTimeString(Date date) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
        String dateFormat = null;
        dateFormat = sdf.format(date);
        return dateFormat;
    }

    public static String formatDateTimeToDateTimeString(Date date) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String dateFormat = null;
        dateFormat = sdf.format(date);
        return dateFormat;
    }

    public static String formatDateToDateTimeString(Date date) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy hh:mm a");
        String dateFormat = null;
        dateFormat = sdf.format(date);
        return dateFormat;
    }

    public static String parseStringDateToMYSQLFormat(String date) throws Exception {
        Date stringDate = parseToDate(date);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateFormat = null;
        dateFormat = sdf.format(stringDate);
        return dateFormat;
    }

    public static String parseDateToMYSQLFormat(String strdate) throws Exception {
        SimpleDateFormat oldSdf = new SimpleDateFormat("dd-MMM-yyyy");
        SimpleDateFormat newSdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateFormat = null;
        Date date = new Date((oldSdf.parse(strdate)).getTime());
        dateFormat = newSdf.format(date);
        return dateFormat;
    }

    public static String parseToMYSQLFormat(String strDate) throws Exception {
        SimpleDateFormat oldSdf = new SimpleDateFormat("dd-MMM-yyyy");
        SimpleDateFormat newSdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String dateFormat = null;
        Date date = new Date((oldSdf.parse(strDate)).getTime() + TimeUnit.DAYS.toMillis(1));
        dateFormat = newSdf.format(date);
        return dateFormat;
    }

    public static String formatStringDateToAppFormat(Date date) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
        String dateFormat = null;
        dateFormat = sdf.format(date);
        return dateFormat;
    }

    public static String formatStringDateToAppFormatMMM(Date date) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        String dateFormat = null;
        dateFormat = sdf.format(date);
        return dateFormat;
    }

    public static Date formatDateAndParseToDate(Date date) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        Date dateFormat = null;
        dateFormat = sdf.parse(sdf.format(date));
        return dateFormat;
    }

    public static Date formatDateAndParseTo(Date date, String format) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date dateFormat = null;
        dateFormat = sdf.parse(sdf.format(date));
        return dateFormat;
    }

    /**
     * @param startDate
     * @param endDate
     * @return days
     */
    public static long daysBetween(Date startDate, Date endDate) throws Exception {
        Calendar start = Calendar.getInstance();
        start.setTime(startDate);
        Calendar end = Calendar.getInstance();
        end.setTime(endDate);
        return (end.getTimeInMillis() - start.getTimeInMillis()) / (24 * 60 * 60 * 1000);
    }

    public static int compareTo(Date date1, Date date2) {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(date1);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(date2);
        return calendar1.compareTo(calendar2);
    }

    public static int compareTo(String date1, String date2, String format1, String format2) throws Exception {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(parseDate(date1, format1));
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(parseDate(date1, format2));
        return calendar1.compareTo(calendar2);
    }

    public static Date formatDateAndParseDate(Date date) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
        Date dateFormat = null;
        dateFormat = sdf.parse(sdf.format(date));
        return dateFormat;
    }

    public static Date parseToDateForMonthMMMandTimeTT(String date) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
        Date dateFormat = null;
        dateFormat = (Date) sdf.parse(date.trim());
        return dateFormat;
    }

    public static String formatStringDateToTimeTT(Date date) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa");
        String dateFormat = null;
        dateFormat = sdf.format(date);
        return dateFormat;
    }

    public static String formatDateToDDMMMYYYYHHMM(Date date) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy HH:mm");
        String dateFormat = null;
        dateFormat = sdf.format(date);
        return dateFormat;
    }

    public static String formatDateToYYYYMMDD(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String parseDateFormated = null;
        if (date != null) {
            parseDateFormated = sdf.format(date);
        }
        return parseDateFormated;
    }

    public static String formatStringTime(Date date) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String dateFormat = null;
        dateFormat = sdf.format(date);
        return dateFormat;
    }

    public static Date addDays(Date date, Integer days) throws Exception {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, days);
        return calendar.getTime();
    }

    public static String formatDateToMMMMDDYYYY(Date date) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd, yyyy");
        String dateFormat = null;
        dateFormat = sdf.format(date);
        return dateFormat;
    }

    public static long getDateDiffByTotalDays(Date startDate, Date endDate) throws Exception {
        long timeDiff = Math.abs(startDate.getTime() - endDate.getTime());
        long totalDays = TimeUnit.MILLISECONDS.toDays(timeDiff);
        return totalDays;
    }

    public static long getDateDiffByTotalDaysWithNegative(Date startDate, Date endDate) throws Exception {
        startDate = removeTime(startDate);
        endDate =   removeTime(endDate);
        return startDate.compareTo(endDate);
    }

    public static String formateDateToDDMMMYYYY(String date) throws Exception {
        SimpleDateFormat oldFormat = new SimpleDateFormat("MM/dd/yyyy");
        SimpleDateFormat newFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date oldDate = oldFormat.parse(date);
        return newFormat.format(oldDate);
    }

    public static String addDay(String date, Integer day, String format1, String format2) throws Exception {
        SimpleDateFormat oldFormat = new SimpleDateFormat(format1);
        SimpleDateFormat newFormat = new SimpleDateFormat(format2);
        Date oldDate = oldFormat.parse(date);
        Calendar c = Calendar.getInstance();
        c.setTime(oldDate);
        c.add(Calendar.DATE, 1);
        return newFormat.format(c.getTime());
    }
    
    public static Date removeTime(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }
}
