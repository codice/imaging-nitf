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

import org.codice.imaging.nitf.core.common.AbstractSegmentParser;
import org.codice.imaging.nitf.core.common.FileType;
import org.codice.imaging.nitf.core.common.NitfFormatException;
import org.codice.imaging.nitf.core.common.ParseStrategy;
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
import org.codice.imaging.nitf.core.tre.TreSource;

/**
    Parser for a text segment in a NITF file.
*/
public class TextSegmentParser extends AbstractSegmentParser {

    private int textExtendedSubheaderLength = 0;

    private TextSegmentImpl segment = null;

    /**
     * default constructor.
     */
    public TextSegmentParser() {
    }

    /**
     * Parses the TextSegment from the give NitfReader.
     *
     * @param nitfReader The NitfReader to read TextSegment from.
     * @param parseStrategy the parsing strategy to use to process the data.
     * @return the parsed TextSegment.
     * @throws NitfFormatException when the input from the NitfReader isn't what was expected.
     */
    public final TextSegment parse(final NitfReader nitfReader, final ParseStrategy parseStrategy)
            throws NitfFormatException {
        reader = nitfReader;
        segment = new TextSegmentImpl();
        parsingStrategy = parseStrategy;
        segment.setFileType(nitfReader.getFileType());

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

    private void readTE() throws NitfFormatException {
       reader.verifyHeaderMagic(TE);
    }

    private void readTEXTID() throws NitfFormatException {
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
                throw new NitfFormatException("Unsupported reader version", (int) reader.getCurrentOffset());
        }
    }

    private void readTXTALVL() throws NitfFormatException {
        if ((reader.getFileType() == FileType.NITF_TWO_ONE) || (reader.getFileType() == FileType.NSIF_ONE_ZERO)) {
            segment.setAttachmentLevel(reader.readBytesAsInteger(TXTALVL_LENGTH));
        }
    }

    private void readTEXTDT() throws NitfFormatException {
        segment.setTextDateTime(readNitfDateTime());
    }

    private void readTXTITL() throws NitfFormatException {
        segment.setTextTitle(reader.readTrimmedBytes(TXTITL_LENGTH));
    }

    private void readTXTFMT() throws NitfFormatException {
        String txtfmt = reader.readTrimmedBytes(TXTFMT_LENGTH);
        segment.setTextFormat(TextFormat.getEnumValue(txtfmt));
    }

    private void readTXSHDL() throws NitfFormatException {
        textExtendedSubheaderLength = reader.readBytesAsInteger(TXSHDL_LENGTH);
    }

    private void readTXSOFL() throws NitfFormatException {
        segment.setExtendedHeaderDataOverflow(reader.readBytesAsInteger(TXSOFL_LENGTH));
    }

    private void readTXSHD() throws NitfFormatException {
        TreCollection extendedSubheaderTREs = parsingStrategy.parseTREs(reader,
                textExtendedSubheaderLength - TXSOFL_LENGTH,
                TreSource.TextExtendedSubheaderData);
        segment.mergeTREs(extendedSubheaderTREs);
    }
}
