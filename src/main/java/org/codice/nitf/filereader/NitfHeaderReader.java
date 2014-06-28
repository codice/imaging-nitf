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
import java.text.SimpleDateFormat;
import java.util.Date;

public class NitfHeaderReader
{
    private Boolean hasNitfHeader = false;
    private NitfVersion nitfVersion = NitfVersion.UNKNOWN;
    private int nitfComplexityLevel = 0;
    private String nitfStandardType = null;
    private String nitfOriginatingStationId = null;
    private Date nitfFileDateTime = null;
    private String nitfFileTitle = null;
    private NitfSecurityClassification nitfSecurityClassification = NitfSecurityClassification.UNKNOWN;

    private BufferedReader reader;
    private int numBytesRead = 0;

    private static final int FHDR_LENGTH = 4;
    private static final String NITF_FHDR = "NITF";
    private static final int FVER_LENGTH = 5;
    private static final int CLEVEL_LENGTH = 2;
    private static final int STYPE_LENGTH = 4;
    private static final int OSTAID_LENGTH = 10;
    private static final int FDT_LENGTH = 14;
    private static final int FTITLE_LENGTH = 80;
    private static final int FSCLAS_LENGTH = 1;

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

    public int getComplexityLevel() {
        return nitfComplexityLevel;
    }

    public String getStandardType() {
        return nitfStandardType;
    }

    public String getOriginatingStationId() {
        return nitfOriginatingStationId;
    }

    public Date getFileDateTime() {
        return nitfFileDateTime;
    }

    public String getFileTitle() {
        return nitfFileTitle;
    }

    public NitfSecurityClassification getSecurityClassification() {
        return nitfSecurityClassification;
    }

    private void readBaseHeader() throws ParseException {
        readFHDR();
        readFVER();
        readCLEVEL();
        readSTYPE();
        readOSTAID();
        readFDT();
        readFTITLE();
        readFSCLAS();
    }

    private void readFHDR() throws ParseException {
        String fhdr = readBytes(FHDR_LENGTH);
        hasNitfHeader = fhdr.equals(NITF_FHDR);
    }

    private void readFVER() throws ParseException {
        String fver = readBytes(FVER_LENGTH);
        for (NitfVersion version : NitfVersion.values()) {
            if (fver.equals(version.getTextEquivalent())) {
                nitfVersion = version;
            }
        }
    }

    private void readCLEVEL() throws ParseException {
        String clevel = readBytes(CLEVEL_LENGTH);
        try {
            nitfComplexityLevel = Integer.parseInt(clevel);
            if ((nitfComplexityLevel < 0) || (nitfComplexityLevel > 99)) {
                new ParseException(String.format("CLEVEL out of range: %i", nitfComplexityLevel), numBytesRead);
            }
        } catch (NumberFormatException ex) {
            new ParseException(String.format("Bad CLEVEL format: %s", clevel), numBytesRead);
        }
    }

    private void readSTYPE() throws ParseException {
        nitfStandardType = readBytes(STYPE_LENGTH);
    }

    private void readOSTAID() throws ParseException {
        String ostaid = readBytes(OSTAID_LENGTH);
        nitfOriginatingStationId = ostaid.trim();
    }

    private void readFDT() throws ParseException {
        String fdt = readBytes(FDT_LENGTH);
        // TODO: check if NITF 2.0 uses the same format.
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        nitfFileDateTime = dateFormat.parse(fdt);
        if (nitfFileDateTime == null) {
            new ParseException(String.format("Bad FDT format: %s", fdt), numBytesRead);
        }
    }

    private void readFTITLE() throws ParseException {
        String ftitle = readBytes(FTITLE_LENGTH);
        nitfFileTitle = ftitle.trim();
    }

    private void readFSCLAS() throws ParseException {
        String fsclas = readBytes(FSCLAS_LENGTH);
        for (NitfSecurityClassification classification : NitfSecurityClassification.values()) {
            if (fsclas.equals(classification.getTextEquivalent())) {
                nitfSecurityClassification = classification;
            }
        }
    }

    private String readBytes(int count) throws ParseException {
        int thisRead = 0;
        try {
            char[] bytes = new char[count];
            thisRead = reader.read(bytes, 0, count);
            if (thisRead == -1) {
                new ParseException("End of file reading from NITF stream.", numBytesRead);
            } else if (thisRead < count) {
                new ParseException("Short read while reading from NITF stream.", numBytesRead + thisRead);
            }
            numBytesRead += thisRead;
            return String.valueOf(bytes);
        } catch (IOException ex) {
            new ParseException("Error reading from NITF stream: " + ex.getMessage(), numBytesRead);
        }
        return null;
    }

}
