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

import java.time.ZoneId;
import java.time.ZonedDateTime;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import uk.org.lidalia.slf4jtest.TestLogger;
import uk.org.lidalia.slf4jtest.TestLoggerFactory;

/**
 * Tests for DateTimeParser class
 */
public class DateTimeParserTest {
    TestLogger logger = TestLoggerFactory.getTestLogger(DateTimeParser.class);

    public DateTimeParserTest() {
    }

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void testDateParsingWithoutReaderVersion() throws NitfFormatException {
        AbstractSegmentParser parser = mock(AbstractSegmentParser.class, CALLS_REAL_METHODS);
        NitfReader mockReader = mock(NitfReader.class);
        parser.reader = mockReader;
        when(mockReader.getFileType()).thenReturn(FileType.UNKNOWN);
        exception.expect(NitfFormatException.class);
        exception.expectMessage("Need to set NITF file type prior to reading dates");
        NitfDateTime date = parser.readNitfDateTime();

    }

    // Test when we have yyyyMMddHH----
    @Test
    public void testPaddedDateHourParsing() throws NitfFormatException {
        NitfReader mockReader = mock(NitfReader.class);
        when(mockReader.getFileType()).thenReturn(FileType.NITF_TWO_ONE);
        when(mockReader.readBytes(CommonConstants.STANDARD_DATE_TIME_LENGTH)).thenReturn("2014070423----");
        ZonedDateTime expectedDate = ZonedDateTime.of(2014, 7, 4, 23, 0, 0, 0, ZoneId.of("UTC"));
        DateTimeParser parser = new DateTimeParser();
        assertEquals(expectedDate, parser.readNitfDateTime(mockReader).getZonedDateTime());
    }

    // Test when we have yyyyMMdd------
    @Test
    public void testPaddedDateParsing() throws NitfFormatException {
        NitfReader mockReader = mock(NitfReader.class);
        when(mockReader.getFileType()).thenReturn(FileType.NITF_TWO_ONE);
        when(mockReader.readBytes(CommonConstants.STANDARD_DATE_TIME_LENGTH)).thenReturn("20140704------");
        ZonedDateTime expectedDate = ZonedDateTime.of(2014, 7, 4, 0, 0, 0, 0, ZoneId.of("UTC"));
        DateTimeParser parser = new DateTimeParser();
        assertEquals(expectedDate, parser.readNitfDateTime(mockReader).getZonedDateTime());
    }

    // Test when we only have yyyyMMdd. Some commercial producers do this, although it should be yyyyMMdd------.
    @Test
    public void testDateOnlyParsing() throws NitfFormatException {
        NitfReader mockReader = mock(NitfReader.class);
        when(mockReader.getFileType()).thenReturn(FileType.NITF_TWO_ONE);
        when(mockReader.readBytes(CommonConstants.STANDARD_DATE_TIME_LENGTH)).thenReturn("20140704");
        ZonedDateTime expectedDate = ZonedDateTime.of(2014, 7, 4, 0, 0, 0, 0, ZoneId.of("UTC"));
        DateTimeParser parser = new DateTimeParser();
        assertEquals(expectedDate, parser.readNitfDateTime(mockReader).getZonedDateTime());
    }

    // Test when we only have all -.
    @Test
    public void testEmptyParsing() throws NitfFormatException {
        NitfReader mockReader = mock(NitfReader.class);
        when(mockReader.getFileType()).thenReturn(FileType.NITF_TWO_ONE);
        when(mockReader.readBytes(CommonConstants.STANDARD_DATE_TIME_LENGTH)).thenReturn("--------------");
        DateTimeParser parser = new DateTimeParser();
        NitfDateTime ndt = parser.readNitfDateTime(mockReader);
        assertNull(ndt.getZonedDateTime());
        assertEquals("--------------", ndt.getSourceString());
    }

    // Test incomplete format.
    @Test
    public void testIncompleteParsing() throws NitfFormatException {
        NitfReader mockReader = mock(NitfReader.class);
        when(mockReader.getFileType()).thenReturn(FileType.NITF_TWO_ONE);
        when(mockReader.readBytes(CommonConstants.STANDARD_DATE_TIME_LENGTH)).thenReturn("3");
        DateTimeParser parser = new DateTimeParser();
        NitfDateTime ndt = parser.readNitfDateTime(mockReader);
        assertNull(ndt.getZonedDateTime());
        assertEquals("3", ndt.getSourceString());
    }

    // Test when we have all - markers in NITF 2.0 .
    @Test
    public void testEmptyParsing20() throws NitfFormatException {
        NitfReader mockReader = mock(NitfReader.class);
        when(mockReader.getFileType()).thenReturn(FileType.NITF_TWO_ZERO);
        when(mockReader.readBytes(CommonConstants.STANDARD_DATE_TIME_LENGTH)).thenReturn("--------------");
        DateTimeParser parser = new DateTimeParser();
        NitfDateTime ndt = parser.readNitfDateTime(mockReader);
        assertNull(ndt.getZonedDateTime());
        assertEquals("--------------", ndt.getSourceString());
    }

    // Test when we have just the Z marker in NITF 2.0 .
    @Test
    public void testEmptyZParsing20() throws NitfFormatException {
        NitfReader mockReader = mock(NitfReader.class);
        when(mockReader.getFileType()).thenReturn(FileType.NITF_TWO_ZERO);
        when(mockReader.readBytes(CommonConstants.STANDARD_DATE_TIME_LENGTH)).thenReturn("--------Z-----");
        DateTimeParser parser = new DateTimeParser();
        assertNull(parser.readNitfDateTime(mockReader).getZonedDateTime());
        assertEquals("--------Z-----", parser.readNitfDateTime(mockReader).getSourceString());
    }

    // Test when we have just spaces in NITF 2.0 .
    @Test
    public void testEmptySpaceParsing20() throws NitfFormatException {
        NitfReader mockReader = mock(NitfReader.class);
        when(mockReader.getFileType()).thenReturn(FileType.NITF_TWO_ZERO);
        when(mockReader.readBytes(CommonConstants.STANDARD_DATE_TIME_LENGTH)).thenReturn("              ");
        DateTimeParser parser = new DateTimeParser();
        assertNull(parser.readNitfDateTime(mockReader).getZonedDateTime());
        assertEquals("              ", parser.readNitfDateTime(mockReader).getSourceString());
    }
}
