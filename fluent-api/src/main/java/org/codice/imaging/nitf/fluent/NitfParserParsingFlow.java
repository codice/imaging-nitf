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
package org.codice.imaging.nitf.fluent;

import java.text.ParseException;
import java.util.function.Supplier;
import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.ImageInputStream;
import org.codice.imaging.nitf.core.AllDataExtractionParseStrategy;
import org.codice.imaging.nitf.core.FileBackedHeapStrategy;
import org.codice.imaging.nitf.core.HeaderOnlyNitfParseStrategy;
import org.codice.imaging.nitf.core.HeapStrategy;
import org.codice.imaging.nitf.core.NitfFileParser;
import org.codice.imaging.nitf.core.SlottedNitfParseStrategy;
import org.codice.imaging.nitf.core.common.NitfReader;
import org.codice.imaging.nitf.core.dataextension.DataExtensionSegmentNitfParseStrategy;
import org.codice.imaging.nitf.core.image.ImageDataExtractionParseStrategy;

/**
 * A builder class that handles parsing.
 */
public class NitfParserParsingFlow {
    private final NitfReader nitfReader;

    private HeapStrategy<ImageInputStream> imageDataStrategy =
            new FileBackedHeapStrategy<>(file -> new FileImageInputStream(file));

    NitfParserParsingFlow(NitfReader nitfReader) {
        this.nitfReader = nitfReader;
    }

    /**
     * Configures the ImageDataStrategy for parsing operations.
     *
     * @param imageDataStrategySupplier a supplier for the ImageDataStrategy.
     * @return this NitfParserParsingFlow.
     */
    public NitfParserParsingFlow imageDataStrategy(Supplier<HeapStrategy<ImageInputStream>> imageDataStrategySupplier) {
        this.imageDataStrategy = imageDataStrategySupplier.get();
        return this;
    }

    /**
     * Parses the NITF file using an AllDataExtractionParseStrategy.
     *
     * @return a new NitfSegmentsFlow.
     * @throws ParseException when it's thrown by the parser.
     */
    public NitfSegmentsFlow allData() throws ParseException {
        SlottedNitfParseStrategy parseStrategy = new AllDataExtractionParseStrategy();
        parseStrategy.setImageHeapStrategy(imageDataStrategy);
        return build(parseStrategy);
    }

    /**
     * Parses the NITF file using the HeaderOnlyNitfParseStrategy.
     *
     * @return a new NitfSegmentsFlow.
     * @throws ParseException when it's thrown by the parser.
     */
    public NitfSegmentsFlow headerOnly() throws ParseException {
        SlottedNitfParseStrategy parseStrategy = new HeaderOnlyNitfParseStrategy();
        parseStrategy.setImageHeapStrategy(imageDataStrategy);
        return build(parseStrategy);
    }

    /**
     * Parses the NITF file using the DataExtensionSegmentNitfParseStrategy.
     *
     * @return a new NitfSegmentsFlow.
     * @throws ParseException when it's thrown by the parser.
     */
    public NitfSegmentsFlow dataExtensionSegment() throws ParseException {
        SlottedNitfParseStrategy parseStrategy = new DataExtensionSegmentNitfParseStrategy();
        parseStrategy.setImageHeapStrategy(imageDataStrategy);
        return build(parseStrategy);
    }

    /**
     * Parses the NITF file using the ImageDataExtractionParseStrategy.
     *
     * @return a new NitfSegmentsFlow.
     * @throws ParseException when it's thrown by the parser.
     */
    public NitfSegmentsFlow imageData() throws ParseException {
        SlottedNitfParseStrategy parseStrategy = new ImageDataExtractionParseStrategy();
        parseStrategy.setImageHeapStrategy(imageDataStrategy);
        return build(parseStrategy);
    }

    /**
     * Parses the NITF file using the supplied SlottedNitfParseStrategy.
     *
     * @param parseStrategy the SlottedNitfParseStrategy to use for parsing.
     * @return a new NitfSegmentsFlow.
     * @throws ParseException when it's thrown by the parser.
     *
     */
    public NitfSegmentsFlow build(SlottedNitfParseStrategy parseStrategy) throws ParseException {
        NitfFileParser.parse(nitfReader, parseStrategy);
        return new NitfSegmentsFlow(parseStrategy.getNitfDataSource());
    }
}
