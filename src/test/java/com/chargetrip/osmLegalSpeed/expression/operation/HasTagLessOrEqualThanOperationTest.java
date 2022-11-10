package com.chargetrip.osmLegalSpeed.expression.operation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class HasTagLessOrEqualThanOperationTest {
    @Test
    @DisplayName("HasTagLessOrEqualThanOperation matches on equal")
    void testMatchesOnEqual() {
        HasTagLessOrEqualThanOperation operation = new HasTagLessOrEqualThanOperation("lanes", 2);

        assertTrue(operation.matches(TagsUtil.getTags()));
        assertFalse(operation.matches(TagsUtil.getEmptyTags()));
    }

    @Test
    @DisplayName("HasTagLessOrEqualThanOperation matches on smaller")
    void testMatchesOnSmaller() {
        HasTagLessOrEqualThanOperation operation = new HasTagLessOrEqualThanOperation("lanes", 3);

        assertTrue(operation.matches(TagsUtil.getTags()));
        assertFalse(operation.matches(TagsUtil.getEmptyTags()));
    }

    @Test
    @DisplayName("HasTagLessOrEqualThanOperation do not matches on invalid numeric value")
    void testDoNotMatchesOnInvalidNumericValue() {
        HasTagLessOrEqualThanOperation operation = new HasTagLessOrEqualThanOperation("network", 1);

        assertFalse(operation.matches(TagsUtil.getTags()));
        assertFalse(operation.matches(TagsUtil.getEmptyTags()));
    }

    @Test
    @DisplayName("HasTagLessOrEqualThanOperation do not matches on greater")
    void testDoNotMatchesOnGreater() {
        HasTagLessOrEqualThanOperation operation = new HasTagLessOrEqualThanOperation("lanes", 1);

        assertFalse(operation.matches(TagsUtil.getTags()));
        assertFalse(operation.matches(TagsUtil.getEmptyTags()));
    }

    @Test
    @DisplayName("HasTagLessOrEqualThanOperation toString")
    void testToString() {
        HasTagLessOrEqualThanOperation operation = new HasTagLessOrEqualThanOperation("lanes", 2);

        assertEquals("lanes<=2.0", operation.toString());
    }
}
