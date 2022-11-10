package com.chargetrip.osmLegalSpeed.expression.operation;

public interface BooleanOperation<T> {
    /**
     * Check if the boolean operation is true over an object
     *
     * @param object The object to check against the operation
     */
    public boolean matches(T object);

    /**
     * Converting the operation to string
     */
    public String toString();
}
