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

import org.codice.imaging.nitf.core.header.NitfHeader;
import org.codice.imaging.nitf.core.tre.TreCollection;
import org.codice.imaging.nitf.core.tre.TreSource;

/**
 * Strategy for parsing NITF file components.
 */
public interface NitfParseStrategy {

    /**
     * Set the file-level header for this parsed file.
     *
     * @param nitfHeader the file-level header
     */
    void setFileHeader(NitfHeader nitfHeader);

    /**
     * Return the file-level header for the parsed file.
     *
     * @return the file-level header
     */
    NitfHeader getNitfHeader();

    /**
     * Parse and return the TREs.
     *
     * @param reader the reader to read the TRE data from
     * @param length the length of the TRE data (for all TREs)
     * @param source the source segment part for the TRE (where it is being read
     * from)
     * @return TRE collection for this header part
     * @throws NitfFormatException if there is a problem loading the TRE descriptions, or in parsing TREs.
     */
    TreCollection parseTREs(NitfReader reader, int length, TreSource source) throws NitfFormatException;

    /**
     * Handle the text segment header and data.
     *
     * @param reader the reader to use, assumed to be positioned at the start of the header
     * @param dataLength the length of the data in this segment.
     * @throws NitfFormatException if there is a problem handling the segment
     */
    void handleTextSegment(final NitfReader reader, final long dataLength) throws NitfFormatException;

    /**
     * Handle the data extension segment header and data.
     *
     * @param reader the reader to use, assumed to be positioned at the start of the header
     * @param dataLength the length of the data in this segment.
     * @throws NitfFormatException if there is a problem handling the segment
     */
    void handleDataExtensionSegment(final NitfReader reader, final long dataLength) throws NitfFormatException;

    /**
     * Handle the graphic segment header and data.
     *
     * @param reader the reader to use, assumed to be positioned at the start of the header
     * @param dataLength the length of the data in this segment.
     * @throws NitfFormatException if there is a problem handling the segment
     */
    void handleGraphicSegment(final NitfReader reader, final long dataLength) throws NitfFormatException;

    /**
     * Handle the image segment header and data.
     *
     * @param reader the reader to use, assumed to be positioned at the start of the header
     * @param dataLength the length of the data in this segment.
     * @throws NitfFormatException if there is a problem handling the segment
     */
    void handleImageSegment(final NitfReader reader, final long dataLength) throws NitfFormatException;

    /**
     * Handle the label segment header and data.
     *
     * @param reader the reader to use, assumed to be positioned at the start of the header
     * @param dataLength the length of the data in this segment.
     * @throws NitfFormatException if there is a problem handling the segment
     */
    void handleLabelSegment(final NitfReader reader, final long dataLength) throws NitfFormatException;

    /**
     * Handle the symbol segment header and data.
     *
     * @param reader the reader to use, assumed to be positioned at the start of the header
     * @param dataLength the length of the data in this segment.
     * @throws NitfFormatException if there is a problem handling the segment
     */
    void handleSymbolSegment(final NitfReader reader, final long dataLength) throws NitfFormatException;

}
