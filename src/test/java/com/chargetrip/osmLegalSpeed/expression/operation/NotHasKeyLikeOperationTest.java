package com.chargetrip.osmLegalSpeed.expression.operation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class NotHasKeyLikeOperationTest {
    protected static Stream<OperationTestSource> successSource() {
        return Stream.of(
                new OperationTestSource("ten.*"),
                new OperationTestSource("refe.*")
        );
    }

    @ParameterizedTest
    @MethodSource("successSource")
    @DisplayName("NotHasKeyLikeOperation matches successfully")
    void testMatchesSuccessfully(OperationTestSource source) {
        NotHasKeyLikeOperation operation = new NotHasKeyLikeOperation(source.key);

        assertTrue(operation.matches(TagsUtil.getTags()));
        assertTrue(operation.matches(TagsUtil.getEmptyTags()));
    }

    protected static Stream<OperationTestSource> failSource() {
        return Stream.of(
                new OperationTestSource("net.*"),
                new OperationTestSource("ref.*")
        );
    }

    @ParameterizedTest
    @MethodSource("failSource")
    @DisplayName("NotHasKeyLikeOperation do not matches")
    void testDoNotMatches(OperationTestSource source) {
        NotHasKeyLikeOperation operation = new NotHasKeyLikeOperation(source.key);

        assertFalse(operation.matches(TagsUtil.getTags()));
        assertTrue(operation.matches(TagsUtil.getEmptyTags()));
    }

    @Test
    @DisplayName("NotHasKeyLikeOperation toString")
    void testToString() {
        NotHasKeyLikeOperation operation = new NotHasKeyLikeOperation("net");

        assertEquals("!~net", operation.toString());
    }
}
