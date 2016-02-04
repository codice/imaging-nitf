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

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import javax.imageio.stream.ImageInputStream;
import org.codice.imaging.nitf.core.NitfDataSource;
import org.codice.imaging.nitf.core.NitfFileHeader;
import org.codice.imaging.nitf.core.dataextension.NitfDataExtensionSegmentHeader;
import org.codice.imaging.nitf.core.image.NitfImageSegmentHeader;

/**
 * The NitfSegmentsFlow provides methods for processing the contents of the NITF file.
 */
public class NitfSegmentsFlow {

    private NitfDataSource mDataSource;

    NitfSegmentsFlow(NitfDataSource dataSource) {
        if (dataSource == null) {
            throw new IllegalArgumentException(
                    "ImageSegmentFlow(): constructor argument 'dataSource' may not be null.");
        }

        mDataSource = dataSource;
    }

    /**
     * Iterates over the images in the NITF file and passes them to the supplied consumer.
     *
     * @param consumer The consumer to pass the image header and image data to.
     * @return this NitfSegmentsFlow.
     */
    public NitfSegmentsFlow forEachImage(BiConsumer<NitfImageSegmentHeader, ImageInputStream> consumer) {
        for (int i = 0; i < mDataSource.getImageSegmentHeaders().size(); i++) {
            NitfImageSegmentHeader header = mDataSource.getImageSegmentHeaders().get(i);
            ImageInputStream imageInputStream = mDataSource.getImageSegmentData().get(i);
            consumer.accept(header, imageInputStream);
        }

        return this;
    }

    /**
     * Iterates over the data extension segments in the NITF file and passes them to the supplied consumer.
     *
     * @param consumer The consumer to pass the data extension segment to.
     * @return this NitfSegmentsFlow.
     */
    public NitfSegmentsFlow forEachDataSegment(Consumer<NitfDataExtensionSegmentHeader> consumer) {
        List<NitfDataExtensionSegmentHeader> headers = mDataSource.getDataExtensionSegmentHeaders();

        for (NitfDataExtensionSegmentHeader header : headers) {
            consumer.accept(header);
        }

        return this;
    }

    /**
     * Passes the NITF file header to the supplied consumer.
     *
     * @param consumer the consumer to pass the NITF file header to.
     * @return this NitfSegmentsFlow.
     */
    public NitfSegmentsFlow fileHeader(Consumer<NitfFileHeader> consumer) {
        NitfFileHeader nitfFileHeader = mDataSource.getNitfHeader();
        consumer.accept(nitfFileHeader);
        return this;
    }
}
