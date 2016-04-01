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

import org.codice.imaging.nitf.core.common.NitfFormatException;
import org.codice.imaging.nitf.core.common.NitfReader;

/**
 * Parse strategy that only extracts headers.
 */
public class HeaderOnlyNitfParseStrategy extends SlottedNitfParseStrategy {

    /**
     * {@inheritDoc}
     */
    @Override
    public final void handleImageSegment(final NitfReader reader, final long dataLength) throws NitfFormatException {
        nitfStorage.getImageSegments().add(readImageSegment(reader, false, dataLength));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void handleSymbolSegment(final NitfReader reader, final long dataLength) throws NitfFormatException {
        nitfStorage.getSymbolSegments().add(readSymbolSegment(reader, false, dataLength));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void handleLabelSegment(final NitfReader reader, final long dataLength) throws NitfFormatException {
        nitfStorage.getLabelSegments().add(readLabelSegment(reader, dataLength, false));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void handleGraphicSegment(final NitfReader reader, final long dataLength) throws NitfFormatException {
        nitfStorage.getGraphicSegments().add(readGraphicSegment(reader, false, dataLength));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void handleTextSegment(final NitfReader reader, final long dataLength) throws NitfFormatException {
        nitfStorage.getTextSegments().add(readTextSegment(reader, dataLength, false));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void handleDataExtensionSegment(final NitfReader reader, final long dataLength) throws NitfFormatException {
        nitfStorage.getDataExtensionSegments().add(readDataExtensionSegment(reader, false, dataLength));
    }
}
