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
package org.codice.imaging.nitf.core.symbol;

/**
    NITF 2.0 Symbol colour encoding.
*/
public enum SymbolColour {

    /**
     * Unknown colour encoding.
     *
     * This indicates an unknown colour encoding, and typically indicates a broken file or an error during parsing. This
     * is not a valid value in a NITF symbol segment.
     */
    UNKNOWN (""),

    /**
        Use colour lookup table.
    */
    USE_COLOUR_LUT ("C"),

    /**
        Use grayscale lookup table.
    */
    USE_GRAYSCALE_LUT ("G"),

    /**
        Zero black, one white.
        <p>
        This is only valid if the number of bits per pixel is 1.
    */
    ZERO_BLACK_ONE_WHITE ("N"),

    /**
        Zero transparent, one black.
        <p>
        This is only valid if the number of bits per pixel is 1.
    */
    ZERO_TRANSPARENT_ONE_BLACK ("K"),

    /**
        Zero transparent, one white.
        <p>
        This is only valid if the number of bits per pixel is 1.
    */
    ZERO_TRANSPARENT_ONE_WHITE ("W"),

    /**
        Zero transparent, one red.
        <p>
        This is only valid if the number of bits per pixel is 1.
    */
    ZERO_TRANSPARENT_ONE_RED ("R"),

    /**
        Zero transparent, one orange.
        <p>
        This is only valid if the number of bits per pixel is 1.
    */
    ZERO_TRANSPARENT_ONE_ORANGE ("O"),

    /**
        Zero transparent, one blue.
        <p>
        This is only valid if the number of bits per pixel is 1.
    */
    ZERO_TRANSPARENT_ONE_BLUE ("B"),

    /**
        Zero transparent, one yellow.
        <p>
        This is only valid if the number of bits per pixel is 1.
    */
    ZERO_TRANSPARENT_ONE_YELLOW ("Y"),

    /**
        Not applicable.
        <p>
        This is the only valid code for CGM.
    */
    NOT_APPLICABLE (" ");

    private final String textEquivalent;

    /**
        Constructor.
        <p>
        This is required for enumeration initialisation.

        @param abbreviation the text abbreviation for the enumeration value.
    */
    SymbolColour(final String abbreviation) {
        this.textEquivalent = abbreviation;
    }

    /**
        Create symbol colour from the text equivalent.
        <p>
        This is intended to support file parsing, and is not usually necessary
        for other purposes.

        @param textEquivalent the single character text equivalent for a symbol colour
        @return the symbol colour enumerated value.
    */
    public static SymbolColour getEnumValue(final String textEquivalent) {
        for (SymbolColour sc : values()) {
            if (textEquivalent.equals(sc.textEquivalent)) {
                return sc;
            }
        }
        return UNKNOWN;
    }

    /**
        Return the text equivalent for a symbol colour.
        <p>
        This is intended for debug output and output writing, and is not usually
        necessary for other purposes.

        @return the single character text equivalent for a symbol colour.
    */
    public String getTextEquivalent() {
        return textEquivalent;
    }
}

