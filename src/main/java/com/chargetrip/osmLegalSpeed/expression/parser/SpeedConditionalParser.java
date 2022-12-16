package com.chargetrip.osmLegalSpeed.expression.parser;

import com.chargetrip.osmLegalSpeed.expression.operation.TagOperation;
import com.chargetrip.osmLegalSpeed.expression.operation.TrueOperation;
import com.chargetrip.osmLegalSpeed.types.SpeedConditional;
import com.chargetrip.osmLegalSpeed.util.NumberUtil;
import com.chargetrip.osmLegalSpeed.util.StringCursor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class SpeedConditionalParser {
    /**
     * The string cursor used to parse the input value
     */
    public final StringCursor cursor;

    public SpeedConditionalParser(String value) {
        this.cursor = new StringCursor(value);
    }

    /**
     * Parsing the input into a list of speed conditions
     *
     * @return A list of speed conditional
     */
    public List<SpeedConditional> parse() {
        List<SpeedConditional> result = new ArrayList<>();
        List<String> conditions = Arrays.stream(cursor.value.split(";")).map(String::trim).collect(Collectors.toList());
        for (String condition : conditions) {
            try {
                String[] data = condition.split("@");
                Double speedValue = NumberUtil.withOptionalUnitToDoubleOrNull(data[0].trim());
                if (speedValue == null) {
                    continue;
                }

                // In case we do not have a condition to parse, we assume a true
                TagOperation speedCondition = data.length > 1
                        ? new ExpressionParser(data[1].trim().toLowerCase()).parse()
                        : new TrueOperation();

                result.add(new SpeedConditional(speedValue.floatValue(), speedCondition));
            } catch (Exception e) {
                System.err.println("Parsing condition: '" + condition + "'");
                System.err.println(e.getMessage());
            }
        }

        // Make sure they are in reverse order
        Collections.reverse(result);

        return result;
    }
}
