package com.chargetrip.osmLegalSpeed.expression.operation;

import com.chargetrip.osmLegalSpeed.expression.OperationType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AndOperationTest {
    @Test
    @DisplayName("AndOperation matches true on all children")
    void testMatchesTrueOnAll() {
        AndOperation operation = new AndOperation();

        operation.addChild(new TrueOperation());
        operation.addChild(new TrueOperation());

        assertTrue(operation.matches(TagsUtil.getTags()));
        assertTrue(operation.matches(TagsUtil.getEmptyTags()));
    }

    @Test
    @DisplayName("AndOperation matches false on all children")
    void testMatchesFalseOnAll() {
        AndOperation operation = new AndOperation();

        operation.addChild(new FalseOperation());
        operation.addChild(new FalseOperation());

        assertFalse(operation.matches(TagsUtil.getTags()));
        assertFalse(operation.matches(TagsUtil.getEmptyTags()));
    }

    @Test
    @DisplayName("AndOperation matches false at least on one child")
    void testMatchesPartialFalseOnAll() {
        AndOperation operation = new AndOperation();

        operation.addChild(new FalseOperation());
        operation.addChild(new TrueOperation());
        operation.addChild(new TrueOperation());

        assertFalse(operation.matches(TagsUtil.getTags()));
        assertFalse(operation.matches(TagsUtil.getEmptyTags()));
    }

    @Test
    @DisplayName("AndOperation toString should merge children")
    void testToString() {
        AndOperation operation = new AndOperation();

        operation.addChild(new FalseOperation());
        operation.addChild(new TrueOperation());

        assertEquals("(false " + OperationType.AND + " true)", operation.toString());
    }
}