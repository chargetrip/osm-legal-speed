package com.chargetrip.osmLegalSpeed.expression.operation;

import java.util.Map;

public class FalseOperation implements TagOperation {
    public FalseOperation() {}

    @Override
    public String toString() {
        return "false";
    }

    @Override
    public boolean matches(Map<String, String> obj) {
        return false;
    }
}
