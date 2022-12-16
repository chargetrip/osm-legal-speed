package com.chargetrip.osmLegalSpeed.config;

import com.chargetrip.osmLegalSpeed.util.ReaderUtil;

import java.io.InputStream;

public class ResourceInputStream {
    /**
     * Return the countries resource input stream
     *
     * @return The input stream to countries json
     */
    public static InputStream getCountriesInputStream() {
        return ResourceInputStream.class.getResourceAsStream("/com/chargetrip/osmLegalSpeed/countries.json");
    }

    /**
     * Read and return the string content of the countries resource
     *
     * @return Read the content of countries json and return it as string
     */
    public static String readCountries() {
        return ReaderUtil.readInputStreamContent(ResourceInputStream.getCountriesInputStream());
    }

    /**
     * Return the legal default speeds resource input stream
     *
     * @return The input stream to legal speeds json
     */
    public static InputStream getLegalSpeedInputStream() {
        return ResourceInputStream.class.getResourceAsStream("/com/chargetrip/osmLegalSpeed/legal_default_speeds.json");
    }

    /**
     * Read and return the string content of the legal default speeds resource
     *
     * @return Read the content of legal speeds json and return it as string
     */
    public static String readLegalSpeed() {
        return ReaderUtil.readInputStreamContent(ResourceInputStream.getLegalSpeedInputStream());
    }
}
