package com.chargetrip.osmLegalSpeed.expression.operation;

import java.util.ArrayList;
import java.util.List;

public abstract class TreeOperation implements TagOperation {
    public List<TagOperation> children = new ArrayList<>();

    public TreeOperation addChild(TagOperation child) {
        this.children.add(child);

        return this;
    }

    protected String mergeChildren(String operation) {
        String result = "";
        for (TagOperation child : children) {
            if (!result.equalsIgnoreCase("")) {
                result += " " + operation + " ";
            }

            result += child.toString();
        }

        return "(" + result + ")";
    }
}
