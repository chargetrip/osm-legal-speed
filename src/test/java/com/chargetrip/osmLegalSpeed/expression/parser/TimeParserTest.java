package com.chargetrip.osmLegalSpeed.expression.parser;

import com.chargetrip.osmLegalSpeed.expression.operation.TimeOperation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.text.ParseException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class TimeParserTest {
    @Test
    @DisplayName("TimeParser parse")
    void testExtract() {
        try {
            new TimeParser("invalid").parse();
            fail();
        } catch (ParseException ignored) {
        }
        try {
            TimeOperation operation = new TimeParser("(sunset-sunrise)").parse();

            assertEquals(operation.toString(), "((sunset+00:00)-(sunrise+00:00))");
        } catch (ParseException e) {
            fail();
        }
        try {
            TimeOperation operation = new TimeParser("(sunrise-sunset)").parse();

            assertEquals(operation.toString(), "((sunrise+00:00)-(sunset+00:00))");
        } catch (ParseException e) {
            fail();
        }
        try {
            TimeOperation operation = new TimeParser("((sunset+01:30)-(sunrise-01:30))").parse();

            assertEquals(operation.toString(), "((sunset+01:30)-(sunrise-01:30))");
        } catch (ParseException e) {
            fail();
        }
        try {
            new TimeParser("((sunset_01:30)-(sunrise_01:30))").parse();
            fail();
        } catch (ParseException ignored) {
        }
        try {
            new TimeParser("(sunrise-sunrise)").parse();
            fail();
        } catch (ParseException ignored) {
        }
        try {
            new TimeParser("(sunset-sunset)").parse();
            fail();
        } catch (ParseException ignored) {
        }
        try {
            TimeOperation operation = new TimeParser("06:00-19:00").parse();

            assertEquals(operation.toString(), "(6:0-19:0)");
        } catch (ParseException e) {
            fail();
        }
        try {
            TimeOperation operation = new TimeParser("19:00-06:00").parse();

            assertEquals(operation.toString(), "(19:0-6:0)");
        } catch (ParseException e) {
            fail();
        }
        try {
            TimeOperation operation = new TimeParser("Mo-Fr").parse();

            assertEquals(operation.toString(), "(mo-fr)");
        } catch (ParseException e) {
            fail();
        }
        try {
            TimeOperation operation = new TimeParser("Jan-Dec").parse();

            assertEquals(operation.toString(), "(jan-dec)");
        } catch (ParseException e) {
            fail();
        }
        try {
            TimeOperation operation = new TimeParser("22:00-30:00").parse();

            assertEquals(operation.toString(), "(22:0-6:0)");
        } catch (ParseException e) {
            fail();
        }
    }
}
