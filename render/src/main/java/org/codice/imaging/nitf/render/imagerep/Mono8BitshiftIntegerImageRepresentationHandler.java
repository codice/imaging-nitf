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
import org.codice.imaging.nitf.core.image.ImageSegment;
import org.codice.imaging.nitf.core.image.PixelJustification;

/**
 * Image representation handler for 8 bit images needing bit shifts on data.
 *
 * In NITF, this means actual bits per pixel per band does not equal number of bits per pixel per band. It also handles
 * cases where the number of bits is not a whole byte.
 */
class Mono8BitshiftIntegerImageRepresentationHandler extends SharedMonoImageRepresentationHandler implements ImageRepresentationHandler {

    private final int bitsToRead;
    private final int bitShift;

    public Mono8BitshiftIntegerImageRepresentationHandler(ImageSegment segment, int selectedBandZeroBase) {
        super(selectedBandZeroBase);
        this.bitsToRead = segment.getNumberOfBitsPerPixelPerBand();
        if (segment.getPixelJustification() == PixelJustification.RIGHT) {
            this.bitShift = Byte.SIZE - segment.getActualBitsPerPixelPerBand();
        } else {
            this.bitShift = Byte.SIZE - segment.getNumberOfBitsPerPixelPerBand();
        }
    }

    @Override
    public void renderPixelBand(DataBuffer dataBuffer, int pixelIndex, ImageInputStream imageInputStream, int bandIndex) throws IOException {
        if (bandIndex != selectedBandZeroBase) {
            imageInputStream.readBits(this.bitsToRead);
        } else {
            dataBuffer.setElem(pixelIndex, (byte) (imageInputStream.readBits(this.bitsToRead) << this.bitShift));
        }
    }

    @Override
    public BufferedImage createBufferedImage(int width, int height) {
        return new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
    }

}
