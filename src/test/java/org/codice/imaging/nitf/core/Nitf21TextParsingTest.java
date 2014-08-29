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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.InputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.EnumSet;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.Test;

public class Nitf21TextParsingTest {

    private SimpleDateFormat formatter = null;

    @Before
    public void beforeTest() {
        formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    @Test
    public void testExtractionWithOptionTurnedOn() throws IOException, ParseException {
        NitfFile file = NitfFileFactory.parseSelectedDataSegments(getInputStream(), EnumSet.of(ParseOption.EXTRACT_TEXT_SEGMENT_DATA));
        assertEquals(1, file.getNumberOfTextSegments());

        NitfTextSegment textSegment = file.getTextSegment(1);
        assertTextSegmentMetadataIsAsExpected(textSegment);
        assertEquals("Paragon Imaging rftopidf, version 1.0\n\nConverted on Wed Jun 30 11:02:27 1993\n\n", textSegment.getTextData());
    }

    @Test
    public void testExtractionWithOptionTurnedOff() throws IOException, ParseException {
        NitfFile file = NitfFileFactory.parseSelectedDataSegments(getInputStream(), EnumSet.noneOf(ParseOption.class));
        assertEquals(1, file.getNumberOfTextSegments());

        NitfTextSegment textSegment = file.getTextSegment(1);
        assertTextSegmentMetadataIsAsExpected(textSegment);
        assertNull(textSegment.getTextData());
    }

    @Test
    public void testExtractionWithDefault() throws IOException, ParseException {
        NitfFile file = NitfFileFactory.parseHeadersOnly(getInputStream());
        assertEquals(1, file.getNumberOfTextSegments());

        NitfTextSegment textSegment = file.getTextSegment(1);
        assertTextSegmentMetadataIsAsExpected(textSegment);
        assertNull(textSegment.getTextData());
    }

    private void assertTextSegmentMetadataIsAsExpected(NitfTextSegment textSegment) {
        assertNotNull(textSegment);
        assertEquals(" PIDF T", textSegment.getIdentifier());
        assertEquals(1, textSegment.getAttachmentLevel());
        assertEquals("1998-02-17 10:19:39", formatter.format(textSegment.getTextDateTime().toDate()));
        assertEquals("                                                    Paragon Imaging Comment File", textSegment.getTextTitle());
        assertEquals(TextFormat.BASICCHARACTERSET, textSegment.getTextFormat());
    }

    private InputStream getInputStream() {
        final String testfile = "/ns3201a.nsf";

        assertNotNull("Test file missing", getClass().getResource(testfile));
        return getClass().getResourceAsStream(testfile);
    }

}