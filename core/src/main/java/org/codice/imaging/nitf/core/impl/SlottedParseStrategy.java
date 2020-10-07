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
package org.codice.imaging.nitf.core.impl;

import java.io.ByteArrayInputStream;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.MemoryCacheImageInputStream;
import javax.xml.transform.Source;

import org.codice.imaging.nitf.core.DataSource;
import org.codice.imaging.nitf.core.HeapStrategy;
import org.codice.imaging.nitf.core.common.NitfFormatException;
import org.codice.imaging.nitf.core.common.NitfReader;
import org.codice.imaging.nitf.core.common.ParseStrategy;
import org.codice.imaging.nitf.core.dataextension.DataExtensionSegment;
import org.codice.imaging.nitf.core.dataextension.impl.DataExtensionSegmentParser;
import org.codice.imaging.nitf.core.graphic.GraphicSegment;
import org.codice.imaging.nitf.core.graphic.impl.GraphicSegmentParser;
import org.codice.imaging.nitf.core.header.NitfHeader;
import org.codice.imaging.nitf.core.image.ImageSegment;
import org.codice.imaging.nitf.core.image.impl.ImageSegmentParser;
import org.codice.imaging.nitf.core.label.LabelSegment;
import org.codice.imaging.nitf.core.label.impl.LabelSegmentParser;
import org.codice.imaging.nitf.core.symbol.SymbolSegment;
import org.codice.imaging.nitf.core.symbol.impl.SymbolSegmentParser;
import org.codice.imaging.nitf.core.text.TextSegment;
import org.codice.imaging.nitf.core.text.impl.TextSegmentParser;
import org.codice.imaging.nitf.core.tre.TreCollection;
import org.codice.imaging.nitf.core.tre.impl.TreCollectionParser;
import org.codice.imaging.nitf.core.tre.TreSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * "Slotted" parse strategy.
 */
public class SlottedParseStrategy implements ParseStrategy {

    private HeapStrategy<ImageInputStream> imageHeapStrategy =
            new InMemoryHeapStrategy<>(MemoryCacheImageInputStream::new);
    private HeapStrategy<ImageInputStream> desHeapStrategy
            = new InMemoryHeapStrategy<>(MemoryCacheImageInputStream::new);

    /**
     * Stores the NITF data.
     */
    protected SlottedStorage nitfStorage = new SlottedStorage();

    /**
     * Flag to indicate only headers (not associated data) should be extracted.
     */
    public static final int HEADERS_ONLY = 0x0000;

    /**
     * Flag to indicate image segment data should be extracted.
     */
    public static final int IMAGE_DATA = 0x0001;

    /**
     * Flag to indicate graphic segment data should be extracted.
     */
    public static final int GRAPHIC_DATA = 0x0002;

    /**
     * Flag to indicate symbol segment data should be extracted.
     */
    public static final int SYMBOL_DATA = 0x0004;

    /**
     * Flag to indicate label segment data should be extracted.
     */
    public static final int LABEL_DATA = 0x0008;

    /**
     * Flag to indicate text segment data should be extracted.
     */
    public static final int TEXT_DATA = 0x0010;

    /**
     * Flag to indicate Data Extension Segment data should be extracted.
     */
    public static final int DES_DATA = 0x0020;

    /**
     * Flag to indicate all segment data should be extracted.
     */
    public static final int ALL_SEGMENT_DATA = TEXT_DATA
            | LABEL_DATA
            | SYMBOL_DATA
            | GRAPHIC_DATA
            | IMAGE_DATA
            | DES_DATA;

    private static final Logger LOGGER = LoggerFactory.getLogger(SlottedParseStrategy.class);

    /**
     * The TRE parser to use. Must be initialised before use, see initialiseTreCollectionParserIfRequired()
     */
    protected TreCollectionParser treCollectionParser;

    private int segmentsToExtract = ALL_SEGMENT_DATA;

    /**
     * Constructor.
     */
    public SlottedParseStrategy() {
        treCollectionParser = null;
    }

    /**
     * Constructor allowing selection of data segments to extract.
     *
     * @param requiredSegments bitmask of segments required from this parse strategy.
     *
     */
    public SlottedParseStrategy(final int requiredSegments) {
        treCollectionParser = null;
        segmentsToExtract = requiredSegments;
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
    public final NitfHeader getNitfHeader() {
        return nitfStorage.getNitfHeader();
    }

    @Override
    public final void setFileHeader(final NitfHeader nitfFileHeader) {
        nitfStorage.setNitfHeader(nitfFileHeader);
    }

    /**
     *
     * {@inheritDoc}
     */
    public final DataSource getDataSource() {
        return nitfStorage;
    }

    private void initialiseTreCollectionParserIfRequired() throws NitfFormatException {
        if (treCollectionParser == null) {
            treCollectionParser = new TreCollectionParser();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void handleImageSegment(final NitfReader reader, final long dataLength) throws NitfFormatException {
        ImageSegmentParser imageSegmentParser = new ImageSegmentParser();
        ImageSegment imageSegment = imageSegmentParser.parse(reader, this, dataLength);
        if ((segmentsToExtract & IMAGE_DATA) == IMAGE_DATA) {
            if (dataLength > 0) {
                ImageInputStream iis = imageHeapStrategy.handleSegment(reader, dataLength);
                imageSegment.setData(iis);
            }
        } else {
            if (dataLength > 0) {
                reader.skip(dataLength);
            }
        }
        nitfStorage.getImageSegments().add(imageSegment);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void handleGraphicSegment(final NitfReader reader, final long dataLength) throws NitfFormatException {
        GraphicSegmentParser graphicSegmentParser = new GraphicSegmentParser();
        GraphicSegment graphicSegment = graphicSegmentParser.parse(reader, this, dataLength);
        if ((segmentsToExtract & GRAPHIC_DATA) == GRAPHIC_DATA) {
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
        nitfStorage.getGraphicSegments().add(graphicSegment);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void handleSymbolSegment(final NitfReader reader, final long dataLength) throws NitfFormatException {
        SymbolSegmentParser symbolSegmentParser = new SymbolSegmentParser();
        SymbolSegment symbolSegment = symbolSegmentParser.parse(reader, this, dataLength);
        if ((segmentsToExtract & SYMBOL_DATA) == SYMBOL_DATA) {
            if (dataLength > 0) {
                byte[] bytes = reader.readBytesRaw((int) dataLength);
                symbolSegment.setData(new MemoryCacheImageInputStream(new ByteArrayInputStream(bytes)));
            }
        } else {
            if (dataLength > 0) {
                reader.skip(dataLength);
            }
        }
        nitfStorage.getSymbolSegments().add(symbolSegment);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void handleLabelSegment(final NitfReader reader, final long dataLength) throws NitfFormatException {
        LabelSegmentParser labelSegmentParser = new LabelSegmentParser();
        LabelSegment labelSegment = labelSegmentParser.parse(reader, this);
        if ((segmentsToExtract & LABEL_DATA) == LABEL_DATA) {
            if (dataLength > 0) {
                labelSegment.setData(reader.readBytes((int) dataLength));
            }
        } else {
            if (dataLength > 0) {
                reader.skip(dataLength);
            }
        }
        nitfStorage.getLabelSegments().add(labelSegment);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void handleTextSegment(final NitfReader reader, final long dataLength) throws NitfFormatException {
        TextSegmentParser textSegmentParser = new TextSegmentParser();
        TextSegment textSegment = textSegmentParser.parse(reader, this);
        if ((segmentsToExtract & TEXT_DATA) == TEXT_DATA) {
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
        nitfStorage.getTextSegments().add(textSegment);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void handleDataExtensionSegment(final NitfReader reader, final long dataLength) throws NitfFormatException {

        DataExtensionSegmentParser dataExtensionSegmentParser = new DataExtensionSegmentParser();
        DataExtensionSegment dataExtensionSegment = dataExtensionSegmentParser.parse(reader, dataLength);
        if ((segmentsToExtract & DES_DATA) == DES_DATA) {
            if (dataLength > 0) {
                readDataExtensionSegmentData(dataExtensionSegment, reader, dataLength);
            }
        } else {
            if (dataLength > 0) {
                reader.skip(dataLength);
            }
        }
        nitfStorage.getDataExtensionSegments().add(dataExtensionSegment);
    }

    /**
     * Read the data extension segment data.
     *
     * The reader is assumed to be positioned at the end of the segment header before this call, and will be positioned
     * at the start of the next header after this call.
     *
     * @param dataExtensionSegment the data extension segment that is to be completed
     * @param reader the reader to use to read the data.
     * @throws NitfFormatException if there is a problem reading the segment header or data
     */
    private void readDataExtensionSegmentData(final DataExtensionSegment dataExtensionSegment,
            final NitfReader reader, final long dataLength) throws NitfFormatException {
        if (dataLength > 0) {
            if (dataExtensionSegment.isTreOverflow()) {
                initialiseTreCollectionParserIfRequired();
                TreCollection overflowTres = treCollectionParser.parse(reader,
                        (int) dataLength,
                        TreSource.TreOverflowDES);
                dataExtensionSegment.mergeTREs(overflowTres);
            } else if (!"STREAMING_FILE_HEADER".equals(dataExtensionSegment.getIdentifier().trim())) {
                ImageInputStream iis = desHeapStrategy.handleSegment(reader, dataLength);
                dataExtensionSegment.setDataConsumer(c -> c.accept(iis));
            }
        }
    }

    /**
     *
     * {@inheritDoc}
     */
    public final void registerAdditionalTREdescriptor(final Source source) throws NitfFormatException {
        initialiseTreCollectionParserIfRequired();
        treCollectionParser.registerAdditionalTREdescriptor(source);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final TreCollection parseTREs(final NitfReader reader, final int length, final TreSource source) throws NitfFormatException {
        initialiseTreCollectionParserIfRequired();
        return treCollectionParser.parse(reader, length, source);
    }
}
