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

import java.io.BufferedInputStream;
import java.io.IOException;
import java.text.ParseException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
    NitfReader implementation using an InputStream.
*/
class NitfInputStreamReader extends NitfReaderDefaultImpl implements NitfReader {

    private static final Logger LOG = LoggerFactory.getLogger(NitfInputStreamReader.class);

    private BufferedInputStream input = null;
    private long numBytesRead = 0;

    private static final String GENERIC_READ_ERROR_MESSAGE = "Error reading from NITF stream: ";

    /**
        Constructor.

        @param nitfInputStream the input stream to read the NITF file contents from.
    */
    public NitfInputStreamReader(final BufferedInputStream nitfInputStream) {
        input = nitfInputStream;
    }

    @Override
    public final Boolean canSeek() {
        return false;
    }

    @Override
    public final void seekToEndOfFile() {
        // Nothing - we can't seek.
    }

    @Override
    public final void seekBackwards(final long relativeOffset) {
        // Nothing - we can't seek.
    }

    @Override
    public final void seekToAbsoluteOffset(final long absoluteOffset) {
        // Nothing - we can't seek.
    }

    @Override
    public final long getCurrentOffset() {
        return numBytesRead;
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
    public final byte[] readBytesRaw(final int count) throws ParseException {
        try {
            byte[] bytes = new byte[count];
            int thisRead = input.read(bytes, 0, count);
            if (thisRead == -1) {
                throw new ParseException("End of file reading from NITF stream.", (int) numBytesRead);
            } else if (thisRead < count) {
                throw new ParseException(String.format("Short read while reading from NITF stream (%s/%s).", thisRead, count),
                                         (int) (numBytesRead + thisRead));
            }
            numBytesRead += thisRead;
            return bytes;
        } catch (IOException ex) {
            LOG.warn("IO Exception reading raw bytes: {}", ex.getMessage());
            throw new ParseException(GENERIC_READ_ERROR_MESSAGE + ex.getMessage(), (int) numBytesRead);
        }
    }

    @Override
    public final void skip(final long count) throws ParseException {
        long bytesToRead = count;
        try {
            long thisRead = 0;
            do {
                thisRead = input.skip(bytesToRead);
                numBytesRead += thisRead;
                bytesToRead -= thisRead;
            } while (bytesToRead > 0);
        } catch (IOException ex) {
            LOG.warn("IO Exception skipping bytes: {}", ex.getMessage());
            throw new ParseException(GENERIC_READ_ERROR_MESSAGE + ex.getMessage(), (int) numBytesRead);
        }
    }
}
