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
package org.codice.imaging.nitf.core.header;

import java.nio.charset.Charset;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.codice.imaging.nitf.core.RGBColour;
import org.codice.imaging.nitf.core.common.AbstractSegmentParser;
import org.codice.imaging.nitf.core.common.FileType;
import org.codice.imaging.nitf.core.common.NitfParseStrategy;
import org.codice.imaging.nitf.core.common.NitfReader;
import static org.codice.imaging.nitf.core.header.NitfHeaderConstants.UDHOFL_LENGTH;
import static org.codice.imaging.nitf.core.header.NitfHeaderConstants.XHDLOFL_LENGTH;
import org.codice.imaging.nitf.core.security.FileSecurityMetadataParser;
import org.codice.imaging.nitf.core.tre.TreCollection;
import org.codice.imaging.nitf.core.tre.TreSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
    Parser for a NITF file.
*/
public final class NitfFileParser extends AbstractSegmentParser {

    private static final Logger LOGGER = LoggerFactory.getLogger(NitfFileParser.class);

    private long nitfFileLength = -1;

    private int numberImageSegments = 0;
    private int numberGraphicSegments = 0;
    private int numberTextSegments = 0;
    private int numberLabelSegments = 0;
    private int numberDataExtensionSegments = 0;
    private int numberReservedExtensionSegments = 0;

    private int userDefinedHeaderDataLength = 0;
    private int extendedHeaderDataLength = 0;

    private NitfHeaderImpl nitfFileHeader = null;
    private final List<Integer> lish = new ArrayList<>();
    private final List<Long> li = new ArrayList<>();
    private final List<Integer> lssh = new ArrayList<>();
    private final List<Integer> ls = new ArrayList<>();
    private final List<Integer> llsh = new ArrayList<>();
    private final List<Integer> ll = new ArrayList<>();
    private final List<Integer> ltsh = new ArrayList<>();
    private final List<Integer> lt = new ArrayList<>();
    private final List<Integer> ldsh = new ArrayList<>();
    private final List<Long> ld = new ArrayList<>();

    private NitfFileParser(final NitfReader nitfReader, final NitfParseStrategy parseStrategy) throws ParseException {
        nitfFileHeader = new NitfHeaderImpl();
        reader = nitfReader;
        parseStrategy.setFileHeader(nitfFileHeader);
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

        NitfHeader nitfHeader = parser.nitfFileHeader;

        try {
            for (Long dataLength : parser.li) {
                parseStrategy.handleImageSegment(nitfReader, dataLength);
            }
            if (nitfHeader.getFileType() == FileType.NITF_TWO_ZERO) {
                for (Integer dataLength : parser.ls) {
                    parseStrategy.handleSymbolSegment(nitfReader, dataLength);
                }
                for (Integer dataLength : parser.ll) {
                    parseStrategy.handleLabelSegment(nitfReader, dataLength);
                }
            } else {
                for (Integer dataLength : parser.ls) {
                    parseStrategy.handleGraphicSegment(nitfReader, dataLength);
                }
            }
            for (Integer dataLength : parser.lt) {
                parseStrategy.handleTextSegment(nitfReader, dataLength);
            }
            for (Long dataLength : parser.ld) {
                parseStrategy.handleDataExtensionSegment(nitfReader, dataLength);
            }
        } catch (ParseException ex) {
            LOGGER.error(ex.getMessage() + ex);
        }
    }


    private void readBaseHeaders() throws ParseException {
        readFHDRFVER();
        reader.setFileType(nitfFileHeader.getFileType());
        readCLEVEL();
        readSTYPE();
        readOSTAID();
        readFDT();
        readFTITLE();
        nitfFileHeader.setFileSecurityMetadata(new FileSecurityMetadataParser().parseFileSecurityMetadata(reader));
        readENCRYP();
        readFBKGCandONAME();
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
        return nitfFileLength == NitfHeaderConstants.STREAMING_FILE_MODE;
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

        long sfhL2 = reader.readBytesAsLong(NitfHeaderConstants.SFH_L2_LENGTH);

        seekToSfhDelim1(sfhL2);

        // verify the lengths match.
        long sfhL1 = reader.readBytesAsLong(NitfHeaderConstants.SFH_L1_LENGTH);
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
        reader.seekBackwards(NitfHeaderConstants.SFH_DELIM2_LENGTH + NitfHeaderConstants.SFH_L2_LENGTH);
    }

    private void verifySfhDelim2() throws ParseException {
        byte[] sfhDelim2 = reader.readBytesRaw(NitfHeaderConstants.SFH_DELIM2_LENGTH);
        if (!Arrays.equals(NitfHeaderConstants.SFH_DELIM2, sfhDelim2)) {
            throw new ParseException("Unexpected SFH_DELIM2 value read from file", (int) reader.getCurrentOffset());
        }
    }

    private void seekToSfhDelim1(final long sfhDrLength) throws ParseException {
        reader.seekBackwards(NitfHeaderConstants.SFH_L1_LENGTH + NitfHeaderConstants.SFH_DELIM1_LENGTH
                + sfhDrLength + NitfHeaderConstants.SFH_DELIM2_LENGTH + NitfHeaderConstants.SFH_L2_LENGTH);
    }

    private void verifySfhDelim1() throws ParseException {
        byte[] sfhDelim1 = reader.readBytesRaw(NitfHeaderConstants.SFH_DELIM1_LENGTH);
        if (!Arrays.equals(NitfHeaderConstants.SFH_DELIM1, sfhDelim1)) {
            throw new ParseException("Unexpected SFH_DELIM1 value read from file", (int) reader.getCurrentOffset());
        }
    }

    private void readFHDRFVER() throws ParseException {
        String fhdrfver = reader.readBytes(NitfHeaderConstants.FHDR_LENGTH + NitfHeaderConstants.FVER_LENGTH);
        nitfFileHeader.setFileType(FileType.getEnumValue(fhdrfver));
    }

    private void readCLEVEL() throws ParseException {
        nitfFileHeader.setComplexityLevel(reader.readBytesAsInteger(NitfHeaderConstants.CLEVEL_LENGTH));
        if ((nitfFileHeader.getComplexityLevel() < NitfHeaderConstants.MIN_COMPLEXITY_LEVEL)
                || (nitfFileHeader.getComplexityLevel() > NitfHeaderConstants.MAX_COMPLEXITY_LEVEL)) {
            throw new ParseException(String.format("CLEVEL out of range: %d", nitfFileHeader.getComplexityLevel()), (int) reader.getCurrentOffset());
        }
    }

    private void readSTYPE() throws ParseException {
        nitfFileHeader.setStandardType(reader.readTrimmedBytes(NitfHeaderConstants.STYPE_LENGTH));
    }

    private void readOSTAID() throws ParseException {
        nitfFileHeader.setOriginatingStationId(reader.readTrimmedBytes(NitfHeaderConstants.OSTAID_LENGTH));
    }

    private void readFDT() throws ParseException {
        nitfFileHeader.setFileDateTime(readNitfDateTime());
    }

    private void readFTITLE() throws ParseException {
        nitfFileHeader.setFileTitle(reader.readTrimmedBytes(NitfHeaderConstants.FTITLE_LENGTH));
    }

    private void readFBKGCandONAME() throws ParseException {
        byte[] rgb = reader.readBytesRaw(RGBColour.RGB_COLOUR_LENGTH);
        // See IPON 2.0 Section 3.6.2 for recommendation to do this in NITF 2.0.
        nitfFileHeader.setFileBackgroundColour(new RGBColour(rgb));
        if ((nitfFileHeader.getFileType() == FileType.NITF_TWO_ONE) || (nitfFileHeader.getFileType() == FileType.NSIF_ONE_ZERO)) {
            nitfFileHeader.setOriginatorsName(reader.readTrimmedBytes(NitfHeaderConstants.ONAME_LENGTH));
        } else if (bytesAreOutsideBCSrange(rgb)) {
            // High probability NITF 2.0 file employs FBKGC
            nitfFileHeader.setOriginatorsName(reader.readTrimmedBytes(NitfHeaderConstants.ONAME_LENGTH));
        } else if (bytesAreIdentical(rgb)) {
            // High probability NITF 2.0 file employs FBKGC
            nitfFileHeader.setOriginatorsName(reader.readTrimmedBytes(NitfHeaderConstants.ONAME_LENGTH));
        } else {
            // low probability of NITF 2.0 using FBKGC
            nitfFileHeader.setOriginatorsName(new String(rgb, Charset.forName("UTF-8")) + reader.readTrimmedBytes(NitfHeaderConstants.ONAME_LENGTH));
        }
    }

    private boolean bytesAreOutsideBCSrange(final byte[] rgb) {
        for (short thisByte : rgb) {
            if ((thisByte > '~') || (thisByte < ' ')) {
                return true;
            }
        }
        return false;
    }

    private boolean bytesAreIdentical(final byte[] rgb) {
        return ((rgb[0] == rgb[1]) && (rgb[1] == rgb[2]));
    }

    private void readOPHONE() throws ParseException {
        nitfFileHeader.setOriginatorsPhoneNumber(reader.readTrimmedBytes(NitfHeaderConstants.OPHONE_LENGTH));
    }

    private void readFL() throws ParseException {
        nitfFileLength = reader.readBytesAsLong(NitfHeaderConstants.FL_LENGTH);
    }

    private void readHL() throws ParseException {
        reader.skip(NitfHeaderConstants.HL_LENGTH);
    }

    private void readNUMI() throws ParseException {
        numberImageSegments = reader.readBytesAsInteger(NitfHeaderConstants.NUMI_LENGTH);
    }

    private void readLISH(final int i) throws ParseException {
        if (i < lish.size()) {
            lish.set(i, reader.readBytesAsInteger(NitfHeaderConstants.LISH_LENGTH));
        } else {
            lish.add(reader.readBytesAsInteger(NitfHeaderConstants.LISH_LENGTH));
        }
    }

    private void readLI(final int i) throws ParseException {
        if (i < li.size()) {
            li.set(i, reader.readBytesAsLong(NitfHeaderConstants.LI_LENGTH));
        } else {
            li.add(reader.readBytesAsLong(NitfHeaderConstants.LI_LENGTH));
        }
    }

    // The next three methods are also used for NITF 2.0 Symbol segment lengths
    private void readNUMS() throws ParseException {
        numberGraphicSegments = reader.readBytesAsInteger(NitfHeaderConstants.NUMS_LENGTH);
    }

    private void readLSSH() throws ParseException {
        lssh.add(reader.readBytesAsInteger(NitfHeaderConstants.LSSH_LENGTH));
    }

    private void readLS() throws ParseException {
        ls.add(reader.readBytesAsInteger(NitfHeaderConstants.LS_LENGTH));
    }

    private void readNUMX() throws ParseException {
        if (reader.getFileType() == FileType.NITF_TWO_ZERO) {
            numberLabelSegments = reader.readBytesAsInteger(NitfHeaderConstants.NUML20_LENGTH);
        } else {
            reader.skip(NitfHeaderConstants.NUMX_LENGTH);
        }
    }

    private void readLLSH() throws ParseException {
        llsh.add(reader.readBytesAsInteger(NitfHeaderConstants.LLSH_LENGTH));
    }

    private void readLL() throws ParseException {
        ll.add(reader.readBytesAsInteger(NitfHeaderConstants.LL_LENGTH));
    }

    private void readNUMT() throws ParseException {
        numberTextSegments = reader.readBytesAsInteger(NitfHeaderConstants.NUMT_LENGTH);
    }

    private void readLTSH() throws ParseException {
        ltsh.add(reader.readBytesAsInteger(NitfHeaderConstants.LTSH_LENGTH));
    }

    private void readLT() throws ParseException {
        lt.add(reader.readBytesAsInteger(NitfHeaderConstants.LT_LENGTH));
    }

    private void readNUMDES() throws ParseException {
        numberDataExtensionSegments = reader.readBytesAsInteger(NitfHeaderConstants.NUMDES_LENGTH);
    }

    private void readLDSH(final int i) throws ParseException {
        if (i < ldsh.size()) {
            ldsh.set(i, reader.readBytesAsInteger(NitfHeaderConstants.LDSH_LENGTH));
        } else {
            ldsh.add(reader.readBytesAsInteger(NitfHeaderConstants.LDSH_LENGTH));
        }
    }

    private void readLD(final int i) throws ParseException {
        if (i < ld.size()) {
            ld.set(i, reader.readBytesAsLong(NitfHeaderConstants.LD_LENGTH));
        } else {
            ld.add(reader.readBytesAsLong(NitfHeaderConstants.LD_LENGTH));
        }
    }

    private void readNUMRES() throws ParseException {
        numberReservedExtensionSegments = reader.readBytesAsInteger(NitfHeaderConstants.NUMRES_LENGTH);
    }

    private void readUDHDL() throws ParseException {
        userDefinedHeaderDataLength = reader.readBytesAsInteger(NitfHeaderConstants.UDHDL_LENGTH);
    }

    private void readUDHOFL() throws ParseException {
        nitfFileHeader.setUserDefinedHeaderOverflow(reader.readBytesAsInteger(NitfHeaderConstants.UDHOFL_LENGTH));
    }

    private void readUDHD() throws ParseException {
        TreCollection userDefinedHeaderTREs = parsingStrategy.parseTREs(reader,
                userDefinedHeaderDataLength - UDHOFL_LENGTH,
                TreSource.UserDefinedHeaderData);
        nitfFileHeader.mergeTREs(userDefinedHeaderTREs);
    }

    private void readXHDL() throws ParseException {
        extendedHeaderDataLength = reader.readBytesAsInteger(NitfHeaderConstants.XHDL_LENGTH);
    }

    private void readXHDLOFL() throws ParseException {
        nitfFileHeader.setExtendedHeaderDataOverflow(reader.readBytesAsInteger(NitfHeaderConstants.XHDLOFL_LENGTH));
    }

    private void readXHD() throws ParseException {
        TreCollection extendedHeaderTres = parsingStrategy.parseTREs(reader,
                extendedHeaderDataLength - XHDLOFL_LENGTH,
                TreSource.ExtendedHeaderData);
        nitfFileHeader.mergeTREs(extendedHeaderTres);
    }
}
