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
package org.codice.imaging.nitf.core.symbol;

import java.io.DataOutput;
import java.io.IOException;
import java.text.ParseException;
import org.codice.imaging.nitf.core.common.AbstractSegmentWriter;
import org.codice.imaging.nitf.core.common.FileType;
import static org.codice.imaging.nitf.core.graphic.GraphicSegmentConstants.SALVL_LENGTH;
import static org.codice.imaging.nitf.core.graphic.GraphicSegmentConstants.SCOLOR_LENGTH;
import static org.codice.imaging.nitf.core.graphic.GraphicSegmentConstants.SDLVL_LENGTH;
import static org.codice.imaging.nitf.core.graphic.GraphicSegmentConstants.SID_LENGTH;
import static org.codice.imaging.nitf.core.graphic.GraphicSegmentConstants.SLOC_HALF_LENGTH;
import static org.codice.imaging.nitf.core.graphic.GraphicSegmentConstants.SNAME_LENGTH;
import static org.codice.imaging.nitf.core.graphic.GraphicSegmentConstants.SXSHDL_LENGTH;
import static org.codice.imaging.nitf.core.graphic.GraphicSegmentConstants.SXSOFL_LENGTH;
import static org.codice.imaging.nitf.core.graphic.GraphicSegmentConstants.SY;
import static org.codice.imaging.nitf.core.symbol.SymbolConstants.NLIPS_LENGTH;
import static org.codice.imaging.nitf.core.symbol.SymbolConstants.NPIXPL_LENGTH;
import static org.codice.imaging.nitf.core.symbol.SymbolConstants.NWDTH_LENGTH;
import static org.codice.imaging.nitf.core.symbol.SymbolConstants.SNUM_LENGTH;
import static org.codice.imaging.nitf.core.symbol.SymbolConstants.SROT_LENGTH;
import static org.codice.imaging.nitf.core.symbol.SymbolConstants.SYNBPP_LENGTH;
import static org.codice.imaging.nitf.core.symbol.SymbolConstants.SYNELUT_LENGTH;
import static org.codice.imaging.nitf.core.symbol.SymbolConstants.SYTYPE_LENGTH;
import org.codice.imaging.nitf.core.tre.TreParser;
import org.codice.imaging.nitf.core.tre.TreSource;

/**
 * Writer for Symbol Segments.
 */
public class SymbolSegmentWriter extends AbstractSegmentWriter {

    /**
     * Constructor.
     *
     * @param output the target to write the symbol segment to.
     * @param treParser TreParser to use to serialise out the TREs.
     */
    public SymbolSegmentWriter(final DataOutput output, final TreParser treParser) {
        super(output, treParser);
    }

    /**
     * Write out the specified symbol segment.
     *
     * @param header the header content to write out
     * @throws IOException on write failure.
     * @throws ParseException on TRE parsing failure.
     */
    public final void writeSymbolSegment(final SymbolSegment header) throws IOException, ParseException {
        writeFixedLengthString(SY, SY.length());
        writeFixedLengthString(header.getIdentifier(), SID_LENGTH);
        writeFixedLengthString(header.getSymbolName(), SNAME_LENGTH);
        writeSecurityMetadata(header.getSecurityMetadata(), FileType.NITF_TWO_ZERO);
        writeENCRYP();
        writeFixedLengthString(header.getSymbolType().getTextEquivalent(), SYTYPE_LENGTH);
        writeFixedLengthNumber(header.getNumberOfLinesPerSymbol(), NLIPS_LENGTH);
        writeFixedLengthNumber(header.getNumberOfPixelsPerLine(), NPIXPL_LENGTH);
        writeFixedLengthNumber(header.getLineWidth(), NWDTH_LENGTH);
        writeFixedLengthNumber(header.getNumberOfBitsPerPixel(), SYNBPP_LENGTH);
        writeFixedLengthNumber(header.getSymbolDisplayLevel(), SDLVL_LENGTH);
        writeFixedLengthNumber(header.getAttachmentLevel(), SALVL_LENGTH);
        writeFixedLengthNumber(header.getSymbolLocationRow(), SLOC_HALF_LENGTH);
        writeFixedLengthNumber(header.getSymbolLocationColumn(), SLOC_HALF_LENGTH);
        writeFixedLengthNumber(header.getSymbolLocation2Row(), SLOC_HALF_LENGTH);
        writeFixedLengthNumber(header.getSymbolLocation2Column(), SLOC_HALF_LENGTH);
        writeFixedLengthString(header.getSymbolColour().getTextEquivalent(), SCOLOR_LENGTH);
        writeFixedLengthString(header.getSymbolNumber(), SNUM_LENGTH);
        writeFixedLengthNumber(header.getSymbolRotation(), SROT_LENGTH);
        // TODO: need to have a LUT list or similar in the symbol segment subheader
        writeFixedLengthNumber(0, SYNELUT_LENGTH);
        byte[] symbolExtendedSubheaderData = mTreParser.getTREs(header, TreSource.SymbolExtendedSubheaderData);
        int symbolExtendedSubheaderDataLength = symbolExtendedSubheaderData.length;
        if ((symbolExtendedSubheaderDataLength > 0) || (header.getExtendedHeaderDataOverflow() != 0)) {
            symbolExtendedSubheaderDataLength += SXSOFL_LENGTH;
        }
        writeFixedLengthNumber(symbolExtendedSubheaderDataLength, SXSHDL_LENGTH);
        if (symbolExtendedSubheaderDataLength > 0) {
            writeFixedLengthNumber(header.getExtendedHeaderDataOverflow(), SXSOFL_LENGTH);
            writeBytes(symbolExtendedSubheaderData, symbolExtendedSubheaderDataLength - SXSOFL_LENGTH);
        }
        writeSegmentData(header.getData());
    }
}
