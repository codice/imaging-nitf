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

public class Nitf20HeaderTest {

    @Test
    public void testCompliantHeaderRead() throws IOException, ParseException {
        final String simpleNitf20File = "/U_1114A.NTF";

        assertNotNull("Test file missing", getClass().getResource(simpleNitf20File));

        InputStream is = getClass().getResourceAsStream(simpleNitf20File);
        NitfFile file = new NitfFile();
        file.parse(is, EnumSet.of(ParseOption.ExtractTextSegmentData));
        assertEquals(FileType.NITF_TWO_ZERO, file.getFileType());
        assertEquals(1, file.getComplexityLevel());
        assertEquals("", file.getStandardType());
        assertEquals("U21H00N1", file.getOriginatingStationId());
        assertEquals("1994-04-03 19:16:36", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(file.getFileDateTime()));
        assertEquals("checks the handling of an NITF file w/ only a text file.", file.getFileTitle());
        NitfFileSecurityMetadata securityMetadata = file.getFileSecurityMetadata();
        assertUnclasAndEmpty(securityMetadata);
        assertEquals("999998", securityMetadata.getDowngradeDateOrSpecialCase());
        assertEquals("This  file   will not need a downgrade.", securityMetadata.getDowngradeEvent());

        assertEquals("00001", file.getFileSecurityMetadata().getFileCopyNumber());
        assertEquals("00001", file.getFileSecurityMetadata().getFileNumberOfCopies());
        assertEquals("JITC Fort Huachuca, AZ", file.getOriginatorsName());
        assertEquals("(602) 538-5458", file.getOriginatorsPhoneNumber());
        assertEquals(0, file.getNumberOfImageSegments());
        assertEquals(0, file.getNumberOfGraphicSegments());
        assertEquals(1, file.getNumberOfTextSegments());
        assertEquals(0, file.getNumberOfDataExtensionSegments());

        NitfTextSegment textSegment = file.getTextSegment(1);
        assertNotNull(textSegment);
        assertEquals("0000000001", textSegment.getTextIdentifier());
        assertEquals(0, textSegment.getTextAttachmentLevel());
        assertEquals("1993-03-27 23:55:36", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(textSegment.getTextDateTime()));
        assertEquals("This is the title of unclassified text file #1 in NITF  file   U21H00N1.", textSegment.getTextTitle());
        NitfSecurityMetadata textSecurityMetadata = textSegment.getSecurityMetadata();
        assertUnclasAndEmpty(textSecurityMetadata);
        assertEquals("", textSecurityMetadata.getSecurityControlNumber());
        assertEquals("999998", textSecurityMetadata.getDowngradeDateOrSpecialCase());
        assertEquals("This text will never need downgrading.", textSecurityMetadata.getDowngradeEvent());
        assertEquals(TextFormat.BASICCHARACTERSET, textSegment.getTextFormat());
        assertEquals("A", textSegment.getTextData());

        is.close();
    }

    @Test
    public void testU1123() throws IOException, ParseException {
        final String nitf20File = "/U_1123A.NTF";

        assertNotNull("Test file missing", getClass().getResource(nitf20File));

        InputStream is = getClass().getResourceAsStream(nitf20File);
        NitfFile file = new NitfFile();
        file.parse(is, EnumSet.allOf(ParseOption.class));
        assertEquals(FileType.NITF_TWO_ZERO, file.getFileType());
        assertEquals(1, file.getComplexityLevel());
        assertEquals("", file.getStandardType());
        assertEquals("U217G0J1", file.getOriginatingStationId());
        assertEquals("1992-11-03 13:52:26", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(file.getFileDateTime()));
        assertEquals("This NITF message contains 5 images, 4 symbols, 4 labels and 1 text.", file.getFileTitle());
        NitfFileSecurityMetadata securityMetadata = file.getFileSecurityMetadata();
        assertUnclasAndEmpty(securityMetadata);
        assertEquals("999998", securityMetadata.getDowngradeDateOrSpecialCase());
        assertEquals("This message will not need a downgrade.", securityMetadata.getDowngradeEvent());

        assertEquals("00001", file.getFileSecurityMetadata().getFileCopyNumber());
        assertEquals("00001", file.getFileSecurityMetadata().getFileNumberOfCopies());
        assertEquals("JITC", file.getOriginatorsName());
        assertEquals("(602) 538-5458", file.getOriginatorsPhoneNumber());
        assertEquals(5, file.getNumberOfImageSegments());
        assertEquals(0, file.getNumberOfGraphicSegments());
        assertEquals(4, file.getNumberOfSymbolSegments());
        assertEquals(4, file.getNumberOfLabelSegments());
        assertEquals(1, file.getNumberOfTextSegments());
        assertEquals(0, file.getNumberOfDataExtensionSegments());

        NitfImageSegment imageSegment1 = file.getImageSegment(1);
        assertNotNull(imageSegment1);
        assertEquals("Missing ID", imageSegment1.getImageIdentifier1());
        assertEquals("1993-03-25 15:25:59",  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(imageSegment1.getImageDateTime()));
        assertEquals("- BASE IMAGE -", imageSegment1.getImageIdentifier2());
        assertEquals("", imageSegment1.getImageTargetId());
        assertUnclasAndEmpty(imageSegment1.getSecurityMetadata());
        assertEquals("Unknown", imageSegment1.getImageSource());
        assertEquals(1024L, imageSegment1.getNumberOfRows());
        assertEquals(1024L, imageSegment1.getNumberOfColumns());
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
        assertEquals(1024, imageSegment1.getNumberOfPixelsPerBlockHorizontal());
        assertEquals(1024, imageSegment1.getNumberOfPixelsPerBlockVertical());
        assertEquals(8, imageSegment1.getNumberOfBitsPerPixelPerBand());
        assertEquals(1, imageSegment1.getImageDisplayLevel());
        assertEquals(0, imageSegment1.getImageAttachmentLevel());
        assertEquals(0, imageSegment1.getImageLocationRow());
        assertEquals(0, imageSegment1.getImageLocationColumn());
        assertEquals("1.0 ", imageSegment1.getImageMagnification());
        assertEquals(1.0, imageSegment1.getImageMagnificationAsDouble(), 0.00000001);

        NitfImageSegment imageSegment2 = file.getImageSegment(2);
        assertNotNull(imageSegment2);
        assertEquals("Missing ID", imageSegment2.getImageIdentifier1());
        assertEquals("1993-03-25 15:25:59",  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(imageSegment2.getImageDateTime()));
        assertEquals("- GROUP 3 -", imageSegment2.getImageIdentifier2());
        assertEquals("", imageSegment2.getImageTargetId());
        assertUnclasAndEmpty(imageSegment2.getSecurityMetadata());
        assertEquals("123456789012345678901234567890123456789012", imageSegment2.getImageSource());
        assertEquals(64L, imageSegment2.getNumberOfRows());
        assertEquals(64L, imageSegment2.getNumberOfColumns());
        assertEquals(PixelValueType.INTEGER, imageSegment2.getPixelValueType());
        assertEquals(ImageRepresentation.MONOCHROME, imageSegment2.getImageRepresentation());
        assertEquals(ImageCategory.VISUAL, imageSegment2.getImageCategory());
        assertEquals(1, imageSegment2.getActualBitsPerPixelPerBand());
        assertEquals(PixelJustification.RIGHT, imageSegment2.getPixelJustification());
        assertEquals(ImageCoordinatesRepresentation.NONE, imageSegment2.getImageCoordinatesRepresentation());
        assertEquals(0, imageSegment2.getNumberOfImageComments());
        assertEquals(ImageCompression.BILEVEL, imageSegment2.getImageCompression());
        assertEquals(1, imageSegment2.getNumBands());
        band1 = imageSegment2.getImageBand(1);
        assertNotNull(band1);
        assertEquals("", band1.getImageRepresentation());
        assertEquals("", band1.getSubCategory());
        assertEquals(0, band1.getNumLUTs());
        assertEquals(0, band1.getNumLUTEntries());
        assertEquals(ImageMode.BLOCKINTERLEVE, imageSegment2.getImageMode());
        assertEquals(1, imageSegment2.getNumberOfBlocksPerRow());
        assertEquals(1, imageSegment2.getNumberOfBlocksPerColumn());
        assertEquals(64, imageSegment2.getNumberOfPixelsPerBlockHorizontal());
        assertEquals(64, imageSegment2.getNumberOfPixelsPerBlockVertical());
        assertEquals(1, imageSegment2.getNumberOfBitsPerPixelPerBand());
        assertEquals(8, imageSegment2.getImageDisplayLevel());
        assertEquals(2, imageSegment2.getImageAttachmentLevel());
        assertEquals(150, imageSegment2.getImageLocationRow());
        assertEquals(0, imageSegment2.getImageLocationColumn());
        assertEquals("1.0 ", imageSegment2.getImageMagnification());

        NitfImageSegment imageSegment3 = file.getImageSegment(3);
        assertNotNull(imageSegment3);
        assertEquals("0000000003", imageSegment3.getImageIdentifier1());
        assertEquals("1989-01-01 12:00:00",  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(imageSegment3.getImageDateTime()));
        assertEquals("This is an unclassified image in an unclassified NITF message called U227C2J0", imageSegment3.getImageIdentifier2());
        assertEquals("", imageSegment3.getImageTargetId());
        assertUnclasAndEmpty(imageSegment3.getSecurityMetadata());
        assertEquals("", imageSegment3.getImageSource());
        assertEquals(64L, imageSegment3.getNumberOfRows());
        assertEquals(64L, imageSegment3.getNumberOfColumns());
        assertEquals(PixelValueType.INTEGER, imageSegment3.getPixelValueType());
        assertEquals(ImageRepresentation.MONOCHROME, imageSegment3.getImageRepresentation());
        assertEquals(ImageCategory.VISUAL, imageSegment3.getImageCategory());
        assertEquals(8, imageSegment3.getActualBitsPerPixelPerBand());
        assertEquals(PixelJustification.RIGHT, imageSegment3.getPixelJustification());
        assertEquals(ImageCoordinatesRepresentation.NONE, imageSegment3.getImageCoordinatesRepresentation());
        assertEquals(9, imageSegment3.getNumberOfImageComments());
        assertEquals("This is image comment 1 for the unclassified image #3 from test message.", imageSegment3.getImageComment(1));
        assertEquals("This is image comment 2 for the unclassified image #3 from test message.", imageSegment3.getImageComment(2));
        assertEquals("This is image comment 3 for the unclassified image #3 from test message.", imageSegment3.getImageComment(3));
        assertEquals("This is image comment 4 for the unclassified image #3 from test message.", imageSegment3.getImageComment(4));
        assertEquals("This is image comment 5 for the unclassified image #3 from test message.", imageSegment3.getImageComment(5));
        assertEquals("This is image comment 6 for the unclassified image #3 from test message.", imageSegment3.getImageComment(6));
        assertEquals("This is image comment 7 for the unclassified image #3 from test message.", imageSegment3.getImageComment(7));
        assertEquals("This is image comment 8 for the unclassified image #3 from test message.", imageSegment3.getImageComment(8));
        assertEquals("This is image comment 9 for the unclassified image #3 from test message.", imageSegment3.getImageComment(9));
        assertEquals(ImageCompression.NOTCOMPRESSED, imageSegment3.getImageCompression());
        assertEquals(1, imageSegment3.getNumBands());
        band1 = imageSegment3.getImageBand(1);
        assertNotNull(band1);
        assertEquals("", band1.getImageRepresentation());
        assertEquals("", band1.getSubCategory());
        assertEquals(0, band1.getNumLUTs());
        assertEquals(0, band1.getNumLUTEntries());
        assertEquals(ImageMode.BLOCKINTERLEVE, imageSegment3.getImageMode());
        assertEquals(1, imageSegment3.getNumberOfBlocksPerRow());
        assertEquals(1, imageSegment3.getNumberOfBlocksPerColumn());
        assertEquals(64, imageSegment3.getNumberOfPixelsPerBlockHorizontal());
        assertEquals(64, imageSegment3.getNumberOfPixelsPerBlockVertical());
        assertEquals(8, imageSegment3.getNumberOfBitsPerPixelPerBand());
        assertEquals(12, imageSegment3.getImageDisplayLevel());
        assertEquals(11, imageSegment3.getImageAttachmentLevel());
        assertEquals(10, imageSegment3.getImageLocationRow());
        assertEquals(10, imageSegment3.getImageLocationColumn());
        assertEquals("1.0 ", imageSegment3.getImageMagnification());

        NitfImageSegment imageSegment4 = file.getImageSegment(4);
        assertNotNull(imageSegment4);
        assertEquals("0000000001", imageSegment4.getImageIdentifier1());
        assertEquals("1989-01-01 12:00:00",  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(imageSegment4.getImageDateTime()));
        assertEquals("This is an unclassified image in an unclassified NITF file.", imageSegment4.getImageIdentifier2());
        assertEquals("", imageSegment4.getImageTargetId());
        assertUnclasAndEmpty(imageSegment4.getSecurityMetadata());
        assertEquals("", imageSegment4.getImageSource());
        assertEquals(191L, imageSegment4.getNumberOfRows());
        assertEquals(231L, imageSegment4.getNumberOfColumns());
        assertEquals(PixelValueType.INTEGER, imageSegment4.getPixelValueType());
        assertEquals(ImageRepresentation.MONOCHROME, imageSegment4.getImageRepresentation());
        assertEquals(ImageCategory.VISUAL, imageSegment4.getImageCategory());
        assertEquals(8, imageSegment4.getActualBitsPerPixelPerBand());
        assertEquals(PixelJustification.RIGHT, imageSegment4.getPixelJustification());
        assertEquals(ImageCoordinatesRepresentation.NONE, imageSegment4.getImageCoordinatesRepresentation());
        assertEquals(0, imageSegment4.getNumberOfImageComments());
        assertEquals(ImageCompression.JPEG, imageSegment4.getImageCompression());
        assertEquals(1, imageSegment4.getNumBands());
        band1 = imageSegment4.getImageBand(1);
        assertNotNull(band1);
        assertEquals("", band1.getImageRepresentation());
        assertEquals("", band1.getSubCategory());
        assertEquals(0, band1.getNumLUTs());
        assertEquals(0, band1.getNumLUTEntries());
        assertEquals(ImageMode.BLOCKINTERLEVE, imageSegment4.getImageMode());
        assertEquals(1, imageSegment4.getNumberOfBlocksPerRow());
        assertEquals(1, imageSegment4.getNumberOfBlocksPerColumn());
        assertEquals(231, imageSegment4.getNumberOfPixelsPerBlockHorizontal());
        assertEquals(191, imageSegment4.getNumberOfPixelsPerBlockVertical());
        assertEquals(8, imageSegment4.getNumberOfBitsPerPixelPerBand());
        assertEquals(5, imageSegment4.getImageDisplayLevel());
        assertEquals(1, imageSegment4.getImageAttachmentLevel());
        assertEquals(40, imageSegment4.getImageLocationRow());
        assertEquals(220, imageSegment4.getImageLocationColumn());
        assertEquals("1.0 ", imageSegment4.getImageMagnification());

        NitfImageSegment imageSegment5 = file.getImageSegment(5);
        assertNotNull(imageSegment5);
        assertEquals("0000000001", imageSegment5.getImageIdentifier1());
        assertEquals("1989-01-01 12:00:00",  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(imageSegment5.getImageDateTime()));
        assertEquals("This is an unclassified image in an unclassified NITF file Q3.", imageSegment5.getImageIdentifier2());
        assertEquals("", imageSegment5.getImageTargetId());
        assertUnclasAndEmpty(imageSegment5.getSecurityMetadata());
        assertEquals("", imageSegment5.getImageSource());
        assertEquals(73L, imageSegment5.getNumberOfRows());
        assertEquals(181L, imageSegment5.getNumberOfColumns());
        assertEquals(PixelValueType.INTEGER, imageSegment5.getPixelValueType());
        assertEquals(ImageRepresentation.MONOCHROME, imageSegment5.getImageRepresentation());
        assertEquals(ImageCategory.VISUAL, imageSegment5.getImageCategory());
        assertEquals(8, imageSegment5.getActualBitsPerPixelPerBand());
        assertEquals(PixelJustification.RIGHT, imageSegment5.getPixelJustification());
        assertEquals(ImageCoordinatesRepresentation.NONE, imageSegment5.getImageCoordinatesRepresentation());
        assertEquals(9, imageSegment5.getNumberOfImageComments());
        assertEquals("This is image comment #1 for the unclassified image #1 from test message Q3.", imageSegment5.getImageComment(1));
        assertEquals("This is image comment #9 for the unclassified image #1 from test message Q3.", imageSegment5.getImageComment(9));
        assertEquals(ImageCompression.JPEG, imageSegment5.getImageCompression());
        assertEquals(1, imageSegment5.getNumBands());
        band1 = imageSegment5.getImageBand(1);
        assertNotNull(band1);
        assertEquals("", band1.getImageRepresentation());
        assertEquals("", band1.getSubCategory());
        assertEquals(0, band1.getNumLUTs());
        assertEquals(0, band1.getNumLUTEntries());
        assertEquals(ImageMode.BLOCKINTERLEVE, imageSegment5.getImageMode());
        assertEquals(1, imageSegment5.getNumberOfBlocksPerRow());
        assertEquals(1, imageSegment5.getNumberOfBlocksPerColumn());
        assertEquals(181, imageSegment5.getNumberOfPixelsPerBlockHorizontal());
        assertEquals(73, imageSegment5.getNumberOfPixelsPerBlockVertical());
        assertEquals(8, imageSegment5.getNumberOfBitsPerPixelPerBand());
        assertEquals(2, imageSegment5.getImageDisplayLevel());
        assertEquals(1, imageSegment5.getImageAttachmentLevel());
        assertEquals(65, imageSegment5.getImageLocationRow());
        assertEquals(30, imageSegment5.getImageLocationColumn());
        assertEquals("1.0 ", imageSegment5.getImageMagnification());

        NitfSymbolSegment symbolSegment1 = file.getSymbolSegment(1);
        assertNotNull(symbolSegment1);
        assertEquals("0000000001", symbolSegment1.getSymbolIdentifier());
        assertEquals("Unclassified Symbol.", symbolSegment1.getSymbolName());
        assertUnclasAndEmpty(symbolSegment1.getSecurityMetadata());
        assertEquals("999998", symbolSegment1.getSecurityMetadata().getDowngradeDateOrSpecialCase());
        assertEquals("This symbol will never need downgrading.", symbolSegment1.getSecurityMetadata().getDowngradeEvent());
        assertEquals("B", symbolSegment1.getSymbolType());
        assertEquals(SymbolColour.ZERO_TRANSPARENT_ONE_BLACK, symbolSegment1.getSymbolColour());
        assertEquals(7, symbolSegment1.getNumberOfLinesPerSymbol());
        assertEquals(7, symbolSegment1.getNumberOfPixelsPerLine());
        assertEquals(0, symbolSegment1.getLineWidth());
        assertEquals(1, symbolSegment1.getNumberOfBitsPerPixel());
        assertEquals(3, symbolSegment1.getSymbolDisplayLevel());
        assertEquals(1, symbolSegment1.getSymbolAttachmentLevel());
        assertEquals(62, symbolSegment1.getSymbolLocationRow());
        assertEquals(170, symbolSegment1.getSymbolLocationColumn());
        assertEquals(0, symbolSegment1.getSymbolLocation2Row());
        assertEquals(0, symbolSegment1.getSymbolLocation2Column());
        assertEquals("000000", symbolSegment1.getSymbolNumber());
        assertEquals(0, symbolSegment1.getSymbolRotation());
        assertEquals(7, symbolSegment1.getSymbolData().length);

        NitfSymbolSegment symbolSegment2 = file.getSymbolSegment(2);
        assertNotNull(symbolSegment2);
        assertEquals("0000000002", symbolSegment2.getSymbolIdentifier());
        assertEquals("Unclassified Symbol.", symbolSegment2.getSymbolName());
        assertUnclasAndEmpty(symbolSegment2.getSecurityMetadata());
        assertEquals("999998", symbolSegment2.getSecurityMetadata().getDowngradeDateOrSpecialCase());
        assertEquals("This symbol will never need downgrading.", symbolSegment2.getSecurityMetadata().getDowngradeEvent());
        assertEquals("B", symbolSegment2.getSymbolType());
        assertEquals(SymbolColour.ZERO_TRANSPARENT_ONE_BLACK, symbolSegment2.getSymbolColour());
        assertEquals(18, symbolSegment2.getNumberOfLinesPerSymbol());
        assertEquals(35, symbolSegment2.getNumberOfPixelsPerLine());
        assertEquals(0, symbolSegment2.getLineWidth());
        assertEquals(1, symbolSegment2.getNumberOfBitsPerPixel());
        assertEquals(7, symbolSegment2.getSymbolDisplayLevel());
        assertEquals(5, symbolSegment2.getSymbolAttachmentLevel());
        assertEquals(100, symbolSegment2.getSymbolLocationRow());
        assertEquals(100, symbolSegment2.getSymbolLocationColumn());
        assertEquals(0, symbolSegment2.getSymbolLocation2Row());
        assertEquals(0, symbolSegment2.getSymbolLocation2Column());
        assertEquals("000000", symbolSegment2.getSymbolNumber());
        assertEquals(0, symbolSegment2.getSymbolRotation());
        assertEquals(79, symbolSegment2.getSymbolData().length);

        NitfSymbolSegment symbolSegment3 = file.getSymbolSegment(3);
        assertNotNull(symbolSegment3);
        assertEquals("0000000003", symbolSegment3.getSymbolIdentifier());
        assertEquals("Unclassified Symbol.", symbolSegment3.getSymbolName());
        assertUnclasAndEmpty(symbolSegment3.getSecurityMetadata());
        assertEquals("999998", symbolSegment3.getSecurityMetadata().getDowngradeDateOrSpecialCase());
        assertEquals("This symbol will never need downgrading.", symbolSegment3.getSecurityMetadata().getDowngradeEvent());
        assertEquals("B", symbolSegment3.getSymbolType());
        assertEquals(SymbolColour.ZERO_TRANSPARENT_ONE_BLACK, symbolSegment3.getSymbolColour());
        assertEquals(18, symbolSegment3.getNumberOfLinesPerSymbol());
        assertEquals(35, symbolSegment3.getNumberOfPixelsPerLine());
        assertEquals(0, symbolSegment3.getLineWidth());
        assertEquals(1, symbolSegment3.getNumberOfBitsPerPixel());
        assertEquals(9, symbolSegment3.getSymbolDisplayLevel());
        assertEquals(8, symbolSegment3.getSymbolAttachmentLevel());
        assertEquals(25, symbolSegment3.getSymbolLocationRow());
        assertEquals(25, symbolSegment3.getSymbolLocationColumn());
        assertEquals(0, symbolSegment3.getSymbolLocation2Row());
        assertEquals(0, symbolSegment3.getSymbolLocation2Column());
        assertEquals("000000", symbolSegment3.getSymbolNumber());
        assertEquals(0, symbolSegment3.getSymbolRotation());
        assertEquals(79, symbolSegment3.getSymbolData().length);

        NitfSymbolSegment symbolSegment4 = file.getSymbolSegment(4);
        assertNotNull(symbolSegment4);
        assertEquals("0000000004", symbolSegment4.getSymbolIdentifier());
        assertEquals("Unclassified Symbol.", symbolSegment4.getSymbolName());
        assertUnclasAndEmpty(symbolSegment4.getSecurityMetadata());
        assertEquals("999998", symbolSegment4.getSecurityMetadata().getDowngradeDateOrSpecialCase());
        assertEquals("This symbol will never need downgrading.", symbolSegment4.getSecurityMetadata().getDowngradeEvent());
        assertEquals("B", symbolSegment4.getSymbolType());
        assertEquals(SymbolColour.ZERO_TRANSPARENT_ONE_BLACK, symbolSegment4.getSymbolColour());
        assertEquals(17, symbolSegment4.getNumberOfLinesPerSymbol());
        assertEquals(35, symbolSegment4.getNumberOfPixelsPerLine());
        assertEquals(0, symbolSegment4.getLineWidth());
        assertEquals(1, symbolSegment4.getNumberOfBitsPerPixel());
        assertEquals(11, symbolSegment4.getSymbolDisplayLevel());
        assertEquals(1, symbolSegment4.getSymbolAttachmentLevel());
        assertEquals(400, symbolSegment4.getSymbolLocationRow());
        assertEquals(400, symbolSegment4.getSymbolLocationColumn());
        assertEquals(0, symbolSegment4.getSymbolLocation2Row());
        assertEquals(0, symbolSegment4.getSymbolLocation2Column());
        assertEquals("000000", symbolSegment4.getSymbolNumber());
        assertEquals(0, symbolSegment4.getSymbolRotation());
        assertEquals(75, symbolSegment4.getSymbolData().length);

        NitfLabelSegment labelSegment1 = file.getLabelSegment(1);
        assertNotNull(labelSegment1);
        assertEquals("0000000001", labelSegment1.getLabelIdentifier());
        assertUnclasAndEmpty(labelSegment1.getSecurityMetadata());
        assertEquals("999998", labelSegment1.getSecurityMetadata().getDowngradeDateOrSpecialCase());
        assertEquals("This label will never need downgrading.", labelSegment1.getSecurityMetadata().getDowngradeEvent());
        assertEquals(20, labelSegment1.getLabelLocationRow());
        assertEquals(160, labelSegment1.getLabelLocationColumn());
        assertEquals(0, labelSegment1.getLabelCellWidth());
        assertEquals(0, labelSegment1.getLabelCellHeight());
        assertEquals(4, labelSegment1.getLabelDisplayLevel());
        assertEquals(2, labelSegment1.getLabelAttachmentLevel());
        // TODO: make this reflect the Table VIII representation.
        assertEquals(1, labelSegment1.getLabelTextColour().getRed());
        assertEquals(1, labelSegment1.getLabelTextColour().getGreen());
        assertEquals(1, labelSegment1.getLabelTextColour().getBlue());
        assertEquals(0, labelSegment1.getLabelBackgroundColour().getRed());
        assertEquals(0, labelSegment1.getLabelBackgroundColour().getGreen());
        assertEquals(0, labelSegment1.getLabelBackgroundColour().getBlue());
        assertEquals("Label 3", labelSegment1.getLabelData());

        NitfLabelSegment labelSegment2 = file.getLabelSegment(2);
        assertNotNull(labelSegment2);
        assertEquals("0000000002", labelSegment2.getLabelIdentifier());
        assertUnclasAndEmpty(labelSegment2.getSecurityMetadata());
        assertEquals("999998", labelSegment2.getSecurityMetadata().getDowngradeDateOrSpecialCase());
        assertEquals("This label will never need downgrading.", labelSegment2.getSecurityMetadata().getDowngradeEvent());
        assertEquals(100, labelSegment2.getLabelLocationRow());
        assertEquals(100, labelSegment2.getLabelLocationColumn());
        assertEquals(0, labelSegment2.getLabelCellWidth());
        assertEquals(0, labelSegment2.getLabelCellHeight());
        assertEquals(6, labelSegment2.getLabelDisplayLevel());
        assertEquals(5, labelSegment2.getLabelAttachmentLevel());
        // TODO: make this reflect the Table VIII representation.
        assertEquals(1, labelSegment2.getLabelTextColour().getRed());
        assertEquals(1, labelSegment2.getLabelTextColour().getGreen());
        assertEquals(1, labelSegment2.getLabelTextColour().getBlue());
        assertEquals(0, labelSegment2.getLabelBackgroundColour().getRed());
        assertEquals(0, labelSegment2.getLabelBackgroundColour().getGreen());
        assertEquals(0, labelSegment2.getLabelBackgroundColour().getBlue());
        assertEquals("Label 2", labelSegment2.getLabelData());

        NitfLabelSegment labelSegment3 = file.getLabelSegment(3);
        assertNotNull(labelSegment3);
        assertEquals("0000000003", labelSegment3.getLabelIdentifier());
        assertUnclasAndEmpty(labelSegment3.getSecurityMetadata());
        assertEquals("999998", labelSegment3.getSecurityMetadata().getDowngradeDateOrSpecialCase());
        assertEquals("This label will never need downgrading.", labelSegment3.getSecurityMetadata().getDowngradeEvent());
        assertEquals(-20, labelSegment3.getLabelLocationRow());
        assertEquals(-20, labelSegment3.getLabelLocationColumn());
        assertEquals(0, labelSegment3.getLabelCellWidth());
        assertEquals(0, labelSegment3.getLabelCellHeight());
        assertEquals(10, labelSegment3.getLabelDisplayLevel());
        assertEquals(9, labelSegment3.getLabelAttachmentLevel());
        // TODO: make this reflect the Table VIII representation.
        assertEquals(1, labelSegment3.getLabelTextColour().getRed());
        assertEquals(1, labelSegment3.getLabelTextColour().getGreen());
        assertEquals(1, labelSegment3.getLabelTextColour().getBlue());
        assertEquals(0, labelSegment3.getLabelBackgroundColour().getRed());
        assertEquals(0, labelSegment3.getLabelBackgroundColour().getGreen());
        assertEquals(0, labelSegment3.getLabelBackgroundColour().getBlue());
        assertEquals("Label 4", labelSegment3.getLabelData());

        NitfLabelSegment labelSegment4 = file.getLabelSegment(4);
        assertNotNull(labelSegment4);
        assertEquals("0000000004", labelSegment4.getLabelIdentifier());
        assertUnclasAndEmpty(labelSegment4.getSecurityMetadata());
        assertEquals("999998", labelSegment4.getSecurityMetadata().getDowngradeDateOrSpecialCase());
        assertEquals("This label will never need downgrading.", labelSegment4.getSecurityMetadata().getDowngradeEvent());
        assertEquals(0, labelSegment4.getLabelLocationRow());
        assertEquals(25, labelSegment4.getLabelLocationColumn());
        assertEquals(0, labelSegment4.getLabelCellWidth());
        assertEquals(0, labelSegment4.getLabelCellHeight());
        assertEquals(13, labelSegment4.getLabelDisplayLevel());
        assertEquals(11, labelSegment4.getLabelAttachmentLevel());
        // TODO: make this reflect the Table VIII representation.
        assertEquals(1, labelSegment4.getLabelTextColour().getRed());
        assertEquals(1, labelSegment4.getLabelTextColour().getGreen());
        assertEquals(1, labelSegment4.getLabelTextColour().getBlue());
        assertEquals(0, labelSegment4.getLabelBackgroundColour().getRed());
        assertEquals(0, labelSegment4.getLabelBackgroundColour().getGreen());
        assertEquals(0, labelSegment4.getLabelBackgroundColour().getBlue());
        assertEquals("Label 1", labelSegment4.getLabelData());

        NitfTextSegment textSegment = file.getTextSegment(1);
        assertNotNull(textSegment);
        assertEquals("0000000001", textSegment.getTextIdentifier());
        assertEquals(0, textSegment.getTextAttachmentLevel());
        assertEquals("1990-06-07 21:11:36", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(textSegment.getTextDateTime()));
        assertEquals("This is the title of unclassified text file #1 in NITF message JR1_B.", textSegment.getTextTitle());
        NitfSecurityMetadata textSecurityMetadata = textSegment.getSecurityMetadata();
        assertUnclasAndEmpty(textSecurityMetadata);
        assertEquals("999998", textSecurityMetadata.getDowngradeDateOrSpecialCase());
        assertEquals("This text will never need downgrading.", textSecurityMetadata.getDowngradeEvent());
        assertEquals(TextFormat.BASICCHARACTERSET, textSegment.getTextFormat());
        assertEquals("123456\r\n", textSegment.getTextData());

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