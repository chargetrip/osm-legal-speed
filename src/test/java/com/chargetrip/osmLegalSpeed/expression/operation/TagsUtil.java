package com.chargetrip.osmLegalSpeed.expression.operation;

import java.util.HashMap;
import java.util.Map;

public class TagsUtil {
    public static Map<String, String> getTags() {
        Map<String, String> tags = new HashMap<>();
        tags.put("ref", "US.rural");
        tags.put("type", "route");
        tags.put("route", "road");
        tags.put("lanes", "2");
        tags.put("network", "US:US");
        tags.put("date", "2022-06-07T12:30:00.000");

        return tags;
    }

    public static Map<String, String> getEmptyTags() {
        return new HashMap<>();
    }
}
