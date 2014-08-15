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

/**
    Image compression format.
*/
public enum ImageCompression {
    UNKNOWN (""),
    BILEVEL ("C1"),
    JPEG ("C3"),
    VECTORQUANTIZATION ("C4"),
    LOSSLESSJPEG ("C5"),
    DOWNSAMPLEDJPEG ("I1"),
    NOTCOMPRESSED ("NC"),
    BILEVELMASK ("M1"),
    JPEGMASK ("M3"),
    VECTORQUANTIZATIONMASK ("M4"),
    LOSSLESSJPEGMASK ("M5"),
    NOTCOMPRESSEDMASK ("NM"),
    JPEG2000 ("C8"),
    JPEG2000MASK ("M8");

    private final String textEquivalent;

    /**
        Constructor.
        <p>
        This is required for enumeration initialisation.

        @param abbreviation the text abbreviation for the enumeration value.
    */
    ImageCompression(final String abbreviation) {
        textEquivalent = abbreviation;
    }

    /**
        Create image compression enumerated value from the text equivalent.
        <p>
        This is intended to support file parsing, and is not usually necessary
        for other purposes.

        @param textEquivalent the single character text equivalent for image compression.
        @return the image compression enumerated value.
    */
    public static ImageCompression getEnumValue(final String textEquivalent) {
        for (ImageCompression ic : values()) {
            if (textEquivalent.equals(ic.textEquivalent)) {
                return ic;
            }
        }
        return UNKNOWN;
    }

    /**
        Return the text equivalent for an image compression type.
        <p>
        This is intended for debug output and output writing, and is not usually
        necessary for other purposes.

        @return the text equivalent for an image compression type.
    */
    public String getTextEquivalent() {
        return textEquivalent;
    }
};

