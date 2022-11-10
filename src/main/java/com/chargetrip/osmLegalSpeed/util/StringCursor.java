package com.chargetrip.osmLegalSpeed.util;

import com.chargetrip.osmLegalSpeed.expression.OperationType;

import java.text.ParseException;

public class StringCursor {
    /**
     * Value of the cursor
     */
    public String value;

    /**
     * Position of the cursor
     */
    public int currentPosition = 0;

    public StringCursor(String value) {
        this.value = value;
    }

    /**
     * Check if next is the provided string and advance by the work length
     *
     * @param value The word to check
     * @return If the work match next work
     */
    public boolean isNextAndAdvance(String value) {
        if (!isNext(value)) {
            return false;
        }
        advanceBy(value.length());

        return true;
    }

    /**
     * Check if next is the provided string
     *
     * @param value The word to check
     * @return If the work match next work
     */
    public boolean isNext(String value) {
        return this.value.startsWith(value, currentPosition);
    }

    /**
     * Advance the cursor by a number of characters
     *
     * @param x The number of characters to advance
     * @return The string which was advanced
     */
    public String advanceBy(int x) {
        if (x < 0) {
            throw new IndexOutOfBoundsException();
        }

        int end = currentPosition + x;

        String result;
        if (value.length() < end) {
            result = value.substring(currentPosition);
            currentPosition = value.length();
        } else {
            result = value.substring(currentPosition, end);
            currentPosition = end;
        }

        return result;
    }

    /**
     * Find next placeholder
     *
     * @return Return the content of next placeholder or null in case was not found
     * @throws ParseException In case we have an open placeholder, but not the close one
     */
    public String findPlaceholder() throws ParseException {
        if (!value.contains(OperationType.PLACEHOLDER_START)) {
            return null;
        }

        int start = value.indexOf(OperationType.PLACEHOLDER_START);
        int end = value.indexOf(OperationType.PLACEHOLDER_END, start);
        if (end == -1) {
            throw new ParseException("Missing closing bracket '}' for placeholder", start);
        }

        return value.substring(start + 1, end);
    }

    /**
     * Find next parentheses with respect to inside parentheses
     *
     * @return The content of the parentheses or null in case we don't have one
     */
    public String findParentheses() {
        int countOpenParentheses = 0;
        for (int i = currentPosition; i < value.length(); i++) {
            switch (value.charAt(i)) {
                case '(' -> {
                    countOpenParentheses++;
                }
                case ')' -> {
                    if (countOpenParentheses > 0) {
                        countOpenParentheses--;
                        break;
                    }

                    return this.advanceBy(i - currentPosition);
                }
            }
        }

        return null;
    }
}
