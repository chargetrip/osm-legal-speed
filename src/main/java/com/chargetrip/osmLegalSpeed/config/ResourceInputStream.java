package com.chargetrip.osmLegalSpeed.config;

import com.chargetrip.osmLegalSpeed.util.ReaderUtil;

import java.io.InputStream;

public class ResourceInputStream {
    /**
     * Return the countries resource input stream
     */
    public static InputStream getCountriesInputStream() {
        return ResourceInputStream.class.getResourceAsStream("/com/chargetrip/osmLegalSpeed/countries.json");
    }

    /**
     * Read and return the string content of the countries resource
     */
    public static String readCountries() {
        return ReaderUtil.readInputStreamContent(ResourceInputStream.getCountriesInputStream());
    }

    /**
     * Return the legal default speeds resource input stream
     */
    public static InputStream getLegalSpeedInputStream() {
        return ResourceInputStream.class.getResourceAsStream("/com/chargetrip/osmLegalSpeed/legal_default_speeds.json");
    }

    /**
     * Read and return the string content of the legal default speeds resource
     */
    public static String readLegalSpeed() {
        return ReaderUtil.readInputStreamContent(ResourceInputStream.getLegalSpeedInputStream());
    }
}
