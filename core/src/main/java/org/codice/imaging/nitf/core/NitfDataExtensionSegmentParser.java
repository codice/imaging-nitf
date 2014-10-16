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
    Parser for a data extension segment (DES) subheader in a NITF file.
*/
class NitfDataExtensionSegmentParser extends AbstractNitfSegmentParser {
    private int lengthOfDataExtension = 0;
    private int userDefinedSubheaderLength = 0;

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
        reader.verifyHeaderMagic(NitfConstants.DE);
    }

    private void readDESID() throws ParseException {
        segment.setIdentifier(reader.readBytes(NitfConstants.DESID_LENGTH));
    }

    private void readDESVER() throws ParseException {
        segment.setDESVersion(reader.readBytesAsInteger(NitfConstants.DESVER_LENGTH));
    }

    private void readDESOFLW() throws ParseException {
        segment.setOverflowedHeaderType(reader.readTrimmedBytes(NitfConstants.DESOFLW_LENGTH));
    }

    private void readDESITEM() throws ParseException {
        segment.setItemOverflowed(reader.readBytesAsInteger(NitfConstants.DESITEM_LENGTH));
    }

    private void readDSSHL() throws ParseException {
        userDefinedSubheaderLength = reader.readBytesAsInteger(NitfConstants.DESSHL_LENGTH);
    }

    private void readDSSHF() throws ParseException {
        segment.setUserDefinedSubheaderField(reader.readBytes(userDefinedSubheaderLength));
    }

    private boolean isTreOverflow() {
        if (reader.getFileType() == FileType.NITF_TWO_ZERO) {
            return segment.getIdentifier().trim().equals(NitfConstants.REGISTERED_EXTENSIONS)
                || segment.getIdentifier().trim().equals(NitfConstants.CONTROLLED_EXTENSIONS);
        } else {
            return segment.getIdentifier().trim().equals(NitfConstants.TRE_OVERFLOW);
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
