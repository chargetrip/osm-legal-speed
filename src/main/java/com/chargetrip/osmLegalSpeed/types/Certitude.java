package com.chargetrip.osmLegalSpeed.types;

public enum Certitude {
    /**
     * It is an exact match with the road type. I.e., the tag filter for the road type matched.
     */
    Exact,

    /**
     * The road type was inferred from the `maxspeed` given in the input
     */
    FromMaxSpeed,

    /**
     * It can be assumed with reasonable certainty that the match is of the given road type.
     * I.e., the fuzzy tag filter for the road type matched.
     */
    Fuzzy,

    /**
     * No road type matched, falling back to the default speed limit for "other roads". No tag
     * filter matched.
     */
    Fallback
}
