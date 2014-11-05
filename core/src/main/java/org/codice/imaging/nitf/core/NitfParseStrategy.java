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

import java.util.List;

/**
 * Strategy for parsing NITF file components.
 */
public interface NitfParseStrategy {

    /**
     * Set the file-level header for this parsed file.
     *
     * @param nitf the file-level header
     */
    void setFileHeader(Nitf nitf);

    /**
     * Return the file-level header for the parsed file.
     *
     * @return the file-level header
     */
    Nitf getNitfHeader();

    /**
     * Return the image segments associated with this file.
     *
     * @return image segments
     */
    List<NitfImageSegmentHeader> getImageSegmentHeaders();

    /**
     * Return the graphic segments associated with this file.
     *
     * @return graphic segments
     */
    List<NitfGraphicSegmentHeader> getGraphicSegments();

    /**
     * Return the symbol segments associated with this file.
     *
     * @return symbol segments
     */
    List<NitfSymbolSegmentHeader> getSymbolSegments();

    /**
     * Return the label segments associated with this file.
     *
     * @return label segments
     */
    List<NitfLabelSegmentHeader> getLabelSegments();

    /**
     * Return the text segments associated with this file.
     *
     * @return text segments
     */
    List<NitfTextSegmentHeader> getTextSegments();

    /**
     * Return the data extension segments associated with this file.
     *
     * @return data extension segments
     */
    List<NitfDataExtensionSegmentHeader> getDataExtensionSegments();

    /**
     * Indication that the "base" file-level headers have been read.
     *
     * @param reader the reader, positioned for reading of the segments
     */
    void baseHeadersRead(NitfReader reader);

}
