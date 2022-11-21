package com.chargetrip.osmLegalSpeed.expression.operation;

/**
 * Boolean operation
 *
 * @param <T> Type of input object
 */
public interface BooleanOperation<T> {
    /**
     * Check if the boolean operation is true over an object
     *
     * @param object The object to check against the operation
     * @return If matches
     */
    public boolean matches(T object);

    /**
     * Converting the operation to string
     *
     * @return The string value
     */
    public String toString();
}
