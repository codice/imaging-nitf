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

public enum ImageCoordinatesRepresentation {

    UNKNOWN ("", ""),
    NONE (" ", "N"),
    MGRS ("U", "U"),
    UTMUPSNORTH ("N", null),
    UTMUPSSOUTH ("S", null),
    GEOGRAPHIC ("G", "G"),
    DECIMALDEGREES ("D", null),
    GEOCENTRIC (null, "C");

    private final String textEquivalent;
    private final String textEquivalentNitf20;

    ImageCoordinatesRepresentation(final String abbreviation, final String nitf20Abbreviation) {
        this.textEquivalent = abbreviation;
        this.textEquivalentNitf20 = nitf20Abbreviation;
    }

    public static ImageCoordinatesRepresentation getEnumValue(final String textEquivalent, final FileType nitfFileType) {
        for (ImageCoordinatesRepresentation icr : values()) {
            if (nitfFileType == FileType.NITF_TWO_ZERO) {
                if (textEquivalent.equals(icr.textEquivalentNitf20)) {
                    return icr;
                }
            } else {
                if (textEquivalent.equals(icr.textEquivalent)) {
                    return icr;
                }
            }
        }
        return UNKNOWN;
    }

    public String getTextEquivalent(final FileType nitfFileType) {
        if (nitfFileType == FileType.NITF_TWO_ZERO) {
            return textEquivalentNitf20;
        } else {
            return textEquivalent;
        }
    }
};

