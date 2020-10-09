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
package org.codice.imaging.nitf.core.symbol;

/**
    Types of symbols (NITF 2.0 only).
    <p>
    This enumeration provides the various type of symbols that can
    occur in a NITF 2.0 file. This corresponds to the STYPE value
    in a Symbol subheader.
    <p>
    <i>This field shall contain a valid indicator of the representation type of the symbol.
    Valid values are B, C, and O. B means bit-mapped. For bit-mapped symbols, the
    symbol parameters are found in the symbol subheader, and the symbol data values
    are contained in the symbol data field immediately following the subheader. C
    means Computer Graphics Metafile. The symbol data contain a Computer Graphics
    Metafile in binary format that defines the symbol according to the specification of
    CGM for NITF in NITFS MIL-STD-2301. O means object. The Symbol Number
    (SNUM) is a reference number that indicates the specific symbol as defined in table
    VIII. No symbol data field if this shall be present contains O, since an object
    symbol only has a subheader. The currently defined objects are standard geometric
    shapes and annotations of sufficient simplicity that they can be implemented
    accurately from verbal descriptions. Future versions of the NITF will include various
    predefined objects such as symbols for military units, vehicles, weapons, aircraft.</i>
    [From MIL-STD-2500A, TABLE VII. "NITF symbol subheader fields"].
    <p>
    Note that Object type symbols are actually defined in Table X of MIL-STD-2500A, rather
    than Table VIII.
*/
public enum SymbolType {

    /**
        Unknown format of symbol.
        <p>
        This indicates an unknown format, and typically indicates a broken file or
        an error during parsing. This is not a valid value in a NITF symbol subheader.
    */
    UNKNOWN (""),
    /**
        Bitmap format of symbol.
    */
    BITMAP ("B"),
    /**
        Computer Graphics Metafile (CGM) format of symbol.
    */
    CGM ("C"),
    /**
        Object format of symbol.
    */
    OBJECT ("O");

    private final String textEquivalent;

    /**
        Constructor.
        <p>
        This is required for enumeration initialisation.

        @param abbreviation the text abbreviation for the enumeration value.
    */
    SymbolType(final String abbreviation) {
        this.textEquivalent = abbreviation;
    }

    /**
        Create symbol type from the text equivalent.
        <p>
        This is intended to support file parsing, and is not usually necessary
        for other purposes.

        @param textEquivalent the single character text equivalent for a symbol type
        @return symbol type enumerated equivalent
    */
    public static SymbolType getEnumValue(final String textEquivalent) {
        for (SymbolType st : values()) {
            if (textEquivalent.equals(st.textEquivalent)) {
                return st;
            }
        }
        return UNKNOWN;
    }

    /**
        Return the text equivalent for a symbol type.
        <p>
        This is intended for debug output and output writing, and is not usually
        necessary for other purposes.

        @return the single character text equivalent for a symbol type.
    */
    public String getTextEquivalent() {
        return textEquivalent;
    }
}

