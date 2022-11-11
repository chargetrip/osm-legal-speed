package com.chargetrip.osmLegalSpeed.expression;

import java.util.regex.Pattern;

public class OperationType {
    public static String OR = "or";
    public static String AND = "and";

    public static String NOT = "!";
    public static String EQUALS = "=";
    public static String NOT_EQUALS = "!=";
    public static String LIKE = "~";
    public static String NOT_LIKE = "!~";

    public static String PLACEHOLDER_START = "{";
    public static String PLACEHOLDER_END = "}";
    public static String PARENTHESES_START = "(";
    public static String QUOTE = "\"";
    public static String GREATER_THAN = ">";
    public static String LESS_THAN = "<";
    public static String GREATER_OR_EQUAL_THAN = ">=";
    public static String LESS_OR_EQUAL_THAN = "<=";

    public static Pattern sunTime = Pattern.compile("\\((\\(?(sunset|sunrise)([+-:0-9]{6})?\\)?)-(\\(?(sunset|sunrise)([+-:0-9]{6})?\\)?)\\)", Pattern.CASE_INSENSITIVE);
    public static Pattern sunsetTime = Pattern.compile("((sunset)([+-:0-9]{6})?)", Pattern.CASE_INSENSITIVE);
    public static Pattern sunriseTime = Pattern.compile("((sunrise)([+-:0-9]{6})?)", Pattern.CASE_INSENSITIVE);


    public static Pattern dateTime = Pattern.compile("(((jan|feb|mar|apr|may|jun|jul|aug|sep|oct|nov|dec)-(jan|feb|mar|apr|may|jun|jul|aug|sep|oct|nov|dec))?( )?((mo|tu|we|th|fr|sa|su)-(mo|tu|we|th|fr|sa|su))?( )?([0-9]{2}:[0-9]{2}-[0-9]{2}:[0-9]{2})?)", Pattern.CASE_INSENSITIVE);
    public static Pattern dateMonthTime = Pattern.compile("(jan|feb|mar|apr|may|jun|jul|aug|sep|oct|nov|dec)-(jan|feb|mar|apr|may|jun|jul|aug|sep|oct|nov|dec)", Pattern.CASE_INSENSITIVE);
    public static Pattern dateWeekTime = Pattern.compile("(mo|tu|we|th|fr|sa|su)-(mo|tu|we|th|fr|sa|su)", Pattern.CASE_INSENSITIVE);
    public static Pattern dateTimeTime = Pattern.compile("([0-9]{2}):([0-9]{2})-([0-9]{2}):([0-9]{2})", Pattern.CASE_INSENSITIVE);
}
