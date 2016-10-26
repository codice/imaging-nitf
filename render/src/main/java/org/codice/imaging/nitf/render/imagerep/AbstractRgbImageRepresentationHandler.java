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

import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.io.IOException;
import java.util.Map;

import javax.imageio.stream.ImageInputStream;

import org.codice.imaging.nitf.render.ImageMask;

abstract class AbstractRgbImageRepresentationHandler implements ImageRepresentationHandler {
    protected final Map<Integer, Integer> bandMapping;

    protected final int numOfReadsPerBand;

    protected static final int ALPHA_MASK = 0xFF000000;

    AbstractRgbImageRepresentationHandler(final Map<Integer, Integer> bandMap,
            final int actualBitsPerPixelPerBand) {
        this.bandMapping = bandMap;
        this.numOfReadsPerBand = (int) Math.ceil(actualBitsPerPixelPerBand / ((double) Byte.SIZE));
    }

    @Override
    public abstract void renderPixelBand(final DataBuffer data, final int pixelIndex,
            final ImageInputStream imageInputStream, final int bandIndex) throws IOException;

    @Override
    public final BufferedImage createBufferedImage(final int blockWidth, final int blockHeight) {
        return new BufferedImage(blockWidth, blockHeight, BufferedImage.TYPE_INT_ARGB);
    }

    @Override
    public final void renderPadPixel(final ImageMask imageMask, final DataBuffer data, final int pixelIndex) {
        if (imageMask.isPadPixel(data.getElem(pixelIndex))) {
            data.setElem(pixelIndex, 0x00000000);
        }
    }
}
