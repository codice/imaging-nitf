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
package org.codice.imaging.nitf.render.datareader;

import java.io.IOException;
import javax.imageio.stream.ImageInputStream;
import org.codice.imaging.nitf.core.image.ImageSegment;
import org.codice.imaging.nitf.core.image.PixelJustification;

/**
 * IOReaderFunction for 12 and 16 bit images that require bit shifts (i.e. NBPPB != ABPPB).
 *
 * The implementation should handle reading NBPP >= ABPP, where NBPP >= 16, and ABPP >=16, for either left or right
 * pixel justification.
 */
class Bitshift16IOReaderFunction implements IOReaderFunction {

    private final int bitsToRead;
    private final int bitShift;

    public Bitshift16IOReaderFunction(ImageSegment segment) {
        this.bitsToRead = segment.getNumberOfBitsPerPixelPerBand();
        if (segment.getPixelJustification() == PixelJustification.RIGHT) {
            this.bitShift = Short.SIZE - segment.getActualBitsPerPixelPerBand();
        } else {
            this.bitShift = Short.SIZE - segment.getNumberOfBitsPerPixelPerBand();
        }
    }

    @Override
    public Object apply(Object imageInputStream) throws IOException {
        return (int) (((ImageInputStream) imageInputStream).readBits(this.bitsToRead) << this.bitShift);
    }

}
