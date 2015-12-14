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
package org.codice.imaging.nitf.dataextension;

import java.text.ParseException;

import org.codice.imaging.nitf.common.dataextension.NitfDataExtensionSegmentHeader;
import org.codice.imaging.nitf.common.reader.NitfReader;
import org.codice.imaging.nitf.parser.AbstractNitfSegmentParser;
import org.codice.imaging.nitf.security.NitfSecurityMetadataImpl;

/**
    Parser for a data extension segment (DES) subheader in a NITF file.
*/
public class NitfDataExtensionSegmentHeaderParser extends AbstractNitfSegmentParser {
    // Data Extenstion Segment (DES)
    /**
     * Marker string for Data Extension Segment (DES) segment header.
     */
    private static final String DE = "DE";

    /**
     * Length of unique DES Type Identifier.
     * <p>
     * MIL-STD-2500C Table A-8(A) / Table A-8(B)
     */
    private static final int DESID_LENGTH = 25;

    /**
     * Length of DES Data Definition version.
     * <p>
     * MIL-STD-2500C Table A-8(A) / Table A-8(B)
     */
    private static final int DESVER_LENGTH = 2;

    /**
     * Length of DES overflowed header type.
     * <p>
     * MIL-STD-2500C Table A-8(A)
     */
    private static final int DESOFLW_LENGTH = 6;

    /**
     * Length of DES Data Overflowed Item field.
     * <p>
     * MIL-STD-2500C Table A-8(A)
     */
    private static final int DESITEM_LENGTH = 3;

    /**
     * Length of the "length of DES-Defined subheader fields" field.
     * <p>
     * MIL-STD-2500C Table A-8(A)
     */
    private static final int DESSHL_LENGTH = 4;

    private int userDefinedSubheaderLength = 0;

    private NitfDataExtensionSegmentHeaderImpl segment = null;

    /**
     *
     * @param nitfReader - the NitfReader that streams the data extension segment header input.
     * @return the fully parsed NitfDataExtensionSegmentHeader
     * @throws ParseException - when an unexpected value is encountered in the input stream.
     */
    public final NitfDataExtensionSegmentHeader parse(final NitfReader nitfReader) throws ParseException {
        reader = nitfReader;
        segment = new NitfDataExtensionSegmentHeaderImpl();

        readDE();
        readDESID();
        readDESVER();
        segment.setSecurityMetadata(new NitfSecurityMetadataImpl(reader));

        if (segment.isTreOverflow(reader.getFileType())) {
            readDESOFLW();
            readDESITEM();
        }
        readDSSHL();
        readDSSHF();
        return segment;
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
}
