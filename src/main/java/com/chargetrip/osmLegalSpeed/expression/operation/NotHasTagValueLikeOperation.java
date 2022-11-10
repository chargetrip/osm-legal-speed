package com.chargetrip.osmLegalSpeed.expression.operation;

import com.chargetrip.osmLegalSpeed.util.RegexOrSet;

import java.util.Map;

public class NotHasTagValueLikeOperation implements TagOperation {
    protected final RegexOrSet regex;
    protected final String key;
    protected final String value;

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
