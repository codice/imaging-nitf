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

import java.nio.charset.Charset;
import java.text.ParseException;

public abstract class NitfReader {
    private FileType nitfFileType = FileType.UNKNOWN;

    private static final Charset UTF8_CHARSET = Charset.forName("UTF-8");
    private static final String GENERIC_READ_ERROR_MESSAGE = "Error reading from NITF stream: ";

    public final void setFileType(final FileType fileType) {
        nitfFileType = fileType;
    }

    public final FileType getFileType() {
        return nitfFileType;
    }

    abstract Boolean canSeek();

    abstract int getCurrentOffset();

    public final void verifyHeaderMagic(final String magicHeader) throws ParseException {
        String actualHeader = readBytes(magicHeader.length());
        if (!actualHeader.equals(magicHeader)) {
            throw new ParseException(String.format("Missing \'%s\' magic header, got \'%s\'", magicHeader, actualHeader), getCurrentOffset());
        }
    }

    abstract Integer readBytesAsInteger(final int count) throws ParseException;

    protected final Integer defaultReadBytesAsInteger(final int count) throws ParseException {
        String intString = readBytes(count);
        Integer intValue = 0;
        try {
            intValue = Integer.parseInt(intString);
        } catch (NumberFormatException ex) {
            throw new ParseException(String.format("Bad Integer format: [%s]", intString), getCurrentOffset());
        }
        return intValue;
    }

    abstract Long readBytesAsLong(final int count) throws ParseException;

    protected final Long defaultReadBytesAsLong(final int count) throws ParseException {
        String longString = readBytes(count);
        Long longValue = 0L;
        try {
            longValue = Long.parseLong(longString);
        } catch (NumberFormatException ex) {
            throw new ParseException(String.format("Bad Long format: %s", longString), getCurrentOffset());
        }
        return longValue;
    }

    abstract Double readBytesAsDouble(final int count) throws ParseException;

    protected final Double defaultReadBytesAsDouble(final int count) throws ParseException {
        String doubleString = readBytes(count);
        Double doubleValue = 0.0;
        try {
            doubleValue = Double.parseDouble(doubleString.trim());
        } catch (NumberFormatException ex) {
            throw new ParseException(String.format("Bad Double format: %s", doubleString), getCurrentOffset());
        }
        return doubleValue;
    }

    abstract String readTrimmedBytes(final int count) throws ParseException;

    protected final String defaultReadTrimmedBytes(final int count) throws ParseException {
        return rightTrim(readBytes(count));
    }

    public static String rightTrim(final String s) {
        int i = s.length() - 1;
        while ((i >= 0) && Character.isWhitespace(s.charAt(i))) {
            i--;
        }
        return s.substring(0, i + 1);
    }

    abstract String readBytes(final int count) throws ParseException;

    protected final String defaultReadBytes(final int count) throws ParseException {
        return new String(readBytesRaw(count), UTF8_CHARSET);
    }

    public abstract byte[] readBytesRaw(final int count) throws ParseException;

    public abstract void skip(final long count) throws ParseException;
}
