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
package org.codice.nitf.filereader;

// import java.text.ParseException;

public class RGBColour {

    private byte red = 0x00;
    private byte green = 0x00;
    private byte blue = 0x00;

    private static final int UNSIGNED_BYTE_MASK = 0xFF;

    public RGBColour(final byte[] rgb) {
        // TODO: check array length = 3;
        red = rgb[0];
        green = rgb[1];
        blue = rgb[2];
    }

    public final byte getRed() {
        return red;
    }

    public final byte getGreen() {
        return green;
    }

    public final byte getBlue() {
        return blue;
    }

    @Override
    public final String toString() {
        String str = String.format("[0x%02x,0x%02x,0x%02x]",
                            (int) (red & UNSIGNED_BYTE_MASK),
                            (int) (green & UNSIGNED_BYTE_MASK),
                            (int) (blue & UNSIGNED_BYTE_MASK));
        return str;
    }
}
