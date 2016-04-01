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
package org.codice.imaging.nitf.core.image;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.format.DateTimeFormatter;
import org.codice.imaging.nitf.core.SlottedParseStrategy;
import org.codice.imaging.nitf.core.common.NitfFormatException;
import org.codice.imaging.nitf.core.common.NitfInputStreamReader;
import org.codice.imaging.nitf.core.common.NitfReader;
import org.codice.imaging.nitf.core.header.NitfParser;
import org.junit.Assert;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import org.junit.Before;
import org.junit.Test;

public class Nitf21ImageParsingTest {

    private DateTimeFormatter formatter = null;

    @Before
    public void beforeTest() {
        formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    }

    @Test
    public void testExtractionWithOptionTurnedOn() throws IOException, NitfFormatException {
        SlottedParseStrategy parseStrategy = new SlottedParseStrategy(SlottedParseStrategy.IMAGE_DATA);
        NitfReader reader = new NitfInputStreamReader(new BufferedInputStream(getInputStream()));
        NitfParser.parse(reader, parseStrategy);
        assertEquals(1, parseStrategy.getDataSource().getImageSegments().size());

        ImageSegment imageSegment = parseStrategy.getDataSource().getImageSegments().get(0);
        assertImageSegmentMetadataIsAsExpected(imageSegment);
        byte[] allData = new byte[1048577];
        int bytesRead = imageSegment.getData().read(allData);
        assertEquals(1048576, bytesRead);
    }

    @Test
    public void testExtractionWithOptionTurnedOff() throws IOException, NitfFormatException {
        SlottedParseStrategy parseStrategy = new SlottedParseStrategy(SlottedParseStrategy.HEADERS_ONLY);
        NitfReader reader = new NitfInputStreamReader(new BufferedInputStream(getInputStream()));
        NitfParser.parse(reader, parseStrategy);
        assertEquals(1, parseStrategy.getDataSource().getImageSegments().size());

        ImageSegment imageSegment = parseStrategy.getDataSource().getImageSegments().get(0);
        assertImageSegmentMetadataIsAsExpected(imageSegment);
        assertNull(imageSegment.getData());
    }

    private void assertImageSegmentMetadataIsAsExpected(ImageSegment imageSegment) {
        assertNotNull(imageSegment);
        assertEquals("Missing ID", imageSegment.getIdentifier());
        assertEquals("1996-12-17 10:26:30", formatter.format(imageSegment.getImageDateTime().getZonedDateTime()));
        assertEquals("          ", imageSegment.getImageTargetId().getBasicEncyclopediaNumber());
        assertEquals("     ", imageSegment.getImageTargetId().getOSuffix());
        assertEquals("  ", imageSegment.getImageTargetId().getCountryCode());
        assertEquals("- BASE IMAGE -", imageSegment.getImageIdentifier2());
        assertEquals("Unknown", imageSegment.getImageSource());
        assertEquals(1024L, imageSegment.getNumberOfRows());
        assertEquals(1024L, imageSegment.getNumberOfColumns());
        Assert.assertEquals(PixelValueType.INTEGER, imageSegment.getPixelValueType());
        assertEquals(ImageRepresentation.MONOCHROME, imageSegment.getImageRepresentation());
        assertEquals(ImageCategory.VISUAL, imageSegment.getImageCategory());
        assertEquals(8, imageSegment.getActualBitsPerPixelPerBand());
        Assert.assertEquals(PixelJustification.RIGHT, imageSegment.getPixelJustification());
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
        ImageBand band1 = imageSegment.getImageBand(1);
        assertNotNull(band1);
        assertEquals("M", band1.getImageRepresentation());
        assertEquals("", band1.getSubCategory());
        assertEquals(0, band1.getNumLUTs());
    }

    private InputStream getInputStream() {
        final String testfile = "/JitcNitf21Samples/i_3001a.ntf";

        assertNotNull("Test file missing", getClass().getResource(testfile));
        return getClass().getResourceAsStream(testfile);
    }

}