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
import org.codice.imaging.nitf.core.RGBColour;
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
     * Write out the specified label segment.
     *
     * @param labelSegment the content to write out
     * @throws IOException on write failure.
     * @throws ParseException on TRE parsing failure.
     */
    public final void writeLabel(final LabelSegment labelSegment) throws IOException, ParseException {
        writeFixedLengthString(LA, LA.length());
        writeFixedLengthString(labelSegment.getIdentifier(), LID_LENGTH);
        writeSecurityMetadata(labelSegment.getSecurityMetadata(), FileType.NITF_TWO_ZERO);
        writeENCRYP();
        writeFixedLengthString(" ", LFS_LENGTH);
        writeFixedLengthNumber(labelSegment.getLabelCellWidth(), LCW_LENGTH);
        writeFixedLengthNumber(labelSegment.getLabelCellHeight(), LCH_LENGTH);
        writeFixedLengthNumber(labelSegment.getLabelDisplayLevel(), LDLVL_LENGTH);
        writeFixedLengthNumber(labelSegment.getAttachmentLevel(), LALVL_LENGTH);
        writeFixedLengthNumber(labelSegment.getLabelLocationRow(), LLOC_HALF_LENGTH);
        writeFixedLengthNumber(labelSegment.getLabelLocationColumn(), LLOC_HALF_LENGTH);
        writeBytes(labelSegment.getLabelTextColour().toByteArray(), RGBColour.RGB_COLOUR_LENGTH);
        writeBytes(labelSegment.getLabelBackgroundColour().toByteArray(), RGBColour.RGB_COLOUR_LENGTH);
        byte[] labelExtendedSubheaderData = mTreParser.getTREs(labelSegment, TreSource.LabelExtendedSubheaderData);
        int labelExtendedSubheaderDataLength = labelExtendedSubheaderData.length;
        if ((labelExtendedSubheaderDataLength > 0) || (labelSegment.getExtendedHeaderDataOverflow() != 0)) {
            labelExtendedSubheaderDataLength += LXSOFL_LENGTH;
        }
        writeFixedLengthNumber(labelExtendedSubheaderDataLength, LXSHDL_LENGTH);
        if (labelExtendedSubheaderDataLength > 0) {
            writeFixedLengthNumber(labelSegment.getExtendedHeaderDataOverflow(), LXSOFL_LENGTH);
            writeBytes(labelExtendedSubheaderData, labelExtendedSubheaderDataLength - LXSOFL_LENGTH);
        }
        mOutput.writeBytes(labelSegment.getData());
    }
}



