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
import static org.junit.Assert.assertTrue;

import java.io.InputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.EnumSet;

import org.junit.rules.ExpectedException;
import org.junit.Rule;
import org.junit.Test;

public class Nitf20HeaderTest {

    @Test
    public void testCompliantHeaderRead() throws IOException, ParseException {
        final String simpleNitf20File = "/U_1114A.NTF";

        assertNotNull("Test file missing", getClass().getResource(simpleNitf20File));

        InputStream is = getClass().getResourceAsStream(simpleNitf20File);
        NitfFile file = new NitfFile();
        file.parse(is, EnumSet.of(ParseOption.ExtractTextSegmentData));
        assertEquals(FileType.NITF_TWO_ZERO, file.getFileType());
        assertEquals(1, file.getComplexityLevel());
        assertEquals("", file.getStandardType());
        assertEquals("U21H00N1", file.getOriginatingStationId());
        assertEquals("1994-04-03 19:16:36", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(file.getFileDateTime()));
        assertEquals("checks the handling of an NITF file w/ only a text file.", file.getFileTitle());
        NitfFileSecurityMetadata securityMetadata = file.getFileSecurityMetadata();
        assertNotNull(securityMetadata);
        assertEquals(NitfSecurityClassification.UNCLASSIFIED, securityMetadata.getSecurityClassification());
        assertNull(securityMetadata.getSecurityClassificationSystem());
        assertEquals("", securityMetadata.getCodewords());
        assertEquals("", securityMetadata.getControlAndHandling());
        assertEquals("", securityMetadata.getReleaseInstructions());
        assertNull(securityMetadata.getDeclassificationType());
        assertNull(securityMetadata.getDeclassificationDate());
        assertNull(securityMetadata.getDeclassificationExemption());
        assertNull(securityMetadata.getDowngrade());
        assertNull(securityMetadata.getDowngradeDate());
        assertNull(securityMetadata.getClassificationText());
        assertNull(securityMetadata.getClassificationAuthorityType());
        assertEquals("", securityMetadata.getClassificationAuthority());
        assertNull(securityMetadata.getClassificationReason());
        assertEquals("", securityMetadata.getSecurityControlNumber());
        assertEquals("999998", securityMetadata.getDowngradeDateOrSpecialCase());
        assertEquals("This  file   will not need a downgrade.", securityMetadata.getDowngradeEvent());

        assertEquals("00001", file.getFileSecurityMetadata().getFileCopyNumber());
        assertEquals("00001", file.getFileSecurityMetadata().getFileNumberOfCopies());
        assertEquals("JITC Fort Huachuca, AZ", file.getOriginatorsName());
        assertEquals("(602) 538-5458", file.getOriginatorsPhoneNumber());
        assertEquals(0, file.getNumberOfImageSegments());
        assertEquals(0, file.getNumberOfGraphicSegments());
        assertEquals(1, file.getNumberOfTextSegments());
        assertEquals(0, file.getNumberOfDataExtensionSegments());

        NitfTextSegment textSegment = file.getTextSegment(1);
        assertNotNull(textSegment);
        assertEquals("0000000001", textSegment.getTextIdentifier());
        assertEquals(0, textSegment.getTextAttachmentLevel());
        assertEquals("1993-03-27 23:55:36", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(textSegment.getTextDateTime()));
        assertEquals("This is the title of unclassified text file #1 in NITF  file   U21H00N1.", textSegment.getTextTitle());
        NitfSecurityMetadata textSecurityMetadata = textSegment.getSecurityMetadata();
        assertNotNull(textSecurityMetadata);
        assertEquals(NitfSecurityClassification.UNCLASSIFIED, textSecurityMetadata.getSecurityClassification());
        assertNull(textSecurityMetadata.getSecurityClassificationSystem());
        assertEquals("", textSecurityMetadata.getCodewords());
        assertEquals("", textSecurityMetadata.getControlAndHandling());
        assertEquals("", textSecurityMetadata.getReleaseInstructions());
        assertNull(textSecurityMetadata.getDeclassificationType());
        assertNull(textSecurityMetadata.getDeclassificationDate());
        assertNull(textSecurityMetadata.getDeclassificationExemption());
        assertNull(textSecurityMetadata.getDowngrade());
        assertNull(textSecurityMetadata.getDowngradeDate());
        assertNull(textSecurityMetadata.getClassificationText());
        assertNull(textSecurityMetadata.getClassificationAuthorityType());
        assertEquals("", textSecurityMetadata.getClassificationAuthority());
        assertNull(textSecurityMetadata.getClassificationReason());
        assertEquals("", textSecurityMetadata.getSecurityControlNumber());
        assertEquals("999998", textSecurityMetadata.getDowngradeDateOrSpecialCase());
        assertEquals("This text will never need downgrading.", textSecurityMetadata.getDowngradeEvent());
        assertEquals(TextFormat.BASICCHARACTERSET, textSegment.getTextFormat());
        assertEquals("A", textSegment.getTextData());

        is.close();
    }

    @Test
    public void testU1123() throws IOException, ParseException {
        final String nitf20File = "/U_1123A.NTF";

        assertNotNull("Test file missing", getClass().getResource(nitf20File));

        InputStream is = getClass().getResourceAsStream(nitf20File);
        NitfFile file = new NitfFile();
        file.parse(is, EnumSet.allOf(ParseOption.class));
        assertEquals(FileType.NITF_TWO_ZERO, file.getFileType());
        assertEquals(1, file.getComplexityLevel());
        assertEquals("", file.getStandardType());
        assertEquals("U217G0J1", file.getOriginatingStationId());
        assertEquals("1992-11-03 13:52:26", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(file.getFileDateTime()));
        assertEquals("This NITF message contains 5 images, 4 symbols, 4 labels and 1 text.", file.getFileTitle());
        NitfFileSecurityMetadata securityMetadata = file.getFileSecurityMetadata();
        assertNotNull(securityMetadata);
        assertEquals(NitfSecurityClassification.UNCLASSIFIED, securityMetadata.getSecurityClassification());
        assertNull(securityMetadata.getSecurityClassificationSystem());
        assertEquals("", securityMetadata.getCodewords());
        assertEquals("", securityMetadata.getControlAndHandling());
        assertEquals("", securityMetadata.getReleaseInstructions());
        assertNull(securityMetadata.getDeclassificationType());
        assertNull(securityMetadata.getDeclassificationDate());
        assertNull(securityMetadata.getDeclassificationExemption());
        assertNull(securityMetadata.getDowngrade());
        assertNull(securityMetadata.getDowngradeDate());
        assertNull(securityMetadata.getClassificationText());
        assertNull(securityMetadata.getClassificationAuthorityType());
        assertEquals("", securityMetadata.getClassificationAuthority());
        assertNull(securityMetadata.getClassificationReason());
        assertEquals("", securityMetadata.getSecurityControlNumber());
        assertEquals("999998", securityMetadata.getDowngradeDateOrSpecialCase());
        assertEquals("This message will not need a downgrade.", securityMetadata.getDowngradeEvent());

        assertEquals("00001", file.getFileSecurityMetadata().getFileCopyNumber());
        assertEquals("00001", file.getFileSecurityMetadata().getFileNumberOfCopies());
        assertEquals("JITC", file.getOriginatorsName());
        assertEquals("(602) 538-5458", file.getOriginatorsPhoneNumber());
        assertEquals(5, file.getNumberOfImageSegments());
        assertEquals(0, file.getNumberOfGraphicSegments());
        assertEquals(4, file.getNumberOfSymbolSegments());
        assertEquals(4, file.getNumberOfLabelSegments());
        assertEquals(1, file.getNumberOfTextSegments());
        assertEquals(0, file.getNumberOfDataExtensionSegments());

        NitfImageSegment imageSegment1 = file.getImageSegment(1);
        assertNotNull(imageSegment1);

        NitfImageSegment imageSegment2 = file.getImageSegment(2);
        assertNotNull(imageSegment2);

        NitfImageSegment imageSegment3 = file.getImageSegment(3);
        assertNotNull(imageSegment3);

        NitfImageSegment imageSegment4 = file.getImageSegment(4);
        assertNotNull(imageSegment4);

        NitfImageSegment imageSegment5 = file.getImageSegment(5);
        assertNotNull(imageSegment5);

        NitfTextSegment textSegment = file.getTextSegment(1);
        assertNotNull(textSegment);
        assertEquals("0000000001", textSegment.getTextIdentifier());
        assertEquals(0, textSegment.getTextAttachmentLevel());
        assertEquals("1990-06-07 21:11:36", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(textSegment.getTextDateTime()));
        assertEquals("This is the title of unclassified text file #1 in NITF message JR1_B.", textSegment.getTextTitle());
        NitfSecurityMetadata textSecurityMetadata = textSegment.getSecurityMetadata();
        assertNotNull(textSecurityMetadata);
        assertEquals(NitfSecurityClassification.UNCLASSIFIED, textSecurityMetadata.getSecurityClassification());
        assertNull(textSecurityMetadata.getSecurityClassificationSystem());
        assertEquals("", textSecurityMetadata.getCodewords());
        assertEquals("", textSecurityMetadata.getControlAndHandling());
        assertEquals("", textSecurityMetadata.getReleaseInstructions());
        assertNull(textSecurityMetadata.getDeclassificationType());
        assertNull(textSecurityMetadata.getDeclassificationDate());
        assertNull(textSecurityMetadata.getDeclassificationExemption());
        assertNull(textSecurityMetadata.getDowngrade());
        assertNull(textSecurityMetadata.getDowngradeDate());
        assertNull(textSecurityMetadata.getClassificationText());
        assertNull(textSecurityMetadata.getClassificationAuthorityType());
        assertEquals("", textSecurityMetadata.getClassificationAuthority());
        assertNull(textSecurityMetadata.getClassificationReason());
        assertEquals("", textSecurityMetadata.getSecurityControlNumber());
        assertEquals("999998", textSecurityMetadata.getDowngradeDateOrSpecialCase());
        assertEquals("This text will never need downgrading.", textSecurityMetadata.getDowngradeEvent());
        assertEquals(TextFormat.BASICCHARACTERSET, textSegment.getTextFormat());
        assertEquals("123456\r\n", textSegment.getTextData());

        is.close();
    }
}