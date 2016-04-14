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
package org.codice.imaging.nitf.core.image;

import org.codice.imaging.nitf.core.common.NitfFormatException;

/**
    A Target ID (TGTID) representation.
    <p>
    <i>Target Identifier. This field shall contain the
    identification of the primary target in the format,
    BBBBBBBBBBOOOOOCC, consisting of ten
    characters of Basic Encyclopedia (BE) identifier,
    followed by five characters of facility OSUFFIX,
    followed by the two character country code as
    specified in FIPS PUB 10-4.</i>
    <p>
    Note that FIPS PUB 10-4 has been withdrawn. The NGA
    has a replacement in the GEC (Geopolitical Entities and
    Codes), but the codes do not always represent the same
    countries (e.g. Australia was AS in FIPS 10-4, and is AU
    in the GEC).
*/
public class TargetId {

    private String beNumber = "";
    private String oSuffix = "";
    private String countryCode = "";

    private static final int BE_LENGTH = 10;
    private static final int OSUFFIX_LENGTH = 5;
    private static final int COUNTRYCODE_LENGTH = 2;
    private static final int TGTID_LENGTH = BE_LENGTH + OSUFFIX_LENGTH + COUNTRYCODE_LENGTH;

    /**
        Default constructor.
    */
    public TargetId() {
    }

    /**
        Construct a target identifier from the string representation.

        @param identifier the full identifier (in BBBBBBBBBBOOOOOCC form).
        @throws NitfFormatException if the identifier is not of the expected length.
    */
    public TargetId(final String identifier) throws NitfFormatException {
        if (identifier == null) {
            throw new NitfFormatException("Null argument for TargetId");
        }
        if (identifier.length() != TGTID_LENGTH) {
            throw new NitfFormatException("Incorrect length for TargetId:" + identifier.length());
        }
        beNumber = identifier.substring(0, BE_LENGTH);
        oSuffix = identifier.substring(BE_LENGTH, BE_LENGTH + OSUFFIX_LENGTH);
        countryCode = identifier.substring(BE_LENGTH + OSUFFIX_LENGTH);
    }

    /**
        Set the Basic Encyclopedia (BE) number.

        @param identifier the BE number for this target identifier.
    */
    public final void setBasicEncyclopediaNumber(final String identifier) {
        beNumber = identifier;
    }

    /**
        Return the Basic Encyclopedia (BE) number.

        @return the BE number for this target identifier.
    */
    public final String getBasicEncyclopediaNumber() {
        return beNumber;
    }

    /**
        Set the O-suffix.

        @param suffix the O-suffix for this target identifier.
    */
    public final void setOSuffix(final String suffix) {
        oSuffix = suffix;
    }

    /**
        Return the O-suffix.

        @return the O-suffix for this target identifier.
    */
    public final String getOSuffix() {
        return oSuffix;
    }

    /**
        Set the Country Code.

        @param code country code for this target identifier.
    */
    public final void setCountryCode(final String code) {
        countryCode = code;
    }

    /**
        Return the Country Code.

        @return country code for this target identifier.
    */
    public final String getCountryCode() {
        return countryCode;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String toString() {
        return beNumber + oSuffix + countryCode;
    }
}
