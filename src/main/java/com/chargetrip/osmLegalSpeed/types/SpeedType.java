package com.chargetrip.osmLegalSpeed.types;

import com.chargetrip.osmLegalSpeed.expression.OperationType;
import com.chargetrip.osmLegalSpeed.expression.parser.SpeedConditionalParser;
import com.chargetrip.osmLegalSpeed.util.NumberUtil;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

public class SpeedType {
    /**
     * Name of the speed type rule which should be found in road types
     */
    public String name = null;

    /**
     * List with all the max speed tags, depending on vehicles and conditional
     */
    public Map<String, String> tags = new HashMap<>();

    /**
     * Map with rules build for each individual vehicle type
     */
    public Map<VehicleType, VehicleSpeedType> vehicleSpeedType;

    /**
     * Read the speed type from a JSONObject
     *
     * @param object The object from which we decode the speed type
     * @return The speed type instance
     */
    public static SpeedType fromJSON(JSONObject object) {
        SpeedType speedType = new SpeedType();

        if (object.has("name")) {
            speedType.name = object.getString("name");
        }
        if(object.has("tags")) {
            speedType.tags = new HashMap<>();
            JSONObject tags = object.getJSONObject("tags");
            for (String key : tags.keySet()) {
                speedType.tags.put(key, tags.getString(key));
            }
        }

        return speedType;
    }

    @Override
    public String toString() {
        return name + ": " + tags;
    }

    /**
     * Build the rules for each vehicle type with respect to *:conditional tags, without any existing speed limits
     */
    public void build() {
        build(new ArrayList<>());
    }

    /**
     * Build the rules for each vehicle type with respect to *:conditional tags
     *
     * @param countryConfigList An array with all the speed config of a country
     */
    public void build(List<SpeedType> countryConfigList) {
        vehicleSpeedType = new HashMap<>();
        Map<VehicleType, List<String>> vehicleListMap = Options.getVehicleSpeedTags();

        for (VehicleType vehicle : vehicleListMap.keySet()) {
            for (String tag : vehicleListMap.get(vehicle)) {
                if (tags.containsKey(tag)) {
                    VehicleSpeedType vst = new VehicleSpeedType();
                    Double speed = NumberUtil.withOptionalUnitToDoubleOrNull(tags.get(tag));
                    if (speed != null) {
                        vst.speed = speed.floatValue();
                    }

                    if (tag.equalsIgnoreCase("maxspeed")) {
                        if (tags.get(tag).equalsIgnoreCase("walk")) {
                            // Default walk speed is 5 km/h
                            vst.speed = 5.0f;
                        } else {
                            Matcher matcher = OperationType.countryDefaultRule.matcher(tags.get(tag));
//                            System.out.println("Find: "  + matcher.find() + "; " + tags.get(tag));
                            if (matcher.find()) {
                                vst.parent = this.searchSpeedTypeParent(countryConfigList, matcher.group(2));
                            }
                        }
                    }

                    if (tags.containsKey(tag + ":conditional")) {
                        SpeedConditionalParser speedConditionalParser = new SpeedConditionalParser(tags.get(tag + ":conditional"));

                        vst.speedConditional = speedConditionalParser.parse();
                    }

                    vehicleSpeedType.put(vehicle, vst);

                    break;
                }
            }
        }
    }

    /**
     * Determine the legal max speed limit for a specific vehicle type
     *
     * @param vehicle The vehicle type
     * @param optionTags Input options as map of tags
     * @return Null in case speed cannot be determined (vehicle type not available) or the speed value
     */
    public Float getSpeedForVehicle(VehicleType vehicle, Map<String, String> optionTags) {
        if (!this.vehicleSpeedType.containsKey(vehicle)) {
            return null;
        }

        VehicleSpeedType speedType = this.vehicleSpeedType.get(vehicle);

        if (speedType.speedConditional != null) {
            for (SpeedConditional speedConditional : speedType.speedConditional) {
                if (speedConditional.condition.matches(optionTags)) {
                    return speedConditional.speed;
                }
            }
        }

        if (speedType.speed != null) {
            return speedType.speed;
        }

        if (speedType.parent != null) {
            return speedType.parent.getSpeedForVehicle(vehicle, optionTags);
        }

        return null;
    }

    /**
     * Searching the speed type parent based on parent name
     *
     * @param countryConfigList The list of possible parents
     * @param name The name of the parent to search for
     * @return The parent speed type, if exists, otherwise null
     */
    protected SpeedType searchSpeedTypeParent(List<SpeedType> countryConfigList, String name) {
        for (SpeedType speedType : countryConfigList) {
            if (name.equalsIgnoreCase(speedType.name)) {
                return speedType;
            }
        }

        return null;
    }

    /**
     * Type of vehicle speed
     */
    public static class VehicleSpeedType {
        /**
         * Parent rule to infer the speed value
         */
        public SpeedType parent = null;

        /**
         * Default speed value, in km/h, inferred from tags without *:conditional, if is not inferred from another rule
         */
        public Float speed = null;

        /**
         * Conditional speed inferred from *:conditional tags
         */
        public List<SpeedConditional> speedConditional = null;
    }
}
