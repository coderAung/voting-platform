package edu.ucsy.app.ui;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public abstract class DateTimeUtils {
    private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("HH:mm:ss");

    public static String formatTime(LocalTime time) {
        return time.format(DTF);
    }
}
