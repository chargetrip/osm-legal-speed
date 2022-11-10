package com.chargetrip.osmLegalSpeed.types;

import com.chargetrip.osmLegalSpeed.expression.operation.*;
import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class RoadTypeTest {
    @Test
    @DisplayName("RoadType fromJSON")
    void testFromJSON() {
        RoadType speedType1 = RoadType.fromJSON(new JSONObject());

        assertNull(speedType1.filter);
        assertNull(speedType1.filterOperation);
        assertNull(speedType1.fuzzyFilter);
        assertNull(speedType1.fuzzyFilterOperation);
        assertNull(speedType1.relationFilter);
        assertNull(speedType1.relationFilterOperation);

        JSONObject object = new JSONObject();
        object.put("filter", "trailer");
        object.put("fuzzyFilter", "ref~motorway");
        object.put("relationFilter", "road=route");

        RoadType speedType2 = RoadType.fromJSON(object);

        assertEquals(speedType2.filter, "trailer");
        assertNull(speedType2.filterOperation);
        assertEquals(speedType2.fuzzyFilter, "ref~motorway");
        assertNull(speedType2.fuzzyFilterOperation);
        assertEquals(speedType2.relationFilter, "road=route");
        assertNull(speedType2.relationFilterOperation);
    }

    @Test
    @DisplayName("RoadType build")
    void testBuild() {
        RoadType roadType = new RoadType();

        try {
            roadType.build();
        } catch (ParseException e) {
            fail();
        }

        assertNull(roadType.filterOperation);
        assertNull(roadType.fuzzyFilterOperation);
        assertNull(roadType.relationFilterOperation);

        roadType.filter = "trailer";
        roadType.fuzzyFilter = "ref~motorway";
        roadType.relationFilter = "road=route and (route or !ref)";

        try {
            roadType.build();
        } catch (ParseException e) {
            fail();
        }

        assertNotNull(roadType.filterOperation);
        assertNotNull(roadType.fuzzyFilterOperation);
        assertNotNull(roadType.relationFilterOperation);
        assertEquals(roadType.filterOperation.toString(), "trailer");
        assertEquals(roadType.fuzzyFilterOperation.toString(), "ref~motorway");
        assertEquals(roadType.relationFilterOperation.toString(), "(road=route and (route or !ref))");
    }

    @Test
    @DisplayName("RoadType matches")
    void testMatches() {
        RoadType roadType = new RoadType();

        assertEquals(roadType.matches(new HashMap<>()), Certitude.Fallback);

        roadType.filterOperation = new HasKeyOperation("trailer");
        roadType.fuzzyFilterOperation = new HasTagValueLikeOperation("ref", "motorway");
        roadType.relationFilterOperation = (new AndOperation()) //
                .addChild(new HasTagValueLikeOperation("road", "route")) //
                .addChild(new OrOperation() //
                        .addChild(new HasKeyOperation("route"))
                        .addChild(new NotHasKeyOperation("ref"))
                )
        ;

        assertEquals(roadType.matches(new HashMap<>()), Certitude.Fallback);
        assertEquals(roadType.matches(Map.of("trailer", "yes")), Certitude.Exact);
        assertEquals(roadType.matches(Map.of("road", "route", "route", "motorway")), Certitude.Exact);
        assertEquals(roadType.matches(Map.of("road", "route")), Certitude.Exact);
        assertEquals(roadType.matches(Map.of("road", "route", "ref", "motorway")), Certitude.Fuzzy);
    }
}
