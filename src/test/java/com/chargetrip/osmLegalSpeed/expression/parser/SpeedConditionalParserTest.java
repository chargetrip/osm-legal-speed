package com.chargetrip.osmLegalSpeed.expression.parser;

import com.chargetrip.osmLegalSpeed.expression.operation.TrueOperation;
import com.chargetrip.osmLegalSpeed.types.SpeedConditional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SpeedConditionalParserTest {
    @Test
    @DisplayName("SpeedConditionalParser parse")
    void testParse() {
        List<SpeedConditional> validList = new SpeedConditionalParser("100 @ trailer; 90 @ articulated; 50 @ 06:19").parse();
        assertEquals(validList.size(), 3);

        List<SpeedConditional> validTrue = new SpeedConditionalParser("100; 90 @ trailer").parse();
        assertEquals(validTrue.size(), 2);
        assertTrue(validTrue.get(1).condition instanceof TrueOperation);

        List<SpeedConditional> failList = new SpeedConditionalParser("1.a @ trailer; a @ articulated; 10 @ ~ref~(US:").parse();
        assertTrue(failList.isEmpty());
    }
}
