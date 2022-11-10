package com.chargetrip.osmLegalSpeed.expression.operation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class HasTagLikeOperationTest {
    protected static Stream<OperationTestSource> successSource() {
        return Stream.of(
                new OperationTestSource("net.*", "US:.*"),
                new OperationTestSource("network", "US:.*"),
                new OperationTestSource("network", "US:US")
        );
    }

    @ParameterizedTest
    @MethodSource("successSource")
    @DisplayName("HasTagLikeOperation matches successfully")
    void testMatchesSuccessfully(OperationTestSource source) {
        HasTagLikeOperation operation = new HasTagLikeOperation(source.key, source.value);

        assertTrue(operation.matches(TagsUtil.getTags()));
        assertFalse(operation.matches(TagsUtil.getEmptyTags()));
    }

    protected static Stream<OperationTestSource> failSource() {
        return Stream.of(
                new OperationTestSource("ten.*", "US:.*"),
                new OperationTestSource("net.*", "CA:.*")
        );
    }

    @ParameterizedTest
    @MethodSource("failSource")
    @DisplayName("HasTagLikeOperation do not matches")
    void testDoNotMatches(OperationTestSource source) {
        HasTagLikeOperation operation = new HasTagLikeOperation(source.key, source.value);

        assertFalse(operation.matches(TagsUtil.getTags()));
        assertFalse(operation.matches(TagsUtil.getEmptyTags()));
    }

    @Test
    @DisplayName("HasTagLikeOperation toString")
    void testToString() {
        HasTagLikeOperation operation = new HasTagLikeOperation("net", "US:.*");

        assertEquals("~net~US:.*", operation.toString());
    }
}
