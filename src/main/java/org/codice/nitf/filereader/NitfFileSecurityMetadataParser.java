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

import java.text.ParseException;

public class NitfFileSecurityMetadataParser extends NitfSecurityMetadataParser {

    private static final int FSCOP_LENGTH = 5;
    private static final int FSCPYS_LENGTH = 5;

    public NitfFileSecurityMetadataParser() {
    }

    public final void parse(final NitfReader nitfReader, final NitfFileSecurityMetadata metadata) throws ParseException {
        doParse(nitfReader, metadata);
        readFileSecurityMetadataExtras(metadata);
    }

    private void readFileSecurityMetadataExtras(final NitfFileSecurityMetadata metadata) throws ParseException {
        readFSCOP(metadata);
        readFSCPYS(metadata);
    }

    private void readFSCOP(final NitfFileSecurityMetadata metadata) throws ParseException {
        metadata.setFileCopyNumber(reader.readTrimmedBytes(FSCOP_LENGTH));
    }

    private void readFSCPYS(final NitfFileSecurityMetadata metadata) throws ParseException {
        metadata.setFileNumberOfCopies(reader.readTrimmedBytes(FSCPYS_LENGTH));
    }
};

