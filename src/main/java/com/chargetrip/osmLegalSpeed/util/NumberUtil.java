package com.chargetrip.osmLegalSpeed.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NumberUtil {
    private static final Pattern feetInchRegex = Pattern.compile("([0-9]+)\\s*(?:'|ft)\\s*([0-9]+)\\s*(?:\"|in)");
    private static final Pattern withUnitRegex = Pattern.compile("([0-9]+|[0-9]*\\.[0-9]+)\\s*([a-z\'\"]+)");

    public static Double withOptionalUnitToDoubleOrNull(String value) {
        if (value.isEmpty()) {
            return null;
        }

        char first = value.charAt(0);
        if (!Character.isDigit(first) && first != '.') {
            return null;
        }

        char last = value.charAt(value.length()-1);
        if (!Character.isLetter(last) && last != '"' && last != '\'') {
            return Double.parseDouble(value);
        }

        Matcher feetInchResult = feetInchRegex.matcher(value);
        if (feetInchResult.find()) {
            int feet = Integer.parseInt(feetInchResult.group(1));
            int inches = Integer.parseInt(feetInchResult.group(2));

            return feet * toStandardUnitsFactor("ft") + inches * toStandardUnitsFactor("in");
        }

        Matcher withUnitResult = withUnitRegex.matcher(value);
        if (withUnitResult.find()) {
            double v = Double.parseDouble(withUnitResult.group(1));
            Double factor = toStandardUnitsFactor(withUnitResult.group(2));
            if (factor != null) {
                return v * factor;
            }
        }

        return null;
    }

    public static Double toStandardUnitsFactor(String unit) {
        return switch (unit) {
            // speed: to kilometers per hour
            case "km/h", "kph" -> 1.0;
            case "mph" -> 1.609344;
            // width/length/height: to meters
            case "m" -> 1.0;
            case "mm" -> 0.001;
            case "cm" -> 0.01;
            case "km" -> 1000.0;
            case "ft", "'" -> 0.3048;
            case "in", "\"" -> 0.0254;
            case "yd", "yds" -> 0.9144;
            // weight: to tonnes
            case "t" -> 1.0;
            case "kg" -> 0.001;
            case "st" -> 0.90718474; // short tons
            case "lt" -> 1.0160469; // long tons
            case "lb", "lbs" -> 0.00045359237;
            case "cwt" -> 0.05080234544; // imperial (=long) hundredweight. short cwt is not in use in road traffic
            default -> null;
        };
    }
}
