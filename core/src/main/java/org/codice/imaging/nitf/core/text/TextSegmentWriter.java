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

import java.io.DataOutput;
import java.io.IOException;
import org.codice.imaging.nitf.core.common.AbstractSegmentWriter;
import org.codice.imaging.nitf.core.common.FileType;
import org.codice.imaging.nitf.core.common.NitfFormatException;
import static org.codice.imaging.nitf.core.text.TextConstants.TEXTID20_LENGTH;
import static org.codice.imaging.nitf.core.text.TextConstants.TEXTID_LENGTH;
import static org.codice.imaging.nitf.core.text.TextConstants.TXSHDL_LENGTH;
import static org.codice.imaging.nitf.core.text.TextConstants.TXSOFL_LENGTH;
import static org.codice.imaging.nitf.core.text.TextConstants.TXTALVL_LENGTH;
import static org.codice.imaging.nitf.core.text.TextConstants.TXTFMT_LENGTH;
import static org.codice.imaging.nitf.core.text.TextConstants.TXTITL_LENGTH;
import org.codice.imaging.nitf.core.tre.TreParser;
import org.codice.imaging.nitf.core.tre.TreSource;

/**
 * Writer for text segments.
 */
public class TextSegmentWriter extends AbstractSegmentWriter {

    /**
     * Constructor.
     *
     * @param output the target to write the text segment to.
     * @param treParser TreParser to use to serialise out the TREs.
     */
    public TextSegmentWriter(final DataOutput output, final TreParser treParser) {
        super(output, treParser);
    }

    /**
     * Write out the specified text segment.
     *
     * @param textSegment the content to write out
     * @param fileType the type of file (NITF version) to write the text header out for.
     * @throws IOException on write failure.
     * @throws NitfFormatException on TRE parsing failure.
     */
    public final void writeTextSegment(final TextSegment textSegment, final FileType fileType) throws IOException, NitfFormatException {
        writeFixedLengthString(TextConstants.TE, TextConstants.TE.length());
        if (fileType == FileType.NITF_TWO_ZERO) {
            writeFixedLengthString(textSegment.getIdentifier(), TEXTID20_LENGTH);
        } else {
            writeFixedLengthString(textSegment.getIdentifier(), TEXTID_LENGTH);
            writeFixedLengthNumber(textSegment.getAttachmentLevel(), TXTALVL_LENGTH);
        }
        writeDateTime(textSegment.getTextDateTime());
        writeFixedLengthString(textSegment.getTextTitle(), TXTITL_LENGTH);
        writeSecurityMetadata(textSegment.getSecurityMetadata());
        writeENCRYP();
        writeFixedLengthString(textSegment.getTextFormat().getTextEquivalent(), TXTFMT_LENGTH);
        byte[] textExtendedSubheaderData = mTreParser.getTREs(textSegment, TreSource.TextExtendedSubheaderData);
        int textExtendedSubheaderDataLength = textExtendedSubheaderData.length;
        if ((textExtendedSubheaderDataLength > 0) || (textSegment.getExtendedHeaderDataOverflow() != 0)) {
            textExtendedSubheaderDataLength += TXSOFL_LENGTH;
        }
        writeFixedLengthNumber(textExtendedSubheaderDataLength, TXSHDL_LENGTH);
        if (textExtendedSubheaderDataLength > 0) {
            writeFixedLengthNumber(textSegment.getExtendedHeaderDataOverflow(), TXSOFL_LENGTH);
            writeBytes(textExtendedSubheaderData, textExtendedSubheaderDataLength - TXSOFL_LENGTH);
        }
        mOutput.writeBytes(textSegment.getData());
    }
}
