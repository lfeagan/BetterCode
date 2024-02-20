package com.github.lfeagan;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateUtilityV1 {

    public static final String YEAR_MONTH_DAY = "yyyy-MM-dd";
    public static final String HOURS_MINUTES_SECONDS = "HH:mm:ss";

    public static final String GMT = "GMT";

    // https://en.wikipedia.org/wiki/ISO_8601#Combined_date_and_time_representations
    private static final String RFC3339_DATE_FORMAT_STRING =  "yyyy-MM-dd HH:mm:ss.SSS'Z'";
    private static final String ISO8601_DATE_FORMAT_STRING =  "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    /**
     * To fetch the given time without any rounded value
     * 
     * @param dateString
     * @return
     * @throws ParseException
     */
    public static Date formatDate(String dateString) throws ParseException {
        Date yearMonthDate = new Date();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(RFC3339_DATE_FORMAT_STRING);
        yearMonthDate = simpleDateFormat.parse(dateString);

        Calendar cal = Calendar.getInstance();
        cal.setTime(yearMonthDate);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int date = cal.get(Calendar.DAY_OF_MONTH);
        month += 1;
        Date datetime = new DateTime(year, month, date, cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE), cal.get(Calendar.SECOND), DateTimeZone.UTC).toDate();
        return datetime;
    }

    /**
     * To fetch the rounded time for the given query
     * 
     * @param dateString
     * @return
     * @throws ParseException
     */
    public static Date formatDateRounded(String dateString) throws ParseException {
        Date yearMonthDate = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(RFC3339_DATE_FORMAT_STRING);
        yearMonthDate = simpleDateFormat.parse(dateString);

        Calendar cal = Calendar.getInstance();
        cal.setTime(yearMonthDate);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int date = cal.get(Calendar.DAY_OF_MONTH);
        month += 1;
        Date datetime = new DateTime(year, month, date, 0, 0, 0, DateTimeZone.UTC).toDate();
        return datetime;
    }

    /**
     * Returns number of days between the given start and end date
     * 
     * @param startDate
     * @param endDate
     * @return
     * @throws ParseException
     */
    public static long getDays(String startDate, String endDate) throws ParseException {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat();

        if (startDate.contains("/"))
            simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        else
            simpleDateFormat = new SimpleDateFormat(RFC3339_DATE_FORMAT_STRING);

        Date startDateCov = simpleDateFormat.parse(startDate);
        Date endDateCov = simpleDateFormat.parse(endDate);
        // Getting the default zone id
        ZoneId defaultZoneId = ZoneId.systemDefault();

        // Converting the date to Instant
        Instant startInstant = startDateCov.toInstant();
        Instant endInstamt = endDateCov.toInstant();

        // Converting the Date to LocalDate
        LocalDate dateBefore = startInstant.atZone(defaultZoneId).toLocalDate();
        LocalDate dateAfter = endInstamt.atZone(defaultZoneId).toLocalDate();
        long noOfDaysBetween = ChronoUnit.DAYS.between(dateBefore, dateAfter);

        LocalTime startTime = startInstant.atZone(defaultZoneId).toLocalTime();
        LocalTime endTime = endInstamt.atZone(defaultZoneId).toLocalTime();
        long noOfHoursBetween = ChronoUnit.HOURS.between(startTime, endTime);
        noOfHoursBetween = noOfHoursBetween + ((noOfDaysBetween) * 24);

        return noOfHoursBetween;
    }

    public static String parseDateTimeFromJson(String date) {
        if (date.indexOf("-") > 0) {
            date = date.replace("-", " ");
        }
        if (date.indexOf(",") > 0) {
            date = date.replace(",", " ");
        }

        return date;
    }

    public static String convertDateToString(Date date) {
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        ZonedDateTime ldt = date.toInstant().atZone(ZoneId.of("UTC"));
        return ldt.format(dateFormat);
    }

    public static Date parseDateTimeFromJson(String dateTime, String format) {
        Date parsedDate = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        try {
            parsedDate = simpleDateFormat.parse(dateTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return parsedDate;
    }

    public static Date frameQueryForDateForMobile(String date, String key) {
        String format = "";
        Date dateTime = null;
        if (date.indexOf("-") > 0) {
            date = date.replace("-", " ");
        }
        if (date.indexOf(",") > 0) {
            date = date.replace(",", " ");
        }
        if (date.indexOf(":") > 0) {
            format = "MM/dd/yyyy HH:mm:ss";
            Calendar cal = Calendar.getInstance();
            Date yearMonthDate = parseDateTimeFromJson(date, format);
            cal.setTime(yearMonthDate);
            int year = cal.get(Calendar.YEAR);
            int monthNumber = cal.get(Calendar.MONTH);
            int dateNumber = cal.get(Calendar.DAY_OF_MONTH);
            monthNumber += 1;
            dateTime = new DateTime(year, monthNumber, dateNumber, cal.get(Calendar.HOUR_OF_DAY),
                    cal.get(Calendar.MINUTE), cal.get(Calendar.SECOND), DateTimeZone.UTC).toDate();
        } else {
            format = "MM/dd/yyyy";
            Calendar cal = Calendar.getInstance();
            Date yearMonthDate = parseDateTimeFromJson(date, format);
            cal.setTime(yearMonthDate);
            int year = cal.get(Calendar.YEAR);
            int monthNumber = cal.get(Calendar.MONTH);
            int dateNumber = cal.get(Calendar.DAY_OF_MONTH);
            monthNumber += 1;
            if (key.equalsIgnoreCase("from_date")) {
                dateTime = new DateTime(year, monthNumber, dateNumber, 0, 0, DateTimeZone.UTC)
                        .toDate();
            } else if (key.equalsIgnoreCase("to_date")) {
                dateTime = new DateTime(year, monthNumber, dateNumber, 23, 59, DateTimeZone.UTC)
                        .toDate();
            }
        }
        return dateTime;
    }

    public static Date removeMinutesFromDate(String date) {
        if (date.indexOf("-") > 0) {
            date = date.replace("-", " ");
        }
        if (date.indexOf(",") > 0) {
            date = date.replace(",", " ");
        }
        String format = "MM/dd/yyyy HH:mm:ss";
        Date dateTime = null;
        Calendar cal = Calendar.getInstance();
        Date yearMonthDate = parseDateTimeFromJson(date, format);
        cal.setTime(yearMonthDate);
        int year = cal.get(Calendar.YEAR);
        int monthNumber = cal.get(Calendar.MONTH);
        int dateNumber = cal.get(Calendar.DAY_OF_MONTH);
        monthNumber += 1;
        dateTime = new DateTime(year, monthNumber, dateNumber, cal.get(Calendar.HOUR_OF_DAY), 0, 0,
                DateTimeZone.UTC).toDate();
        return dateTime;
    }

    public static Date removeTimeFromDate(String date) {
        if (date.indexOf("-") > 0) {
            date = date.replace("-", " ");
        }
        if (date.indexOf(",") > 0) {
            date = date.replace(",", " ");
        }
        String format = "MM/dd/yyyy HH:mm:ss";
        Date dateTime = null;
        Calendar cal = Calendar.getInstance();
        Date yearMonthDate = parseDateTimeFromJson(date, format);
        cal.setTime(yearMonthDate);
        int year = cal.get(Calendar.YEAR);
        int monthNumber = cal.get(Calendar.MONTH);
        int dateNumber = cal.get(Calendar.DAY_OF_MONTH);
        monthNumber += 1;
        dateTime = new DateTime(year, monthNumber, dateNumber, 0, 0, 0, DateTimeZone.UTC).toDate();
        return dateTime;
    }


    /**
     * @param messageTime
     * @param format
     * @return
     */
    public static String parseDateTimeFromStringUTC(Date messageTime, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone(GMT));
        String yearMonthDate = simpleDateFormat.format(messageTime);
        return yearMonthDate;
    }

    public static LocalDateTime parseDateTimeFromString(String dateTime, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        try {
            return simpleDateFormat.parse(dateTime).toInstant().atZone(ZoneId.of("UTC"))
                    .toLocalDateTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Date getDateTimeFromString(String dateTime) {
        if (dateTime.indexOf("-") > 0) {
            dateTime = dateTime.replace("-", " ");
        }
        if (dateTime.indexOf(",") > 0) {
            dateTime = dateTime.replace(",", " ");
        }
        LocalDateTime localDateTime = parseDateTimeFromString(dateTime, "MM/dd/yyyy hh:mm:ss");
        return new DateTime(localDateTime.getYear(), localDateTime.getMonthValue(),
                localDateTime.getDayOfMonth(), localDateTime.getHour(), localDateTime.getMinute(),
                localDateTime.getSecond(), DateTimeZone.UTC).toDate();
    }

    public static LocalDateTime parseDateTimeFromString(Date messageTime, String format) {
        String yearMonthDate = null;

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        yearMonthDate = simpleDateFormat.format(messageTime);
        try {
            return simpleDateFormat.parse(yearMonthDate).toInstant().atZone(ZoneId.of("UTC"))
                    .toLocalDateTime();
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public static Object formatDateRoundedMonthWise(String dateString) throws ParseException {
        Date yearMonthDate = new Date();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(RFC3339_DATE_FORMAT_STRING);
        yearMonthDate = simpleDateFormat.parse(dateString);

        Calendar cal = Calendar.getInstance();
        cal.setTime(yearMonthDate);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int date = cal.get(Calendar.DAY_OF_MONTH);
        month += 1;
        Date datetime = new DateTime(year, month, 01, 0, 0, 0, DateTimeZone.UTC).toDate();
        return datetime;
    }

    public static Instant parseDateTime(String dateTimeString) throws IllegalArgumentException {
        final String pattern;
        if (dateTimeString.contains("/")) {
            pattern = "MM/dd/yyyy-HH:mm:ss";
        } else {
            pattern = RFC3339_DATE_FORMAT_STRING;
        }

        DateTimeFormatter formatter = new DateTimeFormatterBuilder().appendPattern(pattern)
                .parseDefaulting(ChronoField.NANO_OF_SECOND, 0).toFormatter()
                .withZone(ZoneId.of("UTC"));

        try {
            return formatter.parse(dateTimeString, Instant::from);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException(
                    MessageFormat.format("Invalid date format - {0}", dateTimeString));
        }
    }

    /**
     * This method converts a String date into a java.time.Instant date. This method is mainly used in time-series APIs
     * 
     * @param date
     * @return
     * @throws ParseException
     */
    public static Instant convertToInstantDateFormat(String date) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        Date formattedDate = simpleDateFormat.parse(date);
        return formattedDate.toInstant();
    }

    /**
     * This method is mainly used in time-series APIs
     * 
     * @param date
     * @return
     */
    public static String convertToDateFormatString(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.ENGLISH);
        return simpleDateFormat.format(date);
    }

   
    /**
     * An enum used by the method 'getInfoFromMessageTime'
     */
    public enum DateTimeEnum {
        DATE, TIME
     }

    /**
     * This method is mainly used in time-series APIs
     * 
     * @param messageTime
     * @return
     * 
     * Sample date format: "2023-03-13T09:00:00.000Z"
     * @throws ParseException
     */
    public static String getInfoFromMessageTime(String messageTime, DateTimeEnum dateTime) throws ParseException{
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date d = dateFormatter.parse(messageTime);
        DateFormat date = new SimpleDateFormat(YEAR_MONTH_DAY);
        DateFormat time = new SimpleDateFormat(HOURS_MINUTES_SECONDS);
        return (dateTime == DateTimeEnum.DATE) ? date.format(d) : time.format(d);
    }

 }
