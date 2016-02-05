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
package org.codice.imaging.nitf.core.image;

import org.codice.imaging.nitf.core.common.FileType;

/**
    Image coordinates representation (ICORDS).
    <p>
    Indicates the type of coordinate representation used for providing an
    approximate location of the image in the Image Geographic Location field (IGEOLO).
*/
public enum ImageCoordinatesRepresentation {

    /**
     * Unknown image coordinate representation.
     *
     * This indicates an unknown representation, and typically indicates a broken file or an error during parsing. This
     * is not a valid value in a NITF image segment.
     */
    UNKNOWN ("", ""),

    /**
        No coordinate representation.
    */
    NONE (" ", "N"),

    /**
        UTM expressed in Military Grid Reference System (MGRS) form.
    */
    MGRS ("U", "U"),

    /**
        UTM, Northern Hemisphere.
        <p>
        This is not valid in NITF 2.0 files.
    */
    UTMUPSNORTH ("N", null),

    /**
        UTM, Southern Hemisphere.
        <p>
        This is not valid in NITF 2.0 files.
    */
    UTMUPSSOUTH ("S", null),

    /**
        Geographic representation.
        <p>
        This means degrees, minutes, seconds.
    */
    GEOGRAPHIC ("G", "G"),

    /**
        Decimal degrees.
    */
    DECIMALDEGREES ("D", null),

    /**
        Geocentric representation.
        <p>
        This is only valid for NITF 2.0 files.
    */
    GEOCENTRIC (null, "C");

    private final String textEquivalent;
    private final String textEquivalentNitf20;

    /**
        Constructor.
        <p>
        This is required for enumeration initialisation.

        @param abbreviation the text abbreviation for the enumeration value for NITF 2.1 / NSIF 1.0.
        @param nitf20Abbreviation the text abbreviation for the enumeration value for NITF 2.0.
    */
    ImageCoordinatesRepresentation(final String abbreviation, final String nitf20Abbreviation) {
        this.textEquivalent = abbreviation;
        this.textEquivalentNitf20 = nitf20Abbreviation;
    }

    /**
        Create an image coordinate representation from the text equivalent.
        <p>
        This is intended to support file parsing, and is not usually necessary
        for other purposes.

        @param textEquivalent the single character text equivalent for an image coordinate representation.
        @param nitfFileType the type (version) of NITF file
        @return the image coordinate representation (enumerated type)
    */
    public static ImageCoordinatesRepresentation getEnumValue(final String textEquivalent, final FileType nitfFileType) {
        for (ImageCoordinatesRepresentation icr : values()) {
            if (nitfFileType == FileType.NITF_TWO_ZERO) {
                if (textEquivalent.equals(icr.textEquivalentNitf20)) {
                    return icr;
                }
            } else {
                if (textEquivalent.equals(icr.textEquivalent)) {
                    return icr;
                }
            }
        }
        return UNKNOWN;
    }

    /**
        Return the text equivalent for an image coordinate representation.
        <p>
        This is intended for debug output and output writing, and is not usually
        necessary for other purposes.

        @param nitfFileType the type (version) of NITF file
        @return the single character text equivalent for an image coordinate representation.
    */
    public String getTextEquivalent(final FileType nitfFileType) {
        if (nitfFileType == FileType.NITF_TWO_ZERO) {
            return textEquivalentNitf20;
        } else {
            return textEquivalent;
        }
    }
};

