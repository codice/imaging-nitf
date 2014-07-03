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

import java.io.BufferedInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class NitfReader
{
    BufferedInputStream input = null;
    int numBytesRead = 0;
    private TreParser treParser = null;

    private static final Charset UTF8_CHARSET = Charset.forName("UTF-8");
    private static final int STANDARD_DATE_TIME_LENGTH = 14;
    private static final int ENCRYP_LENGTH = 1;

    public NitfReader(BufferedInputStream nitfInputStream, int offset) throws ParseException {
        input = nitfInputStream;
        numBytesRead = offset;
        treParser = new TreParser();
    }

    public Boolean canSeek() {
        return false;
    }

    public void verifyHeaderMagic(String magicHeader) throws ParseException {
        String actualHeader = readBytes(magicHeader.length());
        if (!actualHeader.equals(magicHeader)) {
            throw new ParseException(String.format("Missing %s magic header, got %s", magicHeader, actualHeader), numBytesRead);
        }
    }

    public Date readNitfDateTime() throws ParseException {
        String dateString = readTrimmedBytes(STANDARD_DATE_TIME_LENGTH);
        // TODO: check if NITF 2.0 uses the same format.
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        if (dateString.length() == "yyyyMMdd".length()) {
            // Fallback for files that aren't spec compliant
            dateFormat = new SimpleDateFormat("yyyyMMdd");
        }
        Date dateTime = dateFormat.parse(dateString);
        if (dateTime == null) {
            throw new ParseException(String.format("Bad DATETIME format: %s", dateString), numBytesRead);
        }
        return dateTime;
    }

    public Integer readBytesAsInteger(int count) throws ParseException {
        String intString = readBytes(count);
        Integer intValue = 0;
        try {
            intValue = Integer.parseInt(intString);
        } catch (NumberFormatException ex) {
            throw new ParseException(String.format("Bad Integer format: %s", intString), numBytesRead);
        }
        return intValue;
    }

    public Long readBytesAsLong(int count) throws ParseException {
        String longString = readBytes(count);
        Long longValue = 0L;
        try {
            longValue = Long.parseLong(longString);
        } catch (NumberFormatException ex) {
            throw new ParseException(String.format("Bad Long format: %s", longString), numBytesRead);
        }
        return longValue;
    }

    public Double readBytesAsDouble(int count) throws ParseException {
        String doubleString = readBytes(count);
        Double doubleValue = 0.0;
        try {
            doubleValue = Double.parseDouble(doubleString.trim());
        } catch (NumberFormatException ex) {
            throw new ParseException(String.format("Bad Double format: %s", doubleString), numBytesRead);
        }
        return doubleValue;
    }

    public String readTrimmedBytes(int count) throws ParseException {
        return readBytes(count).trim();
    }

    public void readENCRYP() throws ParseException {
        if (!readBytes(ENCRYP_LENGTH).equals("0")) {
            throw new ParseException("Unexpected ENCRYP value", numBytesRead);
        }
    }

    public String readBytes(int count) throws ParseException {
        return new String(readBytesRaw(count), UTF8_CHARSET);
    }

    public byte[] readBytesRaw(int count) throws ParseException {
        try {
            byte[] bytes = new byte[count];
            int thisRead = input.read(bytes, 0, count);
            if (thisRead == -1) {
                throw new ParseException("End of file reading from NITF stream.", numBytesRead);
            } else if (thisRead < count) {
                throw new ParseException(String.format("Short read while reading from NITF stream (%s/%s).", thisRead, count), numBytesRead + thisRead);
            }
            numBytesRead += thisRead;
            return bytes;
        } catch (IOException ex) {
            throw new ParseException("Error reading from NITF stream: " + ex.getMessage(), numBytesRead);
        }
    }

    public void skip(long count) throws ParseException {
        try {
            long thisRead = 0;
            do {
                thisRead = input.skip(count);
                numBytesRead += thisRead;
            } while ((count -= thisRead) > 0);
        } catch (IOException ex) {
            throw new ParseException("Error reading from NITF stream: " + ex.getMessage(), numBytesRead);
        }
    }

    public Map<String, List<Tre>> parseTREs (int treLength) throws ParseException {
        return treParser.parse(this, treLength);
    }
}
