package com.chargetrip.osmLegalSpeed.expression.operation;

/**
 * Less Or Equal Numeric operation
 */
public class HasTagLessOrEqualThanOperation extends NumberCompareOperation {
    /**
     * Constructor for operation
     *
     * @param key The key name of the tag which hold the numeric value
     * @param value The numeric value to compare against
     */
    public HasTagLessOrEqualThanOperation(String key, double value) {
        super(key, value);
    }

    @Override
    public String toString() {
        return key + "<=" + value;
    }

    @Override
    public boolean compareTo(double tagValue) {
        return tagValue <= value;
    }
}
