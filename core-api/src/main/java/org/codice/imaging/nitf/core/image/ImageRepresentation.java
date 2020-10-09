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
    Image Representation (IREP).
    <p>
    The Image Representation (IREP) field contains a valid
    indicator for the general kind of image represented by the data. It is an indication of the processing required in
    order to display an image. Valid representation indicators are MONO for monochrome; RGB for red, green, or
    blue true colour, RGB/LUT for mapped colour; MULTI for multiband imagery, NODISPLY for an image not
    intended for display, NVECTOR and POLAR for vectors with Cartesian and polar coordinates respectively, and
    VPH for Synthetic Aperture Radar (SAR) Video Phase History. In addition, compressed imagery can have this
    field set to YCbCr601 when represented in the ITU-R Recommendation BT.601-5 colour space prior to using
    JPEG compression (if the value of the Image Compression (IC) field is equal to C3, C8, M3, or M8). An image
    may include multiple data bands and colour Look-Up Tables (LUTs), the latter within its header fields. True
    colour images (three band) may be specified to be interpreted using either the Red, Green, Blue (RGB) or the
    YCbCr601 (Y = Brightness of signal, Cb = Chrominance (blue), Cr = Chrominance (red)) colour system. Grids
    or matrix data may include one, two, or several bands of attribute values intended to provide additional
    geographic or geo-referenced information. VPH requires SAR processing to produce a displayable image.
    Vectors with Cartesian coordinates (NVECTOR) and vectors with polar coordinates (POLAR) require
    appropriate vector calculations to produce a displayable image. The processing required to display each band of
    the image is indicated in the nth Band Representation (IREPBANDn) field. Table A-2 shows representative
    IREP examples and some of its associated fields.
*/
public enum ImageRepresentation {

    /**
     * Unknown image representation.
     *
     * This indicates an unknown format, and typically indicates a broken file or an error during parsing. This is not a
     * valid value in a NITF image segment.
     */
    UNKNOWN (""),

    /**
        Monochrome.
    */
    MONOCHROME ("MONO"),

    /**
        RGB true colour.
    */
    RGBTRUECOLOUR ("RGB"),

    /**
        RGB Lookup Table.
    */
    RGBLUT ("RGB/LUT"),

    /**
        Multiband imagery.
    */
    MULTIBAND ("MULTI"),

    /**
        Image not for display.
    */
    NOTFORDISPLAY ("NODISPLY"),

    /**
        Vectors in cartesian form.
    */
    CARTESIANVECTOR ("NVECTOR"),

    /**
        Vectors in polar form.
    */
    POLARVECTOR ("POLAR"),

    /**
         Synthetic Aperture Radar (SAR) Video Phase History.
    */
    SARVIDEOPHASE ("VPH"),

    /**
        ITU-R Recommendation BT.601-5 colour space.
    */
    ITUBT6015 ("YCbCr601");

    private final String textEquivalent;

    /**
        Constructor.
        <p>
        This is required for enumeration initialisation.

        @param abbreviation the text abbreviation for the enumeration value.
    */
    ImageRepresentation(final String abbreviation) {
        this.textEquivalent = abbreviation;
    }

    /**
        Create image representation from the text equivalent.
        <p>
        This is intended to support file parsing, and is not usually necessary
        for other purposes.

        @param textEquivalent the text equivalent for an image representation
        @return the corresponding image representation (enumerated type)
    */
    public static ImageRepresentation getEnumValue(final String textEquivalent) {
        for (ImageRepresentation irep : values()) {
            if (textEquivalent.equals(irep.textEquivalent)) {
                return irep;
            }
        }
        return UNKNOWN;
    }

    /**
        Return the text equivalent for an image representation.
        <p>
        This is intended for debug output and output writing, and is not usually
        necessary for other purposes.

        @return the text equivalent for an image representation.
    */
    public String getTextEquivalent() {
        return textEquivalent;
    }

}

