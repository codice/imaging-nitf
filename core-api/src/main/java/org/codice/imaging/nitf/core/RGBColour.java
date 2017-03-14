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

/**
 * Red / Green / Blue colour representation.
 */
public interface RGBColour {
    /**
     * Return the red component of the colour.
     *
     * @return red component of the colour
     */
    byte getRed();

    /**
     * Return the green component of the colour.
     *
     * @return green component of the colour
     */
    byte getGreen();

    /**
     * Return the blue component of the colour.
     *
     * @return blue component of the colour
     */
    byte getBlue();

    @Override
    String toString();

    /**
     * Return the RGBColourImpl as an AWT Color.
     *
     * @return the colour as a Color.
     */
    Color toColour();

    /**
     * Return the RGBColourImpl as a byte array.
     * <p>
     * This is the same format as was read in (so is suitable for writing out).
     *
     * @return the colour as a byte array
     */
    byte[] toByteArray();
}
