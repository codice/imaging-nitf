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
package org.codice.imaging.nitf.render.flow;

import java.text.ParseException;
import org.codice.imaging.nitf.core.AllDataExtractionParseStrategy;
import org.codice.imaging.nitf.core.HeaderOnlyNitfParseStrategy;
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

    NitfParserParsingFlow(NitfReader nitfReader) {
        this.nitfReader = nitfReader;
    }

    /**
     * Parses the NITF file using an AllDataExtractionParseStrategy.
     *
     * @return a new NitfSegmentsFlow.
     * @throws ParseException when it's thrown by the parser.
     *
     * @see {@link org.codice.imaging.nitf.core.AllDataExtractionParseStrategy}
     */
    public NitfSegmentsFlow allData() throws ParseException {
        SlottedNitfParseStrategy parseStrategy = new AllDataExtractionParseStrategy();
        return build(parseStrategy);
    }

    /**
     * Parses the NITF file using the HeaderOnlyNitfParseStrategy.
     *
     * @return a new NitfSegmentsFlow.
     * @throws ParseException when it's thrown by the parser.
     *
     * @see {@link org.codice.imaging.nitf.core.HeaderOnlyNitfParseStrategy}
     */
    public NitfSegmentsFlow headerOnly() throws ParseException {
        SlottedNitfParseStrategy parseStrategy = new HeaderOnlyNitfParseStrategy();
        return build(parseStrategy);
    }

    /**
     * Parses the NITF file using the DataExtensionSegmentNitfParseStrategy.
     *
     * @return a new NitfSegmentsFlow.
     * @throws ParseException when it's thrown by the parser.
     *
     * @see
     * {@link org.codice.imaging.nitf.core.dataextension.DataExtensionSegmentNitfParseStrategy}
     */
    public NitfSegmentsFlow dataExtensionSegment() throws ParseException {
        SlottedNitfParseStrategy parseStrategy = new DataExtensionSegmentNitfParseStrategy();
        return build(parseStrategy);
    }

    /**
     * Parses the NITF file using the ImageDataExtractionParseStrategy.
     *
     * @return a new NitfSegmentsFlow.
     * @throws ParseException when it's thrown by the parser.
     *
     * @see
     * {@link org.codice.imaging.nitf.core.image.ImageDataExtractionParseStrategy}
     */
    public NitfSegmentsFlow imageData() throws ParseException {
        SlottedNitfParseStrategy parseStrategy = new ImageDataExtractionParseStrategy();
        return build(parseStrategy);
    }

    /**
     * Parses the NITF file using the supplied SlottedNitfParseStrategy.
     *
     * @param parseStrategy - The NitfParserStrategy to use for parsing.
     * @return a new NitfSegmentsFlow.
     * @throws ParseException when it's thrown by the parser.
     *
     * @see {@link org.codice.imaging.nitf.core.SlottedNitfParseStrategy}
     */
    public NitfSegmentsFlow build(SlottedNitfParseStrategy parseStrategy) throws ParseException {
        NitfFileParser.parse(nitfReader, parseStrategy);
        return new NitfSegmentsFlow(parseStrategy);
    }
}
