package com.chargetrip.osmLegalSpeed.expression.operation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class NotHasKeyOperationTest {
    protected static Stream<OperationTestSource> successSource() {
        return Stream.of(
                new OperationTestSource("ten"),
                new OperationTestSource("refe")
        );
    }

    @ParameterizedTest
    @MethodSource("successSource")
    @DisplayName("NotHasKeyOperation matches successfully")
    void testMatchesSuccessfully(OperationTestSource source) {
        NotHasKeyOperation operation = new NotHasKeyOperation(source.key);

        assertTrue(operation.matches(TagsUtil.getTags()));
        assertTrue(operation.matches(TagsUtil.getEmptyTags()));
    }

    protected static Stream<OperationTestSource> failSource() {
        return Stream.of(
                new OperationTestSource("network"),
                new OperationTestSource("ref")
        );
    }

    @ParameterizedTest
    @MethodSource("failSource")
    @DisplayName("NotHasKeyOperation do not matches")
    void testDoNotMatches(OperationTestSource source) {
        NotHasKeyOperation operation = new NotHasKeyOperation(source.key);

        assertFalse(operation.matches(TagsUtil.getTags()));
        assertTrue(operation.matches(TagsUtil.getEmptyTags()));
    }

    @Test
    @DisplayName("NotHasKeyOperation toString")
    void testToString() {
        NotHasKeyOperation operation = new NotHasKeyOperation("net");

        assertEquals("!net", operation.toString());
    }
}
