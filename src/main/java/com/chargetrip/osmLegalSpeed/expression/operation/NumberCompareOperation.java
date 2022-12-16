package com.chargetrip.osmLegalSpeed.expression.operation;

import com.chargetrip.osmLegalSpeed.util.NumberUtil;

import java.util.Map;

/**
 * Numeric operation
 */
public abstract class NumberCompareOperation implements TagOperation {
    /**
     * The key name used to extract the number value
     */
    protected final String key;

    /**
     * The value to compare against
     */
    protected final double value;

    /**
     * Constructor for operation
     *
     * @param key The key name of the tag which hold the numeric value
     * @param value The numeric value to compare against
     */
    public NumberCompareOperation(String key, double value) {
        this.key = key;
        this.value = value;
    }

    /**
     * Compare the current value with an external value
     *
     * @param value The external value
     * @return Compare result
     */
    public abstract boolean compareTo(double value);

    @Override
    public boolean matches(Map<String, String> object) {
        if (object.containsKey(key)) {
            Double tagValue = NumberUtil.withOptionalUnitToDoubleOrNull(object.get(key));
            if (tagValue != null) {
                return compareTo(tagValue);
            }
        }

        return false;
    }
}
