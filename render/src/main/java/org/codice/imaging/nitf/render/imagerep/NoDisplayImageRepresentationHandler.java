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
import javax.imageio.stream.ImageInputStream;
import org.codice.imaging.nitf.render.ImageMask;

class NoDisplayImageRepresentationHandler implements ImageRepresentationHandler {
    @Override
    public void renderPixelBand(final DataBuffer dataBuffer, final int pixelIndex,
            final ImageInputStream imageInputStream, final int bandIndex) {
        // Intentionally Empty
    }

    @Override
    public final BufferedImage createBufferedImage(final int width, final int height) {
        return new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
    }

    @Override
    public final void renderPadPixel(final ImageMask imageMask, final DataBuffer data, final int pixelIndex) {
        return;
    }
}
