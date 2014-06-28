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

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.text.ParseException;

public class NitfHeaderReader
{
    private BufferedReader reader;
    private long numBytesRead = 0;
    private Boolean hasNitfHeader = false;
    private NitfVersion nitfVersion = NitfVersion.UNKNOWN;

    private static final int FHDR_LENGTH = 4;
    private static final String NITF_FHDR = "NITF";
    private static final int FVER_LENGTH = 5;
    private static final String NITF_2_0 = "02.00";
    private static final String NITF_2_1 = "02.10";

    public NitfHeaderReader(InputStream nitfInputStream) throws ParseException {
        reader = new BufferedReader(new InputStreamReader(nitfInputStream));
        readBaseHeader();
    }

    public Boolean isNitf() {
        return hasNitfHeader;
    }

    public NitfVersion getVersion() {
        return nitfVersion;
    }

    private void readBaseHeader() throws ParseException {
        readFHDR();
        readFVER();
    }

    private void readFHDR() throws ParseException {
        String fhdr = readBytes(FHDR_LENGTH);
        hasNitfHeader = fhdr.equals(NITF_FHDR);
    }

    private void readFVER() throws ParseException {
        String fver = readBytes(FVER_LENGTH);
        if (fver.equals(NITF_2_0)) {
            nitfVersion = NitfVersion.TWO_ZERO;
        } else if (String.valueOf(fver).equals(NITF_2_1)) {
            nitfVersion = NitfVersion.TWO_ONE;
        }
    }

    private String readBytes(int count) throws ParseException {
        int thisRead = 0;
        try {
            char[] bytes = new char[count];
            thisRead = reader.read(bytes, 0, count);
            if (thisRead == -1) {
                new ParseException("End of file reading from NITF stream.", (int)numBytesRead);
            } else if (thisRead < count) {
                new ParseException("Short read while reading from NITF stream.", (int)(numBytesRead + thisRead));
            }
            numBytesRead += thisRead;
            return String.valueOf(bytes);
        } catch (IOException ex) {
            new ParseException("Error reading from NITF stream: " + ex.getMessage(), (int)numBytesRead);
        }
        return null;
    }

}
