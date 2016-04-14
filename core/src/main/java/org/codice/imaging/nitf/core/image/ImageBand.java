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
package org.codice.imaging.nitf.core.image;

import java.util.ArrayList;
import java.util.List;

/**
    Image Band.
*/
public class ImageBand {

    // An enum might have been useful, but this is extensible
    private String imageRepresentation = "";
    private String imageSubcategory = "";
    private int numEntriesLUT = 0;
    private List<ImageBandLUT> luts = new ArrayList<>();

    /**
        Default constructor.
    */
    public ImageBand() {
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
        Get the image representation for the band (IREPBAND).
        <p>
        This provides information on the properties of this band.
        See MIL-STD-2500C Table A-3 for the interpretation of this field.

        @return the image representation for the band.
    */
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
        Get the image band subcategory (ISUBCAT).
        <p>
        This provides interpretation of this band within the image
        category. See MIL-STD-2500C for the interpretation of this band.

        @return the image band subcategory.
    */
    public final String getSubCategory() {
        return imageSubcategory;
    }

    /**
        Get the number of lookup tables for an image band (NLUTSn).
        <p>
        This field shall contain the number of LUTs associated with
        the nth band of the image.
        <p>
        LUTs are allowed only if the value of the PVTYPE field is INT or B.
        <p>
        If the nth band of the image is monochromatic, this
        field can contain the value 1 or 2. If the value is 2,
        the first and second LUTs shall map respectively
        the most significant byte and the least significant
        byte of the 16 bit values. NOTE: If a system cannot
        support more than 256 different values, it may use
        only the values of the first LUT. In this case, the
        number of entries in the LUT (NELUTn) may
        exceed 256.
        <p>
        If the nth band of the image is color-coded (the value
        of the IREPBNDn field is LU), this field shall
        contain the value 3. The first, second, and third
        LUTs, in this case, shall map the image to the red,
        green, and blue display bands respectively.
        <p>
        The value 4 is reserved for future use.

        @return the number of lookup tables.
    */
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
        Get the number of entries in each lookup table (NELUTn).
        <p>
        This field shall contain the number of entries in
        each of the LUTs for the nth image band. This field
        shall be omitted if the value in NLUTSn is BCS
        zero (0x30).
        <p>
        This field will be zero if there are no entries or no
        lookup tables.

        @return the number of lookup table entries.
    */
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
        Get a specific lookup table.

        @param lutNumber the index of the lookup table (1-base).
        @return the lookup table corresponding to the index.
    */
    public final ImageBandLUT getLUT(final int lutNumber) {
        return getLUTZeroBase(lutNumber - 1);
    }

    /**
        Get a specific lookup table.

        @param lutNumberZeroBase the index of the lookup table (0-base).
        @return the lookup table corresponding to the index.
    */
    public final ImageBandLUT getLUTZeroBase(final int lutNumberZeroBase) {
        return luts.get(lutNumberZeroBase);
    }
}
