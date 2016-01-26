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
package org.codice.imaging.nitf.core.common;

import java.text.ParseException;

/**
    Interface for reader.
*/
public interface NitfReader {

    /**
        Set the file type (NITF version).

        @param fileType the file type
    */
    void setFileType(final FileType fileType);

    /**
        Return the file type (NITF version).

        @return the file type
    */
    FileType getFileType();

    /**
        Return whether the reader can seek.

        @return true if the reader can seek, otherwise false.
    */
    Boolean canSeek();

    /**
        Seek to the end of the file.
        <p>
        This is only valid if the reader can seek.

        @throws ParseException if there was a problem performing the required operation.
    */
    void seekToEndOfFile() throws ParseException;

    /**
        Seek backwards from the current position.
        <p>
        This is only valid if the reader can seek.

        @param relativeOffset the number of bytes to seek backwards.
        @throws ParseException if there was a problem performing the required operation.
    */
    void seekBackwards(final long relativeOffset) throws ParseException;

    /**
        Seek to an absolute position.
        <p>
        This is only valid if the reader can seek.

        @param absoluteOffset the position to.
        @throws ParseException if there was a problem performing the required operation.
    */
    void seekToAbsoluteOffset(final long absoluteOffset) throws ParseException;

    /**
        Return the current offset into the NITF file.

        @return the number of bytes from the start of the NITF file
    */
    long getCurrentOffset();

    /**
        Check if the NITF file contains an expected "magic" header value.
        <p>
        This is intended to verify header values and other constants during parsing.

        @param magicHeader the string content to match.
        @throws ParseException if the content did not match, or something else went wrong during parsing (e.g. end of file).
    */
    void verifyHeaderMagic(final String magicHeader) throws ParseException;

    /**
        Read an integer value from the file.
        <p>
        This is intended to read a given number of bytes, and interpret them as a String representing an integer value.
        <p>
        For example, the next three bytes might be 0x30, 0x34, 0x32. That corresponds to characters: "0", "4", "2",
        so this method returns 42.

        @param count the number of bytes to read and convert to an integer.
        @return integer representation of the specified number of bytes.
        @throws ParseException if the content could not be converted, or something else went wrong during parsing (e.g. end of file).
    */
    Integer readBytesAsInteger(final int count) throws ParseException;

    /**
        Read a long integer value from the file.
        <p>
        This is intended to read a given number of bytes, and interpret them as a String representing a long integer value.
        <p>
        For example, the next three bytes might be 0x30, 0x34, 0x32. That corresponds to characters: "0", "4", "2",
        so this method returns 42.

        @param count the number of bytes to read and convert to a long integer.
        @return long integer representation of the specified number of bytes.
        @throws ParseException if the content could not be converted, or something else went wrong during parsing (e.g. end of file).
    */
    Long readBytesAsLong(final int count) throws ParseException;

    /**
        Read a double value from the file.
        <p>
        This is intended to read a given number of bytes, and interpret them as a String representing a double value.
        <p>
        For example, the next four bytes might be 0x30, 0x34, 0x2E, 0x32. That corresponds to characters: "0", "4", ".", "2",
        so this method returns 4.2.

        @param count the number of bytes to read and convert to a double.
        @return double representation of the specified number of bytes.
        @throws ParseException if the content could not be converted, or something else went wrong during parsing (e.g. end of file).
    */
    Double readBytesAsDouble(final int count) throws ParseException;

    /**
        Read a string from the file, removing any trailing whitespace.

        @param count the number of bytes to read.
        @return trimmed string contents.
        @throws ParseException if something went wrong during parsing (e.g. end of file).
    */
    String readTrimmedBytes(final int count) throws ParseException;

    /**
        Read a string from the file.

        @param count the number of bytes to read.
        @return string contents.
        @throws ParseException if something went wrong during parsing (e.g. end of file).
    */
    String readBytes(final int count) throws ParseException;

    /**
        Read bytes from the file.

        @param count the number of bytes to read.
        @return byte array of the file content.
        @throws ParseException if something went wrong during parsing (e.g. end of file).
    */
    byte[] readBytesRaw(final int count) throws ParseException;

    /**
        Skip over file contents.
        <p>
        This is used to ignore some of the file content (e.g. data segments, when only parsing out the metadata).

        @param count the number of bytes to skip.
        @throws ParseException if something went wrong during parsing (e.g. end of file).
    */
    void skip(final long count) throws ParseException;
}
