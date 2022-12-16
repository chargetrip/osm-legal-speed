package com.chargetrip.osmLegalSpeed.expression.parser;

import com.chargetrip.osmLegalSpeed.expression.OperationType;
import com.chargetrip.osmLegalSpeed.expression.operation.*;
import com.chargetrip.osmLegalSpeed.util.NumberUtil;
import com.chargetrip.osmLegalSpeed.util.StringCursor;
import com.chargetrip.osmLegalSpeed.util.StringUtil;

import java.text.ParseException;
import java.util.List;
import java.util.Optional;

/**
 * Parser for tag operations
 */
public class OperationParser {
    /**
     * List of all possible operators, sorted
     */
    private static final List<String> OPERATORS = List.of( //
            OperationType.GREATER_OR_EQUAL_THAN,
            OperationType.LESS_OR_EQUAL_THAN,
            OperationType.GREATER_THAN,
            OperationType.LESS_THAN,
            OperationType.NOT_EQUALS,
            OperationType.EQUALS,
            OperationType.NOT_LIKE,
            OperationType.LIKE
    );

    /**
     * List of all key-value operators
     */
    private static final List<String> KEY_VALUE_OPERATORS = List.of( //
            OperationType.EQUALS,
            OperationType.NOT_EQUALS,
            OperationType.LIKE,
            OperationType.NOT_LIKE
    );

    /**
     * List of all comparison operators
     */
    private static final List<String> COMPARISON_OPERATORS = List.of( //
            OperationType.GREATER_OR_EQUAL_THAN,
            OperationType.GREATER_THAN,
            OperationType.LESS_OR_EQUAL_THAN,
            OperationType.LESS_THAN
    );

    /**
     * String cursor used to parse the operation
     */
    public final StringCursor cursor;

    /**
     * Constructor for parser
     *
     * @param value The value to parse
     */
    public OperationParser(String value) {
        this.cursor = new StringCursor(value);
    }

    /**
     * Parse simple operation
     *
     * @throws ParseException In case the operation cannot be parsed
     * @return The tag operation
     */
    public TagOperation parse() throws ParseException {
        boolean negative = cursor.isNextAndAdvance(OperationType.NOT);
        boolean like = cursor.isNextAndAdvance(OperationType.LIKE);
        String cursorValue = cursor.value.substring(cursor.currentPosition);

        if (negative) {
            return this.extractNegativeOperation(like, cursorValue);
        }

        String operator = this.getOperator();
        if (operator == null) {
            return this.extractKeyOperation(like, cursorValue);
        }

        String[] members = cursorValue.split(operator);
        if (members.length < 2) {
            throw new ParseException("Operation has less members than expect", cursor.currentPosition);
        } else if (members.length > 2) {
            throw new ParseException("Operation has more members than expect", cursor.currentPosition);
        }

        String key = StringUtil.stripAndUnescapeQuotes(members[0]);
        String value = StringUtil.stripAndUnescapeQuotes(members[1]);

        cursor.advanceBy(key.length() + operator.length());

        if (like) {
            if (!operator.equalsIgnoreCase(OperationType.LIKE)) {
                throw new ParseException("Unexpected operator '" + operator + "': The key prefix operator 'LIKE' must be used together with the binary operator 'LIKE'", cursor.currentPosition);
            }

            return new HasTagLikeOperation(key, value);
        }

        if (KEY_VALUE_OPERATORS.stream().anyMatch(v -> v.equalsIgnoreCase(operator))) {
            return this.extractKeyValueOperation(operator, key, value);
        }

        return this.extractComparisonOperation(operator, key, value);
    }

    /**
     * Extract a simple negative operation: NOT HAS key or NOT HAS key LIKE
     *
     * @param like Flag which indicate if LIKE is present
     * @param cursorValue The value which should be parsed as key
     *
     * @return Operation
     */
    protected TagOperation extractNegativeOperation(boolean like, String cursorValue) {
        return like
                ? new NotHasKeyLikeOperation(StringUtil.stripAndUnescapeQuotes(cursorValue))
                : new NotHasKeyOperation(StringUtil.stripAndUnescapeQuotes(cursorValue));
    }

    /**
     * Extract a simple positive operation: HAS key or HAS keu LIKE
     *
     * @param like Flag which indicate if LIKE is present
     * @param cursorValue The value which should be parsed as key
     *
     * @return Operation
     */
    protected TagOperation extractKeyOperation(boolean like, String cursorValue) {
        return like
                ? new HasKeyLikeOperation(StringUtil.stripAndUnescapeQuotes(cursorValue))
                : new HasKeyOperation(StringUtil.stripAndUnescapeQuotes(cursorValue));
    }

    /**
     * Extract a simple key-value operation
     *
     * @param operator The operator of the operation
     * @param key The key of the operation
     * @param value The value of the operation
     *
     * @return Operation
     */
    protected TagOperation extractKeyValueOperation(String operator, String key, String value) {
        if (operator.equalsIgnoreCase(OperationType.EQUALS)) {
            return new HasTagOperation(key, value);
        }
        if (operator.equalsIgnoreCase(OperationType.NOT_EQUALS)) {
            return new NotHasTagOperation(key, value);
        }
        if (operator.equalsIgnoreCase(OperationType.LIKE)) {
            return new HasTagValueLikeOperation(key, value);
        }

        return new NotHasTagValueLikeOperation(key, value);
    }

    /**
     * Extract a simple comparison operation
     *
     * @param operator The operator of the operation
     * @param key The key of the operation
     * @param value The value of the operation
     *
     * @return Operation
     * @throws ParseException In case the value is not a valid number or the operator cannot be interpreted
     */
    protected TagOperation extractComparisonOperation(String operator, String key, String value) throws ParseException {
        Double numericValue = NumberUtil.withOptionalUnitToDoubleOrNull(value);
        if (numericValue == null) {
            throw new ParseException("Expected a number (e.g. 3.5) or a number with a known unit (e.g. 3.5st)", cursor.currentPosition);
        }

        if (operator.equalsIgnoreCase(OperationType.GREATER_THAN)) {
            return new HasTagGreaterThanOperation(key, numericValue);
        }
        if (operator.equalsIgnoreCase(OperationType.GREATER_OR_EQUAL_THAN)) {
            return new HasTagGreaterOrEqualThanOperation(key, numericValue);
        }
        if (operator.equalsIgnoreCase(OperationType.LESS_THAN)) {
            return new HasTagLessThanOperation(key, numericValue);
        }

        return new HasTagLessOrEqualThanOperation(key, numericValue);
    }

    /**
     * Getting the operator of the operation, is exists, otherwise return null
     *
     * @return Operator
     */
    protected String getOperator() {
        String cursorValue = cursor.value.substring(cursor.currentPosition);
        Optional<String> operator = OPERATORS.stream().filter(cursorValue::contains).findFirst();
        if (operator.isEmpty()) {
            return null;
        }

        return operator.get();
    }
}
