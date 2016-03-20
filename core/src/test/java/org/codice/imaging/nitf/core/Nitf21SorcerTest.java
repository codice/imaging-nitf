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
import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.ImageInputStream;
import org.codice.imaging.nitf.core.common.NitfInputStreamReader;
import org.codice.imaging.nitf.core.common.NitfReader;
import org.codice.imaging.nitf.core.graphic.GraphicColour;
import org.codice.imaging.nitf.core.graphic.GraphicSegment;
import org.codice.imaging.nitf.core.image.ImageBand;
import org.codice.imaging.nitf.core.image.ImageCategory;
import org.codice.imaging.nitf.core.image.ImageCompression;
import org.codice.imaging.nitf.core.image.ImageCoordinatesRepresentation;
import org.codice.imaging.nitf.core.image.ImageMode;
import org.codice.imaging.nitf.core.image.ImageRepresentation;
import org.codice.imaging.nitf.core.image.ImageSegment;
import org.codice.imaging.nitf.core.image.PixelJustification;
import org.codice.imaging.nitf.core.image.PixelValueType;
import org.codice.imaging.nitf.core.security.SecurityClassification;
import org.codice.imaging.nitf.core.security.SecurityMetadata;
import org.codice.imaging.nitf.core.text.TextFormat;
import org.codice.imaging.nitf.core.text.TextSegment;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.Test;

public class Nitf21SorcerTest {

    private DateTimeFormatter formatter = null;

    @Before
    public void beforeTest() {
        formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    }

    @Test
    public void testSorcerParse() throws IOException, ParseException {
        AllDataExtractionParseStrategy parseStrategy = new AllDataExtractionParseStrategy();
        HeapStrategy<ImageInputStream> imageDataStrategy = new FileBackedHeapStrategy<>(file -> new FileImageInputStream(file));
        parseStrategy.setImageHeapStrategy(imageDataStrategy);
        NitfReader reader = new NitfInputStreamReader(new BufferedInputStream(getInputStream()));
        NitfFileParser.parse(reader, parseStrategy);
        NitfDataSource parseResult = parseStrategy.getNitfDataSource();
        assertEquals(1, parseResult.getImageSegments().size());
        assertEquals(1, parseResult.getGraphicSegments().size());
        assertEquals(0, parseResult.getSymbolSegments().size());
        assertEquals(0, parseResult.getLabelSegments().size());
        assertEquals(1, parseResult.getTextSegments().size());
        assertEquals(0, parseResult.getDataExtensionSegments().size());

        // Checks for ImageSegment.
        ImageSegment imageSegment = parseResult.getImageSegments().get(0);
        assertNotNull(imageSegment);
        assertEquals("Image Id1", imageSegment.getIdentifier());
        assertEquals("2002-10-06 22:03:20", formatter.format(imageSegment.getImageDateTime().getZonedDateTime()));
        assertEquals("1234-56789", imageSegment.getImageTargetId().getBasicEncyclopediaNumber());
        assertEquals("A1234", imageSegment.getImageTargetId().getOSuffix());
        assertEquals("AU", imageSegment.getImageTargetId().getCountryCode());
        assertEquals("Another title", imageSegment.getImageIdentifier2());
        assertUnclasAndEmpty(imageSegment.getSecurityMetadata());
        assertEquals("", imageSegment.getImageSource());
        assertEquals(512L, imageSegment.getNumberOfRows());
        assertEquals(683L, imageSegment.getNumberOfColumns());
        assertEquals(PixelValueType.INTEGER, imageSegment.getPixelValueType());
        assertEquals(ImageRepresentation.ITUBT6015, imageSegment.getImageRepresentation());
        assertEquals(ImageCategory.VISUAL, imageSegment.getImageCategory());
        assertEquals(8, imageSegment.getActualBitsPerPixelPerBand());
        assertEquals(PixelJustification.RIGHT, imageSegment.getPixelJustification());
        assertEquals(ImageCoordinatesRepresentation.NONE, imageSegment.getImageCoordinatesRepresentation());
        assertEquals(0, imageSegment.getImageComments().size());
        assertEquals(ImageCompression.JPEG, imageSegment.getImageCompression());
        assertEquals(3, imageSegment.getNumBands());

        // Checks for ImageBand
        ImageBand band1 = imageSegment.getImageBand(1);
        assertNotNull(band1);
        assertEquals("Y", band1.getImageRepresentation());
        assertEquals("", band1.getSubCategory());
        assertEquals(0, band1.getNumLUTs());
        assertEquals(0, band1.getNumLUTEntries());
        ImageBand band2 = imageSegment.getImageBand(2);
        assertNotNull(band2);
        assertEquals("Cb", band2.getImageRepresentation());
        assertEquals("", band2.getSubCategory());
        assertEquals(0, band2.getNumLUTs());
        assertEquals(0, band2.getNumLUTEntries());
        ImageBand band3 = imageSegment.getImageBand(3);
        assertNotNull(band3);
        assertEquals("Cr", band3.getImageRepresentation());
        assertEquals("", band3.getSubCategory());
        assertEquals(0, band3.getNumLUTs());
        assertEquals(0, band3.getNumLUTEntries());

        assertEquals(ImageMode.PIXELINTERLEVE, imageSegment.getImageMode());
        assertEquals(1, imageSegment.getNumberOfBlocksPerRow());
        assertEquals(1, imageSegment.getNumberOfBlocksPerColumn());
        assertEquals(683, imageSegment.getNumberOfPixelsPerBlockHorizontal());
        assertEquals(512, imageSegment.getNumberOfPixelsPerBlockVertical());
        assertEquals(8, imageSegment.getNumberOfBitsPerPixelPerBand());
        assertEquals(1, imageSegment.getImageDisplayLevel());
        assertEquals(0, imageSegment.getAttachmentLevel());
        assertEquals(0, imageSegment.getImageLocationRow());
        assertEquals(0, imageSegment.getImageLocationColumn());
        assertEquals("1.0 ", imageSegment.getImageMagnification());

        GraphicSegment graphicSegment = parseResult.getGraphicSegments().get(0);
        assertNotNull(graphicSegment);
        assertEquals("", graphicSegment.getIdentifier());
        assertEquals("", graphicSegment.getGraphicName());
        assertUnclasAndEmpty(graphicSegment.getSecurityMetadata());
        assertEquals(2, graphicSegment.getGraphicDisplayLevel());
        assertEquals(0, graphicSegment.getAttachmentLevel());
        assertEquals(12, graphicSegment.getGraphicLocationRow());
        assertEquals(620, graphicSegment.getGraphicLocationColumn());
        assertEquals(0, graphicSegment.getBoundingBox1Row());
        assertEquals(0, graphicSegment.getBoundingBox1Column());
        assertEquals(GraphicColour.UNKNOWN, graphicSegment.getGraphicColour());
        assertEquals(0, graphicSegment.getBoundingBox2Row());
        assertEquals(0, graphicSegment.getBoundingBox2Column());

        TextSegment textSegment = parseResult.getTextSegments().get(0);
        assertUnclasAndEmpty(textSegment.getSecurityMetadata());
        assertNotNull(textSegment);
        assertEquals("       ", textSegment.getIdentifier());
        assertEquals(0, textSegment.getAttachmentLevel());
        assertEquals("2002-10-06 22:03:20", formatter.format(textSegment.getTextDateTime().getZonedDateTime()));
        assertEquals("", textSegment.getTextTitle());
        assertEquals(TextFormat.BASICCHARACTERSET, textSegment.getTextFormat());
        assertEquals("****************LOCATION INFORMATION (GPS/USER INPUTS)*****************\r\nInput Parameters:\r\nLatitude: \r\nLongitude: \r\nRange: 0 m (0 ft)\r\nBearing: 0 deg (0 mils)\r\nInclination: 0 deg (0 mils)\r\n\r\nOutput Coordinates:\r\nN/A\r\n\r\n***************************PHOTO INFORMATION***********************************\r\n\r\nCamera Make: Canon\r\nCamera Model: Canon PowerShot S30\r\nDate/Time Original: 2002:10:07 08:03:20\r\nDate/Time Digitized: 2002:10:07 08:03:20\r\nExif Version: 0210\r\nComponent Configuration: YCbCr\r\nPixel Width x Height: 683 x 512\r\nX Resolution (dpi): 180.0\r\nY Resolution (dpi): 180.0\r\nOrientation: Top Left\r\nYCbCr Positioning: Centered\r\nColor Space:  RGB\r\nFlash Used: No\r\nFocal Length:  7.1mm\r\nExposure Time:  0.002 s  (1/640)\r\nF-Number: 2.8\r\nFocus Distance: 3.11m\r\nExposure Bias: 0.00\r\nLight Source: Unknown\r\nWhite Balance:  Auto White Balance\r\nMetering Mode: Matrix\r\nShutter Speed: 0.00157 s\r\nAperture Value: 2.96875\r\nEncoding: Baseline\r\nSensing Method:  One-chip color area sensor\r\n\r\n", textSegment.getData());
    }

    private InputStream getInputStream() {
        final String testfile = "/WithBE.ntf";

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
