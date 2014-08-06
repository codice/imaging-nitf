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

public class NitfFileSecurityMetadata extends NitfSecurityMetadata {
    private String nitfFileCopyNumber = null;
    private String nitfFileNumberOfCopies = null;

    public NitfFileSecurityMetadata(final NitfReader nitfReader) throws ParseException {
        NitfFileSecurityMetadataParser parser = new NitfFileSecurityMetadataParser();
        parser.parse(nitfReader, this);
    }

    public final void setFileCopyNumber(final String copyNumber) {
        nitfFileCopyNumber = copyNumber;
    }

    public final String getFileCopyNumber() {
        return nitfFileCopyNumber;
    }

    public final void setFileNumberOfCopies(final String numberOfCopies) {
        nitfFileNumberOfCopies = numberOfCopies;
    }

    public final String getFileNumberOfCopies() {
        return nitfFileNumberOfCopies;
    }

};

