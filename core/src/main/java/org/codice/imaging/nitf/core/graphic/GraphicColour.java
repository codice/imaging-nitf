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
package org.codice.imaging.nitf.core.graphic;

/**
    Graphic Colour format (NITF 2.1 only).
    <p>
    This represents the colour format in a NITF 2.1 graphic segment.
    <p>
    <i>If SFMT = C, this field shall contain a C
    if the CGM contains any color pieces or an M if it is
    monochrome (i.e., black, white, or levels of grey).</i>
    [From MIL-STD-2500C, TABLE A-5. "NITF graphic subheader"]
    <p>
    SFMT only has one valid value (which is C) in NITF 2.1 files.
*/
public enum GraphicColour {

    /**
     * Unknown format of symbol. This indicates an unknown format, and may indicate a broken file or an error during
     * parsing. This is not a valid value in a NITF graphic segment, but there are software products that produce it.
     */
    UNKNOWN (""),
    /**
        Colour format.
        <p>
        As noted above, this indicates that the CGM has some colour pieces.
    */
    COLOUR ("C"),
    /**
        Monochrome (black / white / levels of gray) format.
        <p>
        As noted above, this indicates that the CGM has no colour pieces.
    */
    MONOCHROME ("M");

    private final String textEquivalent;

    /**
        Constructor.
        <p>
        This is required for enumeration initialisation.

        @param abbreviation the text abbreviation for the enumeration value.
    */
    GraphicColour(final String abbreviation) {
        this.textEquivalent = abbreviation;
    }

    /**
        Create graphic colour from the text equivalent.
        <p>
        This is intended to support file parsing, and is not usually necessary
        for other purposes.

        @param textEquivalent the single character text equivalent for a graphic colour
        @return the corresponding graphic colour
    */
    public static GraphicColour getEnumValue(final String textEquivalent) {
        for (GraphicColour scolor : values()) {
            if (textEquivalent.equals(scolor.textEquivalent)) {
                return scolor;
            }
        }
        return UNKNOWN;
    }

    /**
        Return the text equivalent for a graphic colour.
        <p>
        This is intended for debug output and output writing, and is not usually
        necessary for other purposes.

        @return the single character text equivalent for a graphic colour.
    */
    public String getTextEquivalent() {
        return textEquivalent;
    }
};

