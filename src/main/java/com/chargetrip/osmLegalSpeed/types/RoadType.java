package com.chargetrip.osmLegalSpeed.types;

import com.chargetrip.osmLegalSpeed.expression.parser.ExpressionParser;
import com.chargetrip.osmLegalSpeed.expression.operation.TagOperation;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.Map;

public class RoadType {
    /**
     * String value of the filter expression loaded from file
     */
    public String filter = null;

    /**
     * String value of the fuzzy filter expression loaded from file
     */
    public String fuzzyFilter = null;

    /**
     * String value of the relation filter expression loaded from file
     */
    public String relationFilter = null;

    /**
     * Operation (matchable) value of the filter expression
     */
    public TagOperation filterOperation = null;

    /**
     * Operation (matchable) value of the fuzzy filter expression
     */
    public TagOperation fuzzyFilterOperation = null;

    /**
     * Operation (matchable) value of the relation filter expression
     */
    public TagOperation relationFilterOperation = null;

    /**
     * Read the road type from a JSONObject
     *
     * @param object The object from which we decode the road type
     * @return The road type instance
     */
    public static RoadType fromJSON(JSONObject object) {
        RoadType roadTypeFilter = new RoadType();

        if (object.has("filter")) {
            roadTypeFilter.filter = object.getString("filter");
        }
        if (object.has("fuzzyFilter")) {
            roadTypeFilter.fuzzyFilter = object.getString("fuzzyFilter");
        }
        if (object.has("relationFilter")) {
            roadTypeFilter.relationFilter = object.getString("relationFilter");
        }

        return roadTypeFilter;
    }

    /**
     * Build the rules for each filter type: filters, fuzzy filters and relation filters, which exists
     *
     * @throws ParseException In case any of the filters cannot be parsed
     */
    public void build() throws ParseException {
        if (filter != null) {
            filterOperation = new ExpressionParser(filter).parse();
        }
        if (fuzzyFilter != null) {
            fuzzyFilterOperation = new ExpressionParser(fuzzyFilter).parse();
        }
        if (relationFilter != null) {
            relationFilterOperation = new ExpressionParser(relationFilter).parse();
        }
    }

    /**
     * Matches the OSM tags against filters operation and return the certitude of matching
     *
     * @param tags The OSM tags as a map of strings
     * @return The match certitude or Fallback in case it doesn't match
     */
    public Certitude matches(Map<String, String> tags) {
        if (filterOperation != null && filterOperation.matches(tags)) {
            // We have an exact match
            return Certitude.Exact;
        }

        if (relationFilterOperation != null && relationFilterOperation.matches(tags)) {
            // We have a relation match
            return Certitude.Exact;
        }

        if (fuzzyFilterOperation != null && fuzzyFilterOperation.matches(tags)) {
            // We have a fuzzy match
            return Certitude.Fuzzy;
        }

        return Certitude.Fallback;
    }
}
