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
package org.codice.imaging.nitf.core.image.impl;

import org.codice.imaging.nitf.core.image.ImageBandLUT;

/**
    Lookup table for an image band.
*/
public class ImageBandLUTImpl implements ImageBandLUT {

    private byte[] entries = null;

    /**
        Create a new image band lookup table (LUT).

        @param lutEntries the LUT data, in order.
    */
    public ImageBandLUTImpl(final byte[] lutEntries) {
        entries = lutEntries;
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public final int getNumberOfEntries() {
        return entries.length;
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public final byte getEntry(final int i) {
        return entries[i];
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public final byte[] getEntries() {
        return entries;
    }
}
