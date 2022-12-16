package com.chargetrip.osmLegalSpeed.types;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Options for searching the legal max speed
 */
public class Options {
    /**
     * The type of the vehicle for the search. Default is Car. See "VehicleType".
     */
    public VehicleType vehicle = VehicleType.Car;

    /**
     * GPS latitude of the location for the search. This property is very important in order to determine the country
     * and state for legal speed rules.
     */
    public Double latitude = null;

    /**
     * GPS longitude of the location for the search. This property is very important in order to determine the country
     * and state for legal speed rules.
     */
    public Double longitude = null;

    /**
     * The weight of the vehicle, in tones.
     * If not provided, will be ignored.
     */
    public Float weight = null;

    /**
     * The capacity weight (max weight which can be loaded) of the vehicle, in tones.
     * If not provided, will be ignored.
     */
    public Float weightcapacity = null;

    /**
     * The weight of the vehicle when is empty, in tones.
     * If not provided, will be ignored.
     */
    public Float emptyweight = null;

    /**
     * The max allow rating of weight, in tones.
     * If not provided, will be ignored.
     */
    public Float maxweightrating = null;

    /**
     * Flag which indicate if one or more trailers are attached to the vehicle. Default value is false.
     */
    public boolean trailer = false;

    /**
     * Total number of trailers attached to the vehicle. Applies only if "trailer" is true, otherwise will be ignored.
     */
    public Integer trailers = null;

    /**
     * Total weight of trailers, in tones. Applies only if "trailer" is true, otherwise will be ignored.
     */
    public Float trailerweight = null;

    /**
     * Flag which indicate if this is part of a caravan. Default is false.
     */
    public boolean caravan = false;

    /**
     * Flag which indicate if the vehicle is articulated (have one or more articulation). Default is false.
     */
    public boolean articulated = false;

    /**
     * Number of seats in the vehicle.
     * If not provided, will be ignored.
     */
    public Integer seats = null;

    /**
     * Length of the vehicle, in meters.
     * If not provided, will be ignored.
     */
    public Float length = null;

    /**
     * Number of axles of the vehicle.
     * If not provided, will be ignored.
     */
    public Integer axles = null;

    /**
     * The date and time for which the max speed need to be searched. Some countries/states have specific rules for type
     * of day, time of week or even time of year.
     * If not provided, will be ignored.
     */
    public LocalDateTime datetime = null;

    /**
     * Flag which indicate if outside is wet, rained or snowed. Default is false.
     */
    public boolean wet = false;

    /**
     * Flag which indicate if the vehicle is empty. Default is false.
     */
    public boolean empty = false;

    /**
     * Flag for fuzzy search. Some rules cannot match on existing OSM tags, so there are some fuzzy filters. If you do
     * not trust it, please disable it. Default is true.
     */
    public boolean fuzzySearch = true;

    /**
     * Get a map of tags for searching the max speed from a list of max speed tags.
     *
     * @return The map of tags
     */
    public Map<String, String> getTags() {
        Map<String, String> result = new HashMap<>();

        if (latitude != null) {
            result.put("latitude", latitude.toString());
        }
        if (longitude != null) {
            result.put("longitude", longitude.toString());
        }
        if (weight != null) {
            result.put("weight", weight.toString());
        }
        if (weightcapacity != null) {
            result.put("weightcapacity", weightcapacity.toString());
        }
        if (emptyweight != null) {
            result.put("emptyweight", emptyweight.toString());
        }
        if (maxweightrating != null) {
            result.put("maxweightrating", maxweightrating.toString());
        }
        if (trailer) {
            result.put("trailer", "yes");

            if (trailers != null) {
                result.put("trailers", trailers.toString());
            }
            if (trailerweight != null) {
                result.put("trailerweight", trailerweight.toString());
            }
        }
        if (caravan) {
            result.put("caravan", "yes");
        }
        if (articulated) {
            result.put("articulated", "yes");
        }
        if (seats != null) {
            result.put("seats", seats.toString());
        }
        if (length != null) {
            result.put("length", length.toString());
        }
        if (axles != null) {
            result.put("axles", axles.toString());
        }
        if (wet) {
            result.put("wet", "yes");
        }
        if (empty) {
            result.put("empty", "yes");
        }
        if (datetime != null) {
            result.put("date", datetime.toString());
        }

        return result;
    }

    /**
     * Getting a list of tags for each vehicle to get the max speed
     *
     * @return The map of vehicle maxspeed tags
     */
    public static Map<VehicleType, List<String>> getVehicleSpeedTags() {
        Map<VehicleType, List<String>> result = new HashMap<>();

        result.put(VehicleType.Car, List.of("maxspeed", "maxspeed:advisory"));
        result.put(VehicleType.Bus, List.of("maxspeed:bus", "maxspeed", "maxspeed:advisory"));
        result.put(VehicleType.MiniBus, List.of("maxspeed:minibus", "maxspeed", "maxspeed:advisory"));
        result.put(VehicleType.SchoolBus, List.of("maxspeed:school_bus", "maxspeed", "maxspeed:advisory"));
        result.put(VehicleType.TruckBus, List.of("maxspeed:truck_bus", "maxspeed", "maxspeed:advisory"));
        result.put(VehicleType.Coach, List.of("maxspeed:coach", "maxspeed", "maxspeed:advisory"));
        result.put(VehicleType.Goods, List.of("maxspeed:goods", "maxspeed", "maxspeed:advisory"));
        result.put(VehicleType.Hazmat, List.of("maxspeed:hazmat", "maxspeed", "maxspeed:advisory"));
        result.put(VehicleType.Hgv, List.of("maxspeed:hgv", "maxspeed", "maxspeed:advisory"));
        result.put(VehicleType.Motorcycle, List.of("maxspeed:motorcycle", "motorcycle", "maxspeed:motorcycle:advisory", "maxspeed", "maxspeed:advisory"));
        result.put(VehicleType.Tricycle, List.of("maxspeed:tricycle", "tricycle", "maxspeed", "maxspeed:advisory"));
        result.put(VehicleType.MotorHome, List.of("maxspeed:motorhome", "maxspeed:motorhome:advisory", "maxspeed", "maxspeed:advisory"));

        return result;
    }
}
