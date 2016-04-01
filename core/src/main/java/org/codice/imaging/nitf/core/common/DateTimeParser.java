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
package org.codice.imaging.nitf.core.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import static org.codice.imaging.nitf.core.common.CommonConstants.NITF20_DATE_FORMAT;
import static org.codice.imaging.nitf.core.common.CommonConstants.NITF21_DATE_FORMAT;
import static org.codice.imaging.nitf.core.common.CommonConstants.STANDARD_DATE_TIME_LENGTH;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Parser for a NITF date-time string.
 *
 * This class handles both NITF 2.0 and 2.1 formats, including cases that are possibly not entirely spec compliant.
 */
public class DateTimeParser {

    private static final Logger LOG = LoggerFactory.getLogger(DateTimeParser.class);

    private static String removeHyphens(final String s) {
        int i = s.length() - 1;
        while ((i >= 0) && (s.charAt(i) == '-')) {
            i--;
        }
        return s.substring(0, i + 1);
    }

    private void parseNitf21Date(final String sourceString, final DateTime dateTime) throws NitfFormatException {
        String strippedSourceString = removeHyphens(sourceString.trim());
        SimpleDateFormat dateFormat = null;
        if (strippedSourceString.length() == STANDARD_DATE_TIME_LENGTH) {
            dateFormat = new SimpleDateFormat(NITF21_DATE_FORMAT);
        } else if ((strippedSourceString.length() < STANDARD_DATE_TIME_LENGTH) && (strippedSourceString.length() % 2 == 0)) {
            dateFormat = new SimpleDateFormat(NITF21_DATE_FORMAT.substring(0, strippedSourceString.length()));
        }
        parseDateString(sourceString, dateFormat, dateTime);
    }

    private void parseNitf20Date(final String sourceString, final DateTime dateTime) throws NitfFormatException {
        String strippedSourceString = sourceString.trim();
        SimpleDateFormat dateFormat = null;
        if (strippedSourceString.length() == STANDARD_DATE_TIME_LENGTH) {
            dateFormat = new SimpleDateFormat(NITF20_DATE_FORMAT);
        } else if (strippedSourceString.length() == 0) {
            return;
        }
        parseDateString(sourceString, dateFormat, dateTime);
    }

    /**
     * Read in a DateTime from the current reader position.
     *
     * @param reader the reader to parse from
     * @return a DateTime from head of the reader stream.
     * @throws NitfFormatException when the next token is not the expected format for a DateTime.
     */
    public final DateTime readNitfDateTime(final NitfReader reader) throws NitfFormatException {
        DateTime dateTime = new DateTime();
        String sourceString = reader.readBytes(CommonConstants.STANDARD_DATE_TIME_LENGTH);
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
                throw new NitfFormatException("Need to set NITF file type prior to reading dates", reader.getCurrentOffset());
        }
        return dateTime;
    }

    private void parseDateString(final String sourceString, final SimpleDateFormat dateFormat, final DateTime dateTime)
            throws NitfFormatException {
        if (dateFormat != null) {
            Date date;
            dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            try {
                date = dateFormat.parse(sourceString);
                Calendar calendar = Calendar.getInstance();
                calendar.clear();
                calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
                calendar.setTime(date);
                dateTime.set(calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH) + 1,
                        calendar.get(Calendar.DAY_OF_MONTH),
                        calendar.get(Calendar.HOUR_OF_DAY),
                        calendar.get(Calendar.MINUTE),
                        calendar.get(Calendar.SECOND));
            } catch (ParseException ex) {
                LOG.warn("Invalid NITF date time value: '" + sourceString + "'");
            }
        } else {
            LOG.warn("Unhandled date format: {}", sourceString);
        }
    }

}
