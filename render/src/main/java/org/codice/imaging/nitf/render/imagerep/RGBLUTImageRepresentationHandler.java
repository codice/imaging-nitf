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
import java.awt.image.IndexColorModel;
import java.io.IOException;
import javax.imageio.stream.ImageInputStream;
import org.codice.imaging.nitf.core.image.ImageCompression;
import org.codice.imaging.nitf.core.image.ImageSegment;
import org.codice.imaging.nitf.render.ImageMask;
import org.codice.imaging.nitf.render.datareader.IOReaderFunction;

/**
 * Image representation handler for LUT (RGB) images.
 */
class RGBLUTImageRepresentationHandler implements ImageRepresentationHandler {

    private final int selectedBand;
    private final IOReaderFunction reader;
    private final IndexColorModel colourModel;

    RGBLUTImageRepresentationHandler(int selectedBandZeroBase, ImageSegment segment, IOReaderFunction readerFunc) {
        selectedBand = selectedBandZeroBase;
        if (segment.getImageCompression().equals(ImageCompression.NOTCOMPRESSEDMASK)) {
            colourModel = new IndexColorModel(segment.getActualBitsPerPixelPerBand(),
                    segment.getImageBandZeroBase(selectedBandZeroBase).getNumLUTEntries(),
                    segment.getImageBandZeroBase(selectedBandZeroBase).getLUTZeroBase(0).getEntries(),
                    segment.getImageBandZeroBase(selectedBandZeroBase).getLUTZeroBase(1).getEntries(),
                    segment.getImageBandZeroBase(selectedBandZeroBase).getLUTZeroBase(2).getEntries(),
                    0);
        } else {
            colourModel = new IndexColorModel(segment.getActualBitsPerPixelPerBand(),
                    segment.getImageBandZeroBase(selectedBandZeroBase).getNumLUTEntries(),
                    segment.getImageBandZeroBase(selectedBandZeroBase).getLUTZeroBase(0).getEntries(),
                    segment.getImageBandZeroBase(selectedBandZeroBase).getLUTZeroBase(1).getEntries(),
                    segment.getImageBandZeroBase(selectedBandZeroBase).getLUTZeroBase(2).getEntries());
        }
        reader = readerFunc;
    }

    @Override
    public void renderPixelBand(DataBuffer dataBuffer, int pixelIndex, ImageInputStream imageInputStream, int bandIndex) throws IOException {
        if (bandIndex == selectedBand) {
            dataBuffer.setElem(pixelIndex, (Integer) reader.apply(imageInputStream));
        } else {
            reader.apply(imageInputStream);
        }
    }

    @Override
    public void renderPadPixel(ImageMask imageMask, DataBuffer data, int pixelIndex) {
        // Handled by the colour model.
    }

    @Override
    public BufferedImage createBufferedImage(int width, int height) {
        return new BufferedImage(width, height, BufferedImage.TYPE_BYTE_INDEXED, colourModel);
    }
}
