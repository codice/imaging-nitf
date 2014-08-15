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

import java.text.ParseException;

/**
    Interface for reader.
*/
public interface NitfReader {
    void setFileType(final FileType fileType);

    FileType getFileType();

    Boolean canSeek();

    int getCurrentOffset();

    void verifyHeaderMagic(final String magicHeader) throws ParseException;

    Integer readBytesAsInteger(final int count) throws ParseException;

    Long readBytesAsLong(final int count) throws ParseException;

    Double readBytesAsDouble(final int count) throws ParseException;

    String readTrimmedBytes(final int count) throws ParseException;

    String readBytes(final int count) throws ParseException;

    byte[] readBytesRaw(final int count) throws ParseException;

    void skip(final long count) throws ParseException;
}
