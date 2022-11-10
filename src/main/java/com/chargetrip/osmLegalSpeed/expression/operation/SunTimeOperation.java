package com.chargetrip.osmLegalSpeed.expression.operation;

import org.shredzone.commons.suncalc.SunTimes;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Map;

public class SunTimeOperation implements TimeOperation {
    public final boolean sunsetFirst;
    public final SunOffset sunsetOffset;
    public final SunOffset sunriseOffset;

    public SunTimeOperation(SunOffset sunsetOffset, SunOffset sunriseOffset, boolean sunsetFirst) {
        this.sunsetOffset = sunsetOffset;
        this.sunriseOffset = sunriseOffset;
        this.sunsetFirst = sunsetFirst;
    }

    @Override
    public boolean matches(Map<String, String> object) {
        if (!object.containsKey("latitude") || !object.containsKey("longitude") || !object.containsKey("date")) {
            // If we do not have GPS data, we cannot match this
            return false;
        }

        LocalDateTime date = LocalDateTime.parse(object.get("date"));
        SunDates sunDates = new SunDates(
                date,
                Double.parseDouble(object.get("latitude")),
                Double.parseDouble(object.get("longitude")),
                sunsetFirst,
                sunriseOffset,
                sunsetOffset
        );

        SunTime firstTime = sunDates.getFirstTime(sunsetFirst);
        SunTime lastTime = sunDates.getLastTime(sunsetFirst);
        SunTime currentTime = new SunTime(date.getHour(), date.getMinute());

        int subtractHours = firstTime.hour;
        firstTime.minusHours(subtractHours);
        currentTime.minusHours(subtractHours);
        lastTime.minusHours(subtractHours);

        return firstTime.getTime() <= currentTime.getTime() && currentTime.getTime() <= lastTime.getTime();
    }

    @Override
    public String toString() {
        String sunset = "(sunset" + (sunsetOffset.negative ? "-" : "+") + getFormattedTime(sunsetOffset.hour) + ":" + getFormattedTime(sunsetOffset.minute) + ")";
        String sunrise = "(sunrise" + (sunriseOffset.negative ? "-" : "+") + getFormattedTime(sunriseOffset.hour) + ":" + getFormattedTime(sunriseOffset.minute) + ")";

        return sunsetFirst ? "(" + sunset + "-" + sunrise + ")" : "(" + sunrise + "-" + sunset + ")";
    }

    protected String getFormattedTime(int value) {
        StringBuilder stringValue = new StringBuilder("" + value);
        while (stringValue.length() < 2) {
            stringValue.insert(0, "0");
        }

        return stringValue.toString();
    }

    public static class SunOffset {
        public final boolean negative;
        public final int hour;
        public final int minute;

        public SunOffset(boolean negative, int hour, int minute) {
            this.negative = negative;
            this.hour = hour;
            this.minute = minute;
        }
    }

    public static class SunDates {
        public boolean sunsetFirst;
        public SunTime sunset;
        public SunTime sunrise;

        public SunDates(
                LocalDateTime date,
                double latitude,
                double longitude,
                boolean sunsetFirst,
                SunOffset sunriseOffset,
                SunOffset sunsetOffset
        ) {
            this.sunsetFirst = sunsetFirst;
            SunTimes sunTimes = SunTimes.compute()
                    .on(date.getYear(), date.getMonthValue(), date.getDayOfMonth())
                    .at(latitude, longitude)
                    .execute();

            ZonedDateTime sunset = sunTimes.getSet();
            ZonedDateTime sunrise = sunTimes.getRise();

            this.sunset = new SunTime(sunset.getHour(), sunset.getMinute());
            if (sunsetOffset.negative) {
                this.sunset //
                        .minusHours(sunsetOffset.hour) //
                        .minusMinutes(sunsetOffset.minute) //
                ;
            } else {
                this.sunset //
                        .plusHours(sunsetOffset.hour) //
                        .plusMinutes(sunsetOffset.minute) //
                ;
            }

            this.sunrise = new SunTime(sunrise.getHour(), sunrise.getMinute());
            if (sunriseOffset.negative) {
                this.sunrise //
                        .minusHours(sunriseOffset.hour) //
                        .minusMinutes(sunriseOffset.minute) //
                ;
            } else {
                this.sunrise //
                        .plusHours(sunriseOffset.hour) //
                        .plusMinutes(sunriseOffset.minute) //
                ;
            }
        }

        public SunTime getFirstTime(boolean sunsetFirst) {
            return sunsetFirst ? sunset : sunrise;
        }

        public SunTime getLastTime(boolean sunsetFirst) {
            return sunsetFirst ? sunrise: sunset;
        }
    }

    public static class SunTime {
        public int hour;
        public int minute;

        public SunTime(int hour, int minute) {
            this.hour = hour;
            this.minute = minute;
        }

        public SunTime plusHours(int hours) {
            this.hour += hours;
            while (this.hour >= 24) {
                this.hour -= 24;
            }

            return this;
        }

        public SunTime minusHours(int hours) {
            this.hour -= hours;
            while (this.hour < 0) {
                this.hour += 24;
            }

            return this;
        }

        public SunTime plusMinutes(int minutes) {
            this.minute += minutes;
            while (this.minute >= 60) {
                this.minute -= 60;
            }

            return this;
        }

        public SunTime minusMinutes(int minutes) {
            this.minute -= minutes;
            while (this.minute < 0) {
                this.minute += 60;
            }

            return this;
        }

        public int getTime() {
            return hour * 60 + minute;
        }
    }
}
