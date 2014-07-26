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
import java.util.EnumSet;

public class NitfTextSegmentParser extends AbstractNitfSegmentParser {

    private int lengthOfText = 0;
    private int textExtendedSubheaderLength = 0;

    private static final String TE = "TE";
    private static final int TEXTID_LENGTH = 7;
    private static final int TXTALVL_LENGTH = 3;
    private static final int TXTITL_LENGTH = 80;
    private static final int TXTFMT_LENGTH = 3;
    private static final int TXSHDL_LENGTH = 5;

    private boolean shouldParseTextData = false;

    private NitfTextSegment segment = null;

    public NitfTextSegmentParser(final NitfReader nitfReader,
                                 final int textLength,
                                 final EnumSet<ParseOption> parseOptions,
                                 final NitfTextSegment textSegment) throws ParseException {
        reader = nitfReader;
        lengthOfText = textLength;
        segment = textSegment;
        shouldParseTextData = parseOptions.contains(ParseOption.ExtractTextSegmentData);

        readTE();
        readTEXTID();
        readTXTALVL();
        readTEXTDT();
        readTXTITL();
        segment.setSecurityMetadata(new NitfSecurityMetadata(reader));
        reader.readENCRYP();
        readTXTFMT();
        readTXSHDL();
        if (textExtendedSubheaderLength > 0) {
            // TODO: find a case that exercises this and implement it
            throw new UnsupportedOperationException("IMPLEMENT TXSOFL / TXSHD PARSING");
        }
        readTextData();
    }

    private void readTE() throws ParseException {
       reader.verifyHeaderMagic(TE);
    }

    private void readTEXTID() throws ParseException {
        segment.setTextIdentifier(reader.readBytes(TEXTID_LENGTH));
    }

    private void readTXTALVL() throws ParseException {
        segment.setTextAttachmentLevel(reader.readBytesAsInteger(TXTALVL_LENGTH));
    }

    private void readTEXTDT() throws ParseException {
        segment.setTextDateTime(reader.readNitfDateTime());
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
