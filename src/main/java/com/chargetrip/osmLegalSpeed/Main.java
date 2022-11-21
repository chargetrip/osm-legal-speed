package com.chargetrip.osmLegalSpeed;

import com.chargetrip.osmLegalSpeed.types.Options;
import com.chargetrip.osmLegalSpeed.types.VehicleType;

import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Example class on how the SDK works
 */
public class Main {
    /**
     * Main method as an example
     *
     * @param args Input arguments
     */
    public static void main(String[] args) {
        Map<String, String> tags = new HashMap<>();
        tags.put("type", "route");
        tags.put("route", "road");
        tags.put("highway", "motorway");

        try {
            Options options = new Options();
            options.latitude = 52.3727598;
            options.longitude = 4.8936041;
            options.datetime = LocalDateTime.now().withHour(12).withMinute(0);

            LegalSpeed legalSpeed = new LegalSpeed();

            // How to get a country with region code
            String countryWithRegion = legalSpeed.getCountryWithRegion(options.latitude, options.longitude);
            System.out.println("Country with region: " + countryWithRegion);

            // How to search for max speed tags
            LegalSpeed.SearchResult searchResult = legalSpeed.searchSpeedLimits(tags, countryWithRegion, true);
            System.out.println("SearchResult: ");
            System.out.println("- certitude: " + searchResult.certitude);
            System.out.println("- tags: " + searchResult.speedType);

            // How to get the legal speed for all vehicles
            System.out.println("Legal speeds:");
            for (VehicleType vehicle : VehicleType.values()) {
                options.vehicle = vehicle;
                System.out.println("- " + vehicle + ": " + legalSpeed.getSpeedLimit(tags, options));
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }
}