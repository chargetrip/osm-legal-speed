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

            // How to get speed from maxspeed tag
            options.vehicle = VehicleType.Car;
            tags.put("maxspeed", "50;30");
            System.out.println("From maxspeed with multiple values: " + legalSpeed.getSpeedLimit(tags, options));

            tags.put("maxspeed", "NL:urban");
            System.out.println("From maxspeed with parent rule: " + legalSpeed.getSpeedLimit(tags, options));

            tags.put("maxspeed", "null");
            System.out.println("From null maxspeed: " + legalSpeed.getSpeedLimit(tags, options));

            tags.put("maxspeed", "NL:trunk");
            System.out.println("From maxspeed with parent rule: " + legalSpeed.getSpeedLimit(tags, options));

            tags.put("maxspeed", "none");
            options.latitude = 52.5170365;
            options.longitude = 13.3888599;
            System.out.println("From none maxspeed in DE: " + legalSpeed.getSpeedLimit(tags, options));

            Map<String, String> germanyTags = new HashMap<>();
            germanyTags.put("destination:ref", "B 173");
            germanyTags.put("destination:lanes", "Dresden|Freiberg;Freital;Kesselsdorf");
            germanyTags.put("bdouble", "yes");
            germanyTags.put("oneway", "yes");
            germanyTags.put("turn:lanes", "left|right");
            germanyTags.put("lit", "no");
            germanyTags.put("hazmat", "designated");
            germanyTags.put("lanes", "2");
            germanyTags.put("highway", "motorway_link");
            System.out.println("From motorway_link in DE: " + legalSpeed.getSpeedLimit(germanyTags, options));
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }
}