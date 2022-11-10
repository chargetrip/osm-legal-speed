package com.chargetrip.osmLegalSpeed.expression.operation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class SunTimeOperationTest {
    @Test
    @DisplayName("SunTimeOperation not match without GPS")
    void testNoMatchGPS() {
        SunTimeOperation operation = new SunTimeOperation(
                new SunTimeOperation.SunOffset(true, 0, 30),
                new SunTimeOperation.SunOffset(true, 0, 30),
                true
        );

        Map<String, String> tags = TagsUtil.getTags();
        tags.put("latitude", "69.65807894706734");

        assertFalse(operation.matches(tags));

        tags = TagsUtil.getTags();
        tags.put("longitude", "18.963335420335035");

        assertFalse(operation.matches(tags));
    }

    @Test
    @DisplayName("SunTimeOperation not match without date")
    void testNoMatchDate() {
        SunTimeOperation operation = new SunTimeOperation(
                new SunTimeOperation.SunOffset(true, 0, 30),
                new SunTimeOperation.SunOffset(true, 0, 30),
                true
        );

        Map<String, String> tags = TagsUtil.getTags();
        tags.put("latitude", "69.65807894706734");
        tags.put("longitude", "18.963335420335035");
        tags.remove("date");

        assertFalse(operation.matches(tags));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "2022-06-07T12:00:00.000",
            "2022-06-06T22:55:00.000",
            "2022-06-07T06:22:00.000"
    })
    @DisplayName("SunTimeOperation not match because of time interval")
    void testNoMatchTime(String date) {
        SunTimeOperation operation = new SunTimeOperation(
                new SunTimeOperation.SunOffset(true, 0, 0),
                new SunTimeOperation.SunOffset(false, 0, 0),
                true
        );

        Map<String, String> tags = TagsUtil.getTags();
        tags.put("latitude", "52.3727598");
        tags.put("longitude", "4.8936041");
        tags.put("date", date);

        assertFalse(operation.matches(tags));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "2022-06-07T00:00:00.000",
            "2022-06-06T22:57:00.000",
            "2022-06-07T06:20:00.000"
    })
    @DisplayName("SunTimeOperation match sunset first")
    void testMatchSunsetFirst(String date) {
        SunTimeOperation operation = new SunTimeOperation(
                new SunTimeOperation.SunOffset(false, 0, 0),
                new SunTimeOperation.SunOffset(true, 0, 0),
                true
        );

        Map<String, String> tags = TagsUtil.getTags();
        tags.put("latitude", "52.3727598");
        tags.put("longitude", "4.8936041");
        tags.put("date", date);

        assertTrue(operation.matches(tags));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "2022-06-07T12:00:00.000",
            "2022-06-06T22:57:00.000",
            "2022-06-07T06:20:00.000"
    })
    @DisplayName("SunTimeOperation match sunrise first")
    void testMatchSunriseFirst(String date) {
        SunTimeOperation operation = new SunTimeOperation(
                new SunTimeOperation.SunOffset(false, 0, 0),
                new SunTimeOperation.SunOffset(true, 0, 0),
                false
        );

        Map<String, String> tags = TagsUtil.getTags();
        tags.put("latitude", "52.3727598");
        tags.put("longitude", "4.8936041");
        tags.put("date", date);

        assertTrue(operation.matches(tags));
        assertFalse(operation.matches(TagsUtil.getEmptyTags()));
    }

    @Test
    @DisplayName("SunTimeOperation toString negative values")
    void testToStringNegativeValues() {
        SunTimeOperation operation = new SunTimeOperation(
                new SunTimeOperation.SunOffset(true, 0, 30),
                new SunTimeOperation.SunOffset(true, 0, 30),
                true
        );

        assertEquals(operation.toString(), "((sunset-00:30)-(sunrise-00:30))");
    }

    @Test
    @DisplayName("SunTimeOperation toString positive values")
    void testToStringPositiveValues() {
        SunTimeOperation operation = new SunTimeOperation(
                new SunTimeOperation.SunOffset(false, 0, 30),
                new SunTimeOperation.SunOffset(false, 0, 30),
                true
        );

        assertEquals(operation.toString(), "((sunset+00:30)-(sunrise+00:30))");
    }

    @Test
    @DisplayName("SunTimeOperation toString sunrise first")
    void testToStringSunriseFirst() {
        SunTimeOperation operation = new SunTimeOperation(
                new SunTimeOperation.SunOffset(false, 0, 30),
                new SunTimeOperation.SunOffset(false, 0, 30),
                false
        );

        assertEquals(operation.toString(), "((sunrise+00:30)-(sunset+00:30))");
    }

    @Test
    @DisplayName("SunTimeOperation.SunTime plus and minus")
    void testSunTime() {
        SunTimeOperation.SunTime sunTime = new SunTimeOperation.SunTime(0, 0);

        sunTime.plusHours(49);
        assertEquals(sunTime.hour, 1);

        sunTime.minusHours(50);
        assertEquals(sunTime.hour, 23);

        sunTime.plusMinutes(121);
        assertEquals(sunTime.minute, 1);

        sunTime.minusMinutes(122);
        assertEquals(sunTime.minute, 59);
    }
}
