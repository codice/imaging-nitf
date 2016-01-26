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
package org.codice.imaging.nitf.core.common;

/**
 * Constants used for coordinate parsing.
 */
public final class CoordinateConstants {

    /**
     * The number of minutes in a degree.
     */
    public static final double MINUTES_IN_ONE_DEGREE = 60.0;

    /**
     * The number of seconds in a degree.
     */
    public static final double SECONDS_IN_ONE_MINUTE = 60.0;

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
    public static final String LAT_DECIMAL_DEGREES_FORMAT = "+dd.ddd";

    /**
     * The length of the latitude decimal degrees format.
     */
    public static final int LAT_DECIMAL_DEGREES_FORMAT_LENGTH = LAT_DECIMAL_DEGREES_FORMAT.length();

    private CoordinateConstants() {
    }
}
