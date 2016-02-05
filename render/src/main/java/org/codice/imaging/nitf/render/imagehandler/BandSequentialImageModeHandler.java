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

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.IOException;
import java.nio.IntBuffer;
import java.util.function.Consumer;
import org.codice.imaging.nitf.core.image.ImageCompression;
import org.codice.imaging.nitf.core.image.ImageMode;
import org.codice.imaging.nitf.core.image.ImageSegment;
import org.codice.imaging.nitf.render.ImageMask;

public class BandSequentialImageModeHandler implements ImageModeHandler {
    private static final String NULL_ARG_ERROR_MESSAGE =
            "BandSequentialImageModeHandler(): constructor argument '%s' may not be null.";

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

        ImageMask iMask = null;

        if (ImageCompression.NOTCOMPRESSEDMASK.equals(imageSegment.getImageCompression())) {
            iMask = new ImageMask(imageSegment, imageSegment.getData());
        } else {
            iMask = new ImageMask(imageSegment);
        }

        final ImageMask imageMask = iMask;

        if (!ImageMode.BANDSEQUENTIAL.equals(imageSegment.getImageMode())) {
            throw new IllegalStateException(
                    "BandSequentialImageModeHandler():  argument 'imageSegment' must have be ImageMode of 'S'.");
        }

        ImageBlockMatrix matrix = new ImageBlockMatrix(imageSegment.getNumberOfBlocksPerColumn(), imageSegment.getNumberOfBlocksPerRow(),
                imageSegment.getNumberOfPixelsPerBlockHorizontal() * imageSegment.getNumberOfPixelsPerBlockVertical());

        for (int bandIndex = 0; bandIndex < imageSegment.getNumBands(); bandIndex++) {
            final int index = bandIndex;

            forEachBlock(imageSegment, matrix, block -> readBlock(imageSegment, block, imageRepresentationHandler, index, imageMask));
        }

        forEachBlock(imageSegment, matrix, block -> { renderBlock(imageSegment, targetImage, block);
            block.getData().clear(); } );
    }

    private void checkNull(Object value, String valueName) {
        if (value == null) {
            throw new IllegalArgumentException(String.format(NULL_ARG_ERROR_MESSAGE, valueName));
        }
    }

    private void forEachBlock(ImageSegment imageSegment, ImageBlockMatrix matrix, Consumer<ImageBlock> intBufferConsumer)
        throws IOException {
        for (int i = 0; i < imageSegment.getNumberOfBlocksPerColumn(); i++) {
            for (int j = 0; j < imageSegment.getNumberOfBlocksPerRow(); j++) {
                ImageBlock currentBlock = matrix.getImageBlock(i, j);
                intBufferConsumer.accept(currentBlock);
            }
        }
    }

    private void readBlock(ImageSegment imageSegment, ImageBlock block,
            ImageRepresentationHandler imageRepresentationHandler, int bandIndex, ImageMask imageMask) {

        final IntBuffer data = block.getData();
        final int blockHeight = imageSegment.getNumberOfPixelsPerBlockVertical();
        final int blockWidth = imageSegment.getNumberOfPixelsPerBlockHorizontal();

        try {
            for (int row = 0; row < blockHeight; row++) {
                for (int column = 0; column < blockWidth; column++) {
                    int i = row * blockWidth + column;
                    int bandValue = imageSegment.getData().read();
                    data.put(i, imageRepresentationHandler.renderPixel(data.get(i), bandValue, bandIndex));
                }
            }

            applyMask(block, imageMask);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void renderBlock(ImageSegment imageSegment, Graphics2D targetImage, ImageBlock block ) {
        final int blockWidth = imageSegment.getNumberOfPixelsPerBlockHorizontal();
        final int blockHeight = imageSegment.getNumberOfPixelsPerBlockVertical();

        BufferedImage img = new BufferedImage(blockWidth, blockHeight, BufferedImage.TYPE_INT_ARGB);
        int[] imgData = ((DataBufferInt) img.getRaster().getDataBuffer()).getData();
        System.arraycopy(block.getData().array(), 0, imgData, 0, block.getData().array().length);
        targetImage.drawImage(img, block.getColumn() * blockHeight, block.getRow() * blockWidth, null);
    }

    private void applyMask(ImageBlock block, ImageMask imageMask) throws IOException {
        final IntBuffer data = block.getData();
        final int dataSize = data.array().length;

        if (imageMask != null) {
            for (int pixel = 0; pixel < dataSize; ++pixel) {
                data.put(pixel, data.get(pixel) | 0xFF000000);

                if (imageMask.isPadPixel(data.get(pixel))) {
                    data.put(pixel, 0x00000000);
                }
            }
        } else {
            for (int pixel = 0; pixel < dataSize; ++pixel) {
                data.put(pixel, data.get(pixel) | 0xFF000000);
            }
        }
    }
}
