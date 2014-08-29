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
import static org.junit.Assert.assertTrue;

import java.io.InputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.EnumSet;
import java.util.TimeZone;

import org.junit.rules.ExpectedException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class Nitf20SymbolTest {

    private SimpleDateFormat formatter = null;

    @Before
    public void beforeTest() {
        formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    @Test
    public void testU1060A() throws IOException, ParseException {
        InputStream is = getInputStream();
        NitfFile file = NitfFileFactory.parseSelectedDataSegments(is, EnumSet.allOf(ParseOption.class));
        assertFileSegmentDataIsAsExpected(file);

        NitfSymbolSegment symbolSegment1 = file.getSymbolSegment(1);
        assertSymbolSegmentDataIsAsExpected(symbolSegment1);
        assertEquals(930, symbolSegment1.getSymbolData().length);

        is.close();
    }

    @Test
    public void testNoSegmentDataU1060A() throws IOException, ParseException {
        InputStream is = getInputStream();
        NitfFile file = NitfFileFactory.parseSelectedDataSegments(is, EnumSet.noneOf(ParseOption.class));
        assertFileSegmentDataIsAsExpected(file);

        NitfSymbolSegment symbolSegment1 = file.getSymbolSegment(1);
        assertSymbolSegmentDataIsAsExpected(symbolSegment1);
        assertNull(symbolSegment1.getSymbolData());

        is.close();
    }

    private InputStream getInputStream() {
        final String nitf20File = "/U_1060A.NTF";

        assertNotNull("Test file missing", getClass().getResource(nitf20File));

        return getClass().getResourceAsStream(nitf20File);
    }

    private void assertFileSegmentDataIsAsExpected(NitfFile file) {
        assertEquals(FileType.NITF_TWO_ZERO, file.getFileType());
        assertEquals(1, file.getComplexityLevel());
        assertEquals("", file.getStandardType());
        assertEquals("PLYLIN2", file.getOriginatingStationId());
        assertEquals("1993-09-03 19:16:36", formatter.format(file.getFileDateTime().toDate()));
        assertEquals("checks for rendering of polyline. line width 1, line type 3,4,5. def line type.", file.getFileTitle());
        NitfFileSecurityMetadata securityMetadata = file.getFileSecurityMetadata();
        assertUnclasAndEmpty(securityMetadata);
        assertEquals("999998", securityMetadata.getDowngradeDateOrSpecialCase());
        assertEquals("This  file   will not need a downgrade.", securityMetadata.getDowngradeEvent());
        assertNull(securityMetadata.getSecuritySourceDate());

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
    }

    private void assertSymbolSegmentDataIsAsExpected(NitfSymbolSegment symbolSegment1) {
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
    }

    private void assertUnclasAndEmpty(NitfSecurityMetadata securityMetadata) {
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
