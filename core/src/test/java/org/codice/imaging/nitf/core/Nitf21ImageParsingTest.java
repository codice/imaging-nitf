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

import java.io.InputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.EnumSet;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.Test;

public class Nitf21ImageParsingTest {

    private SimpleDateFormat formatter = null;

    @Before
    public void beforeTest() {
        formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    @Test
    public void testExtractionWithOptionTurnedOn() throws IOException, ParseException {
        ImageDataExtractionParseStrategy parseStrategy = new ImageDataExtractionParseStrategy();
        NitfFileFactory.parse(getInputStream(), parseStrategy);
        assertEquals(1, parseStrategy.getImageSegmentHeaders().size());

        NitfImageSegmentHeader imageSegment = parseStrategy.getImageSegmentHeaders().get(0);
        assertImageSegmentMetadataIsAsExpected(imageSegment);
        assertEquals(1048576, parseStrategy.getImageSegmentData().get(0).length);
    }

    @Test
    public void testExtractionWithOptionTurnedOff() throws IOException, ParseException {
        NitfParseStrategy parseStrategy = new HeaderOnlyNitfParseStrategy();
        NitfFileFactory.parse(getInputStream(), parseStrategy);
        assertEquals(1, parseStrategy.getImageSegmentHeaders().size());

        NitfImageSegmentHeader imageSegment = parseStrategy.getImageSegmentHeaders().get(0);
        assertImageSegmentMetadataIsAsExpected(imageSegment);
    }

    private void assertImageSegmentMetadataIsAsExpected(NitfImageSegmentHeader imageSegment) {
        assertNotNull(imageSegment);
        assertEquals("Missing ID", imageSegment.getIdentifier());
        assertEquals("1996-12-17 10:26:30", formatter.format(imageSegment.getImageDateTime().toDate()));
        assertEquals("          ", imageSegment.getImageTargetId().getBasicEncyclopediaNumber());
        assertEquals("     ", imageSegment.getImageTargetId().getOSuffix());
        assertEquals("  ", imageSegment.getImageTargetId().getCountryCode());
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
        assertEquals(0, imageSegment.getImageComments().size());
        assertEquals(ImageCompression.NOTCOMPRESSED, imageSegment.getImageCompression());
        assertEquals(1, imageSegment.getNumBands());
        NitfImageBand band1 = imageSegment.getImageBand(1);
        assertNotNull(band1);
        assertEquals("M", band1.getImageRepresentation());
        assertEquals("", band1.getSubCategory());
        assertEquals(0, band1.getNumLUTs());
    }

    private InputStream getInputStream() {
        final String testfile = "/i_3001a.ntf";

        assertNotNull("Test file missing", getClass().getResource(testfile));
        return getClass().getResourceAsStream(testfile);
    }

}