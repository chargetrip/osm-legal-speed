package com.chargetrip.osmLegalSpeed.expression.operation;

import java.util.Map;

public class TrueOperation implements TagOperation {
    @Override
    public boolean matches(Map<String, String> object) {
        return true;
    }

    @Override
    public String toString() {
        return "true";
    }
}
