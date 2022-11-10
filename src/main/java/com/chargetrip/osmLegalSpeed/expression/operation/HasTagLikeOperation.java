package com.chargetrip.osmLegalSpeed.expression.operation;

import com.chargetrip.osmLegalSpeed.util.RegexOrSet;

import java.util.Map;

public class HasTagLikeOperation implements TagOperation {
    protected final RegexOrSet keyRegex;
    protected final RegexOrSet valueRegex;
    protected final String key;
    protected final String value;

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
