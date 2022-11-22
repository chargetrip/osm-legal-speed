package com.chargetrip.osmLegalSpeed.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class NumberUtilTest {
    @Test
    @DisplayName("NumberUtil toStandardUnitsFactor")
    void testWithOptionalUnitToDoubleOrNull() {
        new NumberUtil();

        assertNull(NumberUtil.withOptionalUnitToDoubleOrNull(""));
        assertNull(NumberUtil.withOptionalUnitToDoubleOrNull("a"));
        assertNull(NumberUtil.withOptionalUnitToDoubleOrNull("\"0\""));
        assertEquals(NumberUtil.withOptionalUnitToDoubleOrNull("0"), 0.0);
        assertEquals(NumberUtil.withOptionalUnitToDoubleOrNull(".1"), 0.1);
        assertEquals(NumberUtil.withOptionalUnitToDoubleOrNull("10"), 10.0);
        assertEquals(NumberUtil.withOptionalUnitToDoubleOrNull("10 cm"), 0.1);
        assertEquals(NumberUtil.withOptionalUnitToDoubleOrNull("10 \""), 0.254);
        assertEquals(NumberUtil.withOptionalUnitToDoubleOrNull("10 '"), 3.048);
        assertNull(NumberUtil.withOptionalUnitToDoubleOrNull("10a"));
        assertEquals(NumberUtil.withOptionalUnitToDoubleOrNull("10 ft 100 in"), 5.588);
        assertNull(NumberUtil.withOptionalUnitToDoubleOrNull("a1"));
        assertNull(NumberUtil.withOptionalUnitToDoubleOrNull("10.a cm"));
        assertEquals(NumberUtil.withOptionalUnitToDoubleOrNull("50;30"), 50);
        assertEquals(NumberUtil.withOptionalUnitToDoubleOrNull("90 ;70"), 90);
        assertEquals(NumberUtil.withOptionalUnitToDoubleOrNull("90; 70"), 90);
    }

    @Test
    @DisplayName("NumberUtil toStandardUnitsFactor")
    void testToStandardUnitsFactor() {
        assertEquals(NumberUtil.toStandardUnitsFactor("km/h"), 1.0);
        assertEquals(NumberUtil.toStandardUnitsFactor("kph"), 1.0);
        assertEquals(NumberUtil.toStandardUnitsFactor("mph"), 1.609344);
        assertEquals(NumberUtil.toStandardUnitsFactor("m"), 1.0);
        assertEquals(NumberUtil.toStandardUnitsFactor("mm"), 0.001);
        assertEquals(NumberUtil.toStandardUnitsFactor("cm"), 0.01);
        assertEquals(NumberUtil.toStandardUnitsFactor("km"), 1000.0);
        assertEquals(NumberUtil.toStandardUnitsFactor("ft"), 0.3048);
        assertEquals(NumberUtil.toStandardUnitsFactor("'"), 0.3048);
        assertEquals(NumberUtil.toStandardUnitsFactor("in"), 0.0254);
        assertEquals(NumberUtil.toStandardUnitsFactor("\""), 0.0254);
        assertEquals(NumberUtil.toStandardUnitsFactor("yd"), 0.9144);
        assertEquals(NumberUtil.toStandardUnitsFactor("yds"), 0.9144);
        assertEquals(NumberUtil.toStandardUnitsFactor("t"), 1.0);
        assertEquals(NumberUtil.toStandardUnitsFactor("kg"), 0.001);
        assertEquals(NumberUtil.toStandardUnitsFactor("st"), 0.90718474);
        assertEquals(NumberUtil.toStandardUnitsFactor("lt"), 1.0160469);
        assertEquals(NumberUtil.toStandardUnitsFactor("lb"), 0.00045359237);
        assertEquals(NumberUtil.toStandardUnitsFactor("lbs"), 0.00045359237);
        assertEquals(NumberUtil.toStandardUnitsFactor("cwt"), 0.05080234544);
        assertNull(NumberUtil.toStandardUnitsFactor(""));
    }
}
