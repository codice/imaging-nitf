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
import org.codice.imaging.nitf.core.image.ImageSegment;
import org.codice.imaging.nitf.render.ImageMask;
import org.codice.imaging.nitf.render.imagerep.ImageRepresentationHandler;

/**
 * A shared implementation of the various ImageModeHandler cases.
 */
abstract class SharedImageModeHandler extends BaseImageModeHandler implements ImageModeHandler {

    protected SharedImageModeHandler(final ImageRepresentationHandler imageRepresentationHandler) {
        checkNull(imageRepresentationHandler, "imageRepresentationHandler");
        this.imageRepresentationHandler = imageRepresentationHandler;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handleImage(final ImageSegment imageSegment, final Graphics2D targetImage) throws IOException {
        checkNull(imageSegment, "imageSegment");
        checkNull(targetImage, "targetImage");
        checkImageMode(imageSegment);

        final ImageMask imageMask = getImageMask(imageSegment);

        ImageBlockMatrix matrix = new ImageBlockMatrix(imageSegment,
                () -> imageRepresentationHandler.createBufferedImage((int) imageSegment.getNumberOfPixelsPerBlockHorizontal(),
                        (int) imageSegment.getNumberOfPixelsPerBlockVertical()));

        matrix.forEachBlock(block -> {
            if (!imageMask.isMaskedBlock(block.getBlockIndex(), 0)) {
                readBlock(block, imageSegment);
                applyMask(block, imageMask);
            }
        });

        matrix.forEachBlock(block -> block.render(targetImage, true));
    }

    protected abstract void readBlock(final ImageBlock block, final ImageSegment imageSegment);

}
