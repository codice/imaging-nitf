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
import java.util.EnumSet;

public class NitfSymbolSegment extends AbstractNitfSegment {

    private String symbolIdentifier = null;
    private String symbolName = null;
    private String symbolType = null;
    private int numberOfLinesPerSymbol = 0;
    private int numberofPixelsPerLine = 0;
    private int lineWidth = 0;
    private int numberOfBitsPerPixel = 0;
    private int symbolDisplayLevel = 0;
    private int symbolAttachmentLevel = 0;
    private int symbolLocationRow = 0;
    private int symbolLocationColumn = 0;
//     private int boundingBox1Row = 0;
//     private int boundingBox1Column = 0;
//     private GraphicColour graphicColour = GraphicColour.UNKNOWN;
//     private int boundingBox2Row = 0;
//     private int boundingBox2Column = 0;
    private byte[] data = null;

    private NitfSecurityMetadata securityMetadata = null;

    public NitfSymbolSegment() {
    }

    public final void parse(final NitfReader nitfReader, final int symbolLength, final EnumSet<ParseOption> parseOptions) throws ParseException {
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

    public final void setSymbolType(final String type) {
        symbolType = type;
    }

    public final String getSymbolType() {
        return symbolType;
    }

    public final void setNumberOfLinesPerSymbol(final int numLinesPerSymbol) {
        numberOfLinesPerSymbol = numLinesPerSymbol;
    }

    public final void setNumberOfPixelsPerLine(final int numPixelsPerLine) {
        numberofPixelsPerLine = numPixelsPerLine;
    }

    public final void setLineWidth(final int width) {
        lineWidth = width;
    }

    public final void setNumberOfBitsPerPixel(final int bitsPerPixel) {
        numberOfBitsPerPixel = bitsPerPixel;
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

//     public final void setBoundingBox1Row(final int rowNumber) {
//         boundingBox1Row = rowNumber;
//     }
//
//     public final int getBoundingBox1Row() {
//         return boundingBox1Row;
//     }
//
//     public final void setBoundingBox1Column(final int columnNumber) {
//         boundingBox1Column = columnNumber;
//     }
//
//     public final int getBoundingBox1Column() {
//         return boundingBox1Column;
//     }
//
//     public final void setGraphicColour(final GraphicColour colour) {
//         graphicColour = colour;
//     }
//
//     public final GraphicColour getGraphicColour() {
//         return graphicColour;
//     }
//
//     public final void setBoundingBox2Row(final int rowNumber) {
//         boundingBox2Row = rowNumber;
//     }
//
//     public final int getBoundingBox2Row() {
//         return boundingBox2Row;
//     }
//
//     public final void setBoundingBox2Column(final int columnNumber) {
//         boundingBox2Column = columnNumber;
//     }
//
//     public final int getBoundingBox2Column() {
//         return boundingBox2Column;
//     }
//
    public final void setSymbolData(final byte[] symbolData) {
        data = symbolData;
    }

    public final byte[] getSymbolData() {
        return data;
    }
}
