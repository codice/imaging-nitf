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

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;
import org.codice.imaging.nitf.core.NitfDataSource;
import org.codice.imaging.nitf.core.NitfFileHeader;
import org.codice.imaging.nitf.core.common.CommonSegment;
import org.codice.imaging.nitf.core.dataextension.DataExtensionSegment;
import org.codice.imaging.nitf.core.graphic.GraphicSegment;
import org.codice.imaging.nitf.core.image.ImageSegment;
import org.codice.imaging.nitf.core.label.LabelSegment;
import org.codice.imaging.nitf.core.symbol.SymbolSegment;
import org.codice.imaging.nitf.core.text.TextSegment;

/**
 * The NitfSegmentsFlow provides methods for processing the contents of the NITF file.
 */
public class NitfSegmentsFlow {

    private NitfDataSource mDataSource;

    NitfSegmentsFlow(final NitfDataSource dataSource) {
        if (dataSource == null) {
            throw new IllegalArgumentException(
                    "ImageSegmentFlow(): constructor argument 'dataSource' may not be null.");
        }

        mDataSource = dataSource;
    }

    /**
     * Passes the NITF file header to the supplied consumer.
     *
     * @param consumer the consumer to pass the NITF file header to.
     * @return this NitfSegmentsFlow.
     */
    public final NitfSegmentsFlow fileHeader(final Consumer<NitfFileHeader> consumer) {
        NitfFileHeader nitfFileHeader = mDataSource.getNitfHeader();
        consumer.accept(nitfFileHeader);
        return this;
    }

    /**
     * Iterates over the images in the NITF file and passes them to the supplied consumer.
     *
     * @param consumer The consumer to pass the image segment to.
     * @return this NitfSegmentsFlow.
     */
    public final NitfSegmentsFlow forEachImageSegment(final Consumer<ImageSegment> consumer) {
        return forEachSegment(consumer, () -> mDataSource.getImageSegments());
    }

    /**
     * Iterates over the data extension segments in the NITF file and passes them to the supplied consumer.
     *
     * @param consumer The consumer to pass the data extension segment to.
     * @return this NitfSegmentsFlow.
     */
    public final NitfSegmentsFlow forEachDataExtensionSegment(final Consumer<DataExtensionSegment> consumer) {
        return forEachSegment(consumer, () -> mDataSource.getDataExtensionSegments());
    }

    /**
     * Iterates over the text segments in the NITF file and passes them to the supplied consumer.
     *
     * @param consumer The consumer to pass the text segment to.
     * @return this NitfSegmentsFlow.
     */
    public final NitfSegmentsFlow forEachTextSegment(final Consumer<TextSegment> consumer) {
        return forEachSegment(consumer, () -> mDataSource.getTextSegments());
    }

    /**
     * Iterates over the graphic segments in the NITF file and passes them to the supplied consumer.
     *
     * @param consumer The consumer to pass the graphic segment to.
     * @return this NitfSegmentsFlow.
     */
    public final NitfSegmentsFlow forEachGraphicSegment(final Consumer<GraphicSegment> consumer) {
        return forEachSegment(consumer, () -> mDataSource.getGraphicSegments());
    }

    /**
     * Iterates over the label segments in the NITF file and passes them to the supplied consumer.
     *
     * @param consumer The consumer to pass the label segment to.
     * @return this NitfSegmentsFlow.
     */
    public final NitfSegmentsFlow forEachLabelSegment(final Consumer<LabelSegment> consumer) {
        return forEachSegment(consumer, () -> mDataSource.getLabelSegments());
    }

    /**
     * Iterates over the symbol segments in the NITF file and passes them to the supplied consumer.
     *
     * @param consumer The consumer to pass the symbol segment to.
     * @return this NitfSegmentsFlow.
     */
    public final NitfSegmentsFlow forEachSymbolSegment(final Consumer<SymbolSegment> consumer) {
        return forEachSegment(consumer, () -> mDataSource.getSymbolSegments());
    }

    /**
     * passes this NitfSegmentParserFlow's NitfDataSource to the supplied consumer.
     *
     * @param dataSourceConsumer the consumer for the data source
     * @return this NitfSegmentsFlow
     */
    public final NitfSegmentsFlow dataSource(final Consumer<NitfDataSource> dataSourceConsumer) {
        dataSourceConsumer.accept(mDataSource);
        return this;
    }

    private <T extends CommonSegment> NitfSegmentsFlow forEachSegment(final Consumer<T> consumer, final Supplier<List<T>> supplier) {
        if (consumer != null && supplier != null) {
            List<T> segments = supplier.get();

            if (segments != null) {
                for (T segment : segments) {
                    consumer.accept(segment);
                }
            }
        }

        return this;
    }
}
