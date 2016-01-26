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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.channels.Channels;
import java.text.ParseException;
import java.util.logging.Level;
import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.ImageInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
    NitfReader implementation using a (random access) File.
*/
public class FileReader extends SharedReader implements NitfReader {
    // Error Messages
    static final String GENERIC_READ_ERROR_MESSAGE = "Error reading from NITF file: ";
    static final String FILE_NOT_FOUND_EXCEPTION_MESSAGE = "File Not Found Exception opening file:";
    static final String NOT_FOUND_MESSAGE_JOINER = " not found: ";

    static final String READ_MODE = "r";

    private static final Logger LOG = LoggerFactory.getLogger(FileReader.class);

    private RandomAccessFile nitfFile = null;

    /**
        Constructor for File.

        @param file the File to read the NITF file contents from.
        @throws ParseException if file does not exist as a regular file, or some other errors occurs during opening of the file.
    */
    public FileReader(final File file) throws ParseException {
        try {
            nitfFile = makeRandomAccessFile(file, READ_MODE);
        } catch (FileNotFoundException ex) {
            LOG.warn(FILE_NOT_FOUND_EXCEPTION_MESSAGE + file.getPath(), ex);
            throw new ParseException(file.getPath() + NOT_FOUND_MESSAGE_JOINER +  ex.getMessage(), 0);
        }
    }

    /**
        Constructor for string file name.

        @param filename the name of the file to read the NITF file contents from.
        @throws ParseException if file does not exist as a regular file, or some other errors occurs during opening of the file.
    */
    public FileReader(final String filename) throws ParseException {
        try {
            nitfFile = makeRandomAccessFile(filename, READ_MODE);
        } catch (FileNotFoundException ex) {
            LOG.warn(FILE_NOT_FOUND_EXCEPTION_MESSAGE + filename, ex);
            throw new ParseException(filename + NOT_FOUND_MESSAGE_JOINER +  ex.getMessage(), 0);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Boolean canSeek() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final long getCurrentOffset() {
        try {
            return nitfFile.getFilePointer();
        } catch (IOException ex) {
            LOG.warn("IO Exception getting file pointer", ex);
            return 0;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void seekToEndOfFile() throws ParseException {
        try {
            nitfFile.seek(nitfFile.length());
        } catch (IOException ex) {
            LOG.warn("IO Exception seeking to end of file: {}", ex);
            throw new ParseException("Unable to seek to end of file: " + ex.getMessage(), 0);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void seekBackwards(final long relativeOffset) throws ParseException {
        try {
            nitfFile.seek(nitfFile.getFilePointer() - relativeOffset);
        } catch (IOException ex) {
            LOG.warn("IO Exception seeking backwards", ex);
            throw new ParseException("Unable to seek backwards: " + ex.getMessage(), 0);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void seekToAbsoluteOffset(final long absoluteOffset) throws ParseException {
        try {
            nitfFile.seek(absoluteOffset);
        } catch (IOException ex) {
            LOG.warn("IO Exception seeking to absolute offset", ex);
            throw new ParseException("Unable to seek to absolute offset: " + ex.getMessage(), 0);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final byte[] readBytesRaw(final int count) throws ParseException {
        int currentOffset = 0;
        try {
            currentOffset = (int) nitfFile.getFilePointer();
            byte[] bytes = new byte[count];
            nitfFile.readFully(bytes);
            return bytes;
        } catch (IOException ex) {
            LOG.warn("IO Exception reading raw bytes", ex);
            throw new ParseException(GENERIC_READ_ERROR_MESSAGE + ex.getMessage(), currentOffset);
        }
    }

    /**
     * {@inheritDoc}
     */
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
            LOG.warn("IO Exception skipping bytes", ex);
            throw new ParseException(GENERIC_READ_ERROR_MESSAGE + ex.getMessage(), currentOffset);
        }
    }

    private RandomAccessFile makeRandomAccessFile(final File file, final String mode) throws FileNotFoundException {
        return new RandomAccessFile(file, mode);
    }

    private RandomAccessFile makeRandomAccessFile(final String filename, final String mode) throws FileNotFoundException {
        return new RandomAccessFile(filename, mode);
    }

    /**
     * Get an input stream at a specified point in the file.
     *
     * @param offset the point in the file the input stream should read from
     * @return input stream for the specified content
     * @throws ParseException if creating the input stream fails.
     */
    public final InputStream getInputStreamAt(final long offset) throws ParseException {
        try {
            return Channels.newInputStream(nitfFile.getChannel().position(offset));
        } catch (IOException ex) {
            throw new ParseException("IOException while getting input stream: " + ex, (int) offset);
        }
    }

    /**
     * Get an image input stream at a specified point in the file.
     *
     * @param offset the point in the file the image input stream should read from
     * @return image input stream for the specified content
     * @throws ParseException if creating the image input stream fails.
     */
    public final ImageInputStream getImageInputStreamAt(final long offset) throws ParseException {
        try {
            ImageInputStream iis = new FileImageInputStream(nitfFile);
            iis.seek(offset);
            return iis;
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(FileReader.class.getName()).log(Level.SEVERE, null, ex);
            throw new ParseException("Error seeking while creating image input stream: " + ex, (int) offset);
        }
    }

}
