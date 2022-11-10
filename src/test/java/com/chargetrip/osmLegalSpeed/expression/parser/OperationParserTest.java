package com.chargetrip.osmLegalSpeed.expression.parser;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class OperationParserTest {
    @Test
    @DisplayName("OperationParser parse")
    void testParse() {
        try {
            new OperationParser("a<").parse();
            fail();
        } catch (Exception ignored) {
        }
        try {
            new OperationParser("a<>b").parse();
            fail();
        } catch (Exception ignored) {
        }
        try {
            new OperationParser("a<b<c").parse();
            fail();
        } catch (Exception ignored) {
        }
        try {
            new OperationParser("~a!~b").parse();
            fail();
        } catch (Exception ignored) {
        }
        try {
            new OperationParser("~a<b").parse();
            fail();
        } catch (Exception ignored) {
        }
        try {
            assertEquals(new OperationParser("ref").parse().toString(), "ref");
        } catch (Exception e) {
            fail();
        }
        try {
            assertEquals(new OperationParser("!ref").parse().toString(), "!ref");
        } catch (Exception e) {
            fail();
        }
        try {
            assertEquals(new OperationParser("~ref").parse().toString(), "~ref");
        } catch (Exception e) {
            fail();
        }
        try {
            assertEquals(new OperationParser("!~ref").parse().toString(), "!~ref");
        } catch (Exception e) {
            fail();
        }
        try {
            assertEquals(new OperationParser("~ref~motorway").parse().toString(), "~ref~motorway");
        } catch (Exception e) {
            fail();
        }
        try {
            assertEquals(new OperationParser("ref!~motorway").parse().toString(), "ref!~motorway");
        } catch (Exception e) {
            fail();
        }
        try {
            assertEquals(new OperationParser("ref=motorway").parse().toString(), "ref=motorway");
        } catch (Exception e) {
            fail();
        }
        try {
            assertEquals(new OperationParser("ref!=motorway").parse().toString(), "ref!=motorway");
        } catch (Exception e) {
            fail();
        }
        try {
            assertEquals(new OperationParser("a>10").parse().toString(), "a>10.0");
        } catch (Exception e) {
            fail();
        }
        try {
            assertEquals(new OperationParser("a>=10").parse().toString(), "a>=10.0");
        } catch (Exception e) {
            fail();
        }
        try {
            assertEquals(new OperationParser("a<10").parse().toString(), "a<10.0");
        } catch (Exception e) {
            fail();
        }
        try {
            assertEquals(new OperationParser("a<=10").parse().toString(), "a<=10.0");
        } catch (Exception e) {
            fail();
        }
    }
}
