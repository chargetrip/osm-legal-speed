package com.chargetrip.osmLegalSpeed.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StringUtilTest {
    @Test
    @DisplayName("StringUtil stripAndUnescapeQuotes")
    void testStripAndUnescapeQuotes() {
        new StringUtil();

        assertEquals(StringUtil.stripAndUnescapeQuotes("\"' - '\""), "-");
    }
}
