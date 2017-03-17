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

import java.net.URI;
import java.util.function.Supplier;

import javax.imageio.stream.ImageInputStream;
import javax.xml.transform.Source;

import org.codice.imaging.nitf.core.HeapStrategy;
import org.codice.imaging.nitf.core.common.NitfFormatException;
import org.codice.imaging.nitf.core.common.ParseStrategy;

/**
 * A builder class that handles parsing.
 */
public interface NitfParserParsingFlow {

    /**
     * Configures the ImageDataStrategy for parsing operations.
     *
     * @param imageDataStrategySupplier a supplier for the ImageDataStrategy.
     * @return this NitfParserParsingFlow.
     */
    NitfParserParsingFlow imageDataStrategy(
            Supplier<HeapStrategy<ImageInputStream>> imageDataStrategySupplier);

    /**
     * Add a TRE descriptor to support TRE parsing.
     *
     * @param xmlDescriptor string containing the TRE (or TREs) format.
     * @return this NitfParserParsingFlow
     */
    NitfParserParsingFlow treDescriptor(String xmlDescriptor);

    /**
     * Add a TRE descriptor to support TRE parsing.
     *
     * @param xmlDescriptor XML Source containing the TRE (or TREs) format.
     * @return this NitfParserParsingFlow
     */
    NitfParserParsingFlow treDescriptor(Source xmlDescriptor);

    /**
     * Add a TRE descriptor to support TRE parsing.
     *
     * @param xmlDescriptor URI containing the TRE (or TREs) format.
     * @return this NitfParserParsingFlow
     */
    NitfParserParsingFlow treDescriptor(URI xmlDescriptor);

    /**
     * Parses the NITF file, extracting all data.
     *
     * @return a new NitfSegmentsFlow.
     * @throws NitfFormatException when it's thrown by the parser.
     */
    NitfSegmentsFlow allData() throws NitfFormatException;

    /**
     * Parses only headers from the NITF file.
     *
     * @return a new NitfSegmentsFlow.
     * @throws NitfFormatException when it's thrown by the parser.
     */
    NitfSegmentsFlow headerOnly() throws NitfFormatException;

    /**
     * Parses the NITF file using the supplied SlottedParseStrategy.
     *
     * @param parseStrategy the SlottedParseStrategy to use for parsing.
     * @return a new NitfSegmentsFlow.
     * @throws NitfFormatException when it's thrown by the parser.
     */
    NitfSegmentsFlow build(ParseStrategy parseStrategy)
            throws NitfFormatException;
}
