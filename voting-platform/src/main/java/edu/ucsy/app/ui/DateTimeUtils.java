package edu.ucsy.app.ui;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public abstract class DateTimeUtils {
    private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("HH:mm:ss");
    private static final DateTimeFormatter DF = DateTimeFormatter.ofPattern("dd-MM-yy");

    public static String formatTime(LocalDateTime time) {
        return format(time, DTF);
    }

    public static String formatDate(LocalDateTime dateTime) {
        return format(dateTime, DF);
    }

    private static String format(LocalDateTime dateTime, DateTimeFormatter formatter) {
        if(dateTime == null) {
            return "";
        }
        return dateTime.format(formatter);
    }

}
