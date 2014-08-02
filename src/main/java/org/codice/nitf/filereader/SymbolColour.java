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
package org.codice.nitf.filereader;

public enum SymbolColour {

    UNKNOWN (""),
    USE_COLOUR_LUT ("C"),
    USE_GRAYSCALE_LUT ("G"),
    ZERO_BLACK_ONE_WHITE ("N"),
    ZERO_TRANSPARENT_ONE_BLACK ("K"),
    ZERO_TRANSPARENT_ONE_WHITE ("W"),
    ZERO_TRANSPARENT_ONE_RED ("R"),
    ZERO_TRANSPARENT_ONE_ORANGE ("O"),
    ZERO_TRANSPARENT_ONE_BLUE ("B"),
    ZERO_TRANSPARENT_ONE_YELLOW ("Y"),
    NOT_APPLICABLE (" ");

    private final String textEquivalent;

    SymbolColour(final String abbreviation) {
        this.textEquivalent = abbreviation;
    }

    public static SymbolColour getEnumValue(final String textEquivalent) {
        for (SymbolColour sc : values()) {
            if (textEquivalent.equals(sc.textEquivalent)) {
                return sc;
            }
        }
        return UNKNOWN;
    }

    public String getTextEquivalent() {
        return textEquivalent;
    }
};

