package com.chargetrip.osmLegalSpeed.expression.operation;

import java.util.Map;

public class NotHasKeyOperation implements TagOperation {
    protected final String key;

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
