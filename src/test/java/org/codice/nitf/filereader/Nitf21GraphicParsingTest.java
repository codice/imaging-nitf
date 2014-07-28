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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.InputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.EnumSet;

import org.junit.Test;

public class Nitf21GraphicParsingTest {

    @Test
    public void testExtractionWithOptionTurnedOn() throws IOException, ParseException {
        NitfFile file = new NitfFile();
        file.parse(getInputStream(), EnumSet.of(ParseOption.ExtractGraphicSegmentData));
        assertEquals(1, file.getNumberOfGraphicSegments());

        NitfGraphicSegment graphicSegment = file.getGraphicSegment(1);
        assertGraphicSegmentMetadataIsAsExpected(graphicSegment);
        assertEquals(780, graphicSegment.getGraphicData().length);
    }

    @Test
    public void testExtractionWithOptionTurnedOff() throws IOException, ParseException {
        NitfFile file = new NitfFile();
        file.parse(getInputStream(), EnumSet.noneOf(ParseOption.class));
        assertEquals(1, file.getNumberOfGraphicSegments());

        NitfGraphicSegment graphicSegment = file.getGraphicSegment(1);
        assertGraphicSegmentMetadataIsAsExpected(graphicSegment);
        assertNull(graphicSegment.getGraphicData());
    }

    private void assertGraphicSegmentMetadataIsAsExpected(NitfGraphicSegment graphicSegment) {
        assertNotNull(graphicSegment);
        // TODO: check all the graphic bits
    }

    private InputStream getInputStream() {
        final String testfile = "/i_3051e.ntf";

        assertNotNull("Test file missing", getClass().getResource(testfile));
        return getClass().getResourceAsStream(testfile);
    }

}