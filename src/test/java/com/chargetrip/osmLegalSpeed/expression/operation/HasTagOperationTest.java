package com.chargetrip.osmLegalSpeed.expression.operation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class HasTagOperationTest {
    protected static Stream<OperationTestSource> successSource() {
        return Stream.of(
                new OperationTestSource("type", "route"),
                new OperationTestSource("route", "road")
        );
    }

    @ParameterizedTest
    @MethodSource("successSource")
    @DisplayName("HasTagOperation matches successfully")
    void testMatchesSuccessfully(OperationTestSource source) {
        HasTagOperation operation = new HasTagOperation(source.key, source.value);

        assertTrue(operation.matches(TagsUtil.getTags()));
        assertFalse(operation.matches(TagsUtil.getEmptyTags()));
    }

    protected static Stream<OperationTestSource> failSource() {
        return Stream.of(
                new OperationTestSource("type", "road"),
                new OperationTestSource("route", "route")
        );
    }

    @ParameterizedTest
    @MethodSource("failSource")
    @DisplayName("HasTagOperation do not matches")
    void testDoNotMatches(OperationTestSource source) {
        HasTagOperation operation = new HasTagOperation(source.key, source.value);

        assertFalse(operation.matches(TagsUtil.getTags()));
        assertFalse(operation.matches(TagsUtil.getEmptyTags()));
    }

    @Test
    @DisplayName("HasTagOperation toString")
    void testToString() {
        HasTagOperation operation = new HasTagOperation("type", "route");

        assertEquals("type=route", operation.toString());
    }
}
