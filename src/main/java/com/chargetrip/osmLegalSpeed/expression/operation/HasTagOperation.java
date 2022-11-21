package com.chargetrip.osmLegalSpeed.expression.operation;

import java.util.Map;

/**
 * Has tag value operation
 */
public class HasTagOperation implements TagOperation {
    /**
     * The value for the tag key
     */
    protected final String key;

    /**
     * The value for the tag value
     */
    protected final String value;

    /**
     * Constructor for operation
     *
     * @param key The value for the tag key
     * @param value The value for the tag value
     */
    public HasTagOperation(String key, String value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public String toString() {
        return key + "=" + value;
    }

    @Override
    public boolean matches(Map<String, String> obj) {
        return obj.containsKey(key) && obj.get(key).equalsIgnoreCase(value);
    }
}
