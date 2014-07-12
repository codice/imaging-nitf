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

public class NitfDataExtensionSegment extends AbstractNitfSegment
{
    NitfReader reader = null;
    int lengthOfDataExtension = 0;

    private String desIdentifier = null;
    private int desVersion = -1;
    private NitfSecurityMetadata securityMetadata = null;
    private String desOverflowedHeaderType = null;
    private int desItemOverflowed = 0;
    private int userDefinedSubheaderLength = 0;
    private String userDefinedSubheaderField = null;
    private String desData = null;

    private static final String DE = "DE";
    private static final String TRE_OVERFLOW = "TRE_OVERFLOW";
    private static final int DESID_LENGTH = 25;
    private static final int DESVER_LENGTH = 2;
    private static final int DESOFLW_LENGTH = 6;
    private static final int DESITEM_LENGTH = 3;
    private static final int DESSHL_LENGTH = 4;

    public NitfDataExtensionSegment(NitfReader nitfReader, int desLength) throws ParseException {
        reader = nitfReader;
        lengthOfDataExtension = desLength;

        readDE();
        readDESID();
        readDESVER();
        securityMetadata = new NitfSecurityMetadata(reader);

        if (isTreOverflow()) {
            readDESOFLW();
            readDESITEM();
        }
        readDSSHL();
        readDSSHF();
        readDESDATA();
    }

    public String getDESIdentifier() {
        return desIdentifier;
    }

    public int getDESVersion() {
        return desVersion;
    }

    public NitfSecurityMetadata getSecurityMetadata() {
        return securityMetadata;
    }

    private boolean isTreOverflow() {
        return (desIdentifier.trim().equals(TRE_OVERFLOW));
    }

    private void readDE() throws ParseException {
        reader.verifyHeaderMagic(DE);
    }

    private void readDESID() throws ParseException {
        desIdentifier = reader.readBytes(DESID_LENGTH);
    }

    private void readDESVER() throws ParseException {
        desVersion = reader.readBytesAsInteger(DESVER_LENGTH);
    }

    private void readDESOFLW() throws ParseException {
        desOverflowedHeaderType = reader.readTrimmedBytes(DESOFLW_LENGTH);
        System.out.println("DES Overflowed Header Type: " + desOverflowedHeaderType);
    }

    private void readDESITEM() throws ParseException {
        desItemOverflowed = reader.readBytesAsInteger(DESITEM_LENGTH);
        System.out.println("Item: " + desItemOverflowed);
    }

    private void readDSSHL() throws ParseException {
        userDefinedSubheaderLength = reader.readBytesAsInteger(DESSHL_LENGTH);
        System.out.println("User defined subheader length: " + userDefinedSubheaderLength);
    }

    private void readDSSHF() throws ParseException {
        userDefinedSubheaderField = reader.readBytes(userDefinedSubheaderLength);
    }

    private void readDESDATA() throws ParseException {
        if (isTreOverflow()) {
            TreParser treParser = new TreParser();
            TreCollection overflowTres = treParser.parse(reader, lengthOfDataExtension);
            mergeTREs(overflowTres);
        } else {
            desData = reader.readBytes(lengthOfDataExtension);
        }
    }
}
