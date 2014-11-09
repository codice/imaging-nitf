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
 * "Slotted" parse strategy.
 * This could probably be split into
 * an abstract parent called SlottedNitfParseStrategy
 * and
 * the abstract subclass with the data storage (SlottedStorageNitfParseStrategy)
 */
public abstract class SlottedNitfParseStrategy implements NitfParseStrategy {
    /**
     * The file level header.
     */
    protected Nitf nitfFileLevelHeader;
    /**
     * The list of image segment headers.
     */
    protected final List<NitfImageSegmentHeader> imageSegmentHeaders = new ArrayList<>();
    /**
     * The list of image segment data.
     */
    protected final List<byte[]> imageSegmentData = new ArrayList<>();
    /**
     * The list of graphic segment headers.
     */
    protected final List<NitfGraphicSegmentHeader> graphicSegmentHeaders = new ArrayList<>();
    /**
     * The list of graphic segment data.
     */
    protected final List<byte[]> graphicSegmentData = new ArrayList<>();
    /**
     * The list of symbol segment headers.
     */
    protected final List<NitfSymbolSegmentHeader> symbolSegmentHeaders = new ArrayList<>();
    /**
     * The list of symbol segment data.
     */
    protected final List<byte[]> symbolSegmentData = new ArrayList<>();
    /**
     * The list of label segment headers.
     */
    protected final List<NitfLabelSegmentHeader> labelSegmentHeaders = new ArrayList<>();
    /**
     * The list of label segment data.
     */
    protected final List<String> labelSegmentData = new ArrayList<>();
    /**
     * The list of text segment headers.
     */
    protected final ArrayList<NitfTextSegmentHeader> textSegmentHeaders = new ArrayList<>();
    /**
     * The list of text segment data.
     */
    protected final ArrayList<String> textSegmentData = new ArrayList<>();
    /**
     * The list of DES headers.
     */
    protected final List<NitfDataExtensionSegmentHeader> dataExtensionSegmentHeaders = new ArrayList<>();
    /**
     * The list of DES data.
     */
    protected final List<byte[]> dataExtensionSegmentData = new ArrayList<>();

    /**
     * Constructor.
     */
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

    /**
     * Return the list of image segment data.
     *
     * @return image segment data
     */
    public final List<byte[]> getImageSegmentData() {
        return imageSegmentData;
    }

    @Override
    public final List<NitfSymbolSegmentHeader> getSymbolSegments() {
        return symbolSegmentHeaders;
    }

    /**
     * Return the list of symbol segment data.
     *
     * @return symbol segment data
     */
    public final List<byte[]> getSymbolSegmentData() {
        return symbolSegmentData;
    }

    @Override
    public final List<NitfLabelSegmentHeader> getLabelSegments() {
        return labelSegmentHeaders;
    }

    /**
     * Return list of label segment data.
     *
     * @return label segment data
     */
    public final List<String> getLabelSegmentData() {
        return labelSegmentData;
    }

    @Override
    public final List<NitfGraphicSegmentHeader> getGraphicSegments() {
        return graphicSegmentHeaders;
    }

    /**
     * Return list of graphic segment data.
     *
     * @return graphic segment data
     */
    public final List<byte[]> getGraphicSegmentData() {
        return graphicSegmentData;
    }

    @Override
    public final List<NitfTextSegmentHeader> getTextSegments() {
        return textSegmentHeaders;
    }

    /**
     * Return list of text segment data.
     *
     * @return text segment data
     */
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
    /**
     * Parse the image segment header and associated data.
     *
     * @param reader Reader to use for reading
     * @param i the index of the header / data segment to read (zero base)
     * @throws ParseException on parse error
     */
    protected final void parseImageSegmentHeaderAndData(final NitfReader reader, final int i) throws ParseException {
        NitfImageSegmentHeader imageSegmentHeader = readImageSegmentHeader(reader, i);
        readImageSegmentData(imageSegmentHeader, reader);
        imageSegmentHeaders.add(imageSegmentHeader);
    }

    /**
     * Parse the image segment header and skip over the associated data.
     *
     * @param reader Reader to use for reading
     * @param i the index of the header / data segment to read (zero base)
     * @throws ParseException on parse error
     */
    protected final void parseImageSegmentHeaderButSkipData(final NitfReader reader, final int i) throws ParseException {
        NitfImageSegmentHeader imageSegmentHeader = readImageSegmentHeader(reader, i);
        skipImageSegmentData(imageSegmentHeader, reader);
        imageSegmentHeaders.add(imageSegmentHeader);
    }

    /**
     * Parse the image segment header.
     *
     * The reader will be positioned at the start of the associated data segment.
     *
     * @param reader Reader to use for reading
     * @param i the index of the header to read (zero base)
     * @return the segment header data
     * @throws ParseException on parse error
     */
    protected final NitfImageSegmentHeader readImageSegmentHeader(final NitfReader reader, final int i) throws ParseException {
        NitfImageSegmentHeaderParser imageSegmentHeaderParser = new NitfImageSegmentHeaderParser();
        NitfImageSegmentHeader imageSegmentHeader = imageSegmentHeaderParser.parse(reader);
        imageSegmentHeader.setImageSegmentDataLength(nitfFileLevelHeader.getImageSegmentDataLengths().get(i));
        return imageSegmentHeader;
    }

    /**
     * Read the image segment data.
     *
     * The reader is assumed to be positioned at the end of the segment header before this call, and will be positioned
     * at the start of the next header after this call.
     *
     * @param imageSegmentHeader the header for the image segment that is to be read
     * @param reader the reader to use to read the data.
     * @throws ParseException on failure.
     */
    protected final void readImageSegmentData(final NitfImageSegmentHeader imageSegmentHeader, final NitfReader reader) throws ParseException {
        if (imageSegmentHeader.getImageDataLength() > 0) {
            imageSegmentData.add(reader.readBytesRaw((int) imageSegmentHeader.getImageDataLength()));
        }
    }

    /**
     * Skip the image segment data.
     *
     * The reader is assumed to be positioned at the end of the segment before this call, and will be positioned
     * at the start of the next header after this call.
     *
     * @param imageSegmentHeader the header for the image segment that is to be skipped
     * @param reader the reader to use to skip the data.
     * @throws ParseException on failure.
     */
    protected final void skipImageSegmentData(final NitfImageSegmentHeader imageSegmentHeader, final NitfReader reader) throws ParseException {
        if (imageSegmentHeader.getImageDataLength() > 0) {
            reader.skip(imageSegmentHeader.getImageDataLength());
        }
    }

    // Graphic segment methods
    /**
     * Parse the graphic segment header and associated data.
     *
     * @param reader Reader to use for reading
     * @param i the index of the header / data segment to read (zero base)
     * @throws ParseException on parse error
     */
    protected final void parseGraphicSegmentHeaderAndData(final NitfReader reader, final int i) throws ParseException {
        NitfGraphicSegmentHeader graphicSegmentHeader = readGraphicSegmentHeader(reader, i);
        readGraphicSegmentData(graphicSegmentHeader, reader);
        graphicSegmentHeaders.add(graphicSegmentHeader);
    }

    /**
     * Parse the graphic segment header and skip over the associated data.
     *
     * @param reader Reader to use for reading
     * @param i the index of the header / data segment to read (zero base)
     * @throws ParseException on parse error
     */
    protected final void parseGraphicSegmentHeaderButSkipData(final NitfReader reader, final int i) throws ParseException {
        NitfGraphicSegmentHeader graphicSegmentHeader = readGraphicSegmentHeader(reader, i);
        skipGraphicSegmentData(graphicSegmentHeader, reader);
        graphicSegmentHeaders.add(graphicSegmentHeader);
    }

    /**
     * Parse the graphic segment header.
     *
     * The reader will be positioned at the start of the associated data segment.
     *
     * @param reader Reader to use for reading
     * @param i the index of the header to read (zero base)
     * @return the segment header data
     * @throws ParseException on parse error
     */
    protected final NitfGraphicSegmentHeader readGraphicSegmentHeader(final NitfReader reader, final int i) throws ParseException {
        NitfGraphicSegmentHeaderParser graphicSegmentHeaderParser = new NitfGraphicSegmentHeaderParser();
        NitfGraphicSegmentHeader graphicSegmentHeader = graphicSegmentHeaderParser.parse(reader);
        graphicSegmentHeader.setGraphicSegmentDataLength(nitfFileLevelHeader.getGraphicSegmentDataLengths().get(i));
        return graphicSegmentHeader;
    }

    /**
     * Read the graphic segment data.
     *
     * The reader is assumed to be positioned at the end of the segment header before this call, and will be positioned
     * at the start of the next header after this call.
     *
     * @param graphicSegmentHeader the header for the graphic segment that is to be read
     * @param reader the reader to use to read the data.
     * @throws ParseException on failure.
     */
    protected final void readGraphicSegmentData(final NitfGraphicSegmentHeader graphicSegmentHeader, final NitfReader reader) throws ParseException {
        if (graphicSegmentHeader.getGraphicDataLength() > 0) {
            graphicSegmentData.add(reader.readBytesRaw(graphicSegmentHeader.getGraphicDataLength()));
        }
    }

    /**
     * Skip the graphic segment data.
     *
     * The reader is assumed to be positioned at the end of the segment before this call, and will be positioned
     * at the start of the next header after this call.
     *
     * @param graphicSegmentHeader the header for the graphic segment that is to be skipped
     * @param reader the reader to use to skip the data.
     * @throws ParseException on failure.
     */
    protected final void skipGraphicSegmentData(final NitfGraphicSegmentHeader graphicSegmentHeader, final NitfReader reader) throws ParseException {
        if (graphicSegmentHeader.getGraphicDataLength() > 0) {
            reader.skip(graphicSegmentHeader.getGraphicDataLength());
        }
    }

    // Symbol segment methods
    /**
     * Parse the symbol segment header and associated data.
     *
     * @param reader Reader to use for reading
     * @param i the index of the header / data segment to read (zero base)
     * @throws ParseException on parse error
     */
    protected final void parseSymbolSegmentHeaderAndData(final NitfReader reader, final int i) throws ParseException {
        NitfSymbolSegmentHeader symbolSegmentHeader = readSymbolSegmentHeader(reader, i);
        readSymbolSegmentData(symbolSegmentHeader, reader);
        symbolSegmentHeaders.add(symbolSegmentHeader);
    }

    /**
     * Parse the symbol segment header and skip over the associated data.
     *
     * @param reader Reader to use for reading
     * @param i the index of the header / data segment to read (zero base)
     * @throws ParseException on parse error
     */
    protected final void parseSymbolSegmentHeaderButSkipData(final NitfReader reader, final int i) throws ParseException {
        NitfSymbolSegmentHeader symbolSegmentHeader = readSymbolSegmentHeader(reader, i);
        skipSymbolSegmentData(symbolSegmentHeader, reader);
        symbolSegmentHeaders.add(symbolSegmentHeader);
    }

    /**
     * Parse the symbol segment header.
     *
     * The reader will be positioned at the start of the associated data segment.
     *
     * @param reader Reader to use for reading
     * @param i the index of the header to read (zero base)
     * @return the segment header data
     * @throws ParseException on parse error
     */
    protected final NitfSymbolSegmentHeader readSymbolSegmentHeader(final NitfReader reader, final int i) throws ParseException {
        NitfSymbolSegmentHeaderParser symbolSegmentHeaderParser = new NitfSymbolSegmentHeaderParser();
        NitfSymbolSegmentHeader symbolSegmentHeader = symbolSegmentHeaderParser.parse(reader);
        symbolSegmentHeader.setSymbolSegmentDataLength(nitfFileLevelHeader.getSymbolSegmentDataLengths().get(i));
        return symbolSegmentHeader;
    }

    /**
     * Read the symbol segment data.
     *
     * The reader is assumed to be positioned at the end of the segment header before this call, and will be positioned
     * at the start of the next header after this call.
     *
     * @param symbolSegmentHeader the header for the symbol segment that is to be read
     * @param reader the reader to use to read the data.
     * @throws ParseException on failure.
     */
    protected final void readSymbolSegmentData(final NitfSymbolSegmentHeader symbolSegmentHeader, final NitfReader reader) throws ParseException {
        if (symbolSegmentHeader.getSymbolDataLength() > 0) {
            symbolSegmentData.add(reader.readBytesRaw(symbolSegmentHeader.getSymbolDataLength()));
        }
    }

    /**
     * Skip the symbol segment data.
     *
     * The reader is assumed to be positioned at the end of the segment before this call, and will be positioned
     * at the start of the next header after this call.
     *
     * @param symbolSegmentHeader the header for the symbol segment that is to be skipped
     * @param reader the reader to use to skip the data.
     * @throws ParseException on failure.
     */
    protected final void skipSymbolSegmentData(final NitfSymbolSegmentHeader symbolSegmentHeader, final NitfReader reader) throws ParseException {
        if (symbolSegmentHeader.getSymbolDataLength() > 0) {
            reader.skip(symbolSegmentHeader.getSymbolDataLength());
        }
    }

    // Label segment methods
    /**
     * Parse the label segment header and associated data.
     *
     * @param reader Reader to use for reading
     * @param i the index of the header / data segment to read (zero base)
     * @throws ParseException on parse error
     */
    protected final void parseLabelSegmentHeaderAndData(final NitfReader reader, final int i) throws ParseException {
        NitfLabelSegmentHeader labelSegmentHeader = readLabelSegmentHeader(reader, i);
        readLabelSegmentData(labelSegmentHeader, reader);
        labelSegmentHeaders.add(labelSegmentHeader);
    }

    /**
     * Parse the label segment header and skip over the associated data.
     *
     * @param reader Reader to use for reading
     * @param i the index of the header / data segment to read (zero base)
     * @throws ParseException on parse error
     */
    protected final void parseLabelSegmentHeaderButSkipData(final NitfReader reader, final int i) throws ParseException {
        NitfLabelSegmentHeader labelSegmentHeader = readLabelSegmentHeader(reader, i);
        skipLabelSegmentData(labelSegmentHeader, reader);
        labelSegmentHeaders.add(labelSegmentHeader);
    }

    /**
     * Parse the label segment header.
     *
     * The reader will be positioned at the start of the associated data segment.
     *
     * @param reader Reader to use for reading
     * @param i the index of the header to read (zero base)
     * @return the segment header data
     * @throws ParseException on parse error
     */
    protected final NitfLabelSegmentHeader readLabelSegmentHeader(final NitfReader reader, final int i) throws ParseException {
        NitfLabelSegmentHeaderParser labelSegmentHeaderParser = new NitfLabelSegmentHeaderParser();
        NitfLabelSegmentHeader labelSegmentHeader = labelSegmentHeaderParser.parse(reader);
        labelSegmentHeader.setLabelSegmentDataLength(nitfFileLevelHeader.getLabelSegmentDataLengths().get(i));
        return labelSegmentHeader;
    }

    /**
     * Read the label segment data.
     *
     * The reader is assumed to be positioned at the end of the segment header before this call, and will be positioned
     * at the start of the next header after this call.
     *
     * @param labelSegmentHeader the header for the label segment that is to be read
     * @param reader the reader to use to read the data.
     * @throws ParseException on failure.
     */
    protected final void readLabelSegmentData(final NitfLabelSegmentHeader labelSegmentHeader, final NitfReader reader) throws ParseException {
        if (labelSegmentHeader.getLabelDataLength() > 0) {
            labelSegmentData.add(reader.readBytes(labelSegmentHeader.getLabelDataLength()));
        }
    }

    /**
     * Skip the label segment data.
     *
     * The reader is assumed to be positioned at the end of the segment header before this call, and will be positioned
     * at the start of the next header after this call.
     *
     * @param labelSegmentHeader the header for the label segment that is to be skipped
     * @param reader the reader to use to skip the data.
     * @throws ParseException on failure.
     */
    protected final void skipLabelSegmentData(final NitfLabelSegmentHeader labelSegmentHeader, final NitfReader reader) throws ParseException {
        if (labelSegmentHeader.getLabelDataLength() > 0) {
            reader.skip(labelSegmentHeader.getLabelDataLength());
        }
    }

    // Text segment methods
    /**
     * Parse the text segment header and associated data.
     *
     * @param reader Reader to use for reading
     * @param i the index of the header / data segment to read (zero base)
     * @throws ParseException on parse error
     */
    protected final void parseTextSegmentHeaderAndData(final NitfReader reader, final int i) throws ParseException {
        NitfTextSegmentHeader textSegmentHeader = readTextSegmentHeader(reader, i);
        readTextSegmentData(textSegmentHeader, reader);
        textSegmentHeaders.add(textSegmentHeader);
    }

    /**
     * Parse the text segment header and skip over the associated data.
     *
     * @param reader Reader to use for reading
     * @param i the index of the header / data segment to read (zero base)
     * @throws ParseException on parse error
     */
    protected final void parseTextSegmentHeaderButSkipData(final NitfReader reader, final int i) throws ParseException {
        NitfTextSegmentHeader textSegmentHeader = readTextSegmentHeader(reader, i);
        skipTextSegmentData(textSegmentHeader, reader);
        textSegmentHeaders.add(textSegmentHeader);
    }

    /**
     * Parse the text segment header.
     *
     * The reader will be positioned at the start of the associated data segment.
     *
     * @param reader Reader to use for reading
     * @param i the index of the header to read (zero base)
     * @return the segment header data
     * @throws ParseException on parse error
     */
    protected final NitfTextSegmentHeader readTextSegmentHeader(final NitfReader reader, final int i) throws ParseException {
        NitfTextSegmentHeaderParser textSegmentHeaderParser = new NitfTextSegmentHeaderParser();
        NitfTextSegmentHeader textSegmentHeader = textSegmentHeaderParser.parse(reader);
        textSegmentHeader.setTextSegmentDataLength(nitfFileLevelHeader.getTextSegmentDataLengths().get(i));
        return textSegmentHeader;
    }

    /**
     * Read the text segment data.
     *
     * The reader is assumed to be positioned at the end of the segment header before this call, and will be positioned
     * at the start of the next header after this call.
     *
     * @param textSegmentHeader the header for the text segment that is to be read
     * @param reader the reader to use to read the data.
     * @throws ParseException on failure.
     */
    protected final void readTextSegmentData(final NitfTextSegmentHeader textSegmentHeader, final NitfReader reader) throws ParseException {
        if (textSegmentHeader.getTextDataLength() > 0) {
            textSegmentData.add(reader.readBytes(textSegmentHeader.getTextDataLength()));
        }
    }

    /**
     * Skip the text segment data.
     *
     * The reader is assumed to be positioned at the end of the segment before this call, and will be positioned
     * at the start of the next header after this call.
     *
     * @param textSegmentHeader the header for the text segment that is to be skipped
     * @param reader the reader to use to skip the data.
     * @throws ParseException on failure.
     */
    protected final void skipTextSegmentData(final NitfTextSegmentHeader textSegmentHeader, final NitfReader reader) throws ParseException {
        if (textSegmentHeader.getTextDataLength() > 0) {
            reader.skip(textSegmentHeader.getTextDataLength());
        }
    }

    // Data extension segment methods
    /**
     * Parse the data extension segment header and associated data.
     *
     * @param reader Reader to use for reading
     * @param i the index of the header / data segment to read (zero base)
     * @throws ParseException on parse error
     */
    protected final void parseDataExtensionSegmentHeaderAndData(final NitfReader reader, final int i) throws ParseException {
        NitfDataExtensionSegmentHeader dataExtensionSegmentHeader = readDataExtensionSegmentHeader(reader, i);
        readDataExtensionSegmentData(dataExtensionSegmentHeader, reader);
        dataExtensionSegmentHeaders.add(dataExtensionSegmentHeader);
    }

    /**
     * Parse the data extension segment header and skip over the associated data.
     *
     * @param reader Reader to use for reading
     * @param i the index of the header / data segment to read (zero base)
     * @throws ParseException on parse error
     */
    protected final void parseDataExtensionSegmentHeaderButSkipData(final NitfReader reader, final int i) throws ParseException {
        NitfDataExtensionSegmentHeader dataExtensionSegmentHeader = readDataExtensionSegmentHeader(reader, i);
        skipDataExtensionSegmentData(dataExtensionSegmentHeader, reader);
        dataExtensionSegmentHeaders.add(dataExtensionSegmentHeader);
    }

    /**
     * Parse the data extension segment header.
     *
     * The reader will be positioned at the start of the associated data segment.
     *
     * @param reader Reader to use for reading
     * @param i the index of the header to read (zero base)
     * @return the segment header data
     * @throws ParseException on parse error
     */
    protected final NitfDataExtensionSegmentHeader readDataExtensionSegmentHeader(final NitfReader reader, final int i) throws ParseException {
        NitfDataExtensionSegmentHeaderParser dataExtensionSegmentHeaderParser = new NitfDataExtensionSegmentHeaderParser();
        NitfDataExtensionSegmentHeader dataExtensionSegmentHeader = dataExtensionSegmentHeaderParser.parse(reader);
        dataExtensionSegmentHeader.setDataExtensionSegmentDataLength(nitfFileLevelHeader.getDataExtensionSegmentDataLengths().get(i));
        return dataExtensionSegmentHeader;
    }

    /**
     * Read the data extension segment data.
     *
     * The reader is assumed to be positioned at the end of the segment header before this call, and will be positioned
     * at the start of the next header after this call.
     *
     * @param dataExtensionSegmentHeader the header for the data extension segment that is to be read
     * @param reader the reader to use to read the data.
     * @throws ParseException on failure.
     */
    protected final void readDataExtensionSegmentData(final NitfDataExtensionSegmentHeader dataExtensionSegmentHeader,
            final NitfReader reader) throws ParseException {
        if (dataExtensionSegmentHeader.getDataExtensionSegmentDataLength() > 0) {
            if (dataExtensionSegmentHeader.isTreOverflow(reader.getFileType())) {
                TreCollectionParser treCollectionParser = new TreCollectionParser();
                TreCollection overflowTres = treCollectionParser.parse(reader, dataExtensionSegmentHeader.getDataExtensionSegmentDataLength());
                dataExtensionSegmentHeader.mergeTREs(overflowTres);
            } else if (!"STREAMING_FILE_HEADER".equals(dataExtensionSegmentHeader.getIdentifier().trim())) {
                dataExtensionSegmentData.add(reader.readBytesRaw(dataExtensionSegmentHeader.getDataExtensionSegmentDataLength()));
            }
        }
    }

    /**
     * Skip the data extension segment data.
     *
     * The reader is assumed to be positioned at the end of the segment before this call, and will be positioned
     * at the start of the next header after this call.
     *
     * @param dataExtensionSegmentHeader the header for the data extension segment that is to be skipped
     * @param reader the reader to use to skip the data.
     * @throws ParseException on failure.
     */
    protected final void skipDataExtensionSegmentData(final NitfDataExtensionSegmentHeader dataExtensionSegmentHeader,
            final NitfReader reader) throws ParseException {
        if (dataExtensionSegmentHeader.getDataExtensionSegmentDataLength() > 0) {
            reader.skip(dataExtensionSegmentHeader.getDataExtensionSegmentDataLength());
        }
    }

//    /**
//     * TODO: this needs to get fixed.
//     *
//     * @param reader the reader to read from
//     * @throws ParseException on parse error.
//     */
//    protected final void readDataExtensionSegments(final NitfReader reader) throws ParseException {
//        for (int i = 0; i < nitfFileLevelHeader.getDataExtensionSegmentSubHeaderLengths().size(); ++i) {
//            NitfDataExtensionSegmentHeaderParser parser = new NitfDataExtensionSegmentHeaderParser();
//            NitfDataExtensionSegmentHeader segment = parser.parse(reader);
//            // TODO: partition properly
//            int lengthOfDataExtensionSegmentData = nitfFileLevelHeader.getDataExtensionSegmentDataLengths().get(i);
//            if (segment.isTreOverflow(reader.getFileType())) {
//                TreCollectionParser treCollectionParser = new TreCollectionParser();
//                TreCollection overflowTres = treCollectionParser.parse(reader, lengthOfDataExtensionSegmentData);
//                segment.mergeTREs(overflowTres);
//            } else {
//                segment.setData(reader.readBytesRaw(lengthOfDataExtensionSegmentData));
//            }
//            dataExtensionSegmentHeaders.add(segment);
//        }
//    }
}
