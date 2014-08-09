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

class NitfDataExtensionSegmentParser extends AbstractNitfSegmentParser {
    private int lengthOfDataExtension = 0;
    private int userDefinedSubheaderLength = 0;
    private String userDefinedSubheaderField = null;

    private static final String DE = "DE";
    private static final int DESID_LENGTH = 25;
    private static final int DESVER_LENGTH = 2;
    private static final int DESOFLW_LENGTH = 6;
    private static final int DESITEM_LENGTH = 3;
    private static final int DESSHL_LENGTH = 4;

    private static final String TRE_OVERFLOW = "TRE_OVERFLOW";
    private static final String REGISTERED_EXTENSIONS = "Registered Extensions";
    private static final String CONTROLLED_EXTENSIONS = "Controlled Extensions";

    private NitfDataExtensionSegment segment = null;

    NitfDataExtensionSegmentParser(final NitfReader nitfReader,
                                          final int desLength,
                                          final NitfDataExtensionSegment desSegment) throws ParseException {
        reader = nitfReader;
        lengthOfDataExtension = desLength;
        segment = desSegment;

        readDE();
        readDESID();
        readDESVER();
        segment.setSecurityMetadata(new NitfSecurityMetadata(reader));

        if (isTreOverflow()) {
            readDESOFLW();
            readDESITEM();
        }
        readDSSHL();
        readDSSHF();
        readDESDATA();
    }

    private void readDE() throws ParseException {
        reader.verifyHeaderMagic(DE);
    }

    private void readDESID() throws ParseException {
        segment.setIdentifier(reader.readBytes(DESID_LENGTH));
    }

    private void readDESVER() throws ParseException {
        segment.setDESVersion(reader.readBytesAsInteger(DESVER_LENGTH));
    }

    private void readDESOFLW() throws ParseException {
        segment.setOverflowedHeaderType(reader.readTrimmedBytes(DESOFLW_LENGTH));
    }

    private void readDESITEM() throws ParseException {
        segment.setItemOverflowed(reader.readBytesAsInteger(DESITEM_LENGTH));
    }

    private void readDSSHL() throws ParseException {
        userDefinedSubheaderLength = reader.readBytesAsInteger(DESSHL_LENGTH);
    }

    private void readDSSHF() throws ParseException {
        segment.setUserDefinedSubheaderField(reader.readBytes(userDefinedSubheaderLength));
    }

    private boolean isTreOverflow() {
        if (reader.getFileType() == FileType.NITF_TWO_ZERO) {
            return segment.getIdentifier().trim().equals(REGISTERED_EXTENSIONS)
                || segment.getIdentifier().trim().equals(CONTROLLED_EXTENSIONS);
        } else {
            return segment.getIdentifier().trim().equals(TRE_OVERFLOW);
        }
    }

    private void readDESDATA() throws ParseException {
        if (isTreOverflow()) {
            TreParser treParser = new TreParser();
            TreCollection overflowTres = treParser.parse(reader, lengthOfDataExtension);
            segment.mergeTREs(overflowTres);
        } else {
            segment.setData(reader.readBytesRaw(lengthOfDataExtension));
        }
    }
}
