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
package org.codice.imaging.nitf.parser;

import java.text.ParseException;
import java.util.Arrays;

import org.codice.imaging.nitf.common.FileType;
import org.codice.imaging.nitf.common.Nitf;
import org.codice.imaging.nitf.common.reader.NitfReader;
import org.codice.imaging.nitf.security.NitfFileSecurityMetadataImpl;
import org.codice.imaging.nitf.parser.strategy.NitfParseStrategy;
import org.codice.imaging.nitf.tre.TreCollectionImpl;

/**
    Parser for a NITF file.
*/
public final class NitfFileParser extends AbstractNitfSegmentParser {
    /**
     * The expected content of the "SFH Delimiter 2" field.
     * <p>
     * See MIL-STD-2500C Table A-8(B).
     */
    static final byte[] SFH_DELIM2 = new byte[]{0x0e, (byte) 0xca, 0x14, (byte) 0xbf};

    /**
     * The expected content of the "SFH Delimiter 1" field.
     * <p>
     * See MIL-STD-2500C Table A-8(B).
     */
    static final byte[] SFH_DELIM1 = new byte[]{0x0a, (byte) 0x6e, 0x1d, (byte) 0x97};

    // NITF file lengths
    // file header
    /**
     * Length of the File Profile name field in the NITF file header.
     * <p>
     * See MIL-STD-2500C Table A-1.
     */
    private static final int FHDR_LENGTH = 4;

    /**
     * Length of the File Version field in the NITF file header.
     * <p>
     * See MIL-STD-2500C Table A-1.
     */
    private static final int FVER_LENGTH = 5;

    /**
     * Length of the Complexity Level field in the NITF file header.
     * <p>
     * See MIL-STD-2500C Table A-1.
     */
    private static final int CLEVEL_LENGTH = 2;

    /**
     * Length of the Standard Type field in the NITF file header.
     * <p>
     * See MIL-STD-2500C Table A-1.
     */
    private static final int STYPE_LENGTH = 4;

    /**
     * Length of the Originating Station field in the NITF file header.
     * <p>
     * See MIL-STD-2500C Table A-1.
     */
    private static final int OSTAID_LENGTH = 10;
    /**
     * Length of the File Title field in the NITF file header.
     * <p>
     * See MIL-STD-2500C Table A-1.
     */
    private static final int FTITLE_LENGTH = 80;

    /**
     * Length of the Originator's Name field in the NITF 2.1 file header.
     * <p>
     * See MIL-STD-2500C Table A-1.
     */
    private static final int ONAME_LENGTH = 24;

    /**
     * Length of the Originator's Name field in the NITF 2.0 file header.
     * <p>
     * See MIL-STD-2500A.
     */
    private static final int ONAME20_LENGTH = 27;

    /**
     * Length of the Originator's Phone Number field in the NITF file header.
     * <p>
     * See MIL-STD-2500C Table A-1.
     */
    private static final int OPHONE_LENGTH = 18;

    /**
     * Length of the File Length (FL) field in the NITF file header.
     * <p>
     * See MIL-STD-2500C Table A-1.
     */
    private static final int FL_LENGTH = 12;

    /**
     * Length of the NITF File Header Length (HL) field in the NITF file header.
     * <p>
     * See MIL-STD-2500C Table A-1.
     */
    private static final int HL_LENGTH = 6;

    /**
     * Length of the Number of Image Segments field in the NITF file header.
     * <p>
     * See MIL-STD-2500C Table A-1.
     */
    private static final int NUMI_LENGTH = 3;

    /**
     * Length of each of the "Length of Nth Image Subheader" fields in the NITF file header.
     * <p>
     * See MIL-STD-2500C Table A-1.
     */
    private static final int LISH_LENGTH = 6;

    /**
     * Length of each of the "Length of Nth Image Segment" fields in the NITF file header.
     * <p>
     * See MIL-STD-2500C Table A-1.
     */
    private static final int LI_LENGTH = 10;

    /**
     * Length of the Number of Graphic Segments field in the NITF file header.
     * <p>
     * See MIL-STD-2500C Table A-1.
     */
    private static final int NUMS_LENGTH = 3;

    /**
     * Length of each of the "Length of Nth Graphic Subheader" fields in the NITF file header.
     * <p>
     * See MIL-STD-2500C Table A-1.
     */
    private static final int LSSH_LENGTH = 4;

    /**
     * Length of each of the "Length of Nth Graphic Segment" fields in the NITF file header.
     * <p>
     * See MIL-STD-2500C Table A-1.
     */
    private static final int LS_LENGTH = 6;

    /**
     * Length of the "Reserved for Future Use" field in the NITF 2.1 file header.
     * <p>
     * See MIL-STD-2500C Table A-1.
     */
    private static final int NUMX_LENGTH = 3;

    /**
     * Length of the Number of Label Segments field in the NITF 2.0 file header.
     * <p>
     * See MIL-STD-2500A.
     */
    private static final int NUML20_LENGTH = 3;

    /**
     * Length of each of the "Length of Nth Label Subheader" fields in the NITF 2.0 file header.
     * <p>
     * See MIL-STD-2500A.
     */
    private static final int LLSH_LENGTH = 4;

    /**
     * Length of each of the "Length of Nth Label Segment" fields in the NITF 2.0 file header.
     * <p>
     * See MIL-STD-2500A
     */
    private static final int LL_LENGTH = 3;

    /**
     * Length of the "Number of Text Segments" field in the NITF 2.1 file header.
     * <p>
     * See MIL-STD-2500C Table A-1.
     */
    private static final int NUMT_LENGTH = 3;

    /**
     * Length of each of the "Length of Nth Text Subheader" fields in the NITF file header.
     * <p>
     * See MIL-STD-2500C Table A-1.
     */
    private static final int LTSH_LENGTH = 4;

    /**
     * Length of each of the "Length of Nth Text Segment" fields in the NITF file header.
     * <p>
     * See MIL-STD-2500C Table A-1.
     */
    private static final int LT_LENGTH = 5;

    /**
     * Length of the "Number of Data Extension Segments" field in the NITF 2.1 file header.
     * <p>
     * See MIL-STD-2500C Table A-1.
     */
    private static final int NUMDES_LENGTH = 3;

    /**
     * Length of each of the "Length of Nth Data Extension Segment Subheader" fields in the NITF file header.
     * <p>
     * See MIL-STD-2500C Table A-1.
     */
    private static final int LDSH_LENGTH = 4;

    /**
     * Length of each of the "Length of Nth Data Extension Segment" fields in the NITF file header.
     * <p>
     * See MIL-STD-2500C Table A-1.
     */
    private static final int LD_LENGTH = 9;

    /**
     * Length of the "Number of Reserved Extension Segments" field in the NITF 2.1 file header.
     * <p>
     * See MIL-STD-2500C Table A-1.
     */
    private static final int NUMRES_LENGTH = 3;

    /**
     * Length of the "User Defined Header Data Length" field in the NITF 2.1 file header.
     * <p>
     * See MIL-STD-2500C Table A-1.
     */
    private static final int UDHDL_LENGTH = 5;

    /**
     * Length of the "User Defined Header Overflow" field in the NITF 2.1 file header.
     * <p>
     * See MIL-STD-2500C Table A-1.
     */
    private static final int UDHOFL_LENGTH = 3;

    /**
     * Length of the "Extended Header Data Length" field in the NITF 2.1 file header.
     * <p>
     * See MIL-STD-2500C Table A-1.
     */
    private static final int XHDL_LENGTH = 5;

    /**
     * Length of the "Extended Header Data Overflow" field in the NITF 2.1 file header.
     * <p>
     * See MIL-STD-2500C Table A-1.
     */
    private static final int XHDLOFL_LENGTH = 3;

    /**
     * The minimum allowable complexity level value.
     */
    private static final int MIN_COMPLEXITY_LEVEL = 0;

    /**
     * The maximum allowable complexity level value.
     * <p>
     * Actual complexity levels are only defined to 9.
     */
    private static final int MAX_COMPLEXITY_LEVEL = 99;

    /**
     * The length of the "SFH Length 1" field.
     * <p>
     * See MIL-STD-2500C Table A-8(B)
     */
    private static final int SFH_L1_LENGTH = 7;

    /**
     * The length of the "SFH Delimiter 1" field.
     * <p>
     * See MIL-STD-2500C Table A-8(B)
     */
    private static final int SFH_DELIM1_LENGTH = 4;

    /**
     * The length of the "SFH Delimiter 2" field.
     * <p>
     * See MIL-STD-2500C Table A-8(B)
     */
    private static final int SFH_DELIM2_LENGTH = 4;

    /**
     * Length of the "SFH Length 2" field.
     * <p>
     * See MIL-STD-2500C Table A-8 (B).
     */
    private static final int SFH_L2_LENGTH = 7;

    /**
     * The "magic flag" value used in length fields to indicate streaming mode.
     * <p>
     * See MIL-STD-2500C Section 5.2.1 for more information.
     */
    private static final long STREAMING_FILE_MODE = 999999999999L;

    private long nitfFileLength = -1;

    private int numberImageSegments = 0;
    private int numberGraphicSegments = 0;
    private int numberTextSegments = 0;
    private int numberLabelSegments = 0;
    private int numberDataExtensionSegments = 0;
    private int numberReservedExtensionSegments = 0;

    private int userDefinedHeaderDataLength = 0;
    private int extendedHeaderDataLength = 0;

    private Nitf nitf = null;

    private NitfFileParser(final NitfReader nitfReader, final NitfParseStrategy parseStrategy) throws ParseException {
        nitf = new Nitf();
        reader = nitfReader;
        parseStrategy.setFileHeader(nitf);
        parsingStrategy = parseStrategy;
    };

    /**
     * Parse a NITF file from a specific reader and parsing strategy.
     *
     * The concept is that the parsing strategy will store the parse results.
     *
     * @param nitfReader the reader to use
     * @param parseStrategy the parsing strategy
     * @throws ParseException if an error occurs during parsing
     */
    public static void parse(final NitfReader nitfReader, final NitfParseStrategy parseStrategy) throws ParseException {
        NitfFileParser parser = new NitfFileParser(nitfReader, parseStrategy);

        parser.readBaseHeaders();
        if (parser.isStreamingMode()) {
            parser.handleStreamingMode();
        }
        parseStrategy.baseHeadersRead(nitfReader);
    }


    private void readBaseHeaders() throws ParseException {
        readFHDRFVER();
        reader.setFileType(nitf.getFileType());
        readCLEVEL();
        readSTYPE();
        readOSTAID();
        readFDT();
        readFTITLE();
        nitf.setFileSecurityMetadata(new NitfFileSecurityMetadataImpl(reader));
        readENCRYP();
        if ((reader.getFileType() == FileType.NITF_TWO_ONE) || (reader.getFileType() == FileType.NSIF_ONE_ZERO)) {
            readFBKGC();
        }
        readONAME();
        readOPHONE();
        readFL();
        readHL();
        readBaseHeaderImageParts();
        readBaseHeaderGraphicParts();
        readBaseHeaderLabelParts();
        readBaseHeaderTextParts();
        readBaseHeaderDataExtensionSegmentParts();
        readBaseHeaderReservedExtensionParts();
        readBaseHeaderUserDefinedHeaderData();
        readBaseHeaderExtendedHeader();
    }

    private void readBaseHeaderImageParts() throws ParseException {
        readNUMI();
        for (int i = 0; i < numberImageSegments; ++i) {
            readLISH(i);
            readLI(i);
        }
    }

    private void readBaseHeaderGraphicParts() throws ParseException {
        readNUMS();
        for (int i = 0; i < numberGraphicSegments; ++i) {
            readLSSH();
            readLS();
        }
    }

    private void readBaseHeaderLabelParts() throws ParseException {
        readNUMX();
        for (int i = 0; i < numberLabelSegments; ++i) {
            readLLSH();
            readLL();
        }
    }

    private void readBaseHeaderTextParts() throws ParseException {
        readNUMT();
        for (int i = 0; i < numberTextSegments; ++i) {
            readLTSH();
            readLT();
        }
    }

    private void readBaseHeaderDataExtensionSegmentParts() throws ParseException {
        readNUMDES();
        for (int i = 0; i < numberDataExtensionSegments; ++i) {
            readLDSH(i);
            readLD(i);
        }
    }

    private void readBaseHeaderReservedExtensionParts() throws ParseException {
        readNUMRES();
        for (int i = 0; i < numberReservedExtensionSegments; ++i) {
            // TODO: find a case that exercises this and implement it
            throw new UnsupportedOperationException("IMPLEMENT RES PARSING");
        }
    }

    private void readBaseHeaderUserDefinedHeaderData() throws ParseException {
        readUDHDL();
        if (userDefinedHeaderDataLength > 0) {
            readUDHOFL();
            readUDHD();
        }
    }

     private void readBaseHeaderExtendedHeader() throws ParseException {
        readXHDL();
        if (extendedHeaderDataLength > 0) {
            readXHDLOFL();
            readXHD();
        }
    }

    private boolean isStreamingMode() {
        return nitfFileLength == STREAMING_FILE_MODE;
    }

    private void handleStreamingMode() throws ParseException {
        if (reader.canSeek()) {
            readStreamingModeHeader();
        } else {
            throw new ParseException("No support for streaming mode unless input is seekable", 0);
        }
    }

    // This code will probably make more sense if you read MIL-STD-2500C Section 5.8.3.2 and then have
    // MIL-STD-2500C Table A-8(B) open.
    private void readStreamingModeHeader() throws ParseException {
        // Store the current position
        long dataSegmentsOffset = reader.getCurrentOffset();

        seekToSfhDelim2();

        verifySfhDelim2();

        long sfhL2 = reader.readBytesAsLong(SFH_L2_LENGTH);

        seekToSfhDelim1(sfhL2);

        // verify the lengths match.
        long sfhL1 = reader.readBytesAsLong(SFH_L1_LENGTH);
        if (sfhL1 != sfhL2) {
            throw new ParseException("Mismatch between SFH_L1 and SFH_L2", (int) reader.getCurrentOffset());
        }

        verifySfhDelim1();

        // Read the replacement header content
        // This assumes that the streaming mode header will contain the full "base" headers from MIL-STD-2500C Table A-1.
        readBaseHeaders();

        // Continue to read the subheaders and associated data
        reader.seekToAbsoluteOffset(dataSegmentsOffset);
    }

    private void seekToSfhDelim2() throws ParseException {
        reader.seekToEndOfFile();
        reader.seekBackwards(SFH_DELIM2_LENGTH + SFH_L2_LENGTH);
    }

    private void verifySfhDelim2() throws ParseException {
        byte[] sfhDelim2 = reader.readBytesRaw(SFH_DELIM2_LENGTH);
        if (!Arrays.equals(SFH_DELIM2, sfhDelim2)) {
            throw new ParseException("Unexpected SFH_DELIM2 value read from file", (int) reader.getCurrentOffset());
        }
    }

    private void seekToSfhDelim1(final long sfhDrLength) throws ParseException {
        reader.seekBackwards(SFH_L1_LENGTH + SFH_DELIM1_LENGTH
                + sfhDrLength + SFH_DELIM2_LENGTH + SFH_L2_LENGTH);
    }

    private void verifySfhDelim1() throws ParseException {
        byte[] sfhDelim1 = reader.readBytesRaw(SFH_DELIM1_LENGTH);
        if (!Arrays.equals(SFH_DELIM1, sfhDelim1)) {
            throw new ParseException("Unexpected SFH_DELIM1 value read from file", (int) reader.getCurrentOffset());
        }
    }

    private void readFHDRFVER() throws ParseException {
        String fhdrfver = reader.readBytes(FHDR_LENGTH + FVER_LENGTH);
        nitf.setFileType(FileType.getEnumValue(fhdrfver));
    }

    private void readCLEVEL() throws ParseException {
        nitf.setComplexityLevel(reader.readBytesAsInteger(CLEVEL_LENGTH));
        if ((nitf.getComplexityLevel() < MIN_COMPLEXITY_LEVEL) || (nitf.getComplexityLevel() > MAX_COMPLEXITY_LEVEL)) {
            throw new ParseException(String.format("CLEVEL out of range: %d", nitf.getComplexityLevel()), (int) reader.getCurrentOffset());
        }
    }

    private void readSTYPE() throws ParseException {
        nitf.setStandardType(reader.readTrimmedBytes(STYPE_LENGTH));
    }

    private void readOSTAID() throws ParseException {
        nitf.setOriginatingStationId(reader.readTrimmedBytes(OSTAID_LENGTH));
    }

    private void readFDT() throws ParseException {
        nitf.setFileDateTime(readNitfDateTime());
    }

    private void readFTITLE() throws ParseException {
        nitf.setFileTitle(reader.readTrimmedBytes(FTITLE_LENGTH));
    }

    private void readFBKGC() throws ParseException {
        nitf.setFileBackgroundColour(readRGBColour());
    }

    private void readONAME() throws ParseException {
        if ((nitf.getFileType() == FileType.NITF_TWO_ONE) || (nitf.getFileType() == FileType.NSIF_ONE_ZERO)) {
            nitf.setOriginatorsName(reader.readTrimmedBytes(ONAME_LENGTH));
        } else {
            nitf.setOriginatorsName(reader.readTrimmedBytes(ONAME20_LENGTH));
        }
    }

    private void readOPHONE() throws ParseException {
        nitf.setOriginatorsPhoneNumber(reader.readTrimmedBytes(OPHONE_LENGTH));
    }

    private void readFL() throws ParseException {
        nitfFileLength = reader.readBytesAsLong(FL_LENGTH);
    }

    private void readHL() throws ParseException {
        reader.skip(HL_LENGTH);
    }

    private void readNUMI() throws ParseException {
        numberImageSegments = reader.readBytesAsInteger(NUMI_LENGTH);
    }

    private void readLISH(final int i) throws ParseException {
        if (i < nitf.getImageSegmentSubHeaderLengths().size()) {
            nitf.getImageSegmentSubHeaderLengths().set(i, reader.readBytesAsInteger(LISH_LENGTH));
        } else {
            nitf.getImageSegmentSubHeaderLengths().add(reader.readBytesAsInteger(LISH_LENGTH));
        }
    }

    private void readLI(final int i) throws ParseException {
        if (i < nitf.getImageSegmentDataLengths().size()) {
            nitf.getImageSegmentDataLengths().set(i, reader.readBytesAsLong(LI_LENGTH));
        } else {
            nitf.getImageSegmentDataLengths().add(reader.readBytesAsLong(LI_LENGTH));
        }
    }

    // The next three methods are also used for NITF 2.0 Symbol segment lengths
    private void readNUMS() throws ParseException {
        numberGraphicSegments = reader.readBytesAsInteger(NUMS_LENGTH);
    }

    private void readLSSH() throws ParseException {
        nitf.getGraphicSegmentSubHeaderLengths().add(reader.readBytesAsInteger(LSSH_LENGTH));
    }

    private void readLS() throws ParseException {
        nitf.getGraphicSegmentDataLengths().add(reader.readBytesAsInteger(LS_LENGTH));
    }

    private void readNUMX() throws ParseException {
        if (reader.getFileType() == FileType.NITF_TWO_ZERO) {
            numberLabelSegments = reader.readBytesAsInteger(NUML20_LENGTH);
        } else {
            reader.skip(NUMX_LENGTH);
        }
    }

    private void readLLSH() throws ParseException {
        nitf.getLabelSegmentSubHeaderLengths().add(reader.readBytesAsInteger(LLSH_LENGTH));
    }

    private void readLL() throws ParseException {
        nitf.getLabelSegmentDataLengths().add(reader.readBytesAsInteger(LL_LENGTH));
    }

    private void readNUMT() throws ParseException {
       numberTextSegments = reader.readBytesAsInteger(NUMT_LENGTH);
    }

    private void readLTSH() throws ParseException {
        nitf.getTextSegmentSubHeaderLengths().add(reader.readBytesAsInteger(LTSH_LENGTH));
    }

    private void readLT() throws ParseException {
        nitf.getTextSegmentDataLengths().add(reader.readBytesAsInteger(LT_LENGTH));
    }

    private void readNUMDES() throws ParseException {
        numberDataExtensionSegments = reader.readBytesAsInteger(NUMDES_LENGTH);
    }

    private void readLDSH(final int i) throws ParseException {
        if (i < nitf.getDataExtensionSegmentDataLengths().size()) {
            nitf.getDataExtensionSegmentSubHeaderLengths().set(i, reader.readBytesAsInteger(LDSH_LENGTH));
        } else {
            nitf.getDataExtensionSegmentSubHeaderLengths().add(reader.readBytesAsInteger(LDSH_LENGTH));
        }
    }

    private void readLD(final int i) throws ParseException {
        if (i < nitf.getDataExtensionSegmentDataLengths().size()) {
            nitf.getDataExtensionSegmentDataLengths().set(i, reader.readBytesAsInteger(LD_LENGTH));
        } else {
            nitf.getDataExtensionSegmentDataLengths().add(reader.readBytesAsInteger(LD_LENGTH));
        }
    }

    private void readNUMRES() throws ParseException {
        numberReservedExtensionSegments = reader.readBytesAsInteger(NUMRES_LENGTH);
    }

    private void readUDHDL() throws ParseException {
        userDefinedHeaderDataLength = reader.readBytesAsInteger(UDHDL_LENGTH);
    }

    private void readUDHOFL() throws ParseException {
        nitf.setUserDefinedHeaderOverflow(reader.readBytesAsInteger(UDHOFL_LENGTH));
    }

    private void readUDHD() throws ParseException {
        TreCollectionImpl userDefinedHeaderTREs = parsingStrategy.parseTREs(reader, userDefinedHeaderDataLength - UDHOFL_LENGTH);
        nitf.mergeTREs(userDefinedHeaderTREs);
    }

    private void readXHDL() throws ParseException {
        extendedHeaderDataLength = reader.readBytesAsInteger(XHDL_LENGTH);
    }

    private void readXHDLOFL() throws ParseException {
        nitf.setExtendedHeaderDataOverflow(reader.readBytesAsInteger(XHDLOFL_LENGTH));
    }

    private void readXHD() throws ParseException {
        TreCollectionImpl extendedHeaderTres = parsingStrategy.parseTREs(reader, extendedHeaderDataLength - XHDLOFL_LENGTH);
        nitf.mergeTREs(extendedHeaderTres);
    }
}
