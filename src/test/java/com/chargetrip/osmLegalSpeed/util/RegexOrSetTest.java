package com.chargetrip.osmLegalSpeed.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RegexOrSetTest {
    @Test
    @DisplayName("RegexOrSet from")
    void testFrom() {
        assertTrue(RegexOrSet.from("a|b") instanceof RegexOrSet.SetRegex);
        assertTrue(RegexOrSet.from("a|b").matches("a"));
        assertFalse(RegexOrSet.from("a|b").matches("c"));
        assertTrue(RegexOrSet.from("a.*") instanceof RegexOrSet.RealRegex);
        assertTrue(RegexOrSet.from("a.*").matches("ab"));
        assertFalse(RegexOrSet.from("a.*").matches("bc"));
    }
}
