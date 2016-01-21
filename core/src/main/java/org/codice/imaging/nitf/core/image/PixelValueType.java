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
package org.codice.imaging.nitf.core.image;

/**
    Pixel value type (PVTYPE).
*/
public enum PixelValueType {

    /**
        Unknown pixel value type.
        <p>
        This indicates an unknown format, and typically indicates a broken file or
        an error during parsing. This is not a valid value in a NITF image.
    */
    UNKNOWN (""),

    /**
        Integer.
    */
    INTEGER ("INT"),

    /**
        Bi-level.
    */
    BILEVEL ("B"),

    /**
        Signed integer.
    */
    SIGNEDINTEGER ("SI"),

    /**
        Real (floating point).
    */
    REAL ("R"),

    /**
        Complex (real and imaginary floating point).
    */
    COMPLEX ("C");

    private final String textEquivalent;

    /**
        Constructor.
        <p>
        This is required for enumeration initialisation.

        @param abbreviation the text abbreviation for the enumeration value.
    */
    PixelValueType(final String abbreviation) {
        this.textEquivalent = abbreviation;
    }

    /**
        Create pixel value type from the text equivalent.
        <p>
        This is intended to support file parsing, and is not usually necessary
        for other purposes.

        @param textEquivalent the text equivalent for a pixel value type
        @return the pixel value type enumerated value.
    */
    public static PixelValueType getEnumValue(final String textEquivalent) {
        for (PixelValueType pv : values()) {
            if (textEquivalent.equals(pv.textEquivalent)) {
                return pv;
            }
        }
        return UNKNOWN;
    }

    /**
        Return the text equivalent for a pixel value type.
        <p>
        This is intended for debug output and output writing, and is not usually
        necessary for other purposes.

        @return the text equivalent for a pixel value type.
    */
    public String getTextEquivalent() {
        return textEquivalent;
    }
};

