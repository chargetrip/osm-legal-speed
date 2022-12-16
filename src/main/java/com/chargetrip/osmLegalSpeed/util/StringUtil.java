package com.chargetrip.osmLegalSpeed.util;

public class StringUtil {
    /**
     * Trimming and removing quotes
     *
     * @param value The value to process
     * @return Escaped string
     */
    public static String stripAndUnescapeQuotes(String value) {
        return value.trim() //
                .replaceAll("\\\"", "")
                .replaceAll("\'","")
                .replaceAll("\\'","")
                .replaceAll("'", "")
                .trim();
    }
}
