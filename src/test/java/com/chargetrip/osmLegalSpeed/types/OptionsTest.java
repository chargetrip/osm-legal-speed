package com.chargetrip.osmLegalSpeed.types;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class OptionsTest {
    @Test
    @DisplayName("Options getTags")
    void testGetTags() {
        Options options = new Options();

        Map<String, String> defaultTags = options.getTags();
        assertFalse(defaultTags.containsKey("latitude"));
        assertFalse(defaultTags.containsKey("longitude"));
        assertFalse(defaultTags.containsKey("weight"));
        assertFalse(defaultTags.containsKey("weightcapacity"));
        assertFalse(defaultTags.containsKey("emptyweight"));
        assertFalse(defaultTags.containsKey("maxweightrating"));
        assertFalse(defaultTags.containsKey("trailer"));
        assertFalse(defaultTags.containsKey("trailers"));
        assertFalse(defaultTags.containsKey("trailerweight"));
        assertFalse(defaultTags.containsKey("caravan"));
        assertFalse(defaultTags.containsKey("articulated"));
        assertFalse(defaultTags.containsKey("seats"));
        assertFalse(defaultTags.containsKey("length"));
        assertFalse(defaultTags.containsKey("axles"));
        assertFalse(defaultTags.containsKey("wet"));
        assertFalse(defaultTags.containsKey("empty"));
        assertFalse(defaultTags.containsKey("date"));

        options.latitude = 52.3727598;
        options.longitude = 4.8936041;
        options.weight = 2.f;
        options.weightcapacity = 3.f;
        options.emptyweight = 1.f;
        options.maxweightrating = 5.f;
        options.trailer = true;
        options.trailers = 1;
        options.trailerweight = 1.5f;
        options.caravan = true;
        options.articulated = true;
        options.seats = 5;
        options.length = 12.f;
        options.axles = 4;
        options.wet = true;
        options.empty = true;
        options.datetime = LocalDateTime.now().withHour(12).withMinute(0);

        Map<String, String> fullTags = options.getTags();
        assertTrue(fullTags.containsKey("latitude"));
        assertTrue(fullTags.containsKey("longitude"));
        assertTrue(fullTags.containsKey("weight"));
        assertTrue(fullTags.containsKey("weightcapacity"));
        assertTrue(fullTags.containsKey("emptyweight"));
        assertTrue(fullTags.containsKey("maxweightrating"));
        assertTrue(fullTags.containsKey("trailer"));
        assertTrue(fullTags.containsKey("trailers"));
        assertTrue(fullTags.containsKey("trailerweight"));
        assertTrue(fullTags.containsKey("caravan"));
        assertTrue(fullTags.containsKey("articulated"));
        assertTrue(fullTags.containsKey("seats"));
        assertTrue(fullTags.containsKey("length"));
        assertTrue(fullTags.containsKey("axles"));
        assertTrue(fullTags.containsKey("wet"));
        assertTrue(fullTags.containsKey("empty"));
        assertTrue(fullTags.containsKey("date"));

        Options optionsTrailer = new Options();
        optionsTrailer.trailer = false;
        optionsTrailer.trailers = 1;
        optionsTrailer.trailerweight = 1.5f;

        Map<String, String> noTrailerTags = optionsTrailer.getTags();
        assertFalse(noTrailerTags.containsKey("trailer"));
        assertFalse(noTrailerTags.containsKey("trailers"));
        assertFalse(noTrailerTags.containsKey("trailerweight"));

        optionsTrailer.trailer = true;
        optionsTrailer.trailers = null;
        optionsTrailer.trailerweight = null;

        Map<String, String> withTrailerTags = optionsTrailer.getTags();
        assertTrue(withTrailerTags.containsKey("trailer"));
        assertFalse(withTrailerTags.containsKey("trailers"));
        assertFalse(withTrailerTags.containsKey("trailerweight"));
    }

    @Test
    @DisplayName("Options getVehicleSpeedTags")
    void testGetVehicleSpeedTags() {
        Map<VehicleType, List<String>> map = Options.getVehicleSpeedTags();

        for (VehicleType vehicle : VehicleType.values()) {
            assertTrue(map.containsKey(vehicle));
            assertFalse(map.get(vehicle).isEmpty());
            assertTrue(map.get(vehicle).contains("maxspeed"));
        }
    }
}
