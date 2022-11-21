package com.chargetrip.osmLegalSpeed.expression.operation;

/**
 * Greater Or Equal Numeric operation
 */
public class HasTagGreaterOrEqualThanOperation extends NumberCompareOperation {
    /**
     * Constructor for operation
     *
     * @param key The key name of the tag which hold the numeric value
     * @param value The numeric value to compare against
     */
    public HasTagGreaterOrEqualThanOperation(String key, double value) {
        super(key, value);
    }

    @Override
    public String toString() {
        return key + ">=" + value;
    }

    @Override
    public boolean compareTo(double tagValue) {
        return tagValue >= value;
    }
}
