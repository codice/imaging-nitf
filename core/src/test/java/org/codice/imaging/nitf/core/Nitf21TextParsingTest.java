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

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.format.DateTimeFormatter;
import org.codice.imaging.nitf.core.common.NitfFormatException;
import org.codice.imaging.nitf.core.common.NitfInputStreamReader;
import org.codice.imaging.nitf.core.common.NitfReader;
import org.codice.imaging.nitf.core.header.NitfParser;
import org.codice.imaging.nitf.core.text.TextFormat;
import org.codice.imaging.nitf.core.text.TextSegment;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import org.junit.Before;
import org.junit.Test;

public class Nitf21TextParsingTest {

    private DateTimeFormatter formatter = null;

    @Before
    public void beforeTest() {
        formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    }

    @Test
    public void testExtractionWithOptionTurnedOn() throws IOException, NitfFormatException {
        SlottedParseStrategy parseStrategy = new SlottedParseStrategy(SlottedParseStrategy.TEXT_DATA);
        NitfReader reader = new NitfInputStreamReader(new BufferedInputStream(getInputStream()));
        NitfParser.parse(reader, parseStrategy);
        assertEquals(1, parseStrategy.getDataSource().getTextSegments().size());

        TextSegment textSegment = parseStrategy.getDataSource().getTextSegments().get(0);
        assertTextSegmentMetadataIsAsExpected(textSegment);
        assertEquals("Paragon Imaging rftopidf, version 1.0\n\nConverted on Wed Jun 30 11:02:27 1993\n\n", textSegment.getData());
    }

    @Test
    public void testExtractionWithDefault() throws IOException, NitfFormatException {
        SlottedParseStrategy parseStrategy = new SlottedParseStrategy(SlottedParseStrategy.HEADERS_ONLY);
        NitfReader reader = new NitfInputStreamReader(new BufferedInputStream(getInputStream()));
        NitfParser.parse(reader, parseStrategy);
        assertEquals(1, parseStrategy.getDataSource().getTextSegments().size());

        TextSegment textSegment = parseStrategy.getDataSource().getTextSegments().get(0);
        assertTextSegmentMetadataIsAsExpected(textSegment);
        assertNull(parseStrategy.getDataSource().getTextSegments().get(0).getData());
    }

    private void assertTextSegmentMetadataIsAsExpected(TextSegment textSegment) {
        assertNotNull(textSegment);
        assertEquals(" PIDF T", textSegment.getIdentifier());
        assertEquals(1, textSegment.getAttachmentLevel());
        assertEquals("1998-02-17 10:19:39", formatter.format(textSegment.getTextDateTime().getZonedDateTime()));
        assertEquals("                                                    Paragon Imaging Comment File", textSegment.getTextTitle());
        assertEquals(TextFormat.BASICCHARACTERSET, textSegment.getTextFormat());
    }

    private InputStream getInputStream() {
        final String testfile = "/JitcNitf21Samples/ns3201a.nsf";

        assertNotNull("Test file missing", getClass().getResource(testfile));
        return getClass().getResourceAsStream(testfile);
    }

}