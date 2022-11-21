package com.chargetrip.osmLegalSpeed.expression.operation;

import java.util.Map;

/**
 * Not has tag operation
 */
public class NotHasTagOperation implements TagOperation {
    /**
     * The value for key name of tag
     */
    protected final String key;

    /**
     * The value for value of tag
     */
    protected final String value;

    /**
     * Constructor for operation
     *
     * @param key The value for key name of tag
     * @param value The value for value of tag
     */
    public NotHasTagOperation(String key, String value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public String toString() {
        return key + "!=" + value;
    }

    @Override
    public boolean matches(Map<String, String> obj) {
        return !obj.containsKey(key) || !obj.get(key).equalsIgnoreCase(value);
    }
}
