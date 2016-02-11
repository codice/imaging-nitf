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
import java.io.IOException;
import org.codice.imaging.nitf.core.image.ImageCompression;
import org.codice.imaging.nitf.core.image.ImageSegment;
import org.codice.imaging.nitf.render.ImageMask;
import org.codice.imaging.nitf.render.imagerep.ImageRepresentationHandler;

/**
 * A shared implementation of the various ImageModeHandler cases.
 */
abstract class SharedImageModeHandler extends BaseImageModeHandler implements ImageModeHandler {
    protected static final String NULL_ARG_ERROR_MESSAGE = "%s: argument '%s' may not be null.";

    protected final ImageRepresentationHandler imageRepresentationHandler;

    protected SharedImageModeHandler(ImageRepresentationHandler imageRepresentationHandler) {
        checkNull(imageRepresentationHandler, "imageRepresentationHandler");
        this.imageRepresentationHandler = imageRepresentationHandler;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handleImage(ImageSegment imageSegment, Graphics2D targetImage) throws IOException {

        nullCheckArguments(imageSegment, targetImage, imageRepresentationHandler);

        ImageMask iMask = null;

        if (ImageCompression.NOTCOMPRESSEDMASK.equals(imageSegment.getImageCompression())) {
            iMask = new ImageMask(imageSegment, imageSegment.getData());
        } else {
            iMask = new ImageMask(imageSegment);
        }

        final ImageMask imageMask = iMask;

        checkImageMode(imageSegment);

        ImageBlockMatrix matrix = new ImageBlockMatrix(imageSegment,
                () -> imageRepresentationHandler.createBufferedImage(imageSegment.getNumberOfPixelsPerBlockHorizontal(),
                        imageSegment.getNumberOfPixelsPerBlockVertical()));

        matrix.forEachBlock(block -> {
            if (!imageMask.isMaskedBlock(block.getBlockIndex(), 0)) {
                readBlock(block, imageSegment);
                applyMask(block, imageMask);
            }
        });

        matrix.forEachBlock((block) -> block.render(targetImage, true));
    }

    protected abstract void readBlock(ImageBlock block, ImageSegment imageSegment);

    private void nullCheckArguments(ImageSegment imageSegment, Graphics2D targetImage,
            ImageRepresentationHandler imageRepresentationHandler) {
        checkNull(imageSegment, "imageSegment");
        checkNull(targetImage, "targetImage");
        checkNull(imageRepresentationHandler, "imageRepresentationHandler");
    }

    private void checkNull(Object value, String valueName) {
        if (value == null) {
            throw new IllegalArgumentException(String.format(NULL_ARG_ERROR_MESSAGE, getHandlerName(), valueName));
        }
    }

    private void applyMask(ImageBlock block, ImageMask imageMask) {
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

}
