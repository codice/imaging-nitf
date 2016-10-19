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

class Rgb24ImageRepresentationHandler implements ImageRepresentationHandler {
    private final Map<Integer, Integer> bandMapping;

    private final int numOfReadsPerBand;

    private final int bitsToDiscard;

    private static final int ALPHA_MASK = 0xFF000000;

    Rgb24ImageRepresentationHandler(final Map<Integer, Integer> bandMap,
            final int actualBitsPerPixelPerBand) {
        this.bandMapping = bandMap;
        this.numOfReadsPerBand = (int) Math.ceil(actualBitsPerPixelPerBand / ((double) Byte.SIZE));
        if (actualBitsPerPixelPerBand - Byte.SIZE < 0) {
            this.bitsToDiscard = 0;
        } else {
            this.bitsToDiscard = actualBitsPerPixelPerBand - Byte.SIZE;
        }
    }

    @Override
    public void renderPixelBand(final DataBuffer data, final int pixelIndex,
            final ImageInputStream imageInputStream, final int bandIndex) throws IOException {
        int pixelBandValue = 0;

        //Read a byte for every 8 bits per pixel per band.
        for (int i = numOfReadsPerBand - 1; i >= 0; i--) {
            pixelBandValue = pixelBandValue | (imageInputStream.read() << i * Byte.SIZE);
        }

        //Convert the value down to 8 bits.
        pixelBandValue = pixelBandValue >> bitsToDiscard;

        data.setElem(pixelIndex,
                ALPHA_MASK | data.getElem(pixelIndex) | (pixelBandValue
                        << bandMapping.get(bandIndex)));
    }

    @Override
    public BufferedImage createBufferedImage(final int blockWidth, final int blockHeight) {
        return new BufferedImage(blockWidth, blockHeight, BufferedImage.TYPE_INT_ARGB);
    }

    @Override
    public void renderPadPixel(final ImageMask imageMask, final DataBuffer data, final int pixelIndex) {
        if (imageMask.isPadPixel(data.getElem(pixelIndex))) {
            data.setElem(pixelIndex, 0x00000000);
        }
    }
}
