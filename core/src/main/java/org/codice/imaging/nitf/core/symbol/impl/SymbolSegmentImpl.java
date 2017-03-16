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
package org.codice.imaging.nitf.core.symbol.impl;

import java.io.IOException;
import javax.imageio.stream.ImageInputStream;
import org.codice.imaging.nitf.core.common.impl.CommonBasicSegmentImpl;
import org.codice.imaging.nitf.core.common.impl.CommonConstants;
import org.codice.imaging.nitf.core.common.NitfFormatException;
import org.codice.imaging.nitf.core.graphic.impl.GraphicSegmentConstants;
import org.codice.imaging.nitf.core.symbol.SymbolColour;
import org.codice.imaging.nitf.core.symbol.SymbolSegment;
import org.codice.imaging.nitf.core.symbol.SymbolType;
import org.codice.imaging.nitf.core.tre.impl.TreParser;
import org.codice.imaging.nitf.core.tre.TreSource;

/**
    Symbol segment information (NITF 2.0 only).
*/
class SymbolSegmentImpl extends CommonBasicSegmentImpl implements SymbolSegment {

    private String symbolName = null;
    private SymbolType symbolType = null;
    private int numberOfLinesPerSymbol = 0;
    private int numberofPixelsPerLine = 0;
    private int lineWidth = 0;
    private int numberOfBitsPerPixel = 0;
    private int symbolDisplayLevel = 0;
    private int symbolLocationRow = 0;
    private int symbolLocationColumn = 0;
    private int symbolLocation2Row = 0;
    private int symbolLocation2Column = 0;
    private String symbolNumber = "000000";
    private int symbolRotation = 0;
    private SymbolColour symbolColourFormat = SymbolColour.UNKNOWN;
    private ImageInputStream dataStream = null;
    private long dataLength = 0;

    /**
        Default constructor.
    */
    SymbolSegmentImpl() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void setSymbolName(final String name) {
        symbolName = name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String getSymbolName() {
        return symbolName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void setSymbolType(final SymbolType type) {
        symbolType = type;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final SymbolType getSymbolType() {
        return symbolType;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void setSymbolColourFormat(final SymbolColour colourFormat) {
        symbolColourFormat = colourFormat;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final SymbolColour getSymbolColour() {
        return symbolColourFormat;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void setNumberOfLinesPerSymbol(final int numLinesPerSymbol) {
        numberOfLinesPerSymbol = numLinesPerSymbol;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final int getNumberOfLinesPerSymbol() {
        return numberOfLinesPerSymbol;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void setNumberOfPixelsPerLine(final int numPixelsPerLine) {
        numberofPixelsPerLine = numPixelsPerLine;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final int getNumberOfPixelsPerLine() {
        return numberofPixelsPerLine;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void setLineWidth(final int width) {
        lineWidth = width;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final int getLineWidth() {
        return lineWidth;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void setNumberOfBitsPerPixel(final int bitsPerPixel) {
        numberOfBitsPerPixel = bitsPerPixel;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final int getNumberOfBitsPerPixel() {
        return numberOfBitsPerPixel;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void setSymbolDisplayLevel(final int displayLevel) {
        symbolDisplayLevel = displayLevel;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final int getSymbolDisplayLevel() {
        return symbolDisplayLevel;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void setSymbolLocationRow(final int rowNumber) {
        symbolLocationRow = rowNumber;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final int getSymbolLocationRow() {
        return symbolLocationRow;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void setSymbolLocationColumn(final int columnNumber) {
        symbolLocationColumn = columnNumber;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final int getSymbolLocationColumn() {
        return symbolLocationColumn;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void setSymbolLocation2Row(final int rowNumber) {
        symbolLocation2Row = rowNumber;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final int getSymbolLocation2Row() {
        return symbolLocation2Row;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void setSymbolLocation2Column(final int columnNumber) {
        symbolLocation2Column = columnNumber;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final int getSymbolLocation2Column() {
        return symbolLocation2Column;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void setSymbolNumber(final String number) {
        symbolNumber = number;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String getSymbolNumber() {
        return symbolNumber;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void setSymbolRotation(final int rotation) {
        symbolRotation = rotation;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final int getSymbolRotation() {
        return symbolRotation;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ImageInputStream getData() {
        return dataStream;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setData(final ImageInputStream data) {
        dataStream = data;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getHeaderLength() throws NitfFormatException, IOException {
        long headerLength = GraphicSegmentConstants.SY.length()
                + GraphicSegmentConstants.SID_LENGTH
                + GraphicSegmentConstants.SNAME_LENGTH
                + getSecurityMetadata().getSerialisedLength()
                + CommonConstants.ENCRYP_LENGTH
                + SymbolConstants.SYTYPE_LENGTH
                + SymbolConstants.NLIPS_LENGTH
                + SymbolConstants.NPIXPL_LENGTH
                + SymbolConstants.NWDTH_LENGTH
                + SymbolConstants.SYNBPP_LENGTH
                + GraphicSegmentConstants.SDLVL_LENGTH
                + GraphicSegmentConstants.SALVL_LENGTH
                + GraphicSegmentConstants.SLOC_HALF_LENGTH * 2
                + GraphicSegmentConstants.SLOC_HALF_LENGTH * 2
                + GraphicSegmentConstants.SCOLOR_LENGTH
                + SymbolConstants.SNUM_LENGTH
                + SymbolConstants.SROT_LENGTH
                + SymbolConstants.SYNELUT_LENGTH
                + GraphicSegmentConstants.SXSHDL_LENGTH;
        // NOTE: We don't support LUT entries in symbol segments yet.
        TreParser treParser = new TreParser();
        int extendedDataLength = treParser.getTREs(this, TreSource.SymbolExtendedSubheaderData).length;
        if (extendedDataLength > 0) {
            headerLength += GraphicSegmentConstants.SXSOFL_LENGTH;
            headerLength += extendedDataLength;
        }
        return headerLength;
    }

    @Override
    public final long getDataLength() {
        return dataLength;
    }

    @Override
    public void setDataLength(final long length) {
        dataLength = length;
    }

}
