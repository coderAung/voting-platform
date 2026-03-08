package edu.ucsy.app.ui;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public abstract class DateTimeUtils {
    private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("HH:mm:ss");
    private static final DateTimeFormatter DF = DateTimeFormatter.ofPattern("dd-MM-yy");

    public static String format(LocalTime time) {
        return time.format(DTF);
    }

    public static String format(LocalDateTime dateTime) {
        if(dateTime == null) {
            return "";
        }
        return dateTime.format(DF);
    }

}
