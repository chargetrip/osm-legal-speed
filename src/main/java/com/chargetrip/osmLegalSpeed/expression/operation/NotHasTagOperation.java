package com.chargetrip.osmLegalSpeed.expression.operation;

import java.util.Map;

public class NotHasTagOperation implements TagOperation {
    protected final String key;
    protected final String value;

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
