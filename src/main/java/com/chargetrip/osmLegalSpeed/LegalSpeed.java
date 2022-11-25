package com.chargetrip.osmLegalSpeed;

import com.chargetrip.osmLegalSpeed.config.ResourceInputStream;
import com.chargetrip.osmLegalSpeed.expression.ExpressionReader;
import com.chargetrip.osmLegalSpeed.types.*;
import com.mapbox.geojson.*;
import com.mapbox.turf.TurfJoins;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Rules to search for legal max speed in most of the world. There are some part of the world, for which we do not have
 * the set of rules, and we cannot determine the legal max speed.
 */
public class LegalSpeed {
    /**
     * Regex for detecting the OSM max speed tags
     */
    protected final Pattern maxSpeedTags = Pattern.compile("maxspeed.*");

    /**
     * Reader of the expressions to match legal max speed rules
     */
    protected final ExpressionReader reader;

    /**
     * A GeoJSON feature collection with all countries/states polygons in order to match them on GPS coordinates
     */
    protected final FeatureCollection countryFeatureCollection;

    /**
     * Constructor for the legal speed
     *
     * @throws IOException When cannot read config file
     * @throws ParseException Cannot parse expression from config file
     */
    public LegalSpeed() throws IOException, ParseException {
        reader = ExpressionReader.read();
        countryFeatureCollection = FeatureCollection.fromJson(ResourceInputStream.readCountries());
    }

    /**
     * Getting the legal max speed for a specific road segment based on GPS coordinates and OSM tags. The legal max
     * speed rules are defined per state/region in some countries or per country in most of them. Sadly, we do not have
     * the rules for all the countries of the world, so we may return null in some cases.
     *
     * @param tags A map with all the OSM tags of the way
     * @param options The options for the max speed, like vehicle type, location, and so on
     * @return Null in case we do not have the rules or the legal max speed
     */
    public Float getSpeedLimit(Map<String, String> tags, Options options) {
        if (options.latitude == null || options.longitude == null) {
            // No location was provided
            return null;
        }

        String countryWithRegion = getCountryWithRegion(options.latitude, options.longitude);
        if (countryWithRegion == null) {
            // Country and/or region cannot be found
            return null;
        }

        SearchResult searchResult = searchSpeedLimits(tags, countryWithRegion, options.fuzzySearch);
        if (searchResult == null) {
            // No result were found
            return null;
        }

        return searchResult.speedType.getSpeedForVehicle(options.vehicle, options.getTags());
    }

    /**
     * Based on GPS coordinates (latitude and longitude), determine the country code and, in some cases, the region code.
     * Some countries, like the US, have different speed rules for different states.
     * Because we do not have the legal speed rules for all the countries of the world, for some locations this will
     * return null value.
     *
     * @param latitude The GPS latitude as a double
     * @param longitude The GPS longitude as a double
     * @return The country code and region (is exists) in a format like "NL" or "US-NY", or a null value
     */
    public String getCountryWithRegion(Double latitude, Double longitude) {
        if (countryFeatureCollection.features() == null) {
            return null;
        }

        Point point = Point.fromLngLat(longitude, latitude);

        for (Feature country : countryFeatureCollection.features()) {
            boolean isInside = false;
            if (country.geometry() instanceof Polygon) {
                isInside = TurfJoins.inside(point, ((Polygon) country.geometry()));
            } else if (country.geometry() instanceof MultiPolygon) {
                isInside = TurfJoins.inside(point, ((MultiPolygon) country.geometry()));
            }

            if (isInside) {
                return country.getStringProperty("id");
            }
        }

        return null;
    }

    /**
     * Searching for the max speed limits of a country with region with a specific set of OSM tags.
     *
     * @param tags A map with all the OSM tags of the way
     * @param countryWithRegion The country with region for which the search occurs, see "getCountryWithRegion"
     * @param fuzzySearch Flag which enable fuzzy search
     * @return An object with certitude, speed type and road type
     */
    public SearchResult searchSpeedLimits(
            Map<String, String> tags,
            String countryWithRegion,
            boolean fuzzySearch
    ) {
        String countryWithRegionCode = countryWithRegion.toUpperCase();
        if (!reader.speedLimits.containsKey(countryWithRegionCode)) {
            // We cannot find the country config, we need to check to see if "countryWithRegionCode" contains "-"
            if (countryWithRegionCode.contains("-")) {
                String[] countryWithRegionSplit = countryWithRegionCode.split("-");
                if (!reader.speedLimits.containsKey(countryWithRegionSplit[0])) {
                    // We still do not have the data for country code, we need to return null
                    return null;
                }

                countryWithRegionCode = countryWithRegionSplit[0];
            } else {
                // Country code is without region, and we do not have it, we need to return null
                return null;
            }
        }

        List<SpeedType> countryConfigList = reader.speedLimits.get(countryWithRegionCode);

        List<String> maxSpeedKeys = tags.keySet().stream().filter(key -> maxSpeedTags.matcher(key).find()).collect(Collectors.toList());
        if (!maxSpeedKeys.isEmpty()) {
            SearchResult fromMaxSpeed = new SearchResult();
            fromMaxSpeed.roadType = null;
            fromMaxSpeed.certitude = Certitude.FromMaxSpeed;
            fromMaxSpeed.speedType = new SpeedType();
            for (String key : maxSpeedKeys) {
                fromMaxSpeed.speedType.tags.put(key, tags.get(key));
            }
            fromMaxSpeed.speedType.build(countryConfigList);

            // We check to see if we have a valid max speed from OSM tags
            if (fromMaxSpeed.speedType.getSpeedForVehicle(VehicleType.Car, new HashMap<>()) != null) {
                // We have a valid max speed from OSM tags
                return fromMaxSpeed;
            }
        }

        SpeedType defaultSpeedType = countryConfigList.stream().filter(config -> config.name == null).findFirst().orElse(null);
        SpeedType firstExactSpeedType = null;
        SpeedType firstFuzzySpeedType = null;

        for (SpeedType speedType : countryConfigList) {
            if (speedType.name == null) {
                // this is the default rule, we skip checking it
                continue;
            }

            if (!reader.roadTypes.containsKey(speedType.name)) {
                // We don't have the set of rules defined for this speed type, we skip it
                continue;
            }

            Certitude expressionCertitude = reader.roadTypes.get(speedType.name).matches(tags);
            switch (expressionCertitude) {
                case Exact -> {
                    if (firstExactSpeedType == null) {
                        firstExactSpeedType = speedType;
                    }
                }
                case Fuzzy -> {
                    if (fuzzySearch && firstFuzzySpeedType == null) {
                        firstFuzzySpeedType = speedType;
                    }
                }
            }
        }

        SearchResult legalSpeedResult = new SearchResult();
        legalSpeedResult.roadType = null;
        legalSpeedResult.speedType = defaultSpeedType;
        legalSpeedResult.certitude = Certitude.Fallback;

        if (firstExactSpeedType != null) {
            legalSpeedResult.roadType = reader.roadTypes.get(firstExactSpeedType.name);
            legalSpeedResult.speedType = firstExactSpeedType;
            legalSpeedResult.certitude = Certitude.Exact;
        } else if (fuzzySearch && firstFuzzySpeedType != null) {
            legalSpeedResult.roadType = reader.roadTypes.get(firstFuzzySpeedType.name);
            legalSpeedResult.speedType = firstFuzzySpeedType;
            legalSpeedResult.certitude = Certitude.Fuzzy;
        }

        return legalSpeedResult;
    }

    /**
     * Result for the max speed search
     */
    public static class SearchResult {
        /**
         * Matched filters for the road type
         */
        public RoadType roadType;

        /**
         * Matched speed limits for the road
         */
        public SpeedType speedType;

        /**
         * Matched certitude
         */
        public Certitude certitude;
    }
}
