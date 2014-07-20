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

public class NitfGraphicSegment extends AbstractNitfSegment {

    private NitfReader reader = null;
    private int lengthOfGraphic = 0;

    private String graphicIdentifier = null;
    private String graphicName = null;
    private int graphicDisplayLevel = 0;
    private int graphicAttachmentLevel = 0;
    private int graphicLocationRow = 0;
    private int graphicLocationColumn = 0;
    private int boundingBox1Row = 0;
    private int boundingBox1Column = 0;
    private GraphicColour graphicColour = GraphicColour.UNKNOWN;
    private int boundingBox2Row = 0;
    private int boundingBox2Column = 0;
    private int graphicExtendedSubheaderLength = 0;
    private int graphicExtendedSubheaderOverflow = 0;

    private NitfSecurityMetadata securityMetadata = null;

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

    public NitfGraphicSegment(final NitfReader nitfReader, final int graphicLength) throws ParseException {
        reader = nitfReader;
        lengthOfGraphic = graphicLength;

        readSY();
        readSID();
        readSNAME();
        securityMetadata = new NitfSecurityMetadata(reader);
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

    public String getGraphicIdentifier() {
        return graphicIdentifier;
    }

    public String getGraphicName() {
        return graphicName;
    }

    public NitfSecurityMetadata getSecurityMetadata() {
        return securityMetadata;
    }

    public int getGraphicDisplayLevel() {
        return graphicDisplayLevel;
    }

    public int getGraphicAttachmentLevel() {
        return graphicAttachmentLevel;
    }

    public int getGraphicLocationRow() {
        return graphicLocationRow;
    }

    public int getGraphicLocationColumn() {
        return graphicLocationColumn;
    }

    public int getBoundingBox1Row() {
        return boundingBox1Row;
    }

    public int getBoundingBox1Column() {
        return boundingBox1Column;
    }

    public GraphicColour getGraphicColour() {
        return graphicColour;
    }

    public int getBoundingBox2Row() {
        return boundingBox2Row;
    }

    public int getBoundingBox2Column() {
        return boundingBox2Column;
    }

    public int getGraphicExtendedSubheaderLength() {
        return graphicExtendedSubheaderLength;
    }

    private void readSY() throws ParseException {
        reader.verifyHeaderMagic(SY);
    }

    private void readSID() throws ParseException {
        graphicIdentifier = reader.readTrimmedBytes(SID_LENGTH);
    }

    private void readSNAME() throws ParseException {
        graphicName = reader.readTrimmedBytes(SNAME_LENGTH);
    }

    private void readSFMT() throws ParseException {
        reader.verifyHeaderMagic(SFMT_CGM);
    }

    private void readSSTRUCT() throws ParseException {
        reader.verifyHeaderMagic(SSTRUCT);
    }

    private void readSDLVL() throws ParseException {
        graphicDisplayLevel = reader.readBytesAsInteger(SDLVL_LENGTH);
    }

    private void readSALVL() throws ParseException {
        graphicAttachmentLevel = reader.readBytesAsInteger(SALVL_LENGTH);
    }

    private void readSLOC() throws ParseException {
        graphicLocationRow = reader.readBytesAsInteger(SLOC_HALF_LENGTH);
        graphicLocationColumn = reader.readBytesAsInteger(SLOC_HALF_LENGTH);
    }

    private void readSBND1() throws ParseException {
        boundingBox1Row = reader.readBytesAsInteger(SBND1_HALF_LENGTH);
        boundingBox1Column = reader.readBytesAsInteger(SBND1_HALF_LENGTH);
    }

    private void readSCOLOR() throws ParseException {
        String scolor = reader.readTrimmedBytes(SCOLOR_LENGTH);
        graphicColour = GraphicColour.getEnumValue(scolor);
    }

    private void readSBND2() throws ParseException {
        boundingBox2Row = reader.readBytesAsInteger(SBND2_HALF_LENGTH);
        boundingBox2Column = reader.readBytesAsInteger(SBND2_HALF_LENGTH);
    }

    private void readSRES() throws ParseException {
        reader.verifyHeaderMagic(SRES);
    }

    private void readSXSHDL() throws ParseException {
        graphicExtendedSubheaderLength = reader.readBytesAsInteger(SXSHDL_LENGTH);
    }

    private void readSXSOFL() throws ParseException {
        graphicExtendedSubheaderOverflow = reader.readBytesAsInteger(SXSOFL_LENGTH);
    }

    private void readSXSHD() throws ParseException {
        TreParser treParser = new TreParser();
        TreCollection extendedSubheaderTREs = treParser.parse(reader, graphicExtendedSubheaderLength - SXSOFL_LENGTH);
        mergeTREs(extendedSubheaderTREs);
    }

    private void readGraphicData() throws ParseException {
        // TODO: we could use this if needed later
        reader.skip(lengthOfGraphic);
    }
}
