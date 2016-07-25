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

import java.io.IOException;
import javax.imageio.stream.ImageInputStream;
import org.codice.imaging.nitf.core.common.CommonBasicSegmentImpl;
import org.codice.imaging.nitf.core.common.CommonConstants;
import org.codice.imaging.nitf.core.common.NitfFormatException;
import org.codice.imaging.nitf.core.tre.TreParser;
import org.codice.imaging.nitf.core.tre.TreSource;

/**
    Graphic segment information (NITF 2.1 / NSIF 1.0 only).
*/
class GraphicSegmentImpl extends CommonBasicSegmentImpl
        implements GraphicSegment {

    private String graphicName = "";
    private int graphicDisplayLevel = 0;
    private int graphicLocationRow = 0;
    private int graphicLocationColumn = 0;
    private int boundingBox1Row = 0;
    private int boundingBox1Column = 0;
    private GraphicColour graphicColour = GraphicColour.UNKNOWN;
    private int boundingBox2Row = 0;
    private int boundingBox2Column = 0;
    private ImageInputStream dataStream = null;
    private long dataLength = 0;

    /**
        Default constructor.
    */
    GraphicSegmentImpl() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void setGraphicName(final String name) {
        graphicName = name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String getGraphicName() {
        return graphicName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void setGraphicDisplayLevel(final int displayLevel) {
        graphicDisplayLevel = displayLevel;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final int getGraphicDisplayLevel() {
        return graphicDisplayLevel;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void setGraphicLocationRow(final int rowNumber) {
        graphicLocationRow = rowNumber;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final int getGraphicLocationRow() {
        return graphicLocationRow;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void setGraphicLocationColumn(final int columnNumber) {
        graphicLocationColumn = columnNumber;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final int getGraphicLocationColumn() {
        return graphicLocationColumn;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void setBoundingBox1Row(final int rowNumber) {
        boundingBox1Row = rowNumber;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final int getBoundingBox1Row() {
        return boundingBox1Row;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void setBoundingBox1Column(final int columnNumber) {
        boundingBox1Column = columnNumber;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final int getBoundingBox1Column() {
        return boundingBox1Column;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void setGraphicColour(final GraphicColour colour) {
        graphicColour = colour;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final GraphicColour getGraphicColour() {
        return graphicColour;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void setBoundingBox2Row(final int rowNumber) {
        boundingBox2Row = rowNumber;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final int getBoundingBox2Row() {
        return boundingBox2Row;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void setBoundingBox2Column(final int columnNumber) {
        boundingBox2Column = columnNumber;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final int getBoundingBox2Column() {
        return boundingBox2Column;
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
    public ImageInputStream getData() {
        return dataStream;
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
                + GraphicSegmentConstants.SFMT_CGM.length()
                + GraphicSegmentConstants.SSTRUCT.length()
                + GraphicSegmentConstants.SDLVL_LENGTH
                + GraphicSegmentConstants.SALVL_LENGTH
                + GraphicSegmentConstants.SLOC_HALF_LENGTH * 2
                + GraphicSegmentConstants.SBND1_HALF_LENGTH * 2
                + GraphicSegmentConstants.SCOLOR_LENGTH
                + GraphicSegmentConstants.SBND2_HALF_LENGTH * 2
                + GraphicSegmentConstants.SRES.length()
                + GraphicSegmentConstants.SXSHDL_LENGTH;
        TreParser treParser = new TreParser();
        int extendedDataLength = treParser.getTREs(this, TreSource.GraphicExtendedSubheaderData).length;
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

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDataLength(final long length) {
        dataLength = length;
    }
}
