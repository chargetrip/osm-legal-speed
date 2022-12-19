package com.chargetrip.osmLegalSpeed.expression.parser;

import com.chargetrip.osmLegalSpeed.expression.OperationType;
import com.chargetrip.osmLegalSpeed.util.StringCursor;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Abstract parser class for complex expressions
 */
public abstract class AbstractParser<T> {
    /**
     * The string cursor used to parse the input value
     */
    public final StringCursor cursor;

    /**
     * A list with all the quotation
     */
    protected List<String> allQuotes;

    /**
     * A list with all parentheses operations
     */
    protected List<T> allParentheses;

    /**
     * Constructor of the parser
     *
     * @param value The string expression to parse
     */
    public AbstractParser(String value) {
        this.cursor = new StringCursor(value);
    }

    /**
     * Extract and preserve all quotations
     *
     * @throws ParseException In case the operation cannot be parsed
     */
    protected void preserveQuotes() throws ParseException {
        allQuotes = new ArrayList<>();

        while (cursor.value.contains(OperationType.QUOTE)) {
            int start = cursor.value.indexOf(OperationType.QUOTE);
            int end = cursor.value.indexOf(OperationType.QUOTE, start + 1);
            cursor.currentPosition = start + 1;
            int index = allQuotes.size();

            if (end == -1) {
                throw new ParseException("Missing closing quote '\"'", start);
            }

            String quote = cursor.value.substring(start + 1, end);

            cursor.value = cursor.value.replace("\""+quote+"\"" , "@" + index);

            allQuotes.add(quote);
        }
    }

    /**
     * Restore all possible quotation from an input value
     *
     * @param value The input value to restore
     * @return The value with quotation
     */
    protected String restoreQuotes(String value) {
        for (int i=0; i<allQuotes.size(); i++) {
            String quote = allQuotes.get(i);

            value = value.replace("@"+i, "\""+quote+"\"");
        }

        return value;
    }

    /**
     * Extract and replace all parentheses operation for later restoration
     *
     * @throws ParseException In case the operation cannot be parsed
     */
    protected void extractParentheses() throws ParseException {
        allParentheses = new ArrayList<>();

        while (cursor.value.contains(OperationType.PARENTHESES_START)) {
            int start = cursor.value.indexOf(OperationType.PARENTHESES_START);
            cursor.currentPosition = start + 1;
            boolean negative = start > 0 && cursor.value.charAt(start - 1) == '!';
            int index = allParentheses.size();

            String parentheses = cursor.findParentheses();
            if (parentheses == null) {
                throw new ParseException("Missing closing parentheses ')'", start);
            }

            cursor.value = cursor.value.replace((negative ? "!" : "") + "("+parentheses+")" , "#" + index);

            // Make sure we restore quotes on the parentheses string
            parentheses = this.restoreQuotes(parentheses);
            allParentheses.add(this.parseParentheses(parentheses));
        }
    }

    /**
     * Parse the content of a parentheses
     *
     * @param parentheses The content of the parentheses
     */
    protected abstract T parseParentheses(String parentheses) throws ParseException;
}
