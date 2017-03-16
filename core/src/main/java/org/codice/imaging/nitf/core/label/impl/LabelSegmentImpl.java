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

import java.io.IOException;

import org.codice.imaging.nitf.core.RGBColour;
import org.codice.imaging.nitf.core.impl.RGBColourImpl;
import org.codice.imaging.nitf.core.common.impl.CommonBasicSegmentImpl;
import org.codice.imaging.nitf.core.common.impl.CommonConstants;
import org.codice.imaging.nitf.core.common.NitfFormatException;
import org.codice.imaging.nitf.core.label.LabelSegment;
import org.codice.imaging.nitf.core.tre.impl.TreParser;
import org.codice.imaging.nitf.core.tre.TreSource;

/**
    Label segment information (NITF 2.0 only).
*/
class LabelSegmentImpl extends CommonBasicSegmentImpl implements LabelSegment {

    private int labelLocationRow = 0;
    private int labelLocationColumn = 0;
    private int labelCellWidth = 0;
    private int labelCellHeight = 0;
    private int labelDisplayLevel = 0;
    private RGBColour labelTextColour = null;
    private RGBColour labelBackgroundColour = null;
    private String labelText = null;

    /**
        Default constructor.
    */
    LabelSegmentImpl() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void setLabelLocationRow(final int rowNumber) {
        labelLocationRow = rowNumber;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final int getLabelLocationRow() {
        return labelLocationRow;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void setLabelLocationColumn(final int columnNumber) {
        labelLocationColumn = columnNumber;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final int getLabelLocationColumn() {
        return labelLocationColumn;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void setLabelCellWidth(final int cellWidth) {
        labelCellWidth = cellWidth;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final int getLabelCellWidth() {
        return labelCellWidth;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void setLabelCellHeight(final int cellHeight) {
        labelCellHeight = cellHeight;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final int getLabelCellHeight() {
        return labelCellHeight;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void setLabelDisplayLevel(final int displayLevel) {
        labelDisplayLevel = displayLevel;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final int getLabelDisplayLevel() {
        return labelDisplayLevel;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void setLabelTextColour(final RGBColour textColour) {
        labelTextColour = textColour;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final RGBColour getLabelTextColour() {
        return labelTextColour;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void setLabelBackgroundColour(final RGBColour backgroundColour) {
        labelBackgroundColour = backgroundColour;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final RGBColour getLabelBackgroundColour() {
        return labelBackgroundColour;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getData() {
        return labelText;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setData(final String labelData) {
        labelText = labelData;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final long getHeaderLength() throws NitfFormatException, IOException {
        long headerLength = LabelConstants.LA.length()
                + LabelConstants.LID_LENGTH
                + getSecurityMetadata().getSerialisedLength()
                + CommonConstants.ENCRYP_LENGTH
                + LabelConstants.LFS_LENGTH
                + LabelConstants.LCW_LENGTH
                + LabelConstants.LCH_LENGTH
                + LabelConstants.LDLVL_LENGTH
                + LabelConstants.LALVL_LENGTH
                + LabelConstants.LLOC_HALF_LENGTH * 2
                + RGBColourImpl.RGB_COLOUR_LENGTH
                + RGBColourImpl.RGB_COLOUR_LENGTH
                + LabelConstants.LXSHDL_LENGTH;
        TreParser treParser = new TreParser();
        int extendedDataLength = treParser.getTREs(this, TreSource.LabelExtendedSubheaderData).length;
        if (extendedDataLength > 0) {
            headerLength += LabelConstants.LXSOFL_LENGTH;
            headerLength += extendedDataLength;
        }
        return headerLength;
    }

}
