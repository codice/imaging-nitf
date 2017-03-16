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

import java.io.IOException;

import org.codice.imaging.nitf.core.security.SecurityMetadata;

/**
 * Common data elements for NITF segments.
 *
 * This includes image segments, symbol segments, graphic segments, label segments, text segments and data extension
 * segments.
 *
 */
public interface CommonSegment extends TaggedRecordExtensionHandler {

    /**
     * Return the identifier (IID1/SID/LID/TEXTID/DESID) for the segment.
     * <p>
     * This field shall contain a valid alphanumeric identification code associated with the segment. The valid codes
     * are determined by the application.
     *
     * This is a fixed length field in the segment, and any space padding will be included in the result. You may wish
     * to trim() the result before displaying or storing this field content.
     *
     * @return the identifier, including any space padding.
     */
    String getIdentifier();

    /**
     * Set the identifier (IID1/SID/LID/TEXTID/DESID) for the segment.
     * <p>
     * This field shall contain a valid alphanumeric identification code associated with the segment. The valid codes
     * are determined by the application.
     *
     * @param identifier the identifier for the segment
     */
    void setIdentifier(final String identifier);

    /**
     * Return the segment-level security metadata for the segment.
     *
     * @return security metadata
     */
    SecurityMetadata getSecurityMetadata();

    /**
     * Set the segment-level security metadata elements for the segment.
     *
     * See SecurityMetadata for the various elements, which differ slightly between NITF 2.0 and NITF 2.1/NSIF 1.0.
     *
     * @param metaData the security metadata values to set.
     */
    void setSecurityMetadata(final SecurityMetadata metaData);

    /**
     * Get the length of this segment, excluding the data.
     *
     * @return actual header length in bytes.
     * @throws NitfFormatException if there was a problem building the TRE parser.
     * @throws IOException if there was a problem reading configuration data.
     */
    long getHeaderLength() throws NitfFormatException, IOException;

    /**
     * Get the FileType (NITF / NSIF version) for the segment.
     *
     * @return the file type for the segment
     */
    FileType getFileType();

    /**
     * Set the FileType (NITF / NSIF version) for the segment.
     *
     * @param fileType the fileType for the segment
     */
    void setFileType(final FileType fileType);
}
