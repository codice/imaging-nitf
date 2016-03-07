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
import javax.imageio.stream.ImageInputStream;
import org.codice.imaging.nitf.render.datareader.IOReaderFunction;

/**
 * Image representation handler for 1 bit mono (greyscale) images.
 */
class Mono1ImageRepresentationHandler extends SharedMonoImageRepresentationHandler implements ImageRepresentationHandler {

    private static final int MAX_WHITE_BYTE_VALUE = 0xFF;

    Mono1ImageRepresentationHandler(final int selectedBandZeroBase, final IOReaderFunction readerFunc) {
        super(selectedBandZeroBase, readerFunc);
    }

    @Override
    public void renderPixelBand(final DataBuffer dataBuffer, final int pixelIndex,
            final ImageInputStream imageInputStream, final int bandIndex) throws IOException {
        if (bandIndex == selectedBandZeroBase) {
            if ((Integer) reader.apply(imageInputStream) == 1) {
                dataBuffer.setElem(pixelIndex, MAX_WHITE_BYTE_VALUE);
            } else {
                dataBuffer.setElem(pixelIndex, 0x00);
            }
        } else {
            reader.apply(imageInputStream);
        }
    }

    @Override
    public BufferedImage createBufferedImage(final int width, final int height) {
        return new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
    }

}
