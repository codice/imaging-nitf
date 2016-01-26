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
    Pixel justification (PJUST) options.
    <p>
    NITF images can have different "actual bits per pixel" to "number of
    bits per pixel" (e.g. 12 bits per pixel packed into 16 bits). To determine
    where the data (as opposed to the padding) is to be found, there is a
    pixel justification (left or right) setting.
*/
public enum PixelJustification {
    /**
        Unknown justification.
        <p>
        This indicates an unknown justification, and typically indicates a broken file or
        an error during parsing. This is not a valid value in a NITF .
    */
    UNKNOWN (""),

    /**
        Left justification.
    */
    LEFT ("L"),

    /**
        Right justification.
        <p>
        This is the preferred setting.
    */
    RIGHT ("R");

    private final String textEquivalent;

    /**
        Constructor.
        <p>
        This is required for enumeration initialisation.

        @param abbreviation the text abbreviation for the enumeration value.
    */
    PixelJustification(final String abbreviation) {
        textEquivalent = abbreviation;
    }

    /**
        Create pixel justification from the text equivalent.
        <p>
        This is intended to support file parsing, and is not usually necessary
        for other purposes.

        @param textEquivalent the single character text equivalent for pixel justification.
        @return the pixel justification enumerated value.
    */
    public static PixelJustification getEnumValue(final String textEquivalent) {
        for (PixelJustification pj : values()) {
            if (textEquivalent.equals(pj.textEquivalent)) {
                return pj;
            }
        }
        return UNKNOWN;
    }

    /**
        Return the text equivalent for a pixel justification
        <p>
        This is intended for debug output and output writing, and is not usually
        necessary for other purposes.

        @return the single character text equivalent for a pixel justification.
    */
    public String getTextEquivalent() {
        return textEquivalent;
    }
};

