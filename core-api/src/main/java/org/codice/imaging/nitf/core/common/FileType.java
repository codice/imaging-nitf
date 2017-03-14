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
package org.codice.imaging.nitf.core.common;

/**
    Type of NITF file.
    <p>
    This is the version of NITF that the file claims to comply with.
*/
public enum FileType {

    /**
        Unknown file format.
        <p>
        This indicates an unknown format, and typically indicates a broken file or
        an error during parsing. This is not a valid value in a NITF file header,
        and will probably cause other problems during parsing.
    */
    UNKNOWN (""),

    /**
        NITF 2.0 file.
        <p>
        This means a file complying with MIL-STD-2500A or MIL-STD-2500B.
    */
    NITF_TWO_ZERO ("NITF02.00"),

    /**
        NITF 2.1 file.
        <p>
        This means a file complying with MIL-STD-2500C.
    */
    NITF_TWO_ONE ("NITF02.10"),

    /**
        NATO Secondary Image Format (NSIF) 1.0.
        <p>
        This means a file complying with NATO STANAG 4545, which is basically identical to MIL-STD-2500C.
    */
    NSIF_ONE_ZERO ("NSIF01.00");

    private final String textEquivalent;

    /**
        Constructor.
        <p>
        This is required for enumeration initialisation.

        @param abbreviation the text abbreviation for the enumeration value.
    */
    FileType(final String abbreviation) {
        this.textEquivalent = abbreviation;
    }

    /**
        Create file type from the text equivalent.
        <p>
        This is intended to support file parsing, and is not usually necessary
        for other purposes.

        @param textEquivalent the text equivalent for a file type
        @return the file type enumerated value.
    */
    public static FileType getEnumValue(final String textEquivalent) {
        for (FileType version : values()) {
            if (textEquivalent.equals(version.textEquivalent)) {
                return version;
            }
        }
        return UNKNOWN;
    }

    /**
     * Return the text equivalent for a NITF file type.
     * <p>
     * This is intended for debug output and output writing, and is not usually
     * necessary for other purposes.
     *
     * @return the text equivalent for a NITF file type.
     */
    public String getTextEquivalent() {
        return textEquivalent;
    }
};

