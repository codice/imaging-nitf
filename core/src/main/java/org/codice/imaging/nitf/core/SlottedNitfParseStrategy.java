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

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.ParseException;

import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.MemoryCacheImageInputStream;
import javax.xml.transform.Source;

import org.codice.imaging.nitf.core.common.FileType;
import org.codice.imaging.nitf.core.common.NitfParseStrategy;
import org.codice.imaging.nitf.core.common.NitfReader;
import org.codice.imaging.nitf.core.dataextension.DataExtensionSegment;
import org.codice.imaging.nitf.core.dataextension.DataExtensionSegmentParser;
import org.codice.imaging.nitf.core.graphic.GraphicSegment;
import org.codice.imaging.nitf.core.graphic.GraphicSegmentParser;
import org.codice.imaging.nitf.core.image.ImageSegment;
import org.codice.imaging.nitf.core.image.ImageSegmentParser;
import org.codice.imaging.nitf.core.label.LabelSegment;
import org.codice.imaging.nitf.core.label.LabelSegmentParser;
import org.codice.imaging.nitf.core.symbol.SymbolSegment;
import org.codice.imaging.nitf.core.symbol.SymbolSegmentParser;
import org.codice.imaging.nitf.core.text.TextSegment;
import org.codice.imaging.nitf.core.text.TextSegmentParser;
import org.codice.imaging.nitf.core.tre.TreCollection;
import org.codice.imaging.nitf.core.tre.TreCollectionParser;
import org.codice.imaging.nitf.core.tre.TreSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * "Slotted" parse strategy.
 */
public abstract class SlottedNitfParseStrategy implements NitfParseStrategy {
    private static final Logger LOGGER = LoggerFactory.getLogger(SlottedNitfParseStrategy.class);

    private HeapStrategy<ImageInputStream> imageHeapStrategy =
            new InMemoryHeapStrategy<ImageInputStream>((InputStream is) -> new MemoryCacheImageInputStream(is));

    /**
     * The Stores the NITF data.
     */
    protected SlottedNitfStorage nitfStorage = new SlottedNitfStorage();

    /**
     * The TRE parser to use. Must be initialised before use, see initialiseTreCollectionParserIfRequired()
     */
    protected TreCollectionParser treCollectionParser;

    /**
     * Constructor.
     */
    public SlottedNitfParseStrategy() {
        treCollectionParser = null;
    }

    /**
     *
     * @param dataStrategy the HeapStrategy to use for this parser's image storage.  If null,
     *                          then this class will use an InMemoryHeapStrategy instance.
     */
    public final void setImageHeapStrategy(final HeapStrategy dataStrategy) {
        if (dataStrategy != null) {
            this.imageHeapStrategy = dataStrategy;
        }
    }

    @Override
    public final void setFileHeader(final NitfFileHeader nitfFileHeader) {
        nitfStorage.setNitfHeader(nitfFileHeader);
    }

    /**
     *
     * @return the NitfFileHeader.
     */
    public final NitfFileHeader getNitfHeader() {
        return nitfStorage.getNitfHeader();
    }

    /**
     *
     * @return a NitfDataSource containing the parsed NITF.
     */
    public final NitfDataSource getNitfDataSource() {
        return this.nitfStorage;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void baseHeadersRead(final NitfReader reader) {
        NitfFileHeader nitfFileLevelHeader = nitfStorage.getNitfHeader();

        try {
            for (int i = 0; i < nitfFileLevelHeader.getImageSegmentSubHeaderLengths().size(); ++i) {
                handleImageSegment(reader, i);
            }
            if (nitfFileLevelHeader.getFileType() == FileType.NITF_TWO_ZERO) {
                for (int i = 0; i < nitfFileLevelHeader.getSymbolSegmentSubHeaderLengths().size(); ++i) {
                    handleSymbolSegment(reader, i);
                }
                for (int i = 0; i < nitfFileLevelHeader.getLabelSegmentSubHeaderLengths().size(); ++i) {
                   handleLabelSegment(reader, i);
                }
            } else {
                for (int i = 0; i < nitfFileLevelHeader.getGraphicSegmentSubHeaderLengths().size(); ++i) {
                   handleGraphicSegment(reader, i);
                }
            }
            for (int i = 0; i < nitfFileLevelHeader.getTextSegmentSubHeaderLengths().size(); ++i) {
                handleTextSegment(reader, i);
            }
            for (int i = 0; i < nitfFileLevelHeader.getDataExtensionSegmentSubHeaderLengths().size(); ++i) {
                handleDataExtensionSegment(reader, i);
            }
        } catch (ParseException ex) {
            LOGGER.error(ex.getMessage() + ex);
        }
    }

    private void initialiseTreCollectionParserIfRequired() throws ParseException {
        if (treCollectionParser == null) {
            treCollectionParser = new TreCollectionParser();
        }
    }

//<editor-fold defaultstate="collapsed" desc="Image segment methods">
    /**
     * Parse the image segment.
     *
     * @param reader Reader to use for reading
     * @param i the index of the segment to read (zero base)
     * @param parseData whether to parse the associated data (true) or only to parse the header, skipping over the data
     * (false)
     * @return the segment
     * @throws ParseException on parse error
     */
    protected final ImageSegment readImageSegment(final NitfReader reader, final int i, final boolean parseData)
            throws ParseException {
        ImageSegmentParser imageSegmentParser = new ImageSegmentParser();
        ImageSegment imageSegment = imageSegmentParser.parse(reader, this);
        long dataLength = nitfStorage.getNitfHeader().getImageSegmentDataLengths().get(i);
        ImageInputStream iis = imageHeapStrategy.handleSegment(reader, dataLength);
        imageSegment.setData(iis);
        return imageSegment;
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Graphic segment methods">
    /**
     * Parse the graphic segment.
     *
     * @param reader Reader to use for reading
     * @param i the index of the header to read (zero base)
     * @param parseData whether to extract associated data (true) or to parse only the header and skip over the data
     * (false)
     * @return the segment header data
     * @throws ParseException on parse error
     */
    protected final GraphicSegment readGraphicSegment(final NitfReader reader, final int i, final boolean parseData) throws ParseException {
        GraphicSegmentParser graphicSegmentParser = new GraphicSegmentParser();
        GraphicSegment graphicSegment = graphicSegmentParser.parse(reader, this);
        long dataLength = nitfStorage.getNitfHeader().getGraphicSegmentDataLengths().get(i);
        if (parseData) {
            if (dataLength > 0) {
                // TODO: [IMG-77] this implementation probably should have a file-backed option
                byte[] bytes = reader.readBytesRaw((int) dataLength);
                graphicSegment.setData(new MemoryCacheImageInputStream(new ByteArrayInputStream(bytes)));
            }
        } else {
            if (dataLength > 0) {
                reader.skip(dataLength);
            }
        }
        return graphicSegment;
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Symbol segment methods">
    /**
     * Parse the symbol segment.
     *
     * @param reader Reader to use for reading
     * @param i the index of the header to read (zero base)
     * @param parseData whether to parse (true) or skip (false) the data for this symbol segment
     * @return the segment header data
     * @throws ParseException on parse error
     */
    protected final SymbolSegment readSymbolSegment(final NitfReader reader, final int i, final boolean parseData) throws ParseException {
        SymbolSegmentParser symbolSegmentParser = new SymbolSegmentParser();
        SymbolSegment symbolSegment = symbolSegmentParser.parse(reader, this);
        long dataLength = nitfStorage.getNitfHeader().getSymbolSegmentDataLengths().get(i);
        if (parseData) {
            if (dataLength > 0) {
                // TODO: [IMG-77] this implementation probably should have a file-backed option
                byte[] bytes = reader.readBytesRaw((int) dataLength);
                symbolSegment.setData(new MemoryCacheImageInputStream(new ByteArrayInputStream(bytes)));
            }
        } else {
            if (dataLength > 0) {
                reader.skip(dataLength);
            }
        }
        return symbolSegment;
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Label Segment Methods">
    /**
     * Parse the label segment.
     *
     * The reader will be positioned at the start of the associated data segment.
     *
     * @param reader Reader to use for reading
     * @param i the index of the header to read (zero base)
     * @param parseData whether to parse out the text for the label (true) or just parse the header and skip the text
     * (false)
     * @return the segment header data
     * @throws ParseException on parse error
     */
    protected final LabelSegment readLabelSegment(final NitfReader reader, final int i, final boolean parseData) throws ParseException {
        LabelSegmentParser labelSegmentParser = new LabelSegmentParser();
        LabelSegment labelSegment = labelSegmentParser.parse(reader, this);
        final long dataLength = nitfStorage.getNitfHeader().getLabelSegmentDataLengths().get(i);
        if (parseData) {
            if (dataLength > 0) {
                labelSegment.setData(reader.readBytes((int) dataLength));
            }
        } else {
            if (dataLength > 0) {
                reader.skip(dataLength);
            }
        }
        return labelSegment;
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Text segment methods">
    /**
     * Parse the text segment.
     *
     * @param reader Reader to use for reading
     * @param i the index of the segment to read (zero base)
     * @param parseData whether to extract associated text data (true) or just parse the header and skip the text
     * (false)
     * @return the text segment
     * @throws ParseException on parse error
     */
    protected final TextSegment readTextSegment(final NitfReader reader, final int i, final boolean parseData) throws ParseException {
        TextSegmentParser textSegmentParser = new TextSegmentParser();
        TextSegment textSegment = textSegmentParser.parse(reader, this);
        long dataLength = nitfStorage.getNitfHeader().getTextSegmentDataLengths().get(i);
        if (parseData) {
            if (dataLength > 0) {
                String text = reader.readBytes((int) dataLength);
                textSegment.setData(text);
            } else {
                textSegment.setData("");
            }
        } else {
            if (dataLength > 0) {
                reader.skip(dataLength);
            }
        }
        return textSegment;
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="DES methods">
    /**
     * Parse the data extension segment.
     *
     * @param reader Reader to use for reading
     * @param i the index of the segment to read (zero base)
     * @param parseData whether to parse the DES content (true) or skip over it (false).
     * @return the DES
     * @throws ParseException on parse error
     */
    protected final DataExtensionSegment readDataExtensionSegment(final NitfReader reader, final int i,
            final boolean parseData) throws ParseException {
        DataExtensionSegmentParser dataExtensionSegmentParser = new DataExtensionSegmentParser();
        DataExtensionSegment dataExtensionSegment = dataExtensionSegmentParser.parse(reader);
        long dataLength = nitfStorage.getNitfHeader().getDataExtensionSegmentDataLengths().get(i);
        if (parseData) {
            readDataExtensionSegmentData(dataExtensionSegment, reader, dataLength);
        } else {
            if (dataLength > 0) {
                reader.skip(dataLength);
            }
        }
        return dataExtensionSegment;
    }

    /**
     * Read the data extension segment data.
     *
     * The reader is assumed to be positioned at the end of the segment header before this call, and will be positioned
     * at the start of the next header after this call.
     *
     * @param dataExtensionSegment the header for the data extension segment that is to be read
     * @param reader the reader to use to read the data.
     * @throws ParseException on failure.
     */
    private void readDataExtensionSegmentData(final DataExtensionSegment dataExtensionSegment,
            final NitfReader reader, final long dataLength) throws ParseException {
        if (dataLength > 0) {
            if (dataExtensionSegment.isTreOverflow(reader.getFileType())) {
                initialiseTreCollectionParserIfRequired();
                TreCollection overflowTres = treCollectionParser.parse(reader,
                        (int) dataLength,
                        TreSource.TreOverflowDES);
                dataExtensionSegment.mergeTREs(overflowTres);
            } else if (!"STREAMING_FILE_HEADER".equals(dataExtensionSegment.getIdentifier().trim())) {
                dataExtensionSegment.setData(reader.readBytesRaw((int) dataLength));
            }
        }
    }
//</editor-fold>

    /**
     * Handle the image segment header and data.
     *
     * @param reader the reader to use, assumed to be positioned at the start of the header
     * @param i the index (zero base) of the segment to read
     * @throws ParseException if there is a problem handling the segment
     */
    protected abstract void handleImageSegment(final NitfReader reader, final int i) throws ParseException;

    /**
     * Handle the symbol segment header and data.
     *
     * @param reader the reader to use, assumed to be positioned at the start of the header
     * @param i the index (zero base) of the segment to read
     * @throws ParseException if there is a problem handling the segment
     */
    protected abstract void handleSymbolSegment(final NitfReader reader, final int i) throws ParseException;

    /**
     * Handle the label segment header and data.
     *
     * @param reader the reader to use, assumed to be positioned at the start of the header
     * @param i the index (zero base) of the segment to read
     * @throws ParseException if there is a problem handling the segment
     */
    protected abstract void handleLabelSegment(final NitfReader reader, final int i) throws ParseException;

    /**
     * Handle the graphic segment header and data.
     *
     * @param reader the reader to use, assumed to be positioned at the start of the header
     * @param i the index (zero base) of the segment to read
     * @throws ParseException if there is a problem handling the segment
     */
    protected abstract void handleGraphicSegment(final NitfReader reader, final int i) throws ParseException;

    /**
     * Handle the text segment header and data.
     *
     * @param reader the reader to use, assumed to be positioned at the start of the header
     * @param i the index (zero base) of the segment to read
     * @throws ParseException if there is a problem handling the segment
     */
    protected abstract void handleTextSegment(final NitfReader reader, final int i) throws ParseException;

    /**
     * Handle the data extension segment header and data.
     *
     * @param reader the reader to use, assumed to be positioned at the start of the header
     * @param i the index (zero base) of the segment to read
     * @throws ParseException if there is a problem handling the segment
     */
    protected abstract void handleDataExtensionSegment(final NitfReader reader, final int i) throws ParseException;

    /**
     * Register an additional TRE descriptor.
     *
     * @param source the source of the additional TreImpl descriptor.
     * @throws ParseException - when the TRE descriptors in the source are not in the expected format.
     */
    public final void registerAdditionalTREdescriptor(final Source source) throws ParseException {
        initialiseTreCollectionParserIfRequired();
        treCollectionParser.registerAdditionalTREdescriptor(source);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final TreCollection parseTREs(final NitfReader reader, final int length, final TreSource source) throws ParseException {
        initialiseTreCollectionParserIfRequired();
        return treCollectionParser.parse(reader, length, source);
    }
}
