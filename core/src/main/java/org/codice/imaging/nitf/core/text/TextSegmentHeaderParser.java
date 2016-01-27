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
package org.codice.imaging.nitf.core.text;

import java.text.ParseException;
import org.codice.imaging.nitf.core.common.AbstractNitfSegmentParser;
import org.codice.imaging.nitf.core.common.FileType;
import org.codice.imaging.nitf.core.common.NitfParseStrategy;
import org.codice.imaging.nitf.core.common.NitfReader;
import org.codice.imaging.nitf.core.security.SecurityMetadataParser;
import static org.codice.imaging.nitf.core.text.TextConstants.TE;
import static org.codice.imaging.nitf.core.text.TextConstants.TEXTID20_LENGTH;
import static org.codice.imaging.nitf.core.text.TextConstants.TEXTID_LENGTH;
import static org.codice.imaging.nitf.core.text.TextConstants.TXSHDL_LENGTH;
import static org.codice.imaging.nitf.core.text.TextConstants.TXSOFL_LENGTH;
import static org.codice.imaging.nitf.core.text.TextConstants.TXTALVL_LENGTH;
import static org.codice.imaging.nitf.core.text.TextConstants.TXTFMT_LENGTH;
import static org.codice.imaging.nitf.core.text.TextConstants.TXTITL_LENGTH;
import org.codice.imaging.nitf.core.tre.TreCollection;

/**
    Parser for a text segment subheader in a NITF file.
*/
public class TextSegmentHeaderParser extends AbstractNitfSegmentParser {

    private int textExtendedSubheaderLength = 0;

    private TextSegmentHeaderImpl segment = null;

    /**
     * default constructor.
     */
    public TextSegmentHeaderParser() {
    }

    /**
     * Parses the TextSegmentHeader from the give NitfReader.
     *
     * @param nitfReader The NitfReader to read TextSegmentHeader from.
     * @param parseStrategy the parsing strategy to use to process the data.
     * @return the parsed TextSegmentHeader.
     * @throws ParseException when the input from the NitfReader isn't what was expected.
     */
    public final TextSegmentHeader parse(final NitfReader nitfReader, final NitfParseStrategy parseStrategy)
            throws ParseException {
        reader = nitfReader;
        segment = new TextSegmentHeaderImpl();
        parsingStrategy = parseStrategy;

        readTE();
        readTEXTID();
        readTXTALVL();
        readTEXTDT();
        readTXTITL();
        segment.setSecurityMetadata(new SecurityMetadataParser().parseSecurityMetadata(reader));
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
        TreCollection extendedSubheaderTREs = parsingStrategy.parseTREs(reader, textExtendedSubheaderLength - TXSOFL_LENGTH);
        segment.mergeTREs(extendedSubheaderTREs);
    }
}
