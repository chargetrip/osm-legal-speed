package com.chargetrip.osmLegalSpeed.expression.parser;

import com.chargetrip.osmLegalSpeed.expression.OperationType;
import com.chargetrip.osmLegalSpeed.expression.operation.DateTimeOperation;
import com.chargetrip.osmLegalSpeed.expression.operation.SunTimeOperation;
import com.chargetrip.osmLegalSpeed.expression.operation.TimeOperation;
import com.chargetrip.osmLegalSpeed.util.StringCursor;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TimeParser {
    /**
     * The string cursor used to parse the input value
     */
    public final StringCursor cursor;

    public TimeParser(String value) {
        this.cursor = new StringCursor(value);
    }

    /**
     * Parse all the time expressions: sun time and time in day/week/month
     *
     * @return The time operation
     * @throws ParseException In case expression cannot be parsed
     */
    public TimeOperation parse() throws ParseException {
        if (matches(OperationType.sunTime)) {
            return extractSunTime();
        }
        if (matches(OperationType.dateTime)) {
            return extractDateTime();
        }

        throw new ParseException("Invalid time operation", 0);
    }

    /**
     * Check if a pattern matches
     *
     * @param pattern The pattern to check
     * @return If is matches with values
     */
    protected boolean matches(Pattern pattern) {
        Matcher dateMatcher = pattern.matcher(cursor.value);
        if (dateMatcher.find()) {
            for (int i = 0; i < dateMatcher.groupCount(); i++) {
                if (dateMatcher.group(i) != null && !dateMatcher.group(i).equalsIgnoreCase("")) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Extract the sun operations
     *
     * @return The matchable operation
     * @throws ParseException In case expression cannot be parsed
     */
    protected TimeOperation extractSunTime() throws ParseException {
        Matcher sunsetMatcher = OperationType.sunsetTime.matcher(cursor.value);
        if (!sunsetMatcher.find()) {
            throw new ParseException("Cannot parse sunset", 0);
        }
        Matcher sunriseMatcher = OperationType.sunriseTime.matcher(cursor.value);
        if (!sunriseMatcher.find()) {
            throw new ParseException("Cannot parse sunrise", 0);
        }

        String sunsetOffset = sunsetMatcher.group().replaceAll("(sunset)?", "");
        String sunriseOffset = sunriseMatcher.group().replaceAll("(sunrise)?", "");

        return new SunTimeOperation(
                getSunOffset(sunsetOffset),
                getSunOffset(sunriseOffset),
                cursor.value.indexOf("sunset") < cursor.value.indexOf("sunrise")
        );
    }

    /**
     * Extract the sun offsets from an input value
     *
     * @param value The input value
     * @return The sun offset
     */
    protected SunTimeOperation.SunOffset getSunOffset(String value) {
        if (value.isEmpty()) {
            return new SunTimeOperation.SunOffset(false, 0, 0);
        }

        boolean negative = value.charAt(0) == '-';
        value = value.substring(1);

        String[] valueTime = value.split(":");

        return new SunTimeOperation.SunOffset(negative, Integer.parseInt(valueTime[0]), Integer.parseInt(valueTime[1]));
    }

    /**
     * Extract date and time operations
     *
     * @return The matchable operation
     */
    protected TimeOperation extractDateTime() {
        int startMonth = 0;
        int endMonth = 0;
        int startWeek = -1;
        int endWeek = -1;
        LocalDateTime startTime = null;
        LocalDateTime endTime = null;

        Matcher monthMatcher = OperationType.dateMonthTime.matcher(cursor.value.toLowerCase());
        if (monthMatcher.find()) {
            startMonth = DateTimeOperation.getMonthIndex(monthMatcher.group(1));
            endMonth = DateTimeOperation.getMonthIndex(monthMatcher.group(2));
        }

        Matcher weekMatcher = OperationType.dateWeekTime.matcher(cursor.value.toLowerCase());
        if (weekMatcher.find()) {
            startWeek = DateTimeOperation.getWeekIndex(weekMatcher.group(1));
            endWeek = DateTimeOperation.getWeekIndex(weekMatcher.group(2));
        }

        Matcher timeMatcher = OperationType.dateTimeTime.matcher(cursor.value.toLowerCase());
        if (timeMatcher.find()) {
            startTime = LocalDateTime.now() //
                    .withHour(this.getValidHour(Integer.parseInt(timeMatcher.group(1)))) //
                    .withMinute(this.getValidHour(Integer.parseInt(timeMatcher.group(2)))) //
            ;
            endTime = LocalDateTime.now() //
                    .withHour(this.getValidHour(Integer.parseInt(timeMatcher.group(3)))) //
                    .withMinute(this.getValidHour(Integer.parseInt(timeMatcher.group(4)))) //
            ;

            if (endTime.isBefore(startTime)) {
                // Make sure end time is after start time
                endTime = endTime.plusDays(1);
            }
        }

        return new DateTimeOperation(
                startMonth,
                endMonth,
                startWeek,
                endWeek,
                startTime,
                endTime
        );
    }

    /**
     * Determine the correct hour value between 0 and 23 by adding or subtracting 24 from exterior interval
     *
     * @param hour The original hour value
     * @return The correct hour value
     */
    protected int getValidHour(int hour) {
        if (hour > 23) {
            while (hour > 23) {
                hour -= 24;
            }
        }

        return hour;
    }
}
