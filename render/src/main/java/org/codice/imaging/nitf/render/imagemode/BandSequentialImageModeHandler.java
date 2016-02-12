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
import javax.imageio.stream.ImageInputStream;
import org.codice.imaging.nitf.core.image.ImageMode;
import org.codice.imaging.nitf.core.image.ImageSegment;
import org.codice.imaging.nitf.render.ImageMask;
import org.codice.imaging.nitf.render.imagerep.ImageRepresentationHandler;

class BandSequentialImageModeHandler extends BaseImageModeHandler implements ImageModeHandler {

    BandSequentialImageModeHandler(ImageRepresentationHandler imageRepresentationHandler) {
        checkNull(imageRepresentationHandler, "imageRepresentationHandler");
        this.imageRepresentationHandler = imageRepresentationHandler;
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public void handleImage(ImageSegment imageSegment, Graphics2D targetImage) throws IOException {

        checkNull(imageSegment, "imageSegment");
        checkNull(targetImage, "targetImage");
        checkImageMode(imageSegment);

        final ImageMask imageMask = getImageMask(imageSegment);

        ImageBlockMatrix matrix = new ImageBlockMatrix(imageSegment,
                () -> imageRepresentationHandler.createBufferedImage(imageSegment.getNumberOfPixelsPerBlockHorizontal(),
                        imageSegment.getNumberOfPixelsPerBlockVertical()));

        for (int bandIndex = 0; bandIndex < imageSegment.getNumBands(); bandIndex++) {
            final int index = bandIndex;

            matrix.forEachBlock(block -> {
                if (!imageMask.isMaskedBlock(block.getBlockIndex(), index)) {
                    readBlock(block, imageSegment.getData(), index);
                    applyMask(block, imageMask);
                }
            });
        }

        matrix.forEachBlock((block) -> block.render(targetImage, true));
    }

    private void readBlock(ImageBlock block, ImageInputStream imageInputStream, int bandIndex) {

        final DataBuffer data = block.getDataBuffer();

        try {
            for (int row = 0; row < block.getHeight(); row++) {
                for (int column = 0; column < block.getWidth(); column++) {
                    int i = row * block.getWidth() + column;
                    imageRepresentationHandler.renderPixelBand(data, i, imageInputStream, bandIndex);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
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
