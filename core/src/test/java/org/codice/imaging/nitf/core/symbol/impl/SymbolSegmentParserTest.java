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
package org.codice.imaging.nitf.core.symbol.impl;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import org.codice.imaging.nitf.core.common.FileType;
import org.codice.imaging.nitf.core.common.NitfFormatException;
import org.codice.imaging.nitf.core.common.NitfReader;
import org.codice.imaging.nitf.core.common.impl.NitfInputStreamReader;
import org.codice.imaging.nitf.core.impl.SlottedParseStrategy;
import org.codice.imaging.nitf.core.security.SecurityClassification;
import org.codice.imaging.nitf.core.symbol.SymbolColour;
import org.codice.imaging.nitf.core.symbol.SymbolSegment;
import org.codice.imaging.nitf.core.symbol.SymbolType;
import static org.junit.Assert.assertEquals;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Unit Tests for symbol segment parsing
 */
public class SymbolSegmentParserTest {

    public SymbolSegmentParserTest() {
    }

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void CheckCGM() throws NitfFormatException {
        InputStream inputStream = new ByteArrayInputStream(("SY0000000001multi.cgm  SYMBOL.  U                                                                                                                                                                999998This symbol will never need downgrading.0"
                + "C000000000000000100000000000000000000000 00000000000000000").getBytes(StandardCharsets.ISO_8859_1));
        SlottedParseStrategy parseStrategy = new SlottedParseStrategy();
        NitfReader reader = new NitfInputStreamReader(new BufferedInputStream(inputStream));
        reader.setFileType(FileType.NITF_TWO_ZERO);
        SymbolSegmentParser parser = new SymbolSegmentParser();
        SymbolSegment symbolSegment = parser.parse(reader, parseStrategy, 0);
        assertEquals("0000000001", symbolSegment.getIdentifier());
        assertEquals("multi.cgm  SYMBOL.", symbolSegment.getSymbolName());
        assertEquals(SecurityClassification.UNCLASSIFIED, symbolSegment.getSecurityMetadata().getSecurityClassification());
        assertEquals(SymbolType.CGM, symbolSegment.getSymbolType());
        assertEquals(0, symbolSegment.getNumberOfLinesPerSymbol());
        assertEquals(0, symbolSegment.getNumberOfPixelsPerLine());
        assertEquals(0, symbolSegment.getLineWidth());
        assertEquals(0, symbolSegment.getNumberOfBitsPerPixel());
        assertEquals(1, symbolSegment.getSymbolDisplayLevel());
        assertEquals(0, symbolSegment.getAttachmentLevel());
        assertEquals(0, symbolSegment.getSymbolLocationRow());
        assertEquals(0, symbolSegment.getSymbolLocationColumn());
        assertEquals(0, symbolSegment.getSymbolLocation2Row());
        assertEquals(0, symbolSegment.getSymbolLocation2Column());
        assertEquals(SymbolColour.NOT_APPLICABLE, symbolSegment.getSymbolColour());
        assertEquals("000000", symbolSegment.getSymbolNumber());
        assertEquals(0, symbolSegment.getSymbolRotation());
        assertEquals(0, symbolSegment.getExtendedHeaderDataOverflow());
    }

    @Test
    public void CheckBitmapGrayscaleLUT() throws NitfFormatException {
        InputStream inputStream = new ByteArrayInputStream(("SY0000000021abcdefghijklmnopqrstU                                                                                                                                                                999998This symbol will never need downgrading.0"
                + "B001800350000100900800025000350000000000G000000145002pf00003002").getBytes(StandardCharsets.ISO_8859_1));
        SlottedParseStrategy parseStrategy = new SlottedParseStrategy();
        NitfReader reader = new NitfInputStreamReader(new BufferedInputStream(inputStream));
        reader.setFileType(FileType.NITF_TWO_ZERO);
        SymbolSegmentParser parser = new SymbolSegmentParser();
        SymbolSegment symbolSegment = parser.parse(reader, parseStrategy, 0);
        assertEquals("0000000021", symbolSegment.getIdentifier());
        assertEquals("abcdefghijklmnopqrst", symbolSegment.getSymbolName());
        assertEquals(SecurityClassification.UNCLASSIFIED, symbolSegment.getSecurityMetadata().getSecurityClassification());
        assertEquals(SymbolType.BITMAP, symbolSegment.getSymbolType());
        assertEquals(18, symbolSegment.getNumberOfLinesPerSymbol());
        assertEquals(35, symbolSegment.getNumberOfPixelsPerLine());
        assertEquals(0, symbolSegment.getLineWidth());
        assertEquals(1, symbolSegment.getNumberOfBitsPerPixel());
        assertEquals(9, symbolSegment.getSymbolDisplayLevel());
        assertEquals(8, symbolSegment.getAttachmentLevel());
        assertEquals(25, symbolSegment.getSymbolLocationRow());
        assertEquals(35, symbolSegment.getSymbolLocationColumn());
        assertEquals(0, symbolSegment.getSymbolLocation2Row());
        assertEquals(0, symbolSegment.getSymbolLocation2Column());
        assertEquals(SymbolColour.USE_GRAYSCALE_LUT, symbolSegment.getSymbolColour());
        assertEquals("000000", symbolSegment.getSymbolNumber());
        assertEquals(145, symbolSegment.getSymbolRotation());
        assertEquals(2, symbolSegment.getExtendedHeaderDataOverflow());
    }

    @Test
    public void CheckBitmapColourLUT() throws NitfFormatException {
        InputStream inputStream = new ByteArrayInputStream(("SY0000000021abcdefghijklmnopqrstU                                                                                                                                                                999998This symbol will never need downgrading.0"
                + "B001800350000100900800025000350000000000C000000145002pfgAHC00003002").getBytes(StandardCharsets.ISO_8859_1));
        SlottedParseStrategy parseStrategy = new SlottedParseStrategy();
        NitfReader reader = new NitfInputStreamReader(new BufferedInputStream(inputStream));
        reader.setFileType(FileType.NITF_TWO_ZERO);
        SymbolSegmentParser parser = new SymbolSegmentParser();
        SymbolSegment symbolSegment = parser.parse(reader, parseStrategy, 0);
        assertEquals("0000000021", symbolSegment.getIdentifier());
        assertEquals("abcdefghijklmnopqrst", symbolSegment.getSymbolName());
        assertEquals(SecurityClassification.UNCLASSIFIED, symbolSegment.getSecurityMetadata().getSecurityClassification());
        assertEquals(SymbolType.BITMAP, symbolSegment.getSymbolType());
        assertEquals(18, symbolSegment.getNumberOfLinesPerSymbol());
        assertEquals(35, symbolSegment.getNumberOfPixelsPerLine());
        assertEquals(0, symbolSegment.getLineWidth());
        assertEquals(1, symbolSegment.getNumberOfBitsPerPixel());
        assertEquals(9, symbolSegment.getSymbolDisplayLevel());
        assertEquals(8, symbolSegment.getAttachmentLevel());
        assertEquals(25, symbolSegment.getSymbolLocationRow());
        assertEquals(35, symbolSegment.getSymbolLocationColumn());
        assertEquals(0, symbolSegment.getSymbolLocation2Row());
        assertEquals(0, symbolSegment.getSymbolLocation2Column());
        assertEquals(SymbolColour.USE_COLOUR_LUT, symbolSegment.getSymbolColour());
        assertEquals("000000", symbolSegment.getSymbolNumber());
        assertEquals(145, symbolSegment.getSymbolRotation());
        assertEquals(2, symbolSegment.getExtendedHeaderDataOverflow());
    }

    @Test
    public void CheckBadSymbolTypeLUT() throws NitfFormatException {
        InputStream inputStream = new ByteArrayInputStream(("SY0000000021abcdefghijklmnopqrstU                                                                                                                                                                999998This symbol will never need downgrading.0"
                + "O001800350000100900800025000350000000000C000000145002pfgAHC00003002").getBytes(StandardCharsets.ISO_8859_1));
        SlottedParseStrategy parseStrategy = new SlottedParseStrategy();
        NitfReader reader = new NitfInputStreamReader(new BufferedInputStream(inputStream));
        reader.setFileType(FileType.NITF_TWO_ZERO);
        SymbolSegmentParser parser = new SymbolSegmentParser();

        exception.expect(NitfFormatException.class);
        exception.expectMessage("Symbol LUT only permitted for Symbol Type B with Symbol Colour G or C.");
        parser.parse(reader, parseStrategy, 0);
    }

    @Test
    public void CheckBadSymbolColourLUT() throws NitfFormatException {
        InputStream inputStream = new ByteArrayInputStream(("SY0000000021abcdefghijklmnopqrstU                                                                                                                                                                999998This symbol will never need downgrading.0"
                + "B001800350000100900800025000350000000000K000000145002pfgAHC00003002").getBytes(StandardCharsets.ISO_8859_1));
        SlottedParseStrategy parseStrategy = new SlottedParseStrategy();
        NitfReader reader = new NitfInputStreamReader(new BufferedInputStream(inputStream));
        reader.setFileType(FileType.NITF_TWO_ZERO);
        SymbolSegmentParser parser = new SymbolSegmentParser();

        exception.expect(NitfFormatException.class);
        exception.expectMessage("Symbol LUT only permitted for Symbol Type B with Symbol Colour G or C.");
        parser.parse(reader, parseStrategy, 0);
    }
}
