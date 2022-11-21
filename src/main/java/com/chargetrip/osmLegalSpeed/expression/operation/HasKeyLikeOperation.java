package com.chargetrip.osmLegalSpeed.expression.operation;

import com.chargetrip.osmLegalSpeed.util.RegexOrSet;

import java.util.Map;

/**
 * Has key like operation
 */
public class HasKeyLikeOperation implements TagOperation {
    /**
     * Regular expression for the key match
     */
    protected final RegexOrSet regex;

    /**
     * The key expression
     */
    protected final String key;

    /**
     * Constructor for has key like operation
     *
     * @param key The key expression
     */
    public HasKeyLikeOperation(String key) {
        this.key = key;
        this.regex = RegexOrSet.from(key);
    }

    @Override
    public String toString() {
        return "~" + key;
    }

    @Override
    public boolean matches(Map<String, String> obj) {
        return obj.keySet().stream().anyMatch(regex::matches);
    }
}
