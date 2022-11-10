package com.chargetrip.osmLegalSpeed.expression.operation;

public class HasTagLessThanOperation extends NumberCompareOperation {
    public HasTagLessThanOperation(String key, double value) {
        super(key, value);
    }

    @Override
    public String toString() {
        return key + "<" + value;
    }

    @Override
    public boolean compareTo(double tagValue) {
        return tagValue < value;
    }
}
