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

import java.awt.image.DataBuffer;
import java.io.IOException;
import java.util.Map;

import javax.imageio.stream.ImageInputStream;

class Rgb48ImageRepresentationHandler extends AbstractRgbImageRepresentationHandler {
    Rgb48ImageRepresentationHandler(final Map<Integer, Integer> bandMap,
            final int actualBitsPerPixelPerBand) {
        super(bandMap, actualBitsPerPixelPerBand);
    }

    @Override
    public final void renderPixelBand(final DataBuffer data, final int pixelIndex, final ImageInputStream imageInputStream,
            final int bandIndex) throws IOException {
        int pixelBandValue = (imageInputStream.read() << Byte.SIZE) | (imageInputStream.read());

        //Convert the value down to 8 bits.
        pixelBandValue = pixelBandValue >> bitsToDiscard;

        data.setElem(pixelIndex,
                ALPHA_MASK | data.getElem(pixelIndex) | (pixelBandValue
                        << bandMapping.get(bandIndex)));
    }
}
