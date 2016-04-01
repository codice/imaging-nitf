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
package org.codice.imaging.nitf.core.common;

import static org.codice.imaging.nitf.core.common.CommonConstants.ENCRYP_LENGTH;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
    Common segment parsing functionality.
*/
public abstract class AbstractSegmentParser {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractSegmentParser.class);

    /**
     * The NitfReader which streams the input.
     */
    protected NitfReader reader = null;

    /**
     * The NitfParsingStrategy to be used when parsing.
     */
    protected NitfParseStrategy parsingStrategy;

    /**
     * Read a ENCRYP value, and check the result.
     *
     * @throws NitfFormatException when the input doesn't match the expected format for ENCRYP.
     */
    protected final void readENCRYP() throws NitfFormatException {
        if (!"0".equals(reader.readBytes(ENCRYP_LENGTH))) {
            LOG.warn("Mismatch while reading ENCRYP");
            throw new NitfFormatException("Unexpected ENCRYP value", (int) reader.getCurrentOffset());
        }
    }

    /**
     * Read in a NITF date/time format.
     *
     * Note that this is relatively tolerant, and may not result in something usable as a date/time class in Java.
     *
     * @return a NitfDateTime from head of the reader stream.
     * @throws NitfFormatException when the next token is not the expected format for a NitfDateTime.
     *
     * @see NitfDateTime for the "best effort" nature of this parsing.
     */
    public final NitfDateTime readNitfDateTime() throws NitfFormatException {
        DateTimeParser dateTimeParser = new DateTimeParser();
        return dateTimeParser.readNitfDateTime(reader);
    }
}
