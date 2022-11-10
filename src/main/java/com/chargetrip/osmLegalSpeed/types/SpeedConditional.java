package com.chargetrip.osmLegalSpeed.types;

import com.chargetrip.osmLegalSpeed.expression.operation.TagOperation;

public class SpeedConditional {
    /**
     * Speed value, in km/h
     */
    public final float speed;

    /**
     * Operation (matchable) for this speed to apply
     */
    public final TagOperation condition;

    public SpeedConditional(float speed, TagOperation condition) {
        this.speed = speed;
        this.condition = condition;
    }

    @Override
    public String toString() {
        return speed + " @ " + condition;
    }
}
