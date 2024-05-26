package com.example.chasechallenge.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Utils {

    public static String kelvinToFahrenheit(double kelvin) {
        return String.valueOf((int) ((kelvin - 273.15) * 9 / 5 + 32));
    }

    public static String formatDateTime(String dateTimeString) {
        if (dateTimeString == null || dateTimeString.isBlank() )
            return "";
        LocalDateTime dateTime = LocalDateTime.parse(dateTimeString, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        return dateTime.format(DateTimeFormatter.ofPattern("M/d h a"));
    }
}
