package com.example.vacationplanner;

public class SecurityUtil {

    // Sanitizes text input
    public static String sanitize(String input) {
        if (input == null) return "";

        input = input.trim();

        input = input.replaceAll("[<>\"';]", "");

        if (input.length() > 100) {
            input = input.substring(0, 100);
        }

        return input;
    }
}
