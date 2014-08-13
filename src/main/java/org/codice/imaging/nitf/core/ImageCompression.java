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

    ImageCompression(final String abbreviation) {
        textEquivalent = abbreviation;
    }

    public static ImageCompression getEnumValue(final String textEquivalent) {
        for (ImageCompression ic : values()) {
            if (textEquivalent.equals(ic.textEquivalent)) {
                return ic;
            }
        }
        return UNKNOWN;
    }

    public String getTextEquivalent() {
        return textEquivalent;
    }
};

