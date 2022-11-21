package com.chargetrip.osmLegalSpeed.expression.operation;

import java.util.Map;

/**
 * Not has key operation
 */
public class NotHasKeyOperation implements TagOperation {
    /**
     * The value for key name of tag
     */
    protected final String key;

    /**
     * Constructor for operation
     *
     * @param key The value for key name of tag
     */
    public NotHasKeyOperation(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return "!" + key;
    }

    @Override
    public boolean matches(Map<String, String> obj) {
        return !obj.containsKey(key);
    }
}
