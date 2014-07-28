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

import java.io.InputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.EnumSet;

import org.junit.Test;

public class Nitf21ImageParsingTest {

    @Test
    public void testExtractionWithOptionTurnedOn() throws IOException, ParseException {
        NitfFile file = new NitfFile();
        file.parse(getInputStream(), EnumSet.of(ParseOption.ExtractImageSegmentData));
        assertEquals(1, file.getNumberOfImageSegments());

        NitfImageSegment imageSegment = file.getImageSegment(1);
        assertImageSegmentMetadataIsAsExpected(imageSegment);
        assertEquals(1048576, imageSegment.getImageData().length);
    }

    @Test
    public void testExtractionWithOptionTurnedOff() throws IOException, ParseException {
        NitfFile file = new NitfFile();
        file.parse(getInputStream(), EnumSet.noneOf(ParseOption.class));
        assertEquals(1, file.getNumberOfImageSegments());

        NitfImageSegment imageSegment = file.getImageSegment(1);
        assertImageSegmentMetadataIsAsExpected(imageSegment);
        assertNull(imageSegment.getImageData());
    }

    private void assertImageSegmentMetadataIsAsExpected(NitfImageSegment imageSegment) {
        assertNotNull(imageSegment);
        assertEquals("Missing ID", imageSegment.getImageIdentifier1());
        assertEquals("1996-12-17 10:26:30", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(imageSegment.getImageDateTime()));
        assertEquals("", imageSegment.getImageTargetId());
        assertEquals("- BASE IMAGE -", imageSegment.getImageIdentifier2());
        assertEquals("Unknown", imageSegment.getImageSource());
        assertEquals(1024L, imageSegment.getNumberOfRows());
        assertEquals(1024L, imageSegment.getNumberOfColumns());
        assertEquals(PixelValueType.INTEGER, imageSegment.getPixelValueType());
        assertEquals(ImageRepresentation.MONOCHROME, imageSegment.getImageRepresentation());
        assertEquals(ImageCategory.VISUAL, imageSegment.getImageCategory());
        assertEquals(8, imageSegment.getActualBitsPerPixelPerBand());
        assertEquals(PixelJustification.RIGHT, imageSegment.getPixelJustification());
        assertEquals(ImageCoordinatesRepresentation.GEOGRAPHIC, imageSegment.getImageCoordinatesRepresentation());
        ImageCoordinates imageCoords = imageSegment.getImageCoordinates();
        assertNotNull(imageCoords);
        assertEquals(32.98333333333, imageCoords.getCoordinate00().getLatitude(), 0.000001);
        assertEquals(85.00000000000, imageCoords.getCoordinate00().getLongitude(), 0.000001);
        assertEquals(32.98333333333, imageCoords.getCoordinate0MaxCol().getLatitude(), 0.000001);
        assertEquals(85.00027777777, imageCoords.getCoordinate0MaxCol().getLongitude(), 0.000001);
        assertEquals(32.98305555555, imageCoords.getCoordinateMaxRowMaxCol().getLatitude(), 0.000001);
        assertEquals(85.00027777777, imageCoords.getCoordinateMaxRowMaxCol().getLongitude(), 0.000001);
        assertEquals(32.98305555555, imageCoords.getCoordinateMaxRow0().getLatitude(), 0.000001);
        assertEquals(85.00000000000, imageCoords.getCoordinateMaxRow0().getLongitude(), 0.000001);
        assertEquals(0, imageSegment.getNumberOfImageComments());
        assertEquals(ImageCompression.NOTCOMPRESSED, imageSegment.getImageCompression());
        assertEquals(1, imageSegment.getNumBands());
    }

    private InputStream getInputStream() {
        final String testfile = "/i_3001a.ntf";

        assertNotNull("Test file missing", getClass().getResource(testfile));
        return getClass().getResourceAsStream(testfile);
    }

}