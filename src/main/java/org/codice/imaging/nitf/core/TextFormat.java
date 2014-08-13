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
package org.codice.imaging.nitf.core;

public enum TextFormat {

    UNKNOWN (""),
    USMTF ("MTF"),
    BASICCHARACTERSET ("STA"),
    EXTENDEDCHARACTERSET ("UT1"),
    UTF8SUBSET ("U8S");

    private final String textEquivalent;

    TextFormat(final String abbreviation) {
        this.textEquivalent = abbreviation;
    }

    public static TextFormat getEnumValue(final String textEquivalent) {
        for (TextFormat tf : values()) {
            if (textEquivalent.equals(tf.textEquivalent)) {
                return tf;
            }
        }
        return UNKNOWN;
    }

    public String getTextEquivalent() {
        return textEquivalent;
    }
};

