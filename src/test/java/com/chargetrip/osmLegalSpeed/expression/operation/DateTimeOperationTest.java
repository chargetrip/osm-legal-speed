package com.chargetrip.osmLegalSpeed.expression.operation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class DateTimeOperationTest {
    protected static Stream<LocalDateTime[]> successTime() {
        return Stream.of(
                new LocalDateTime[] {
                        LocalDateTime.now().withHour(7).withMinute(0).withSecond(0).withNano(0),
                        LocalDateTime.now().withHour(19).withMinute(0).withSecond(0).withNano(0)
                },
                new LocalDateTime[] {
                        LocalDateTime.now().withHour(12).withMinute(30).withSecond(0).withNano(0),
                        LocalDateTime.now().withHour(19).withMinute(0).withSecond(0).withNano(0)
                },
                new LocalDateTime[] {
                        LocalDateTime.now().withHour(7).withMinute(0).withSecond(0).withNano(0),
                        LocalDateTime.now().withHour(12).withMinute(30).withSecond(0).withNano(0)
                },
                new LocalDateTime[] {
                        LocalDateTime.now().withHour(7).withMinute(0).withSecond(0).withNano(0),
                        null
                },
                new LocalDateTime[] {
                        null,
                        LocalDateTime.now().withHour(12).withMinute(30).withSecond(0).withNano(0)
                }
        );
    }

    protected static Stream<LocalDateTime[]> failTime() {
        return Stream.of(
                new LocalDateTime[] {
                        LocalDateTime.now().withHour(7).withMinute(0).withSecond(0).withNano(0),
                        LocalDateTime.now().withHour(12).withMinute(0).withSecond(0).withNano(0)
                },
                new LocalDateTime[] {
                        LocalDateTime.now().withHour(13).withMinute(0).withSecond(0).withNano(0),
                        LocalDateTime.now().withHour(19).withMinute(0).withSecond(0).withNano(0)
                }
        );
    }

    protected static Stream<Integer[]> successWeek() {
        return Stream.of(
                new Integer[] { 0, 5 },
                new Integer[] { 2, 1 },
                new Integer[] { 5, 2 },
                new Integer[] { 5, -1 },
                new Integer[] { -1, 0 }
        );
    }

    protected static Stream<Integer[]> failWeek() {
        return Stream.of(
                new Integer[] { 3, 5 },
                new Integer[] { 5, 0 },
                new Integer[] { 5, 6 },
                new Integer[] { 1, 1 }
        );
    }

    protected static Stream<Integer[]> successMonth() {
        return Stream.of(
                new Integer[] { 9, 6 },
                new Integer[] { 9, 8 },
                new Integer[] { 6, 1 },
                new Integer[] { 5, 1 },
                new Integer[] { 6, 7 },
                new Integer[] { 5, 7 },
                new Integer[] { 5, 6 },
                new Integer[] { 6, 6 },
                new Integer[] { 6, 0 },
                new Integer[] { 0, 6 }
        );
    }

    protected static Stream<Integer[]> failMonth() {
        return Stream.of(
                new Integer[] { 9, 3 },
                new Integer[] { 9, 10 },
                new Integer[] { 3, 5 }
        );
    }

    protected static Stream<Integer[]> successMonthAndWeek() {
        return Stream.of(
                new Integer[] { 9, 7, 0, 5 },
                new Integer[] { 5, 7, 5, 2 }
        );
    }

    protected static Stream<Integer[]> failMonthAndWeek() {
        return Stream.of(
                new Integer[] { 9, 3, 3, 5 },
                new Integer[] { 3, 5, 5, 0 }
        );
    }

    @ParameterizedTest
    @MethodSource("successTime")
    @DisplayName("DateTimeOperation matches only on time")
    void testMatchesOnlyOnTime(LocalDateTime startTime, LocalDateTime endTime) {
        DateTimeOperation operation = new DateTimeOperation(0, 0, -1, -1, startTime, endTime);

        assertTrue(operation.matches(TagsUtil.getTags()));
        assertFalse(operation.matches(TagsUtil.getEmptyTags()));
    }

    // failTime
    @ParameterizedTest
    @MethodSource("failTime")
    @DisplayName("DateTimeOperation not matches only on time")
    void testNotMatchesOnlyOnTime(LocalDateTime startTime, LocalDateTime endTime) {
        DateTimeOperation operation = new DateTimeOperation(0, 0, -1, -1, startTime, endTime);

        assertFalse(operation.matches(TagsUtil.getTags()));
    }

    @ParameterizedTest
    @MethodSource("successWeek")
    @DisplayName("DateTimeOperation matches only on day of week")
    void testMatchesOnlyOnWeek(Integer startWeek, Integer endWeek) {
        DateTimeOperation operation = new DateTimeOperation(0, 0, startWeek, endWeek, null, null);

        assertTrue(operation.matches(TagsUtil.getTags()));
        assertFalse(operation.matches(TagsUtil.getEmptyTags()));
    }

    @ParameterizedTest
    @MethodSource("failWeek")
    @DisplayName("DateTimeOperation not matches only on day of week")
    void testNotMatchesOnlyOnWeek(Integer startWeek, Integer endWeek) {
        DateTimeOperation operation = new DateTimeOperation(0, 0, startWeek, endWeek, null, null);

        assertFalse(operation.matches(TagsUtil.getTags()));
    }

    @ParameterizedTest
    @MethodSource("successMonth")
    @DisplayName("DateTimeOperation matches only on month")
    void testMatchesOnlyOnMonth(Integer startMonth, Integer endMonth) {
        DateTimeOperation operation = new DateTimeOperation(startMonth, endMonth, -1, -1, null, null);

        assertTrue(operation.matches(TagsUtil.getTags()));
        assertFalse(operation.matches(TagsUtil.getEmptyTags()));
    }

    @ParameterizedTest
    @MethodSource("failMonth")
    @DisplayName("DateTimeOperation not matches only on month")
    void testNotMatchesOnlyOnMonth(Integer startMonth, Integer endMonth) {
        DateTimeOperation operation = new DateTimeOperation(startMonth, endMonth, -1, -1, null, null);

        assertFalse(operation.matches(TagsUtil.getTags()));
    }

    @ParameterizedTest
    @MethodSource("successMonthAndWeek")
    @DisplayName("DateTimeOperation matches only on month and week")
    void testMatchesOnlyOnMonthAndWeek(Integer startMonth, Integer endMonth, Integer startWeek, Integer endWeek) {
        DateTimeOperation operation = new DateTimeOperation(startMonth, endMonth, startWeek, endWeek, null, null);

        assertTrue(operation.matches(TagsUtil.getTags()));
        assertFalse(operation.matches(TagsUtil.getEmptyTags()));
    }

    @ParameterizedTest
    @MethodSource("failMonthAndWeek")
    @DisplayName("DateTimeOperation not matches only on month and week")
    void testNptMatchesOnlyOnMonthAndWeek(Integer startMonth, Integer endMonth, Integer startWeek, Integer endWeek) {
        DateTimeOperation operation = new DateTimeOperation(startMonth, endMonth, startWeek, endWeek, null, null);

        assertFalse(operation.matches(TagsUtil.getTags()));
    }

    @ParameterizedTest
    @MethodSource("successWeek")
    @DisplayName("DateTimeOperation matches only on week and time")
    void testMatchesOnlyOnWeekAndTime(Integer startWeek, Integer endWeek) {
        DateTimeOperation operation = new DateTimeOperation(
                0,
                0,
                startWeek,
                endWeek,
                LocalDateTime.now().withHour(7).withMinute(0).withSecond(0).withNano(0),
                LocalDateTime.now().withHour(19).withMinute(0).withSecond(0).withNano(0)
        );

        assertTrue(operation.matches(TagsUtil.getTags()));
        assertFalse(operation.matches(TagsUtil.getEmptyTags()));
    }

    @ParameterizedTest
    @MethodSource("failWeek")
    @DisplayName("DateTimeOperation not matches only on week and time")
    void testNotMatchesOnlyOnWeekAndTime(Integer startWeek, Integer endWeek) {
        DateTimeOperation operation = new DateTimeOperation(
                0,
                0,
                startWeek,
                endWeek,
                LocalDateTime.now().withHour(14).withMinute(0).withSecond(0).withNano(0),
                LocalDateTime.now().withHour(19).withMinute(0).withSecond(0).withNano(0)
        );

        assertFalse(operation.matches(TagsUtil.getTags()));
    }

    @ParameterizedTest
    @MethodSource("successMonth")
    @DisplayName("DateTimeOperation matches only on month and time")
    void testMatchesOnlyOnMonthAndTime(Integer startMonth, Integer endMonth) {
        DateTimeOperation operation = new DateTimeOperation(
                startMonth,
                endMonth,
                -1,
                -1,
                LocalDateTime.now().withHour(7).withMinute(0).withSecond(0).withNano(0),
                LocalDateTime.now().withHour(19).withMinute(0).withSecond(0).withNano(0)
        );

        assertTrue(operation.matches(TagsUtil.getTags()));
        assertFalse(operation.matches(TagsUtil.getEmptyTags()));
    }

    @ParameterizedTest
    @MethodSource("failMonth")
    @DisplayName("DateTimeOperation not matches only on month and time")
    void testNotMatchesOnlyOnMonthAndTime(Integer startMonth, Integer endMonth) {
        DateTimeOperation operation = new DateTimeOperation(
                startMonth,
                endMonth,
                -1,
                -1,
                LocalDateTime.now().withHour(14).withMinute(0).withSecond(0).withNano(0),
                LocalDateTime.now().withHour(19).withMinute(0).withSecond(0).withNano(0)
        );

        assertFalse(operation.matches(TagsUtil.getTags()));
    }

    @ParameterizedTest
    @MethodSource("successMonthAndWeek")
    @DisplayName("DateTimeOperation matches on all")
    void testMatchesOnAll(Integer startMonth, Integer endMonth, Integer startWeek, Integer endWeek) {
        DateTimeOperation operation = new DateTimeOperation(
                startMonth,
                endMonth,
                startWeek,
                endWeek,
                LocalDateTime.now().withHour(7).withMinute(0).withSecond(0).withNano(0),
                LocalDateTime.now().withHour(19).withMinute(0).withSecond(0).withNano(0)
        );

        assertTrue(operation.matches(TagsUtil.getTags()));
        assertFalse(operation.matches(TagsUtil.getEmptyTags()));
    }

    @ParameterizedTest
    @MethodSource("failMonthAndWeek")
    @DisplayName("DateTimeOperation not matches on all")
    void testNotMatchesOnAll(Integer startMonth, Integer endMonth, Integer startWeek, Integer endWeek) {
        DateTimeOperation operation = new DateTimeOperation(
                startMonth,
                endMonth,
                startWeek,
                endWeek,
                LocalDateTime.now().withHour(14).withMinute(0).withSecond(0).withNano(0),
                LocalDateTime.now().withHour(19).withMinute(0).withSecond(0).withNano(0)
        );

        assertFalse(operation.matches(TagsUtil.getTags()));
    }

    @Test
    @DisplayName("DateTimeOperation toString only on time")
    void testToStringOnlyOnTime() {
        DateTimeOperation operation = new DateTimeOperation(
                0,
                0,
                -1,
                -1,
                LocalDateTime.now().withHour(14).withMinute(0).withSecond(0).withNano(0),
                LocalDateTime.now().withHour(19).withMinute(0).withSecond(0).withNano(0)
        );

        assertEquals(operation.toString(), "(14:0-19:0)");
    }

    @Test
    @DisplayName("DateTimeOperation toString only on partial time")
    void testToStringOnlyOnPartialTime() {
        DateTimeOperation operation1 = new DateTimeOperation(
                0,
                0,
                -1,
                -1,
                LocalDateTime.now().withHour(14).withMinute(0).withSecond(0).withNano(0),
                null
        );
        DateTimeOperation operation2 = new DateTimeOperation(
                0,
                0,
                -1,
                -1,
                null,
                LocalDateTime.now().withHour(19).withMinute(0).withSecond(0).withNano(0)
        );

        assertEquals(operation1.toString(), "()");
        assertEquals(operation2.toString(), "()");
    }

    @Test
    @DisplayName("DateTimeOperation toString only on month")
    void testToStringOnlyOnMonth() {
        DateTimeOperation operation = new DateTimeOperation(1, 12, -1, -1, null, null);

        assertEquals(operation.toString(), "(jan-dec)");
    }

    @Test
    @DisplayName("DateTimeOperation toString only on partial month")
    void testToStringOnlyOnPartialMonth() {
        DateTimeOperation operation1 = new DateTimeOperation(1, 0, -1, -1, null, null);
        DateTimeOperation operation2 = new DateTimeOperation(0, 12, -1, -1, null, null);

        assertEquals(operation1.toString(), "()");
        assertEquals(operation2.toString(), "()");
    }

    @Test
    @DisplayName("DateTimeOperation toString only on day of week")
    void testToStringOnlyOnWeek() {
        DateTimeOperation operation = new DateTimeOperation(0, 0, 0, 1, null, null);

        assertEquals(operation.toString(), "(mo-tu)");
    }

    @Test
    @DisplayName("DateTimeOperation toString only on partial day of week")
    void testToStringOnlyOnPartialWeek() {
        DateTimeOperation operation1 = new DateTimeOperation(0, 0, 0, -1, null, null);
        DateTimeOperation operation2 = new DateTimeOperation(0, 0, -1, 1, null, null);

        assertEquals(operation1.toString(), "()");
        assertEquals(operation2.toString(), "()");
    }

    @Test
    @DisplayName("DateTimeOperation getMonthIndex")
    void testGetMonthIndex() {
        assertEquals(DateTimeOperation.getMonthIndex("jan"), 1);
        assertEquals(DateTimeOperation.getMonthIndex("feb"), 2);
        assertEquals(DateTimeOperation.getMonthIndex("mar"), 3);
        assertEquals(DateTimeOperation.getMonthIndex("apr"), 4);
        assertEquals(DateTimeOperation.getMonthIndex("may"), 5);
        assertEquals(DateTimeOperation.getMonthIndex("jun"), 6);
        assertEquals(DateTimeOperation.getMonthIndex("jul"), 7);
        assertEquals(DateTimeOperation.getMonthIndex("aug"), 8);
        assertEquals(DateTimeOperation.getMonthIndex("sep"), 9);
        assertEquals(DateTimeOperation.getMonthIndex("oct"), 10);
        assertEquals(DateTimeOperation.getMonthIndex("nov"), 11);
        assertEquals(DateTimeOperation.getMonthIndex("dec"), 12);
        assertEquals(DateTimeOperation.getMonthIndex(""), 0);
    }

    @Test
    @DisplayName("DateTimeOperation getMonthName")
    void testGetMonthName() {
        assertEquals(DateTimeOperation.getMonthName(0), "");
        assertEquals(DateTimeOperation.getMonthName(1), "jan");
        assertEquals(DateTimeOperation.getMonthName(2), "feb");
        assertEquals(DateTimeOperation.getMonthName(3), "mar");
        assertEquals(DateTimeOperation.getMonthName(4), "apr");
        assertEquals(DateTimeOperation.getMonthName(5), "may");
        assertEquals(DateTimeOperation.getMonthName(6), "jun");
        assertEquals(DateTimeOperation.getMonthName(7), "jul");
        assertEquals(DateTimeOperation.getMonthName(8), "aug");
        assertEquals(DateTimeOperation.getMonthName(9), "sep");
        assertEquals(DateTimeOperation.getMonthName(10), "oct");
        assertEquals(DateTimeOperation.getMonthName(11), "nov");
        assertEquals(DateTimeOperation.getMonthName(12), "dec");
    }

    @Test
    @DisplayName("DateTimeOperation getWeekIndex")
    void testGetWeekIndex() {
        assertEquals(DateTimeOperation.getWeekIndex(""), -1);
        assertEquals(DateTimeOperation.getWeekIndex("mo"), 0);
        assertEquals(DateTimeOperation.getWeekIndex("tu"), 1);
        assertEquals(DateTimeOperation.getWeekIndex("we"), 2);
        assertEquals(DateTimeOperation.getWeekIndex("th"), 3);
        assertEquals(DateTimeOperation.getWeekIndex("fr"), 4);
        assertEquals(DateTimeOperation.getWeekIndex("sa"), 5);
        assertEquals(DateTimeOperation.getWeekIndex("su"), 6);
    }

    @Test
    @DisplayName("DateTimeOperation getWeekName")
    void testGetWeekName() {
        assertEquals(DateTimeOperation.getWeekName(-1), "");
        assertEquals(DateTimeOperation.getWeekName(0), "mo");
        assertEquals(DateTimeOperation.getWeekName(1), "tu");
        assertEquals(DateTimeOperation.getWeekName(2), "we");
        assertEquals(DateTimeOperation.getWeekName(3), "th");
        assertEquals(DateTimeOperation.getWeekName(4), "fr");
        assertEquals(DateTimeOperation.getWeekName(5), "sa");
        assertEquals(DateTimeOperation.getWeekName(6), "su");
    }
}
