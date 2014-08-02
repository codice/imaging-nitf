/**
 * Copyright (c) Codice Foundation
 *
 * This is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details. A copy of the GNU Lesser General Public License
 * is distributed along with this program and can be found at
 * <http://www.gnu.org/licenses/lgpl.html>.
 *
 **/
package org.codice.nitf.filereader;

import java.text.ParseException;

public class ImageCoordinatePair {

    private double lat = 0.0;
    private double lon = 0.0;
    private String sourceString = null;

    private static final double MINUTES_IN_ONE_DEGREE = 60.0;
    private static final double SECONDS_IN_ONE_MINUTE = 60.0;
    private static final int LAT_DEGREES_OFFSET = 0;
    private static final int LAT_DEGREES_LENGTH = 2;
    private static final int LAT_MINUTES_OFFSET = LAT_DEGREES_OFFSET + LAT_DEGREES_LENGTH;
    private static final int MINUTES_LENGTH = 2;
    private static final int LAT_SECONDS_OFFSET = LAT_MINUTES_OFFSET + MINUTES_LENGTH;
    private static final int SECONDS_LENGTH = 2;
    private static final int LAT_HEMISPHERE_MARKER_OFFSET = LAT_SECONDS_OFFSET + SECONDS_LENGTH;
    private static final int HEMISPHERE_MARKER_LENGTH = 1;
    private static final int LON_DEGREES_OFFSET = LAT_HEMISPHERE_MARKER_OFFSET + HEMISPHERE_MARKER_LENGTH;
    private static final int LON_DEGREES_LENGTH = 3;
    private static final int LON_MINUTES_OFFSET = LON_DEGREES_OFFSET + LON_DEGREES_LENGTH;
    private static final int LON_SECONDS_OFFSET = LON_MINUTES_OFFSET + SECONDS_LENGTH;
    private static final int LON_HEMISPHERE_MARKER_OFFSET = LON_SECONDS_OFFSET + SECONDS_LENGTH;
    private static final String LAT_DECIMAL_DEGREES_FORMAT = "+dd.ddd";
    private static final int LAT_DECIMAL_DEGREES_FORMAT_LENGTH = LAT_DECIMAL_DEGREES_FORMAT.length();

    public ImageCoordinatePair() {
    }

    public ImageCoordinatePair(final double latitude, final double longitude) {
        lat = latitude;
        lon = longitude;
    }

    public final double getLatitude() {
        return lat;
    }

    public final double getLongitude() {
        return lon;
    }

    /**
        Set latitude and longitude from Degrees/Minutes/Seconds string

        /param dms string representation in MIL-STD-2500C ddmmssXdddmmssY form
     */
    public final void setFromDMS(final String dms) throws ParseException {
        sourceString = dms;
        if (dms.length() != "ddmmssXdddmmssY".length()) {
            throw new ParseException("Incorrect length for DMS parsing", 0);
        }
        String latDegrees = dms.substring(LAT_DEGREES_OFFSET, LAT_DEGREES_LENGTH);
        String latMinutes = dms.substring(LAT_MINUTES_OFFSET, LAT_MINUTES_OFFSET + MINUTES_LENGTH);
        String latSeconds = dms.substring(LAT_SECONDS_OFFSET, LAT_SECONDS_OFFSET + SECONDS_LENGTH);
        String latNS = dms.substring(LAT_HEMISPHERE_MARKER_OFFSET, LAT_HEMISPHERE_MARKER_OFFSET + HEMISPHERE_MARKER_LENGTH);
        String lonDegrees = dms.substring(LON_DEGREES_OFFSET, LON_DEGREES_OFFSET + LON_DEGREES_LENGTH);
        String lonMinutes = dms.substring(LON_MINUTES_OFFSET, LON_MINUTES_OFFSET + MINUTES_LENGTH);
        String lonSeconds = dms.substring(LON_SECONDS_OFFSET, LON_SECONDS_OFFSET + SECONDS_LENGTH);
        String lonEW = dms.substring(LON_HEMISPHERE_MARKER_OFFSET, LON_HEMISPHERE_MARKER_OFFSET + HEMISPHERE_MARKER_LENGTH);
        if ((!"N".equals(latNS)) && (!"S".equals(latNS))) {
            throw new ParseException(String.format("Incorrect format for N/S flag while DMS parsing: %s(%s)", latNS, dms),
                                     LAT_HEMISPHERE_MARKER_OFFSET);
        }
        if ((!"E".equals(lonEW)) && (!"W".equals(lonEW))) {
            throw new ParseException(String.format("Incorrect format for E/W flag while DMS parsing: %s(%s)", lonEW, dms),
                                     LON_HEMISPHERE_MARKER_OFFSET);
        }
        try {
            lat = buildDecimalDegrees(latDegrees, latMinutes, latSeconds);
            if ("S".equals(latNS)) {
                lat = -1.0 * lat;
            }
            lon = buildDecimalDegrees(lonDegrees, lonMinutes, lonSeconds);
            if ("W".equals(lonEW)) {
                lon = -1.0 * lon;
            }
        } catch (NumberFormatException ex) {
            throw new ParseException(String.format("Incorrect DMS format: %s", dms), 0);
        }
    }

    private double buildDecimalDegrees(final String degStr, final String minStr, final String secStr) {
        int degrees = Integer.parseInt(degStr);
        int minutes = Integer.parseInt(minStr);
        int seconds = Integer.parseInt(secStr);
        return degrees + ((minutes + (seconds / SECONDS_IN_ONE_MINUTE)) / MINUTES_IN_ONE_DEGREE);
    }

    public final void setFromUTMUPSNorth(final String utm) throws ParseException {
        if (utm.length() != "zzeeeeeennnnnnn".length()) {
            throw new ParseException("Incorrect length for UTM / UPS North String", 0);
        }
        sourceString = utm;
    }

    public final void setFromGeocentric(final String geocentric) throws ParseException {
        sourceString = geocentric;
        // TODO: add conversion to degrees
    }

    /**
        Set latitude and longitude from Decimal Degrees string

        /param dd string representation in MIL-STD-2500C ±dd.ddd±ddd.ddd form
     */
    public final void setFromDecimalDegrees(final String dd) throws ParseException {
        if (dd.length() != "+dd.ddd+ddd.ddd".length()) {
            throw new ParseException("Incorrect length for decimal degrees parsing", 0);
        }
        sourceString = dd;
        String latPart = dd.substring(0, LAT_DECIMAL_DEGREES_FORMAT_LENGTH);
        String lonPart = dd.substring(LAT_DECIMAL_DEGREES_FORMAT_LENGTH);
        try {
            lat = Double.parseDouble(latPart);
            lon = Double.parseDouble(lonPart);
        } catch (NumberFormatException ex) {
            throw new ParseException(String.format("Incorrect decimal degrees format: %s", dd), 0);
        }
    }

    public final String getSourceFormat() {
        return sourceString;
    }
}
