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

import javax.imageio.stream.ImageInputStream;
import org.codice.imaging.nitf.core.image.ImageSegment;
import org.codice.imaging.nitf.core.image.PixelJustification;

/**
 * Factory class for creating data readers.
 */
public class DataReaderFactory {

    private DataReaderFactory() {
    }

    /**
     * Get an appropriate data reader function for the specified image segment.
     *
     * In this context, the reader gets the data value for a single pixel for a
     * single band.
     *
     * @param segment the image segment specifying the image characteristics to
     * be read.
     * @return a reader for the segment data pixels, or null if an appropriate
     * reader could not be found.
     */
    public static IOReaderFunction forImageSegment(ImageSegment segment) {
        if ((segment.getActualBitsPerPixelPerBand() != segment.getNumberOfBitsPerPixelPerBand())
                && (segment.getPixelJustification() == PixelJustification.RIGHT)) {
            return getBitshiftReader(segment);
        }
        switch (segment.getNumberOfBitsPerPixelPerBand()) {
            case 1:
                return (imageInputStream) -> ((ImageInputStream) imageInputStream).readBit();
            case 8:
                return (imageInputStream) -> ((ImageInputStream) imageInputStream).readUnsignedByte();
            case 12:
                return new Bitshift16IOReaderFunction(segment);
            case 16:
                return (imageInputStream) -> ((ImageInputStream) imageInputStream).readUnsignedShort();
            default:
                return null;
        }
    }

    private static IOReaderFunction getBitshiftReader(ImageSegment segment) {
        if (segment.getNumberOfBitsPerPixelPerBand() <= 8) {
            return new Bitshift8IOReaderFunction(segment);
        } else if (segment.getNumberOfBitsPerPixelPerBand() <= 16) {
            return new Bitshift16IOReaderFunction(segment);
        }

        return null;
    }
}
