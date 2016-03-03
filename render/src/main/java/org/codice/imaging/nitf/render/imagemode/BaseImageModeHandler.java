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

import java.io.IOException;
import org.codice.imaging.nitf.core.image.ImageCompression;
import org.codice.imaging.nitf.core.image.ImageMode;
import org.codice.imaging.nitf.core.image.ImageSegment;
import org.codice.imaging.nitf.render.ImageMask;
import org.codice.imaging.nitf.render.imagerep.ImageRepresentationHandler;

abstract class BaseImageModeHandler implements ImageModeHandler {
    private static final String NULL_ARG_ERROR_MESSAGE = "%s: argument '%s' may not be null.";

    protected ImageRepresentationHandler imageRepresentationHandler;

    abstract ImageMode getSupportedImageMode();

    abstract String getHandlerName();

    protected void checkImageMode(final ImageSegment imageSegment) {
        if (!getSupportedImageMode().equals(imageSegment.getImageMode())) {
            throw new IllegalStateException(String.format("%s: argument 'imageSegment' must have an ImageMode of '%s'.",
                    getHandlerName(),
                    getSupportedImageMode().getTextEquivalent()
            ));
        }
    }

    protected void checkNull(final Object value, final String valueName) {
        if (value == null) {
            throw new IllegalArgumentException(String.format(NULL_ARG_ERROR_MESSAGE, getHandlerName(), valueName));
        }
    }

    protected ImageMask getImageMask(final ImageSegment imageSegment) throws IOException {
        if (ImageCompression.NOTCOMPRESSEDMASK.equals(imageSegment.getImageCompression())) {
            return new ImageMask(imageSegment, imageSegment.getData());
        } else {
            return new ImageMask(imageSegment);
        }
    }

    protected void applyMask(final ImageBlock block, final ImageMask imageMask) {
        if ((imageMask != null) && (imageMask.hasPixelMasks())) {
            final int dataSize = block.getWidth() * block.getHeight();

            for (int pixelIndex = 0; pixelIndex < dataSize; ++pixelIndex) {
                imageRepresentationHandler.renderPadPixel(imageMask, block.getDataBuffer(), pixelIndex);
            }
        }
    }
}
