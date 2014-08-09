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
import java.util.Set;

class NitfGraphicSegmentParser extends AbstractNitfSegmentParser {

    private int lengthOfGraphic = 0;

    private int graphicExtendedSubheaderLength = 0;

    private static final String SY = "SY";
    private static final int SID_LENGTH = 10;
    private static final int SNAME_LENGTH = 20;
    private static final String SFMT_CGM = "C";
    private static final String SSTRUCT = "0000000000000";
    private static final int SDLVL_LENGTH = 3;
    private static final int SALVL_LENGTH = 3;
    private static final int SLOC_HALF_LENGTH = 5;
    private static final int SBND1_HALF_LENGTH = 5;
    private static final int SCOLOR_LENGTH = 1;
    private static final int SBND2_HALF_LENGTH = 5;
    private static final String SRES = "00";
    private static final int SXSHDL_LENGTH = 5;
    private static final int SXSOFL_LENGTH = 3;

    private boolean shouldParseGraphicData = false;

    private NitfGraphicSegment segment = null;

    NitfGraphicSegmentParser(final NitfReader nitfReader,
                                    final int graphicLength,
                                    final Set<ParseOption> parseOptions,
                                    final NitfGraphicSegment graphicSegment) throws ParseException {

        reader = nitfReader;
        lengthOfGraphic = graphicLength;
        segment = graphicSegment;

        shouldParseGraphicData = parseOptions.contains(ParseOption.ExtractGraphicSegmentData);

        readSY();
        readSID();
        readSNAME();
        segment.setSecurityMetadata(new NitfSecurityMetadata(reader));
        reader.readENCRYP();
        readSFMT();
        readSSTRUCT();
        readSDLVL();
        readSALVL();
        readSLOC();
        readSBND1();
        readSCOLOR();
        readSBND2();
        readSRES();
        readSXSHDL();
        if (graphicExtendedSubheaderLength > 0) {
            readSXSOFL();
            readSXSHD();
        }
        readGraphicData();
    }

    private void readSY() throws ParseException {
        reader.verifyHeaderMagic(SY);
    }

    private void readSID() throws ParseException {
        segment.setGraphicIdentifier(reader.readTrimmedBytes(SID_LENGTH));
    }

    private void readSNAME() throws ParseException {
        segment.setGraphicName(reader.readTrimmedBytes(SNAME_LENGTH));
    }

    private void readSFMT() throws ParseException {
        reader.verifyHeaderMagic(SFMT_CGM);
    }

    private void readSSTRUCT() throws ParseException {
        reader.verifyHeaderMagic(SSTRUCT);
    }

    private void readSDLVL() throws ParseException {
        segment.setGraphicDisplayLevel(reader.readBytesAsInteger(SDLVL_LENGTH));
    }

    private void readSALVL() throws ParseException {
        segment.setGraphicAttachmentLevel(reader.readBytesAsInteger(SALVL_LENGTH));
    }

    private void readSLOC() throws ParseException {
        segment.setGraphicLocationRow(reader.readBytesAsInteger(SLOC_HALF_LENGTH));
        segment.setGraphicLocationColumn(reader.readBytesAsInteger(SLOC_HALF_LENGTH));
    }

    private void readSBND1() throws ParseException {
        segment.setBoundingBox1Row(reader.readBytesAsInteger(SBND1_HALF_LENGTH));
        segment.setBoundingBox1Column(reader.readBytesAsInteger(SBND1_HALF_LENGTH));
    }

    private void readSCOLOR() throws ParseException {
        String scolor = reader.readTrimmedBytes(SCOLOR_LENGTH);
        segment.setGraphicColour(GraphicColour.getEnumValue(scolor));
    }

    private void readSBND2() throws ParseException {
        segment.setBoundingBox2Row(reader.readBytesAsInteger(SBND2_HALF_LENGTH));
        segment.setBoundingBox2Column(reader.readBytesAsInteger(SBND2_HALF_LENGTH));
    }

    private void readSRES() throws ParseException {
        reader.verifyHeaderMagic(SRES);
    }

    private void readSXSHDL() throws ParseException {
        graphicExtendedSubheaderLength = reader.readBytesAsInteger(SXSHDL_LENGTH);
    }

    private void readSXSOFL() throws ParseException {
        segment.setExtendedHeaderDataOverflow(reader.readBytesAsInteger(SXSOFL_LENGTH));
    }

    private void readSXSHD() throws ParseException {
        TreParser treParser = new TreParser();
        TreCollection extendedSubheaderTREs = treParser.parse(reader, graphicExtendedSubheaderLength - SXSOFL_LENGTH);
        segment.mergeTREs(extendedSubheaderTREs);
    }

    private void readGraphicData() throws ParseException {
        if (lengthOfGraphic == 0) {
            return;
        }
        if (shouldParseGraphicData) {
            segment.setGraphicData(reader.readBytesRaw(lengthOfGraphic));
        } else {
            reader.skip(lengthOfGraphic);
        }
    }
}
