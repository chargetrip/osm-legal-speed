package com.chargetrip.osmLegalSpeed.expression.operation;

import com.chargetrip.osmLegalSpeed.util.NumberUtil;

import java.util.Map;

public abstract class NumberCompareOperation implements TagOperation {
    protected final String key;
    protected final double value;

    public NumberCompareOperation(String key, double value) {
        this.key = key;
        this.value = value;
    }

    /**
     * Compare the current value with an external value
     *
     * @param value The external value
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
