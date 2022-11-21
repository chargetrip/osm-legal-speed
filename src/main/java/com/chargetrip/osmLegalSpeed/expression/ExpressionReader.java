package com.chargetrip.osmLegalSpeed.expression;

import com.chargetrip.osmLegalSpeed.config.ResourceInputStream;
import com.chargetrip.osmLegalSpeed.types.RoadType;
import com.chargetrip.osmLegalSpeed.types.SpeedType;
import com.chargetrip.osmLegalSpeed.util.StringCursor;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.*;

/**
 * Reader class for complex expressions
 */
public class ExpressionReader {
    /**
     * Map with all the road type rules
     */
    public Map<String, RoadType> roadTypes = new HashMap<>();

    /**
     * Map with all the speed limit rules
     */
    public Map<String, List<SpeedType>> speedLimits = new HashMap<>();

    /**
     * Replace all the placeholders with the rules of them
     *
     * @return self
     */
    public ExpressionReader replacePlaceholders() {
        for (String key : roadTypes.keySet()) {
            RoadType roadType = roadTypes.get(key);

            roadType.filter = replacePlaceholderValues(roadType.filter, rt -> rt.filter);
            roadType.fuzzyFilter = replacePlaceholderValues(roadType.fuzzyFilter, rt -> {
                if (rt.fuzzyFilter != null) {
                    return rt.fuzzyFilter;
                }

                return rt.filter;
            });
            roadType.relationFilter = replacePlaceholderValues(roadType.relationFilter, rt -> {
                if (rt.relationFilter != null) {
                    return rt.relationFilter;
                }

                return rt.filter;
            });
        }

        return this;
    }

    /**
     * Building the operations for all road types
     *
     * @return self
     * @throws ParseException When cannot parse expression
     */
    public ExpressionReader buildRoadTypes() throws ParseException {
        for (String key : roadTypes.keySet()) {
            roadTypes.get(key).build();
        }

        return this;
    }

    /**
     * Building the operations for all speed limits
     *
     * @return self
     */
    public ExpressionReader buildSpeedLimits() {
        for (String country : speedLimits.keySet()) {
            for (SpeedType speedType : speedLimits.get(country)) {
                speedType.build();
            }
        }

        return this;
    }

    /**
     * Replace all the placeholders with the rules of them for filters
     *
     * @param filter The input filter value to replace
     * @param callback A callback to get the placeholder value from a RoadType
     * @return The value with placeholders replaced
     */
    protected String replacePlaceholderValues(String filter, GetFilter callback) {
        if (filter == null) {
            return null;
        }

        StringCursor cursor = new StringCursor(filter);
        do {
            try {
                String placeholderName = cursor.findPlaceholder();
                if (placeholderName == null) {
                    // No more placeholders
                    break;
                }

                RoadType roadTypePlaceholder = roadTypes.get(placeholderName);
                if (roadTypePlaceholder == null) {
                    throw new ParseException("Cannot find placeholder '" + placeholderName + "'", 0);
                }
                String placeholderValue = callback.get(roadTypePlaceholder);
                if (placeholderValue == null) {
                    throw new ParseException("Placeholder '" + placeholderName + "' does not have filters", 0);
                }

                cursor.value = cursor.value.replace("{"+placeholderName+"}", "("+placeholderValue+")");
                cursor.currentPosition = 0;
            } catch(ParseException e) {
                // One of the placeholders is not closed
                e.printStackTrace();
                break;
            }
        } while (true);

        return cursor.value;
    }

    /**
     * Read the config from a JSON object
     *
     * @param object The object from which to read
     */
    public static ExpressionReader fromJSON(JSONObject object) {
        ExpressionReader config = new ExpressionReader();

        if (object.has("roadTypesByName")) {
            JSONObject roadTypesByNameObject = object.getJSONObject("roadTypesByName");

            for (String key : roadTypesByNameObject.keySet()) {
                config.roadTypes.put(key, RoadType.fromJSON(roadTypesByNameObject.getJSONObject(key)));
            }
        }
        if (object.has("speedLimitsByCountryCode")) {
            JSONObject speedLimitsByCountryCodeObject = object.getJSONObject("speedLimitsByCountryCode");

            for (String key : speedLimitsByCountryCodeObject.keySet()) {
                List<SpeedType> list = new ArrayList<>();
                JSONArray speedLimitsByCountryCodeList = speedLimitsByCountryCodeObject.getJSONArray(key);

                for (int i=0; i<speedLimitsByCountryCodeList.length(); i++) {
                    list.add(SpeedType.fromJSON(speedLimitsByCountryCodeList.getJSONObject(i)));
                }

                // Make sure the list is in reverse order
                Collections.reverse(list);

                config.speedLimits.put(key.toUpperCase(), list);
            }
        }

        return config;
    }

    /**
     * Read the config from file and load it
     *
     * @return New instance of parser
     * @throws ParseException When cannot parse expression
     */
    public static ExpressionReader read() throws ParseException {
        return ExpressionReader.fromJSON(new JSONObject(ResourceInputStream.readLegalSpeed())) //
                .replacePlaceholders() //
                .buildRoadTypes() //
                .buildSpeedLimits() //
                ;
    }

    /**
     * Class for getting the filter
     */
    @FunctionalInterface
    protected interface GetFilter {
        /**
         * Get method
         *
         * @param roadType Type of the road
         * @return The placeholder
         */
        String get(RoadType roadType);
    }
}