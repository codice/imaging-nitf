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
package org.codice.imaging.nitf.core;

/**
    Symbol segment subheader information (NITF 2.0 only).
*/
public class NitfSymbolSegment extends AbstractNitfSubSegment {

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

    private byte[] data = null;

    /**
        Default constructor.
    */
    public NitfSymbolSegment() {
    }

    public final void setSymbolName(final String name) {
        symbolName = name;
    }

    public final String getSymbolName() {
        return symbolName;
    }

    public final void setSymbolType(final SymbolType type) {
        symbolType = type;
    }

    public final SymbolType getSymbolType() {
        return symbolType;
    }

    public final void setSymbolColourFormat(final SymbolColour colourFormat) {
        symbolColourFormat = colourFormat;
    }

    public final SymbolColour getSymbolColour() {
        return symbolColourFormat;
    }

    public final void setNumberOfLinesPerSymbol(final int numLinesPerSymbol) {
        numberOfLinesPerSymbol = numLinesPerSymbol;
    }

    public final int getNumberOfLinesPerSymbol() {
        return numberOfLinesPerSymbol;
    }

    public final void setNumberOfPixelsPerLine(final int numPixelsPerLine) {
        numberofPixelsPerLine = numPixelsPerLine;
    }

    public final int getNumberOfPixelsPerLine() {
        return numberofPixelsPerLine;
    }

    public final void setLineWidth(final int width) {
        lineWidth = width;
    }

    public final int getLineWidth() {
        return lineWidth;
    }

    public final void setNumberOfBitsPerPixel(final int bitsPerPixel) {
        numberOfBitsPerPixel = bitsPerPixel;
    }

    public final int getNumberOfBitsPerPixel() {
        return numberOfBitsPerPixel;
    }

    public final void setSymbolDisplayLevel(final int displayLevel) {
        symbolDisplayLevel = displayLevel;
    }

    public final int getSymbolDisplayLevel() {
        return symbolDisplayLevel;
    }

    public final void setSymbolLocationRow(final int rowNumber) {
        symbolLocationRow = rowNumber;
    }

    public final int getSymbolLocationRow() {
        return symbolLocationRow;
    }

    public final void setSymbolLocationColumn(final int columnNumber) {
        symbolLocationColumn = columnNumber;
    }

    public final int getSymbolLocationColumn() {
        return symbolLocationColumn;
    }

    public final void setSymbolLocation2Row(final int rowNumber) {
        symbolLocation2Row = rowNumber;
    }

    public final int getSymbolLocation2Row() {
        return symbolLocation2Row;
    }

    public final void setSymbolLocation2Column(final int columnNumber) {
        symbolLocation2Column = columnNumber;
    }

    public final int getSymbolLocation2Column() {
        return symbolLocation2Column;
    }

    public final void setSymbolNumber(final String number) {
        symbolNumber = number;
    }

    public final String getSymbolNumber() {
        return symbolNumber;
    }

    public final void setSymbolRotation(final int rotation) {
        symbolRotation = rotation;
    }

    public final int getSymbolRotation() {
        return symbolRotation;
    }

    /**
        Set the symbol data.
        <p>
        This is the contents of the data segment.

        @param symbolData the symbol data
    */
    public final void setSymbolData(final byte[] symbolData) {
        data = symbolData;
    }

    /**
        Return the symbol data.
        <p>
        This is the contents of the data segment. Depending on how the
        parsing was configured, this can be null.

        @return the symbol data
    */
    public final byte[] getSymbolData() {
        return data;
    }
}
