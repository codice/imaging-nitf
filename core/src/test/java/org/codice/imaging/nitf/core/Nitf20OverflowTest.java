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
import java.text.ParseException;
import java.time.format.DateTimeFormatter;
import org.codice.imaging.nitf.core.common.FileType;
import org.codice.imaging.nitf.core.common.NitfInputStreamReader;
import org.codice.imaging.nitf.core.common.NitfReader;
import org.codice.imaging.nitf.core.dataextension.DataExtensionSegment;
import org.codice.imaging.nitf.core.image.ImageCategory;
import org.codice.imaging.nitf.core.image.ImageCompression;
import org.codice.imaging.nitf.core.image.ImageCoordinatesRepresentation;
import org.codice.imaging.nitf.core.image.ImageMode;
import org.codice.imaging.nitf.core.image.ImageRepresentation;
import org.codice.imaging.nitf.core.image.ImageBand;
import org.codice.imaging.nitf.core.image.ImageSegment;
import org.codice.imaging.nitf.core.image.PixelJustification;
import org.codice.imaging.nitf.core.image.PixelValueType;
import org.codice.imaging.nitf.core.label.LabelSegment;
import org.codice.imaging.nitf.core.security.FileSecurityMetadata;
import org.codice.imaging.nitf.core.security.SecurityClassification;
import org.codice.imaging.nitf.core.security.SecurityMetadata;
import org.codice.imaging.nitf.core.symbol.SymbolColour;
import org.codice.imaging.nitf.core.symbol.SymbolSegment;
import org.codice.imaging.nitf.core.symbol.SymbolType;
import org.codice.imaging.nitf.core.text.TextFormat;
import org.codice.imaging.nitf.core.text.TextSegment;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import org.junit.Before;
import org.junit.Test;

public class Nitf20OverflowTest {

    private DateTimeFormatter formatter = null;

    @Before
    public void beforeTest() {
        formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    }

    @Test
    public void testU1130F() throws IOException, ParseException {
        final String nitf20File = "/JitcNitf20Samples/U_1130F.NTF";

        assertNotNull("Test file missing", getClass().getResource(nitf20File));

        InputStream is = getClass().getResourceAsStream(nitf20File);
        AllDataExtractionParseStrategy parseStrategy = new AllDataExtractionParseStrategy();
        NitfReader reader = new NitfInputStreamReader(new BufferedInputStream(is));
        NitfFileParser.parse(reader, parseStrategy);
        NitfFileHeader nitfFileHeader = parseStrategy.getNitfHeader();
        assertEquals(FileType.NITF_TWO_ZERO, nitfFileHeader.getFileType());
        assertEquals(1, nitfFileHeader.getComplexityLevel());
        assertEquals("", nitfFileHeader.getStandardType());
        assertEquals("ALLOVERFLO", nitfFileHeader.getOriginatingStationId());
        assertEquals("1997-09-15 09:00:00", formatter.format(nitfFileHeader.getFileDateTime().getZonedDateTime()));
        assertEquals("Checks overflow from all possible areas. Created by George Levy.", nitfFileHeader.getFileTitle());
        FileSecurityMetadata securityMetadata = nitfFileHeader.getFileSecurityMetadata();
        assertUnclasAndEmpty(securityMetadata);
        assertEquals("      ", securityMetadata.getDowngradeDateOrSpecialCase());
        assertNull(securityMetadata.getDowngradeEvent());

        assertEquals("00000", nitfFileHeader.getFileSecurityMetadata().getFileCopyNumber());
        assertEquals("00000", nitfFileHeader.getFileSecurityMetadata().getFileNumberOfCopies());
        assertEquals("JITC FT HUACHUCA", nitfFileHeader.getOriginatorsName());
        assertEquals("(520) 538-5494", nitfFileHeader.getOriginatorsPhoneNumber());
        assertEquals(1, parseStrategy.getImageSegments().size());
        assertEquals(0, parseStrategy.getGraphicSegments().size());
        assertEquals(1, parseStrategy.getSymbolSegments().size());
        assertEquals(1, parseStrategy.getLabelSegments().size());
        assertEquals(1, parseStrategy.getTextSegments().size());
        assertEquals(7, parseStrategy.getDataExtensionSegments().size());
        assertEquals(1, nitfFileHeader.getUserDefinedHeaderOverflow());
        assertEquals(2, nitfFileHeader.getExtendedHeaderDataOverflow());

        ImageSegment imageSegment1 = parseStrategy.getImageSegments().get(0);
        assertNotNull(imageSegment1);
        assertEquals("512 Lenna", imageSegment1.getIdentifier());
        assertEquals("1993-03-25 15:25:59", formatter.format(imageSegment1.getImageDateTime().getZonedDateTime()));
        assertEquals("- BASE IMAGE -", imageSegment1.getImageIdentifier2());
        assertEquals("          ", imageSegment1.getImageTargetId().getBasicEncyclopediaNumber());
        assertEquals("     ", imageSegment1.getImageTargetId().getOSuffix());
        assertEquals("  ", imageSegment1.getImageTargetId().getCountryCode());
        assertUnclasAndEmpty(imageSegment1.getSecurityMetadata());
        assertEquals("Unknown", imageSegment1.getImageSource());
        assertEquals(512L, imageSegment1.getNumberOfRows());
        assertEquals(512L, imageSegment1.getNumberOfColumns());
        assertEquals(PixelValueType.INTEGER, imageSegment1.getPixelValueType());
        assertEquals(ImageRepresentation.MONOCHROME, imageSegment1.getImageRepresentation());
        assertEquals(ImageCategory.VISUAL, imageSegment1.getImageCategory());
        assertEquals(8, imageSegment1.getActualBitsPerPixelPerBand());
        assertEquals(PixelJustification.RIGHT, imageSegment1.getPixelJustification());
        assertEquals(ImageCoordinatesRepresentation.NONE, imageSegment1.getImageCoordinatesRepresentation());
        assertEquals(0, imageSegment1.getImageComments().size());
        assertEquals(ImageCompression.NOTCOMPRESSED, imageSegment1.getImageCompression());
        assertEquals(1, imageSegment1.getNumBands());
        ImageBand band1 = imageSegment1.getImageBand(1);
        assertNotNull(band1);
        assertEquals("", band1.getImageRepresentation());
        assertEquals("", band1.getSubCategory());
        assertEquals(0, band1.getNumLUTs());
        assertEquals(0, band1.getNumLUTEntries());
        assertEquals(ImageMode.BLOCKINTERLEVE, imageSegment1.getImageMode());
        assertEquals(1, imageSegment1.getNumberOfBlocksPerRow());
        assertEquals(1, imageSegment1.getNumberOfBlocksPerColumn());
        assertEquals(512, imageSegment1.getNumberOfPixelsPerBlockHorizontal());
        assertEquals(512, imageSegment1.getNumberOfPixelsPerBlockVertical());
        assertEquals(8, imageSegment1.getNumberOfBitsPerPixelPerBand());
        assertEquals(1, imageSegment1.getImageDisplayLevel());
        assertEquals(0, imageSegment1.getAttachmentLevel());
        assertEquals(0, imageSegment1.getImageLocationRow());
        assertEquals(0, imageSegment1.getImageLocationColumn());
        assertEquals("1.0 ", imageSegment1.getImageMagnification());
        assertEquals(1.0, imageSegment1.getImageMagnificationAsDouble(), 0.00000001);
        assertEquals(3, imageSegment1.getUserDefinedHeaderOverflow());
        assertEquals(4, imageSegment1.getExtendedHeaderDataOverflow());

        SymbolSegment symbolSegment1 = parseStrategy.getSymbolSegments().get(0);
        assertNotNull(symbolSegment1);
        assertEquals("Text", symbolSegment1.getIdentifier());
        assertEquals("", symbolSegment1.getSymbolName());
        assertUnclasAndEmpty(symbolSegment1.getSecurityMetadata());
        assertEquals("      ", symbolSegment1.getSecurityMetadata().getDowngradeDateOrSpecialCase());
        assertEquals(SymbolType.CGM, symbolSegment1.getSymbolType());
        assertEquals(SymbolColour.UNKNOWN, symbolSegment1.getSymbolColour());
        assertEquals(0, symbolSegment1.getNumberOfLinesPerSymbol());
        assertEquals(0, symbolSegment1.getNumberOfPixelsPerLine());
        assertEquals(0, symbolSegment1.getLineWidth());
        assertEquals(0, symbolSegment1.getNumberOfBitsPerPixel());
        assertEquals(2, symbolSegment1.getSymbolDisplayLevel());
        assertEquals(0, symbolSegment1.getAttachmentLevel());
        assertEquals(20, symbolSegment1.getSymbolLocationRow());
        assertEquals(20, symbolSegment1.getSymbolLocationColumn());
        assertEquals(0, symbolSegment1.getSymbolLocation2Row());
        assertEquals(0, symbolSegment1.getSymbolLocation2Column());
        assertEquals("000000", symbolSegment1.getSymbolNumber());
        assertEquals(0, symbolSegment1.getSymbolRotation());
        byte[] allData = new byte[211];
        int bytesRead = symbolSegment1.getData().read(allData);
        assertEquals(210, bytesRead);
        assertEquals(5, symbolSegment1.getExtendedHeaderDataOverflow());

        LabelSegment labelSegment1 = parseStrategy.getLabelSegments().get(0);
        assertNotNull(labelSegment1);
        assertEquals("label", labelSegment1.getIdentifier());
        assertNotNull(labelSegment1.getSecurityMetadata());
        assertEquals(SecurityClassification.UNCLASSIFIED, labelSegment1.getSecurityMetadata().getSecurityClassification());
        assertNull(labelSegment1.getSecurityMetadata().getSecurityClassificationSystem());
        assertEquals("", labelSegment1.getSecurityMetadata().getCodewords());
        assertEquals("Control and Handling", labelSegment1.getSecurityMetadata().getControlAndHandling());
        assertEquals("Releasing Instructions", labelSegment1.getSecurityMetadata().getReleaseInstructions());
        assertNull(labelSegment1.getSecurityMetadata().getDeclassificationType());
        assertNull(labelSegment1.getSecurityMetadata().getDeclassificationDate());
        assertNull(labelSegment1.getSecurityMetadata().getDeclassificationExemption());
        assertNull(labelSegment1.getSecurityMetadata().getDowngrade());
        assertNull(labelSegment1.getSecurityMetadata().getDowngradeDate());
        assertNull(labelSegment1.getSecurityMetadata().getClassificationText());
        assertNull(labelSegment1.getSecurityMetadata().getClassificationAuthorityType());
        assertEquals("Classification", labelSegment1.getSecurityMetadata().getClassificationAuthority());
        assertNull(labelSegment1.getSecurityMetadata().getClassificationReason());
        assertEquals("0", labelSegment1.getSecurityMetadata().getSecurityControlNumber());
        assertEquals("999999", labelSegment1.getSecurityMetadata().getDowngradeDateOrSpecialCase());
        assertEquals(40, labelSegment1.getLabelLocationRow());
        assertEquals(20, labelSegment1.getLabelLocationColumn());
        assertEquals(12, labelSegment1.getLabelCellWidth());
        assertEquals(25, labelSegment1.getLabelCellHeight());
        assertEquals(8, labelSegment1.getLabelDisplayLevel());
        assertEquals(0, labelSegment1.getAttachmentLevel());
        assertEquals("This is a label on Lenna in an \"OverflowTestFile\"!", labelSegment1.getData());
        assertEquals(6, labelSegment1.getExtendedHeaderDataOverflow());

        TextSegment textSegment = parseStrategy.getTextSegments().get(0);
        assertNotNull(textSegment);
        assertEquals("Text ID   ", textSegment.getIdentifier());
        assertEquals(0, textSegment.getAttachmentLevel());
        assertEquals("Text Title", textSegment.getTextTitle());
        assertEquals(TextFormat.BASICCHARACTERSET, textSegment.getTextFormat());
        assertEquals("Example of a SideArm text file.  Marc Smelser\r\nCreated this NITFText file 07/07/95", parseStrategy.getTextSegments().get(0).getData());
        assertEquals(7, textSegment.getExtendedHeaderDataOverflow());

        DataExtensionSegment des1 = parseStrategy.getDataExtensionSegments().get(0);
        assertNotNull(des1);
        assertEquals("Registered Extensions    ", des1.getIdentifier());
        assertEquals(99, des1.getDESVersion());
        assertEquals("UDHD", des1.getOverflowedHeaderType());
        assertEquals(0, des1.getItemOverflowed());

        DataExtensionSegment des2 = parseStrategy.getDataExtensionSegments().get(1);
        assertNotNull(des2);
        assertEquals("Controlled Extensions    ", des2.getIdentifier());
        assertEquals(99, des2.getDESVersion());
        assertEquals("UDHD", des1.getOverflowedHeaderType());
        assertEquals(0, des1.getItemOverflowed());

        DataExtensionSegment des3 = parseStrategy.getDataExtensionSegments().get(2);
        assertNotNull(des3);
        assertEquals("Registered Extensions    ", des3.getIdentifier());
        assertEquals(99, des3.getDESVersion());
        assertEquals("UDID", des3.getOverflowedHeaderType());
        assertEquals(1, des3.getItemOverflowed());

        DataExtensionSegment des4 = parseStrategy.getDataExtensionSegments().get(3);
        assertNotNull(des4);
        assertEquals("Controlled Extensions    ", des4.getIdentifier());
        assertEquals(99, des4.getDESVersion());
        assertEquals("IXSHD", des4.getOverflowedHeaderType());
        assertEquals(1, des4.getItemOverflowed());

        DataExtensionSegment des5 = parseStrategy.getDataExtensionSegments().get(4);
        assertNotNull(des5);
        assertEquals("Controlled Extensions    ", des5.getIdentifier());
        assertEquals(99, des5.getDESVersion());
        assertEquals("SXSHD", des5.getOverflowedHeaderType());
        assertEquals(1, des5.getItemOverflowed());

        DataExtensionSegment des6 = parseStrategy.getDataExtensionSegments().get(5);
        assertNotNull(des6);
        assertEquals("Controlled Extensions    ", des6.getIdentifier());
        assertEquals(99, des6.getDESVersion());
        assertEquals("LXSHD", des6.getOverflowedHeaderType());
        assertEquals(1, des6.getItemOverflowed());

        DataExtensionSegment des7 = parseStrategy.getDataExtensionSegments().get(6);
        assertNotNull(des7);
        assertEquals("Controlled Extensions    ", des7.getIdentifier());
        assertEquals(99, des7.getDESVersion());
        assertEquals("TXSHD", des7.getOverflowedHeaderType());
        assertEquals(1, des7.getItemOverflowed());

        is.close();
    }

    void assertUnclasAndEmpty(SecurityMetadata securityMetadata) {
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