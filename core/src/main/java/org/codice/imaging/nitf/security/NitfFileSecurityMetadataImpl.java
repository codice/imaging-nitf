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
package org.codice.imaging.nitf.security;

import java.text.ParseException;

import org.codice.imaging.nitf.common.reader.NitfReader;
import org.codice.imaging.nitf.common.security.NitfFileSecurityMetadata;

/**
    File security metadata.
    <p>
    The security metadata at the file level is the same as the subheaders, except for
    two extra fields (copy number, and number of copies).
*/
public class NitfFileSecurityMetadataImpl extends NitfSecurityMetadataImpl
        implements NitfFileSecurityMetadata {
    private String nitfFileCopyNumber = null;
    private String nitfFileNumberOfCopies = null;

    /**
        Constructor.
        <p>
        This builds a new object using the specified NitfReader.

        @param nitfReader the reader to use, positioned at the start of the file security metadata.
        @throws ParseException if any error in the metadata is detected.
    */
    public NitfFileSecurityMetadataImpl(final NitfReader nitfReader) throws ParseException {
        NitfFileSecurityMetadataParser parser = new NitfFileSecurityMetadataParser();
        parser.parse(nitfReader, this);
    }

    /**
        Set the file copy number.
        <p>
        "This field shall contain the copy number of the file. If this field is all BCS zeros (0x30),
        it shall imply that there is no tracking of numbered file copies."

        @param copyNumber the copy number
    */
    public final void setFileCopyNumber(final String copyNumber) {
        nitfFileCopyNumber = copyNumber;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String getFileCopyNumber() {
        return nitfFileCopyNumber;
    }

    /**
        Set the file number of copies.
        <p>
        "This field shall contain the total number of copies of the file. If this field is all BCS
        zeros (0x30), it shall imply that there is no tracking of numbered file copies."

        @param numberOfCopies the number of copies.
    */
    public final void setFileNumberOfCopies(final String numberOfCopies) {
        nitfFileNumberOfCopies = numberOfCopies;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String getFileNumberOfCopies() {
        return nitfFileNumberOfCopies;
    }

};

