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
package org.codice.imaging.nitf.core.dataextension;

import org.codice.imaging.nitf.core.common.AbstractSegmentParser;
import org.codice.imaging.nitf.core.common.NitfFormatException;
import org.codice.imaging.nitf.core.common.NitfReader;
import static org.codice.imaging.nitf.core.dataextension.DataExtensionConstants.DE;
import static org.codice.imaging.nitf.core.dataextension.DataExtensionConstants.DESID_LENGTH;
import static org.codice.imaging.nitf.core.dataextension.DataExtensionConstants.DESITEM_LENGTH;
import static org.codice.imaging.nitf.core.dataextension.DataExtensionConstants.DESOFLW_LENGTH;
import static org.codice.imaging.nitf.core.dataextension.DataExtensionConstants.DESSHL_LENGTH;
import static org.codice.imaging.nitf.core.dataextension.DataExtensionConstants.DESVER_LENGTH;
import org.codice.imaging.nitf.core.security.SecurityMetadataParser;

/**
    Parser for a data extension segment (DES) in a NITF file.
*/
public class DataExtensionSegmentParser extends AbstractSegmentParser {
    private int userDefinedSubheaderLength = 0;

    private DataExtensionSegmentImpl segment = null;

    /**
     * Default Constructor.
     */
    public DataExtensionSegmentParser() {
    }

    /**
     * Parse DataExtensionSegment from the specified reader.
     *
     * @param nitfReader the NITF input reader.
     * @param dataLength the length of the data part of this segment
     * @return a fully parsed DataExtensionSegment
     * @throws NitfFormatException when the parser encounters unexpected input from the reader.
     */
    public final DataExtensionSegment parse(final NitfReader nitfReader, final long dataLength) throws NitfFormatException {
        reader = nitfReader;
        segment = new DataExtensionSegmentImpl(nitfReader.getFileType());
        segment.setDataLength(dataLength);

        readDE();
        readDESID();
        readDESVER();
        segment.setSecurityMetadata(new SecurityMetadataParser().parseSecurityMetadata(reader));

        if (segment.isTreOverflow()) {
            readDESOFLW();
            readDESITEM();
        }
        readDSSHL();
        readDSSHF();
        return segment;
    }

    private void readDE() throws NitfFormatException {
        reader.verifyHeaderMagic(DE);
    }

    private void readDESID() throws NitfFormatException {
        segment.setIdentifier(reader.readBytes(DESID_LENGTH));
    }

    private void readDESVER() throws NitfFormatException {
        segment.setDESVersion(reader.readBytesAsInteger(DESVER_LENGTH));
    }

    private void readDESOFLW() throws NitfFormatException {
        segment.setOverflowedHeaderType(reader.readTrimmedBytes(DESOFLW_LENGTH));
    }

    private void readDESITEM() throws NitfFormatException {
        segment.setItemOverflowed(reader.readBytesAsInteger(DESITEM_LENGTH));
    }

    private void readDSSHL() throws NitfFormatException {
        userDefinedSubheaderLength = reader.readBytesAsInteger(DESSHL_LENGTH);
    }

    private void readDSSHF() throws NitfFormatException {
        segment.setUserDefinedSubheaderField(reader.readBytes(userDefinedSubheaderLength));
    }
}
