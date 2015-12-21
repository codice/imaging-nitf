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

import java.text.ParseException;

/**
 * A coordinate pair (latitude / longitude or equivalent).
 */
public class ImageUtmCoordinate extends ImageCoordinatePoint {

    private static final int ZONE_FIELD_LENGTH = 2;

    private static final int EASTING_FIELD_LENGTH = 6;

    private final ImageCoordinatesRepresentation imageCoordinatesRepresentation;

    private Integer zone;

    private Integer easting;

    private Integer northing;

    /**
     * Default constructor.
     *
     * @param coordinatesRepresentation - must be either ImageCoordinateRepresentation.UTMUPSNORTH or ImageCoordinateRepresentation.UTMUPSSOUTH.
     *                                       may not be null.
     */
    public ImageUtmCoordinate(final ImageCoordinatesRepresentation coordinatesRepresentation) {
        super();

        if (coordinatesRepresentation != ImageCoordinatesRepresentation.UTMUPSNORTH
                && coordinatesRepresentation != ImageCoordinatesRepresentation.UTMUPSSOUTH) {
            throw new IllegalArgumentException(
                    "ImageUtmCoordinate(): constructor argument 'imageCoordinateRepresentation' must be either 'N' or 'S'.");
        }

        this.imageCoordinatesRepresentation = coordinatesRepresentation;
    }

    /**
     * Set the value from UTM / UPS North.
     * <p>
     * This format will not be converted to degrees, so getLatitude and getLongitude() will return
     * default (0) values.
     *
     * @param utm the string representation of the coordinates.
     * @throws ParseException if the string does not have the correct length / format.
     */
    public final void setFromUtmUps(final String utm) throws ParseException {
        if (utm == null) {
            throw new ParseException("Null argument for UTM parsing", 0);
        }

        if (utm.length() != "zzeeeeeennnnnnn".length()) {
            throw new ParseException("Incorrect length for UTM / UPS North String", 0);
        }

        this.zone = Integer.valueOf(utm.substring(0, ZONE_FIELD_LENGTH).trim());
        this.easting = Integer
                .valueOf(utm.substring(2, ZONE_FIELD_LENGTH + EASTING_FIELD_LENGTH).trim());
        this.northing = Integer
                .valueOf(utm.substring(ZONE_FIELD_LENGTH + EASTING_FIELD_LENGTH).trim());

        super.sourceString = utm;
    }

    /**
     * @return the zone portion of the supplied utm string.
     */
    public final Integer getZone() {
        return this.zone;
    }

    /**
     * @return the easting portion of the supplied utm string.
     */
    public final Integer getEasting() {
        return this.easting;
    }

    /**
     * @return the northing portion of the supplied utm string.
     */
    public final Integer getNorthing() {
        return this.northing;
    }

    @Override
    public final ImageCoordinatesRepresentation getImageCoordinatesRepresentation() {
        return this.imageCoordinatesRepresentation;
    }
}
