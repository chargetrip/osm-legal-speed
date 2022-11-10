package com.chargetrip.osmLegalSpeed.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ReaderUtil {
    /**
     * Reading the content of am input stream
     *
     * @param inputStream The input stream
     */
    public static String readInputStreamContent(InputStream inputStream) {
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        StringBuilder stringBuilder = new StringBuilder();

        try {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }

            bufferedReader.close();
            inputStreamReader.close();
        } catch (IOException ignored) {
        }

        return stringBuilder.toString();
    }
}
