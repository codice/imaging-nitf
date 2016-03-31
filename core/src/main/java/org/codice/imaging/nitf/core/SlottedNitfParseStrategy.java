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
import org.codice.imaging.nitf.core.common.NitfParseStrategy;
import org.codice.imaging.nitf.core.common.NitfReader;
import org.codice.imaging.nitf.core.dataextension.DataExtensionSegment;
import org.codice.imaging.nitf.core.dataextension.DataExtensionSegmentParser;
import org.codice.imaging.nitf.core.graphic.GraphicSegment;
import org.codice.imaging.nitf.core.graphic.GraphicSegmentParser;
import org.codice.imaging.nitf.core.header.NitfHeader;
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

/**
 * "Slotted" parse strategy.
 */
public abstract class SlottedNitfParseStrategy implements NitfParseStrategy {

    private HeapStrategy<ImageInputStream> imageHeapStrategy =
            new InMemoryHeapStrategy<>((InputStream is) -> new MemoryCacheImageInputStream(is));
    private HeapStrategy<ImageInputStream> desHeapStrategy
            = new InMemoryHeapStrategy<>((InputStream is) -> new MemoryCacheImageInputStream(is));

    /**
     * Stores the NITF data.
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
     * Set the strategy to use for storing image segment data.
     *
     * @param dataStrategy the HeapStrategy to use for this parser's image storage. If null, then this instance will use
     * an InMemoryHeapStrategy instance.
     */
    public final void setImageHeapStrategy(final HeapStrategy dataStrategy) {
        if (dataStrategy != null) {
            this.imageHeapStrategy = dataStrategy;
        }
    }

    /**
     * Set the strategy to use for storing DES data.
     *
     * @param dataStrategy the HeapStrategy to use for this parser's DES data storage. If null, then this instance will
     * use an InMemoryHeapStrategy instance.
     */
    public final void setDataExtensionSegmentHeapStrategy(final HeapStrategy dataStrategy) {
        if (dataStrategy != null) {
            this.desHeapStrategy = dataStrategy;
        }
    }

    @Override
    public final void setFileHeader(final NitfHeader nitfFileHeader) {
        nitfStorage.setNitfHeader(nitfFileHeader);
    }

    /**
     *
     * @return the NitfFileHeader.
     */
    @Override
    public final NitfHeader getNitfHeader() {
        return nitfStorage.getNitfHeader();
    }

    /**
     * Get the resulting data.
     *
     * @return a NitfDataSource containing the parsed NITF.
     */
    public final NitfDataSource getNitfDataSource() {
        return this.nitfStorage;
    }

    private void initialiseTreCollectionParserIfRequired() throws ParseException {
        if (treCollectionParser == null) {
            treCollectionParser = new TreCollectionParser();
        }
    }

    /**
     * Parse the image segment.
     *
     * @param reader Reader to use for reading
     * @param parseData whether to parse the associated data (true) or only to parse the header, skipping over the data
     * (false)
     * @param dataLength the length of the data associated with this segment.
     * @return the segment
     * @throws ParseException on parse error
     */
    protected final ImageSegment readImageSegment(final NitfReader reader, final boolean parseData, final long dataLength)
            throws ParseException {
        ImageSegmentParser imageSegmentParser = new ImageSegmentParser();
        ImageSegment imageSegment = imageSegmentParser.parse(reader, this, dataLength);
        if (parseData) {
            if (dataLength > 0) {
                ImageInputStream iis = imageHeapStrategy.handleSegment(reader, dataLength);
                imageSegment.setData(iis);
            }
        } else {
            if (dataLength > 0) {
                reader.skip(dataLength);
            }
        }
        return imageSegment;
    }

    /**
     * Parse the graphic segment.
     *
     * @param reader Reader to use for reading
     * @param parseData whether to extract associated data (true) or to parse only the header and skip over the data
     * (false)
     * @param dataLength the length of the data associated with this segment.
     * @return the segment header data
     * @throws ParseException on parse error
     */
    protected final GraphicSegment readGraphicSegment(final NitfReader reader, final boolean parseData, final long dataLength) throws ParseException {
        GraphicSegmentParser graphicSegmentParser = new GraphicSegmentParser();
        GraphicSegment graphicSegment = graphicSegmentParser.parse(reader, this, dataLength);
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

    /**
     * Parse the symbol segment.
     *
     * @param reader Reader to use for reading
     * @param parseData whether to parse (true) or skip (false) the data for this symbol segment
     * @param dataLength the length of the data associated with this segment.
     * @return the segment header data
     * @throws ParseException on parse error
     */
    protected final SymbolSegment readSymbolSegment(final NitfReader reader, final boolean parseData, final long dataLength) throws ParseException {
        SymbolSegmentParser symbolSegmentParser = new SymbolSegmentParser();
        SymbolSegment symbolSegment = symbolSegmentParser.parse(reader, this, dataLength);
        if (parseData) {
            if (dataLength > 0) {
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

    /**
     * Parse the label segment.
     *
     * The reader will be positioned at the start of the associated data segment.
     *
     * @param reader Reader to use for reading
     * @param dataLength the length of the data part of the segment
     * @param parseData whether to parse out the text for the label (true) or just parse the header and skip the text
     * (false)
     * @return the segment header data
     * @throws ParseException on parse error
     */
    protected final LabelSegment readLabelSegment(final NitfReader reader, final long dataLength, final boolean parseData) throws ParseException {
        LabelSegmentParser labelSegmentParser = new LabelSegmentParser();
        LabelSegment labelSegment = labelSegmentParser.parse(reader, this);
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

    /**
     * Parse the text segment.
     *
     * @param reader Reader to use for reading
     * @param dataLength the length of the data part of the segment
     * @param parseData whether to extract associated text data (true) or just parse the header and skip the text
     * (false)
     * @return the text segment
     * @throws ParseException on parse error
     */
    protected final TextSegment readTextSegment(final NitfReader reader, final long dataLength, final boolean parseData) throws ParseException {
        TextSegmentParser textSegmentParser = new TextSegmentParser();
        TextSegment textSegment = textSegmentParser.parse(reader, this);
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

    /**
     * Parse the data extension segment.
     *
     * @param reader Reader to use for reading
     * @param parseData whether to parse the DES content (true) or skip over it (false).
     * @param dataLength the length of the data part of this segment
     * @return the DES
     * @throws ParseException on parse error
     */
    protected final DataExtensionSegment readDataExtensionSegment(final NitfReader reader, final boolean parseData,
            final long dataLength) throws ParseException {
        DataExtensionSegmentParser dataExtensionSegmentParser = new DataExtensionSegmentParser();
        DataExtensionSegment dataExtensionSegment = dataExtensionSegmentParser.parse(reader, dataLength);
        if (parseData) {
            if (dataLength > 0) {
                readDataExtensionSegmentData(dataExtensionSegment, reader, dataLength);
            }
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
     * @param dataExtensionSegment the data extension segment that is to be completed
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
                ImageInputStream iis = desHeapStrategy.handleSegment(reader, dataLength);
                dataExtensionSegment.setData(iis);
            }
        }
    }

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
