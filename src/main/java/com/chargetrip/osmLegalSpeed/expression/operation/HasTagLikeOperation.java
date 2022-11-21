package com.chargetrip.osmLegalSpeed.expression.operation;

import com.chargetrip.osmLegalSpeed.util.RegexOrSet;

import java.util.Map;

/**
 * Has tag value like operation
 */
public class HasTagLikeOperation implements TagOperation {
    /**
     * Regular expression for the key of the tag
     */
    protected final RegexOrSet keyRegex;

    /**
     * Regular expression for the value of the tag
     */
    protected final RegexOrSet valueRegex;

    /**
     * The expression for the tag key
     */
    protected final String key;

    /**
     * The expression for the tag value
     */
    protected final String value;

    /**
     * Constructor for operation
     *
     * @param key The expression for the tag key
     * @param value The expression for the tag value
     */
    public HasTagLikeOperation(String key, String value) {
        this.key = key;
        this.value = value;
        this.keyRegex = RegexOrSet.from(key);
        this.valueRegex = RegexOrSet.from(value);
    }

    @Override
    public String toString() {
        return "~" + key + "~" + value;
    }

    @Override
    public boolean matches(Map<String, String> obj) {
        return obj.keySet().stream().anyMatch(key -> keyRegex.matches(key) && valueRegex.matches(obj.get(key)));
    }
}
