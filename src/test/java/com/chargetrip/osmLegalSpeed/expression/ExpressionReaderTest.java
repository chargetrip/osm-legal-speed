package com.chargetrip.osmLegalSpeed.expression;

import com.chargetrip.osmLegalSpeed.config.ResourceInputStream;
import com.chargetrip.osmLegalSpeed.types.RoadType;
import com.chargetrip.osmLegalSpeed.types.SpeedType;
import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class ExpressionReaderTest {
    @Test
    @DisplayName("ExpressionReader replacePlaceholders success")
    void testReplacePlaceholdersSuccess() {
        ExpressionReader reader = new ExpressionReader();
        reader.roadTypes = new HashMap<>();
        reader.roadTypes.put("filter", new RoadType());
        reader.roadTypes.get("filter").filter = "ref";
        reader.roadTypes.put("fuzzyFilter", new RoadType());
        reader.roadTypes.get("fuzzyFilter").fuzzyFilter = "highway=primary";
        reader.roadTypes.put("relationFilter", new RoadType());
        reader.roadTypes.get("relationFilter").relationFilter = "type=route and route=road and name~\"RN(IE)?.*\"";
        reader.roadTypes.put("filter fuzzyFilter", new RoadType());
        reader.roadTypes.get("filter fuzzyFilter").filter = "{filter}";
        reader.roadTypes.get("filter fuzzyFilter").fuzzyFilter = "{filter} and {fuzzyFilter}";
        reader.roadTypes.put("filter relationFilter", new RoadType());
        reader.roadTypes.get("filter relationFilter").filter = "{filter}";
        reader.roadTypes.get("filter relationFilter").relationFilter = "{filter} and {relationFilter}";
        reader.roadTypes.put("fuzzyFilter relationFilter", new RoadType());
        reader.roadTypes.get("fuzzyFilter relationFilter").fuzzyFilter = "{fuzzyFilter}";
        reader.roadTypes.get("fuzzyFilter relationFilter").relationFilter = "{filter} and {relationFilter}";
        reader.roadTypes.put("filter fuzzyFilter relationFilter", new RoadType());
        reader.roadTypes.get("filter fuzzyFilter relationFilter").filter = "{filter}";
        reader.roadTypes.get("filter fuzzyFilter relationFilter").fuzzyFilter = "{fuzzyFilter}";
        reader.roadTypes.get("filter fuzzyFilter relationFilter").relationFilter = "{filter} and {relationFilter}";

        reader.replacePlaceholders();

        for (String key : reader.roadTypes.keySet()) {
            RoadType roadType = reader.roadTypes.get(key);

            if (roadType.filter != null) {
                assertFalse(roadType.filter.contains(OperationType.PLACEHOLDER_START));
                assertFalse(roadType.filter.contains(OperationType.PLACEHOLDER_END));
            }
            if (roadType.fuzzyFilter != null) {
                assertFalse(roadType.fuzzyFilter.contains(OperationType.PLACEHOLDER_START));
                assertFalse(roadType.fuzzyFilter.contains(OperationType.PLACEHOLDER_END));
            }
            if (roadType.relationFilter != null) {
                assertFalse(roadType.relationFilter.contains(OperationType.PLACEHOLDER_START));
                assertFalse(roadType.relationFilter.contains(OperationType.PLACEHOLDER_END));
            }
        }
    }

    @Test
    @DisplayName("ExpressionReader replacePlaceholders fail")
    void testReplacePlaceholdersFail() {
        ExpressionReader reader = new ExpressionReader();
        reader.roadTypes = new HashMap<>();
        reader.roadTypes.put("filter", new RoadType());
        reader.roadTypes.get("filter").filter = "ref";
        reader.roadTypes.put("fuzzyFilter", new RoadType());
        reader.roadTypes.get("fuzzyFilter").fuzzyFilter = "highway=primary";
        reader.roadTypes.put("relationFilter", new RoadType());
        reader.roadTypes.get("relationFilter").relationFilter = "type=route and route=road and name~\"RN(IE)?.*\"";
        reader.roadTypes.put("placeholder not found", new RoadType());
        reader.roadTypes.get("placeholder not found").filter = "{filter should not be found}";
        reader.roadTypes.put("placeholder value not found", new RoadType());
        reader.roadTypes.get("placeholder value not found").relationFilter = "{fuzzyFilter} and {relationFilter}";
        reader.roadTypes.put("invalid placeholder", new RoadType());
        reader.roadTypes.get("invalid placeholder").fuzzyFilter = "{fuzzyFilter";

        reader.replacePlaceholders();

        for (String key : new String[] { "placeholder not found", "placeholder value not found", "invalid placeholder" }) {
            RoadType roadType = reader.roadTypes.get(key);

            if (roadType.filter != null) {
                assertTrue(roadType.filter.contains(OperationType.PLACEHOLDER_START));
            }
            if (roadType.fuzzyFilter != null) {
                assertTrue(roadType.fuzzyFilter.contains(OperationType.PLACEHOLDER_START));
            }
            if (roadType.relationFilter != null) {
                assertTrue(roadType.relationFilter.contains(OperationType.PLACEHOLDER_START));
            }
        }
    }

    @Test
    @DisplayName("ExpressionReader buildRoadTypes")
    void testBuildRoadTypes() {
        ExpressionReader reader = new ExpressionReader();
        reader.roadTypes = new HashMap<>();
        reader.roadTypes.put("filter", new RoadType());
        reader.roadTypes.get("filter").filter = "ref";

        try {
            reader.buildRoadTypes();
        } catch (ParseException e) {
            fail();
        }

        assertNotNull(reader.roadTypes.get("filter").filterOperation);
    }

    @Test
    @DisplayName("ExpressionReader buildSpeedLimits")
    void testBuildSpeedLimits() {
        SpeedType speedType = new SpeedType();
        speedType.name = "filter";
        speedType.tags = Map.of("maxspeed", "100");

        ExpressionReader reader = new ExpressionReader();
        reader.roadTypes = new HashMap<>();
        reader.roadTypes.put("filter", new RoadType());
        reader.roadTypes.get("filter").filter = "ref";
        reader.speedLimits.put("NL", new ArrayList<>());
        reader.speedLimits.get("NL").add(speedType);

        reader.buildSpeedLimits();

        assertNotNull(reader.speedLimits.get("NL").get(0).vehicleSpeedType);
        assertFalse(reader.speedLimits.get("NL").get(0).vehicleSpeedType.isEmpty());
    }

    @Test
    @DisplayName("ExpressionReader fromJSON")
    void testFromJSON() {
        JSONObject object = new JSONObject("{\"roadTypesByName\": {\"rural highway\": {\"filter\": \"ref~\\\"(US|AL|I).*\\\"\",\"fuzzyFilter\": \"highway~trunk|trunk_link|primary|primary_link\",\"relationFilter\": \"type=route and route=road and network~\\\"US:(AL|US)(:.*)?\\\"\"},\"rural highway with 2 or more lanes in each direction\": {\"filter\": \"{Alabama: rural highway} and {road with 2 or more lanes in each direction}\"},\"Carretera general\": {\"filter\": \"ref~CG.*\",\"fuzzyFilter\": \"highway=primary\"},\"Route nationale\": {\"filter\": \"ref~\\\"RN(IE)?.*\\\"\",\"relationFilter\": \"type=route and route=road and name~\\\"RN(IE)?.*\\\"\"}},\n" +
                "  \"speedLimitsByCountryCode\": {\"NL\": [{\"tags\": {\"maxspeed\": \"80\",\"maxspeed:bus\": \"80\",\"maxspeed:coach\": \"100\",\"maxspeed:hgv\": \"80\",\"maxspeed:motorhome:conditional\": \"80 @ (maxweightrating>3.5)\"}},{\"name\": \"rural\",\"tags\": {\"maxspeed\": \"80\",\"maxspeed:bus\": \"80\",\"maxspeed:coach\": \"100\",\"maxspeed:hgv\": \"80\",\"maxspeed:motorhome:conditional\": \"80 @ (maxweightrating>3.5)\"}},{\"name\": \"motorway\",\"tags\": {\"maxspeed\": \"130\",\"maxspeed:bus\": \"80\",\"maxspeed:coach\": \"100\",\"maxspeed:conditional\": \"100 @ (06:00-19:00); 90 @ (trailer); 80 @ (maxweightrating>3.5 AND trailer)\",\"maxspeed:hgv\": \"80\",\"maxspeed:motorhome:conditional\": \"80 @ (maxweightrating>3.5)\"}}]}}");

        ExpressionReader reader = ExpressionReader.fromJSON(object);

        assertNotNull(reader.roadTypes);
        assertFalse(reader.roadTypes.isEmpty());
        assertTrue(reader.roadTypes.containsKey("rural highway"));
        assertTrue(reader.roadTypes.containsKey("rural highway with 2 or more lanes in each direction"));
        assertTrue(reader.roadTypes.containsKey("Carretera general"));
        assertTrue(reader.roadTypes.containsKey("Route nationale"));
        assertNotNull(reader.speedLimits);
        assertFalse(reader.speedLimits.isEmpty());
        assertTrue(reader.speedLimits.containsKey("NL"));
        assertEquals(reader.speedLimits.get("NL").size(), 3);
        assertEquals(reader.speedLimits.get("NL").get(0).name, "motorway");
        assertEquals(reader.speedLimits.get("NL").get(1).name, "rural");
        assertNull(reader.speedLimits.get("NL").get(2).name);

        ExpressionReader reader2 = ExpressionReader.fromJSON(new JSONObject());
        assertTrue(reader2.roadTypes.isEmpty());
        assertTrue(reader2.speedLimits.isEmpty());
    }

    @Test
    @DisplayName("ExpressionReader read")
    void testRead() {
        MockedStatic<ResourceInputStream> mockedResourceInputStream = Mockito.mockStatic(ResourceInputStream.class);
        mockedResourceInputStream.when(ResourceInputStream::readLegalSpeed).thenReturn("{\"roadTypesByName\": {\"rural highway\": {\"filter\": \"ref~\\\"(US|AL|I).*\\\"\",\"fuzzyFilter\": \"highway~trunk|trunk_link|primary|primary_link\",\"relationFilter\": \"type=route and route=road and network~\\\"US:(AL|US)(:.*)?\\\"\"},\"rural highway with 2 or more lanes in each direction\": {\"filter\": \"ref\"},\"Carretera general\": {\"filter\": \"ref~CG.*\",\"fuzzyFilter\": \"highway=primary\"},\"Route nationale\": {\"filter\": \"ref~\\\"RN(IE)?.*\\\"\",\"relationFilter\": \"type=route and route=road and name~\\\"RN(IE)?.*\\\"\"}},\n" +
                "  \"speedLimitsByCountryCode\": {\"NL\": [{\"tags\": {\"maxspeed\": \"80\",\"maxspeed:bus\": \"80\",\"maxspeed:coach\": \"100\",\"maxspeed:hgv\": \"80\",\"maxspeed:motorhome:conditional\": \"80 @ (maxweightrating>3.5)\"}},{\"name\": \"rural\",\"tags\": {\"maxspeed\": \"80\",\"maxspeed:bus\": \"80\",\"maxspeed:coach\": \"100\",\"maxspeed:hgv\": \"80\",\"maxspeed:motorhome:conditional\": \"80 @ (maxweightrating>3.5)\"}},{\"name\": \"motorway\",\"tags\": {\"maxspeed\": \"130\",\"maxspeed:bus\": \"80\",\"maxspeed:coach\": \"100\",\"maxspeed:conditional\": \"100 @ (06:00-19:00); 90 @ (trailer); 80 @ (maxweightrating>3.5 AND trailer)\",\"maxspeed:hgv\": \"80\",\"maxspeed:motorhome:conditional\": \"80 @ (maxweightrating>3.5)\"}}]}}");

        try {
            ExpressionReader reader = ExpressionReader.read();

            mockedResourceInputStream.close();

            assertNotNull(reader.roadTypes);
            assertFalse(reader.roadTypes.isEmpty());
            assertNotNull(reader.speedLimits);
            assertFalse(reader.speedLimits.isEmpty());
        } catch (ParseException e) {
            mockedResourceInputStream.close();

            fail();
        }
    }
}
