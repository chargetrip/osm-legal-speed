package com.chargetrip.osmLegalSpeed;

import com.chargetrip.osmLegalSpeed.config.ResourceInputStream;
import com.chargetrip.osmLegalSpeed.types.Certitude;
import com.chargetrip.osmLegalSpeed.types.Options;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class LegalSpeedTest {
    protected static class SpeedLimitTestSource {
        Float speed;
        Options options;
        Map<String, String> tags;

        SpeedLimitTestSource(Float speed, Options options, Map<String, String> tags) {
            this.speed = speed;
            this.options = options;
            this.tags = tags;
        }

        @Override
        public String toString() {
            return speed + ": " + options.getTags() + " -> " + tags;
        }
    }

    protected static Stream<SpeedLimitTestSource> successGetSpeedLimit() {
        Options optionsInNL = new Options();
        optionsInNL.latitude = 52.3727598;
        optionsInNL.longitude = 4.8936041;
        optionsInNL.datetime = LocalDateTime.now().withHour(12).withMinute(0).withSecond(0).withNano(0);

        Options optionsInUS = new Options();
        optionsInUS.latitude = 40.7127281;
        optionsInUS.longitude = -74.0060152;
        optionsInUS.datetime = LocalDateTime.now().withHour(12).withMinute(0).withSecond(0).withNano(0);

        Options optionsNoLat = new Options();
        optionsNoLat.latitude = null;
        optionsNoLat.longitude = -74.0060152;
        optionsNoLat.datetime = LocalDateTime.now().withHour(12).withMinute(0).withSecond(0).withNano(0);
        Options optionsNoLng = new Options();
        optionsNoLng.latitude = 40.7127281;
        optionsNoLng.longitude = null;
        optionsNoLng.datetime = LocalDateTime.now().withHour(12).withMinute(0).withSecond(0).withNano(0);

        return Stream.of(
                new SpeedLimitTestSource(100.0f, optionsInNL, Map.of("highway", "motorway")),
                new SpeedLimitTestSource(null, optionsInUS, Map.of("highway", "motorway")),
                new SpeedLimitTestSource(null, optionsNoLat, Map.of("highway", "motorway")),
                new SpeedLimitTestSource(null, optionsNoLng, Map.of("highway", "motorway"))
        );
    }

    @ParameterizedTest
    @MethodSource("successGetSpeedLimit")
    @DisplayName("LegalSpeed getSpeedLimit")
    void testGetSpeedLimit(SpeedLimitTestSource testSource) {
        MockedStatic<ResourceInputStream> mockedResourceInputStream = getMockedResourceInputStream();
        Float speed = null;

        try {
            LegalSpeed legalSpeed = new LegalSpeed();
            speed = legalSpeed.getSpeedLimit(testSource.tags, testSource.options);
        } catch (Exception e) {
            fail();
        } finally {
            mockedResourceInputStream.close();
        }

        assertEquals(testSource.speed, speed);
    }

    @Test
    @DisplayName("LegalSpeed getCountryWithRegion success")
    void testGetCountryWithRegionSuccess() {
        MockedStatic<ResourceInputStream> mockedResourceInputStream = getMockedResourceInputStream();
        String nl = null;
        String zm = null;
        String us = null;

        try {
            LegalSpeed legalSpeed = new LegalSpeed();
            nl = legalSpeed.getCountryWithRegion(52.3727598, 4.8936041);
            us = legalSpeed.getCountryWithRegion(40.7127281, -74.0060152);
            zm = legalSpeed.getCountryWithRegion(-15.421548305470441, 28.29408786701945);
        } catch (Exception e) {
            fail();
        } finally {
            mockedResourceInputStream.close();
        }

        assertEquals(nl, "NL");
        assertNull(us);
        assertEquals(zm, "ZM");
    }

    @Test
    @DisplayName("LegalSpeed getCountryWithRegion no features loaded")
    void testGetCountryWithRegionWithoutFeatures() {
        MockedStatic<ResourceInputStream> mockedResourceInputStream = getMockedResourceInputStreamNoFeatures();
        String nl = null;

        try {
            LegalSpeed legalSpeed = new LegalSpeed();
            nl = legalSpeed.getCountryWithRegion(52.3727598, 4.8936041);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        } finally {
            mockedResourceInputStream.close();
        }

        assertNull(nl);
    }

    protected static class SearchSpeedLimitsTestSource {
        Certitude certitude;
        String countryWithRegion;
        boolean fuzzySearch;
        Map<String, String> tags;

        SearchSpeedLimitsTestSource(Certitude certitude, String countryWithRegion, Map<String, String> tags, boolean fuzzySearch) {
            this.certitude = certitude;
            this.countryWithRegion = countryWithRegion;
            this.tags = tags;
            this.fuzzySearch = fuzzySearch;
        }

        @Override
        public String toString() {
            return certitude + ": " + countryWithRegion + " | " + fuzzySearch + " -> " + tags;
        }
    }

    protected static Stream<SearchSpeedLimitsTestSource> successSearchSpeedLimits() {
        return Stream.of(
                new SearchSpeedLimitsTestSource(Certitude.FromMaxSpeed, "NL", Map.of("maxspeed", "100"), false),
                new SearchSpeedLimitsTestSource(Certitude.Exact, "NL", Map.of("motorroad", "yes"), true),
                new SearchSpeedLimitsTestSource(Certitude.Exact, "NL-DK", Map.of("motorroad", "yes"), false),
                new SearchSpeedLimitsTestSource(Certitude.Fuzzy, "NL", Map.of("lit", "no"), true),
                new SearchSpeedLimitsTestSource(Certitude.Fallback, "NL", new HashMap<>(), false),
                new SearchSpeedLimitsTestSource(Certitude.Fallback, "NL", new HashMap<>(), true),
                new SearchSpeedLimitsTestSource(null, "US", Map.of("highway", "motorway"), false),
                new SearchSpeedLimitsTestSource(null, "US-PO", Map.of("highway", "motorway"), false)
        );
    }

    @ParameterizedTest
    @MethodSource("successSearchSpeedLimits")
    @DisplayName("LegalSpeed searchSpeedLimits")
    void testSearchSpeedLimits(SearchSpeedLimitsTestSource testSource) {
        MockedStatic<ResourceInputStream> mockedResourceInputStream = getMockedResourceInputStream();
        LegalSpeed.SearchResult searchResult = null;

        try {
            LegalSpeed legalSpeed = new LegalSpeed();
            searchResult = legalSpeed.searchSpeedLimits(testSource.tags, testSource.countryWithRegion, testSource.fuzzySearch);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        } finally {
            mockedResourceInputStream.close();
        }

        if (testSource.certitude != null) {
            assertNotNull(searchResult);
            assertEquals(searchResult.certitude, testSource.certitude);
        } else {
            assertNull(searchResult);
        }
    }

    protected MockedStatic<ResourceInputStream> getMockedResourceInputStream() {
        MockedStatic<ResourceInputStream> mockedResourceInputStream = Mockito.mockStatic(ResourceInputStream.class);
        mockedResourceInputStream.when(ResourceInputStream::readLegalSpeed).thenReturn("{\"roadTypesByName\":{\"living street\":{\"filter\":\"highway=living_street or living_street=yes\"},\"urban\":{\"filter\":\"source:maxspeed~.*urban or maxspeed:type~.*urban or zone:maxspeed~.*urban or zone:traffic~.*urban or maxspeed~.*urban or HFCS~.*Urban.* or rural=no\",\"fuzzyFilter\":\"highway~living_street|residential or lit=yes or {has sidewalk}\"},\"rural\":{\"filter\":\"source:maxspeed~.*rural or maxspeed:type~\\\".*(rural|nsl_single|nsl_dual)\\\" or zone:maxspeed~.*rural or zone:traffic~.*rural or maxspeed~.*rural or HFCS~.*Rural.* or rural=yes\",\"fuzzyFilter\":\"lit=no or {has no sidewalk}\"},\"motorroad\":{\"filter\":\"motorroad=yes\"},\"has sidewalk\":{\"filter\":\"sidewalk~yes|both|left|right|separate or sidewalk:left~yes|separate or sidewalk:right~yes|separate or sidewalk:both~yes|separate\"},\"has no sidewalk\":{\"filter\":\"sidewalk~no|none or sidewalk:both~no|none or (sidewalk:left~no|none and sidewalk:right~no|none)\"},\"motorway\":{\"filter\":\"highway~motorway|motorway_link\"}},\"speedLimitsByCountryCode\":{\"NL\":[{\"name\":\"living street\",\"tags\":{\"maxspeed\":\"15\"}},{\"name\":\"urban\",\"tags\":{\"maxspeed\":\"50\",\"maxspeed:bus\":\"80\",\"maxspeed:coach\":\"100\",\"maxspeed:hgv\":\"80\",\"maxspeed:motorhome:conditional\":\"80 @ (maxweightrating>3.5)\"}},{\"tags\":{\"maxspeed\":\"80\",\"maxspeed:bus\":\"80\",\"maxspeed:coach\":\"100\",\"maxspeed:hgv\":\"80\",\"maxspeed:motorhome:conditional\":\"80 @ (maxweightrating>3.5)\"}},{\"name\":\"rural\",\"tags\":{\"maxspeed\":\"80\",\"maxspeed:bus\":\"80\",\"maxspeed:coach\":\"100\",\"maxspeed:hgv\":\"80\",\"maxspeed:motorhome:conditional\":\"80 @ (maxweightrating>3.5)\"}},{\"name\":\"motorroad\",\"tags\":{\"maxspeed\":\"100\",\"maxspeed:bus\":\"80\",\"maxspeed:coach\":\"100\",\"maxspeed:conditional\":\"90 @ (trailer); 80 @ (maxweightrating>3.5 AND trailer)\",\"maxspeed:hgv\":\"80\",\"maxspeed:motorhome:conditional\":\"80 @ (maxweightrating>3.5)\",\"minspeed\":\"50\"}},{\"name\":\"motorway\",\"tags\":{\"maxspeed\":\"130\",\"maxspeed:bus\":\"80\",\"maxspeed:coach\":\"100\",\"maxspeed:conditional\":\"100 @ (06:00-19:00); 90 @ (trailer); 80 @ (maxweightrating>3.5 AND trailer)\",\"maxspeed:hgv\":\"80\",\"maxspeed:motorhome:conditional\":\"80 @ (maxweightrating>3.5)\",\"minspeed\":\"60\"}}]}}");
        mockedResourceInputStream.when(ResourceInputStream::readCountries).thenReturn("{\"type\":\"FeatureCollection\",\"features\":[{\"type\":\"Feature\",\"properties\":{\"id\":\"NL\"},\"geometry\":{\"type\":\"MultiPolygon\",\"coordinates\":[[[[-70.34259,12.92535],[-70.24399,12.38063],[-69.4514,12.18025],[-68.99639,11.79035],[-68.33524,11.78151],[-68.01417,11.77722],[-67.89186,12.4116],[-68.90012,12.62309],[-69.5195,12.75292],[-70.34259,12.92535]]],[[[-63.58819,17.61311],[-63.22932,17.32592],[-63.11114,17.23125],[-62.76692,17.64353],[-63.07669,17.79659],[-62.93924,18.02904],[-63.02323,18.05757],[-63.04039,18.05619],[-63.0579,18.06614],[-63.07759,18.04943],[-63.09686,18.04608],[-63.11096,18.05368],[-63.13584,18.0541],[-63.33064,17.9615],[-63.29212,17.90532],[-63.58819,17.61311]]],[[[2.56575,51.85301],[3.36263,51.37112],[3.38696,51.33436],[3.35847,51.31572],[3.38289,51.27331],[3.41704,51.25933],[3.43488,51.24135],[3.52698,51.2458],[3.51502,51.28697],[3.58939,51.30064],[3.78999,51.25766],[3.78783,51.2151],[3.90125,51.20371],[3.97889,51.22537],[4.01957,51.24504],[4.05165,51.24171],[4.16721,51.29348],[4.24024,51.35371],[4.21923,51.37443],[4.33265,51.37687],[4.34086,51.35738],[4.39292,51.35547],[4.43777,51.36989],[4.38064,51.41965],[4.39747,51.43316],[4.38122,51.44905],[4.47736,51.4778],[4.5388,51.48184],[4.54675,51.47265],[4.52846,51.45002],[4.53521,51.4243],[4.57489,51.4324],[4.65442,51.42352],[4.72935,51.48424],[4.74578,51.48937],[4.77321,51.50529],[4.78803,51.50284],[4.84139,51.4799],[4.82409,51.44736],[4.82946,51.4213],[4.78314,51.43319],[4.76577,51.43046],[4.77229,51.41337],[4.78941,51.41102],[4.84988,51.41502],[4.90016,51.41404],[4.92152,51.39487],[5.00393,51.44406],[5.0106,51.47167],[5.03281,51.48679],[5.04774,51.47022],[5.07891,51.4715],[5.10456,51.43163],[5.07102,51.39469],[5.13105,51.34791],[5.13377,51.31592],[5.16222,51.31035],[5.2002,51.32243],[5.24244,51.30495],[5.22542,51.26888],[5.23814,51.26064],[5.26461,51.26693],[5.29716,51.26104],[5.33886,51.26314],[5.347,51.27502],[5.41672,51.26248],[5.4407,51.28169],[5.46519,51.2849],[5.48476,51.30053],[5.515,51.29462],[5.5569,51.26544],[5.5603,51.22249],[5.65145,51.19788],[5.65528,51.18736],[5.70344,51.1829],[5.74617,51.18928],[5.77735,51.17845],[5.77697,51.1522],[5.82564,51.16753],[5.85508,51.14445],[5.80798,51.11661],[5.8109,51.10861],[5.83226,51.10585],[5.82921,51.09328],[5.79903,51.09371],[5.79835,51.05834],[5.77258,51.06196],[5.75961,51.03113],[5.77688,51.02483],[5.76242,50.99703],[5.71864,50.96092],[5.72875,50.95428],[5.74752,50.96202],[5.75927,50.95601],[5.74644,50.94723],[5.72545,50.92312],[5.72644,50.91167],[5.71626,50.90796],[5.69858,50.91046],[5.67886,50.88142],[5.64504,50.87107],[5.64009,50.84742],[5.65259,50.82309],[5.70118,50.80764],[5.68995,50.79641],[5.70107,50.7827],[5.68091,50.75804],[5.69469,50.75529],[5.72216,50.76398],[5.73904,50.75674],[5.74356,50.7691],[5.76533,50.78159],[5.77513,50.78308],[5.80673,50.7558],[5.84548,50.76542],[5.84888,50.75448],[5.88734,50.77092],[5.89129,50.75125],[5.89132,50.75124],[5.95942,50.7622],[5.97545,50.75441],[6.01976,50.75398],[6.02624,50.77453],[5.97497,50.79992],[5.98404,50.80988],[6.00462,50.80065],[6.02328,50.81694],[6.01921,50.84435],[6.05623,50.8572],[6.05702,50.85179],[6.07431,50.84674],[6.07693,50.86025],[6.08805,50.87223],[6.07486,50.89307],[6.09297,50.92066],[6.01615,50.93367],[6.02697,50.98303],[5.95282,50.98728],[5.90296,50.97356],[5.90493,51.00198],[5.87849,51.01969],[5.86735,51.05182],[5.9134,51.06736],[5.9541,51.03496],[5.98292,51.07469],[6.16706,51.15677],[6.17384,51.19589],[6.07889,51.17038],[6.07889,51.24432],[6.16977,51.33169],[6.22674,51.36135],[6.22641,51.39948],[6.20654,51.40049],[6.21724,51.48568],[6.18017,51.54096],[6.09055,51.60564],[6.11759,51.65609],[6.02767,51.6742],[6.04091,51.71821],[5.95003,51.7493],[5.98665,51.76944],[5.94568,51.82786],[5.99848,51.83195],[6.06705,51.86136],[6.10337,51.84829],[6.16902,51.84094],[6.11551,51.89769],[6.15349,51.90439],[6.21443,51.86801],[6.29872,51.86801],[6.30593,51.84998],[6.40704,51.82771],[6.38815,51.87257],[6.47179,51.85395],[6.50231,51.86313],[6.58556,51.89386],[6.68386,51.91861],[6.72319,51.89518],[6.82357,51.96711],[6.83035,51.9905],[6.68128,52.05052],[6.76117,52.11895],[6.83984,52.11728],[6.97189,52.20329],[6.9897,52.2271],[7.03729,52.22695],[7.06365,52.23789],[7.02703,52.27941],[7.07044,52.37805],[7.03417,52.40237],[6.99041,52.47235],[6.94293,52.43597],[6.69507,52.488],[6.71641,52.62905],[6.77307,52.65375],[7.04557,52.63318],[7.07253,52.81083],[7.21694,53.00742],[7.17898,53.13817],[7.22681,53.18165],[7.21679,53.20058],[7.19052,53.31866],[7.00198,53.32672],[6.91025,53.44221],[5.45168,54.20039],[2.56575,51.85301]],[[4.91493,51.4353],[4.91935,51.43634],[4.92227,51.44252],[4.91811,51.44621],[4.92287,51.44741],[4.92811,51.4437],[4.92566,51.44273],[4.92815,51.43856],[4.92879,51.44161],[4.93544,51.44634],[4.94025,51.44193],[4.93416,51.44185],[4.93471,51.43861],[4.94265,51.44003],[4.93986,51.43064],[4.92952,51.42984],[4.92652,51.43329],[4.91493,51.4353]],[[4.93295,51.44945],[4.95244,51.45207],[4.9524,51.45014],[4.93909,51.44632],[4.93295,51.44945]]]]}},{\"type\":\"Feature\",\"properties\":{\"id\":\"ZM\"},\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[21.97988,-13.00148],[22.00323,-16.18028],[22.17217,-16.50269],[23.20038,-17.47563],[23.47474,-17.62877],[24.23619,-17.47489],[24.32811,-17.49082],[24.38712,-17.46818],[24.5621,-17.52963],[24.70864,-17.49501],[25.00198,-17.58221],[25.26433,-17.79571],[25.51646,-17.86232],[25.6827,-17.81987],[25.85738,-17.91403],[25.85892,-17.97726],[26.08925,-17.98168],[26.0908,-17.93021],[26.21601,-17.88608],[26.55918,-17.99638],[26.68403,-18.07411],[26.74314,-18.0199],[26.89926,-17.98756],[27.14196,-17.81398],[27.30736,-17.60487],[27.61377,-17.34378],[27.62795,-17.24365],[27.83141,-16.96274],[28.73725,-16.5528],[28.76199,-16.51575],[28.81454,-16.48611],[28.8501,-16.04537],[28.9243,-15.93987],[29.01298,-15.93805],[29.21955,-15.76589],[29.4437,-15.68702],[29.8317,-15.6126],[30.35574,-15.6513],[30.41902,-15.62269],[30.22098,-14.99447],[33.24249,-14.00019],[33.16749,-13.93992],[33.07568,-13.98447],[33.02977,-14.05022],[32.99042,-13.95689],[32.88985,-13.82956],[32.79015,-13.80755],[32.76962,-13.77224],[32.84528,-13.71576],[32.7828,-13.64805],[32.68654,-13.64268],[32.66468,-13.60019],[32.68436,-13.55769],[32.73683,-13.57682],[32.84176,-13.52794],[32.86113,-13.47292],[33.0078,-13.19492],[32.98289,-13.12671],[33.02181,-12.88707],[32.96733,-12.88251],[32.94397,-12.76868],[33.05917,-12.59554],[33.18837,-12.61377],[33.28177,-12.54692],[33.37517,-12.54085],[33.54485,-12.35996],[33.47636,-12.32498],[33.3705,-12.34931],[33.25998,-12.14242],[33.33937,-11.91252],[33.32692,-11.59248],[33.24252,-11.59302],[33.23663,-11.40637],[33.29267,-11.43536],[33.29267,-11.3789],[33.39697,-11.15296],[33.25998,-10.88862],[33.28022,-10.84428],[33.47636,-10.78465],[33.70675,-10.56896],[33.54797,-10.36077],[33.53863,-10.20148],[33.31297,-10.05133],[33.37902,-9.9104],[33.36581,-9.81063],[33.31517,-9.82364],[33.2095,-9.61099],[33.12144,-9.58929],[33.10163,-9.66525],[33.05485,-9.61316],[33.00256,-9.63053],[33.00476,-9.5133],[32.95389,-9.40138],[32.76233,-9.31963],[32.75611,-9.28583],[32.53661,-9.24281],[32.49147,-9.14754],[32.43543,-9.11988],[32.25486,-9.13371],[32.16146,-9.05993],[32.08206,-9.04609],[31.98866,-9.07069],[31.94196,-9.02303],[31.94663,-8.93846],[31.81587,-8.88618],[31.71158,-8.91386],[31.57147,-8.81388],[31.57147,-8.70619],[31.37533,-8.60769],[31.00796,-8.58615],[30.79243,-8.27382],[28.88917,-8.4831],[28.9711,-8.66935],[28.38526,-9.23393],[28.36562,-9.30091],[28.52636,-9.35379],[28.51627,-9.44726],[28.56208,-9.49122],[28.68532,-9.78],[28.62795,-9.92942],[28.65032,-10.65133],[28.37241,-11.57848],[28.48357,-11.87532],[29.18592,-12.37921],[29.4992,-12.43843],[29.48404,-12.23604],[29.8139,-12.14898],[29.81551,-13.44683],[29.65078,-13.41844],[29.60531,-13.21685],[29.01918,-13.41353],[28.33199,-12.41375],[27.59932,-12.22123],[27.21025,-11.76157],[27.22541,-11.60323],[27.04351,-11.61312],[26.88687,-12.01868],[26.01777,-11.91488],[25.33058,-11.65767],[25.34069,-11.19707],[24.42612,-11.44975],[24.34528,-11.06816],[24.00027,-10.89356],[24.02603,-11.15368],[23.98804,-12.13149],[24.06672,-12.29058],[23.90937,-12.844],[24.03339,-12.99091],[21.97988,-13.00148]]]}}]}");

        return mockedResourceInputStream;
    }

    protected MockedStatic<ResourceInputStream> getMockedResourceInputStreamNoFeatures() {
        MockedStatic<ResourceInputStream> mockedResourceInputStream = Mockito.mockStatic(ResourceInputStream.class);
        mockedResourceInputStream.when(ResourceInputStream::readLegalSpeed).thenReturn("{\"roadTypesByName\":{\"living street\":{\"filter\":\"highway=living_street or living_street=yes\"},\"urban\":{\"filter\":\"source:maxspeed~.*urban or maxspeed:type~.*urban or zone:maxspeed~.*urban or zone:traffic~.*urban or maxspeed~.*urban or HFCS~.*Urban.* or rural=no\",\"fuzzyFilter\":\"highway~living_street|residential or lit=yes or {has sidewalk}\"},\"rural\":{\"filter\":\"source:maxspeed~.*rural or maxspeed:type~\\\".*(rural|nsl_single|nsl_dual)\\\" or zone:maxspeed~.*rural or zone:traffic~.*rural or maxspeed~.*rural or HFCS~.*Rural.* or rural=yes\",\"fuzzyFilter\":\"lit=no or {has no sidewalk}\"},\"motorroad\":{\"filter\":\"motorroad=yes\"},\"has sidewalk\":{\"filter\":\"sidewalk~yes|both|left|right|separate or sidewalk:left~yes|separate or sidewalk:right~yes|separate or sidewalk:both~yes|separate\"},\"has no sidewalk\":{\"filter\":\"sidewalk~no|none or sidewalk:both~no|none or (sidewalk:left~no|none and sidewalk:right~no|none)\"},\"motorway\":{\"filter\":\"highway~motorway|motorway_link\"}},\"speedLimitsByCountryCode\":{\"NL\":[{\"name\":\"living street\",\"tags\":{\"maxspeed\":\"15\"}},{\"name\":\"urban\",\"tags\":{\"maxspeed\":\"50\",\"maxspeed:bus\":\"80\",\"maxspeed:coach\":\"100\",\"maxspeed:hgv\":\"80\",\"maxspeed:motorhome:conditional\":\"80 @ (maxweightrating>3.5)\"}},{\"tags\":{\"maxspeed\":\"80\",\"maxspeed:bus\":\"80\",\"maxspeed:coach\":\"100\",\"maxspeed:hgv\":\"80\",\"maxspeed:motorhome:conditional\":\"80 @ (maxweightrating>3.5)\"}},{\"name\":\"rural\",\"tags\":{\"maxspeed\":\"80\",\"maxspeed:bus\":\"80\",\"maxspeed:coach\":\"100\",\"maxspeed:hgv\":\"80\",\"maxspeed:motorhome:conditional\":\"80 @ (maxweightrating>3.5)\"}},{\"name\":\"motorroad\",\"tags\":{\"maxspeed\":\"100\",\"maxspeed:bus\":\"80\",\"maxspeed:coach\":\"100\",\"maxspeed:conditional\":\"90 @ (trailer); 80 @ (maxweightrating>3.5 AND trailer)\",\"maxspeed:hgv\":\"80\",\"maxspeed:motorhome:conditional\":\"80 @ (maxweightrating>3.5)\",\"minspeed\":\"50\"}},{\"name\":\"motorway\",\"tags\":{\"maxspeed\":\"130\",\"maxspeed:bus\":\"80\",\"maxspeed:coach\":\"100\",\"maxspeed:conditional\":\"100 @ (06:00-19:00); 90 @ (trailer); 80 @ (maxweightrating>3.5 AND trailer)\",\"maxspeed:hgv\":\"80\",\"maxspeed:motorhome:conditional\":\"80 @ (maxweightrating>3.5)\",\"minspeed\":\"60\"}}]}}");
        mockedResourceInputStream.when(ResourceInputStream::readCountries).thenReturn("{\"type\":\"FeatureCollection\"}");

        return mockedResourceInputStream;
    }
}
