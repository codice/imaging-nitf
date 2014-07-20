/**
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
 **/
package org.codice.nitf.filereader;

import java.text.ParseException;
import java.util.ArrayList;

public class NitfImageBand {

    private NitfReader reader = null;

    // An enum might have been useful, but this is extensible
    private String imageRepresentation = null;
    private String imageSubcategory = null;
    private int numLUTs = 0;
    private int numEntriesLUT = 0;
    private ArrayList<NitfImageBandLUT> luts = new ArrayList<NitfImageBandLUT>();

    private static final int IREPBAND_LENGTH = 2;
    private static final int ISUBCAT_LENGTH = 6;
    private static final int IFC_LENGTH = 1;
    private static final int IMFLT_LENGTH = 3;
    private static final int NLUTS_LENGTH = 1;
    private static final int NELUT_LENGTH = 5;

    public NitfImageBand(NitfReader nitfReader) throws ParseException {
        reader = nitfReader;
        readIREPBAND();
        readISUBCAT();
        readIFC();
        readIMFLT();
        readNLUTS();
        if (numLUTs > 0) {
            readNELUT();
            for (int i = 0; i < numLUTs; ++i) {
                luts.add(new NitfImageBandLUT());
            }
            for (int i = 0; i < numLUTs; ++i) {
                for (int j = 0; j < numEntriesLUT; ++j) {
                    // TODO: read bytes for real
                    reader.skip(1);
                }
            }
        }
    }

    public String getImageRepresentation() {
        return imageRepresentation;
    }

    public String getSubCategory() {
        return imageSubcategory;
    }

    public int getNumLUTs() {
        return numLUTs;
    }

    public int getNumLUTEntries() {
        return numEntriesLUT;
    }

    public NitfImageBandLUT getLUT(int lutNumber) {
        return getLUTZeroBase(lutNumber - 1);
    }

    public NitfImageBandLUT getLUTZeroBase(int lutNumberZeroBase) {
        return luts.get(lutNumberZeroBase);
    }

    private void readIREPBAND() throws ParseException {
        imageRepresentation = reader.readTrimmedBytes(IREPBAND_LENGTH);
    }

    private void readISUBCAT() throws ParseException {
        imageSubcategory = reader.readTrimmedBytes(ISUBCAT_LENGTH);
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
        numEntriesLUT = reader.readBytesAsInteger(NELUT_LENGTH);
    }
}
