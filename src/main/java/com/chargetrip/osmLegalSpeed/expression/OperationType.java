package com.chargetrip.osmLegalSpeed.expression;

import java.util.regex.Pattern;

/**
 * Type of operations
 */
public class OperationType {
    /**
     * OR operation
     */
    public static String OR = "or";

    /**
     * AND operation
     */
    public static String AND = "and";

    /**
     * Not operation
     */
    public static String NOT = "!";

    /**
     * Equal operation
     */
    public static String EQUALS = "=";

    /**
     * Not equal operation
     */
    public static String NOT_EQUALS = "!=";

    /**
     * Like operation
     */
    public static String LIKE = "~";

    /**
     * Not like operation
     */
    public static String NOT_LIKE = "!~";

    /**
     * Placeholder start operation
     */
    public static String PLACEHOLDER_START = "{";

    /**
     * Placeholder end operation
     */
    public static String PLACEHOLDER_END = "}";

    /**
     * Parentheses start operation
     */
    public static String PARENTHESES_START = "(";

    /**
     * Quote operation
     */
    public static String QUOTE = "\"";

    /**
     * Greater than operation
     */
    public static String GREATER_THAN = ">";

    /**
     * Less than operation
     */
    public static String LESS_THAN = "<";

    /**
     * Greater or equal than operation
     */
    public static String GREATER_OR_EQUAL_THAN = ">=";

    /**
     * Less or equal than operation
     */
    public static String LESS_OR_EQUAL_THAN = "<=";

    /**
     * Regular expression for sun time operation
     */
    public static Pattern sunTime = Pattern.compile("\\((\\(?(sunset|sunrise)([+-:0-9]{6})?\\)?)-(\\(?(sunset|sunrise)([+-:0-9]{6})?\\)?)\\)", Pattern.CASE_INSENSITIVE);

    /**
     * Regular expression for sunset sun time operation
     */
    public static Pattern sunsetTime = Pattern.compile("((sunset)([+-:0-9]{6})?)", Pattern.CASE_INSENSITIVE);

    /**
     * Regular expression for sunrise sun time operation
     */
    public static Pattern sunriseTime = Pattern.compile("((sunrise)([+-:0-9]{6})?)", Pattern.CASE_INSENSITIVE);

    /**
     * Regular expression for time base operation
     */
    public static Pattern dateTime = Pattern.compile("(((jan|feb|mar|apr|may|jun|jul|aug|sep|oct|nov|dec)-(jan|feb|mar|apr|may|jun|jul|aug|sep|oct|nov|dec))?( )?((mo|tu|we|th|fr|sa|su)-(mo|tu|we|th|fr|sa|su))?( )?([0-9]{2}:[0-9]{2}-[0-9]{2}:[0-9]{2})?)", Pattern.CASE_INSENSITIVE);

    /**
     * Regular expression for month restriction operation
     */
    public static Pattern dateMonthTime = Pattern.compile("(jan|feb|mar|apr|may|jun|jul|aug|sep|oct|nov|dec)-(jan|feb|mar|apr|may|jun|jul|aug|sep|oct|nov|dec)", Pattern.CASE_INSENSITIVE);

    /**
     * Regular expression for days of week restriction operation
     */
    public static Pattern dateWeekTime = Pattern.compile("(mo|tu|we|th|fr|sa|su)-(mo|tu|we|th|fr|sa|su)", Pattern.CASE_INSENSITIVE);

    /**
     * Regular expression for time restriction operation
     */
    public static Pattern dateTimeTime = Pattern.compile("([0-9]{2}):([0-9]{2})-([0-9]{2}):([0-9]{2})", Pattern.CASE_INSENSITIVE);
}
