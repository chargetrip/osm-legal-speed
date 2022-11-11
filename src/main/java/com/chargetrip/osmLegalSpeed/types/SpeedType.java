package com.chargetrip.osmLegalSpeed.types;

import com.chargetrip.osmLegalSpeed.expression.parser.SpeedConditionalParser;
import com.chargetrip.osmLegalSpeed.util.NumberUtil;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
     * Build the rules for each vehicle type with respect to *:conditional tags
     */
    public void build() {
        vehicleSpeedType = new HashMap<>();
        Map<VehicleType, List<String>> vehicleListMap = Options.getVehicleSpeedTags();

        for (VehicleType vehicle : vehicleListMap.keySet()) {
            for (String tag : vehicleListMap.get(vehicle)) {
                if (tags.containsKey(tag)) {
                    VehicleSpeedType vst = new VehicleSpeedType();
                    Double speed = NumberUtil.withOptionalUnitToDoubleOrNull(tags.get(tag));
                    if (speed == null) {
                        continue;
                    }

                    vst.speed = speed.floatValue();

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

        return speedType.speed;
    }

    /**
     * Type of vehicle speed
     */
    public static class VehicleSpeedType {
        /**
         * Default speed value, in km/h, inferred from tags without *:conditional
         */
        public float speed;

        /**
         * Conditional speed inferred from *:conditional tags
         */
        public List<SpeedConditional> speedConditional = null;
    }
}
