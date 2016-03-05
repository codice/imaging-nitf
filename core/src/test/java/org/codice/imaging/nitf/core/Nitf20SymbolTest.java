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

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.time.format.DateTimeFormatter;

import org.codice.imaging.nitf.core.common.FileType;
import org.codice.imaging.nitf.core.common.NitfInputStreamReader;
import org.codice.imaging.nitf.core.common.NitfReader;
import org.codice.imaging.nitf.core.header.NitfFileParser;
import org.codice.imaging.nitf.core.header.NitfHeader;
import org.codice.imaging.nitf.core.security.FileSecurityMetadata;
import org.codice.imaging.nitf.core.security.SecurityClassification;
import org.codice.imaging.nitf.core.security.SecurityMetadata;
import org.codice.imaging.nitf.core.symbol.SymbolColour;
import org.codice.imaging.nitf.core.symbol.SymbolSegment;
import org.codice.imaging.nitf.core.symbol.SymbolType;
import org.junit.Before;
import org.junit.Test;

public class Nitf20SymbolTest {

    private DateTimeFormatter formatter = null;

    @Before
    public void beforeTest() {
        formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    }

    @Test
    public void testU1060A() throws IOException, ParseException {
        InputStream is = getInputStream();
        AllDataExtractionParseStrategy parseStrategy = new AllDataExtractionParseStrategy();
        NitfReader reader = new NitfInputStreamReader(new BufferedInputStream(is));
        NitfFileParser.parse(reader, parseStrategy);
        assertFileSegmentDataIsAsExpected(parseStrategy.getNitfDataSource());

        SymbolSegment symbolSegment1 = parseStrategy.getNitfDataSource().getSymbolSegments().get(0);
        assertSymbolSegmentHeaderDataIsAsExpected(symbolSegment1);
        byte[] allData = new byte[931];
        int bytesRead = symbolSegment1.getData().read(allData);
        assertEquals(930, bytesRead);
        is.close();
    }

    @Test
    public void testNoSegmentDataU1060A() throws IOException, ParseException {
        InputStream is = getInputStream();
        HeaderOnlyNitfParseStrategy parseStrategy = new HeaderOnlyNitfParseStrategy();
        NitfReader reader = new NitfInputStreamReader(new BufferedInputStream(is));
        NitfFileParser.parse(reader, parseStrategy);
        assertFileSegmentDataIsAsExpected(parseStrategy.getNitfDataSource());

        SymbolSegment symbolSegment1 = parseStrategy.getNitfDataSource().getSymbolSegments().get(0);
        assertSymbolSegmentHeaderDataIsAsExpected(symbolSegment1);
        assertEquals(1, parseStrategy.getNitfDataSource().getSymbolSegments().size());
        assertNull(parseStrategy.getNitfDataSource().getSymbolSegments().get(0).getData());

        is.close();
    }

    private InputStream getInputStream() {
        final String nitf20File = "/JitcNitf20Samples/U_1060A.NTF";

        assertNotNull("Test file missing", getClass().getResource(nitf20File));

        return getClass().getResourceAsStream(nitf20File);
    }

    private void assertFileSegmentDataIsAsExpected(NitfDataSource dataSource) {
        NitfHeader nitfHeader = dataSource.getNitfHeader();
        assertEquals(FileType.NITF_TWO_ZERO, nitfHeader.getFileType());
        assertEquals(1, nitfHeader.getComplexityLevel());
        assertEquals("", nitfHeader.getStandardType());
        assertEquals("PLYLIN2", nitfHeader.getOriginatingStationId());
        assertEquals("1993-09-03 19:16:36", formatter.format(nitfHeader.getFileDateTime().getZonedDateTime()));
        assertEquals("checks for rendering of polyline. line width 1, line type 3,4,5. def line type.", nitfHeader.getFileTitle());
        FileSecurityMetadata securityMetadata = nitfHeader.getFileSecurityMetadata();
        assertUnclasAndEmpty(securityMetadata);
        assertEquals("999998", securityMetadata.getDowngradeDateOrSpecialCase());
        assertEquals("This  file   will not need a downgrade.", securityMetadata.getDowngradeEvent());
        assertNull(securityMetadata.getSecuritySourceDate());

        assertEquals("00001", nitfHeader.getFileSecurityMetadata().getFileCopyNumber());
        assertEquals("00001", nitfHeader.getFileSecurityMetadata().getFileNumberOfCopies());
        assertEquals("JITC Fort Huachuca, AZ", nitfHeader.getOriginatorsName());
        assertEquals("(602) 538-5458", nitfHeader.getOriginatorsPhoneNumber());
        assertEquals(0, dataSource.getImageSegments().size());
        assertEquals(0, dataSource.getGraphicSegments().size());
        assertEquals(0, dataSource.getLabelSegments().size());
        assertEquals(0, dataSource.getTextSegments().size());
        assertEquals(0, dataSource.getDataExtensionSegments().size());
    }

    private void assertSymbolSegmentHeaderDataIsAsExpected(SymbolSegment symbolSegment1) {
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

    private void assertUnclasAndEmpty(SecurityMetadata securityMetadata) {
        assertNotNull(securityMetadata);
        assertEquals(SecurityClassification.UNCLASSIFIED, securityMetadata.getSecurityClassification());
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
