package com.chargetrip.osmLegalSpeed.expression.operation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class HasTagGreaterOrEqualThanOperationTest {
    @Test
    @DisplayName("HasTagGreaterOrEqualThanOperation matches on equal")
    void testMatchesOnEqual() {
        HasTagGreaterOrEqualThanOperation operation = new HasTagGreaterOrEqualThanOperation("lanes", 2);

        assertTrue(operation.matches(TagsUtil.getTags()));
        assertFalse(operation.matches(TagsUtil.getEmptyTags()));
    }

    @Test
    @DisplayName("HasTagGreaterOrEqualThanOperation matches on greater")
    void testMatchesOnGreater() {
        HasTagGreaterOrEqualThanOperation operation = new HasTagGreaterOrEqualThanOperation("lanes", 1);

        assertTrue(operation.matches(TagsUtil.getTags()));
        assertFalse(operation.matches(TagsUtil.getEmptyTags()));
    }

    @Test
    @DisplayName("HasTagGreaterOrEqualThanOperation do not matches on smaller")
    void testDoNotMatchesOnSmaller() {
        HasTagGreaterOrEqualThanOperation operation = new HasTagGreaterOrEqualThanOperation("lanes", 3);

        assertFalse(operation.matches(TagsUtil.getTags()));
        assertFalse(operation.matches(TagsUtil.getEmptyTags()));
    }

    @Test
    @DisplayName("HasTagGreaterOrEqualThanOperation do not matches on invalid numeric value")
    void testDoNotMatchesOnInvalidNumericValue() {
        HasTagGreaterOrEqualThanOperation operation = new HasTagGreaterOrEqualThanOperation("network", 1);

        assertFalse(operation.matches(TagsUtil.getTags()));
        assertFalse(operation.matches(TagsUtil.getEmptyTags()));
    }

    @Test
    @DisplayName("HasTagGreaterOrEqualThanOperation toString")
    void testToString() {
        HasTagGreaterOrEqualThanOperation operation = new HasTagGreaterOrEqualThanOperation("lanes", 2);

        assertEquals("lanes>=2.0", operation.toString());
    }
}
