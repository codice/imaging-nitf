package org.codice.imaging.nitf.core;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.ParseException;
import java.util.function.Function;

import org.codice.imaging.nitf.core.common.NitfReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An implementation of HeapStrategy which returns an ImageInputStream backed by an
 * in-memory stream of bytes.
 *
 * @param <R> - the return type for this heap strategy.
 */
public class InMemoryHeapStrategy<R> implements HeapStrategy<R> {
    private static final Logger LOGGER = LoggerFactory.getLogger(InMemoryHeapStrategy.class);

    private Function<InputStream, R> resultConversionFunction;

    /**
     * @param resultConverter - a function that converts a RandomAccessFile to <R>
     */
    public InMemoryHeapStrategy(final Function<InputStream, R> resultConverter) {
        this.resultConversionFunction = resultConverter;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final R handleSegment(final NitfReader reader, final long length)
            throws ParseException {
        LOGGER.info(String.format("Storing %s bytes in heap space.", length));
        ByteArrayInputStream inputStream = new ByteArrayInputStream(
                reader.readBytesRaw((int) length));
        R result = resultConversionFunction.apply(inputStream);
        return result;
    }
}
