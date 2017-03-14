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
package org.codice.imaging.nitf.core.image.impl;

import java.util.ArrayList;
import java.util.List;

import org.codice.imaging.nitf.core.image.ImageBand;
import org.codice.imaging.nitf.core.image.ImageBandLUT;

/**
    Image Band.
*/
class ImageBandImpl implements ImageBand {

    // An enum might have been useful, but this is extensible
    private String imageRepresentation = "";
    private String imageSubcategory = "";
    private int numEntriesLUT = 0;
    private List<ImageBandLUT> luts = new ArrayList<>();

    /**
        Default constructor.
    */
    ImageBandImpl() {
    }

    /**
        Set the image representation for the band (IREPBAND).
        <p>
        This provides information on the properties of this band.
        See MIL-STD-2500C Table A-3 for the interpretation of this field.

        @param representation the image representation for the band.
    */
    public final void setImageRepresentation(final String representation) {
        imageRepresentation = representation;
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public final String getImageRepresentation() {
        return imageRepresentation;
    }

    /**
        Set the image band subcategory (ISUBCAT).
        <p>
        This provides interpretation of this band within the image
        category. See MIL-STD-2500C for the interpretation of this band.

        @param subcategory the image band subcategory.
    */
    public final void setImageSubcategory(final String subcategory) {
        imageSubcategory = subcategory;
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public final String getSubCategory() {
        return imageSubcategory;
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public final int getNumLUTs() {
        return luts.size();
    }

    /**
        Set the number of entries in each lookup table (NELUTn).
        <p>
        This field shall contain the number of entries in
        each of the LUTs for the nth image band. This field
        shall be omitted if the value in NLUTSn is BCS
        zero (0x30).
        <p>
        This field will be zero if there are no entries or no
        lookup tables.

        @param numLUTEntries the number of lookup table entries.
    */
    public final void setNumLUTEntries(final int numLUTEntries) {
        numEntriesLUT = numLUTEntries;
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public final int getNumLUTEntries() {
        return numEntriesLUT;
    }

    /**
        Add a lookup table to this image band.

        @param lut the lookup table to add.
    */
    public final void addLUT(final ImageBandLUT lut) {
        luts.add(lut);
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public final ImageBandLUT getLUT(final int lutNumber) {
        return getLUTZeroBase(lutNumber - 1);
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public final ImageBandLUT getLUTZeroBase(final int lutNumberZeroBase) {
        return luts.get(lutNumberZeroBase);
    }
}
