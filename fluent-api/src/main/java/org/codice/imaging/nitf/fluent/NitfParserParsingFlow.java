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

import java.io.File;
import java.io.StringReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.ImageInputStream;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import org.codice.imaging.nitf.core.FileBackedHeapStrategy;
import org.codice.imaging.nitf.core.HeapStrategy;
import org.codice.imaging.nitf.core.SlottedParseStrategy;
import org.codice.imaging.nitf.core.common.NitfFormatException;
import org.codice.imaging.nitf.core.common.NitfReader;
import org.codice.imaging.nitf.core.header.NitfParser;

/**
 * A builder class that handles parsing.
 */
public class NitfParserParsingFlow {
    private final NitfReader reader;

    private HeapStrategy<ImageInputStream> imageDataStrategy =
            new FileBackedHeapStrategy<>(file -> new FileImageInputStream(file));

    private final List<Source> treDescriptors = new ArrayList<>();

    NitfParserParsingFlow(final NitfReader nitfReader) {
        reader = nitfReader;
    }

    /**
     * Configures the ImageDataStrategy for parsing operations.
     *
     * @param imageDataStrategySupplier a supplier for the ImageDataStrategy.
     * @return this NitfParserParsingFlow.
     */
    public final NitfParserParsingFlow imageDataStrategy(
            final Supplier<HeapStrategy<ImageInputStream>> imageDataStrategySupplier) {
        this.imageDataStrategy = imageDataStrategySupplier.get();
        return this;
    }

    /**
     * Add a TRE descriptor to support TRE parsing.
     *
     * @param xmlDescriptor string containing the TRE (or TREs) format.
     * @return this NitfParserParsingFlow
     */
    public final NitfParserParsingFlow treDescriptor(final String xmlDescriptor) {
        this.treDescriptors.add(new StreamSource(new StringReader(xmlDescriptor)));
        return this;
    }

    /**
     * Add a TRE descriptor to support TRE parsing.
     *
     * @param xmlDescriptor XML Source containing the TRE (or TREs) format.
     * @return this NitfParserParsingFlow
     */
    public final NitfParserParsingFlow treDescriptor(final Source xmlDescriptor) {
        this.treDescriptors.add(xmlDescriptor);
        return this;
    }

    /**
     * Add a TRE descriptor to support TRE parsing.
     *
     * @param xmlDescriptor URI containing the TRE (or TREs) format.
     * @return this NitfParserParsingFlow
     */
    public final NitfParserParsingFlow treDescriptor(final URI xmlDescriptor) {
        this.treDescriptors.add(new StreamSource(new File(xmlDescriptor)));
        return this;
    }

    /**
     * Parses the NITF file, extracting all data.
     *
     * @return a new NitfSegmentsFlow.
     * @throws NitfFormatException when it's thrown by the parser.
     */
    public final NitfSegmentsFlow allData() throws NitfFormatException {
        SlottedParseStrategy parseStrategy = new SlottedParseStrategy();
        parseStrategy.setImageHeapStrategy(imageDataStrategy);
        return build(parseStrategy);
    }

    /**
     * Parses only headers from the NITF file.
     *
     * @return a new NitfSegmentsFlow.
     * @throws NitfFormatException when it's thrown by the parser.
     */
    public final NitfSegmentsFlow headerOnly() throws NitfFormatException {
        SlottedParseStrategy parseStrategy =
                new SlottedParseStrategy(SlottedParseStrategy.HEADERS_ONLY);
        parseStrategy.setImageHeapStrategy(imageDataStrategy);
        return build(parseStrategy);
    }

    /**
     * Parses the NITF file using the supplied SlottedParseStrategy.
     *
     * @param parseStrategy the SlottedParseStrategy to use for parsing.
     * @return a new NitfSegmentsFlow.
     * @throws NitfFormatException when it's thrown by the parser.
     */
    public final NitfSegmentsFlow build(final SlottedParseStrategy parseStrategy)
            throws NitfFormatException {
        for (Source treDescriptor : treDescriptors) {
            parseStrategy.registerAdditionalTREdescriptor(treDescriptor);
        }
        NitfParser.parse(reader, parseStrategy);
        return new NitfSegmentsFlow(parseStrategy.getDataSource(), imageDataStrategy::cleanUp);
    }
}
