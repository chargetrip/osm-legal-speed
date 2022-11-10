package com.chargetrip.osmLegalSpeed.expression.operation;

import com.chargetrip.osmLegalSpeed.expression.OperationType;

import java.util.Map;

public class AndOperation extends TreeOperation {
    @Override
    public boolean matches(Map<String, String> object) {
        return this.children.stream().allMatch(child -> child.matches(object));
    }

    @Override
    public String toString() {
        return this.mergeChildren(OperationType.AND);
    }
}
