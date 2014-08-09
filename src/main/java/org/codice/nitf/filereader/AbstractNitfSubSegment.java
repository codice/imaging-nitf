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
package org.codice.nitf.filereader;

public class AbstractNitfSubSegment extends AbstractNitfSegment {

    private int extendedHeaderDataOverflow = 0;

    /**
        Set the extended subheader overflow index (IXSOFL/SXSOFL/LXSOFL/TXSOFL).
        <p>
        This is the (1-base) index of the TRE into which extended header data
        overflows.

        @param overflow the extended header data overflow index
    */
    public final void setExtendedHeaderDataOverflow(final int overflow) {
        extendedHeaderDataOverflow = overflow;
    }

    /**
        Return the extended subheader overflow index (IXSOFL/SXSOFL/LXSOFL/TXSOFL).
        <p>
        This is the (1-base) index of the TRE into which extended header data
        overflows.

        @return the extended header data overflow index
    */
    public final int getExtendedHeaderDataOverflow() {
        return extendedHeaderDataOverflow;
    }
}
