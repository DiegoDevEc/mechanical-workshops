package com.mechanical.workshops.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DateUtil {

    private static final String PATTERN_DATE = "dd-MM-yyyy";
    private static final String PATTERN_TIME = "h:mm a";

    public static LocalDateTime convertDateToString(String dateString, String timeString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(PATTERN_DATE);
        LocalDate date = LocalDate.parse(dateString, formatter);

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern(PATTERN_TIME);
        LocalTime time = LocalTime.parse(timeString.toUpperCase(), timeFormatter);

        return LocalDateTime.of(date, time);
    }

    public static LocalDate parseDate(String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(PATTERN_DATE);
        return LocalDate.parse(dateString, formatter);
    }

    public static LocalTime parseTime(String timeString) {
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern(PATTERN_TIME);
        return LocalTime.parse(timeString.toUpperCase(), timeFormatter);
    }

}
