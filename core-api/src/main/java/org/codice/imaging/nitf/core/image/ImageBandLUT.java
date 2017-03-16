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

/**
 Lookup table for an image band.
 */
public interface ImageBandLUT {
    /**
     Return the number of entries in the LUT.

     @return the number of entries in the LUT
     */
    int getNumberOfEntries();

    /**
     Return a select entry from the LUT.

     @param i the index of the LUT entry, zero base
     @return the LUT content for the specified index
     */
    byte getEntry(int i);

    /**
     Return data for the LUT.

     @return the LUT content
     */
    byte[] getEntries();
}
