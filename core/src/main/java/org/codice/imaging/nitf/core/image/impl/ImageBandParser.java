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
package org.codice.imaging.nitf.core.image.impl;

import static org.codice.imaging.nitf.core.image.impl.ImageConstants.IFC_LENGTH;
import static org.codice.imaging.nitf.core.image.impl.ImageConstants.IMFLT_LENGTH;
import static org.codice.imaging.nitf.core.image.impl.ImageConstants.IREPBAND_LENGTH;
import static org.codice.imaging.nitf.core.image.impl.ImageConstants.ISUBCAT_LENGTH;
import static org.codice.imaging.nitf.core.image.impl.ImageConstants.NELUT_LENGTH;
import static org.codice.imaging.nitf.core.image.impl.ImageConstants.NLUTS_LENGTH;

import org.codice.imaging.nitf.core.common.NitfFormatException;
import org.codice.imaging.nitf.core.common.NitfReader;
import org.codice.imaging.nitf.core.image.ImageBandLUT;

/**
    Image Band and Image Band LUT parser.
*/
class ImageBandParser {

    private NitfReader reader = null;
    private ImageBandImpl imageBand = null;
    private int numLUTs = 0;

    /**
        Construct from a NitfReader instance.

        @param nitfReader the reader, positioned to read an image band.
        @throws NitfFormatException if an obviously invalid value is detected during parsing,
        or if another problem occurs during parsing (e.g. end of file).
    */
    ImageBandParser(final NitfReader nitfReader, final ImageBandImpl band) throws NitfFormatException {
        reader = nitfReader;
        imageBand = band;
        readIREPBAND();
        readISUBCAT();
        readIFC();
        readIMFLT();
        readNLUTS();
        if (numLUTs > 0) {
            readNELUT();
            for (int i = 0; i < numLUTs; ++i) {
                ImageBandLUT lut = new ImageBandLUTImpl(reader.readBytesRaw(imageBand.getNumLUTEntries()));
                imageBand.addLUT(lut);
            }
        }
    }

    private void readIREPBAND() throws NitfFormatException {
        imageBand.setImageRepresentation(reader.readTrimmedBytes(IREPBAND_LENGTH));
    }

    private void readISUBCAT() throws NitfFormatException {
        imageBand.setImageSubcategory(reader.readTrimmedBytes(ISUBCAT_LENGTH));
    }

    private void readIFC() throws NitfFormatException {
        reader.skip(IFC_LENGTH);
    }

    private void readIMFLT() throws NitfFormatException {
        reader.skip(IMFLT_LENGTH);
    }

    private void readNLUTS() throws NitfFormatException {
        numLUTs = reader.readBytesAsInteger(NLUTS_LENGTH);
    }

    private void readNELUT() throws NitfFormatException {
        imageBand.setNumLUTEntries(reader.readBytesAsInteger(NELUT_LENGTH));
    }
}
