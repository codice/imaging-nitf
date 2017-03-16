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
package org.codice.imaging.nitf.core.label.impl;

import static org.codice.imaging.nitf.core.label.impl.LabelConstants.LA;
import static org.codice.imaging.nitf.core.label.impl.LabelConstants.LALVL_LENGTH;
import static org.codice.imaging.nitf.core.label.impl.LabelConstants.LCH_LENGTH;
import static org.codice.imaging.nitf.core.label.impl.LabelConstants.LCW_LENGTH;
import static org.codice.imaging.nitf.core.label.impl.LabelConstants.LDLVL_LENGTH;
import static org.codice.imaging.nitf.core.label.impl.LabelConstants.LFS_LENGTH;
import static org.codice.imaging.nitf.core.label.impl.LabelConstants.LID_LENGTH;
import static org.codice.imaging.nitf.core.label.impl.LabelConstants.LLOC_HALF_LENGTH;
import static org.codice.imaging.nitf.core.label.impl.LabelConstants.LXSHDL_LENGTH;
import static org.codice.imaging.nitf.core.label.impl.LabelConstants.LXSOFL_LENGTH;

import java.io.DataOutput;
import java.io.IOException;

import org.codice.imaging.nitf.core.common.NitfFormatException;
import org.codice.imaging.nitf.core.common.impl.AbstractSegmentWriter;
import org.codice.imaging.nitf.core.impl.RGBColourImpl;
import org.codice.imaging.nitf.core.label.LabelSegment;
import org.codice.imaging.nitf.core.tre.TreSource;
import org.codice.imaging.nitf.core.tre.impl.TreParser;

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
     * @throws NitfFormatException on TRE parsing failure.
     */
    public final void writeLabel(final LabelSegment labelSegment) throws IOException, NitfFormatException {
        writeFixedLengthString(LA, LA.length());
        writeFixedLengthString(labelSegment.getIdentifier(), LID_LENGTH);
        writeSecurityMetadata(labelSegment.getSecurityMetadata());
        writeENCRYP();
        writeFixedLengthString(" ", LFS_LENGTH);
        writeFixedLengthNumber(labelSegment.getLabelCellWidth(), LCW_LENGTH);
        writeFixedLengthNumber(labelSegment.getLabelCellHeight(), LCH_LENGTH);
        writeFixedLengthNumber(labelSegment.getLabelDisplayLevel(), LDLVL_LENGTH);
        writeFixedLengthNumber(labelSegment.getAttachmentLevel(), LALVL_LENGTH);
        writeFixedLengthNumber(labelSegment.getLabelLocationRow(), LLOC_HALF_LENGTH);
        writeFixedLengthNumber(labelSegment.getLabelLocationColumn(), LLOC_HALF_LENGTH);
        writeBytes(labelSegment.getLabelTextColour().toByteArray(), RGBColourImpl.RGB_COLOUR_LENGTH);
        writeBytes(labelSegment.getLabelBackgroundColour().toByteArray(), RGBColourImpl.RGB_COLOUR_LENGTH);
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



