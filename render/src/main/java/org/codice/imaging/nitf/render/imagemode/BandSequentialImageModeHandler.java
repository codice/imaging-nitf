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
<<<<<<< HEAD:render/src/main/java/org/codice/imaging/nitf/render/imagehandler/BandSequentialImageModeHandler.java
package org.codice.imaging.nitf.render.imagehandler;
=======
package org.codice.imaging.nitf.render.imagemode;
>>>>>>> Refactored uncompressed rendering pattern:render/src/main/java/org/codice/imaging/nitf/render/imagemode/BandSequentialImageModeHandler.java

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferInt;
import java.io.IOException;
import java.nio.IntBuffer;
import java.util.function.Consumer;

import javax.imageio.stream.ImageInputStream;

import org.codice.imaging.nitf.core.image.ImageCompression;
import org.codice.imaging.nitf.core.image.ImageMode;
import org.codice.imaging.nitf.core.image.NitfImageSegmentHeader;
import org.codice.imaging.nitf.render.ImageMask;
import org.codice.imaging.nitf.render.imagerep.ImageRepresentationHandler;

public class BandSequentialImageModeHandler implements ImageModeHandler {
    private static final String NULL_ARG_ERROR_MESSAGE =
            "BandSequentialImageModeHandler(): constructor argument '%s' may not be null.";

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public void handleImage(NitfImageSegmentHeader imageSegmentHeader,
            ImageInputStream imageInputStream, Graphics2D targetImage,
            ImageRepresentationHandler imageRepresentationHandler) throws IOException {

        checkNull(imageInputStream, "imageInputStream");
        checkNull(imageSegmentHeader, "imageSegmentHeader");
        checkNull(targetImage, "targetImage");
        checkNull(imageRepresentationHandler, "imageRepresentationHandler");

        ImageMask iMask = null;

        if (ImageCompression.NOTCOMPRESSEDMASK.equals(imageSegmentHeader.getImageCompression())) {
            iMask = new ImageMask(imageSegmentHeader, imageInputStream);
        } else {
            iMask = new ImageMask(imageSegmentHeader);
        }

        final ImageMask imageMask = iMask;

        if (!ImageMode.BANDSEQUENTIAL.equals(imageSegmentHeader.getImageMode())) {
            throw new IllegalStateException(
                    "BandSequentialImageModeHandler(): constructor argument 'imageSegmentHeader' must have be ImageMode of 'S'.");
        }

        ImageBlockMatrix matrix = new ImageBlockMatrix(imageSegmentHeader.getNumberOfBlocksPerColumn(), imageSegmentHeader.getNumberOfBlocksPerRow());

        for (int bandIndex = 0; bandIndex < imageSegmentHeader.getNumBands(); bandIndex++) {
            final int index = bandIndex;

            forEachBlock(imageSegmentHeader, matrix, block -> readBlock(imageSegmentHeader, block, imageInputStream,
                            imageRepresentationHandler, index));
        }

        forEachBlock(imageSegmentHeader, matrix, block -> renderBlock(imageSegmentHeader, targetImage, block));
    }

    private void checkNull(Object value, String valueName) {
        if (value == null) {
            throw new IllegalArgumentException(String.format(NULL_ARG_ERROR_MESSAGE, valueName));
        }
    }

    private void forEachBlock(NitfImageSegmentHeader imageSegmentHeader, ImageBlockMatrix matrix, Consumer<ImageBlock> intBufferConsumer) {
        for (int i = 0; i < imageSegmentHeader.getNumberOfBlocksPerColumn(); i++) {
            for (int j = 0; j < imageSegmentHeader.getNumberOfBlocksPerRow(); j++) {
                ImageBlock currentBlock = matrix.getImageBlock(i, j);
                intBufferConsumer.accept(currentBlock);
            }
        }
    }

    private void readBlock(NitfImageSegmentHeader imageSegmentHeader, ImageBlock block,
            ImageInputStream imageInputStream, ImageRepresentationHandler imageRepresentationHandler, int bandIndex) {

        final DataBuffer data = block.getData();
        final int blockHeight = imageSegmentHeader.getNumberOfPixelsPerBlockVertical();
        final int blockWidth = imageSegmentHeader.getNumberOfPixelsPerBlockHorizontal();

        try {
            for (int row = 0; row < blockHeight; row++) {
                for (int column = 0; column < blockWidth; column++) {
                    int i = row * blockWidth + column;
                    int bandValue = imageInputStream.read();
                    data.setElem(i, imageRepresentationHandler.renderPixel(data.getElem(i), imageInputStream, bandIndex));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void renderBlock(NitfImageSegmentHeader imageSegmentHeader, Graphics2D targetImage, ImageBlock block ) {
        final int blockWidth = imageSegmentHeader.getNumberOfPixelsPerBlockHorizontal();
        final int blockHeight = imageSegmentHeader.getNumberOfPixelsPerBlockVertical();

        BufferedImage img = new BufferedImage(blockWidth, blockHeight, BufferedImage.TYPE_INT_ARGB);
        int[] imgData = ((DataBufferInt) img.getRaster().getDataBuffer()).getData();
        System.arraycopy(block.getData(), 0, imgData, 0, block.getData().length);
        targetImage.drawImage(img, block.getColumn() * blockHeight, block.getRow() * blockWidth, null);
    }

//    private void applyMask(NitfImageSegmentHeader imageSegmentHeader,
//            ImageInputStream imageInputStream, ImageBlock block) throws IOException {
//        final DataBuffer data = block.getData();
//        final int dataSize = data.array().length;
//        ImageMask imageMask = null;
//
//        if (imageSegmentHeader.getImageCompression() == ImageCompression.NOTCOMPRESSEDMASK) {
//            imageMask = new ImageMask(imageSegmentHeader, imageInputStream);
//        } else {
//            imageMask = new ImageMask(imageSegmentHeader);
//        }
//
//        if (imageMask != null) {
//            for (int pixel = 0; pixel < dataSize; ++pixel) {
//                data.put(pixel, data.get(pixel) | 0xFF000000);
//
//                if (imageMask.isPadPixel(data.get(pixel))) {
//                    data.put(pixel, 0x00000000);
//                }
//            }
//        } else {
//            for (int pixel = 0; pixel < dataSize; ++pixel) {
//                data.put(pixel, data.get(pixel) | 0xFF000000);
//            }
//        }
//    }
}
