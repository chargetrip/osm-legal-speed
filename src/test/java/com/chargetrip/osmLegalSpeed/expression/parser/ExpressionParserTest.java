package com.chargetrip.osmLegalSpeed.expression.parser;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class ExpressionParserTest {
    @Test
    @DisplayName("ExpressionParser parse")
    void testParse() {
        try {
            new ExpressionParser("(ref").parse();
            fail();
        } catch (Exception ignored) {
        }
        try {
            new ExpressionParser("ref~\"US").parse();
            fail();
        } catch (Exception ignored) {
        }
        try {
            assertEquals(new ExpressionParser("ref").parse().toString(), "ref");
        } catch (Exception e) {
            fail();
        }
        try {
            assertEquals(new ExpressionParser("(06:00-12:00) or (sunset-sunrise) or !(ref or road)").parse().toString(), "((6:0-12:0) or sunset-sunrise or (ref or road))");
        } catch (Exception e) {
            fail();
        }
        try {
            assertEquals(new ExpressionParser("road~\"(US|CA).*\"").parse().toString(), "road~(US|CA).*");
        } catch (Exception e) {
            fail();
        }
        try {
            assertEquals(new ExpressionParser("(~)").parse().toString(), "~");
        } catch (Exception e) {
            fail();
        }
        try {
            assertEquals(new ExpressionParser("(~ref!~ref)").parse().toString(), "()");
        } catch (Exception e) {
            fail();
        }
    }
}
