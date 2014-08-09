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

public class Nitf20OverflowTest {
    @Test
    public void testU1130F() throws IOException, ParseException {
        final String nitf20File = "/U_1130F.NTF";

        assertNotNull("Test file missing", getClass().getResource(nitf20File));

        InputStream is = getClass().getResourceAsStream(nitf20File);
        NitfFile file = new NitfFile();
        file.parse(is, EnumSet.allOf(ParseOption.class));
        assertEquals(FileType.NITF_TWO_ZERO, file.getFileType());
        assertEquals(1, file.getComplexityLevel());
        assertEquals("", file.getStandardType());
        assertEquals("ALLOVERFLO", file.getOriginatingStationId());
        assertEquals("1997-09-15 09:00:00", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(file.getFileDateTime()));
        assertEquals("Checks overflow from all possible areas. Created by George Levy.", file.getFileTitle());
        NitfFileSecurityMetadata securityMetadata = file.getFileSecurityMetadata();
        assertUnclasAndEmpty(securityMetadata);
        assertEquals("      ", securityMetadata.getDowngradeDateOrSpecialCase());
        assertNull(securityMetadata.getDowngradeEvent());

        assertEquals("00000", file.getFileSecurityMetadata().getFileCopyNumber());
        assertEquals("00000", file.getFileSecurityMetadata().getFileNumberOfCopies());
        assertEquals("JITC FT HUACHUCA", file.getOriginatorsName());
        assertEquals("(520) 538-5494", file.getOriginatorsPhoneNumber());
        assertEquals(1, file.getNumberOfImageSegments());
        assertEquals(0, file.getNumberOfGraphicSegments());
        assertEquals(1, file.getNumberOfSymbolSegments());
        assertEquals(1, file.getNumberOfLabelSegments());
        assertEquals(1, file.getNumberOfTextSegments());
        assertEquals(7, file.getNumberOfDataExtensionSegments());
        assertEquals(1, file.getUserDefinedHeaderOverflow());
        assertEquals(2, file.getExtendedHeaderDataOverflow());

        NitfImageSegment imageSegment1 = file.getImageSegment(1);
        assertNotNull(imageSegment1);
        assertEquals("512 Lenna", imageSegment1.getImageIdentifier1());
        assertEquals("1993-03-25 15:25:59",  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(imageSegment1.getImageDateTime()));
        assertEquals("- BASE IMAGE -", imageSegment1.getImageIdentifier2());
        assertEquals("", imageSegment1.getImageTargetId());
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
        assertEquals(0, imageSegment1.getNumberOfImageComments());
        assertEquals(ImageCompression.NOTCOMPRESSED, imageSegment1.getImageCompression());
        assertEquals(1, imageSegment1.getNumBands());
        NitfImageBand band1 = imageSegment1.getImageBand(1);
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
        assertEquals(0, imageSegment1.getImageAttachmentLevel());
        assertEquals(0, imageSegment1.getImageLocationRow());
        assertEquals(0, imageSegment1.getImageLocationColumn());
        assertEquals("1.0 ", imageSegment1.getImageMagnification());
        assertEquals(1.0, imageSegment1.getImageMagnificationAsDouble(), 0.00000001);
        assertEquals(4, imageSegment1.getExtendedHeaderDataOverflow());

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