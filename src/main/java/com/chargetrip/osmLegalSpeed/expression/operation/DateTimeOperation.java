package com.chargetrip.osmLegalSpeed.expression.operation;

import java.time.LocalDateTime;
import java.util.Map;

public class DateTimeOperation implements TimeOperation {
    public final int startMonth;
    public final int endMonth;
    public final int startWeek;
    public final int endWeek;
    public final LocalDateTime startTime;
    public final LocalDateTime endTime;

    public DateTimeOperation(
            int startMonth,
            int endMonth,
            int startWeek,
            int endWeek,
            LocalDateTime startTime,
            LocalDateTime endTime
    ) {
        this.startMonth = startMonth;
        this.endMonth = endMonth;
        this.startWeek = startWeek;
        this.endWeek = endWeek;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    @Override
    public boolean matches(Map<String, String> object) {
        if (object.containsKey("date")) {
            LocalDateTime date = LocalDateTime.parse(object.get("date"));

            return isValidMonth(date) && isValidWeek(date) && isValidTime(date);
        }

        return false;
    }

    @Override
    public String toString() {
        String month = startMonth > 0 && endMonth > 0 ? getMonthName(startMonth) + "-" + getMonthName(endMonth) : "";
        String week = startWeek > -1 && endWeek > -1 ? getWeekName(startWeek) + "-" + getWeekName(endWeek) : "";
        String time = startTime != null && endTime != null ? startTime.getHour() + ":" + startTime.getMinute() + "-" + endTime.getHour() + ":" + endTime.getMinute() : "";

        return "(" + (month + " " + week + " " + time).trim() + ")";
    }

    protected boolean isValidMonth(LocalDateTime date) {
        if (startMonth == 0 || endMonth == 0) {
            // We do not have month restriction
            return true;
        }

        int currentMonth = date.getMonth().getValue();

        if (startMonth > endMonth) {
            return currentMonth >= startMonth || currentMonth <= endMonth;
        }

        return startMonth <= currentMonth && currentMonth <= endMonth;
    }

    protected boolean isValidWeek(LocalDateTime date) {
        if (startWeek == -1 || endWeek == -1) {
            // We do not have day of week restriction
            return true;
        }

        int currentWeekDay = date.getDayOfWeek().getValue();

        if (startWeek > endWeek) {
            return currentWeekDay >= startWeek || currentWeekDay <= endWeek;
        }

        return startWeek <= currentWeekDay && currentWeekDay <= endWeek;
    }

    protected boolean isValidTime(LocalDateTime date) {
        if (startTime == null || endTime == null) {
            // We do not have time restriction
            return true;
        }

        LocalDateTime currentDatetime = date //
                .withYear(startTime.getYear()) //
                .withMonth(startTime.getMonthValue()) //
                .withDayOfMonth(startTime.getDayOfMonth()) //
        ;

        return (currentDatetime.isAfter(startTime) || currentDatetime.isEqual(startTime)) && (currentDatetime.isBefore(endTime) || currentDatetime.isEqual(endTime));
    }

    public static int getMonthIndex(String monthValue) {
        return switch (monthValue.toLowerCase()) {
            case "jan" -> 1;
            case "feb" -> 2;
            case "mar" -> 3;
            case "apr" -> 4;
            case "may" -> 5;
            case "jun" -> 6;
            case "jul" -> 7;
            case "aug" -> 8;
            case "sep" -> 9;
            case "oct" -> 10;
            case "nov" -> 11;
            case "dec" -> 12;
            default -> 0;
        };
    }

    public static String getMonthName(int monthIndex) {
        return switch (monthIndex) {
            case 1 -> "jan";
            case 2 -> "feb";
            case 3 -> "mar";
            case 4 -> "apr";
            case 5 -> "may";
            case 6 -> "jun";
            case 7 -> "jul";
            case 8 -> "aug";
            case 9 -> "sep";
            case 10 -> "oct";
            case 11 -> "nov";
            case 12 -> "dec";
            default -> "";
        };
    }

    public static int getWeekIndex(String weekValue) {
        return switch (weekValue.toLowerCase()) {
            case "mo" -> 0;
            case "tu" -> 1;
            case "we" -> 2;
            case "th" -> 3;
            case "fr" -> 4;
            case "sa" -> 5;
            case "su" -> 6;
            default -> -1;
        };
    }

    public static String getWeekName(int weekIndex) {
        return switch (weekIndex) {
            case 0 -> "mo";
            case 1 -> "tu";
            case 2 -> "we";
            case 3 -> "th";
            case 4 -> "fr";
            case 5 -> "sa";
            case 6 -> "su";
            default -> "";
        };
    }
}
