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
        assertUnclasAndEmpty(reader.getFileSecurityMetadata());
        assertEquals("00001", reader.getFileSecurityMetadata().getFileCopyNumber());
        assertEquals("00001", reader.getFileSecurityMetadata().getFileNumberOfCopies());
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
        assertUnclasAndEmpty(segment1.getSecurityMetadata());
        assertEquals("Unknown", segment1.getImageSource());
        assertEquals(18L, segment1.getNumRows());
        assertEquals(35L, segment1.getNumColumns());
        assertEquals(PixelValueType.BILEVEL, segment1.getPixelValueType());
        assertEquals(ImageRepresentation.RGBLUT, segment1.getImageRepresentation());
        assertEquals(ImageCategory.VISUAL, segment1.getImageCategory());
        assertEquals(1, segment1.getActualBitsPerPixelPerBand());
        assertEquals(PixelJustification.RIGHT, segment1.getPixelJustification());
        assertEquals(ImageCoordinatesRepresentation.NONE, segment1.getImageCoordinatesRepresentation());
        assertEquals(0, segment1.getNumberOfImageComments());
        assertEquals(ImageCompression.NOTCOMPRESSED, segment1.getImageCompression());
        assertEquals(1, segment1.getNumBands());

        // Checks for ImageBand
        NitfImageBand band1 = segment1.getImageBand(1);
        assertNotNull(band1);
        assertEquals("LU", band1.getImageRepresentation());
        assertEquals("", band1.getSubCategory());
        assertEquals(3, band1.getNumLUTs());
        assertEquals(2, band1.getNumLUTEntries());
        // Checks for lookup tables
        NitfImageBandLUT lut1 = band1.getLUT(1);
        assertNotNull(lut1);
        // TODO: check values
        NitfImageBandLUT lut2 = band1.getLUT(2);
        assertNotNull(lut2);
        // TODO check values
        NitfImageBandLUT lut3 = band1.getLUT(3);
        assertNotNull(lut3);
        // TODO check values

        assertEquals(ImageMode.BLOCKINTERLEVE, segment1.getImageMode());

        is.close();
    }

    void assertUnclasAndEmpty(NitfSecurityMetadata securityMetadata) {
        assertEquals(NitfSecurityClassification.UNCLASSIFIED, securityMetadata.getSecurityClassification());
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