package com.chargetrip.osmLegalSpeed.expression.operation;

import com.chargetrip.osmLegalSpeed.expression.OperationType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class OrOperationTest {
    @Test
    @DisplayName("OrOperation matches true on all children")
    void testMatchesTrueOnAll() {
        OrOperation operation = new OrOperation();

        operation.addChild(new TrueOperation());
        operation.addChild(new TrueOperation());

        assertTrue(operation.matches(TagsUtil.getTags()));
        assertTrue(operation.matches(TagsUtil.getEmptyTags()));
    }

    @Test
    @DisplayName("OrOperation matches false on all children")
    void testMatchesFalseOnAll() {
        OrOperation operation = new OrOperation();

        operation.addChild(new FalseOperation());
        operation.addChild(new FalseOperation());

        assertFalse(operation.matches(TagsUtil.getTags()));
        assertFalse(operation.matches(TagsUtil.getEmptyTags()));
    }

    @Test
    @DisplayName("OrOperation matches true at least on one child")
    void testMatchesPartialFalseOnAll() {
        OrOperation operation = new OrOperation();

        operation.addChild(new FalseOperation());
        operation.addChild(new TrueOperation());

        assertTrue(operation.matches(TagsUtil.getTags()));
        assertTrue(operation.matches(TagsUtil.getEmptyTags()));
    }

    @Test
    @DisplayName("OrOperation toString should merge children")
    void testToString() {
        OrOperation operation = new OrOperation();

        operation.addChild(new FalseOperation());
        operation.addChild(new TrueOperation());

        assertEquals("(false " + OperationType.OR + " true)", operation.toString());
    }
}
