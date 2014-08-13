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

public class NitfImageBandLUT {

    private byte[] entries = null;

    /**
        Create a new image band lookup table (LUT).

        @param lutEntries the LUT data, in order.
    */
    public NitfImageBandLUT(final byte[] lutEntries) {
        entries = lutEntries;
    }

    /**
        Return the number of entries in the LUT.
    */
    public final int getNumberOfEntries() {
        return entries.length;
    }

    /**
        Return a select entry from the LUT.

        @param i the index of the LUT entry, zero base
    */
    public final byte getEntry(final int i) {
        return entries[i];
    }
}
