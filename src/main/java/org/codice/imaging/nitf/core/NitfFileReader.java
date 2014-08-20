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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.ParseException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
    NitfReader implementation using a (random access) File.
*/
class NitfFileReader extends NitfReaderDefaultImpl implements NitfReader {

    private static final Logger LOG = LoggerFactory.getLogger(NitfFileReader.class);

    private RandomAccessFile nitfFile = null;

    private static final String GENERIC_READ_ERROR_MESSAGE = "Error reading from NITF file: ";
    private static final String READ_MODE = "r";

    /**
        Constructor for File.

        @param file the File to read the NITF file contents from.
        @throws FileNotFoundException if file does not exist as a regular file, or some other errors occurs during opening of the file.
    */
    public NitfFileReader(final File file) throws FileNotFoundException {
        nitfFile = new RandomAccessFile(file, READ_MODE);
    }

    /**
        Constructor for string file name.

        @param filename the name of the file to read the NITF file contents from.
        @throws FileNotFoundException if file does not exist as a regular file, or some other errors occurs during opening of the file.
    */
    public NitfFileReader(final String filename) throws FileNotFoundException {
        nitfFile = new RandomAccessFile(filename, READ_MODE);
    }

    @Override
    public final Boolean canSeek() {
        return true;
    }

    @Override
    public final long getCurrentOffset() {
        try {
            return nitfFile.getFilePointer();
        } catch (IOException ex) {
            LOG.warn("IO Exception getting file pointer: {}", ex.getMessage());
            return 0;
        }
    }

    @Override
    public final Integer readBytesAsInteger(final int count) throws ParseException {
        return defaultReadBytesAsInteger(count);
    }

    @Override
    public final Long readBytesAsLong(final int count) throws ParseException {
        return defaultReadBytesAsLong(count);
    }

    @Override
    public final Double readBytesAsDouble(final int count) throws ParseException {
        return defaultReadBytesAsDouble(count);
    }

    @Override
    public final String readTrimmedBytes(final int count) throws ParseException {
        return defaultReadTrimmedBytes(count);
    }

    @Override
    public final String readBytes(final int count) throws ParseException {
        return defaultReadBytes(count);
    }

    @Override
    public final void seekToEndOfFile() throws ParseException {
        try {
            nitfFile.seek(nitfFile.length());
        } catch (IOException ex) {
            LOG.warn("IO Exception seeking to end of file: {}", ex.getMessage());
            throw new ParseException("Unable to seek to end of file: " + ex.getMessage(), 0);
        }
    }

    @Override
    public final void seekBackwards(final long relativeOffset) throws ParseException {
        try {
            nitfFile.seek(nitfFile.getFilePointer() - relativeOffset);
        } catch (IOException ex) {
            LOG.warn("IO Exception seeking backwards: {}", ex.getMessage());
            throw new ParseException("Unable to seek backwards: " + ex.getMessage(), 0);
        }
    }

    @Override
    public final void seekToAbsoluteOffset(final long absoluteOffset) throws ParseException {
        try {
            nitfFile.seek(absoluteOffset);
        } catch (IOException ex) {
            LOG.warn("IO Exception seeking to absolute offset: {}", ex.getMessage());
            throw new ParseException("Unable to seek to absolute offset: " + ex.getMessage(), 0);
        }
    }

    @Override
    public final byte[] readBytesRaw(final int count) throws ParseException {
        int currentOffset = 0;
        try {
            currentOffset = (int) nitfFile.getFilePointer();
            byte[] bytes = new byte[count];
            nitfFile.readFully(bytes);
            return bytes;
        } catch (IOException ex) {
            LOG.warn("IO Exception reading raw bytes: {}", ex.getMessage());
            throw new ParseException(GENERIC_READ_ERROR_MESSAGE + ex.getMessage(), currentOffset);
        }
    }

    @Override
    public final void skip(final long count) throws ParseException {
        long bytesToRead = count;
        int currentOffset = 0;
        try {
            currentOffset = (int) nitfFile.getFilePointer();
            long thisRead = 0;
            do {
                thisRead = nitfFile.skipBytes((int) bytesToRead);
                bytesToRead -= thisRead;
            } while (bytesToRead > 0);
        } catch (IOException ex) {
            LOG.warn("IO Exception skipping bytes: {}", ex.getMessage());
            throw new ParseException(GENERIC_READ_ERROR_MESSAGE + ex.getMessage(), currentOffset);
        }
    }
}
