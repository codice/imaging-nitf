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

/**
    Tagged registered extension (TRE).
*/
public class Tre extends TreEntryList {
    private String prefix = null;
    private byte[] rawData = null;

    /**
        Construct TRE with specific tag name.

        @param tag the name for the TRE.
    */
    public Tre(final String tag) {
        super(tag);
    }

    /**
        Set the metadata prefix format string.

        @param mdPrefix the metadata prefix.
    */
    public final void setPrefix(final String mdPrefix) {
        prefix = mdPrefix;
    }

    /**
        Return the metadata prefix format string.

        @return the metadata prefix.
    */
    public final String getPrefix() {
        return prefix;
    }

    /**
        Set the raw data for this TRE.
        <p>
        This is only used for TREs that we couldn't parse.

        @param treDataRaw the raw bytes for the TRE.
    */
    public final void setRawData(final byte[] treDataRaw) {
        rawData = treDataRaw;
    }

    /**
        Get the raw data for this TRE.
        <p>
        This is only used for TREs that we couldn't parse.

        @return the raw bytes for the TRE.
    */
    public final byte[] getRawData() {
        return rawData;
    }
}
