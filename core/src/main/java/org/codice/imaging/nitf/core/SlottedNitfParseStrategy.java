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
    protected final List<NitfGraphicSegmentHeader> graphicSegments = new ArrayList<>();
    protected final List<NitfSymbolSegmentHeader> symbolSegments = new ArrayList<>();
    protected final List<NitfLabelSegmentHeader> labelSegments = new ArrayList<>();
    protected final List<NitfTextSegmentHeader> textSegments = new ArrayList<>();
    protected final List<NitfDataExtensionSegmentHeader> dataExtensionSegments = new ArrayList<>();

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
        return symbolSegments;
    }

    @Override
    public final List<NitfLabelSegmentHeader> getLabelSegments() {
        return labelSegments;
    }

    @Override
    public final List<NitfGraphicSegmentHeader> getGraphicSegments() {
        return graphicSegments;
    }

    @Override
    public final List<NitfTextSegmentHeader> getTextSegments() {
        return textSegments;
    }

    @Override
    public final List<NitfDataExtensionSegmentHeader> getDataExtensionSegments() {
        return dataExtensionSegments;
    }

    @Override
    public abstract void baseHeadersRead(final NitfReader reader);

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

    protected void readGraphicSegmentHeadersOnly(final NitfReader reader) throws ParseException {
        for (int i = 0; i < nitfFileLevelHeader.getNumberOfGraphicSegmentsLengths(); ++i) {
            NitfGraphicSegmentHeaderParser parser = new NitfGraphicSegmentHeaderParser();
            NitfGraphicSegmentHeader graphicSegment = parser.parse(reader);
            reader.skip(nitfFileLevelHeader.getLengthOfGraphicSegment(i));
            graphicSegments.add(graphicSegment);
        }
    }

    protected void readGraphicSegmentHeadersAndData(final NitfReader reader) throws ParseException {
        for (int i = 0; i < nitfFileLevelHeader.getNumberOfGraphicSegmentsLengths(); ++i) {
            NitfGraphicSegmentHeaderParser parser = new NitfGraphicSegmentHeaderParser();
            NitfGraphicSegmentHeader graphicSegment = parser.parse(reader);
            // TODO: partition this better
            int lengthOfGraphicSegmentData = nitfFileLevelHeader.getLengthOfGraphicSegment(i);
            if (lengthOfGraphicSegmentData > 0) {
                graphicSegment.setGraphicData(reader.readBytesRaw(lengthOfGraphicSegmentData));
            }
            graphicSegments.add(graphicSegment);
        }
    }

    // We reuse the Graphic Segment length values here, but generate different type
    protected void readSymbolSegmentHeadersOnly(final NitfReader reader) throws ParseException {
        for (int i = 0; i < nitfFileLevelHeader.getNumberOfGraphicSegmentsLengths(); ++i) {
            NitfSymbolSegmentHeaderParser parser = new NitfSymbolSegmentHeaderParser();
            NitfSymbolSegmentHeader symbolSegment = parser.parse(reader);
            reader.skip(nitfFileLevelHeader.getLengthOfGraphicSegment(i));
            symbolSegments.add(symbolSegment);
        }
    }

    // We reuse the Graphic Segment length values here, but generate different type
    protected void readSymbolSegmentHeadersAndData(final NitfReader reader) throws ParseException {
        for (int i = 0; i < nitfFileLevelHeader.getNumberOfGraphicSegmentsLengths(); ++i) {
            NitfSymbolSegmentHeaderParser parser = new NitfSymbolSegmentHeaderParser();
            NitfSymbolSegmentHeader symbolSegment = parser.parse(reader);
            // TODO: partition this better
            int lengthOfSymbolSegmentData = nitfFileLevelHeader.getLengthOfGraphicSegment(i);
            if (lengthOfSymbolSegmentData > 0) {
                symbolSegment.setSymbolData(reader.readBytesRaw(lengthOfSymbolSegmentData));
            }
            symbolSegments.add(symbolSegment);
        }
    }

    protected void readLabelSegmentHeadersOnly(final NitfReader reader) throws ParseException {
        for (int i = 0; i < nitfFileLevelHeader.getNumberOfLabelSegmentSubHeaderLengths(); ++i) {
            NitfLabelSegmentHeaderParser parser = new NitfLabelSegmentHeaderParser();
            NitfLabelSegmentHeader labelSegment = parser.parse(reader);
            reader.skip(nitfFileLevelHeader.getLengthOfLabelSegment(i));
            labelSegments.add(labelSegment);
        }
    }

    protected void readLabelSegmentHeadersAndData(final NitfReader reader) throws ParseException {
        for (int i = 0; i < nitfFileLevelHeader.getNumberOfLabelSegmentSubHeaderLengths(); ++i) {
            NitfLabelSegmentHeaderParser parser = new NitfLabelSegmentHeaderParser();
            NitfLabelSegmentHeader labelSegment = parser.parse(reader);
            // TODO: partition this better
            int lengthOfLabelSegmentData = nitfFileLevelHeader.getLengthOfLabelSegment(i);
            if (lengthOfLabelSegmentData > 0) {
                labelSegment.setLabelData(reader.readBytes(lengthOfLabelSegmentData));
            }
            labelSegments.add(labelSegment);
        }
    }

    protected void readTextSegmentHeadersOnly(final NitfReader reader) throws ParseException {
        for (int i = 0; i < nitfFileLevelHeader.getNumberOfTextSegmentSubHeaderLengths(); ++i) {
            NitfTextSegmentHeaderParser parser = new NitfTextSegmentHeaderParser();
            NitfTextSegmentHeader textSegment = parser.parse(reader);
            textSegments.add(textSegment);
            reader.skip(nitfFileLevelHeader.getLengthOfTextSegment(i));
        }
    }

    protected void readTextSegmentHeadersAndData(final NitfReader reader) throws ParseException {
        for (int i = 0; i < nitfFileLevelHeader.getNumberOfTextSegmentSubHeaderLengths(); ++i) {
            NitfTextSegmentHeaderParser parser = new NitfTextSegmentHeaderParser();
            NitfTextSegmentHeader textSegment = parser.parse(reader);
            // TODO: partition this better
            int lengthOfTextSegmentData = nitfFileLevelHeader.getLengthOfTextSegment(i);
            if (lengthOfTextSegmentData > 0) {
                textSegment.setTextData(reader.readBytes(lengthOfTextSegmentData));
            }
            textSegments.add(textSegment);
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
            dataExtensionSegments.add(segment);
        }
    }
}
