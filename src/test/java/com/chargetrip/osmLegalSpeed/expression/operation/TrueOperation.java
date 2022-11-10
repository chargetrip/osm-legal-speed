package com.chargetrip.osmLegalSpeed.expression.operation;

import java.util.Map;

public class TrueOperation implements TagOperation {
    public TrueOperation() {}

    @Override
    public String toString() {
        return "true";
    }

    @Override
    public boolean matches(Map<String, String> obj) {
        return true;
    }
}
