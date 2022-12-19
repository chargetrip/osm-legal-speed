package com.chargetrip.osmLegalSpeed.expression.parser;

import com.chargetrip.osmLegalSpeed.expression.operation.OrOperation;
import com.chargetrip.osmLegalSpeed.expression.operation.TrueOperation;
import com.chargetrip.osmLegalSpeed.types.SpeedConditional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SpeedConditionalParserTest {
    @Test
    @DisplayName("SpeedConditionalParser parse")
    void testParse() throws ParseException {
        List<SpeedConditional> validList = new SpeedConditionalParser("100 @ trailer; 90 @ articulated; 50 @ 06:19").parse();
        assertEquals(validList.size(), 3);

        List<SpeedConditional> validTrue = new SpeedConditionalParser("100; 90 @ trailer").parse();
        assertEquals(validTrue.size(), 2);
        assertTrue(validTrue.get(1).condition instanceof TrueOperation);

        List<SpeedConditional> failList = new SpeedConditionalParser("1.a @ trailer; a @ articulated").parse();
        assertTrue(failList.isEmpty());

        List<SpeedConditional> validComma = new SpeedConditionalParser("90 @ (trailer; articulated)").parse();
        assertEquals(validComma.size(), 1);
        assertTrue(validComma.get(0).condition instanceof OrOperation);

        List<SpeedConditional> invalidComma = new SpeedConditionalParser("90 @ (trailer; a>b)").parse();
        assertEquals(invalidComma.size(), 1);
        assertTrue(invalidComma.get(0).condition instanceof OrOperation);

        List<SpeedConditional> invalidSpeed = new SpeedConditionalParser("9a0").parse();
        assertTrue(invalidSpeed.isEmpty());

        List<SpeedConditional> invalidExpression = new SpeedConditionalParser("90 @ (a>9b9)").parse();
        assertFalse(invalidExpression.isEmpty());
    }
}
