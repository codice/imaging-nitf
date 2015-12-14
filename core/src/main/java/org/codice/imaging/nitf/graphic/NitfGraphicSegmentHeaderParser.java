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
package org.codice.imaging.nitf.graphic;

import static org.codice.imaging.nitf.graphic.NitfGraphicSegmentConstants.SALVL_LENGTH;
import static org.codice.imaging.nitf.graphic.NitfGraphicSegmentConstants.SBND1_HALF_LENGTH;
import static org.codice.imaging.nitf.graphic.NitfGraphicSegmentConstants.SBND2_HALF_LENGTH;
import static org.codice.imaging.nitf.graphic.NitfGraphicSegmentConstants.SCOLOR_LENGTH;
import static org.codice.imaging.nitf.graphic.NitfGraphicSegmentConstants.SDLVL_LENGTH;
import static org.codice.imaging.nitf.graphic.NitfGraphicSegmentConstants.SFMT_CGM;
import static org.codice.imaging.nitf.graphic.NitfGraphicSegmentConstants.SID_LENGTH;
import static org.codice.imaging.nitf.graphic.NitfGraphicSegmentConstants.SLOC_HALF_LENGTH;
import static org.codice.imaging.nitf.graphic.NitfGraphicSegmentConstants.SNAME_LENGTH;
import static org.codice.imaging.nitf.graphic.NitfGraphicSegmentConstants.SRES;
import static org.codice.imaging.nitf.graphic.NitfGraphicSegmentConstants.SSTRUCT;
import static org.codice.imaging.nitf.graphic.NitfGraphicSegmentConstants.SXSHDL_LENGTH;
import static org.codice.imaging.nitf.graphic.NitfGraphicSegmentConstants.SXSOFL_LENGTH;
import static org.codice.imaging.nitf.graphic.NitfGraphicSegmentConstants.SY;

import java.text.ParseException;

import org.codice.imaging.nitf.common.reader.NitfReader;
import org.codice.imaging.nitf.parser.AbstractNitfSegmentParser;
import org.codice.imaging.nitf.security.NitfSecurityMetadataImpl;
import org.codice.imaging.nitf.parser.strategy.NitfParseStrategy;
import org.codice.imaging.nitf.tre.TreCollectionImpl;

/**
    Parser for a image segment subheader in a NITF 2.1 / NSIF 1.0 file.
*/
public class NitfGraphicSegmentHeaderParser extends AbstractNitfSegmentParser {


    private int graphicExtendedSubheaderLength = 0;

    private NitfGraphicSegmentHeader segment = null;

    /**
     *
     * @param nitfReader - the NitfReader that streams the graphic segment header input.
     * @param parseStrategy - the ParseStrategy that identifies which segment header attributes to parse or skip.
     * @return - the fully parse NitfGraphicSegmentHeader.
     * @throws ParseException - when an unexpected value is encountered in the input stream.
     */
    public final NitfGraphicSegmentHeader parse(final NitfReader nitfReader,
            final NitfParseStrategy parseStrategy) throws ParseException {

        reader = nitfReader;
        segment = new NitfGraphicSegmentHeader();
        parsingStrategy = parseStrategy;

        readSY();
        readSID();
        readSNAME();
        segment.setSecurityMetadata(new NitfSecurityMetadataImpl(reader));
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
        reader.verifyHeaderMagic(SY);
    }

    private void readSID() throws ParseException {
        segment.setIdentifier(reader.readTrimmedBytes(SID_LENGTH));
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
        segment.setAttachmentLevel(reader.readBytesAsInteger(SALVL_LENGTH));
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
        String scolor = reader.readBytes(SCOLOR_LENGTH);
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
        TreCollectionImpl extendedSubheaderTREs = parsingStrategy.parseTREs(reader, graphicExtendedSubheaderLength - SXSOFL_LENGTH);
        segment.mergeTREs(extendedSubheaderTREs);
    }
}
