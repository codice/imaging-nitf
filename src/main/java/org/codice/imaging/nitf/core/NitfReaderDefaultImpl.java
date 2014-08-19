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
package org.codice.imaging.nitf.core;

import java.nio.charset.Charset;
import java.text.ParseException;

/**
    Default implementation for a NitfReader.
    <p>
    This is intended to help with implementing the NitfReader interface, with a
    combination of shared methods and default implementation methods that a concrete
    implementation class can call.
*/
public abstract class NitfReaderDefaultImpl implements NitfReader {

    /**
        The type (version) of NITF file.
        <p>
        This tracks the NITF file type / version. UNKNOWN is the "not set" or
        parse error state.
    */
    private FileType nitfFileType = FileType.UNKNOWN;

    /**
        Standard byte to UTF-8 charset conversion.
    */
    protected static final Charset UTF8_CHARSET = Charset.forName("UTF-8");

    /** {@inheritDoc} */
    public final void setFileType(final FileType fileType) {
        nitfFileType = fileType;
    }

    /** {@inheritDoc} */
    public final FileType getFileType() {
        return nitfFileType;
    }

    /** {@inheritDoc} */
    public final void verifyHeaderMagic(final String magicHeader) throws ParseException {
        String actualHeader = readBytes(magicHeader.length());
        if (!actualHeader.equals(magicHeader)) {
            throw new ParseException(String.format("Missing \'%s\' magic header, got \'%s\'", magicHeader, actualHeader), (int) getCurrentOffset());
        }
    }

    /**
        Default implementation for readBytesAsInteger.
        <p>
        This implements the readBytesAsInteger interface in terms of the underlying readBytes() implementation.

        @param count the number of bytes to read and convert to an integer.
        @return integer representation of the specified number of bytes.
        @throws ParseException if the content could not be converted, or something else went wrong during parsing (e.g. end of file).
    */
    protected final Integer defaultReadBytesAsInteger(final int count) throws ParseException {
        String intString = readBytes(count);
        Integer intValue = 0;
        try {
            intValue = Integer.parseInt(intString);
        } catch (NumberFormatException ex) {
            throw new ParseException(String.format("Bad Integer format: [%s]", intString), (int) getCurrentOffset());
        }
        return intValue;
    }

    /**
        Default implementation for readBytesAsLong.
        <p>
        This implements the readBytesAsLong interface in terms of the underlying readBytes() implementation.

        @param count the number of bytes to read and convert to a long integer.
        @return long integer representation of the specified number of bytes.
        @throws ParseException if the content could not be converted, or something else went wrong during parsing (e.g. end of file).
    */
    protected final Long defaultReadBytesAsLong(final int count) throws ParseException {
        String longString = readBytes(count);
        Long longValue = 0L;
        try {
            longValue = Long.parseLong(longString);
        } catch (NumberFormatException ex) {
            throw new ParseException(String.format("Bad Long format: %s", longString), (int) getCurrentOffset());
        }
        return longValue;
    }

    /**
        Default implementation for readBytesAsDouble.
        <p>
        This implements the readBytesAsDouble interface in terms of the underlying readBytes() implementation.

        @param count the number of bytes to read and convert to a double.
        @return double representation of the specified number of bytes.
        @throws ParseException if the content could not be converted, or something else went wrong during parsing (e.g. end of file).
    */
    protected final Double defaultReadBytesAsDouble(final int count) throws ParseException {
        String doubleString = readBytes(count);
        Double doubleValue = 0.0;
        try {
            doubleValue = Double.parseDouble(doubleString.trim());
        } catch (NumberFormatException ex) {
            throw new ParseException(String.format("Bad Double format: %s", doubleString), (int) getCurrentOffset());
        }
        return doubleValue;
    }

    /**
        Default implementation for readTrimmedBytes.
        <p>
        This implements the readTrimmedBytes() interface in terms of the underlying readBytes() implementation.

        @param count the number of bytes to read.
        @return trimmed string contents.
        @throws ParseException if something went wrong during parsing (e.g. end of file).
    */
    protected final String defaultReadTrimmedBytes(final int count) throws ParseException {
        return rightTrim(readBytes(count));
    }

    /**
        Convenience routine to remove whitespace only from the right hand end of the string.

        @param s the string to trim.
        @return the string with all whitespace on the right hand end removed.
    */
    public static String rightTrim(final String s) {
        int i = s.length() - 1;
        while ((i >= 0) && Character.isWhitespace(s.charAt(i))) {
            i--;
        }
        return s.substring(0, i + 1);
    }

    /**
        Default implementation for readBytes.
        <p>
        This implements the readBytes interface in terms of the underlying readBytesRaw() implementation.

        @param count the number of bytes to read.
        @return string contents.
        @throws ParseException if something went wrong during parsing (e.g. end of file).
    */
    protected final String defaultReadBytes(final int count) throws ParseException {
        return new String(readBytesRaw(count), UTF8_CHARSET);
    }

}
