package com.chargetrip.osmLegalSpeed.expression.parser;

import com.chargetrip.osmLegalSpeed.expression.OperationType;
import com.chargetrip.osmLegalSpeed.expression.operation.AndOperation;
import com.chargetrip.osmLegalSpeed.expression.operation.OrOperation;
import com.chargetrip.osmLegalSpeed.expression.operation.TagOperation;
import com.chargetrip.osmLegalSpeed.expression.operation.TreeOperation;

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
public class ExpressionParser extends AbstractParser<TagOperation> {
    /**
     * A list with all the time operations
     */
    protected List<TagOperation> allTimeOperations;

    /**
     * Constructor of the parser
     *
     * @param value The string expression to parse
     */
    public ExpressionParser(String value) {
        super(value);
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
     * Extract all of remaining operations (AND, OR and simple)
     *
     * @return The matchable operation
     */
    protected TagOperation extractOperations() {
        TreeOperation orOperation = new OrOperation();

        String[] orMembers = cursor.value.split(" " + OperationType.OR + " ");
        for (String orMember : orMembers) {
            TreeOperation andOperation = new AndOperation();
            andOperation.children = Arrays.stream(orMember.split(" " + OperationType.AND + " ")).map(this::extractOperation).filter(Objects::nonNull).collect(Collectors.toList());

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

    @Override
    protected TagOperation parseParentheses(String parentheses) throws ParseException {
        ExpressionParser parenthesesParser = new ExpressionParser(parentheses);

        return parenthesesParser.parse();
    }
}
