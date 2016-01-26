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
import java.text.ParseException;

/**
    Red / Green / Blue colour representation.
*/
public class RGBColour {

    private byte red = 0x00;
    private byte green = 0x00;
    private byte blue = 0x00;

    private static final int REQUIRED_DATA_LENGTH = 3;
    private static final int UNSIGNED_BYTE_MASK = 0xFF;

    /**
        Constructor.

        @param rgb three element array of the red, green and blue component values for the colour
        @throws ParseException if the array does not have the right length.
    */
    public RGBColour(final byte[] rgb) throws ParseException {
        if (rgb.length != REQUIRED_DATA_LENGTH) {
            throw new ParseException("Incorrect number of bytes in RGB constructor array", 0);
        }
        red = rgb[0];
        green = rgb[1];
        blue = rgb[2];
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
}
