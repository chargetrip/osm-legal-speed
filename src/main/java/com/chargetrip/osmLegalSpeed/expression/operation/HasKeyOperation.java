package com.chargetrip.osmLegalSpeed.expression.operation;

import java.util.Map;

public class HasKeyOperation implements TagOperation {
    protected final String key;

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
