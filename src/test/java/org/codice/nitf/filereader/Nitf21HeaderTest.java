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
import static org.junit.Assert.assertTrue;

import java.io.InputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.junit.Test;

public class Nitf21HeaderTest {

    @Test
    public void testCompliantHeaderRead() throws IOException, ParseException {
        final String simpleNitf21File = "/i_3034c.ntf";

        assertNotNull("Test file missing", getClass().getResource(simpleNitf21File));

        InputStream is = getClass().getResourceAsStream(simpleNitf21File);
        NitfHeaderReader reader = new NitfHeaderReader(is);
        assertTrue(reader.isNitf());
        assertEquals(NitfVersion.TWO_ONE, reader.getVersion());
        assertEquals(3, reader.getComplexityLevel());
        assertEquals("BF01", reader.getStandardType());
        assertEquals("I_3034C", reader.getOriginatingStationId());
        assertEquals("1997-12-18 12:15:39", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(reader.getFileDateTime()));
        assertEquals("Check an RGB/LUT 1 bit image maps black to red and white to green.", reader.getFileTitle());
        assertEquals(NitfSecurityClassification.UNCLASSIFIED, reader.getSecurityClassification());
        assertEquals("", reader.getFileSecurityClassificationSystem());
        assertEquals("", reader.getFileCodewords());
        assertEquals("", reader.getFileControlAndHandling());
        assertEquals("", reader.getFileReleaseInstructions());
        assertEquals("", reader.getFileDeclassificationType());
        assertEquals("", reader.getFileDeclassificationDate());
        assertEquals("", reader.getFileDeclassificationExemption());
        assertEquals("", reader.getFileDowngrade());
        assertEquals("", reader.getFileDowngradeDate());
        assertEquals("", reader.getFileClassificationText());
        assertEquals("", reader.getFileClassificationAuthorityType());
        assertEquals("", reader.getFileClassificationAuthority());
        assertEquals("", reader.getFileClassificationReason());
        assertEquals("", reader.getFileSecurityControlNumber());
        assertEquals("00001", reader.getFileCopyNumber());
        assertEquals("00001", reader.getFileNumberOfCopies());
        assertEquals(0, reader.getFileBackgroundColourRed());
        assertEquals(0, reader.getFileBackgroundColourGreen());
        assertEquals(0, reader.getFileBackgroundColourBlue());
        assertEquals("JITC", reader.getOriginatorsName());
        assertEquals("(520) 538-5458", reader.getOriginatorsPhoneNumber());
        assertEquals(933L, reader.getFileLength());
        assertEquals(404, reader.getHeaderLength());
        assertEquals(1, reader.getNumberOfImageSegments());
        assertEquals(450, reader.getLengthOfImageSubheader(0));
        assertEquals(79, reader.getLengthOfImage(0));
        assertEquals(0, reader.getNumberOfGraphicsSegments());
        assertEquals(0, reader.getNumberOfTextSegments());
        assertEquals(0, reader.getNumberOfDataExtensionSegments());
        assertEquals(0, reader.getNumberOfReservedExtensionSegments());
        assertEquals(0, reader.getUserDefinedHeaderDataLength());
        assertEquals(0, reader.getExtendedHeaderDataLength());

        // Checks for ImageSegment.
        NitfImageSegment segment1 = reader.getImageSegment(1);
        assertNotNull(segment1);
        assertEquals("Missing ID", segment1.getImageIdentifier1());
        assertEquals("1996-12-18 12:15:39", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(segment1.getImageDateTime()));
        assertEquals("", segment1.getImageTargetId());
        assertEquals("- BASE IMAGE -", segment1.getImageIdentifier2());

        is.close();
    }

}