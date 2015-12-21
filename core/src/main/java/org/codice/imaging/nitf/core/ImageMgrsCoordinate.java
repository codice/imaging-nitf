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
 * A coordinate in MGRS format (zzBJKeeeeennnnn) where:
 *  'zz' is the zone,
 *  'B' is the band within the zone
 *  'JK' is the 100km grid
 *  'eeeee' is the easting value
 *  'nnnnn' is the northing value.
 */
public class ImageMgrsCoordinate extends ImageCoordinatePoint {
    private static final int ZONE_FIELD_START = 0;

    private static final int ZONE_FIELD_END = 2;

    private static final int BAND_FIELD_START = ZONE_FIELD_END;

    private static final int BAND_FIELD_END = 3;

    private static final int GRID_FIELD_START = BAND_FIELD_END;

    private static final int GRID_FIELD_END = 5;

    private static final int EASTING_FIELD_START = GRID_FIELD_END;

    private static final int EASTING_FIELD_END = 10;

    private static final int NORTHING_FIELD_START = EASTING_FIELD_END;

    private Integer zone;

    private Integer easting;

    private Integer northing;

    private String grid;

    private String band;

    /**
     * Default constructor.
     */
    public ImageMgrsCoordinate() {
        super();
    }

    /**
     * Set the value from MGRS.
     * <p>
     * This format will not be converted to degrees.  The values supplied by the input
     * are captured here and returned as they were read.
     *
     * @param mgrs the string representation of the coordinates.
     */
    final void setFromMgrs(final String mgrs) throws ParseException {
        if (mgrs == null) {
            throw new ParseException("Null argument for MGRS parsing", 0);
        }

        if (mgrs.length() != "zzBJKeeeeennnnn".length()) {
            throw new ParseException("Incorrect length for MGRS String", 0);
        }

        this.zone = Integer.valueOf(mgrs.substring(ZONE_FIELD_START, ZONE_FIELD_END).trim());
        this.band = mgrs.substring(BAND_FIELD_START, BAND_FIELD_END).trim();
        this.grid = mgrs.substring(GRID_FIELD_START, GRID_FIELD_END).trim();
        this.easting = Integer.valueOf(mgrs.substring(EASTING_FIELD_START, EASTING_FIELD_END).trim());
        this.northing = Integer.valueOf(mgrs.substring(NORTHING_FIELD_START).trim());

        super.sourceString = mgrs;
    }

    /**
     *
     * @return the easting value from the source MGRS.
     */
    public final Integer getEasting() {
        return easting;
    }

    /**
     *
     * @return the grid value from the supplied MGRS.
     */
    public final String getGrid() {
        return grid;
    }

    /**
     *
     * @return the northing value from the supplied MGRS.
     */
    public final Integer getNorthing() {
        return northing;
    }

    /**
     *
     * @return the zone value from the supplied MGRS.
     */
    public final Integer getZone() {
        return zone;
    }

    /**
     *
     * @return the band value from the supplied MGRS.
     */
    public final String getBand() {
        return band;
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public final ImageCoordinatesRepresentation getImageCoordinatesRepresentation() {
        return ImageCoordinatesRepresentation.MGRS;
    }
}
