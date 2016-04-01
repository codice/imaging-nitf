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
import org.codice.imaging.nitf.core.dataextension.DataExtensionSegment;
import org.codice.imaging.nitf.core.graphic.GraphicSegment;
import org.codice.imaging.nitf.core.header.NitfHeader;
import org.codice.imaging.nitf.core.image.ImageSegment;
import org.codice.imaging.nitf.core.label.LabelSegment;
import org.codice.imaging.nitf.core.symbol.SymbolSegment;
import org.codice.imaging.nitf.core.text.TextSegment;

/**
 * Data source for NITF file parts.
 */
public interface DataSource {

    /**
     * Return the file-level header for the parsed file.
     *
     * @return the file-level header
     */
    NitfHeader getNitfHeader();

    /**
     * Stores the NITF header.
     *
     * @param nitfFileHeader - the NITF header to set.
     */
    void setNitfHeader(final NitfHeader nitfFileHeader);

    /**
     * Return the image segment headers associated with this file.
     *
     * @return image segment headers
     */
    List<ImageSegment> getImageSegments();

    /**
     * Return the graphic segment headers associated with this file.
     *
     * @return graphic segment headers
     */
    List<GraphicSegment> getGraphicSegments();

    /**
     * Return the symbol segment headers associated with this file.
     *
     * @return symbol segment headers
     */
    List<SymbolSegment> getSymbolSegments();

    /**
     * Return the label segments associated with this file.
     *
     * @return label segments
     */
    List<LabelSegment> getLabelSegments();

    /**
     * Return the text segments associated with this file.
     *
     * @return text segments
     */
    List<TextSegment> getTextSegments();

    /**
     * Return the data extension segments associated with this file.
     *
     * @return data extension segments
     */
    List<DataExtensionSegment> getDataExtensionSegments();
}
