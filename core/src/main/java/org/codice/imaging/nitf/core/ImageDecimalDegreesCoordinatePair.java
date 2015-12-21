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
package org.codice.imaging.nitf.core;

import static org.codice.imaging.nitf.core.NitfConstants.LAT_DECIMAL_DEGREES_FORMAT_LENGTH;
import static org.codice.imaging.nitf.core.NitfConstants.MINUTES_IN_ONE_DEGREE;
import static org.codice.imaging.nitf.core.NitfConstants.SECONDS_IN_ONE_MINUTE;

import java.text.ParseException;
/**
    A coordinate pair (latitude / longitude or equivalent).
*/
public class ImageDecimalDegreesCoordinatePair extends ImageCoordinatePoint {

    private double lat = 0.0;
    private double lon = 0.0;

    /**
        Default constructor.
    */
    public ImageDecimalDegreesCoordinatePair() {
        super();
    }

    /**
        Constructor from a latitude / longitude pair.

        @param latitude the latitude value (positive for North).
        @param longitude the longitude value (positive for East).
    */
    public ImageDecimalDegreesCoordinatePair(final double latitude, final double longitude) {
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

    private double buildDecimalDegrees(final String degStr, final String minStr, final String secStr) {
        int degrees = Integer.parseInt(degStr);
        int minutes = Integer.parseInt(minStr);
        int seconds = Integer.parseInt(secStr);
        return degrees + ((minutes + (seconds / SECONDS_IN_ONE_MINUTE)) / MINUTES_IN_ONE_DEGREE);
    }

    /**
        Set latitude and longitude from Decimal Degrees string.

        @param dd string representation in MIL-STD-2500C ±dd.ddd±ddd.ddd form
        @throws ParseException if the format is not as expected.
     */
    public final void setFromDecimalDegrees(final String dd) throws ParseException {
        if (dd == null) {
            throw new ParseException("Null argument for decimal degrees parsing", 0);
        }

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

    @Override
    public final ImageCoordinatesRepresentation getImageCoordinatesRepresentation() {
        return ImageCoordinatesRepresentation.DECIMALDEGREES;
    }
}
