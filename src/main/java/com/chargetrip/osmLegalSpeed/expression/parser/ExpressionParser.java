package com.chargetrip.osmLegalSpeed.expression.parser;

import com.chargetrip.osmLegalSpeed.expression.OperationType;
import com.chargetrip.osmLegalSpeed.expression.operation.AndOperation;
import com.chargetrip.osmLegalSpeed.expression.operation.OrOperation;
import com.chargetrip.osmLegalSpeed.expression.operation.TagOperation;
import com.chargetrip.osmLegalSpeed.expression.operation.TreeOperation;
import com.chargetrip.osmLegalSpeed.util.StringCursor;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Parser class for complex expressions
 */
public class ExpressionParser {
    /**
     * The string cursor used to parse the input value
     */
    public final StringCursor cursor;

    /**
     * A list with all the time operations
     */
    protected List<TagOperation> allTimeOperations;

    /**
     * A list with all the quotation
     */
    protected List<String> allQuotes;

    /**
     * A list with all parentheses operations
     */
    protected List<TagOperation> allParentheses;

    /**
     * Constructor of the parser
     *
     * @param value The string expression to parse
     */
    public ExpressionParser(String value) {
        this.cursor = new StringCursor(value);
    }

    /**
     * Parse an expression into matchable operation
     *
     * @return The matchable operation
     * @throws ParseException In case the operation cannot be parsed
     */
    public TagOperation parse() throws ParseException {
        // First we should extract all Quotes in order to keep the parentheses from them
        this.preserveQuotes();

        // Next, we extract special time operations
        this.extractTimeOperations();

        // Next, we extract the parentheses in sub-expressions
        this.extractParentheses();

        // Next, we should restore all quotes
        cursor.value = this.restoreQuotes(cursor.value);

        return this.extractOperations();
    }

    /**
     * Extract and replace all the time operation for later restoration
     *
     * @throws ParseException In case the operation cannot be parsed
     */
    protected void extractTimeOperations() throws ParseException {
        allTimeOperations = new ArrayList<>();

        List<Pattern> patterns = List.of(OperationType.sunTime, OperationType.dateTime);

        for (Pattern pattern : patterns) {
            do {
                Matcher matcher = pattern.matcher(cursor.value);
                if (matcher.matches()) {
                    int index = allTimeOperations.size();
                    String sunOperation = matcher.group();

                    cursor.value = cursor.value.replace(sunOperation, "$" + index);

                    TimeParser parser = new TimeParser(sunOperation);

                    allTimeOperations.add(parser.parse());

                    continue;
                }

                break;
            } while (true);
        }
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
            ExpressionParser parenthesesParser = new ExpressionParser(parentheses);
            allParentheses.add(parenthesesParser.parse());
        }
    }

    /**
     * Extract all of remaining operations (AND, OR and simple)
     *
     * @return The matchable operation
     */
    protected TagOperation extractOperations() {
        TreeOperation orOperation = new OrOperation();

        String[] orMembers = cursor.value.split(" "+ OperationType.OR+" ");
        for (String orMember : orMembers) {
            TreeOperation andOperation = new AndOperation();
            andOperation.children = Arrays.stream(orMember.split(" "+ OperationType.AND+" ")).map(this::extractOperation).filter(Objects::nonNull).collect(Collectors.toList());

            if (andOperation.children.size() == 1) {
                // We have only one child in the And list, we push that child as the node
                orOperation.addChild(andOperation.children.get(0));

                continue;
            }

            orOperation.addChild(andOperation);
        }

        if (orOperation.children.size() == 1) {
            // We have only one child in the Or list, we return only that child instead of the or node
            return orOperation.children.get(0);
        }

        return orOperation;
    }

    /**
     * Extract a simple operation
     *
     * @param value The input value for the simple operation
     *
     * @return The matchable operation
     */
    protected TagOperation extractOperation(String value) {
        String trimmedValue = value.trim();

        if (trimmedValue.startsWith("#")) {
            // This is a parentheses, we should replace it
            int index = Integer.parseInt(trimmedValue.replace("#", ""));
            return allParentheses.get(index);
        }
        if (trimmedValue.startsWith("$")) {
            // This is a time operation, we should replace it
            int index = Integer.parseInt(trimmedValue.replace("$", ""));
            return allTimeOperations.get(index);
        }

        try {
            return new OperationParser(trimmedValue).parse();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
