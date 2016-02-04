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
package org.codice.imaging.nitf.core.label;

import java.io.DataOutput;
import java.io.IOException;
import java.text.ParseException;
import org.codice.imaging.nitf.core.common.AbstractSegmentWriter;
import org.codice.imaging.nitf.core.common.FileType;
import static org.codice.imaging.nitf.core.label.LabelConstants.LA;
import static org.codice.imaging.nitf.core.label.LabelConstants.LALVL_LENGTH;
import static org.codice.imaging.nitf.core.label.LabelConstants.LCH_LENGTH;
import static org.codice.imaging.nitf.core.label.LabelConstants.LCW_LENGTH;
import static org.codice.imaging.nitf.core.label.LabelConstants.LDLVL_LENGTH;
import static org.codice.imaging.nitf.core.label.LabelConstants.LFS_LENGTH;
import static org.codice.imaging.nitf.core.label.LabelConstants.LID_LENGTH;
import static org.codice.imaging.nitf.core.label.LabelConstants.LLOC_HALF_LENGTH;
import static org.codice.imaging.nitf.core.label.LabelConstants.LXSHDL_LENGTH;
import static org.codice.imaging.nitf.core.label.LabelConstants.LXSOFL_LENGTH;
import org.codice.imaging.nitf.core.tre.TreParser;
import org.codice.imaging.nitf.core.tre.TreSource;

/**
 * Writer of Label Segments.
 */
public class LabelSegmentWriter extends AbstractSegmentWriter {

    /**
     * Constructor.
     *
     * @param output the target to write the label segment to.
     * @param treParser TreParser to use to serialise out the TREs.
     */
    public LabelSegmentWriter(final DataOutput output, final TreParser treParser) {
        super(output, treParser);
    }

    /**
     * Write out the subheader for the specified label segment.
     *
     * @param header the header content to write out
     * @throws IOException on write failure.
     * @throws ParseException on TRE parsing failure.
     */
    public final void writeLabelHeader(final LabelSegmentHeader header) throws IOException, ParseException {
        writeFixedLengthString(LA, LA.length());
        writeFixedLengthString(header.getIdentifier(), LID_LENGTH);
        writeSecurityMetadata(header.getSecurityMetadata(), FileType.NITF_TWO_ZERO);
        writeENCRYP();
        writeFixedLengthString(" ", LFS_LENGTH);
        writeFixedLengthNumber(header.getLabelCellWidth(), LCW_LENGTH);
        writeFixedLengthNumber(header.getLabelCellHeight(), LCH_LENGTH);
        writeFixedLengthNumber(header.getLabelDisplayLevel(), LDLVL_LENGTH);
        writeFixedLengthNumber(header.getAttachmentLevel(), LALVL_LENGTH);
        writeFixedLengthNumber(header.getLabelLocationRow(), LLOC_HALF_LENGTH);
        writeFixedLengthNumber(header.getLabelLocationColumn(), LLOC_HALF_LENGTH);
        mOutput.write(header.getLabelTextColour().toByteArray());
        mOutput.write(header.getLabelBackgroundColour().toByteArray());
        byte[] labelExtendedSubheaderData = mTreParser.getTREs(header, TreSource.LabelExtendedSubheaderData);
        int labelExtendedSubheaderDataLength = labelExtendedSubheaderData.length;
        if ((labelExtendedSubheaderDataLength > 0) || (header.getExtendedHeaderDataOverflow() != 0)) {
            labelExtendedSubheaderDataLength += LXSOFL_LENGTH;
        }
        writeFixedLengthNumber(labelExtendedSubheaderDataLength, LXSHDL_LENGTH);
        if (labelExtendedSubheaderDataLength > 0) {
            writeFixedLengthNumber(header.getExtendedHeaderDataOverflow(), LXSOFL_LENGTH);
            mOutput.write(labelExtendedSubheaderData);
        }
    }

    /**
     * Write out the data associated with this label segment.
     *
     * @param labelData the data to write out.
     * @throws IOException if there is a writing error.
     */
    public final void writeLabelData(final String labelData) throws IOException {
        mOutput.writeBytes(labelData);
    }
}



