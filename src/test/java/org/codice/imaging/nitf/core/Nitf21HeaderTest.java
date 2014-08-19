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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.InputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.junit.rules.ExpectedException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class Nitf21HeaderTest {

    private SimpleDateFormat formatter = null;

    @Before
    public void beforeTest() {
        formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    @Test
    public void testCompliantHeaderReadInputStream() throws IOException, ParseException {
        final String simpleNitf21File = "/i_3034c.ntf";
        assertNotNull("Test file missing", getClass().getResource(simpleNitf21File));

        InputStream is = getClass().getResourceAsStream(simpleNitf21File);
        NitfFile file = NitfFileFactory.parseHeadersOnly(is);
        checkCompliantHeaderResults(file);
        is.close();
    }

    @Test
    public void testCompliantHeaderReadFile() throws IOException, ParseException {
        final String simpleNitf21File = "/i_3034c.ntf";
        assertNotNull("Test file missing", getClass().getResource(simpleNitf21File));

        File resourceFile = new File(getClass().getResource(simpleNitf21File).getFile());
        NitfFile file = NitfFileFactory.parseHeadersOnly(resourceFile);
        checkCompliantHeaderResults(file);
    }

    private void checkCompliantHeaderResults(NitfFile file) {
        assertEquals(FileType.NITF_TWO_ONE, file.getFileType());
        assertEquals(3, file.getComplexityLevel());
        assertEquals("BF01", file.getStandardType());
        assertEquals("I_3034C", file.getOriginatingStationId());
        assertEquals("1997-12-18 12:15:39", formatter.format(file.getFileDateTime()));
        assertEquals("Check an RGB/LUT 1 bit image maps black to red and white to green.", file.getFileTitle());
        assertUnclasAndEmpty(file.getFileSecurityMetadata());
        assertEquals("00001", file.getFileSecurityMetadata().getFileCopyNumber());
        assertEquals("00001", file.getFileSecurityMetadata().getFileNumberOfCopies());
        assertEquals(0x20, file.getFileBackgroundColour().getRed());
        assertEquals(0x20, file.getFileBackgroundColour().getGreen());
        assertEquals(0x20, file.getFileBackgroundColour().getBlue());
        assertEquals("JITC", file.getOriginatorsName());
        assertEquals("(520) 538-5458", file.getOriginatorsPhoneNumber());
        assertEquals(1, file.getNumberOfImageSegments());
        assertEquals(0, file.getNumberOfGraphicSegments());
        assertEquals(0, file.getNumberOfTextSegments());
        assertEquals(0, file.getNumberOfDataExtensionSegments());

        // Checks for ImageSegment.
        NitfImageSegment segment1 = file.getImageSegment(1);
        assertNotNull(segment1);
        assertEquals("Missing ID", segment1.getIdentifier());
        assertEquals("1996-12-18 12:15:39", formatter.format(segment1.getImageDateTime()));
        assertEquals("", segment1.getImageTargetId());
        assertEquals("- BASE IMAGE -", segment1.getImageIdentifier2());
        assertUnclasAndEmpty(segment1.getSecurityMetadata());
        assertEquals("Unknown", segment1.getImageSource());
        assertEquals(18L, segment1.getNumberOfRows());
        assertEquals(35L, segment1.getNumberOfColumns());
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
        assertEquals(2, lut1.getNumberOfEntries());
        assertEquals((byte)0xFF, lut1.getEntry(0));
        assertEquals((byte)0x00, lut1.getEntry(1));
        NitfImageBandLUT lut2 = band1.getLUT(2);
        assertNotNull(lut2);
        assertEquals(2, lut2.getNumberOfEntries());
        assertEquals((byte)0x00, lut2.getEntry(0));
        assertEquals((byte)0xFF, lut2.getEntry(1));
        NitfImageBandLUT lut3 = band1.getLUT(3);
        assertNotNull(lut3);
        assertEquals(2, lut3.getNumberOfEntries());
        assertEquals((byte)0x00, lut3.getEntry(0));
        assertEquals((byte)0x00, lut3.getEntry(1));

        assertEquals(ImageMode.BLOCKINTERLEVE, segment1.getImageMode());
        assertEquals(1, segment1.getNumberOfBlocksPerRow());
        assertEquals(1, segment1.getNumberOfBlocksPerColumn());
        assertEquals(35, segment1.getNumberOfPixelsPerBlockHorizontal());
        assertEquals(18, segment1.getNumberOfPixelsPerBlockVertical());
        assertEquals(1, segment1.getNumberOfBitsPerPixelPerBand());
        assertEquals(1, segment1.getImageDisplayLevel());
        assertEquals(0, segment1.getAttachmentLevel());
        assertEquals(100, segment1.getImageLocationRow());
        assertEquals(100, segment1.getImageLocationColumn());
        assertEquals("1.0 ", segment1.getImageMagnification());
    }

    @Test
    public void testGeoAirfieldHeaderReader() throws IOException, ParseException {
        final String geoAirfieldNitf21File = "/i_3001a.ntf";

        assertNotNull("Test file missing", getClass().getResource(geoAirfieldNitf21File));

        InputStream is = getClass().getResourceAsStream(geoAirfieldNitf21File);
        NitfFile reader = NitfFileFactory.parseHeadersOnly(is);
        assertEquals(FileType.NITF_TWO_ONE, reader.getFileType());
        assertEquals(3, reader.getComplexityLevel());
        assertEquals("BF01", reader.getStandardType());
        assertEquals("i_3001a", reader.getOriginatingStationId());
        assertEquals("1997-12-17 10:26:30", formatter.format(reader.getFileDateTime()));
        assertEquals("Checks an uncompressed 1024x1024 8 bit mono image with GEOcentric data. Airfield", reader.getFileTitle());
        assertUnclasAndEmpty(reader.getFileSecurityMetadata());
        assertEquals("00000", reader.getFileSecurityMetadata().getFileCopyNumber());
        assertEquals("00000", reader.getFileSecurityMetadata().getFileNumberOfCopies());
        assertEquals((byte)0xFF, reader.getFileBackgroundColour().getRed());
        assertEquals((byte)0xFF, reader.getFileBackgroundColour().getGreen());
        assertEquals((byte)0xFF, reader.getFileBackgroundColour().getBlue());
        assertEquals("JITC Fort Huachuca, AZ", reader.getOriginatorsName());
        assertEquals("(520) 538-5458", reader.getOriginatorsPhoneNumber());
        assertEquals(1, reader.getNumberOfImageSegments());
        assertEquals(0, reader.getNumberOfGraphicSegments());
        assertEquals(0, reader.getNumberOfTextSegments());
        assertEquals(0, reader.getNumberOfDataExtensionSegments());

        // Checks for ImageSegment.
        NitfImageSegment segment1 = reader.getImageSegment(1);
        assertNotNull(segment1);
        assertEquals("Missing ID", segment1.getIdentifier());
        assertEquals("1996-12-17 10:26:30", formatter.format(segment1.getImageDateTime()));
        assertEquals("", segment1.getImageTargetId());
        assertEquals("- BASE IMAGE -", segment1.getImageIdentifier2());
        assertUnclasAndEmpty(segment1.getSecurityMetadata());
        assertEquals("Unknown", segment1.getImageSource());
        assertEquals(1024L, segment1.getNumberOfRows());
        assertEquals(1024L, segment1.getNumberOfColumns());
        assertEquals(PixelValueType.INTEGER, segment1.getPixelValueType());
        assertEquals(ImageRepresentation.MONOCHROME, segment1.getImageRepresentation());
        assertEquals(ImageCategory.VISUAL, segment1.getImageCategory());
        assertEquals(8, segment1.getActualBitsPerPixelPerBand());
        assertEquals(PixelJustification.RIGHT, segment1.getPixelJustification());
        assertEquals(ImageCoordinatesRepresentation.GEOGRAPHIC, segment1.getImageCoordinatesRepresentation());
        ImageCoordinates imageCoords = segment1.getImageCoordinates();
        assertNotNull(imageCoords);
        assertEquals(32.98333333333, imageCoords.getCoordinate00().getLatitude(), 0.000001);
        assertEquals(85.00000000000, imageCoords.getCoordinate00().getLongitude(), 0.000001);
        assertEquals(32.98333333333, imageCoords.getCoordinate0MaxCol().getLatitude(), 0.000001);
        assertEquals(85.00027777777, imageCoords.getCoordinate0MaxCol().getLongitude(), 0.000001);
        assertEquals(32.98305555555, imageCoords.getCoordinateMaxRowMaxCol().getLatitude(), 0.000001);
        assertEquals(85.00027777777, imageCoords.getCoordinateMaxRowMaxCol().getLongitude(), 0.000001);
        assertEquals(32.98305555555, imageCoords.getCoordinateMaxRow0().getLatitude(), 0.000001);
        assertEquals(85.00000000000, imageCoords.getCoordinateMaxRow0().getLongitude(), 0.000001);
        assertEquals(0, segment1.getNumberOfImageComments());
        assertEquals(ImageCompression.NOTCOMPRESSED, segment1.getImageCompression());
        assertEquals(1, segment1.getNumBands());
        // Checks for ImageBand
        NitfImageBand band1 = segment1.getImageBand(1);
        assertNotNull(band1);
        assertEquals("M", band1.getImageRepresentation());
        assertEquals("", band1.getSubCategory());
        assertEquals(0, band1.getNumLUTs());

        assertEquals("1.0 ", segment1.getImageMagnification());
    }

    @Test
    public void testImageTextComment() throws IOException, ParseException {
        final String testfile = "/ns3010a.nsf";

        assertNotNull("Test file missing", getClass().getResource(testfile));

        InputStream is = getClass().getResourceAsStream(testfile);
        NitfFile reader = NitfFileFactory.parseHeadersOnly(is);
        assertEquals(FileType.NSIF_ONE_ZERO, reader.getFileType());
        assertEquals(3, reader.getComplexityLevel());
        assertEquals("BF01", reader.getStandardType());
        assertEquals("NS3010A", reader.getOriginatingStationId());
        assertEquals("1997-12-17 16:00:28", formatter.format(reader.getFileDateTime()));
        assertEquals("Checks a JPEG-compressed 231x191 8-bit mono image. blimp. Not divisable by 8.", reader.getFileTitle());
        assertUnclasAndEmpty(reader.getFileSecurityMetadata());
        assertEquals("00001", reader.getFileSecurityMetadata().getFileCopyNumber());
        assertEquals("00001", reader.getFileSecurityMetadata().getFileNumberOfCopies());
        assertEquals((byte)0xFF, reader.getFileBackgroundColour().getRed());
        assertEquals((byte)0xFF, reader.getFileBackgroundColour().getGreen());
        assertEquals((byte)0xFF, reader.getFileBackgroundColour().getBlue());
        assertEquals("JITC Fort Huachuca, AZ", reader.getOriginatorsName());
        assertEquals("(520) 538-5458", reader.getOriginatorsPhoneNumber());
        assertEquals(1, reader.getNumberOfImageSegments());
        assertEquals(0, reader.getNumberOfGraphicSegments());
        assertEquals(0, reader.getNumberOfTextSegments());
        assertEquals(0, reader.getNumberOfDataExtensionSegments());

        // Checks for ImageSegment.
        NitfImageSegment segment1 = reader.getImageSegment(1);
        assertNotNull(segment1);
        assertEquals("0000000001", segment1.getIdentifier());
        assertEquals("1996-12-17 16:00:28", formatter.format(segment1.getImageDateTime()));
        assertEquals("", segment1.getImageTargetId());
        assertEquals("This is an unclassified image in an unclassified NITF file Q3.", segment1.getImageIdentifier2());
        assertUnclasAndEmpty(segment1.getSecurityMetadata());
        assertEquals("", segment1.getImageSource());
        assertEquals(191L, segment1.getNumberOfRows());
        assertEquals(231L, segment1.getNumberOfColumns());
        assertEquals(PixelValueType.INTEGER, segment1.getPixelValueType());
        assertEquals(ImageRepresentation.MONOCHROME, segment1.getImageRepresentation());
        assertEquals(ImageCategory.VISUAL, segment1.getImageCategory());
        assertEquals(8, segment1.getActualBitsPerPixelPerBand());
        assertEquals(PixelJustification.RIGHT, segment1.getPixelJustification());
        assertEquals(ImageCoordinatesRepresentation.NONE, segment1.getImageCoordinatesRepresentation());
        assertEquals(9, segment1.getNumberOfImageComments());
        assertEquals("This is image comment #1 for the unclassified image #1 from test message Q3.", segment1.getImageComment(1));
        assertEquals("This is image comment #2 for the unclassified image #1 from test message Q3.", segment1.getImageComment(2));
        assertEquals("This is image comment #3 for the unclassified image #1 from test message Q3.", segment1.getImageComment(3));
        assertEquals("This is image comment #4 for the unclassified image #1 from test message Q3.", segment1.getImageComment(4));
        assertEquals("This is image comment #5 for the unclassified image #1 from test message Q3.", segment1.getImageComment(5));
        assertEquals("This is image comment #6 for the unclassified image #1 from test message Q3.", segment1.getImageComment(6));
        assertEquals("This is image comment #7 for the unclassified image #1 from test message Q3.", segment1.getImageComment(7));
        assertEquals("This is image comment #8 for the unclassified image #1 from test message Q3.", segment1.getImageComment(8));
        assertEquals("This is image comment #9 for the unclassified image #1 from test message Q3.", segment1.getImageComment(9));
        assertEquals(ImageCompression.JPEG, segment1.getImageCompression());
        assertEquals("00.0", segment1.getCompressionRate());
        assertEquals(1, segment1.getNumBands());
        NitfImageBand band1 = segment1.getImageBand(1);
        assertNotNull(band1);
        assertEquals("M", band1.getImageRepresentation());
        assertEquals("", band1.getSubCategory());
        assertEquals(0, band1.getNumLUTs());
        assertEquals("1.0 ", segment1.getImageMagnification());
    }

    @Test
    public void testMultiImage() throws IOException, ParseException {
        final String testfile = "/ns3361c.nsf";

        assertNotNull("Test file missing", getClass().getResource(testfile));

        InputStream is = getClass().getResourceAsStream(testfile);
        NitfFile reader = NitfFileFactory.parseHeadersOnly(is);
        assertEquals(FileType.NSIF_ONE_ZERO, reader.getFileType());
        assertEquals(3, reader.getComplexityLevel());
        assertEquals("BF01", reader.getStandardType());
        assertEquals("NS3361c", reader.getOriginatingStationId());
        assertEquals("2000-12-12 12:12:12", formatter.format(reader.getFileDateTime()));
        assertEquals("Boston_1 CONTAINS Four Sub-images lined up to show as a single image, dec data.", reader.getFileTitle());
        assertUnclasAndEmpty(reader.getFileSecurityMetadata());
        assertEquals("00001", reader.getFileSecurityMetadata().getFileCopyNumber());
        assertEquals("00001", reader.getFileSecurityMetadata().getFileNumberOfCopies());
        assertEquals((byte)0x00, reader.getFileBackgroundColour().getRed());
        assertEquals((byte)0x7F, reader.getFileBackgroundColour().getGreen());
        assertEquals((byte)0x00, reader.getFileBackgroundColour().getBlue());
        assertEquals("JITC NITF LAB", reader.getOriginatorsName());
        assertEquals("(520) 538-4858", reader.getOriginatorsPhoneNumber());
        assertEquals(4, reader.getNumberOfImageSegments());
        assertEquals(0, reader.getNumberOfGraphicSegments());
        assertEquals(0, reader.getNumberOfTextSegments());
        assertEquals(0, reader.getNumberOfDataExtensionSegments());

        // Checks for ImageSegment.
        NitfImageSegment segment1 = reader.getImageSegment(1);
        assertNotNull(segment1);
        assertEquals("GRT BOSTON", segment1.getIdentifier());
        assertEquals("2000-12-12 12:12:11", formatter.format(segment1.getImageDateTime()));
        assertEquals("               US", segment1.getImageTargetId());
        assertEquals("LOGAN AIRPORT BOSTON Located at 256,256, display level 4 first image file.", segment1.getImageIdentifier2());
        assertUnclasAndEmpty(segment1.getSecurityMetadata());
        assertEquals("", segment1.getImageSource());
        assertEquals(256L, segment1.getNumberOfRows());
        assertEquals(256L, segment1.getNumberOfColumns());
        assertEquals(PixelValueType.INTEGER, segment1.getPixelValueType());
        assertEquals(ImageRepresentation.MONOCHROME, segment1.getImageRepresentation());
        assertEquals(ImageCategory.VISUAL, segment1.getImageCategory());
        assertEquals(8, segment1.getActualBitsPerPixelPerBand());
        assertEquals(PixelJustification.RIGHT, segment1.getPixelJustification());
        assertEquals(ImageCoordinatesRepresentation.DECIMALDEGREES, segment1.getImageCoordinatesRepresentation());
        ImageCoordinates imageCoords = segment1.getImageCoordinates();
        assertNotNull(imageCoords);
        assertEquals(42.201, imageCoords.getCoordinate00().getLatitude(), 0.000001);
        assertEquals(-71.050, imageCoords.getCoordinate00().getLongitude(), 0.000001);
        assertEquals(42.201, imageCoords.getCoordinate0MaxCol().getLatitude(), 0.000001);
        assertEquals(-70.933, imageCoords.getCoordinate0MaxCol().getLongitude(), 0.000001);
        assertEquals(41.950, imageCoords.getCoordinateMaxRowMaxCol().getLatitude(), 0.000001);
        assertEquals(-70.933, imageCoords.getCoordinateMaxRowMaxCol().getLongitude(), 0.000001);
        assertEquals(41.950, imageCoords.getCoordinateMaxRow0().getLatitude(), 0.000001);
        assertEquals(-71.0500, imageCoords.getCoordinateMaxRow0().getLongitude(), 0.000001);
        assertEquals(0, segment1.getNumberOfImageComments());
        assertEquals(ImageCompression.NOTCOMPRESSED, segment1.getImageCompression());
        assertEquals(1, segment1.getNumBands());
        assertEquals("1.0 ", segment1.getImageMagnification());

        NitfImageSegment segment2 = reader.getImageSegment(2);
        assertNotNull(segment2);
        assertEquals("GRT BOSTON", segment2.getIdentifier());
        assertEquals("2000-12-12 12:12:11", formatter.format(segment2.getImageDateTime()));
        assertEquals("               US", segment2.getImageTargetId());
        assertEquals("LOGAN AIRPORT BOSTON located at 000,256, display level 2, second image file.", segment2.getImageIdentifier2());
        assertUnclasAndEmpty(segment2.getSecurityMetadata());
        assertEquals("", segment2.getImageSource());
        assertEquals(256L, segment2.getNumberOfRows());
        assertEquals(256L, segment2.getNumberOfColumns());
        assertEquals(PixelValueType.INTEGER, segment2.getPixelValueType());
        assertEquals(ImageRepresentation.MONOCHROME, segment2.getImageRepresentation());
        assertEquals(ImageCategory.VISUAL, segment2.getImageCategory());
        assertEquals(8, segment2.getActualBitsPerPixelPerBand());
        assertEquals(PixelJustification.RIGHT, segment2.getPixelJustification());
        assertEquals(ImageCoordinatesRepresentation.DECIMALDEGREES, segment2.getImageCoordinatesRepresentation());
        imageCoords = segment2.getImageCoordinates();
        assertNotNull(imageCoords);
        assertEquals(42.450, imageCoords.getCoordinate00().getLatitude(), 0.000001);
        assertEquals(-71.050, imageCoords.getCoordinate00().getLongitude(), 0.000001);
        assertEquals(42.450, imageCoords.getCoordinate0MaxCol().getLatitude(), 0.000001);
        assertEquals(-70.933, imageCoords.getCoordinate0MaxCol().getLongitude(), 0.000001);
        assertEquals(42.201, imageCoords.getCoordinateMaxRowMaxCol().getLatitude(), 0.000001);
        assertEquals(-70.933, imageCoords.getCoordinateMaxRowMaxCol().getLongitude(), 0.000001);
        assertEquals(42.201, imageCoords.getCoordinateMaxRow0().getLatitude(), 0.000001);
        assertEquals(-71.050, imageCoords.getCoordinateMaxRow0().getLongitude(), 0.000001);
        assertEquals(0, segment2.getNumberOfImageComments());
        assertEquals(ImageCompression.NOTCOMPRESSED, segment2.getImageCompression());
        assertEquals(1, segment2.getNumBands());
        NitfImageBand band1 = segment1.getImageBand(1);
        assertNotNull(band1);
        assertEquals("M", band1.getImageRepresentation());
        assertEquals("", band1.getSubCategory());
        assertEquals(0, band1.getNumLUTs());
        assertEquals("1.0 ", segment2.getImageMagnification());

        NitfImageSegment segment3 = reader.getImageSegment(3);
        assertNotNull(segment3);
        assertEquals("GRT BOSTON", segment3.getIdentifier());
        assertEquals("2000-12-12 12:12:11", formatter.format(segment3.getImageDateTime()));
        assertEquals("               US", segment3.getImageTargetId());
        assertEquals("LOGAN AIRPORT BOSTON located at 256,000, display level 3, third image file.", segment3.getImageIdentifier2());
        assertUnclasAndEmpty(segment3.getSecurityMetadata());
        assertEquals("", segment3.getImageSource());
        assertEquals(256L, segment3.getNumberOfRows());
        assertEquals(256L, segment3.getNumberOfColumns());
        assertEquals(PixelValueType.INTEGER, segment3.getPixelValueType());
        assertEquals(ImageRepresentation.MONOCHROME, segment3.getImageRepresentation());
        assertEquals(ImageCategory.VISUAL, segment3.getImageCategory());
        assertEquals(8, segment3.getActualBitsPerPixelPerBand());
        assertEquals(PixelJustification.RIGHT, segment3.getPixelJustification());
        assertEquals(ImageCoordinatesRepresentation.DECIMALDEGREES, segment3.getImageCoordinatesRepresentation());
        imageCoords = segment3.getImageCoordinates();
        assertNotNull(imageCoords);
        assertEquals(42.201, imageCoords.getCoordinate00().getLatitude(), 0.000001);
        assertEquals(-71.167, imageCoords.getCoordinate00().getLongitude(), 0.000001);
        assertEquals(42.201, imageCoords.getCoordinate0MaxCol().getLatitude(), 0.000001);
        assertEquals(-71.050, imageCoords.getCoordinate0MaxCol().getLongitude(), 0.000001);
        assertEquals(41.950, imageCoords.getCoordinateMaxRowMaxCol().getLatitude(), 0.000001);
        assertEquals(-71.050, imageCoords.getCoordinateMaxRowMaxCol().getLongitude(), 0.000001);
        assertEquals(41.950, imageCoords.getCoordinateMaxRow0().getLatitude(), 0.000001);
        assertEquals(-71.167, imageCoords.getCoordinateMaxRow0().getLongitude(), 0.000001);
        assertEquals(0, segment3.getNumberOfImageComments());
        assertEquals(ImageCompression.NOTCOMPRESSED, segment3.getImageCompression());
        assertEquals(1, segment3.getNumBands());
        assertEquals("1.0 ", segment3.getImageMagnification());

        NitfImageSegment segment4 = reader.getImageSegment(4);
        assertNotNull(segment4);
        assertEquals("GRT BOSTON", segment4.getIdentifier());
        assertEquals("2000-12-12 12:12:11", formatter.format(segment4.getImageDateTime()));
        assertEquals("               US", segment4.getImageTargetId());
        assertEquals("LOGAN AIRPORT BOSTON located at 000,000, display level 1, fourth image file.", segment4.getImageIdentifier2());
        assertUnclasAndEmpty(segment4.getSecurityMetadata());
        assertEquals("", segment4.getImageSource());
        assertEquals(256L, segment4.getNumberOfRows());
        assertEquals(256L, segment4.getNumberOfColumns());
        assertEquals(PixelValueType.INTEGER, segment4.getPixelValueType());
        assertEquals(ImageRepresentation.MONOCHROME, segment4.getImageRepresentation());
        assertEquals(ImageCategory.VISUAL, segment4.getImageCategory());
        assertEquals(8, segment4.getActualBitsPerPixelPerBand());
        assertEquals(PixelJustification.RIGHT, segment4.getPixelJustification());
        assertEquals(ImageCoordinatesRepresentation.DECIMALDEGREES, segment4.getImageCoordinatesRepresentation());
        imageCoords = segment4.getImageCoordinates();
        assertNotNull(imageCoords);
        assertEquals(42.450, imageCoords.getCoordinate00().getLatitude(), 0.000001);
        assertEquals(-71.167, imageCoords.getCoordinate00().getLongitude(), 0.000001);
        assertEquals(42.450, imageCoords.getCoordinate0MaxCol().getLatitude(), 0.000001);
        assertEquals(-71.050, imageCoords.getCoordinate0MaxCol().getLongitude(), 0.000001);
        assertEquals(42.201, imageCoords.getCoordinateMaxRowMaxCol().getLatitude(), 0.000001);
        assertEquals(-71.050, imageCoords.getCoordinateMaxRowMaxCol().getLongitude(), 0.000001);
        assertEquals(42.201, imageCoords.getCoordinateMaxRow0().getLatitude(), 0.000001);
        assertEquals(-71.167, imageCoords.getCoordinateMaxRow0().getLongitude(), 0.000001);
        assertEquals(0, segment4.getNumberOfImageComments());
        assertEquals(ImageCompression.NOTCOMPRESSED, segment4.getImageCompression());
        assertEquals(1, segment4.getNumBands());
        assertEquals("1.0 ", segment4.getImageMagnification());
    }

    @Test
    public void testTextSegmentParsingComment() throws IOException, ParseException {
        final String testfile = "/ns3201a.nsf";

        assertNotNull("Test file missing", getClass().getResource(testfile));
        InputStream is = getClass().getResourceAsStream(testfile);
        NitfFile reader = NitfFileFactory.parseHeadersOnly(is);
        assertEquals(FileType.NSIF_ONE_ZERO, reader.getFileType());
        assertEquals(3, reader.getComplexityLevel());
        assertEquals("BF01", reader.getStandardType());
        assertEquals("NS3201a", reader.getOriginatingStationId());
        assertEquals(1, reader.getNumberOfImageSegments());
        assertEquals(0, reader.getNumberOfGraphicSegments());
        assertEquals(1, reader.getNumberOfTextSegments());
        assertEquals(0, reader.getNumberOfDataExtensionSegments());

        NitfTextSegment textSegment = reader.getTextSegment(1);
        assertNotNull(textSegment);
        assertEquals(" PIDF T", textSegment.getIdentifier());
        assertEquals(1, textSegment.getAttachmentLevel());
        assertEquals("1998-02-17 10:19:39", formatter.format(textSegment.getTextDateTime()));
        assertEquals("                                                    Paragon Imaging Comment File", textSegment.getTextTitle());
        assertUnclasAndEmpty(textSegment.getSecurityMetadata());
        assertEquals(TextFormat.BASICCHARACTERSET, textSegment.getTextFormat());
    }

    @Test
    public void testExtendedHeaderSegmentParsing() throws IOException, ParseException {
        final String testfile = "/ns3228d.nsf";

        assertNotNull("Test file missing", getClass().getResource(testfile));
        InputStream is = getClass().getResourceAsStream(testfile);
        NitfFile reader = NitfFileFactory.parseHeadersOnly(is);
        assertEquals(FileType.NSIF_ONE_ZERO, reader.getFileType());
        assertEquals(3, reader.getComplexityLevel());
        assertEquals("BF01", reader.getStandardType());
        assertEquals("NS3228D", reader.getOriginatingStationId());
        assertEquals(1, reader.getNumberOfImageSegments());
        assertEquals(0, reader.getNumberOfGraphicSegments());
        assertEquals(0, reader.getNumberOfTextSegments());
        assertEquals(0, reader.getNumberOfDataExtensionSegments());
        TreCollection fileTres = reader.getTREsRawStructure();
        assertNotNull(fileTres);
        assertTrue(fileTres.hasTREs());
        List<Tre> treList = fileTres.getTREs();
        assertEquals(1, treList.size());
        Tre tre = treList.get(0);
        assertNotNull(tre);
        assertEquals("JITCID", tre.getName());
        assertEquals("I_3228D, Checks multi spectral image of 6 bands, the image subheader tells the receiving system to display band 2 as red, band 4 as green, and band 6 as blue.                                          ", new String(tre.getRawData(), "UTF-8"));
        assertEquals(0, tre.getEntries().size());
    }

    @Test
    public void testStreamingModeParsingFromFile() throws IOException, ParseException {
        final String testfile = "/ns3321a.nsf";
        assertNotNull("Test file missing", getClass().getResource(testfile));

        File resourceFile = new File(getClass().getResource(testfile).getFile());
        NitfFile file = NitfFileFactory.parseHeadersOnly(resourceFile);
        assertEquals(1, file.getNumberOfImageSegments());
        assertEquals(0, file.getNumberOfGraphicSegments());
        assertEquals(0, file.getNumberOfTextSegments());
        assertEquals(1, file.getNumberOfDataExtensionSegments());
    }

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void testStreamingModeParsingFromStream() throws IOException, ParseException {
        final String testfile = "/ns3321a.nsf";

        assertNotNull("Test file missing", getClass().getResource(testfile));
        InputStream is = getClass().getResourceAsStream(testfile);
        exception.expect(ParseException.class);
        exception.expectMessage("No support for streaming mode unless input is seekable");
        NitfFile reader = NitfFileFactory.parseHeadersOnly(is);
    }

    @Test
    public void testGraphicsSegmentParsing() throws IOException, ParseException {
        final String testfile = "/ns3051v.nsf";

        assertNotNull("Test file missing", getClass().getResource(testfile));

        InputStream is = getClass().getResourceAsStream(testfile);
        NitfFile reader = NitfFileFactory.parseHeadersOnly(is);
        assertEquals(FileType.NSIF_ONE_ZERO, reader.getFileType());
        assertEquals(3, reader.getComplexityLevel());
        assertEquals("BF01", reader.getStandardType());
        assertEquals("NS3051V", reader.getOriginatingStationId());
        assertEquals("1997-09-24 11:25:10", formatter.format(reader.getFileDateTime()));
        assertEquals("Checks for new nitf 2.1 polygon set element, NIST polygonset test 06.", reader.getFileTitle());
        assertUnclasAndEmpty(reader.getFileSecurityMetadata());
        assertEquals("00001", reader.getFileSecurityMetadata().getFileCopyNumber());
        assertEquals("00001", reader.getFileSecurityMetadata().getFileNumberOfCopies());
        assertEquals(0, reader.getFileBackgroundColour().getRed());
        assertEquals(0x7F, reader.getFileBackgroundColour().getGreen());
        assertEquals(0, reader.getFileBackgroundColour().getBlue());
        assertEquals("JITC Fort Huachuca, AZ", reader.getOriginatorsName());
        assertEquals("(520) 538-5458", reader.getOriginatorsPhoneNumber());
        assertEquals(0, reader.getNumberOfImageSegments());
        assertEquals(1, reader.getNumberOfGraphicSegments());
        assertEquals(0, reader.getNumberOfTextSegments());
        assertEquals(0, reader.getNumberOfDataExtensionSegments());

        NitfGraphicSegment segment = reader.getGraphicSegment(1);
        assertNotNull(segment);
        assertEquals("POLYGONSET", segment.getIdentifier());
        assertEquals("POLYGON_SET", segment.getGraphicName());
        assertUnclasAndEmpty(segment.getSecurityMetadata());
        assertEquals(1, segment.getGraphicDisplayLevel());
        assertEquals(0, segment.getAttachmentLevel());
        assertEquals(1100, segment.getGraphicLocationRow());
        assertEquals(100, segment.getGraphicLocationColumn());
        assertEquals(175, segment.getBoundingBox1Row());
        assertEquals(125, segment.getBoundingBox1Column());
        assertEquals(GraphicColour.COLOUR, segment.getGraphicColour());
        assertEquals(1075, segment.getBoundingBox2Row());
        assertEquals(825, segment.getBoundingBox2Column());
    }

    @Test
    public void testTreParsing() throws IOException, ParseException {
        final String testfile = "/i_3128b.ntf";

        assertNotNull("Test file missing", getClass().getResource(testfile));

        InputStream is = getClass().getResourceAsStream(testfile);
        NitfFile reader = NitfFileFactory.parseHeadersOnly(is);
        assertEquals(FileType.NITF_TWO_ONE, reader.getFileType());
        assertEquals(3, reader.getComplexityLevel());
        assertEquals("BF01", reader.getStandardType());
        assertEquals("I_3128b", reader.getOriginatingStationId());
        assertEquals("1999-02-10 14:01:44", formatter.format(reader.getFileDateTime()));
        assertEquals("Checks an uncomp. 512x480 w/PIAPR_,PIAIM_ & 3 PIAPE_tags conf. to STD. Lab Gang.", reader.getFileTitle());
        assertUnclasAndEmpty(reader.getFileSecurityMetadata());
        assertEquals("00000", reader.getFileSecurityMetadata().getFileCopyNumber());
        assertEquals("00000", reader.getFileSecurityMetadata().getFileNumberOfCopies());
        assertEquals(0, reader.getFileBackgroundColour().getRed());
        assertEquals(0x7F, reader.getFileBackgroundColour().getGreen());
        assertEquals(0, reader.getFileBackgroundColour().getBlue());
        assertEquals("JITC FT HUACHUCA", reader.getOriginatorsName());
        assertEquals("(520) 538-5458", reader.getOriginatorsPhoneNumber());
        assertEquals(1, reader.getNumberOfImageSegments());
        assertEquals(0, reader.getNumberOfGraphicSegments());
        assertEquals(0, reader.getNumberOfTextSegments());
        assertEquals(0, reader.getNumberOfDataExtensionSegments());
        Map<String, String> fileTresFlat = reader.getTREsFlat();
        Map<String, String> expectedFileTresFlat = new HashMap<String, String>() {
            {
                put("PIAPRC_ACCESSID", "THIS IS AN IPA FILE.                                       -END-");
                put("PIAPRC_FMCONTROL", "PXX                        -END-");
                put("PIAPRC_SUBDET", "P");
                put("PIAPRC_PRODCODE", "YY");
                put("PIAPRC_PRODUCERSE", "UNKNOW");
                put("PIAPRC_PRODIDNO", "X211");
                put("PIAPRC_PRODSNME", "JUNK FILE.");
                put("PIAPRC_PRODUCERCD", "27");
                put("PIAPRC_PRODCRTIME", "26081023ZOCT95");
                put("PIAPRC_MAPID", "132                                -END-");
                put("PIAPRC_SECTITLEREP", "02");
                put("PIAPRC_SECTITLE_1", "FIRST");
                put("PIAPRC_PPNUM_1", "31/46");
                put("PIAPRC_TPP_1", "001");
                put("PIAPRC_SECTITLE_2", "SECOND");
                put("PIAPRC_PPNUM_2", "32/47");
                put("PIAPRC_TPP_2", "002");
                put("PIAPRC_REQORGREP", "02");
                put("PIAPRC_REQORG_1", "FIRST                                                      -END-");
                put("PIAPRC_REQORG_2", "SECOND                                                     -END-");
                put("PIAPRC_KEYWORDREP", "02");
                put("PIAPRC_KEYWORD_1", "FIRST                                                                                                                                                                                                                                                     -END-");
                put("PIAPRC_KEYWORD_2", "SECOND                                                                                                                                                                                                                                                    -END-");
                put("PIAPRC_ASSRPTREP", "02");
                put("PIAPRC_ASSRPT_1", "FIRST          -END-");
                put("PIAPRC_ASSRPT_2", "SECOND         -END-");
                put("PIAPRC_ATEXTREP", "02");
                put("PIAPRC_ATEXT_1", "FIRST                                                                                                                                                                                                                                                     -END-");
                put("PIAPRC_ATEXT_2", "SECOND                                                                                                                                                                                                                                                    -END-");
            }
        };
        assertEquals(expectedFileTresFlat.size(), fileTresFlat.size());
        for (String fieldName : expectedFileTresFlat.keySet()) {
            assertEquals(expectedFileTresFlat.get(fieldName), fileTresFlat.get(fieldName));
        }

        NitfImageSegment image = reader.getImageSegmentZeroBase(0);
        assertNotNull(image);
        assertEquals("Missing ID", image.getIdentifier());
        assertEquals("1998-02-10 14:01:44", formatter.format(image.getImageDateTime()));
        assertEquals("", image.getImageTargetId());
        assertEquals("- BASE IMAGE -", image.getImageIdentifier2());
        assertUnclasAndEmpty(image.getSecurityMetadata());
        assertEquals("Unknown", image.getImageSource());
        assertEquals(480L, image.getNumberOfRows());
        assertEquals(512L, image.getNumberOfColumns());
        assertEquals(PixelValueType.INTEGER, image.getPixelValueType());
        assertEquals(ImageRepresentation.MONOCHROME, image.getImageRepresentation());
        assertEquals(ImageCategory.VISUAL, image.getImageCategory());
        assertEquals(8, image.getActualBitsPerPixelPerBand());
        assertEquals(PixelJustification.RIGHT, image.getPixelJustification());
        assertEquals(ImageCoordinatesRepresentation.NONE, image.getImageCoordinatesRepresentation());
        assertEquals(0, image.getNumberOfImageComments());
        assertEquals(ImageCompression.NOTCOMPRESSED, image.getImageCompression());
        assertEquals(1, image.getNumBands());
        Map<String, String> tresImageFlat = image.getTREsFlat();
        Map<String, String> expectedTresImageFlat = new HashMap<String, String>() {
            {
                put("PIAIMB_CAMSPECS", "GREAT");
                put("PIAIMB_CLOUDCVR", "050");
                put("PIAIMB_COMGEN", "00");
                put("PIAIMB_ESD", "Y");
                put("PIAIMB_GENERATION", "7");
                put("PIAIMB_OTHERCOND", "NO");
                put("PIAIMB_PIAMSNNUM", "BX-137");
                put("PIAIMB_PROJID", "47");
                put("PIAIMB_SENSMODE", "WHISKBROOM");
                put("PIAIMB_SENSNAME", "EYE BALL");
                put("PIAIMB_SOURCE", "ME LOOKING AT PICTURE TAKEN FROM A GOOD SOURCE.");
                put("PIAIMB_SRP", "Y");
                put("PIAIMB_SUBQUAL", "G");
                put("PIAPEA_0_ASSOCTRY", "US");
                put("PIAPEA_0_DOB", "031260");
                put("PIAPEA_0_FIRSTNME", "JAMES");
                put("PIAPEA_0_LASTNME", "DURHAM");
                put("PIAPEA_0_MIDNME", "A.");
                put("PIAPEA_1_ASSOCTRY", "US");
                put("PIAPEA_1_DOB", "062146");
                put("PIAPEA_1_FIRSTNME", "RICHARD");
                put("PIAPEA_1_LASTNME", "DAILEY");
                put("PIAPEA_1_MIDNME", "R.");
                put("PIAPEA_2_ASSOCTRY", "US");
                put("PIAPEA_2_DOB", "061856");
                put("PIAPEA_2_FIRSTNME", "DAVE");
                put("PIAPEA_2_LASTNME", "WEBB");
                put("PIAPEA_2_MIDNME", "L.");
            }
        };
        assertEquals(expectedTresImageFlat.size(), tresImageFlat.size());
        for (String fieldName : expectedTresImageFlat.keySet()) {
            assertEquals(expectedTresImageFlat.get(fieldName), tresImageFlat.get(fieldName));
        }
    }

    @Test
    public void testDataExtendedSegmentParsing() throws IOException, ParseException {
        final String testfile = "/autzen-utm10.ntf";

        assertNotNull("Test file missing", getClass().getResource(testfile));
        InputStream is = getClass().getResourceAsStream(testfile);
        NitfFile reader = NitfFileFactory.parseHeadersOnly(is);
        assertEquals(FileType.NITF_TWO_ONE, reader.getFileType());
        assertEquals(3, reader.getComplexityLevel());
        assertEquals("BF01", reader.getStandardType());
        assertEquals("ENVI", reader.getOriginatingStationId());
        assertEquals(1, reader.getNumberOfImageSegments());
        assertEquals(0, reader.getNumberOfGraphicSegments());
        assertEquals(0, reader.getNumberOfTextSegments());
        assertEquals(1, reader.getNumberOfDataExtensionSegments());

        NitfDataExtensionSegment des = reader.getDataExtensionSegment(1);
        assertNotNull(des);
        assertEquals("LIDARA DES", des.getIdentifier().trim());
        assertEquals(1, des.getDESVersion());
        assertUnclasAndEmpty(des.getSecurityMetadata());
    }

    @Test
    public void testGraphicsSegmentExtendedHeaderParsing() throws IOException, ParseException {
        final String testfile = "/gdal3453.ntf";

        assertNotNull("Test file missing", getClass().getResource(testfile));

        InputStream is = getClass().getResourceAsStream(testfile);
        NitfFile reader = NitfFileFactory.parseHeadersOnly(is);
        assertEquals(FileType.NITF_TWO_ONE, reader.getFileType());
        assertEquals(3, reader.getComplexityLevel());
        assertEquals("BF01", reader.getStandardType());
        assertEquals("Overwatch", reader.getOriginatingStationId());
        assertEquals("2009-11-25 18:07:24", formatter.format(reader.getFileDateTime()));
        assertEquals("BLAUE MOSCHEE NITF", reader.getFileTitle());

        NitfSecurityMetadata securityMetadata = reader.getFileSecurityMetadata();
        assertEquals("NL", securityMetadata.getSecurityClassificationSystem());
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

        assertEquals("00000", reader.getFileSecurityMetadata().getFileCopyNumber());
        assertEquals("00000", reader.getFileSecurityMetadata().getFileNumberOfCopies());
        assertEquals((byte)0xFF, reader.getFileBackgroundColour().getRed());
        assertEquals((byte)0xFF, reader.getFileBackgroundColour().getGreen());
        assertEquals((byte)0xFF, reader.getFileBackgroundColour().getBlue());
        assertEquals("", reader.getOriginatorsName());
        assertEquals("", reader.getOriginatorsPhoneNumber());
        assertEquals(1, reader.getNumberOfImageSegments());

        // Checks for ImageSegment.
        NitfImageSegment imageSegment = reader.getImageSegment(1);
        assertNotNull(imageSegment);
        assertEquals("Mosaic", imageSegment.getIdentifier());
        assertEquals("2009-10-16 05:20:40", formatter.format(imageSegment.getImageDateTime()));
        assertEquals("", imageSegment.getImageTargetId());
        assertEquals("Versions: 6.0.6.1; 40; Mission: 0000 001 141009 001, AOI ID: 6/21/2", imageSegment.getImageIdentifier2());
        securityMetadata = imageSegment.getSecurityMetadata();
        assertEquals("NL", securityMetadata.getSecurityClassificationSystem());
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

        assertEquals("", imageSegment.getImageSource());
        assertEquals(937L, imageSegment.getNumberOfRows());
        assertEquals(1563L, imageSegment.getNumberOfColumns());
        assertEquals(PixelValueType.BILEVEL, imageSegment.getPixelValueType());
        assertEquals(ImageRepresentation.MONOCHROME, imageSegment.getImageRepresentation());
        assertEquals(ImageCategory.ELECTROOPTICAL, imageSegment.getImageCategory());
        assertEquals(1, imageSegment.getActualBitsPerPixelPerBand());
        assertEquals(PixelJustification.RIGHT, imageSegment.getPixelJustification());
        assertEquals(ImageCoordinatesRepresentation.DECIMALDEGREES, imageSegment.getImageCoordinatesRepresentation());
        ImageCoordinates imageCoords = imageSegment.getImageCoordinates();
        assertNotNull(imageCoords);
        assertEquals(36.709, imageCoords.getCoordinate00().getLatitude(), 0.000001);
        assertEquals(67.105, imageCoords.getCoordinate00().getLongitude(), 0.000001);
        assertEquals(36.714, imageCoords.getCoordinate0MaxCol().getLatitude(), 0.000001);
        assertEquals(67.114, imageCoords.getCoordinate0MaxCol().getLongitude(), 0.000001);
        assertEquals(36.709, imageCoords.getCoordinateMaxRowMaxCol().getLatitude(), 0.000001);
        assertEquals(67.118, imageCoords.getCoordinateMaxRowMaxCol().getLongitude(), 0.000001);
        assertEquals(36.704, imageCoords.getCoordinateMaxRow0().getLatitude(), 0.000001);
        assertEquals(67.109, imageCoords.getCoordinateMaxRow0().getLongitude(), 0.000001);
        assertEquals(3, imageSegment.getNumberOfImageComments());
        assertEquals("", imageSegment.getImageComment(1));
        assertEquals("", imageSegment.getImageComment(2));
        assertEquals("", imageSegment.getImageComment(3));
        assertEquals(ImageCompression.NOTCOMPRESSED, imageSegment.getImageCompression());
        assertEquals(1, imageSegment.getNumBands());
        NitfImageBand band1 = imageSegment.getImageBand(1);
        assertNotNull(band1);
        assertEquals("M", band1.getImageRepresentation());
        assertEquals("", band1.getSubCategory());
        assertEquals(0, band1.getNumLUTs());

        assertEquals(8, reader.getNumberOfGraphicSegments());
        assertEquals(0, reader.getNumberOfTextSegments());
        assertEquals(0, reader.getNumberOfDataExtensionSegments());

        NitfGraphicSegment segment1 = reader.getGraphicSegment(1);
        assertNotNull(segment1);
        assertEquals("30", segment1.getIdentifier());
        assertEquals("", segment1.getGraphicName());
        assertUnclasAndEmpty(segment1.getSecurityMetadata());
        assertEquals(2, segment1.getGraphicDisplayLevel());
        assertEquals(0, segment1.getAttachmentLevel());
        assertEquals(326, segment1.getGraphicLocationRow());
        assertEquals(713, segment1.getGraphicLocationColumn());
        assertEquals(326, segment1.getBoundingBox1Row());
        assertEquals(713, segment1.getBoundingBox1Column());
        assertEquals(GraphicColour.COLOUR, segment1.getGraphicColour());
        assertEquals(411, segment1.getBoundingBox2Row());
        assertEquals(788, segment1.getBoundingBox2Column());
        NitfGraphicSegment segment2 = reader.getGraphicSegment(2);
        assertNotNull(segment2);
        assertEquals("35", segment2.getIdentifier());
        assertEquals("", segment2.getGraphicName());
        assertUnclasAndEmpty(segment2.getSecurityMetadata());
        assertEquals(3, segment2.getGraphicDisplayLevel());
        assertEquals(0, segment2.getAttachmentLevel());
        assertEquals(275, segment2.getGraphicLocationRow());
        assertEquals(762, segment2.getGraphicLocationColumn());
        assertEquals(275, segment2.getBoundingBox1Row());
        assertEquals(762, segment2.getBoundingBox1Column());
        assertEquals(GraphicColour.COLOUR, segment2.getGraphicColour());
        assertEquals(345, segment2.getBoundingBox2Row());
        assertEquals(836, segment2.getBoundingBox2Column());
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