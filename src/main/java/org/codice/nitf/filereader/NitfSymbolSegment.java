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

public class NitfSymbolSegment extends AbstractNitfSegment {

    private String symbolIdentifier = null;
    private String symbolName = null;
    private SymbolType symbolType = null;
    private int numberOfLinesPerSymbol = 0;
    private int numberofPixelsPerLine = 0;
    private int lineWidth = 0;
    private int numberOfBitsPerPixel = 0;
    private int symbolDisplayLevel = 0;
    private int symbolAttachmentLevel = 0;
    private int symbolLocationRow = 0;
    private int symbolLocationColumn = 0;
    private int symbolLocation2Row = 0;
    private int symbolLocation2Column = 0;
    private String symbolNumber = "000000";
    private int symbolRotation = 0;
    private SymbolColour symbolColourFormat = SymbolColour.UNKNOWN;

    private byte[] data = null;

    private NitfSecurityMetadata securityMetadata = null;

    public NitfSymbolSegment() {
    }

    public final void parse(final NitfReader nitfReader, final int symbolLength, final Set<ParseOption> parseOptions) throws ParseException {
        new NitfSymbolSegmentParser(nitfReader, symbolLength, parseOptions, this);
    }

    public final void setSymbolIdentifier(final String identifier) {
        symbolIdentifier = identifier;
    }

    public final String getSymbolIdentifier() {
        return symbolIdentifier;
    }

    public final void setSymbolName(final String name) {
        symbolName = name;
    }

    public final String getSymbolName() {
        return symbolName;
    }

    public final void setSecurityMetadata(final NitfSecurityMetadata nitfSecurityMetadata) {
        securityMetadata = nitfSecurityMetadata;
    }

    public final NitfSecurityMetadata getSecurityMetadata() {
        return securityMetadata;
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

    public final void setSymbolAttachmentLevel(final int attachmentLevel) {
        symbolAttachmentLevel = attachmentLevel;
    }

    public final int getSymbolAttachmentLevel() {
        return symbolAttachmentLevel;
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

    public final void setSymbolData(final byte[] symbolData) {
        data = symbolData;
    }

    public final byte[] getSymbolData() {
        return data;
    }
}
