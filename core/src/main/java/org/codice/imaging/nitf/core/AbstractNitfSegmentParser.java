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
package org.codice.imaging.nitf.core;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
    Common segment parsing functionality.
*/
abstract class AbstractNitfSegmentParser {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractNitfSegmentParser.class);

    protected NitfReader reader = null;

    protected final void readENCRYP() throws ParseException {
        if (!"0".equals(reader.readBytes(NitfConstants.ENCRYP_LENGTH))) {
            LOG.warn("Mismatch while reading ENCRYP");
            throw new ParseException("Unexpected ENCRYP value", (int) reader.getCurrentOffset());
        }
    }

    protected final RGBColour readRGBColour() throws ParseException {
        byte[] rgb = reader.readBytesRaw(NitfConstants.RGB_COLOUR_LENGTH);
        return new RGBColour(rgb);
    }

    protected final NitfDateTime readNitfDateTime() throws ParseException {
        NitfDateTime dateTime = new NitfDateTime();

        String sourceString = reader.readBytes(NitfConstants.STANDARD_DATE_TIME_LENGTH);
        dateTime.setSourceString(sourceString);

        switch (reader.getFileType()) {
            case NITF_TWO_ZERO:
                parseNitf20Date(sourceString, dateTime);
                break;
            case NITF_TWO_ONE:
            case NSIF_ONE_ZERO:
                parseNitf21Date(sourceString, dateTime);
                break;
            case UNKNOWN:
            default:
                LOG.warn("Unknown NITF file type while reading date: " + reader.getFileType());
                throw new ParseException("Need to set NITF file type prior to reading dates", (int) reader.getCurrentOffset());
        }

        return dateTime;
    }

    private void parseNitf20Date(final String sourceString, final NitfDateTime dateTime) throws ParseException {
        String strippedSourceString = sourceString.trim();
        SimpleDateFormat dateFormat = null;
        if (strippedSourceString.length() == NitfConstants.STANDARD_DATE_TIME_LENGTH) {
            dateFormat = new SimpleDateFormat(NitfConstants.NITF20_DATE_FORMAT);
        } else if (strippedSourceString.length() == 0) {
            return;
        }
        parseDateString(sourceString, dateFormat, dateTime);
    }

    private void parseNitf21Date(final String sourceString, final NitfDateTime dateTime) throws ParseException {
        String strippedSourceString = removeHyphens(sourceString.trim());

        SimpleDateFormat dateFormat = null;
        if (strippedSourceString.length() == NitfConstants.STANDARD_DATE_TIME_LENGTH) {
            dateFormat = new SimpleDateFormat(NitfConstants.NITF21_DATE_FORMAT);
        } else if ((strippedSourceString.length() < NitfConstants.STANDARD_DATE_TIME_LENGTH) && (strippedSourceString.length() % 2 == 0)) {
            dateFormat = new SimpleDateFormat(NitfConstants.NITF21_DATE_FORMAT.substring(0, strippedSourceString.length()));
        }
        parseDateString(sourceString, dateFormat, dateTime);
    }

    public static String removeHyphens(final String s) {
        int i = s.length() - 1;
        while ((i >= 0) && (s.charAt(i) == '-')) {
            i--;
        }
        return s.substring(0, i + 1);
    }

    private void parseDateString(final String sourceString, final SimpleDateFormat dateFormat, final NitfDateTime dateTime) throws ParseException {
        if (dateFormat != null) {
            dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date = dateFormat.parse(sourceString);
            if (date == null) {
                throw new ParseException(String.format("Bad DATETIME format: %s", sourceString), (int) reader.getCurrentOffset());
            }
            Calendar calendar = Calendar.getInstance();
            calendar.clear();
            calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
            calendar.setTime(date);
            dateTime.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH),
                        calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND));
        } else {
            LOG.warn("Unhandled date format: {}", sourceString);
        }
    }
}
