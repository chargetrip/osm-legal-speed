package com.chargetrip.osmLegalSpeed.expression.operation;

import com.chargetrip.osmLegalSpeed.util.RegexOrSet;

import java.util.Map;

public class NotHasKeyLikeOperation implements TagOperation {
    protected final RegexOrSet regex;
    protected final String key;

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
