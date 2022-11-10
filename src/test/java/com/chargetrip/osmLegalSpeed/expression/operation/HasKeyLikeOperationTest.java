package com.chargetrip.osmLegalSpeed.expression.operation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class HasKeyLikeOperationTest  {
    @Test
    @DisplayName("HasKeyLikeOperation matches when the tag without regex is present")
    void testMatchesTagWithoutRegex() {
        HasKeyLikeOperation operation = new HasKeyLikeOperation("ref");

        assertTrue(operation.matches(TagsUtil.getTags()));
        assertFalse(operation.matches(TagsUtil.getEmptyTags()));
    }

    @Test
    @DisplayName("HasKeyLikeOperation matches when the tag with regex is present")
    void testMatchesTagWithRegex() {
        HasKeyLikeOperation operation = new HasKeyLikeOperation("re.*");

        assertTrue(operation.matches(TagsUtil.getTags()));
        assertFalse(operation.matches(TagsUtil.getEmptyTags()));
    }

    @Test
    @DisplayName("HasKeyLikeOperation toString")
    void testToString() {
        HasKeyLikeOperation operation = new HasKeyLikeOperation("ref");

        assertEquals("~ref", operation.toString());
    }
}
