package com.basejava.webapp.util;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

public class HtmlUtil {
    public static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("MM.yyyy");

    public static boolean isEmpty(String value) {
        return value == null || value.trim().length() == 0;
    }

    public static String formatDates(YearMonth yearMonth) {
        return yearMonth == null ? "" : yearMonth.format(TIME_FORMATTER);
    }
}
