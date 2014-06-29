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
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NitfReader
{
    BufferedReader reader = null;
    int numBytesRead = 0;

    private static final int STANDARD_DATE_TIME_LENGTH = 14;
    private static final int ENCRYP_LENGTH = 1;

    public NitfReader(BufferedReader nitfBufferedReader, int offset) throws ParseException {
        reader = nitfBufferedReader;
        numBytesRead = offset;
    }

    // TODO make protected
    public int getNumBytesRead() {
        return numBytesRead;
    }

    public void verifyHeaderMagic(String magicHeader) throws ParseException {
        String actualHeader = readBytes(magicHeader.length());
        if (!actualHeader.equals(magicHeader)) {
            new ParseException(String.format("Missing %s magic header", magicHeader), numBytesRead);
        }
    }

    public Date readNitfDateTime() throws ParseException {
        String dateString = readBytes(STANDARD_DATE_TIME_LENGTH);
        // TODO: check if NITF 2.0 uses the same format.
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        Date dateTime = dateFormat.parse(dateString);
        if (dateTime == null) {
            new ParseException(String.format("Bad DATETIME format: %s", dateString), numBytesRead);
        }
        return dateTime;
    }

    public Integer readBytesAsInteger(int count) throws ParseException {
        String intString = readBytes(count);
        Integer intValue = 0;
        try {
            intValue = Integer.parseInt(intString);
        } catch (NumberFormatException ex) {
            new ParseException(String.format("Bad Integer format: %s", intString), numBytesRead);
        }
        return intValue;
    }

    public Long readBytesAsLong(int count) throws ParseException {
        String longString = readBytes(count);
        Long longValue = 0L;
        try {
            longValue = Long.parseLong(longString);
        } catch (NumberFormatException ex) {
            new ParseException(String.format("Bad Long format: %s", longString), numBytesRead);
        }
        return longValue;
    }

    public String readTrimmedBytes(int count) throws ParseException {
        return readBytes(count).trim();
    }

    public void readENCRYP() throws ParseException {
        if (!readBytes(ENCRYP_LENGTH).equals("0")) {
            new ParseException("Unexpected ENCRYP value", numBytesRead);
        }
    }

    public String readBytes(int count) throws ParseException {
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
