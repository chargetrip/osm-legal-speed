package com.chargetrip.osmLegalSpeed.expression.operation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class NotHasTagValueLikeOperationTest {
    protected static Stream<OperationTestSource> successSource() {
        return Stream.of(
                new OperationTestSource("ref", "NL.urban")
        );
    }

    @ParameterizedTest
    @MethodSource("successSource")
    @DisplayName("NotHasTagValueLikeOperation matches successfully")
    void testMatchesSuccessfully(OperationTestSource source) {
        NotHasTagValueLikeOperation operation = new NotHasTagValueLikeOperation(source.key, source.value);

        assertTrue(operation.matches(TagsUtil.getTags()));
        assertTrue(operation.matches(TagsUtil.getEmptyTags()));
    }

    protected static Stream<OperationTestSource> failSource() {
        return Stream.of(
                new OperationTestSource("ref", "US\\..*")
        );
    }

    @ParameterizedTest
    @MethodSource("failSource")
    @DisplayName("NotHasTagValueLikeOperation do not matches")
    void testDoNotMatches(OperationTestSource source) {
        NotHasTagValueLikeOperation operation = new NotHasTagValueLikeOperation(source.key, source.value);

        assertFalse(operation.matches(TagsUtil.getTags()));
        assertTrue(operation.matches(TagsUtil.getEmptyTags()));
    }

    @Test
    @DisplayName("NotHasTagValueLikeOperation toString")
    void testToString() {
        NotHasTagValueLikeOperation operation = new NotHasTagValueLikeOperation("route", "road");

        assertEquals("route!~road", operation.toString());
    }
}
