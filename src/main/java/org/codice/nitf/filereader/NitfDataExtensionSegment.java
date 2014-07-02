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


// TODO: This is partly implemented untested junk
public class NitfDataExtensionSegment
{
    NitfReader reader = null;
    long lengthOfDataExtension = 0;

    private String desIdentifier = null;
    private int desVersion = -1;
    private NitfSecurityMetadata securityMetadata = null;

    private static final String DE = "DE";
    private static final int DESID_LENGTH = 25;
    private static final int DESVER_LENGTH = 2;

    public NitfDataExtensionSegment(NitfReader nitfReader, long desLength) throws ParseException {
        reader = nitfReader;
        lengthOfDataExtension = desLength;

        readDE();
        readDESID();
        readDESVER();
        securityMetadata = new NitfSecurityMetadata(reader);

        //readTXSHDL();
        // if (textExtendedSubheaderLength > 0) {
            // TODO: find a case that exercises this and implement it
        //    throw new UnsupportedOperationException("IMPLEMENT TXSOFL / TXSHD PARSING");
        //}
        // readTextData();
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

    private void readDE() throws ParseException {
        System.out.println("Reading DE at " + reader.numBytesRead);
        reader.verifyHeaderMagic(DE);
    }

    private void readDESID() throws ParseException {
        desIdentifier = reader.readBytes(DESID_LENGTH);
    }

    private void readDESVER() throws ParseException {
        desVersion = reader.readBytesAsInteger(DESVER_LENGTH);
    }

//     private void readTextData() throws ParseException {
//         // TODO: we could use this if needed later
//         reader.skip(lengthOfText);
//     }
}
