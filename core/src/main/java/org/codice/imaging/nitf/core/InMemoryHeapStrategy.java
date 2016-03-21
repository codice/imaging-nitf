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
 * @param <R> the return type for this heap strategy.
 */
public class InMemoryHeapStrategy<R> implements HeapStrategy<R> {
    private static final Logger LOGGER = LoggerFactory.getLogger(InMemoryHeapStrategy.class);

    private final Function<InputStream, R> resultConversionFunction;

    /**
     * @param resultConverter a function that converts a RandomAccessFile to &lt;R&gt;
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
