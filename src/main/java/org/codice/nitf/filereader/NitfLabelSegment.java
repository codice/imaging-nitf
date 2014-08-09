/**
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
 **/
package org.codice.nitf.filereader;

import java.text.ParseException;
import java.util.Set;

public class NitfLabelSegment extends AbstractNitfSubSegment {

    private int labelLocationRow = 0;
    private int labelLocationColumn = 0;
    private int labelCellWidth = 0;
    private int labelCellHeight = 0;
    private int labelDisplayLevel = 0;
    private int labelAttachmentLevel = 0;
    private RGBColour labelTextColour = null;
    private RGBColour labelBackgroundColour = null;

    private String data = null;

    public NitfLabelSegment() {
    }

    public final void parse(final NitfReader nitfReader,
                            final int labelLength,
                            final Set<ParseOption> parseOptions) throws ParseException {
        new NitfLabelSegmentParser(nitfReader, labelLength, parseOptions, this);
    }

    public final void setLabelLocationRow(final int rowNumber) {
        labelLocationRow = rowNumber;
    }

    public final int getLabelLocationRow() {
        return labelLocationRow;
    }

    public final void setLabelLocationColumn(final int columnNumber) {
        labelLocationColumn = columnNumber;
    }

    public final int getLabelLocationColumn() {
        return labelLocationColumn;
    }

    public final void setLabelCellWidth(final int cellWidth) {
        labelCellWidth = cellWidth;
    }

    /**
      Returns the label cell width (LCW) of the label.
      <p>
       "This field shall contain the width in pixels of the character cell (rectangular array
       used to contain a single character in monospaced fonts) used by the file originator.
       The default value of 00 indicates the file originator has not included this information."

      @return cell width (default 0, not set)
    **/
    public final int getLabelCellWidth() {
        return labelCellWidth;
    }

    public final void setLabelCellHeight(final int cellHeight) {
        labelCellHeight = cellHeight;
    }

    /**
      Returns the label cell height (LCH) of the label.
      <p>
        "This field shall contain the height in pixels of the character cell (rectangular array
        used to contain a single character in monospaced fonts) used by the file originator.
        The default value of 00 indicates the file originator has not included this information."

      @return cell height (default 0, not set)
    **/
    public final int getLabelCellHeight() {
        return labelCellHeight;
    }

    public final void setLabelDisplayLevel(final int displayLevel) {
        labelDisplayLevel = displayLevel;
    }

    public final int getLabelDisplayLevel() {
        return labelDisplayLevel;
    }

    public final void setLabelTextColour(final RGBColour textColour) {
        labelTextColour = textColour;
    }

    public final RGBColour getLabelTextColour() {
        return labelTextColour;
    }

    public final void setLabelBackgroundColour(final RGBColour backgroundColour) {
        labelBackgroundColour = backgroundColour;
    }

    public final RGBColour getLabelBackgroundColour() {
        return labelBackgroundColour;
    }

    public final void setLabelData(final String labelData) {
        data = labelData;
    }

    public final String getLabelData() {
        return data;
    }
}
