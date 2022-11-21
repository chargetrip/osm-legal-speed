package com.chargetrip.osmLegalSpeed.expression.operation;

import java.util.Map;

/**
 * Has key operation
 */
public class HasKeyOperation implements TagOperation {
    /**
     * Key name which should exists
     */
    protected final String key;

    /**
     * Constructor ofr has key
     *
     * @param key The key name
     */
    public HasKeyOperation(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return key;
    }

    @Override
    public boolean matches(Map<String, String> obj) {
        return obj.containsKey(key);
    }
}
