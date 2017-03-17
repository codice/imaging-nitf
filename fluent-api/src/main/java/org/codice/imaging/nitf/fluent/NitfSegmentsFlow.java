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

import java.util.function.Consumer;

import org.codice.imaging.nitf.core.DataSource;
import org.codice.imaging.nitf.core.dataextension.DataExtensionSegment;
import org.codice.imaging.nitf.core.graphic.GraphicSegment;
import org.codice.imaging.nitf.core.header.NitfHeader;
import org.codice.imaging.nitf.core.image.ImageSegment;
import org.codice.imaging.nitf.core.label.LabelSegment;
import org.codice.imaging.nitf.core.symbol.SymbolSegment;
import org.codice.imaging.nitf.core.text.TextSegment;

/**
 * The NitfSegmentsFlow provides methods for processing the contents of the NITF file.
 */
public interface NitfSegmentsFlow {

    /**
     * Passes the NITF header to the supplied consumer.
     *
     * @param consumer the consumer to pass the NITF header to.
     * @return this NitfSegmentsFlow.
     */
    NitfSegmentsFlow fileHeader(Consumer<NitfHeader> consumer);

    /**
     * Iterates over the images in the NITF file and passes them to the supplied consumer.
     *
     * @param consumer The consumer to pass the image segment to.
     * @return this NitfSegmentsFlow.
     */
    NitfSegmentsFlow forEachImageSegment(Consumer<ImageSegment> consumer);

    /**
     * Iterates over the data extension segments in the NITF file and passes them to the supplied consumer.
     *
     * @param consumer The consumer to pass the data extension segment to.
     * @return this NitfSegmentsFlow.
     */
    NitfSegmentsFlow forEachDataExtensionSegment(Consumer<DataExtensionSegment> consumer);

    /**
     * Iterates over the text segments in the NITF file and passes them to the supplied consumer.
     *
     * @param consumer The consumer to pass the text segment to.
     * @return this NitfSegmentsFlow.
     */
    NitfSegmentsFlow forEachTextSegment(Consumer<TextSegment> consumer);

    /**
     * Iterates over the graphic segments in the NITF file and passes them to the supplied consumer.
     *
     * @param consumer The consumer to pass the graphic segment to.
     * @return this NitfSegmentsFlow.
     */
    NitfSegmentsFlow forEachGraphicSegment(Consumer<GraphicSegment> consumer);

    /**
     * Iterates over the label segments in the NITF file and passes them to the supplied consumer.
     *
     * @param consumer The consumer to pass the label segment to.
     * @return this NitfSegmentsFlow.
     */
    NitfSegmentsFlow forEachLabelSegment(Consumer<LabelSegment> consumer);

    /**
     * Iterates over the symbol segments in the NITF file and passes them to the supplied consumer.
     *
     * @param consumer The consumer to pass the symbol segment to.
     * @return this NitfSegmentsFlow.
     */
    NitfSegmentsFlow forEachSymbolSegment(Consumer<SymbolSegment> consumer);

    /**
     * passes this NitfSegmentParserFlow's DataSource to the supplied consumer.
     *
     * @param dataSourceConsumer the consumer for the data source
     * @return this NitfSegmentsFlow
     */
    NitfSegmentsFlow dataSource(Consumer<DataSource> dataSourceConsumer);

    /**
     * Performs necessary cleanup.
     */
    void end();
}
