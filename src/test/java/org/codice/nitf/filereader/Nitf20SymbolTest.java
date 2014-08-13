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

public class Nitf20SymbolTest {
    @Test
    public void testU1060A() throws IOException, ParseException {
        final String nitf20File = "/U_1060A.NTF";

        assertNotNull("Test file missing", getClass().getResource(nitf20File));

        InputStream is = getClass().getResourceAsStream(nitf20File);
        NitfFile file = NitfFileFactory.parse(is, EnumSet.allOf(ParseOption.class));
        assertEquals(FileType.NITF_TWO_ZERO, file.getFileType());
        assertEquals(1, file.getComplexityLevel());
        assertEquals("", file.getStandardType());
        assertEquals("PLYLIN2", file.getOriginatingStationId());
        assertEquals("1993-09-03 19:16:36", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(file.getFileDateTime()));
        assertEquals("checks for rendering of polyline. line width 1, line type 3,4,5. def line type.", file.getFileTitle());
        NitfFileSecurityMetadata securityMetadata = file.getFileSecurityMetadata();
        assertUnclasAndEmpty(securityMetadata);
        assertEquals("999998", securityMetadata.getDowngradeDateOrSpecialCase());
        assertEquals("This  file   will not need a downgrade.", securityMetadata.getDowngradeEvent());

        assertEquals("00001", file.getFileSecurityMetadata().getFileCopyNumber());
        assertEquals("00001", file.getFileSecurityMetadata().getFileNumberOfCopies());
        assertEquals("JITC Fort Huachuca, AZ", file.getOriginatorsName());
        assertEquals("(602) 538-5458", file.getOriginatorsPhoneNumber());
        assertEquals(0, file.getNumberOfImageSegments());
        assertEquals(0, file.getNumberOfGraphicSegments());
        assertEquals(1, file.getNumberOfSymbolSegments());
        assertEquals(0, file.getNumberOfLabelSegments());
        assertEquals(0, file.getNumberOfTextSegments());
        assertEquals(0, file.getNumberOfDataExtensionSegments());

        NitfSymbolSegment symbolSegment1 = file.getSymbolSegment(1);
        assertNotNull(symbolSegment1);
        assertEquals("0000000001", symbolSegment1.getIdentifier());
        assertEquals("multi.cgm  SYMBOL.", symbolSegment1.getSymbolName());
        assertUnclasAndEmpty(symbolSegment1.getSecurityMetadata());
        assertEquals("999998", symbolSegment1.getSecurityMetadata().getDowngradeDateOrSpecialCase());
        assertEquals("This symbol will never need downgrading.", symbolSegment1.getSecurityMetadata().getDowngradeEvent());
        assertEquals(SymbolType.CGM, symbolSegment1.getSymbolType());
        assertEquals(SymbolColour.UNKNOWN, symbolSegment1.getSymbolColour());
        assertEquals(0, symbolSegment1.getNumberOfLinesPerSymbol());
        assertEquals(0, symbolSegment1.getNumberOfPixelsPerLine());
        assertEquals(0, symbolSegment1.getLineWidth());
        assertEquals(0, symbolSegment1.getNumberOfBitsPerPixel());
        assertEquals(1, symbolSegment1.getSymbolDisplayLevel());
        assertEquals(0, symbolSegment1.getAttachmentLevel());
        assertEquals(0, symbolSegment1.getSymbolLocationRow());
        assertEquals(0, symbolSegment1.getSymbolLocationColumn());
        assertEquals(0, symbolSegment1.getSymbolLocation2Row());
        assertEquals(0, symbolSegment1.getSymbolLocation2Column());
        assertEquals("000000", symbolSegment1.getSymbolNumber());
        assertEquals(0, symbolSegment1.getSymbolRotation());
        assertEquals(930, symbolSegment1.getSymbolData().length);

        is.close();
    }

    void assertUnclasAndEmpty(NitfSecurityMetadata securityMetadata) {
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
    }
}