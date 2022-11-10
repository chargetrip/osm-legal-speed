package com.chargetrip.osmLegalSpeed.expression.operation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class NotHasTagOperationTest {
    protected static Stream<OperationTestSource> successSource() {
        return Stream.of(
                new OperationTestSource("ref", "value"),
                new OperationTestSource("network", "US:CA")
        );
    }

    @ParameterizedTest
    @MethodSource("successSource")
    @DisplayName("NotHasTagOperation matches successfully")
    void testMatchesSuccessfully(OperationTestSource source) {
        NotHasTagOperation operation = new NotHasTagOperation(source.key, source.value);

        assertTrue(operation.matches(TagsUtil.getTags()));
        assertTrue(operation.matches(TagsUtil.getEmptyTags()));
    }

    protected static Stream<OperationTestSource> failSource() {
        return Stream.of(
                new OperationTestSource("ref", "US.rural"),
                new OperationTestSource("route", "road")
        );
    }

    @ParameterizedTest
    @MethodSource("failSource")
    @DisplayName("NotHasTagOperation do not matches")
    void testDoNotMatches(OperationTestSource source) {
        NotHasTagOperation operation = new NotHasTagOperation(source.key, source.value);

        assertFalse(operation.matches(TagsUtil.getTags()));
        assertTrue(operation.matches(TagsUtil.getEmptyTags()));
    }

    @Test
    @DisplayName("NotHasTagOperation toString")
    void testToString() {
        NotHasTagOperation operation = new NotHasTagOperation("route", "road");

        assertEquals("route!=road", operation.toString());
    }
}
