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
package org.codice.imaging.nitf.text;

import java.text.ParseException;

import org.codice.imaging.nitf.parser.AbstractNitfSegmentParser;
import org.codice.imaging.nitf.common.FileType;
import org.codice.imaging.nitf.parser.strategy.NitfParseStrategy;
import org.codice.imaging.nitf.common.reader.NitfReader;
import org.codice.imaging.nitf.security.NitfSecurityMetadataImpl;
import org.codice.imaging.nitf.tre.TreCollectionImpl;

/**
    Parser for a text segment subheader in a NITF file.
*/
public class NitfTextSegmentHeaderParser extends AbstractNitfSegmentParser {
    // text segment
    /**
     * Marker string for NITF Text subheader.
     * <p>
     * See MIL-STD-2500C Table A-6.
     */
    private static final String TE = "TE";

    /**
     * Length of the "Text Identifier" field in the NITF 2.1 Text Subheader.
     * <p>
     * See MIL-STD-2500C Table A-6.
     */
    private static final int TEXTID_LENGTH = 7;

    /**
     * Length of the "Text Attachment Level" field in the NITF Text Subheader.
     * <p>
     * See MIL-STD-2500C Table A-6.
     */
    private static final int TXTALVL_LENGTH = 3;

    /**
     * Length of the "Text Identifier" field in the NITF 2.0 Text Subheader.
     * <p>
     * See MIL-STD-2500A.
     */
    private static final int TEXTID20_LENGTH = 10;

    /**
     * Length of the "Text Title" field in the NITF Text Subheader.
     * <p>
     * See MIL-STD-2500C Table A-6.
     */
    private static final int TXTITL_LENGTH = 80;

    /**
     * Length of the "Text Format" field in the NITF Text Subheader.
     * <p>
     * See MIL-STD-2500C Table A-6.
     */
    private static final int TXTFMT_LENGTH = 3;

    /**
     * Length of the "Text Extended Subheader Data Length" field in the NITF Text Subheader.
     * <p>
     * See MIL-STD-2500C Table A-6.
     */
    private static final int TXSHDL_LENGTH = 5;

    /**
     * Length of the "Text Extended Subheader Overflow" field in the NITF Text Subheader.
     * <p>
     * See MIL-STD-2500C Table A-6.
     */
    private static final int TXSOFL_LENGTH = 3;

    private int textExtendedSubheaderLength = 0;

    private NitfTextSegmentHeader segment = null;

    /**
     *
     * @param nitfReader - the NitfReader that streams the input
     * @param parseStrategy - the strategy which defines which portions of the input to parse or skip.
     * @return the NitfTextSegmentHeader parsed from 'nitfReader' according to the rules in 'parseStrategy'.
     * @throws ParseException - when an unexpected value is encountered in the input stream.
     */
    public final NitfTextSegmentHeader parse(final NitfReader nitfReader,
            final NitfParseStrategy parseStrategy) throws ParseException {
        reader = nitfReader;
        segment = new NitfTextSegmentHeader();
        parsingStrategy = parseStrategy;

        readTE();
        readTEXTID();
        readTXTALVL();
        readTEXTDT();
        readTXTITL();
        segment.setSecurityMetadata(new NitfSecurityMetadataImpl(reader));
        readENCRYP();
        readTXTFMT();
        readTXSHDL();
        if (textExtendedSubheaderLength > 0) {
            readTXSOFL();
            readTXSHD();
        }
        return segment;
    }

    private void readTE() throws ParseException {
       reader.verifyHeaderMagic(TE);
    }

    private void readTEXTID() throws ParseException {
        switch (reader.getFileType()) {
            case NITF_TWO_ZERO:
                segment.setIdentifier(reader.readBytes(TEXTID20_LENGTH));
                break;
            case NITF_TWO_ONE:
            case NSIF_ONE_ZERO:
                segment.setIdentifier(reader.readBytes(TEXTID_LENGTH));
                break;
            case UNKNOWN:
            default:
                throw new ParseException("Unsupported reader version", (int) reader.getCurrentOffset());
        }
    }

    private void readTXTALVL() throws ParseException {
        if ((reader.getFileType() == FileType.NITF_TWO_ONE) || (reader.getFileType() == FileType.NSIF_ONE_ZERO)) {
            segment.setAttachmentLevel(reader.readBytesAsInteger(TXTALVL_LENGTH));
        }
    }

    private void readTEXTDT() throws ParseException {
        segment.setTextDateTime(readNitfDateTime());
    }

    private void readTXTITL() throws ParseException {
        segment.setTextTitle(reader.readTrimmedBytes(TXTITL_LENGTH));
    }

    private void readTXTFMT() throws ParseException {
        String txtfmt = reader.readTrimmedBytes(TXTFMT_LENGTH);
        segment.setTextFormat(TextFormat.getEnumValue(txtfmt));
    }

    private void readTXSHDL() throws ParseException {
        textExtendedSubheaderLength = reader.readBytesAsInteger(TXSHDL_LENGTH);
    }

    private void readTXSOFL() throws ParseException {
        segment.setExtendedHeaderDataOverflow(reader.readBytesAsInteger(TXSOFL_LENGTH));
    }

    private void readTXSHD() throws ParseException {
        TreCollectionImpl extendedSubheaderTREs = parsingStrategy.parseTREs(reader, textExtendedSubheaderLength - TXSOFL_LENGTH);
        segment.mergeTREs(extendedSubheaderTREs);
    }
}
