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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
    Common segment parsing functionality.
*/
abstract class AbstractNitfSegmentParser {
    protected NitfReader reader = null;

    private static final int ENCRYP_LENGTH = 1;
    private static final int RGB_COLOUR_LENGTH = 3;
    private static final int STANDARD_DATE_TIME_LENGTH = 14;
    private static final String DATE_ONLY_DAY_FORMAT = "yyyyMMdd";
    private static final String DATE_FULL_FORMAT = "yyyyMMddHHmmss";
    private static final String NITF20_DATE_FORMAT = "ddHHmmss'Z'MMMyy";

    protected final void readENCRYP() throws ParseException {
        if (!"0".equals(reader.readBytes(ENCRYP_LENGTH))) {
            throw new ParseException("Unexpected ENCRYP value", (int) reader.getCurrentOffset());
        }
    }

    protected final RGBColour readRGBColour() throws ParseException {
        byte[] rgb = reader.readBytesRaw(RGB_COLOUR_LENGTH);
        return new RGBColour(rgb);
    }

    protected final Date readNitfDateTime() throws ParseException {
        String dateString = reader.readTrimmedBytes(STANDARD_DATE_TIME_LENGTH);
        SimpleDateFormat dateFormat = null;
        Date dateTime = null;
        switch (reader.getFileType()) {
            case NITF_TWO_ZERO:
                dateFormat = new SimpleDateFormat(NITF20_DATE_FORMAT);
                break;
            case NITF_TWO_ONE:
            case NSIF_ONE_ZERO:
                dateFormat = new SimpleDateFormat(DATE_FULL_FORMAT);
                break;
            case UNKNOWN:
            default:
                throw new ParseException("Need to set NITF file type prior to reading dates", (int) reader.getCurrentOffset());
        }
        if (dateString.length() == DATE_ONLY_DAY_FORMAT.length()) {
            // Fallback for files that aren't spec compliant
            dateFormat = new SimpleDateFormat(DATE_ONLY_DAY_FORMAT);
        } else if (dateString.length() == 0) {
            // This can't work
            dateFormat = null;
        } else if (dateString.length() != DATE_FULL_FORMAT.length()) {
            System.out.println("Unhandled date format:" + dateString);
        }
        if (dateFormat == null) {
            dateTime = null;
        } else {
            dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            dateTime = dateFormat.parse(dateString);
            if (dateTime == null) {
                throw new ParseException(String.format("Bad DATETIME format: %s", dateString), (int) reader.getCurrentOffset());
            }
        }
        return dateTime;
    }

}
