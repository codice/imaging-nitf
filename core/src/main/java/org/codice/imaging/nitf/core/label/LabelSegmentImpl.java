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

import java.io.IOException;
import java.text.ParseException;
import org.codice.imaging.nitf.core.RGBColour;
import org.codice.imaging.nitf.core.common.CommonBasicSegmentImpl;
import org.codice.imaging.nitf.core.common.CommonConstants;
import org.codice.imaging.nitf.core.tre.TreParser;
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
        Set the row part of the label location (LLOC).
        <p>
        "A label's location specified by providing the location of the upper left corner of the
        minimum bounding rectangle of the label. This field shall contain the label location
        represented as rrrrrccccc, where rrrrr and ccccc are the row and the column offset
        from the ILOC, SLOC, or LLOC value of the item to which the label is attached.
        A row or column value of 00000 indicates no offset. Positive row and column
        values indicate offsets down and to the right and range from 00001 to 99999, while
        negative row and column values indicate offsets up and to the left and must be
        within the range -0001 to -9999. The coordinate system used to express ILOC,
        SLOC, and LLOC fields shall be common for all images, labels, and symbols in the
        file having attachment level zero. The location in this common coordinate system
        of all displayable graphic components can be computed from the offsets given in the
        ILOC, SLOC, and LLOC fields."

        @param rowNumber the row number for the label location.
    */
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
        Set the column part of the label location (LLOC).
        <p>
        "A label's location specified by providing the location of the upper left corner of the
        minimum bounding rectangle of the label. This field shall contain the label location
        represented as rrrrrccccc, where rrrrr and ccccc are the row and the column offset
        from the ILOC, SLOC, or LLOC value of the item to which the label is attached.
        A row or column value of 00000 indicates no offset. Positive row and column
        values indicate offsets down and to the right and range from 00001 to 99999, while
        negative row and column values indicate offsets up and to the left and must be
        within the range -0001 to -9999. The coordinate system used to express ILOC,
        SLOC, and LLOC fields shall be common for all images, labels, and symbols in the
        file having attachment level zero. The location in this common coordinate system
        of all displayable graphic components can be computed from the offsets given in the
        ILOC, SLOC, and LLOC fields."

        @param columnNumber the column number for the label location.
    */
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
      Set the label cell width (LCW) of the label.
      <p>
       "This field shall contain the width in pixels of the character cell (rectangular array
       used to contain a single character in monospaced fonts) used by the file originator.
       The default value of 00 indicates the file originator has not included this information."

      @param cellWidth cell width
    */
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
      Set the label cell height (LCH) of the label.
      <p>
        "This field shall contain the height in pixels of the character cell (rectangular array
        used to contain a single character in monospaced fonts) used by the file originator.
        The default value of 00 indicates the file originator has not included this information."

      @param cellHeight cell height
    **/
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
        Set the label display level (LDLVL).
        <p>
        "This field shall contain a valid value that indicates the graphic display level of the
        label relative to other displayed file components in a composite display. The valid
        values are 001 to 999. The display level of each displayable file component (image,
        label, symbol) within a file shall be unique; that is, each number from 001 to 999 is
        the display level of, at most, one item. The meaning of display level is discussed
        fully in 5.3.3. The symbol, image, or label component in the file having the
        minimum display level shall have attachment level zero (ILOC, SLOC, and LLOC
        field descriptions)."

        @param displayLevel the label display level
    */
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
        Set the label text colour.

        @param textColour the text colour.
    */
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
        Set the label background colour.

        @param backgroundColour the background colour.
    */
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
    public final long getHeaderLength() throws ParseException, IOException {
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
                + RGBColour.RGB_COLOUR_LENGTH
                + RGBColour.RGB_COLOUR_LENGTH
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
