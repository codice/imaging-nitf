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

import org.junit.rules.ExpectedException;
import org.junit.Rule;
import org.junit.Test;

public class Nitf21HeaderTest {

    @Test
    public void testCompliantHeaderRead() throws IOException, ParseException {
        final String simpleNitf21File = "/i_3034c.ntf";

        assertNotNull("Test file missing", getClass().getResource(simpleNitf21File));

        InputStream is = getClass().getResourceAsStream(simpleNitf21File);
        NitfHeaderReader reader = new NitfHeaderReader(is);
        assertEquals(FileType.NITF_TWO_ONE, reader.getFileType());
        assertEquals(3, reader.getComplexityLevel());
        assertEquals("BF01", reader.getStandardType());
        assertEquals("I_3034C", reader.getOriginatingStationId());
        assertEquals("1997-12-18 12:15:39", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(reader.getFileDateTime()));
        assertEquals("Check an RGB/LUT 1 bit image maps black to red and white to green.", reader.getFileTitle());
        assertUnclasAndEmpty(reader.getFileSecurityMetadata());
        assertEquals("00001", reader.getFileSecurityMetadata().getFileCopyNumber());
        assertEquals("00001", reader.getFileSecurityMetadata().getFileNumberOfCopies());
        assertEquals(0x20, reader.getFileBackgroundColourRed());
        assertEquals(0x20, reader.getFileBackgroundColourGreen());
        assertEquals(0x20, reader.getFileBackgroundColourBlue());
        assertEquals("JITC", reader.getOriginatorsName());
        assertEquals("(520) 538-5458", reader.getOriginatorsPhoneNumber());
        assertEquals(933L, reader.getFileLength());
        assertEquals(404, reader.getHeaderLength());
        assertEquals(1, reader.getNumberOfImageSegments());
        assertEquals(450, reader.getLengthOfImageSubheader(0));
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
        // TODO: check values
        NitfImageBandLUT lut2 = band1.getLUT(2);
        assertNotNull(lut2);
        // TODO check values
        NitfImageBandLUT lut3 = band1.getLUT(3);
        assertNotNull(lut3);
        // TODO check values

        assertEquals(ImageMode.BLOCKINTERLEVE, segment1.getImageMode());
        assertEquals(1, segment1.getNumberOfBlocksPerRow());
        assertEquals(1, segment1.getNumberOfBlocksPerColumn());
        assertEquals(35, segment1.getNumberOfPixelsPerBlockHorizontal());
        assertEquals(18, segment1.getNumberOfPixelsPerBlockVertical());
        assertEquals(1, segment1.getNumberOfBitsPerPixelPerBand());
        assertEquals(1, segment1.getImageDisplayLevel());
        assertEquals(0, segment1.getImageAttachmentLevel());
        assertEquals(100, segment1.getImageLocationRow());
        assertEquals(100, segment1.getImageLocationColumn());
        assertEquals(1.0, segment1.getImageMagnification(), 0.00001);
        assertEquals(0, segment1.getUserDefinedImageDataLength());
        assertEquals(0, segment1.getImageExtendedSubheaderDataLength());
        assertEquals(79, segment1.getLengthOfImage());

        is.close();
    }

    @Test
    public void testGeoAirfieldHeaderReader() throws IOException, ParseException {
        final String geoAirfieldNitf21File = "/i_3001a.ntf";

        assertNotNull("Test file missing", getClass().getResource(geoAirfieldNitf21File));

        InputStream is = getClass().getResourceAsStream(geoAirfieldNitf21File);
        NitfHeaderReader reader = new NitfHeaderReader(is);
        assertEquals(FileType.NITF_TWO_ONE, reader.getFileType());
        assertEquals(3, reader.getComplexityLevel());
        assertEquals("BF01", reader.getStandardType());
        assertEquals("i_3001a", reader.getOriginatingStationId());
        assertEquals("1997-12-17 10:26:30", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(reader.getFileDateTime()));
        assertEquals("Checks an uncompressed 1024x1024 8 bit mono image with GEOcentric data. Airfield", reader.getFileTitle());
        assertUnclasAndEmpty(reader.getFileSecurityMetadata());
        assertEquals("00000", reader.getFileSecurityMetadata().getFileCopyNumber());
        assertEquals("00000", reader.getFileSecurityMetadata().getFileNumberOfCopies());
        assertEquals((byte)0xFF, reader.getFileBackgroundColourRed());
        assertEquals((byte)0xFF, reader.getFileBackgroundColourGreen());
        assertEquals((byte)0xFF, reader.getFileBackgroundColourBlue());
        assertEquals("JITC Fort Huachuca, AZ", reader.getOriginatorsName());
        assertEquals("(520) 538-5458", reader.getOriginatorsPhoneNumber());
        assertEquals(1049479L, reader.getFileLength());
        assertEquals(404, reader.getHeaderLength());
        assertEquals(1, reader.getNumberOfImageSegments());
        assertEquals(499, reader.getLengthOfImageSubheader(0));
        assertEquals(1048576, reader.getLengthOfImage(0));
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
        assertEquals("1996-12-17 10:26:30", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(segment1.getImageDateTime()));
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
    }

    @Test
    public void testImageTextComment() throws IOException, ParseException {
        final String testfile = "/ns3010a.nsf";

        assertNotNull("Test file missing", getClass().getResource(testfile));

        InputStream is = getClass().getResourceAsStream(testfile);
        NitfHeaderReader reader = new NitfHeaderReader(is);
        assertEquals(FileType.NSIF_ONE_ZERO, reader.getFileType());
        assertEquals(3, reader.getComplexityLevel());
        assertEquals("BF01", reader.getStandardType());
        assertEquals("NS3010A", reader.getOriginatingStationId());
        assertEquals("1997-12-17 16:00:28", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(reader.getFileDateTime()));
        assertEquals("Checks a JPEG-compressed 231x191 8-bit mono image. blimp. Not divisable by 8.", reader.getFileTitle());
        assertUnclasAndEmpty(reader.getFileSecurityMetadata());
        assertEquals("00001", reader.getFileSecurityMetadata().getFileCopyNumber());
        assertEquals("00001", reader.getFileSecurityMetadata().getFileNumberOfCopies());
        assertEquals((byte)0xFF, reader.getFileBackgroundColourRed());
        assertEquals((byte)0xFF, reader.getFileBackgroundColourGreen());
        assertEquals((byte)0xFF, reader.getFileBackgroundColourBlue());
        assertEquals("JITC Fort Huachuca, AZ", reader.getOriginatorsName());
        assertEquals("(520) 538-5458", reader.getOriginatorsPhoneNumber());
        assertEquals(10711L, reader.getFileLength());
        assertEquals(404, reader.getHeaderLength());
        assertEquals(1, reader.getNumberOfImageSegments());
        assertEquals(1163, reader.getLengthOfImageSubheader(0));
        assertEquals(9144, reader.getLengthOfImage(0));
        assertEquals(0, reader.getNumberOfGraphicsSegments());
        assertEquals(0, reader.getNumberOfTextSegments());
        assertEquals(0, reader.getNumberOfDataExtensionSegments());
        assertEquals(0, reader.getNumberOfReservedExtensionSegments());
        assertEquals(0, reader.getUserDefinedHeaderDataLength());
        assertEquals(0, reader.getExtendedHeaderDataLength());

        // Checks for ImageSegment.
        NitfImageSegment segment1 = reader.getImageSegment(1);
        assertNotNull(segment1);
        assertEquals("0000000001", segment1.getImageIdentifier1());
        assertEquals("1996-12-17 16:00:28", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(segment1.getImageDateTime()));
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
    }

    @Test
    public void testMultiImage() throws IOException, ParseException {
        final String testfile = "/ns3361c.nsf";

        assertNotNull("Test file missing", getClass().getResource(testfile));

        InputStream is = getClass().getResourceAsStream(testfile);
        NitfHeaderReader reader = new NitfHeaderReader(is);
        assertEquals(FileType.NSIF_ONE_ZERO, reader.getFileType());
        assertEquals(3, reader.getComplexityLevel());
        assertEquals("BF01", reader.getStandardType());
        assertEquals("NS3361c", reader.getOriginatingStationId());
        assertEquals("2000-12-12 12:12:12", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(reader.getFileDateTime()));
        assertEquals("Boston_1 CONTAINS Four Sub-images lined up to show as a single image, dec data.", reader.getFileTitle());
        assertUnclasAndEmpty(reader.getFileSecurityMetadata());
        assertEquals("00001", reader.getFileSecurityMetadata().getFileCopyNumber());
        assertEquals("00001", reader.getFileSecurityMetadata().getFileNumberOfCopies());
        assertEquals((byte)0x00, reader.getFileBackgroundColourRed());
        assertEquals((byte)0x7F, reader.getFileBackgroundColourGreen());
        assertEquals((byte)0x00, reader.getFileBackgroundColourBlue());
        assertEquals("JITC NITF LAB", reader.getOriginatorsName());
        assertEquals("(520) 538-4858", reader.getOriginatorsPhoneNumber());
        assertEquals(264592L, reader.getFileLength());
        assertEquals(452, reader.getHeaderLength());
        assertEquals(4, reader.getNumberOfImageSegments());
        assertEquals(499, reader.getLengthOfImageSubheader(0));
        assertEquals(65536, reader.getLengthOfImage(0));
        assertEquals(499, reader.getLengthOfImageSubheader(1));
        assertEquals(65536, reader.getLengthOfImage(1));
        assertEquals(499, reader.getLengthOfImageSubheader(2));
        assertEquals(65536, reader.getLengthOfImage(2));
        assertEquals(499, reader.getLengthOfImageSubheader(3));
        assertEquals(65536, reader.getLengthOfImage(3));
        assertEquals(0, reader.getNumberOfGraphicsSegments());
        assertEquals(0, reader.getNumberOfTextSegments());
        assertEquals(0, reader.getNumberOfDataExtensionSegments());
        assertEquals(0, reader.getNumberOfReservedExtensionSegments());
        assertEquals(0, reader.getUserDefinedHeaderDataLength());
        assertEquals(0, reader.getExtendedHeaderDataLength());

        // Checks for ImageSegment.
        NitfImageSegment segment1 = reader.getImageSegment(1);
        assertNotNull(segment1);
        assertEquals("GRT BOSTON", segment1.getImageIdentifier1());
        assertEquals("2000-12-12 12:12:11", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(segment1.getImageDateTime()));
        assertEquals("US", segment1.getImageTargetId());
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

        NitfImageSegment segment2 = reader.getImageSegment(2);
        assertNotNull(segment2);
        assertEquals("GRT BOSTON", segment2.getImageIdentifier1());
        assertEquals("2000-12-12 12:12:11", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(segment2.getImageDateTime()));
        assertEquals("US", segment2.getImageTargetId());
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

        NitfImageSegment segment3 = reader.getImageSegment(3);
        assertNotNull(segment3);
        assertEquals("GRT BOSTON", segment3.getImageIdentifier1());
        assertEquals("2000-12-12 12:12:11", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(segment3.getImageDateTime()));
        assertEquals("US", segment3.getImageTargetId());
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

        NitfImageSegment segment4 = reader.getImageSegment(4);
        assertNotNull(segment4);
        assertEquals("GRT BOSTON", segment4.getImageIdentifier1());
        assertEquals("2000-12-12 12:12:11", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(segment4.getImageDateTime()));
        assertEquals("US", segment4.getImageTargetId());
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
    }

    @Test
    public void testTextSegmentParsingComment() throws IOException, ParseException {
        final String testfile = "/ns3201a.nsf";

        assertNotNull("Test file missing", getClass().getResource(testfile));
        InputStream is = getClass().getResourceAsStream(testfile);
        NitfHeaderReader reader = new NitfHeaderReader(is);
        assertEquals(FileType.NSIF_ONE_ZERO, reader.getFileType());
        assertEquals(3, reader.getComplexityLevel());
        assertEquals("BF01", reader.getStandardType());
        assertEquals("NS3201a", reader.getOriginatingStationId());
        assertEquals(1, reader.getNumberOfImageSegments());
        assertEquals(0, reader.getNumberOfGraphicsSegments());
        assertEquals(1, reader.getNumberOfTextSegments());
        assertEquals(0, reader.getNumberOfDataExtensionSegments());
        assertEquals(0, reader.getNumberOfReservedExtensionSegments());
        assertEquals(0, reader.getUserDefinedHeaderDataLength());
        assertEquals(0, reader.getExtendedHeaderDataLength());

        NitfTextSegment textSegment = reader.getTextSegment(1);
        assertNotNull(textSegment);
        assertEquals(" PIDF T", textSegment.getTextIdentifier());
        assertEquals(1, textSegment.getTextAttachmentLevel());
        assertEquals("1998-02-17 10:19:39", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(textSegment.getTextDateTime()));
        assertEquals("Paragon Imaging Comment File", textSegment.getTextTitle());
        assertUnclasAndEmpty(textSegment.getSecurityMetadata());
        assertEquals(TextFormat.BASICCHARACTERSET, textSegment.getTextFormat());
        assertEquals(0, textSegment.getTextExtendedSubheaderLength());
    }

    @Test
    public void testExtendedHeaderSegmentParsing() throws IOException, ParseException {
        final String testfile = "/ns3228d.nsf";

        assertNotNull("Test file missing", getClass().getResource(testfile));
        InputStream is = getClass().getResourceAsStream(testfile);
        NitfHeaderReader reader = new NitfHeaderReader(is);
        assertEquals(FileType.NSIF_ONE_ZERO, reader.getFileType());
        assertEquals(3, reader.getComplexityLevel());
        assertEquals("BF01", reader.getStandardType());
        assertEquals("NS3228D", reader.getOriginatingStationId());
        assertEquals(1, reader.getNumberOfImageSegments());
        assertEquals(0, reader.getNumberOfGraphicsSegments());
        assertEquals(0, reader.getNumberOfTextSegments());
        assertEquals(0, reader.getNumberOfDataExtensionSegments());
        assertEquals(0, reader.getNumberOfReservedExtensionSegments());
        assertEquals(0, reader.getUserDefinedHeaderDataLength());
        assertEquals(214, reader.getExtendedHeaderDataLength());
        assertEquals("JITCID00200I_3228D, Checks multi spectral image of 6 bands, the image subheader tells the receiving system to display band 2 as red, band 4 as green, and band 6 as blue.", reader.getRawExtendedHeaderData());

    }

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void testStreamingModeParsing() throws IOException, ParseException {
        final String testfile = "/ns3321a.nsf";

        assertNotNull("Test file missing", getClass().getResource(testfile));
        InputStream is = getClass().getResourceAsStream(testfile);
        exception.expect(UnsupportedOperationException.class);
        exception.expectMessage("No support for streaming mode unless input is seekable");
        NitfHeaderReader reader = new NitfHeaderReader(is);
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