package com.chargetrip.osmLegalSpeed.expression.operation;

public class HasTagGreaterThanOperation extends NumberCompareOperation {
    public HasTagGreaterThanOperation(String key, double value) {
        super(key, value);
    }

    @Override
    public String toString() {
        return key + ">" + value;
    }

    @Override
    public boolean compareTo(double tagValue) {
        return tagValue > value;
    }
}
