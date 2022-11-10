package com.chargetrip.osmLegalSpeed.util;

import com.chargetrip.osmLegalSpeed.expression.OperationType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.text.ParseException;

import static org.junit.jupiter.api.Assertions.*;

public class StringCursorTest {
    @Test
    @DisplayName("StringCursor isNextAndAdvance")
    void testIsNextAndAdvance() {
        StringCursor cursor = new StringCursor("highway~motorway|motorway_link");

        assertTrue(cursor.isNextAndAdvance("highway"));
        assertEquals(cursor.currentPosition, 7);
        assertFalse(cursor.isNextAndAdvance("road"));
    }

    @Test
    @DisplayName("StringCursor isNext")
    void testIsNext() {
        StringCursor cursor = new StringCursor("highway~motorway|motorway_link");

        assertTrue(cursor.isNext("highway"));
        assertEquals(cursor.currentPosition, 0);
        assertFalse(cursor.isNext("road"));
    }

    @Test
    @DisplayName("StringCursor advanceBy")
    void testAdvanceBy() {
        StringCursor cursor = new StringCursor("highway~motorway|motorway_link");

        try {
            cursor.advanceBy(-1);
            fail();
        } catch (IndexOutOfBoundsException e) {
            assertTrue(true);
        }

        assertEquals(cursor.advanceBy(1), "h");
        assertEquals(cursor.currentPosition, 1);
        assertEquals(cursor.advanceBy(5), "ighwa");
        assertEquals(cursor.currentPosition, 6);
        assertEquals(cursor.advanceBy(50), "y~motorway|motorway_link");
        assertEquals(cursor.currentPosition, 30);
    }

    @Test
    @DisplayName("StringCursor findPlaceholder")
    void testFindPlaceholder() {
        StringCursor cursor = new StringCursor("{highway} and {motorway} or {motorway_link}");
        try {
            assertEquals(cursor.findPlaceholder(), "highway");
        } catch (ParseException e) {
            fail();
        }

        cursor = new StringCursor("highway");
        try {
            assertNull(cursor.findPlaceholder());
        } catch (ParseException e) {
            fail();
        }

        cursor = new StringCursor("{highway");
        try {
            cursor.findPlaceholder();
            fail();
        } catch (ParseException e) {
            assertTrue(true);
        }
    }

    @Test
    @DisplayName("StringCursor findParentheses")
    void testFindParentheses() {
        StringCursor cursor = new StringCursor("a and b");

        assertNull(cursor.findParentheses());

        cursor = new StringCursor("a and (b or (c and d)) and e");
        int start = cursor.value.indexOf(OperationType.PARENTHESES_START);
        cursor.currentPosition = start + 1;

//        assertNull(cursor.findParentheses());
        assertEquals(cursor.findParentheses(), "b or (c and d)");
    }
}
