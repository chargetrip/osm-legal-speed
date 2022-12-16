package com.chargetrip.osmLegalSpeed.expression.operation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TrueOperationTest {
    @Test
    @DisplayName("TrueOperation matches always true")
    void testAlwaysTrue() {
        TrueOperation operation = new TrueOperation();

        assertTrue(operation.matches(TagsUtil.getTags()));
        assertTrue(operation.matches(TagsUtil.getEmptyTags()));
    }

    @Test
    @DisplayName("TrueOperation toString should merge children")
    void testToString() {
        TrueOperation operation = new TrueOperation();

        assertEquals("true", operation.toString());
    }
}
