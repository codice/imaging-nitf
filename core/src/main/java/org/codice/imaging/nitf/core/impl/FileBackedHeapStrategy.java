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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.util.function.Function;

import org.codice.imaging.nitf.core.HeapStrategy;
import org.codice.imaging.nitf.core.common.NitfFormatException;
import org.codice.imaging.nitf.core.common.NitfReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An implementation of HeapStrategy that stores the image data in a temporary file and
 * returns an FileImageImputStream pointing to that.
 *
 * @param <R> the return type for this heap strategy.
 */
public class FileBackedHeapStrategy<R> implements HeapStrategy<R> {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileBackedHeapStrategy.class);

    private final Function<RandomAccessFile, R> resultConversionFunction;

    private File dataFile;

    private RandomAccessFile randomAccessFile;

    /**
     * @param resultConverter a function that converts a RandomAccessFile to &lt;R&gt;
     */
    public FileBackedHeapStrategy(final Function<RandomAccessFile, R> resultConverter) {
        this.resultConversionFunction = resultConverter;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final R handleSegment(final NitfReader reader, final long dataLength)
            throws NitfFormatException {
        LOGGER.info(String.format("Storing %s bytes in temporary file.", dataLength));
        byte[] bytes = reader.readBytesRaw((int) dataLength);

        try {
            dataFile = File.createTempFile("nitf", (String) null);
            dataFile.deleteOnExit();
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }

        try (FileOutputStream fos = new FileOutputStream(dataFile)) {
            fos.write(bytes);
            this.randomAccessFile = new RandomAccessFile(dataFile, "rwd");
            R result = resultConversionFunction.apply(randomAccessFile);
            return result;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public final void cleanUp() {
        if (dataFile != null) {
            try {
                randomAccessFile.close();
                Files.deleteIfExists(dataFile.toPath());
            } catch (IOException e) {
                LOGGER.warn("Unable to delete file.", e);
            }
        }
    }
}
