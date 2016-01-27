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
package org.codice.imaging.nitf.render.imagehandler;

/**
 * An ImageRepresentationHandler calculates the values for a given pixel based on the current pixel value
 * and the value of the band being read.  This interface abstracts the calculation of a single
 * pixel value based on one or more band values for that pixel.
 */

@FunctionalInterface
public interface ImageRepresentationHandler {
    /**
     * Applies the bandValue to currentValue based on bandIndex.
     *
     * @param currentValue - the current value for the pixel.
     * @param bandValue - the value of the band being applied.
     * @param bandIndex - the index of the band being applied, zero-indexed.
     * @return - the new value for the current pixel.
     */
    int renderPixel(int currentValue, int bandValue, int bandIndex);
}
