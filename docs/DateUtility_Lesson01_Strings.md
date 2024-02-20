# Lesson 1: Strings
# DateUtility V0 --> V1
## Topics
1. [Java String Pool](https://www.baeldung.com/java-string-pool)
2. [Compile-Time Constants](https://www.baeldung.com/java-compile-time-constants)

The date-time format string is repeated in four of the functions present in this class. 
```java
String format = "yyyy-MM-dd HH:mm:ss.SSS'Z'";
```

Even though javac is "smart" enough to de-duplicate identical strings into the TEXT area
It is better to define static [compile-time constants](https://www.baeldung.com/java-compile-time-constants) explicitly.
```java
// https://en.wikipedia.org/wiki/ISO_8601#Combined_date_and_time_representations
private static final String RFC3339_DATE_FORMAT_STRING =  "yyyy-MM-dd HH:mm:ss.SSS'Z'";
private static final String ISO8601_DATE_FORMAT_STRING =  "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
```