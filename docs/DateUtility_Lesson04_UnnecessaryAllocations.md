# Lesson 4: Unnecessary Allocations
Unnecessarily invoking a constructor, resulting in a call to malloc and potentially other even more expensive functions, such as retrieving the time, is a sure-fire way to reduce your program's performance. In the original code, we can find many examples similar to the code below, where the `Date` object `yearMonthDate` is initialized with a new `Date()` object, which in addition to calling malloc results in also calling `System.currentTimeMillis()`, and then, just a few short lines later, the field `yearMonthDate` is overwritten with the result of parsing the String `dateString`. 

## V0 Code
```java
    public static Date formatDate(String dateString) throws ParseException {
        Date yearMonthDate = new Date();

        String format = "yyyy-MM-dd HH:mm:ss.SSS'Z'";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
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
```

## Revised Code
The fix is quite easy, simply revise the code by delaying the specification of the `yearMonthDate` field when it is necessary.
```java
    public static Date formatDate(String dateString) throws ParseException {
        String format = "yyyy-MM-dd HH:mm:ss.SSS'Z'";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        Date yearMonthDate = simpleDateFormat.parse(dateString);

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
```

Alternatively, we could declare the field with a `null` value, but an even better fix would be to declare this field as `final`.
```java
    public static Date formatDate(String dateString) throws ParseException {
        String format = "yyyy-MM-dd HH:mm:ss.SSS'Z'";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        final Date yearMonthDate = simpleDateFormat.parse(dateString);

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
```