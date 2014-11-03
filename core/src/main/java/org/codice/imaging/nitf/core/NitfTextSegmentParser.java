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
    Parser for a text segment subheader in a NITF file.
*/
class NitfTextSegmentParser extends AbstractNitfSegmentParser {

    private int lengthOfText = 0;
    private int textExtendedSubheaderLength = 0;

    private boolean shouldParseTextData = false;

    private NitfTextSegment segment = null;

    // TODO: remove this
    NitfTextSegmentParser(final NitfReader nitfReader,
                                 final int textLength,
                                 final Set<ParseOption> parseOptions,
                                 final NitfTextSegment textSegment) throws ParseException {
        reader = nitfReader;
        lengthOfText = textLength;
        segment = textSegment;
        shouldParseTextData = parseOptions.contains(ParseOption.EXTRACT_TEXT_SEGMENT_DATA);

        readTE();
        readTEXTID();
        readTXTALVL();
        readTEXTDT();
        readTXTITL();
        segment.setSecurityMetadata(new NitfSecurityMetadata(reader));
        readENCRYP();
        readTXTFMT();
        readTXSHDL();
        if (textExtendedSubheaderLength > 0) {
            readTXSOFL();
            readTXSHD();
        }
        readTextData();
    }

    // TODO: make this a method
    NitfTextSegmentParser(final NitfReader nitfReader,
            final NitfTextSegment textSegment) throws ParseException {
        reader = nitfReader;
        segment = textSegment;

        readTE();
        readTEXTID();
        readTXTALVL();
        readTEXTDT();
        readTXTITL();
        segment.setSecurityMetadata(new NitfSecurityMetadata(reader));
        readENCRYP();
        readTXTFMT();
        readTXSHDL();
        if (textExtendedSubheaderLength > 0) {
            readTXSOFL();
            readTXSHD();
        }
    }

    private void readTE() throws ParseException {
       reader.verifyHeaderMagic(NitfConstants.TE);
    }

    private void readTEXTID() throws ParseException {
        switch (reader.getFileType()) {
            case NITF_TWO_ZERO:
                segment.setIdentifier(reader.readBytes(NitfConstants.TEXTID20_LENGTH));
                break;
            case NITF_TWO_ONE:
            case NSIF_ONE_ZERO:
                segment.setIdentifier(reader.readBytes(NitfConstants.TEXTID_LENGTH));
                break;
            case UNKNOWN:
            default:
                throw new ParseException("Unsupported reader version", (int) reader.getCurrentOffset());
        }
    }

    private void readTXTALVL() throws ParseException {
        if ((reader.getFileType() == FileType.NITF_TWO_ONE) || (reader.getFileType() == FileType.NSIF_ONE_ZERO)) {
            segment.setAttachmentLevel(reader.readBytesAsInteger(NitfConstants.TXTALVL_LENGTH));
        }
    }

    private void readTEXTDT() throws ParseException {
        segment.setTextDateTime(readNitfDateTime());
    }

    private void readTXTITL() throws ParseException {
        segment.setTextTitle(reader.readTrimmedBytes(NitfConstants.TXTITL_LENGTH));
    }

    private void readTXTFMT() throws ParseException {
        String txtfmt = reader.readTrimmedBytes(NitfConstants.TXTFMT_LENGTH);
        segment.setTextFormat(TextFormat.getEnumValue(txtfmt));
    }

    private void readTXSHDL() throws ParseException {
        textExtendedSubheaderLength = reader.readBytesAsInteger(NitfConstants.TXSHDL_LENGTH);
    }

    private void readTXSOFL() throws ParseException {
        segment.setExtendedHeaderDataOverflow(reader.readBytesAsInteger(NitfConstants.TXSOFL_LENGTH));
    }

    private void readTXSHD() throws ParseException {
        TreCollectionParser treCollectionParser = new TreCollectionParser();
        TreCollection extendedSubheaderTREs = treCollectionParser.parse(reader, textExtendedSubheaderLength - NitfConstants.TXSOFL_LENGTH);
        segment.mergeTREs(extendedSubheaderTREs);
    }

    private void readTextData() throws ParseException {
        if (lengthOfText == 0) {
            return;
        }
        if (shouldParseTextData) {
            segment.setTextData(reader.readBytes(lengthOfText));
        } else {
            reader.skip(lengthOfText);
        }
    }
}
