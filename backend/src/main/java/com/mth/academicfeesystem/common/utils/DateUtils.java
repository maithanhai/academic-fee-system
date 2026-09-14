package com.mth.academicfeesystem.common.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtils {

    private DateUtils() {}  

    private static final DateTimeFormatter DEFAULT_PASSWORD_FORMAT = DateTimeFormatter.ofPattern("ddMMyyyy");
    private static final DateTimeFormatter DISPLAY_DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter DISPLAY_DATETIME_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    public static String toDefaultPassword(LocalDate dateOfBirth) {
        return dateOfBirth.format(DEFAULT_PASSWORD_FORMAT);
    }

    public static String toDisplayDate(LocalDate date) {
        return date == null ? null : date.format(DISPLAY_DATE_FORMAT);
    }

    public static String toDisplayDateTime(LocalDateTime dateTime) {
        return dateTime == null ? null : dateTime.format(DISPLAY_DATETIME_FORMAT);
    }
}