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

public enum FileType {
    UNKNOWN (""),
    NITF_TWO_ZERO ("NITF02.00"),
    NITF_TWO_ONE ("NITF02.10"),
    NSIF_ONE_ZERO ("NSIF01.00");

    private final String textEquivalent;

    FileType(final String abbreviation) {
        this.textEquivalent = abbreviation;
    }

    public static FileType getEnumValue(final String textEquivalent) {
        for (FileType version : values()) {
            if (textEquivalent.equals(version.textEquivalent)) {
                return version;
            }
        }
        return UNKNOWN;
    }

};

