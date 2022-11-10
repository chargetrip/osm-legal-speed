package com.chargetrip.osmLegalSpeed.expression.operation;

public class OperationTestSource {
    public String key;
    public String value;

    public OperationTestSource(String key) {
        this.key = key;
    }

    public OperationTestSource(String key, String value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public String toString() {
        return "OperationTestSource{'" + key + "': '" + value + "'}";
    }
}
