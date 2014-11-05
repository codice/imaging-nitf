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
package org.codice.imaging.nitf.core;

import java.text.ParseException;

/**
    Parser for a graphic segment subheader in a NITF 2.1 / NSIF 1.0 file.
*/
class NitfGraphicSegmentHeaderParser extends AbstractNitfSegmentParser {

    private int graphicExtendedSubheaderLength = 0;

    private NitfGraphicSegmentHeader segment = null;

    NitfGraphicSegmentHeaderParser() {
    }

    final NitfGraphicSegmentHeader parse(final NitfReader nitfReader) throws ParseException {

        reader = nitfReader;
        segment = new NitfGraphicSegmentHeader();

        readSY();
        readSID();
        readSNAME();
        segment.setSecurityMetadata(new NitfSecurityMetadata(reader));
        readENCRYP();
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
        return segment;
    }

    private void readSY() throws ParseException {
        reader.verifyHeaderMagic(NitfConstants.SY);
    }

    private void readSID() throws ParseException {
        segment.setIdentifier(reader.readTrimmedBytes(NitfConstants.SID_LENGTH));
    }

    private void readSNAME() throws ParseException {
        segment.setGraphicName(reader.readTrimmedBytes(NitfConstants.SNAME_LENGTH));
    }

    private void readSFMT() throws ParseException {
        reader.verifyHeaderMagic(NitfConstants.SFMT_CGM);
    }

    private void readSSTRUCT() throws ParseException {
        reader.verifyHeaderMagic(NitfConstants.SSTRUCT);
    }

    private void readSDLVL() throws ParseException {
        segment.setGraphicDisplayLevel(reader.readBytesAsInteger(NitfConstants.SDLVL_LENGTH));
    }

    private void readSALVL() throws ParseException {
        segment.setAttachmentLevel(reader.readBytesAsInteger(NitfConstants.SALVL_LENGTH));
    }

    private void readSLOC() throws ParseException {
        segment.setGraphicLocationRow(reader.readBytesAsInteger(NitfConstants.SLOC_HALF_LENGTH));
        segment.setGraphicLocationColumn(reader.readBytesAsInteger(NitfConstants.SLOC_HALF_LENGTH));
    }

    private void readSBND1() throws ParseException {
        segment.setBoundingBox1Row(reader.readBytesAsInteger(NitfConstants.SBND1_HALF_LENGTH));
        segment.setBoundingBox1Column(reader.readBytesAsInteger(NitfConstants.SBND1_HALF_LENGTH));
    }

    private void readSCOLOR() throws ParseException {
        String scolor = reader.readBytes(NitfConstants.SCOLOR_LENGTH);
        segment.setGraphicColour(GraphicColour.getEnumValue(scolor));
    }

    private void readSBND2() throws ParseException {
        segment.setBoundingBox2Row(reader.readBytesAsInteger(NitfConstants.SBND2_HALF_LENGTH));
        segment.setBoundingBox2Column(reader.readBytesAsInteger(NitfConstants.SBND2_HALF_LENGTH));
    }

    private void readSRES() throws ParseException {
        reader.verifyHeaderMagic(NitfConstants.SRES);
    }

    private void readSXSHDL() throws ParseException {
        graphicExtendedSubheaderLength = reader.readBytesAsInteger(NitfConstants.SXSHDL_LENGTH);
    }

    private void readSXSOFL() throws ParseException {
        segment.setExtendedHeaderDataOverflow(reader.readBytesAsInteger(NitfConstants.SXSOFL_LENGTH));
    }

    private void readSXSHD() throws ParseException {
        TreCollectionParser treCollectionParser = new TreCollectionParser();
        TreCollection extendedSubheaderTREs = treCollectionParser.parse(reader, graphicExtendedSubheaderLength - NitfConstants.SXSOFL_LENGTH);
        segment.mergeTREs(extendedSubheaderTREs);
    }
}
