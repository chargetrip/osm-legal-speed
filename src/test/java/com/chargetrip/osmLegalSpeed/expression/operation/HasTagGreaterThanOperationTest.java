package com.chargetrip.osmLegalSpeed.expression.operation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class HasTagGreaterThanOperationTest {
    @Test
    @DisplayName("HasTagGreaterThanOperation do not matches on equal")
    void testDoNotMatchesOnEqual() {
        HasTagGreaterThanOperation operation = new HasTagGreaterThanOperation("lanes", 2);

        assertFalse(operation.matches(TagsUtil.getTags()));
        assertFalse(operation.matches(TagsUtil.getEmptyTags()));
    }

    @Test
    @DisplayName("HasTagGreaterThanOperation matches on greater")
    void testMatchesOnGreater() {
        HasTagGreaterThanOperation operation = new HasTagGreaterThanOperation("lanes", 1);

        assertTrue(operation.matches(TagsUtil.getTags()));
        assertFalse(operation.matches(TagsUtil.getEmptyTags()));
    }

    @Test
    @DisplayName("HasTagGreaterThanOperation do not matches on smaller")
    void testDoNotMatchesOnSmaller() {
        HasTagGreaterThanOperation operation = new HasTagGreaterThanOperation("lanes", 3);

        assertFalse(operation.matches(TagsUtil.getTags()));
        assertFalse(operation.matches(TagsUtil.getEmptyTags()));
    }
    
    @Test
    @DisplayName("HasTagGreaterThanOperation do not matches on invalid numeric value")
    void testDoNotMatchesOnInvalidNumericValue() {
        HasTagGreaterThanOperation operation = new HasTagGreaterThanOperation("network", 1);

        assertFalse(operation.matches(TagsUtil.getTags()));
        assertFalse(operation.matches(TagsUtil.getEmptyTags()));
    }
    
    @Test
    @DisplayName("HasTagGreaterThanOperation toString")
    void testToString() {
        HasTagGreaterThanOperation operation = new HasTagGreaterThanOperation("lanes", 2);

        assertEquals("lanes>2.0", operation.toString());
    }
}
