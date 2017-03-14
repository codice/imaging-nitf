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

/**
    The image mode.
    <p>
    This field shall indicate how the Image Pixels are stored in the NITF file. Valid
    values are B, P, R, and S. The interpretation of IMODE is dependent on whether the image is JPEG
    compressed (IC = C3, C5, I1, M3, or M5), VQ compressed (IC = C4, or M4), or uncompressed (IC = NC or NM).
*/
public enum ImageMode {

    /**
     * Unknown image mode.
     *
     * This indicates an unknown format, and typically indicates a broken file or an error during parsing. This is not a
     * valid value in a NITF image segment.
     */
    UNKNOWN (""),

    /**
        Band interleved by block.
    */
    BLOCKINTERLEVE ("B"),

    /**
        Band interleved by pixel.
    */
    PIXELINTERLEVE ("P"),

    /**
        Band interleved by row.
        <p>
        This is only valid for NITF 2.1 and NSIF 1.0. It is not valid for NITF 2.0.
    */
    ROWINTERLEVE ("R"),

    /**
        Band sequential.
    */
    BANDSEQUENTIAL ("S"),

    /**
     * Temporal band sequential.
     *
     * This is only valid for MIE4NITF images.
     */
    TEMPORALBANDSEQUENTIAL ("D"),

    /**
     * Temporal interleave by sample.
     *
     * This is only valid for MIE4NITF images containing uncompressed data,
     * (i.e. IC is NC or NM).
     */
    TEMPORALINTERLEAVEBYSAMPLE ("E"),

    /**
     * Temporal band sequential by block.
     *
     * This is only valid for MIE4NITF images.
     */
    TEMPORALBANDBANDSEQUENTIALBYBLOCK ("F"),

    /**
     * Temporal band interleave by block.
     *
     * This is only valid for MIE4NITF images.
     */
    TEMPORALBANDINTERLEAVEBYBLOCK ("T");

    private final String textEquivalent;

    /**
        Constructor.
        <p>
        This is required for enumeration initialisation.

        @param abbreviation the text abbreviation for the enumeration value.
    */
    ImageMode(final String abbreviation) {
        this.textEquivalent = abbreviation;
    }

    /**
        Create image mode from the text equivalent.
        <p>
        This is intended to support file parsing, and is not usually necessary
        for other purposes.

        @param textEquivalent the single character text equivalent for an image mode
        @return the image mode enumerated value corresponding to textEquivalent.
    */
    public static ImageMode getEnumValue(final String textEquivalent) {
        for (ImageMode imode : values()) {
            if (imode.textEquivalent.equals(textEquivalent)) {
                return imode;
            }
        }
        return UNKNOWN;
    }

    /**
        Return the text equivalent for an image mode.
        <p>
        This is intended for debug output and output writing, and is not usually
        necessary for other purposes.

        @return the single character text equivalent for an image mode.
    */
    public String getTextEquivalent() {
        return textEquivalent;
    }
};
