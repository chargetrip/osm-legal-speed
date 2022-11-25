package com.chargetrip.osmLegalSpeed.types;

import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class SpeedTypeTest {
    @Test
    @DisplayName("SpeedType fromJSON")
    void testFromJSON() {
        SpeedType speedType1 = SpeedType.fromJSON(new JSONObject());

        assertNull(speedType1.name);
        assertTrue(speedType1.tags.isEmpty());
        assertNull(speedType1.vehicleSpeedType);

        JSONObject tags = new JSONObject();
        tags.put("maxspeed", "100");
        JSONObject object = new JSONObject();
        object.put("name", "Name");
        object.put("tags", tags);

        SpeedType speedType2 = SpeedType.fromJSON(object);

        assertEquals(speedType2.name, "Name");
        assertFalse(speedType2.tags.isEmpty());
        assertNull(speedType2.vehicleSpeedType);
    }

    @Test
    @DisplayName("SpeedType toString")
    void testToString() {
        SpeedType speedType = new SpeedType();
        speedType.name = "Name";
        speedType.tags.put("maxspeed", "100");

        assertEquals(speedType.toString(), "Name: {maxspeed=100}");
    }

    @Test
    @DisplayName("SpeedType build")
    void testBuild() {
        SpeedType speedType = new SpeedType();
        speedType.name = "Name";
        speedType.tags.put("maxspeed:bus", "100");
        speedType.tags.put("maxspeed:bus:conditional", "90 @ trailer");
        speedType.tags.put("maxspeed:hgv", "100");
        speedType.tags.put("maxspeed:hazmat", "1.a");

        speedType.build();

        assertTrue(speedType.vehicleSpeedType.containsKey(VehicleType.Bus));
        assertNotNull(speedType.vehicleSpeedType.get(VehicleType.Bus).speedConditional);
        assertTrue(speedType.vehicleSpeedType.containsKey(VehicleType.Hgv));
        assertNull(speedType.vehicleSpeedType.get(VehicleType.Hgv).speedConditional);
    }

    @Test
    @DisplayName("SpeedType build for walk speed")
    void testBuildForWalk() {
        SpeedType speedType = new SpeedType();
        speedType.name = "Name";
        speedType.tags.put("maxspeed", "walk");

        speedType.build();

        assertTrue(speedType.vehicleSpeedType.containsKey(VehicleType.Car));
        assertEquals(speedType.vehicleSpeedType.get(VehicleType.Car).speed, 5.0f);
    }

    @Test
    @DisplayName("SpeedType build for parent speed")
    void testBuildParentSpeed() {
        SpeedType speedType = new SpeedType();
        speedType.name = "Name";
        speedType.tags.put("maxspeed", "NL:urban");

        SpeedType ruralSpeedType = new SpeedType();
        ruralSpeedType.name = "rural";
        ruralSpeedType.tags = Map.of("maxspeed", "40");
        SpeedType urbanSpeedType = new SpeedType();
        urbanSpeedType.name = "urban";
        urbanSpeedType.tags = Map.of("maxspeed", "50");

        List<SpeedType> countryConfigList = List.of(urbanSpeedType);

        speedType.build(countryConfigList);

        assertTrue(speedType.vehicleSpeedType.containsKey(VehicleType.Car));
        assertNotNull(speedType.vehicleSpeedType.get(VehicleType.Car).parent);
        assertEquals(speedType.vehicleSpeedType.get(VehicleType.Car).parent.name, "urban");
    }

    @Test
    @DisplayName("SpeedType getVehicleSpeedTags")
    void testGetVehicleSpeedTags() {
        SpeedType speedType = new SpeedType();
        speedType.name = "Name";
        speedType.tags.put("maxspeed", "NL:urban");
        speedType.tags.put("maxspeed:bus", "100");
        speedType.tags.put("maxspeed:bus:conditional", "90 @ trailer");
        speedType.tags.put("maxspeed:hgv", "100");
        speedType.tags.put("maxspeed:hazmat", "1.a");

        speedType.build();

        assertEquals(speedType.getSpeedForVehicle(VehicleType.Bus, new HashMap<>()), 100.f);
        assertEquals(speedType.getSpeedForVehicle(VehicleType.Bus, Map.of("trailer", "yes")), 90.f);
        assertEquals(speedType.getSpeedForVehicle(VehicleType.Hgv, new HashMap<>()), 100.f);
        assertNull(speedType.getSpeedForVehicle(VehicleType.Car, new HashMap<>()));
    }

    @Test
    @DisplayName("SpeedType getVehicleSpeedTags empty")
    void testGetVehicleSpeedTagsEmpty() {
        SpeedType speedType = new SpeedType();
        speedType.name = "Name";

        speedType.build();

        assertNull(speedType.getSpeedForVehicle(VehicleType.Car, new HashMap<>()));
    }

    @Test
    @DisplayName("SpeedType getVehicleSpeedTags infer from parent")
    void testGetVehicleSpeedTagsFromParent() {
        SpeedType speedType = new SpeedType();
        speedType.name = "Name";
        speedType.tags.put("maxspeed", "NL:urban");

        SpeedType ruralSpeedType = new SpeedType();
        ruralSpeedType.name = "rural";
        ruralSpeedType.tags = Map.of("maxspeed", "40");
        ruralSpeedType.build();

        SpeedType urbanSpeedType = new SpeedType();
        urbanSpeedType.name = "urban";
        urbanSpeedType.tags = Map.of("maxspeed", "50");
        urbanSpeedType.build();

        speedType.build(List.of(ruralSpeedType, urbanSpeedType));

        assertEquals(speedType.getSpeedForVehicle(VehicleType.Car, new HashMap<>()), 50.f);
    }
}
