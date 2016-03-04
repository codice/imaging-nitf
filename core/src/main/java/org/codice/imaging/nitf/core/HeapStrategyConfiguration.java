package org.codice.imaging.nitf.core;

import java.util.function.Predicate;

/**
 * Aggregates configuration data for the ConifigurableImageDataStrategy class.
 */
public class HeapStrategyConfiguration {
    private long maximumSegmentSize;

    private Predicate<Long> tempFilePredicate = length -> true;

    private Predicate<Long> maximumFileSizePredicate = length -> length <= maximumSegmentSize;

    /**
     * @param maximumSize the maximum size of an image that will be processed in terms of bytes.
     * @param temporaryFilePredicate  images will be stored in temporary files when the test()
     *                                method of this predicate returns true.
     */
    public HeapStrategyConfiguration(final long maximumSize,
            final Predicate<Long> temporaryFilePredicate) {
        this(maximumSize);

        if (temporaryFilePredicate != null) {
            this.tempFilePredicate = temporaryFilePredicate;
        }
    }

    /**
     * @param maximumSize the maximum size of an image that will be processed in terms of bytes.
     *
     **/
    public HeapStrategyConfiguration(final long maximumSize) {
        this.maximumSegmentSize = maximumSize;
    }

    /**
     * @return the predicate used to determine whether the undrendered image data should be stored
     * in memory or in temporary files.
     */
    public final Predicate<Long> temporaryFilePredicate() {
        return this.tempFilePredicate;
    }

    /**
     * @return the predicate used to determine whether the requested data read length exceeds
     *         'maximumSize'.
     */
    public final Predicate<Long> maximumFileSizePredicate() {
        return this.maximumFileSizePredicate;
    }
}
