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
import org.codice.imaging.nitf.render.ImageMask;

/**
 * An ImageRepresentationHandler calculates the values for a given pixel based on the current pixel value
 * and the value of the band being read.
 *
 * This interface abstracts the calculation of each pixel value based on one or
 * more band values for that pixel.
 */

public interface ImageRepresentationHandler {

    /**
     * Applies the bandValue to currentValue based on bandIndex.
     *
     * @param dataBuffer the buffer that contains the pixel data.
     * @param pixelIndex the index of the pixel being rendered.
     * @param imageInputStream the stream that contains the image data.
     * @param bandIndex the index of the band being applied, zero-based.
     *
     * @throws java.io.IOException if there is a problem reading from the imageInputStream
     */
    void renderPixelBand(DataBuffer dataBuffer, int pixelIndex, ImageInputStream imageInputStream, int bandIndex)
            throws IOException;

    /**
     * Create a new image of the right type for this image handler.
     *
     * @param width the number of horizontal pixels in the image to be created.
     * @param height the number of vertical pixels in the image to be created.
     * @return a new BufferedImage for this ImageRepresentation.  This method should never return
     * the same object that was returned in a previous call.
     */
    BufferedImage createBufferedImage(int width, int height);

    /**
     * Apply image mask to one pixel within a block.
     *
     * @param imageMask the image mask information
     * @param data the data buffer (corresponding the BufferedImage) to mask
     * @param pixelIndex the pixel index within the block to operate on
     */
    public void renderPadPixel(ImageMask imageMask, DataBuffer data, int pixelIndex);
}
