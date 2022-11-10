package com.chargetrip.osmLegalSpeed.expression.operation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class HasTagValueLikeOperationTest {
    protected static Stream<OperationTestSource> successSource() {
        return Stream.of(
                new OperationTestSource("network", "US:.*"),
                new OperationTestSource("network", "US:US")
        );
    }

    @ParameterizedTest
    @MethodSource("successSource")
    @DisplayName("HasTagValueLikeOperation matches successfully")
    void testMatchesSuccessfully(OperationTestSource source) {
        HasTagValueLikeOperation operation = new HasTagValueLikeOperation(source.key, source.value);

        assertTrue(operation.matches(TagsUtil.getTags()));
        assertFalse(operation.matches(TagsUtil.getEmptyTags()));
    }

    protected static Stream<OperationTestSource> failSource() {
        return Stream.of(
                new OperationTestSource("network", "CA:.*")
        );
    }

    @ParameterizedTest
    @MethodSource("failSource")
    @DisplayName("HasTagValueLikeOperation do not matches")
    void testDoNotMatches(OperationTestSource source) {
        HasTagValueLikeOperation operation = new HasTagValueLikeOperation(source.key, source.value);

        assertFalse(operation.matches(TagsUtil.getTags()));
        assertFalse(operation.matches(TagsUtil.getEmptyTags()));
    }

    @Test
    @DisplayName("HasTagValueLikeOperation toString")
    void testToString() {
        HasTagValueLikeOperation operation = new HasTagValueLikeOperation("net", "US:.*");

        assertEquals("net~US:.*", operation.toString());
    }
}
