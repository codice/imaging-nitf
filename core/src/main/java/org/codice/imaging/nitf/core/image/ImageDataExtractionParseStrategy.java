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
package org.codice.imaging.nitf.core.image;

import java.text.ParseException;
import org.codice.imaging.nitf.core.SlottedNitfParseStrategy;
import org.codice.imaging.nitf.core.common.NitfReader;

/**
 * Parse strategy that extracts all headers, plus the image segment data.
 */
public class ImageDataExtractionParseStrategy extends SlottedNitfParseStrategy {

    /**
     * {@inheritDoc}
     */
    @Override
    protected final void handleImageSegment(final NitfReader reader, final int i) throws ParseException {
        parseImageSegmentHeaderAndData(reader, i);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected final void handleSymbolSegment(final NitfReader reader, final int i) throws ParseException {
        parseSymbolSegmentHeaderButSkipData(reader, i);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected final void handleLabelSegment(final NitfReader reader, final int i) throws ParseException {
        parseLabelSegmentHeaderButSkipData(reader, i);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected final void handleGraphicSegment(final NitfReader reader, final int i) throws ParseException {
        parseGraphicSegmentHeaderButSkipData(reader, i);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected final void handleTextSegment(final NitfReader reader, final int i) throws ParseException {
        parseTextSegmentHeaderButSkipData(reader, i);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected final void handleDataExtensionSegment(final NitfReader reader, final int i) throws ParseException {
        parseDataExtensionSegmentHeaderButSkipData(reader, i);
    }

}
