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

import java.io.IOException;
import java.text.ParseException;
import javax.imageio.stream.ImageInputStream;
import org.codice.imaging.nitf.core.common.CommonBasicSegmentImpl;
import org.codice.imaging.nitf.core.common.CommonConstants;
import org.codice.imaging.nitf.core.graphic.GraphicSegmentConstants;
import org.codice.imaging.nitf.core.tre.TreParser;
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
        Set the symbol name (SNAME).

        @param name the symbol name (20 characters maximum).
    */
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
        Set the symbol type (STYPE).
        <p>
        From MIL-STD-2500A: "This field shall contain a valid indicator of the representation type of the symbol.
        Valid values are B, C, and O. B means bit-mapped. For bit-mapped symbols, the
        symbol parameters are found in the symbol subheader, and the symbol data values
        are contained in the symbol data field immediately following the subheader. C
        means Computer Graphics Metafile. The symbol data contain a Computer Graphics
        Metafile in binary format that defines the symbol according to the specification of
        CGM for NITF in NITFS MIL-STD-2301. O means object. The Symbol Number
        (SNUM) is a reference number that indicates the specific symbol as defined in table
        VIII. No symbol data field if this shall be present contains O, since an object
        symbol only has a subheader. The currently defined objects are standard geometric
        shapes and annotations of sufficient simplicity that they can be implemented
        accurately from verbal descriptions. Future versions of the NITF will include various
        predefined objects such as symbols for military units, vehicles, weapons, aircraft."
        <p>
        Note that NITF 2.1 only uses Computer Graphics Metafile ('C', SymbolType.CGM).

        @param type the symbol type.
    */
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
        Set the symbol colour format (SCOLOR).
        <p>
        See the SymbolColour enumeration for the formats

        @param colourFormat the colour format to set.
    */
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
        Set the number of lines per symbol (NLIPS).
        <p>
        "If STYPE = B or O, this field shall contain the number of rows (lines) in the symbol
        image. This field shall contain zero if STYPE = C."

        @param numLinesPerSymbol the number of lines per symbol.
    */
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
        Set the number of pixels per line (NPIXPL).
        <p>
        "If STYPE = B or O, this field shall contain the number of pixels in each row (line)
        of the symbol (equals the number of image columns in the symbol viewed as an
        image). This field shall contain zero if STYPE = C."

        @param numPixelsPerLine the number of pixels per line.
    */
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
        Set the line width (NWDTH).
        <p>
        "If STYPE = O, this field shall contain the line width for the object symbol in pixels.
        If this field equals the value in NLIPS, the symbol should be drawn solid (filled in).
        This field shall contain zero if STYPE = C or B."

        @param width the line width.
    */
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
        Set the number of bits per pixel (NBPP).
        <p>
        "If STYPE = B, this field shall contain the number of storage bits used for the value
        of each pixel in the symbol. If STYPE = C, this field shall contain zero. If STYPE = O,
        this field shall contain the value "1"."

        @param bitsPerPixel the number of bits per pixel.
    */
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
        Set the symbol display level (SDLVL).
        <p>
        "This field shall contain a valid value that indicates the graphic display level of the
        symbol relative to other displayed file components in a composite display. The valid
        values are 001 to 999. The display level of each displayable file component (image,
        label, or symbol) within a file shall be unique; that is, each number from 001 to 999
        is the display level of, at most, one item. The meaning of display level is discussed
        fully in 5.3.3. The symbol, image, or label component in the file having the
        minimum display level shall have attachment level zero (ILOC, SLOC, and LLOC
        field descriptions)."

        @param displayLevel the display level.
    */
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
        Set the row part of the symbol location (SLOC).
        <p>
        "The symbols location is specified by providing the location of a point bearing a
        particular relationship to the symbol. For a bit-mapped symbol, the point is the first
        pixel of the first row. For an object symbol, the point is specified in table VIII as
        part of each symbol's definition. For a CGM symbol, the point is defined in MIL-
        STD-2301. This field shall contain the symbol location represented as rrrrrccccc,
        where rrrrr and ccccc are the row and column offset from the ILOC, SLOC, or
        LLOC value of the item to which the symbol is attached. A row and column value
        of 00000 indicates no offset. Positive row and column values indicate offsets down
        and to the right and range from 00001 to 99999, while negative row and column
        values indicate offsets up and to the left and must be within the range -0001 to -
        9999. The coordinate system used to express ILOC, SLOC, and LLOC fields shall
        be common for all images, labels, and symbols in the file having attachment level
        zero. The location in this common coordinate system of all displayable graphic
        components can be computed from the offsets given in the ILOC, SLOC, and LLOC
        fields."

        @param rowNumber the row number part of the symbol location.
    */
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
        Set the column part of the symbol location (SLOC).
        <p>
        "The symbols location is specified by providing the location of a point bearing a
        particular relationship to the symbol. For a bit-mapped symbol, the point is the first
        pixel of the first row. For an object symbol, the point is specified in table VIII as
        part of each symbol's definition. For a CGM symbol, the point is defined in MIL-
        STD-2301. This field shall contain the symbol location represented as rrrrrccccc,
        where rrrrr and ccccc are the row and column offset from the ILOC, SLOC, or
        LLOC value of the item to which the symbol is attached. A row and column value
        of 00000 indicates no offset. Positive row and column values indicate offsets down
        and to the right and range from 00001 to 99999, while negative row and column
        values indicate offsets up and to the left and must be within the range -0001 to -
        9999. The coordinate system used to express ILOC, SLOC, and LLOC fields shall
        be common for all images, labels, and symbols in the file having attachment level
        zero. The location in this common coordinate system of all displayable graphic
        components can be computed from the offsets given in the ILOC, SLOC, and LLOC
        fields."

        @param columnNumber the column number part of the symbol location.
    */
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
        Set the row part of the second symbol location (SLOC2).
        <p>
        "This field shall contain an ordered pair of integers defining a location in Cartesian
        coordinates for use with object symbols. The meaning of this location is defined in
        table X for object symbols. The format is rrrrrccccc, where rrrrr is the row and
        ccccc is the column offset from the ILOC, CLOC, or LLOC value of the item to
        which the symbol is attached. If the symbol is unattached (SALVL = 0), rrrrr and
        ccccc represent offsets from the origin of the coordinate system that is common to all
        images, labels, and symbols in the file having attachment level zero. rrrrr and ccccc
        each range from -9999 to 99999."

        @param rowNumber the row number part of the second location.
    */
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
        Set the column part of the second symbol location (SLOC2).
        <p>
        "This field shall contain an ordered pair of integers defining a location in Cartesian
        coordinates for use with object symbols. The meaning of this location is defined in
        table X for object symbols. The format is rrrrrccccc, where rrrrr is the row and
        ccccc is the column offset from the ILOC, CLOC, or LLOC value of the item to
        which the symbol is attached. If the symbol is unattached (SALVL = 0), rrrrr and
        ccccc represent offsets from the origin of the coordinate system that is common to all
        images, labels, and symbols in the file having attachment level zero. rrrrr and ccccc
        each range from -9999 to 99999."

        @param columnNumber the column number part of the second location.
    */
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
        Set the symbol number (SNUM).
        <p>
        "For object symbols, this field shall contain the unique numeric identifier (values 1-18)
        of one of the objects defined in table X. For bit-mapped and CGM symbols, this
        field shall contain 000000. The field is alphanumeric to support future use of
        alphanumeric symbol identifiers."

        @param number the symbol number.
    */
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
        Set the symbol rotation (SROT).
        <p>
        "When STYPE = O, this field shall contain the rotation angle of the symbol in integer
        degrees about its rotation point in the counterclockwise direction with respect to the
        nominal orientation. Nominal orientation is the orientation corresponding to SROT = 000.
        If STYPE = B or C, this field shall contain 000, and shall be ignored."

        @param rotation the rotation angle
    */
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
    public long getHeaderLength() throws ParseException, IOException {
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
