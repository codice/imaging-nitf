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

import java.io.DataOutput;
import java.io.IOException;
import javax.imageio.stream.ImageInputStream;
import static org.codice.imaging.nitf.core.common.CommonConstants.STANDARD_DATE_TIME_LENGTH;
import org.codice.imaging.nitf.core.graphic.GraphicSegmentWriter;
import org.codice.imaging.nitf.core.security.SecurityMetadata;
import org.codice.imaging.nitf.core.security.SecurityMetadataWriter;
import org.codice.imaging.nitf.core.tre.TreParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Writer shared implementation details.
 */
public abstract class AbstractSegmentWriter {

    private static final int KILOBYTE = 1024;

    private static final Logger LOG = LoggerFactory.getLogger(AbstractSegmentWriter.class);

    /**
     * Convenient buffer size for copying data.
     */
    protected static final int BUFFER_SIZE = 10 * KILOBYTE;

    /**
     * The target to write the data to.
     */
    protected DataOutput mOutput = null;

    /**
     * The parser to use for TREs.
     */
    protected TreParser mTreParser = null;

    /**
     * Constructor.
     *
     * @param output the target to write to
     * @param treParser TreParser to use to convert TREs.
     */
    public AbstractSegmentWriter(final DataOutput output, final TreParser treParser) {
        mOutput = output;
        mTreParser = treParser;
    }

    private String padNumberToLength(final long number, final int length) {
        return String.format("%0" + length + "d", number);
    }

    private String padStringToLength(final String s, final int length) {
        return String.format("%1$-" + length + "s", s);
    }

    private String hyphenPadStringToLength(final String s, final int length) {
        StringBuilder builder = new StringBuilder(s);
        while (builder.length() < length) {
            builder.append("-");
        }
        return builder.toString();
    }
    /**
     * Write out the fixed field value for the ENCRYP field.
     *
     * @throws IOException on writing failure.
     */
    protected final void writeENCRYP() throws IOException {
        writeFixedLengthString("0", CommonConstants.ENCRYP_LENGTH);
    }

    /**
     * Write out a string of specified fixed length.
     *
     * If the string is not the same as the specified length, an exception will
     * be thrown.
     *
     * @param s the string to write
     * @param length the length that the string should be.
     * @throws IOException on writing problems.
     */
    protected final void writeBytes(final String s, final int length) throws IOException {
        if (s.length() != length) {
            String problem = String.format("String %s was not of expected length %d", s, length);
            LOG.error(problem);
            throw new IllegalArgumentException(problem);
        }
        mOutput.writeBytes(s);
    }

    /**
     * Write out a byte array of specified fixed length.
     *
     * If the array is not the same as the specified length, an exception will
     * be thrown.
     *
     * @param b the byte array to write
     * @param length the length that the string should be.
     * @throws IOException on writing problems.
     */
    protected final void writeBytes(final byte[] b, final int length) throws IOException {
        if (b.length != length) {
            String problem = String.format("Array was length %d, and not expected length %d", b.length, length);
            LOG.error(problem);
            throw new IllegalArgumentException(problem);
        }
        mOutput.write(b);
    }

    /**
     * Write out a string of fixed length, padding with spaces if necessary.
     *
     * If the string is longer than the specified length, it will be truncated,
     * and the event logged.
     *
     * @param s the string to write
     * @param length the length that the string should be.
     * @throws IOException on writing problems.
     */
    protected final void writeFixedLengthString(final String s, final int length) throws IOException {
        String toWrite;
        if (s.length() > length) {
            LOG.warn(String.format("Truncated string \"%s\", max length is %d", s, length));
            toWrite = s.substring(0, length);
        } else {
            toWrite = padStringToLength(s, length);
        }
        writeBytes(toWrite, length);
    }

    /**
     * Write out a number of fixed length, padding if required.
     *
     * @param number the number to write out.
     * @param length the length (number of characters) that the number should be.
     * @throws IOException on writing problems.
     *
     */
    protected final void writeFixedLengthNumber(final long number, final int length) throws IOException {
        if (String.format("%d", number).length() > length) {
            String problem = String.format("Fixed length number %d cannot fit into length %d", number, length);
            LOG.error(problem);
            throw new NumberFormatException(problem);
        }
        writeBytes(padNumberToLength(number, length), length);
    }

    /**
     * Write out the segment-level security metadata.
     *
     * @param securityMetadata security data for the segment.
     * @throws IOException on writing problems.
     */
    protected final void writeSecurityMetadata(final SecurityMetadata securityMetadata) throws IOException {
        // TODO: consider making this a member variable. Requires restructing SecurityMetadataWriter and probably writer code
        SecurityMetadataWriter securityMetadataWriter = new SecurityMetadataWriter(mOutput, mTreParser);
        securityMetadataWriter.writeMetadata(securityMetadata);
    }

    /**
     * Write out a NITF date-time.
     *
     * @param dateTime the date-time to write out.
     * @throws IOException on writing problems.
     */
    protected final void writeDateTime(final NitfDateTime dateTime) throws IOException {
        if (dateTime.getSourceString().length() == STANDARD_DATE_TIME_LENGTH) {
            writeBytes(dateTime.getSourceString(), STANDARD_DATE_TIME_LENGTH);
        } else if (dateTime.getSourceString().length() > STANDARD_DATE_TIME_LENGTH) {
            LOG.warn(String.format("Invalid date format \"%s\"", dateTime.getSourceString()));
            writeBytes(hyphenPadStringToLength("", STANDARD_DATE_TIME_LENGTH), STANDARD_DATE_TIME_LENGTH);
        } else {
            writeBytes(hyphenPadStringToLength(dateTime.getSourceString(), STANDARD_DATE_TIME_LENGTH), STANDARD_DATE_TIME_LENGTH);
        }
    }

    /**
     * Write out the data for the segment.
     *
     * @param data the data to write.
     * @throws IOException on write failure.
     */
    public final void writeSegmentData(final ImageInputStream data) throws IOException {
        if (data == null) {
            return;
        }
        data.seek(0);
        byte[] buffer = new byte[GraphicSegmentWriter.BUFFER_SIZE];
        int bytesRead;
        while ((bytesRead = data.read(buffer)) != -1) {
            mOutput.write(buffer, 0, bytesRead);
        }
    }
}
