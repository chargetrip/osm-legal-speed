package com.chargetrip.osmLegalSpeed.expression.operation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class HasKeyOperationTest {
    @Test
    @DisplayName("HasKeyOperation matches successfully")
    void testMatchesSuccessfully() {
        HasKeyOperation operation = new HasKeyOperation("ref");

        assertTrue(operation.matches(TagsUtil.getTags()));
        assertFalse(operation.matches(TagsUtil.getEmptyTags()));
    }

    @Test
    @DisplayName("HasKeyOperation do not matches")
    void testDoNotMatches() {
        HasKeyOperation operation = new HasKeyOperation("no:match");

        assertFalse(operation.matches(TagsUtil.getTags()));
        assertFalse(operation.matches(TagsUtil.getEmptyTags()));
    }

    @Test
    @DisplayName("HasKeyOperation toString")
    void testToString() {
        HasKeyOperation operation = new HasKeyOperation("ref");

        assertEquals("ref", operation.toString());
    }}
