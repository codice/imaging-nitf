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

public class NitfFileSecurityMetadata extends NitfSecurityMetadata
{
    private String nitfFileCopyNumber = null;
    private String nitfFileNumberOfCopies = null;

    private static final int FSCOP_LENGTH = 5;
    private static final int FSCPYS_LENGTH = 5;

    public NitfFileSecurityMetadata(NitfReader nitfReader) throws ParseException {
        super(nitfReader);
        readFileSecurityMetadataExtras();
    }

    public String getFileCopyNumber() {
        return nitfFileCopyNumber;
    }

    public String getFileNumberOfCopies() {
        return nitfFileNumberOfCopies;
    }

    private void readFileSecurityMetadataExtras() throws ParseException {
        readFSCOP();
        readFSCPYS();
    }

    private void readFSCOP() throws ParseException {
        nitfFileCopyNumber = reader.readTrimmedBytes(FSCOP_LENGTH);
    }

    private void readFSCPYS() throws ParseException {
        nitfFileNumberOfCopies = reader.readTrimmedBytes(FSCPYS_LENGTH);
    }
};

