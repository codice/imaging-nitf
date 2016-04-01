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
package org.codice.imaging.nitf.core;

import java.awt.Color;
import org.codice.imaging.nitf.core.common.NitfFormatException;

/**
    Red / Green / Blue colour representation.
*/
public class RGBColour {

    private byte red = 0x00;
    private byte green = 0x00;
    private byte blue = 0x00;

    private static final int REQUIRED_DATA_LENGTH = 3;
    private static final int UNSIGNED_BYTE_MASK = 0xFF;

    private static final int RED_OFFSET = 0;
    private static final int GREEN_OFFSET = 1;
    private static final int BLUE_OFFSET = 2;

    /**
     * Red component of the main blue colour in the Codice logo.
     */
    public static final byte CODICE_LOGO_RED_COMPONENT = 0;
    /**
     * Green component of the main blue colour in the Codice logo.
     */
    public static final byte CODICE_LOGO_GREEN_COMPONENT = 59;
    /**
     * Blue component of the main blue colour in the Codice logo.
     */
    public static final byte CODICE_LOGO_BLUE_COMPONENT = 121;

    /**
     * Length of an RGB colour field.
     * <p>
     * See, for example, FBKGC in MIL-STD-2500C Table A-1.
     */
    public static final int RGB_COLOUR_LENGTH = 3;

    /**
        Constructor.

        @param rgb three element array of the red, green and blue component values for the colour
        @throws NitfFormatException if the array does not have the right length.
    */
    public RGBColour(final byte[] rgb) throws NitfFormatException {
        if (rgb.length != REQUIRED_DATA_LENGTH) {
            throw new NitfFormatException("Incorrect number of bytes in RGB constructor array");
        }
        red = rgb[RED_OFFSET];
        green = rgb[GREEN_OFFSET];
        blue = rgb[BLUE_OFFSET];
    }

    /**
     * Constructor.
     *
     * @param redComponent the red component value for the colour
     * @param greenComponent the green component value for the colour
     * @param blueComponent the blue component value for the colour
     */
    public RGBColour(final byte redComponent, final byte greenComponent, final byte blueComponent) {
        red = redComponent;
        green = greenComponent;
        blue = blueComponent;
    }

    /**
        Return the red component of the colour.

        @return red component of the colour
    */
    public final byte getRed() {
        return red;
    }

    /**
        Return the green component of the colour.

        @return green component of the colour
    */
    public final byte getGreen() {
        return green;
    }

    /**
        Return the blue component of the colour.

        @return blue component of the colour
    */
    public final byte getBlue() {
        return blue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String toString() {
        return String.format("[0x%02x,0x%02x,0x%02x]",
                            (int) (red & UNSIGNED_BYTE_MASK),
                            (int) (green & UNSIGNED_BYTE_MASK),
                            (int) (blue & UNSIGNED_BYTE_MASK));
    }

    /**
     * Return the RGBColour as an AWT Color.
     *
     * @return the colour as a Color.
     */
    public final Color toColour() {
        return new Color((int) (red & UNSIGNED_BYTE_MASK),
                         (int) (green & UNSIGNED_BYTE_MASK),
                         (int) (blue & UNSIGNED_BYTE_MASK));
    }

    /**
     * Return the RGBColour as a byte array.
     *
     * This is the same format as was read in (so is suitable for writing out).
     *
     * @return the colour as a byte array
     */
    public final byte[] toByteArray() {
        byte[] bytes = new byte[REQUIRED_DATA_LENGTH];
        bytes[RED_OFFSET] = red;
        bytes[GREEN_OFFSET] = green;
        bytes[BLUE_OFFSET] = blue;
        return bytes;
    }
}
