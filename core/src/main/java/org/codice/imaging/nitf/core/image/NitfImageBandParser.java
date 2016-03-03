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
package org.codice.imaging.nitf.core.image;

import java.text.ParseException;
import org.codice.imaging.nitf.core.common.NitfReader;
import static org.codice.imaging.nitf.core.image.ImageConstants.IFC_LENGTH;
import static org.codice.imaging.nitf.core.image.ImageConstants.IMFLT_LENGTH;
import static org.codice.imaging.nitf.core.image.ImageConstants.IREPBAND_LENGTH;
import static org.codice.imaging.nitf.core.image.ImageConstants.ISUBCAT_LENGTH;
import static org.codice.imaging.nitf.core.image.ImageConstants.NELUT_LENGTH;
import static org.codice.imaging.nitf.core.image.ImageConstants.NLUTS_LENGTH;

/**
    Image Band and Image Band LUT parser.
*/
class NitfImageBandParser {

    private NitfReader reader = null;
    private NitfImageBand imageBand = null;
    private int numLUTs = 0;

    /**
        Construct from a NitfReader instance.

        @param nitfReader the reader, positioned to read an image band.
        @throws ParseException if an obviously invalid value is detected during parsing,
        or if another problem occurs during parsing (e.g. end of file).
    */
    NitfImageBandParser(final NitfReader nitfReader, final NitfImageBand band) throws ParseException {
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
                NitfImageBandLUT lut = new NitfImageBandLUT(reader.readBytesRaw(imageBand.getNumLUTEntries()));
                imageBand.addLUT(lut);
            }
        }
    }

    private void readIREPBAND() throws ParseException {
        imageBand.setImageRepresentation(reader.readTrimmedBytes(IREPBAND_LENGTH));
    }

    private void readISUBCAT() throws ParseException {
        imageBand.setImageSubcategory(reader.readTrimmedBytes(ISUBCAT_LENGTH));
    }

    private void readIFC() throws ParseException {
        reader.skip(IFC_LENGTH);
    }

    private void readIMFLT() throws ParseException {
        reader.skip(IMFLT_LENGTH);
    }

    private void readNLUTS() throws ParseException {
        numLUTs = reader.readBytesAsInteger(NLUTS_LENGTH);
    }

    private void readNELUT() throws ParseException {
        imageBand.setNumLUTEntries(reader.readBytesAsInteger(NELUT_LENGTH));
    }
}
