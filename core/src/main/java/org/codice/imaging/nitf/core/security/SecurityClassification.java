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
package org.codice.imaging.nitf.core.security;

/**
    Security classification.
*/
public enum SecurityClassification {

    /**
        Unknown security classification.
        <p>
        This indicates an unknown classification, and typically indicates a broken file or
        an error during parsing. This is not a valid value in a NITF file.
    */
    UNKNOWN (""),

    /**
        Unclassified.
    */
    UNCLASSIFIED ("U"),

    /**
        Restricted.
    */
    RESTRICTED ("R"),

    /**
        Confidential.
    */
    CONFIDENTIAL ("C"),

    /**
        Secret.
    */
    SECRET ("S"),

    /**
        Top Secret.
    */
    TOP_SECRET ("T");

    private final String textEquivalent;

    /**
        Constructor.
        <p>
        This is required for enumeration initialisation.

        @param abbreviation the text abbreviation for the enumeration value.
    */
    SecurityClassification(final String abbreviation) {
        this.textEquivalent = abbreviation;
    }

    /**
        Create a security classification from the text equivalent.
        <p>
        This is intended to support file parsing, and is not usually necessary
        for other purposes.

        @param textEquivalent the single character text equivalent for a security classification.
        @return the security classification (enumerated type)
    */
    public static SecurityClassification getEnumValue(final String textEquivalent) {
        for (SecurityClassification classification : values()) {
            if (textEquivalent.equals(classification.textEquivalent)) {
                return classification;
            }
        }
        return UNKNOWN;
    }

    /**
        Return the text equivalent for a security classification.
        <p>
        This is intended for debug output and output writing, and is not usually
        necessary for other purposes.

        @return the single character text equivalent for a security classification.
    */
    public String getTextEquivalent() {
        return textEquivalent;
    }

};

