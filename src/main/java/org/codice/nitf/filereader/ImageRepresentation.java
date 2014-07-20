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

public enum ImageRepresentation {

    UNKNOWN (""),
    MONOCHROME ("MONO"),
    RGBTRUECOLOUR ("RGB"),
    RGBLUT ("RGB/LUT"),
    MULTIBAND ("MULTI"),
    NOTFORDISPLAY ("NODISPLY"),
    CARTESIANVECTOR ("NVECTOR"),
    POLARVECTOR ("POLAR"),
    SARVIDEOPHASE ("VPH"),
    ITUBT6015 ("YCbCr601");

    private final String textEquivalent;

    ImageRepresentation(final String abbreviation) {
        this.textEquivalent = abbreviation;
    }

    public static ImageRepresentation getEnumValue(final String textEquivalent) {
        for (ImageRepresentation irep : values()) {
            if (textEquivalent.equals(irep.textEquivalent)) {
                return irep;
            }
        }
        return UNKNOWN;
    }

    public String getTextEquivalent() {
        return textEquivalent;
    }

};

