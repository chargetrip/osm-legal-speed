package com.chargetrip.osmLegalSpeed.types;

import com.chargetrip.osmLegalSpeed.expression.operation.HasKeyOperation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SpeedConditionalTest {
    @Test
    @DisplayName("SpeedConditional toString")
    void testMatchesFalseOnAll() {
        SpeedConditional conditional = new SpeedConditional(50, new HasKeyOperation("trailer"));

        assertEquals(conditional.toString(), "50.0 @ trailer");
    }
}
