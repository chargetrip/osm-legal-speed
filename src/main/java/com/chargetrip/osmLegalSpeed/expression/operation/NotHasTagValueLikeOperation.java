package com.chargetrip.osmLegalSpeed.expression.operation;

import com.chargetrip.osmLegalSpeed.util.RegexOrSet;

import java.util.Map;

/**
 * Not has tag value like operation
 */
public class NotHasTagValueLikeOperation implements TagOperation {
    /**
     * Regular expression for tag value
     */
    protected final RegexOrSet regex;

    /**
     * The value for key name of tag
     */
    protected final String key;

    /**
     * The expression for value of tag
     */
    protected final String value;

    /**
     * Constructor for operation
     *
     * @param key The value for key name of tag
     * @param value The expression for value of tag
     */
    public NotHasTagValueLikeOperation(String key, String value) {
        this.key = key;
        this.value = value;
        this.regex = RegexOrSet.from(value);
    }

    @Override
    public String toString() {
        return key + "!~" + value;
    }

    @Override
    public boolean matches(Map<String, String> obj) {
        return !obj.containsKey(key) || !regex.matches(obj.get(key));
    }
}
