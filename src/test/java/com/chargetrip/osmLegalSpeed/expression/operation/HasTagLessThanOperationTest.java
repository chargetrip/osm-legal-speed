package com.chargetrip.osmLegalSpeed.expression.operation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class HasTagLessThanOperationTest {
    @Test
    @DisplayName("HasTagLessThanOperation do not matches on equal")
    void testDoNotMatchesOnEqual() {
        HasTagLessThanOperation operation = new HasTagLessThanOperation("lanes", 2);

        assertFalse(operation.matches(TagsUtil.getTags()));
        assertFalse(operation.matches(TagsUtil.getEmptyTags()));
    }

    @Test
    @DisplayName("HasTagLessThanOperation matches on smaller")
    void testMatchesOnSmaller() {
        HasTagLessThanOperation operation = new HasTagLessThanOperation("lanes", 3);

        assertTrue(operation.matches(TagsUtil.getTags()));
        assertFalse(operation.matches(TagsUtil.getEmptyTags()));
    }

    @Test
    @DisplayName("HasTagLessThanOperation do not matches on greater")
    void testDoNotMatchesOnGreater() {
        HasTagLessThanOperation operation = new HasTagLessThanOperation("lanes", 1);

        assertFalse(operation.matches(TagsUtil.getTags()));
        assertFalse(operation.matches(TagsUtil.getEmptyTags()));
    }

    @Test
    @DisplayName("HasTagLessThanOperation do not matches on invalid numeric value")
    void testDoNotMatchesOnInvalidNumericValue() {
        HasTagLessThanOperation operation = new HasTagLessThanOperation("network", 1);

        assertFalse(operation.matches(TagsUtil.getTags()));
        assertFalse(operation.matches(TagsUtil.getEmptyTags()));
    }

    @Test
    @DisplayName("HasTagLessThanOperation toString")
    void testToString() {
        HasTagLessThanOperation operation = new HasTagLessThanOperation("lanes", 2);

        assertEquals("lanes<2.0", operation.toString());
    }
}
