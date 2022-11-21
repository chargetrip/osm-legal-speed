package com.chargetrip.osmLegalSpeed.expression.operation;

import com.chargetrip.osmLegalSpeed.util.RegexOrSet;

import java.util.Map;

/**
 * Not has key like operation
 */
public class NotHasKeyLikeOperation implements TagOperation {
    /**
     * Regular expression for key name
     */
    protected final RegexOrSet regex;

    /**
     * The expression for key name of tag
     */
    protected final String key;

    /**
     * Constructor for operation
     *
     * @param key The expression for key name of tag
     */
    public NotHasKeyLikeOperation(String key) {
        this.key = key;
        this.regex = RegexOrSet.from(key);
    }

    @Override
    public String toString() {
        return "!~" + key;
    }

    @Override
    public boolean matches(Map<String, String> obj) {
        return obj.keySet().stream().noneMatch(regex::matches);
    }
}
