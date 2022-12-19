package com.chargetrip.osmLegalSpeed.expression.parser;

import com.chargetrip.osmLegalSpeed.expression.operation.OrOperation;
import com.chargetrip.osmLegalSpeed.expression.operation.TagOperation;
import com.chargetrip.osmLegalSpeed.expression.operation.TrueOperation;
import com.chargetrip.osmLegalSpeed.types.SpeedConditional;
import com.chargetrip.osmLegalSpeed.util.NumberUtil;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Parser class for speed conditions expressions
 */
public class SpeedConditionalParser extends AbstractParser<String> {
    /**
     * Constructor of the parser
     *
     * @param value The string expression to parse
     */
    public SpeedConditionalParser(String value) {
        super(value);
    }

    /**
     * Parsing the input into a list of speed conditions
     *
     * @return A list of speed conditional
     * @throws ParseException in case cannot parse the string
     */
    public List<SpeedConditional> parse() throws ParseException {
        List<SpeedConditional> result = new ArrayList<>();
        List<String> conditions = this.splitConditions();
        for (String condition : conditions) {
            try {
                String[] data = condition.split("@");
                Double speedValue = NumberUtil.withOptionalUnitToDoubleOrNull(data[0].trim());
                if (speedValue == null) {
                    continue;
                }

                // In case we do not have a condition to parse, we assume a true
                TagOperation speedCondition = new TrueOperation();
                if (data.length > 1) {
                    try {
                        speedCondition = this.parseExpression(data[1].trim().toLowerCase());
                    } catch (ParseException e) {
                        System.out.println("Error while parsing speed condition '" + data[1].trim().toLowerCase() + "': " + e.getMessage());
                    }
                }

                result.add(new SpeedConditional(speedValue.floatValue(), speedCondition));
            } catch (Exception e) {
                System.err.println("Parsing condition: '" + condition + "' of conditions: '" + cursor.value + "'");
                System.err.println(e.getMessage());
            }
        }

        // Make sure they are in reverse order
        Collections.reverse(result);

        return result;
    }

    /**
     * Split the conditions into a list of conditions divided by ; or ,
     *
     * @return A list of conditions
     */
    protected List<String> splitConditions() throws ParseException {
        // First we should extract all Quotes in order to keep the parentheses from them
        this.preserveQuotes();

        // Next, we extract the parentheses in sub-expressions
        this.extractParentheses();

        List<String> result = new ArrayList<>();

        for (String v1 : cursor.value.split(";")) {
            result.add(this.restoreParentheses(this.restoreQuotes(v1.trim())));
        }

        return result;
    }

    protected TagOperation parseExpression(String expression) throws ParseException {
        if (expression.contains(";")) {
            // We have multiple expressions
            OrOperation result = new OrOperation();
            for (String v1 : expression.split(";")) {
                try {
                    result.addChild(new ExpressionParser(v1.trim()).parse());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            System.out.println(result);

            return result;
        }

        return new ExpressionParser(expression).parse();
    }

    /**
     * Restoring the parentheses content to the value
     *
     * @param value The input value
     * @return The original content with parentheses
     */
    protected String restoreParentheses(String value) {
        int index = 0;
        for (String parentheses : this.allParentheses) {
            value = value.replace("#" + index, parentheses);
            index++;
        }

        return value;
    }

    @Override
    protected String parseParentheses(String parentheses) throws ParseException {
        return parentheses;
    }
}
