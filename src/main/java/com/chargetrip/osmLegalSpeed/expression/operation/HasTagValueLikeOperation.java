package com.chargetrip.osmLegalSpeed.expression.operation;

import com.chargetrip.osmLegalSpeed.util.RegexOrSet;

import java.util.Map;

/**
 * Has tag value like operation
 */
public class HasTagValueLikeOperation implements TagOperation {
    /**
     * Regular expression for the value
     */
    protected final RegexOrSet regex;

    /**
     * The key name of the tag
     */
    protected final String key;

    /**
     * The expression for the tag value
     */
    protected final String value;

    /**
     * Constructor for operation
     *
     * @param key The key name of the tag
     * @param value The expression for the tag value
     */
    public HasTagValueLikeOperation(String key, String value) {
        this.key = key;
        this.value = value;
        this.regex = RegexOrSet.from(value);
    }

    @Override
    public String toString() {
        return key + "~" + value;
    }

    @Override
    public boolean matches(Map<String, String> obj) {
        return obj.containsKey(key) && regex.matches(obj.get(key));
    }
}
