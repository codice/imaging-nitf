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
package org.codice.imaging.nitf.core.graphic;

import java.io.DataOutput;
import java.io.IOException;
import java.text.ParseException;
import org.codice.imaging.nitf.core.common.AbstractSegmentWriter;
import org.codice.imaging.nitf.core.common.FileType;
import static org.codice.imaging.nitf.core.graphic.GraphicSegmentConstants.SALVL_LENGTH;
import static org.codice.imaging.nitf.core.graphic.GraphicSegmentConstants.SBND1_HALF_LENGTH;
import static org.codice.imaging.nitf.core.graphic.GraphicSegmentConstants.SBND2_HALF_LENGTH;
import static org.codice.imaging.nitf.core.graphic.GraphicSegmentConstants.SCOLOR_LENGTH;
import static org.codice.imaging.nitf.core.graphic.GraphicSegmentConstants.SDLVL_LENGTH;
import static org.codice.imaging.nitf.core.graphic.GraphicSegmentConstants.SFMT_CGM;
import static org.codice.imaging.nitf.core.graphic.GraphicSegmentConstants.SID_LENGTH;
import static org.codice.imaging.nitf.core.graphic.GraphicSegmentConstants.SLOC_HALF_LENGTH;
import static org.codice.imaging.nitf.core.graphic.GraphicSegmentConstants.SNAME_LENGTH;
import static org.codice.imaging.nitf.core.graphic.GraphicSegmentConstants.SRES;
import static org.codice.imaging.nitf.core.graphic.GraphicSegmentConstants.SSTRUCT;
import static org.codice.imaging.nitf.core.graphic.GraphicSegmentConstants.SXSHDL_LENGTH;
import static org.codice.imaging.nitf.core.graphic.GraphicSegmentConstants.SXSOFL_LENGTH;
import static org.codice.imaging.nitf.core.graphic.GraphicSegmentConstants.SY;
import org.codice.imaging.nitf.core.tre.TreParser;
import org.codice.imaging.nitf.core.tre.TreSource;

/**
 * Writer for Graphic Segments.
 */
public class GraphicSegmentWriter extends AbstractSegmentWriter {


    /**
     * Constructor.
     *
     * @param output the target to write the graphic segment to.
     * @param treParser TreParser to use to serialise out the TREs.
     */
    public GraphicSegmentWriter(final DataOutput output, final TreParser treParser) {
        super(output, treParser);
    }

    /**
     * Write out the subheader for the specified graphic segment.
     *
     * @param header the header content to write out
     * @throws IOException on write failure.
     * @throws ParseException on TRE parsing failure.
     */
    public final void writeGraphicHeader(final GraphicSegmentHeader header) throws IOException, ParseException {
        writeFixedLengthString(SY, SY.length());
        writeFixedLengthString(header.getIdentifier(), SID_LENGTH);
        writeFixedLengthString(header.getGraphicName(), SNAME_LENGTH);
        writeSecurityMetadata(header.getSecurityMetadata(), FileType.NITF_TWO_ONE);
        writeENCRYP();
        writeFixedLengthString(SFMT_CGM, SFMT_CGM.length());
        writeFixedLengthString(SSTRUCT, SSTRUCT.length());
        writeFixedLengthNumber(header.getGraphicDisplayLevel(), SDLVL_LENGTH);
        writeFixedLengthNumber(header.getAttachmentLevel(), SALVL_LENGTH);
        writeFixedLengthNumber(header.getGraphicLocationRow(), SLOC_HALF_LENGTH);
        writeFixedLengthNumber(header.getGraphicLocationColumn(), SLOC_HALF_LENGTH);
        writeFixedLengthNumber(header.getBoundingBox1Row(), SBND1_HALF_LENGTH);
        writeFixedLengthNumber(header.getBoundingBox1Column(), SBND1_HALF_LENGTH);
        writeFixedLengthString(header.getGraphicColour().getTextEquivalent(), SCOLOR_LENGTH);
        writeFixedLengthNumber(header.getBoundingBox2Row(), SBND2_HALF_LENGTH);
        writeFixedLengthNumber(header.getBoundingBox2Column(), SBND2_HALF_LENGTH);
        writeFixedLengthString(SRES, SRES.length()); // SRES2
        byte[] graphicExtendedSubheaderData = mTreParser.getTREs(header, TreSource.GraphicExtendedSubheaderData);
        int graphicExtendedSubheaderDataLength = graphicExtendedSubheaderData.length;
        if ((graphicExtendedSubheaderDataLength > 0) || (header.getExtendedHeaderDataOverflow() != 0)) {
            graphicExtendedSubheaderDataLength += SXSOFL_LENGTH;
        }
        writeFixedLengthNumber(graphicExtendedSubheaderDataLength, SXSHDL_LENGTH);
        if (graphicExtendedSubheaderDataLength > 0) {
            writeFixedLengthNumber(header.getExtendedHeaderDataOverflow(), SXSOFL_LENGTH);
            mOutput.write(graphicExtendedSubheaderData);
        }
    }

}
