/**
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
 **/
package org.codice.nitf.filereader;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.ParseException;

public class NitfInputStreamReader extends NitfReaderDefaultImpl implements NitfReader {
    private BufferedInputStream input = null;
    private int numBytesRead = 0;

    private static final Charset UTF8_CHARSET = Charset.forName("UTF-8");
    private static final String GENERIC_READ_ERROR_MESSAGE = "Error reading from NITF stream: ";

    public NitfInputStreamReader(final BufferedInputStream nitfInputStream) throws ParseException {
        input = nitfInputStream;
    }

    @Override
    public final Boolean canSeek() {
        return false;
    }

    @Override
    public final int getCurrentOffset() {
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

    public final byte[] readBytesRaw(final int count) throws ParseException {
        try {
            byte[] bytes = new byte[count];
            int thisRead = input.read(bytes, 0, count);
            if (thisRead == -1) {
                throw new ParseException("End of file reading from NITF stream.", numBytesRead);
            } else if (thisRead < count) {
                throw new ParseException(String.format("Short read while reading from NITF stream (%s/%s).", thisRead, count),
                                         numBytesRead + thisRead);
            }
            numBytesRead += thisRead;
            return bytes;
        } catch (IOException ex) {
            throw new ParseException(GENERIC_READ_ERROR_MESSAGE + ex.getMessage(), numBytesRead);
        }
    }

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
            throw new ParseException(GENERIC_READ_ERROR_MESSAGE + ex.getMessage(), numBytesRead);
        }
    }
}
