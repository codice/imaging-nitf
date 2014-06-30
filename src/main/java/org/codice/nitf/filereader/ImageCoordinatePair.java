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

    private static double MINUTES_IN_ONE_DEGREE = 60.0;
    private static double SECONDS_IN_ONE_MINUTE = 60.0;

    public ImageCoordinatePair() {
    }

    public ImageCoordinatePair(double latitude, double longitude) {
        lat = latitude;
        lon = longitude;
    }

    public double latitude() {
        return lat;
    }

    public double longitude() {
        return lon;
    }

    /**
        Set latitude and longitude from Degrees/Minutes/Seconds string

        /param dms string representation in MIL-STD-2500C ddmmssXdddmmssY form
     */
    public void setFromDMS(String dms) throws ParseException {
        if (dms.length() != "ddmmssXdddmmssY".length()) {
            throw new ParseException("Incorrect length for DMS parsing", 0);
        }
        String latDegrees = dms.substring(0, 2);
        String latMinutes = dms.substring(2, 4);
        String latSeconds = dms.substring(4, 6);
        String latNS = dms.substring(6, 7);
        String lonDegrees = dms.substring(7, 10);
        String lonMinutes = dms.substring(10, 12);
        String lonSeconds = dms.substring(12, 14);
        String lonEW = dms.substring(14, 15);
        if ((!latNS.equals("N")) && (!latNS.equals("S"))) {
            throw new ParseException(String.format("Incorrect format for N/S flag while DMS parsing: %s(%s)", latNS, dms), 6);
        }
        if ((!lonEW.equals("E")) && (!lonEW.equals("W"))) {
            throw new ParseException(String.format("Incorrect format for E/W flag while DMS parsing: %s(%s)", lonEW, dms), 14);
        }
        try {
            lat = buildDecimalDegrees(latDegrees, latMinutes, latSeconds);
            if (latNS == "S") {
                lat = -1 * lat;
            }
            lon = buildDecimalDegrees(lonDegrees, lonMinutes, lonSeconds);
            if (lonEW == "W") {
                lon = -1 * lon;
            }
        } catch (NumberFormatException ex) {
            throw new ParseException(String.format("Incorrect DMS format: %s", dms), 0);
        }
    }

    public double buildDecimalDegrees(String degStr, String minStr, String secStr) {
        int degrees = Integer.parseInt(degStr);
        int minutes = Integer.parseInt(minStr);
        int seconds = Integer.parseInt(secStr);
        return degrees + ((minutes + (seconds / SECONDS_IN_ONE_MINUTE)) / MINUTES_IN_ONE_DEGREE);
    }
}
