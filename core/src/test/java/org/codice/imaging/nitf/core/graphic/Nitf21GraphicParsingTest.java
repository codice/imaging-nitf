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
package org.codice.imaging.nitf.core.graphic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;

import org.codice.imaging.nitf.core.HeaderOnlyNitfParseStrategy;
import org.codice.imaging.nitf.core.header.NitfFileParser;
import org.codice.imaging.nitf.core.common.NitfInputStreamReader;
import org.codice.imaging.nitf.core.common.NitfReader;
import org.codice.imaging.nitf.core.security.SecurityClassification;
import org.codice.imaging.nitf.core.security.SecurityMetadata;
import org.junit.Test;

public class Nitf21GraphicParsingTest {

    @Test
    public void testExtractionWithOptionTurnedOn() throws IOException, ParseException {
        GraphicDataExtractionParseStrategy parseStrategy = new GraphicDataExtractionParseStrategy();
        NitfReader reader = new NitfInputStreamReader(new BufferedInputStream(getInputStream()));
        NitfFileParser.parse(reader, parseStrategy);
        assertEquals(1, parseStrategy.getNitfDataSource().getGraphicSegments().size());

        GraphicSegment graphicSegment = parseStrategy.getNitfDataSource().getGraphicSegments().get(0);
        assertGraphicSegmentMetadataIsAsExpected(graphicSegment);
        byte[] allData = new byte[780];
        int bytesRead = parseStrategy.getNitfDataSource().getGraphicSegments().get(0).getData().read(allData);
        assertEquals(780, bytesRead);
    }

    @Test
    public void testExtractionWithOptionTurnedOff() throws IOException, ParseException {
        HeaderOnlyNitfParseStrategy parseStrategy = new HeaderOnlyNitfParseStrategy();
        NitfReader reader = new NitfInputStreamReader(new BufferedInputStream(getInputStream()));
        NitfFileParser.parse(reader, parseStrategy);
        assertEquals(1, parseStrategy.getNitfDataSource().getGraphicSegments().size());

        GraphicSegment graphicSegment = parseStrategy.getNitfDataSource().getGraphicSegments().get(0);
        assertGraphicSegmentMetadataIsAsExpected(graphicSegment);
        assertNull(parseStrategy.getNitfDataSource().getGraphicSegments().get(0).getData());
    }

    private void assertGraphicSegmentMetadataIsAsExpected(GraphicSegment graphicSegment) {
        assertNotNull(graphicSegment);
        assertEquals("0000000001", graphicSegment.getIdentifier());
        assertEquals("multi.cgm  SYMBOL.", graphicSegment.getGraphicName());
        assertUnclasAndEmpty(graphicSegment.getSecurityMetadata());
        assertEquals(1, graphicSegment.getGraphicDisplayLevel());
        assertEquals(0, graphicSegment.getAttachmentLevel());
        assertEquals(0, graphicSegment.getGraphicLocationRow());
        assertEquals(0, graphicSegment.getGraphicLocationColumn());
        assertEquals(25, graphicSegment.getBoundingBox1Row());
        assertEquals(25, graphicSegment.getBoundingBox1Column());
        assertEquals(GraphicColour.COLOUR, graphicSegment.getGraphicColour());
        assertEquals(79, graphicSegment.getBoundingBox2Row());
        assertEquals(430, graphicSegment.getBoundingBox2Column());
    }

    private InputStream getInputStream() {
        final String testfile = "/JitcNitf21Samples/i_3051e.ntf";

        assertNotNull("Test file missing", getClass().getResource(testfile));
        return getClass().getResourceAsStream(testfile);
    }

    void assertUnclasAndEmpty(SecurityMetadata securityMetadata) {
        assertEquals(SecurityClassification.UNCLASSIFIED, securityMetadata.getSecurityClassification());
        assertEquals("", securityMetadata.getSecurityClassificationSystem());
        assertEquals("", securityMetadata.getCodewords());
        assertEquals("", securityMetadata.getControlAndHandling());
        assertEquals("", securityMetadata.getReleaseInstructions());
        assertEquals("", securityMetadata.getDeclassificationType());
        assertEquals("", securityMetadata.getDeclassificationDate());
        assertEquals("", securityMetadata.getDeclassificationExemption());
        assertEquals("", securityMetadata.getDowngrade());
        assertEquals("", securityMetadata.getDowngradeDate());
        assertEquals("", securityMetadata.getClassificationText());
        assertEquals("", securityMetadata.getClassificationAuthorityType());
        assertEquals("", securityMetadata.getClassificationAuthority());
        assertEquals("", securityMetadata.getClassificationReason());
        assertEquals("", securityMetadata.getSecurityControlNumber());
    }

}
