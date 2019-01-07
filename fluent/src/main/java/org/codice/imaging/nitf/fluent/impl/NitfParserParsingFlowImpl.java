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
package org.codice.imaging.nitf.fluent.impl;

import java.io.File;
import java.io.StringReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.MemoryCacheImageInputStream;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import org.codice.imaging.nitf.core.HeapStrategy;
import org.codice.imaging.nitf.core.common.ParseStrategy;
import org.codice.imaging.nitf.core.impl.InMemoryHeapStrategy;
import org.codice.imaging.nitf.core.impl.SlottedParseStrategy;
import org.codice.imaging.nitf.core.common.NitfFormatException;
import org.codice.imaging.nitf.core.common.NitfReader;
import org.codice.imaging.nitf.core.header.impl.NitfParser;
import org.codice.imaging.nitf.fluent.NitfParserParsingFlow;
import org.codice.imaging.nitf.fluent.NitfSegmentsFlow;

/**
 * A builder class that handles parsing.
 */
public class NitfParserParsingFlowImpl implements NitfParserParsingFlow, AutoCloseable {
    private final NitfReader reader;

    private HeapStrategy<ImageInputStream> imageDataStrategy =
            new InMemoryHeapStrategy<>(MemoryCacheImageInputStream::new);

    private final List<Source> treDescriptors = new ArrayList<>();

    NitfParserParsingFlowImpl(final NitfReader nitfReader) {
        reader = nitfReader;
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public final NitfParserParsingFlow imageDataStrategy(
            final Supplier<HeapStrategy<ImageInputStream>> imageDataStrategySupplier) {
        this.imageDataStrategy = imageDataStrategySupplier.get();
        return this;
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public final NitfParserParsingFlow treDescriptor(final String xmlDescriptor) {
        this.treDescriptors.add(new StreamSource(new StringReader(xmlDescriptor)));
        return this;
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public final NitfParserParsingFlow treDescriptor(final Source xmlDescriptor) {
        this.treDescriptors.add(xmlDescriptor);
        return this;
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public final NitfParserParsingFlow treDescriptor(final URI xmlDescriptor) {
        this.treDescriptors.add(new StreamSource(new File(xmlDescriptor)));
        return this;
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public final NitfSegmentsFlow allData() throws NitfFormatException {
        SlottedParseStrategy parseStrategy = new SlottedParseStrategy();
        parseStrategy.setImageHeapStrategy(imageDataStrategy);
        return build(parseStrategy);
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public final NitfSegmentsFlow headerOnly() throws NitfFormatException {
        SlottedParseStrategy parseStrategy =
                new SlottedParseStrategy(SlottedParseStrategy.HEADERS_ONLY);
        parseStrategy.setImageHeapStrategy(imageDataStrategy);
        return build(parseStrategy);
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public final NitfSegmentsFlow build(final ParseStrategy parseStrategy)
            throws NitfFormatException {
        for (Source treDescriptor : treDescriptors) {
            parseStrategy.registerAdditionalTREdescriptor(treDescriptor);
        }
        NitfParser.parse(reader, parseStrategy);
        return new NitfSegmentsFlowImpl(parseStrategy.getDataSource(), imageDataStrategy::cleanUp);
    }

    @Override
    public final void close() throws Exception {
        this.reader.close();
    }
}
