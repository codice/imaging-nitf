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
package org.codice.imaging.nitf.core.impl;

import java.util.function.LongPredicate;

/**
 * Aggregates configuration data for the ConfigurableHeapStrategy class.
 *
 * There are two configuration settings.
 *
 * One setting determines whether the data is stored in a temporary file or in memory. That corresponds to the
 * temporaryFilePredicate parameter.
 *
 * The other setting determines whether data is stored at all, as opposed to being skipped. That corresponds to the
 * maximumFileSizePredicate, and maximumSize parameter. If the maximumFileSizePredicate results in data not being
 * stored, the temporaryFilePredicate has no effect.
 */
public class HeapStrategyConfiguration {
    private long maximumSegmentSize = Long.MAX_VALUE;

    private LongPredicate tempFilePredicate = length -> true;

    private final LongPredicate maximumFileSizePredicate = length -> length <= maximumSegmentSize;

    /**
     * HeapStrategyConfiguration that allows selective storage.
     *
     * @param temporaryFilePredicate segment data will be stored in temporary files when the test() method of this
     * predicate returns true.
     */
    public HeapStrategyConfiguration(final LongPredicate temporaryFilePredicate) {
        if (temporaryFilePredicate != null) {
            this.tempFilePredicate = temporaryFilePredicate;
        }
    }

    /**
     * HeapStrategyConfiguration that limits the maximum file size, and allows selective storage.
     *
     * @param maximumSize the maximum size of segment data that will be processed in units of bytes.
     * @param temporaryFilePredicate segment data will be stored in temporary files when the test() method of this
     * predicate returns true.
     */
    public HeapStrategyConfiguration(final long maximumSize,
            final LongPredicate temporaryFilePredicate) {
        this(maximumSize);

        if (temporaryFilePredicate != null) {
            this.tempFilePredicate = temporaryFilePredicate;
        }
    }

    /**
     * HeapStrategyConfiguration that limits the maximum file size.
     *
     * @param maximumSize the maximum segment data length that will be processed (in units of bytes).
     *
     **/
    public HeapStrategyConfiguration(final long maximumSize) {
        this.maximumSegmentSize = maximumSize;
    }

    /**
     * HeapStrategyConfiguration that does not limit the maximum file size.
     *
     */
    public HeapStrategyConfiguration() {
    }

    /**
     * @return the predicate used to determine whether the segment data should be stored in memory or in temporary
     * files.
     */
    public final LongPredicate temporaryFilePredicate() {
        return this.tempFilePredicate;
    }

    /**
     * @return the predicate used to determine whether the requested data read length exceeds
     *         'maximumSize'.
     */
    public final LongPredicate maximumFileSizePredicate() {
        return this.maximumFileSizePredicate;
    }
}
