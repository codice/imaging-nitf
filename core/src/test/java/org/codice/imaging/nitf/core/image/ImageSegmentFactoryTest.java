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
package org.codice.imaging.nitf.core.image;

import org.codice.imaging.nitf.core.TestUtils;
import org.codice.imaging.nitf.core.common.DateTime;
import org.codice.imaging.nitf.core.common.FileType;
import org.codice.imaging.nitf.core.common.NitfFormatException;
import org.codice.imaging.nitf.core.security.SecurityClassification;
import org.codice.imaging.nitf.core.security.SecurityMetadata;
import org.codice.imaging.nitf.core.security.SecurityMetadataFactory;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;

/**
 * Tests for ImageSegmentFactory
 */
public class ImageSegmentFactoryTest {

    public ImageSegmentFactoryTest() {
    }

    @Test
    public void checkDefaultBuild() throws NitfFormatException {
        ImageSegment segment = ImageSegmentFactory.getDefault(FileType.NITF_TWO_ONE);
        assertNotNull(segment);
        assertEquals(FileType.NITF_TWO_ONE, segment.getFileType());
        assertEquals(0, segment.getAttachmentLevel());
        assertEquals("", segment.getIdentifier());

        assertNotNull(segment.getImageTargetId());
        assertEquals("", segment.getImageTargetId().textValue().trim());

        assertEquals("", segment.getImageIdentifier2());

        assertEquals(ImageCoordinatesRepresentation.NONE, segment.getImageCoordinatesRepresentation());

        assertEquals(ImageCompression.NOTCOMPRESSED, segment.getImageCompression());

        TestUtils.checkDateTimeIsRecent(segment.getImageDateTime().getZonedDateTime());
        assertEquals(SecurityClassification.UNCLASSIFIED, segment.getSecurityMetadata().getSecurityClassification());
    }

    @Test
    public void checkAdditionalDataBuild() throws NitfFormatException {
        ImageSegment segment = ImageSegmentFactory.getDefault(FileType.NITF_TWO_ONE);
        assertNotNull(segment);
        assertEquals(FileType.NITF_TWO_ONE, segment.getFileType());

        segment.setAttachmentLevel(2);
        assertEquals(2, segment.getAttachmentLevel());

        DateTime dt = new DateTime();
        dt.set(2016, 4, 10, 3, 39, 4);
        segment.setImageDateTime(dt);
        assertEquals(dt.getZonedDateTime(), segment.getImageDateTime().getZonedDateTime());

        TargetId tgtid = new TargetId("ABCDEFGHIJUVWXYAU");
        segment.setImageTargetId(tgtid);
        assertEquals(tgtid.textValue(), segment.getImageTargetId().textValue());

        segment.setImageIdentifier2("Secondary Identifier");
        assertEquals("Secondary Identifier", segment.getImageIdentifier2());

        segment.setImageSource("TESTING SOURCE");
        assertEquals("TESTING SOURCE", segment.getImageSource());

        segment.setNumberOfRows(340);
        assertEquals(340L, segment.getNumberOfRows());

        segment.setNumberOfColumns(401);
        assertEquals(401L, segment.getNumberOfColumns());

        SecurityMetadata securityMetadata = SecurityMetadataFactory.getDefaultFileSecurityMetadata(segment.getFileType());
        segment.setSecurityMetadata(securityMetadata);
        assertEquals(SecurityClassification.UNCLASSIFIED, segment.getSecurityMetadata().getSecurityClassification());

        segment.setPixelValueType(PixelValueType.INTEGER);
        assertEquals(PixelValueType.INTEGER, segment.getPixelValueType());

        segment.setImageRepresentation(ImageRepresentation.MULTIBAND);
        assertEquals(ImageRepresentation.MULTIBAND, segment.getImageRepresentation());

        segment.setImageCategory(ImageCategory.ELEVATIONMODEL);
        assertEquals(ImageCategory.ELEVATIONMODEL, segment.getImageCategory());

        segment.setActualBitsPerPixelPerBand(4);
        assertEquals(4, segment.getActualBitsPerPixelPerBand());

        segment.setPixelJustification(PixelJustification.LEFT);
        assertEquals(PixelJustification.LEFT, segment.getPixelJustification());

        segment.setImageCoordinatesRepresentation(ImageCoordinatesRepresentation.MGRS);
        assertEquals(ImageCoordinatesRepresentation.MGRS, segment.getImageCoordinatesRepresentation());

        ImageCoordinatePair coord00 = new ImageCoordinatePair();
        ImageCoordinatePair coord0MaxCol = new ImageCoordinatePair();
        ImageCoordinatePair coordMaxRowMaxCol = new ImageCoordinatePair();
        ImageCoordinatePair coordMaxRow0 = new ImageCoordinatePair();
        ImageCoordinates testCoordinates = new ImageCoordinates(new ImageCoordinatePair[]{coord00, coord0MaxCol, coordMaxRowMaxCol, coordMaxRow0});
        segment.setImageCoordinates(testCoordinates);
        assertEquals(coord00, segment.getImageCoordinates().getCoordinate00());
        assertEquals(coord0MaxCol, segment.getImageCoordinates().getCoordinate0MaxCol());
        assertEquals(coordMaxRowMaxCol, segment.getImageCoordinates().getCoordinateMaxRowMaxCol());
        assertEquals(coordMaxRow0, segment.getImageCoordinates().getCoordinateMaxRow0());

        segment.getImageComments().clear();
        segment.addImageComment("First comment");
        segment.addImageComment("Another comment. This is a little longer.");
        assertEquals(2, segment.getImageComments().size());
        assertEquals("First comment", segment.getImageComments().get(0));
        assertEquals("Another comment. This is a little longer.", segment.getImageComments().get(1));

        segment.setImageCompression(ImageCompression.JPEG2000MASK);
        assertEquals(ImageCompression.JPEG2000MASK, segment.getImageCompression());

        segment.setCompressionRate("2.54");
        assertEquals("2.54", segment.getCompressionRate());

        ImageBand imageBand1 = new ImageBand();
        segment.addImageBand(imageBand1);
        ImageBand imageBand2 = new ImageBand();
        segment.addImageBand(imageBand2);
        assertEquals(2, segment.getNumBands());
        assertEquals(imageBand1, segment.getImageBandZeroBase(0));

        segment.setImageMode(ImageMode.PIXELINTERLEVE);
        assertEquals(ImageMode.PIXELINTERLEVE, segment.getImageMode());

        segment.setNumberOfBlocksPerRow(4);
        assertEquals(4, segment.getNumberOfBlocksPerRow());
        segment.setNumberOfBlocksPerColumn(1);
        assertEquals(1, segment.getNumberOfBlocksPerColumn());

        segment.setNumberOfPixelsPerBlockHorizontal(75);
        assertEquals(75, segment.getNumberOfPixelsPerBlockHorizontal());
        segment.setNumberOfPixelsPerBlockVertical(340);
        assertEquals(340, segment.getNumberOfPixelsPerBlockVertical());

        segment.setNumberOfBitsPerPixelPerBand(11);
        assertEquals(11, segment.getNumberOfBitsPerPixelPerBand());

        segment.setImageDisplayLevel(3);
        assertEquals(3, segment.getImageDisplayLevel());

        segment.setImageLocationColumn(500);
        segment.setImageLocationRow(400);
        assertEquals(500, segment.getImageLocationColumn());
        assertEquals(400, segment.getImageLocationRow());

        segment.setImageMagnification("1.0");
        assertEquals(1.0, segment.getImageMagnificationAsDouble(), 0.0000001);
        assertEquals("1.0", segment.getImageMagnification());

        segment.setImageMagnification("/4");
        assertEquals(0.25, segment.getImageMagnificationAsDouble(), 0.0000001);
        assertEquals("/4", segment.getImageMagnification());
    }
}
