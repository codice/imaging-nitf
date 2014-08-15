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
package org.codice.imaging.nitf.core;

import java.text.ParseException;
import java.util.Set;

/**
    Parser for a symbol segment subheader in a NITF 2.0 file.
*/
class NitfSymbolSegmentParser extends AbstractNitfSegmentParser {

    private int lengthOfSymbol = 0;

    private int numberOfEntriesInLUT = 0;
    private int symbolExtendedSubheaderLength = 0;

    private static final String SY = "SY";
    private static final int SID_LENGTH = 10;
    private static final int SNAME_LENGTH = 20;
    private static final int STYPE_LENGTH = 1;
    private static final int NLIPS_LENGTH = 4;
    private static final int NPIXPL_LENGTH = 4;
    private static final int NWDTH_LENGTH = 4;
    private static final int NBPP_LENGTH = 1;
    private static final int SDLVL_LENGTH = 3;
    private static final int SALVL_LENGTH = 3;
    private static final int SLOC_HALF_LENGTH = 5;
    private static final int SCOLOR_LENGTH = 1;
    private static final int SNUM_LENGTH = 6;
    private static final int SROT_LENGTH = 3;
    private static final int NELUT_LENGTH = 3;
    private static final int SXSHDL_LENGTH = 5;
    private static final int SXSOFL_LENGTH = 3;

    private boolean shouldParseSymbolData = false;

    private NitfSymbolSegment segment = null;

    NitfSymbolSegmentParser(final NitfReader nitfReader,
                                    final int symbolLength,
                                    final Set<ParseOption> parseOptions,
                                    final NitfSymbolSegment symbolSegment) throws ParseException {

        reader = nitfReader;
        lengthOfSymbol = symbolLength;
        segment = symbolSegment;

        shouldParseSymbolData = parseOptions.contains(ParseOption.EXTRACT_SYMBOL_SEGMENT_DATA);

        readSY();
        readSID();
        readSNAME();
        segment.setSecurityMetadata(new NitfSecurityMetadata(reader));
        readENCRYP();
        readSTYPE();
        readNLIPS();
        readNPIXPL();
        readNWDTH();
        readNBPP();
        readSDLVL();
        readSALVL();
        readSLOC();
        readSLOC2();
        readSCOLOR();
        readSNUM();
        readSROT();
        readNELUT();
        for (int i = 0; i < numberOfEntriesInLUT; ++i) {
            throw new UnsupportedOperationException("TODO: Implement LUT parsing when we have an example");
        }
        readSXSHDL();
        if (symbolExtendedSubheaderLength > 0) {
            readSXSOFL();
            readSXSHD();
        }
        readSymbolData();
    }

    private void readSY() throws ParseException {
        reader.verifyHeaderMagic(SY);
    }

    private void readSID() throws ParseException {
        segment.setIdentifier(reader.readTrimmedBytes(SID_LENGTH));
    }

    private void readSNAME() throws ParseException {
        segment.setSymbolName(reader.readTrimmedBytes(SNAME_LENGTH));
    }

    private void readSTYPE() throws ParseException {
        String stype = reader.readTrimmedBytes(STYPE_LENGTH);
        segment.setSymbolType(SymbolType.getEnumValue(stype));
    }

    private void readNLIPS() throws ParseException {
        segment.setNumberOfLinesPerSymbol(reader.readBytesAsInteger(NLIPS_LENGTH));
    }

    private void readNPIXPL() throws ParseException {
        segment.setNumberOfPixelsPerLine(reader.readBytesAsInteger(NPIXPL_LENGTH));
    }

    private void readNWDTH() throws ParseException {
        segment.setLineWidth(reader.readBytesAsInteger(NWDTH_LENGTH));
    }

    private void readNBPP() throws ParseException {
        segment.setNumberOfBitsPerPixel(reader.readBytesAsInteger(NBPP_LENGTH));
    }

    private void readSDLVL() throws ParseException {
        segment.setSymbolDisplayLevel(reader.readBytesAsInteger(SDLVL_LENGTH));
    }

    private void readSALVL() throws ParseException {
        segment.setAttachmentLevel(reader.readBytesAsInteger(SALVL_LENGTH));
    }

    private void readSLOC() throws ParseException {
        segment.setSymbolLocationRow(reader.readBytesAsInteger(SLOC_HALF_LENGTH));
        segment.setSymbolLocationColumn(reader.readBytesAsInteger(SLOC_HALF_LENGTH));
    }

    private void readSLOC2() throws ParseException {
        segment.setSymbolLocation2Row(reader.readBytesAsInteger(SLOC_HALF_LENGTH));
        segment.setSymbolLocation2Column(reader.readBytesAsInteger(SLOC_HALF_LENGTH));
    }

    private void readSCOLOR() throws ParseException {
        String scolor = reader.readTrimmedBytes(SCOLOR_LENGTH);
        segment.setSymbolColourFormat(SymbolColour.getEnumValue(scolor));
    }

    private void readSNUM() throws ParseException {
        segment.setSymbolNumber(reader.readBytes(SNUM_LENGTH));
    }

    private void readSROT() throws ParseException {
        segment.setSymbolRotation(reader.readBytesAsInteger(SROT_LENGTH));
    }

    private void readNELUT() throws ParseException {
        numberOfEntriesInLUT = reader.readBytesAsInteger(NELUT_LENGTH);
    }

    private void readSXSHDL() throws ParseException {
        symbolExtendedSubheaderLength = reader.readBytesAsInteger(SXSHDL_LENGTH);
    }

    private void readSXSOFL() throws ParseException {
        segment.setExtendedHeaderDataOverflow(reader.readBytesAsInteger(SXSOFL_LENGTH));
    }

    private void readSXSHD() throws ParseException {
        TreParser treParser = new TreParser();
        TreCollection extendedSubheaderTREs = treParser.parse(reader, symbolExtendedSubheaderLength - SXSOFL_LENGTH);
        segment.mergeTREs(extendedSubheaderTREs);
    }

    private void readSymbolData() throws ParseException {
        if (lengthOfSymbol == 0) {
            return;
        }
        if (shouldParseSymbolData) {
            segment.setSymbolData(reader.readBytesRaw(lengthOfSymbol));
        } else {
            reader.skip(lengthOfSymbol);
        }
    }
}
