/*
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
 */
package org.codice.imaging.nitf.image;

import java.text.ParseException;

/**
    A coordinate pair (latitude / longitude or equivalent).
*/
public class ImageCoordinatePair {
    /**
     * The offset into the coordinate string where the latitude degrees part is found.
     */
    public static final int LAT_DEGREES_OFFSET = 0;

    /**
     * The length of the part of the coordinate string for the latitude degrees.
     */
    public static final int LAT_DEGREES_LENGTH = 2;

    /**
     * The offset into the coordinate string where the latitude minutes part is found.
     */
    public static final int LAT_MINUTES_OFFSET = LAT_DEGREES_OFFSET + LAT_DEGREES_LENGTH;

    /**
     * The length of the latitude and longitude minutes parts.
     */
    public static final int MINUTES_LENGTH = 2;

    /**
     * The offset into the coordinate string where the latitude seconds part is found.
     */
    public static final int LAT_SECONDS_OFFSET = LAT_MINUTES_OFFSET + MINUTES_LENGTH;

    /**
     * The length of the latitude and longitude seconds parts.
     */
    public static final int SECONDS_LENGTH = 2;

    /**
     * The offset into the coordinate string where the latitude hemisphere marker offset is found.
     */
    public static final int LAT_HEMISPHERE_MARKER_OFFSET = LAT_SECONDS_OFFSET + SECONDS_LENGTH;

    /**
     * The length of the latitude hemisphere marker part.
     */
    public static final int HEMISPHERE_MARKER_LENGTH = 1;

    /**
     * The offset into the coordinate string where the longitude hemisphere degrees part is found.
     */
    public static final int LON_DEGREES_OFFSET = LAT_HEMISPHERE_MARKER_OFFSET + HEMISPHERE_MARKER_LENGTH;

    /**
     * The length of the longitude degrees part.
     */
    public static final int LON_DEGREES_LENGTH = 3;

    /**
     * The offset into the coordinate string where the longitude minutes part is found.
     */
    public static final int LON_MINUTES_OFFSET = LON_DEGREES_OFFSET + LON_DEGREES_LENGTH;

    /**
     * The offset into the coordinate string where the longitude seconds part is found.
     */
    public static final int LON_SECONDS_OFFSET = LON_MINUTES_OFFSET + SECONDS_LENGTH;

    /**
     * The offset into the coordinate string where the longitude hemisphere marker part is found.
     */
    public static final int LON_HEMISPHERE_MARKER_OFFSET = LON_SECONDS_OFFSET + SECONDS_LENGTH;


    /**
     * The format for latitude decimal degrees.
     */
    private static final String LAT_DECIMAL_DEGREES_FORMAT = "+dd.ddd";

    /**
     * The length of the latitude decimal degrees format.
     */
    private static final int LAT_DECIMAL_DEGREES_FORMAT_LENGTH = LAT_DECIMAL_DEGREES_FORMAT.length();

    // Coordinate
    /**
     * The number of minutes in a degree.
     */
    private static final double MINUTES_IN_ONE_DEGREE = 60.0;

    /**
     * The number of seconds in a degree.
     */
    private static final double SECONDS_IN_ONE_MINUTE = 60.0;

    private double lat = 0.0;
    private double lon = 0.0;
    private String sourceString = null;

    /**
        Default constructor.
    */
    public ImageCoordinatePair() {
    }

    /**
        Constructor from a latitude / longitude pair.

        @param latitude the latitude value (positive for North).
        @param longitude the longitude value (positive for East).
    */
    public ImageCoordinatePair(final double latitude, final double longitude) {
        lat = latitude;
        lon = longitude;
    }

    /**
        Return the latitude value.
        <p>
        North is positive.

        @return the latitude value.
    */
    public final double getLatitude() {
        return lat;
    }

    /**
        Return the longitude value.
        <p>
        East is positive.

        @return the longitude value.
    */
    public final double getLongitude() {
        return lon;
    }

    /**
        Set latitude and longitude from Degrees/Minutes/Seconds string.

        @param dms string representation in MIL-STD-2500C ddmmssXdddmmssY form
        @throws ParseException if the format is not as expected.
     */
    public final void setFromDMS(final String dms) throws ParseException {
        sourceString = dms;
        checkDMSparameterIsProbablyValid(dms);
        String latDegrees = dms.substring(LAT_DEGREES_OFFSET, LAT_DEGREES_LENGTH);
        String latMinutes = dms.substring(LAT_MINUTES_OFFSET, LAT_MINUTES_OFFSET + MINUTES_LENGTH);
        String latSeconds = dms.substring(LAT_SECONDS_OFFSET, LAT_SECONDS_OFFSET + SECONDS_LENGTH);
        String latNS = dms.substring(LAT_HEMISPHERE_MARKER_OFFSET,
                                     LAT_HEMISPHERE_MARKER_OFFSET + HEMISPHERE_MARKER_LENGTH);
        String lonDegrees = dms.substring(LON_DEGREES_OFFSET, LON_DEGREES_OFFSET + LON_DEGREES_LENGTH);
        String lonMinutes = dms.substring(LON_MINUTES_OFFSET, LON_MINUTES_OFFSET + MINUTES_LENGTH);
        String lonSeconds = dms.substring(LON_SECONDS_OFFSET, LON_SECONDS_OFFSET + SECONDS_LENGTH);
        String lonEW = dms.substring(LON_HEMISPHERE_MARKER_OFFSET,
                                     LON_HEMISPHERE_MARKER_OFFSET + HEMISPHERE_MARKER_LENGTH);
        checkNSFlagIsValid(latNS, dms);
        checkEWFlagIsValid(lonEW, dms);
        try {
            buildLatitudeFromDecimalDegrees(latDegrees, latMinutes, latSeconds, latNS);
            buildLongitudeFromDecimalDegrees(lonDegrees, lonMinutes, lonSeconds, lonEW);
        } catch (NumberFormatException ex) {
            throw new ParseException(String.format("Incorrect DMS format: %s", dms), 0);
        }
    }

    private void checkDMSparameterIsProbablyValid(final String dms) throws ParseException {
        if (dms == null) {
            throw new ParseException("Null argument for DMS parsing", 0);
        }
        if (dms.length() != "ddmmssXdddmmssY".length()) {
            throw new ParseException("Incorrect length for DMS parsing:" + dms.length(), 0);
        }
    }
    private void checkNSFlagIsValid(final String latNS, final String dms) throws ParseException {
        if ((!"N".equals(latNS)) && (!"S".equals(latNS))) {
            throw new ParseException(String.format("Incorrect format for N/S flag while DMS parsing: %s(%s)", latNS, dms),
                    LAT_HEMISPHERE_MARKER_OFFSET);
        }
    }

    private void checkEWFlagIsValid(final String lonEW, final String dms) throws ParseException {
        if ((!"E".equals(lonEW)) && (!"W".equals(lonEW))) {
            throw new ParseException(String.format("Incorrect format for E/W flag while DMS parsing: %s(%s)", lonEW, dms),
                    LON_HEMISPHERE_MARKER_OFFSET);
        }
    }

    private void buildLatitudeFromDecimalDegrees(final String latDegrees, final String latMinutes, final String latSeconds, final String latNS) {
        lat = buildDecimalDegrees(latDegrees, latMinutes, latSeconds);
        if ("S".equals(latNS)) {
            lat = -1.0 * lat;
        }
    }

    private void buildLongitudeFromDecimalDegrees(final String lonDegrees, final String lonMinutes, final String lonSeconds, final String lonEW) {
        lon = buildDecimalDegrees(lonDegrees, lonMinutes, lonSeconds);
        if ("W".equals(lonEW)) {
            lon = -1.0 * lon;
        }
    }

    private double buildDecimalDegrees(final String degStr, final String minStr, final String secStr) {
        int degrees = Integer.parseInt(degStr);
        int minutes = Integer.parseInt(minStr);
        int seconds = Integer.parseInt(secStr);
        return degrees + ((minutes + (seconds / SECONDS_IN_ONE_MINUTE)) / MINUTES_IN_ONE_DEGREE);
    }

    /**
        Set the value from UTM / UPS North.
        <p>
        This format will not be converted to degrees, so getLatitude and getLongitude() will return
        default (0) values.

        @param utm the string representation of the coordinates.
        @throws ParseException if the string does not have the correct length / format.
    */
    public final void setFromUTMUPSNorth(final String utm) throws ParseException {
        if (utm.length() != "zzeeeeeennnnnnn".length()) {
            throw new ParseException("Incorrect length for UTM / UPS North String", 0);
        }
        sourceString = utm;
    }

    /**
        Set latitude and longitude from Decimal Degrees string.

        @param dd string representation in MIL-STD-2500C ±dd.ddd±ddd.ddd form
        @throws ParseException if the format is not as expected.
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

    /**
        Return the original source format.

        @return the original source format.
    */
    public final String getSourceFormat() {
        return sourceString;
    }

    /**
        Set the value from MGRS.
        <p>
        This format will not be converted to degrees, so getLatitude and getLongitude() will return
        default (0) values.

        @param mgrs the string representation of the coordinates.
    */
    public final void setFromMGRS(final String mgrs) {
        sourceString = mgrs;
    }
}
