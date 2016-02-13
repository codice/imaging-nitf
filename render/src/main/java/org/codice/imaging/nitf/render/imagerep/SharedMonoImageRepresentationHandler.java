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
package org.codice.imaging.nitf.render.imagerep;

import java.awt.image.DataBuffer;

/**
 * Shared implementation details for the MONO image handler implementations.
 */
abstract class SharedMonoImageRepresentationHandler implements ImageRepresentationHandler {
    protected final int selectedBandZeroBase;

    public SharedMonoImageRepresentationHandler(final int selectedBandZeroBase) {
        this.selectedBandZeroBase = selectedBandZeroBase;
    }

    @Override
    public void applyPixelMask(DataBuffer data, int pixelIndex) {
    }

    @Override
    public void renderPadPixel(DataBuffer data, int pixelIndex) {
        data.setElem(pixelIndex, 0x00);
    }

}
