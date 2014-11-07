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
package org.codice.imaging.nitf.core;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO: make public.
 * This could probably be split into
 * an abstract parent called SlottedNitfParseStrategy
 * and
 * the abstract subclass with the data storage (SlottedStorageNitfParseStrategy)
 */
abstract class SlottedNitfParseStrategy implements NitfParseStrategy {
    protected Nitf nitfFileLevelHeader;
    protected final List<NitfImageSegmentHeader> imageSegmentHeaders = new ArrayList<>();
    protected final List<byte[]> imageSegmentData = new ArrayList<>();
    protected final List<NitfGraphicSegmentHeader> graphicSegmentHeaders = new ArrayList<>();
    protected final List<byte[]> graphicSegmentData = new ArrayList<>();
    protected final List<NitfSymbolSegmentHeader> symbolSegmentHeaders = new ArrayList<>();
    protected final List<byte[]> symbolSegmentData = new ArrayList<>();
    protected final List<NitfLabelSegmentHeader> labelSegmentHeaders = new ArrayList<>();
    protected final List<String> labelSegmentData = new ArrayList<>();
    protected final List<NitfTextSegmentHeader> textSegmentHeaders = new ArrayList<>();
    protected final List<String> textSegmentData = new ArrayList<>();
    protected final List<NitfDataExtensionSegmentHeader> dataExtensionSegmentHeaders = new ArrayList<>();

    public SlottedNitfParseStrategy() {
    }

    @Override
    public final void setFileHeader(final Nitf nitf) {
        nitfFileLevelHeader = nitf;
    }

    @Override
    public final Nitf getNitfHeader() {
        return nitfFileLevelHeader;
    }

    @Override
    public final List<NitfImageSegmentHeader> getImageSegmentHeaders() {
        return imageSegmentHeaders;
    }

    public final List<byte[]> getImageSegmentData() {
        return imageSegmentData;
    }

    @Override
    public final List<NitfSymbolSegmentHeader> getSymbolSegments() {
        return symbolSegmentHeaders;
    }

    public final List<byte[]> getSymbolSegmentData() {
        return symbolSegmentData;
    }

    @Override
    public final List<NitfLabelSegmentHeader> getLabelSegments() {
        return labelSegmentHeaders;
    }

    public final List<String> getLabelSegmentData() {
        return labelSegmentData;
    }

    @Override
    public final List<NitfGraphicSegmentHeader> getGraphicSegments() {
        return graphicSegmentHeaders;
    }

    public final List<byte[]> getGraphicSegmentData() {
        return graphicSegmentData;
    }

    @Override
    public final List<NitfTextSegmentHeader> getTextSegments() {
        return textSegmentHeaders;
    }

    public final List<String> getTextSegmentData() {
        return textSegmentData;
    }

    @Override
    public final List<NitfDataExtensionSegmentHeader> getDataExtensionSegments() {
        return dataExtensionSegmentHeaders;
    }

    @Override
    public abstract void baseHeadersRead(final NitfReader reader);

    // Image segment methods
    protected void parseImageSegmentHeaderAndData(final NitfReader reader, final int i) throws ParseException {
        NitfImageSegmentHeader imageSegmentHeader = readImageSegmentHeader(reader, i);
        readImageSegmentData(imageSegmentHeader, reader);
        imageSegmentHeaders.add(imageSegmentHeader);
    }

    protected void parseImageSegmentHeaderButSkipData(final NitfReader reader, final int i) throws ParseException {
        NitfImageSegmentHeader imageSegmentHeader = readImageSegmentHeader(reader, i);
        skipImageSegmentData(imageSegmentHeader, reader);
        imageSegmentHeaders.add(imageSegmentHeader);
    }

    protected NitfImageSegmentHeader readImageSegmentHeader(final NitfReader reader, final int i) throws ParseException {
        NitfImageSegmentHeaderParser imageSegmentHeaderParser = new NitfImageSegmentHeaderParser();
        NitfImageSegmentHeader imageSegmentHeader = imageSegmentHeaderParser.parse(reader);
        imageSegmentHeader.setImageSegmentDataLength(nitfFileLevelHeader.getLengthOfImageSegment(i));
        return imageSegmentHeader;
    }

    protected void readImageSegmentData(final NitfImageSegmentHeader imageSegment, final NitfReader reader) throws ParseException {
        if (imageSegment.getImageDataLength() > 0) {
            imageSegmentData.add(reader.readBytesRaw((int) imageSegment.getImageDataLength()));
        }
    }

    protected void skipImageSegmentData(final NitfImageSegmentHeader imageSegment, final NitfReader reader) throws ParseException {
        if (imageSegment.getImageDataLength() > 0) {
            reader.skip(imageSegment.getImageDataLength());
        }
    }

    // Graphic segment methods
    protected void parseGraphicSegmentHeaderAndData(final NitfReader reader, final int i) throws ParseException {
        NitfGraphicSegmentHeader graphicSegmentHeader = readGraphicSegmentHeader(reader, i);
        readGraphicSegmentData(graphicSegmentHeader, reader);
        graphicSegmentHeaders.add(graphicSegmentHeader);
    }

    protected void parseGraphicSegmentHeaderButSkipData(final NitfReader reader, final int i) throws ParseException {
        NitfGraphicSegmentHeader graphicSegmentHeader = readGraphicSegmentHeader(reader, i);
        skipGraphicSegmentData(graphicSegmentHeader, reader);
        graphicSegmentHeaders.add(graphicSegmentHeader);
    }

    protected NitfGraphicSegmentHeader readGraphicSegmentHeader(final NitfReader reader, final int i) throws ParseException {
        NitfGraphicSegmentHeaderParser graphicSegmentHeaderParser = new NitfGraphicSegmentHeaderParser();
        NitfGraphicSegmentHeader graphicSegmentHeader = graphicSegmentHeaderParser.parse(reader);
        graphicSegmentHeader.setGraphicSegmentDataLength(nitfFileLevelHeader.getLengthOfGraphicSegment(i));
        return graphicSegmentHeader;
    }

    protected void readGraphicSegmentData(final NitfGraphicSegmentHeader graphicSegment, final NitfReader reader) throws ParseException {
        if (graphicSegment.getGraphicDataLength() > 0) {
            graphicSegmentData.add(reader.readBytesRaw(graphicSegment.getGraphicDataLength()));
        }
    }

    protected void skipGraphicSegmentData(final NitfGraphicSegmentHeader graphicSegment, final NitfReader reader) throws ParseException {
        if (graphicSegment.getGraphicDataLength() > 0) {
            reader.skip(graphicSegment.getGraphicDataLength());
        }
    }

    // Symbol segment methods
    protected void parseSymbolSegmentHeaderAndData(final NitfReader reader, final int i) throws ParseException {
        NitfSymbolSegmentHeader symbolSegmentHeader = readSymbolSegmentHeader(reader, i);
        readSymbolSegmentData(symbolSegmentHeader, reader);
        symbolSegmentHeaders.add(symbolSegmentHeader);
    }

    protected void parseSymbolSegmentHeaderButSkipData(final NitfReader reader, final int i) throws ParseException {
        NitfSymbolSegmentHeader symbolSegmentHeader = readSymbolSegmentHeader(reader, i);
        skipSymbolSegmentData(symbolSegmentHeader, reader);
        symbolSegmentHeaders.add(symbolSegmentHeader);
    }

    protected NitfSymbolSegmentHeader readSymbolSegmentHeader(final NitfReader reader, final int i) throws ParseException {
        NitfSymbolSegmentHeaderParser symbolSegmentHeaderParser = new NitfSymbolSegmentHeaderParser();
        NitfSymbolSegmentHeader symbolSegmentHeader = symbolSegmentHeaderParser.parse(reader);
        symbolSegmentHeader.setSymbolSegmentDataLength(nitfFileLevelHeader.getLengthOfSymbolSegment(i));
        return symbolSegmentHeader;
    }

    protected void readSymbolSegmentData(final NitfSymbolSegmentHeader symbolSegment, final NitfReader reader) throws ParseException {
        if (symbolSegment.getSymbolDataLength() > 0) {
            symbolSegmentData.add(reader.readBytesRaw(symbolSegment.getSymbolDataLength()));
        }
    }

    protected void skipSymbolSegmentData(final NitfSymbolSegmentHeader symbolSegment, final NitfReader reader) throws ParseException {
        if (symbolSegment.getSymbolDataLength() > 0) {
            reader.skip(symbolSegment.getSymbolDataLength());
        }
    }

    // Label segment methods
    protected void parseLabelSegmentHeaderAndData(final NitfReader reader, final int i) throws ParseException {
        NitfLabelSegmentHeader labelSegmentHeader = readLabelSegmentHeader(reader, i);
        readLabelSegmentData(labelSegmentHeader, reader);
        labelSegmentHeaders.add(labelSegmentHeader);
    }

    protected void parseLabelSegmentHeaderButSkipData(final NitfReader reader, final int i) throws ParseException {
        NitfLabelSegmentHeader labelSegmentHeader = readLabelSegmentHeader(reader, i);
        skipLabelSegmentData(labelSegmentHeader, reader);
        labelSegmentHeaders.add(labelSegmentHeader);
    }

    protected NitfLabelSegmentHeader readLabelSegmentHeader(final NitfReader reader, final int i) throws ParseException {
        NitfLabelSegmentHeaderParser labelSegmentHeaderParser = new NitfLabelSegmentHeaderParser();
        NitfLabelSegmentHeader labelSegmentHeader = labelSegmentHeaderParser.parse(reader);
        labelSegmentHeader.setLabelSegmentDataLength(nitfFileLevelHeader.getLengthOfLabelSegment(i));
        return labelSegmentHeader;
    }

    protected void readLabelSegmentData(final NitfLabelSegmentHeader labelSegment, final NitfReader reader) throws ParseException {
        if (labelSegment.getLabelDataLength() > 0) {
            labelSegmentData.add(reader.readBytes(labelSegment.getLabelDataLength()));
        }
    }

    protected void skipLabelSegmentData(final NitfLabelSegmentHeader labelSegment, final NitfReader reader) throws ParseException {
        if (labelSegment.getLabelDataLength() > 0) {
            reader.skip(labelSegment.getLabelDataLength());
        }
    }

    // Text segment methods
    protected void parseTextSegmentHeaderAndData(final NitfReader reader, final int i) throws ParseException {
        NitfTextSegmentHeader textSegmentHeader = readTextSegmentHeader(reader, i);
        readTextSegmentData(textSegmentHeader, reader);
        textSegmentHeaders.add(textSegmentHeader);
    }

    protected void parseTextSegmentHeaderButSkipData(final NitfReader reader, final int i) throws ParseException {
        NitfTextSegmentHeader textSegmentHeader = readTextSegmentHeader(reader, i);
        skipTextSegmentData(textSegmentHeader, reader);
        textSegmentHeaders.add(textSegmentHeader);
    }

    protected NitfTextSegmentHeader readTextSegmentHeader(final NitfReader reader, final int i) throws ParseException {
        NitfTextSegmentHeaderParser textSegmentHeaderParser = new NitfTextSegmentHeaderParser();
        NitfTextSegmentHeader textSegmentHeader = textSegmentHeaderParser.parse(reader);
        textSegmentHeader.setTextSegmentDataLength(nitfFileLevelHeader.getLengthOfTextSegment(i));
        return textSegmentHeader;
    }

    protected void readTextSegmentData(final NitfTextSegmentHeader textSegment, final NitfReader reader) throws ParseException {
        if (textSegment.getTextDataLength() > 0) {
            textSegmentData.add(reader.readBytes(textSegment.getTextDataLength()));
        }
    }

    protected void skipTextSegmentData(final NitfTextSegmentHeader textSegment, final NitfReader reader) throws ParseException {
        if (textSegment.getTextDataLength() > 0) {
            reader.skip(textSegment.getTextDataLength());
        }
    }

    protected void readDataExtensionSegments(final NitfReader reader) throws ParseException {
        for (int i = 0; i < nitfFileLevelHeader.getNumberOfDataExtensionSegmentSubHeaderLengths(); ++i) {
            NitfDataExtensionSegmentHeaderParser parser = new NitfDataExtensionSegmentHeaderParser();
            NitfDataExtensionSegmentHeader segment = parser.parse(reader);
            // TODO: partition properly
            int lengthOfDataExtensionSegmentData = nitfFileLevelHeader.getLengthOfDataExtensionSegment(i);
            if (segment.isTreOverflow(reader.getFileType())) {
                TreCollectionParser treCollectionParser = new TreCollectionParser();
                TreCollection overflowTres = treCollectionParser.parse(reader, lengthOfDataExtensionSegmentData);
                segment.mergeTREs(overflowTres);
            } else {
                segment.setData(reader.readBytesRaw(lengthOfDataExtensionSegmentData));
            }
            dataExtensionSegmentHeaders.add(segment);
        }
    }
}
