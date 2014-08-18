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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.rules.ExpectedException;
import org.junit.Rule;
import org.junit.Test;

import org.mockito.Mockito;

public class AbstractParserTest {
    private static final int STANDARD_DATE_TIME_LENGTH = 14;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void testParseOfENCRYP() throws ParseException {
        AbstractNitfSegmentParser parser = Mockito.mock(AbstractNitfSegmentParser.class, Mockito.CALLS_REAL_METHODS);

        NitfReader mockReader = Mockito.mock(NitfReader.class);
        parser.reader = mockReader;
        Mockito.when(mockReader.readBytes(1)).thenReturn("0");

        parser.readENCRYP();
        Mockito.when(mockReader.readBytes(1)).thenReturn("1");
        exception.expect(ParseException.class);
        exception.expectMessage("Unexpected ENCRYP value");
        parser.readENCRYP();
    }

    @Test
    public void testDateParsingWithoutReaderVersion() throws ParseException {
        AbstractNitfSegmentParser parser = Mockito.mock(AbstractNitfSegmentParser.class, Mockito.CALLS_REAL_METHODS);

        NitfReader mockReader = Mockito.mock(NitfReader.class);
        parser.reader = mockReader;
        Mockito.when(mockReader.getFileType()).thenReturn(FileType.UNKNOWN);

        exception.expect(ParseException.class);
        exception.expectMessage("Need to set NITF file type prior to reading dates");
        Date date = parser.readNitfDateTime();
    }

    // Test when we only have yyyyMMdd. Some commercial producers do this, although it should be yyyyMMdd------.
    @Test
    public void testDateOnlyParsing() throws ParseException {
        AbstractNitfSegmentParser parser = Mockito.mock(AbstractNitfSegmentParser.class, Mockito.CALLS_REAL_METHODS);

        NitfReader mockReader = Mockito.mock(NitfReader.class);
        Mockito.when(mockReader.getFileType()).thenReturn(FileType.NITF_TWO_ONE);
        parser.reader = mockReader;
        Mockito.when(mockReader.readTrimmedBytes(STANDARD_DATE_TIME_LENGTH)).thenReturn("20140704");

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"),  Locale.US);
        calendar.clear();
        calendar.set(2014, Calendar.JULY, 4);
        Date expectedDate = calendar.getTime();
        assertEquals(expectedDate, parser.readNitfDateTime());
    }
}
