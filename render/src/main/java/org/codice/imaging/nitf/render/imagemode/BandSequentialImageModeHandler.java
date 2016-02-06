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

package org.codice.imaging.nitf.render.imagemode;

import java.awt.Graphics2D;
import java.awt.image.DataBuffer;
import java.io.IOException;
import org.codice.imaging.nitf.core.image.ImageCompression;
import org.codice.imaging.nitf.core.image.ImageMode;
import org.codice.imaging.nitf.core.image.ImageSegment;
import org.codice.imaging.nitf.render.ImageMask;
import org.codice.imaging.nitf.render.imagerep.ImageRepresentationHandler;

public class BandSequentialImageModeHandler extends BaseImageModeHandler implements ImageModeHandler {
    private static final String NULL_ARG_ERROR_MESSAGE =
            "BandSequentialImageModeHandler(): argument '%s' may not be null.";

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public void handleImage(ImageSegment imageSegment, Graphics2D targetImage,
            ImageRepresentationHandler imageRepresentationHandler) throws IOException {

        checkNull(imageSegment, "imageSegment");
        checkNull(targetImage, "targetImage");
        checkNull(imageRepresentationHandler, "imageRepresentationHandler");
        checkImageMode(imageSegment);

        ImageMask iMask = null;

        if (ImageCompression.NOTCOMPRESSEDMASK.equals(imageSegment.getImageCompression())) {
            iMask = new ImageMask(imageSegment, imageSegment.getData());
        } else {
            iMask = new ImageMask(imageSegment);
        }

        final ImageMask imageMask = iMask;

        if (!ImageMode.BANDSEQUENTIAL.equals(imageSegment.getImageMode())) {
            throw new IllegalStateException(
                "BandSequentialImageModeHandler(): argument 'imageSegment' must have be ImageMode of 'S'.");
        }

        ImageBlockMatrix matrix = new ImageBlockMatrix(imageSegment,
                () -> imageRepresentationHandler.createBufferedImage(imageSegment.getNumberOfPixelsPerBlockHorizontal(),
                        imageSegment.getNumberOfPixelsPerBlockVertical()));

        for (int bandIndex = 0; bandIndex < imageSegment.getNumBands(); bandIndex++) {
            final int index = bandIndex;

            matrix.forEachBlock(block -> {
                readBlock(block, imageSegment, imageRepresentationHandler, index);
                applyMask(block, imageMask, imageRepresentationHandler);
            } );
        }

        matrix.forEachBlock((block) -> block.render(targetImage, true));
    }

    private void checkNull(Object value, String valueName) {
        if (value == null) {
            throw new IllegalArgumentException(String.format(NULL_ARG_ERROR_MESSAGE, valueName));
        }
    }

    private void readBlock(ImageBlock block, ImageSegment imageSegment,
            ImageRepresentationHandler imageRepresentationHandler, int bandIndex) {

        final DataBuffer data = block.getDataBuffer();

        try {
            for (int row = 0; row < block.getHeight(); row++) {
                for (int column = 0; column < block.getWidth(); column++) {
                    int i = row * block.getWidth() + column;
                    imageRepresentationHandler.renderPixelBand(data, i, imageSegment, bandIndex);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void applyMask(ImageBlock block, ImageMask imageMask,
            ImageRepresentationHandler imageRepresentationHandler) {
        if (imageMask != null) {
            final int dataSize = block.getWidth() * block.getHeight();

            for (int pixel = 0; pixel < dataSize; ++pixel) {
                if (imageMask.isPadPixel(block.getDataBuffer().getElem(pixel))) {
                    imageRepresentationHandler.renderPadPixel(block.getDataBuffer(), pixel);
                } else {
                    imageRepresentationHandler.applyPixelMask(block.getDataBuffer(), pixel);
                }
            }
        }
    }

    @Override
    public ImageMode getSupportedImageMode() {
        return ImageMode.BANDSEQUENTIAL;
    }

    @Override
    String getHandlerName() {
        return "BandSequentialImageModeHandler";
    }
}
