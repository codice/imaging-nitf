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
package org.codice.imaging.nitf.core.text;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *     Text format.
    <p>
    This is intended to represent the format of the text in a text segment.
*/
public enum TextFormat {

    /**
     * Unknown text format.
     *
     * This indicates an unknown format, and typically indicates a broken file or an error during parsing. This is not a
     * valid value in a NITF text segment.
     */
    UNKNOWN (""),

    /**
        US Message Text Format.
        <p>
        This indicates text in US Message Text Format, per MIL-STD-6040.
    */
    USMTF ("MTF"),

    /**
        Basic character set format.
        <p>
        Basic Character Set (BCS) is a subset of the Extended Character Set (ECS). The most significant
        bit of the BCS characters is set to 0. The range of valid BCS characters code is limited to 0x20 to 0x7E plus
        line feed (0x0A), form feed (0x0C), and carriage return, (0x0D).
        <p>
        STA designates BCS character codes in a simple format. Any BCS code may
        be used in the text data segment when STA is indicated in the TXTFMT field. All lines within a text data
        segment shall be separated by carriage return/line feed pairs. A carriage return followed by a line feed shall be
        used to delimit lines in the text where the first character from the next line immediately follows the line feed
        character.
    */
    BASICCHARACTERSET ("STA"),

    /**
        ECS Text Formatting (UT1).
        <p>
        Extended Character Set (ECS) is a set of 1-byte encoded characters. Valid ECS character codes
        range from 0x20 to 0x7E, and 0xA0 to 0xFF, as well as Line Feed (0x0A), Form Feed (0x0C) and Carriage
        Return (0x0D). The ECS characters are described in MIL-STD-2500C Table B-1. As an interim measure, because of
        inconsistencies between standards, it is strongly advised that character codes ranging from 0xA0 to
        0xFF should never be used. Therefore, the use of ECS characters should be restricted to its BCS Subset.
        <p>
        UT1 is a legacy formatting that is replaced by the U8S text
        formatting (U8S). UT1 text formatting uses ECS character codes. Any ECS code may be used in the Text Data
        Segment when UT1 is indicated in the TXTFMT field. All lines within the TS shall be separated by Carriage
        Return/Line Feed pairs. A Carriage Return followed by a Line Feed shall be used to delimit lines in the text
        where the first character from the next line immediately follows the Line Feed character.
    */
    EXTENDEDCHARACTERSET ("UT1"),

    /**
        U8S Text Formatting (U8S).
        <p>
        Universal Multiple Octet Coded Character Set (UCS) Transformation Format 8 (UTF-8) Subset (U8S) is a subset
        of the UCS composed of 1-byte and 2-byte UTF-8 encoded characters (Basic Latin and Latin
        Supplement 1). The 1-byte encoded characters of the UTF-8 Subset (U8S) are the BCS characters. The 2-byte
        encoded characters of U8S are described in MIL-STD-2500C Table B-2.
        <p>
        The U8S text formatting replaces the legacy ECS text formatting
        (UT1). U8S text formatting uses U8S character codes. Any U8S character (either 1-byte or 2-byte encoded)
        may be used in the Text Data Segment when U8S is indicated in the TXTFMT field. All lines within Text Data
        Segment shall be separated by Carriage Return/Line Feed pairs. A Carriage Return followed by a Line Feed
        shall be used to delimit lines in the text where the first character from the next line immediately follows the
        Line Feed character.
    */
    UTF8SUBSET ("U8S");

    private static final Logger LOG = LoggerFactory.getLogger(TextFormat.class);

    /**
     * Guess the text format used in the provided string.
     *
     * The logic as given in the IPON (STDI-0005) Section 3.16.
     *
     * @param stringToTest the string to test for format.
     * @return the likely test format.
     */
    public static TextFormat getFormatUsedInString(final String stringToTest) {
        TextFormat likelyTextFormat = BASICCHARACTERSET;
        // It might be nice to try to guess MTF, but that isn't going to be reliable.
        for (char stringChar : stringToTest.toCharArray()) {
            if ((stringChar == '\n') || (stringChar == '\f') || (stringChar == '\r')) {
                // These are valid everwhere
                continue;
            }
            if ((stringChar >= ' ') && (stringChar <= '~')) {
                // These are valid everywhere
                continue;
            }
            if ((stringChar >= '\u00A0') && (stringChar <= '\u00FF')) {
                if (!likelyTextFormat.equals(UTF8SUBSET)) {
                    likelyTextFormat = EXTENDEDCHARACTERSET;
                }
                continue;
            }
            if ((stringChar >= '\uC2A1') && (stringChar <= '\uC3BF')) {
                likelyTextFormat = UTF8SUBSET;
                continue;
            }
            LOG.warn("Invalid character detected in Text string: {}", stringToTest);
        }
        return likelyTextFormat;
    }

    private final String textEquivalent;

    /**
        Constructor.
        <p>
        This is required for enumeration initialisation.

        @param abbreviation the text abbreviation for the enumeration value.
    */
    TextFormat(final String abbreviation) {
        this.textEquivalent = abbreviation;
    }

    /**
        Create text format from the text equivalent.
        <p>
        This is intended to support file parsing, and is not usually necessary
        for other purposes.

        @param textEquivalent the text equivalent for a text format
        @return the text format enumerated type.
    */
    public static TextFormat getEnumValue(final String textEquivalent) {
        for (TextFormat tf : values()) {
            if (textEquivalent.equals(tf.textEquivalent)) {
                return tf;
            }
        }
        return UNKNOWN;
    }

    /**
        Return the text equivalent for a text format.
        <p>
        This is intended for debug output and output writing, and is not usually
        necessary for other purposes.

        @return the text equivalent for a text format.
    */
    public String getTextEquivalent() {
        return textEquivalent;
    }
}

