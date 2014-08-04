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

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;

public class NitfImageSegment extends AbstractNitfSegment {

    private String imageIdentifier1 = null;
    private Date imageDateTime = null;
    // TODO: consider making this a class (BE + O-suffix + country code) if we can find examples
    private String imageTargetId = null;
    private String imageIdentifier2 = null;
    private NitfSecurityMetadata securityMetadata = null;
    private String imageSource = null;
    private long numRows = 0L;
    private long numColumns = 0L;
    private PixelValueType pixelValueType = PixelValueType.UNKNOWN;
    private ImageRepresentation imageRepresentation = ImageRepresentation.UNKNOWN;
    private ImageCategory imageCategory = ImageCategory.UNKNOWN;
    private int actualBitsPerPixelPerBand = 0;
    private PixelJustification pixelJustification = PixelJustification.UNKNOWN;
    private ImageCoordinatesRepresentation imageCoordinatesRepresentation = ImageCoordinatesRepresentation.UNKNOWN;
    private ImageCoordinates imageCoordinates = null;
    private int numImageComments;
    private List<String> imageComments = new ArrayList<String>();
    private ImageCompression imageCompression = ImageCompression.UNKNOWN;
    private String compressionRate = null;
    private int numBands = 0;
    private List<NitfImageBand> imageBands = new ArrayList<NitfImageBand>();
    private ImageMode imageMode = ImageMode.UNKNOWN;
    private int numBlocksPerRow = 0;
    private int numBlocksPerColumn = 0;
    private int numPixelsPerBlockHorizontal = 0;
    private int numPixelsPerBlockVertical = 0;
    private int numBitsPerPixelPerBand = 0;
    private int imageDisplayLevel = 0;
    private int imageAttachmentLevel = 0;
    private int imageLocationRow = 0;
    private int imageLocationColumn = 0;
    private String imageMagnification = null;
    private byte[] data = null;

    public NitfImageSegment() {
    }

    public final void parse(final NitfReader nitfReader, final long imageLength, final EnumSet<ParseOption> parseOptions) throws ParseException {
        new NitfImageSegmentParser(nitfReader, imageLength, parseOptions, this);
    }

    /**
        Set the first image identifier (IID1) for the image.
        <p>
        "This field shall contain a valid alphanumeric identification code associated with the
         image. The valid codes are determined by the application."

         @param identifier the first image identifier for the image (10 character maximum)
    */
    public final void setImageIdentifier1(final String identifier) {
        imageIdentifier1 = identifier;
    }

    /**
        Return the first image identifier (IID1) for the image.
        <p>
        "This field shall contain a valid alphanumeric identification code associated with the
         image. The valid codes are determined by the application."

        @return the identifier
    */
    public final String getImageIdentifier1() {
        return imageIdentifier1;
    }

    /**
        Set the date / time (IDATIM) for the image.
        <p>
        This is supposed to be the date and time that the image was captured.

        @param dateTime the date time for the image.
    */
    public final void setImageDateTime(final Date dateTime) {
        imageDateTime = dateTime;
    }

    /**
        Return the date / time (IDATIM) for the image.
        <p>
        This is supposed to be the date and time that the image was captured.

        @return the date / time that the image was captured, or null if unknown.
    */
    public final Date getImageDateTime() {
        return imageDateTime;
    }

    /**
        Set the target identifier (TGTID) for the image.
        <p>
        "This field shall contain the identification of the primary target in the format,
        BBBBBBBBBBOOOOOCC, consisting of ten characters of Basic Encyclopedia (BE) identifier,
        followed by five characters of facility OSUFFIX, followed by the two character country code as
        specified in FIPS PUB 10-4."

        It is common for some or all of the identifier to be filled with default spaces.

        @param targetId the target identifier as a concatenated string
    */
    public final void setImageTargetId(final String targetId) {
        imageTargetId = targetId;
    }

    /**
        Return the target identifier (TGTID) for the image.
        <p>
        "This field shall contain the identification of the primary target in the format,
        BBBBBBBBBBOOOOOCC, consisting of ten characters of Basic Encyclopedia (BE) identifier,
        followed by five characters of facility OSUFFIX, followed by the two character country code as
        specified in FIPS PUB 10-4."

        It is common for some or all of the identifier to be filled with default spaces.

        @return the target identifier as a concatenated string
    */
    public final String getImageTargetId() {
        return imageTargetId;
    }

    /**
        Set the second image identifier (IID2) for the image.
        <p>
        "This field can contain the identification of additional information about the image."

         In NITF 2.0 files, this is the image title (ITITLE) field.

         @param identifier the second image identifier for the image (80 character maximum)
    */
    public final void setImageIdentifier2(final String identifier) {
        imageIdentifier2 = identifier;
    }

    /**
        Return the second image identifier (IID2) for the image.
        <p>
        "This field can contain the identification of additional information about the image."

        In NITF 2.0 files, this is the image title (ITITLE) field.

        @return the identifier
    */
    public final String getImageIdentifier2() {
        return imageIdentifier2;
    }

    /**
        Set the security metadata elements for the image.

        See NitfSecurityMetadata for the various elements, which differ slightly between NITF 2.0 and NITF 2.1/NSIF 1.0.

        @param nitfSecurityMetadata the security metadata values to set.
    */
    public final void setSecurityMetadata(final NitfSecurityMetadata nitfSecurityMetadata) {
        securityMetadata = nitfSecurityMetadata;
    }

    /**
        Return the security metadata for the image.

        @return security metadata
    */
    public final NitfSecurityMetadata getSecurityMetadata() {
        return securityMetadata;
    }

    /**
        Set the image source (ISORCE) for the image.
        <p>
        "This field shall contain a description of the source of the image. If the source of the data is
        classified, then the description shall be preceded by the classification, including codeword(s)
        contained in table A-4. If this field is all spaces (0x20), it shall imply that no image source
        data applies."

        @param source image source (42 characters maximum)
    */
    public final void setImageSource(final String source) {
        imageSource = source;
    }

    /**
        Return the image source (ISORCE) for the image.
        <p>
        "This field shall contain a description of the source of the image. If the source of the data is
        classified, then the description shall be preceded by the classification, including codeword(s)
        contained in table A-4. If this field is all spaces (0x20), it shall imply that no image source
        data applies."

        @return the image source
    */
    public final String getImageSource() {
        return imageSource;
    }

    /**
        Set the number of significant rows (NROWS) in the image.
        <p>
        "This field shall contain the total number of rows of significant pixels in the image. When the product of the values
        of the NPPBV field and the NBPC field is greater than the value of the NROWS field (NPPBV * NBPC > NROWS), the rows
        indexed with the value of the NROWS field to (NPPBV * NBPC) minus 1 shall contain fill data. NOTE: Only the rows
        indexed 0 to the value of the NROWS field minus 1 of the image contain significant data. The pixel fill values are
        determined by the application."

        @param numberOfRows the number of significant rows
    */
    public final void setNumberOfRows(final long numberOfRows) {
        numRows = numberOfRows;
    }

    /**
        Returns the number of significant rows (NROWS) in the image.
        <p>
        "This field shall contain the total number of rows of significant pixels in the image. When the product of the values
        of the NPPBV field and the NBPC field is greater than the value of the NROWS field (NPPBV * NBPC > NROWS), the rows
        indexed with the value of the NROWS field to (NPPBV * NBPC) minus 1 shall contain fill data. NOTE: Only the rows
        indexed 0 to the value of the NROWS field minus 1 of the image contain significant data. The pixel fill values are
        determined by the application."

        @return the number of significant rows
    */
    public final long getNumberOfRows() {
        return numRows;
    }

    /**
        Set the number of significant columns (NCOLS) in the image.
        <p>
        "This field shall contain the total number of columns of significant pixels in the image. When the product of
        the values of the NPPBH field and the NBPR field is greater than the NCOLS field (NPPBH * NBPR > NCOLS), the
        columns indexed with the value of the NCOLS field to (NPPBH * NBPR) minus 1 shall contain fill data. NOTE: Only
        the columns indexed 0 to the value of the NCOLS field minus 1 of the image contain significant data. The pixel
        fill values are determined by the application."

        @param numberOfColumns the number of significant columns
    */
    public final void setNumberOfColumns(final long numberOfColumns) {
        numColumns = numberOfColumns;
    }

    /**
        Returns the number of significant columns (NCOLS) in the image.
        <p>
        "This field shall contain the total number of columns of significant pixels in the image. When the product of
        the values of the NPPBH field and the NBPR field is greater than the NCOLS field (NPPBH * NBPR > NCOLS), the
        columns indexed with the value of the NCOLS field to (NPPBH * NBPR) minus 1 shall contain fill data. NOTE: Only
        the columns indexed 0 to the value of the NCOLS field minus 1 of the image contain significant data. The pixel
        fill values are determined by the application."

        @return the number of significant columns
    */
    public final long getNumberOfColumns() {
        return numColumns;
    }

    /**
        Set the pixel value type (PVTYPE) for the image.
        <p>
        "This field shall contain an indicator of the type of computer representation
        used for the value for each pixel for each band in the image. Valid entries
        are INT for integer, B for bi-level, SI for 2’s complement signed integer, R
        for real, and C for complex. The data bits of INT and SI values shall appear
        in the file in order of significance, beginning with the MSB and ending with
        the LSB. Except when the data is JPEG 2000 compressed, INT and SI data types
        shall be limited to 8, 12, 16, 32, or 64-bits (see field NBPP). R values shall
        be represented according to IEEE 32 or 64-bit floating point representation
        (IEEE 754). C values shall be represented with the Real and Imaginary parts,
        each represented in IEEE 32 or 64-bit floating point representation (IEEE 754)
        and appearing in adjacent four or eight-byte blocks, first Real, then Imaginary.
        B (bi-level) pixel values shall be represented as single bits with binary value
        1 or 0."

        @param valueType the pixel value type
    */
    public final void setPixelValueType(final PixelValueType valueType) {
        pixelValueType = valueType;
    }

    /**
        Return the pixel value type (PVTYPE) for the image.
        <p>
        "This field shall contain an indicator of the type of computer representation
        used for the value for each pixel for each band in the image. Valid entries
        are INT for integer, B for bi-level, SI for 2’s complement signed integer, R
        for real, and C for complex. The data bits of INT and SI values shall appear
        in the file in order of significance, beginning with the MSB and ending with
        the LSB. Except when the data is JPEG 2000 compressed, INT and SI data types
        shall be limited to 8, 12, 16, 32, or 64-bits (see field NBPP). R values shall
        be represented according to IEEE 32 or 64-bit floating point representation
        (IEEE 754). C values shall be represented with the Real and Imaginary parts,
        each represented in IEEE 32 or 64-bit floating point representation (IEEE 754)
        and appearing in adjacent four or eight-byte blocks, first Real, then Imaginary.
        B (bi-level) pixel values shall be represented as single bits with binary value
        1 or 0."

        @return the pixel value type
    */
    public final PixelValueType getPixelValueType() {
        return pixelValueType;
    }

    /**
        Set the image representation (IREP) for the image.
        <p>
        "This field shall contain a valid indicator of the processing required in order to
        display an image. Valid representation indicators are MONO for monochrome; RGB for
        red, green, or blue true color, RGB/LUT for mapped color; MULTI for multiband imagery,
        NODISPLY for an image not intended for display, NVECTOR and POLAR for vectors with
        Cartesian and polar coordinates respectively, and VPH for SAR video phase history.
        In addition, compressed imagery can have this field set to YCbCr601 when compressed
        in the ITU-R Recommendation BT.601-5 color space using JPEG (IC field = C3). This
        field should be used in conjunction with the IREPBANDn field to interpret the
        processing required to display each band in the image."

        @param representation the image representation to apply
    */
    public final void setImageRepresentation(final ImageRepresentation representation) {
        imageRepresentation = representation;
    }

    /**
        Return the image representation (IREP) for the image.
        <p>
        "This field shall contain a valid indicator of the processing required in order to
        display an image. Valid representation indicators are MONO for monochrome; RGB for
        red, green, or blue true color, RGB/LUT for mapped color; MULTI for multiband imagery,
        NODISPLY for an image not intended for display, NVECTOR and POLAR for vectors with
        Cartesian and polar coordinates respectively, and VPH for SAR video phase history.
        In addition, compressed imagery can have this field set to YCbCr601 when compressed
        in the ITU-R Recommendation BT.601-5 color space using JPEG (IC field = C3). This
        field should be used in conjunction with the IREPBANDn field to interpret the
        processing required to display each band in the image."

        @return the image representation to apply
    */
    public final ImageRepresentation getImageRepresentation() {
        return imageRepresentation;
    }

    /**
        Set the image category (ICAT) for the image.
        <p>
        "This field shall contain a valid indicator of the specific category of image, raster or
        grid data. The specific category of an IS reveals its intended use or the nature of its collector. Valid
        categories include VIS for visible imagery, SL for side-looking radar, TI for thermal infrared, FL for
        forward looking infrared, RD for radar, EO for electro-optical, OP for optical, HR for high
        resolution radar, HS for hyperspectral, CP for color frame photography, BP for black/white frame
        photography, SAR for synthetic aperture radar, SARIQ for SAR radio hologram, IR for infrared,
        MS for multispectral, FP for fingerprints, MRI for magnetic resonance imagery, XRAY for x-rays,
        CAT for CAT scans, VD for video, BARO for barometric pressure, CURRENT for water current,
        DEPTH for water depth, and WIND for air wind charts. Valid categories for geographic products or
        geo-reference support data are MAP for raster maps, PAT for color patch, LEG for legends, DTEM for
        elevation models, MATR for other types of matrix data, and LOCG for location grids. This field
        should be used in conjunction with the ISUBCATn field to interpret the significance of each band in the
        image."

        @param category the image category
    */
    public final void setImageCategory(final ImageCategory category) {
        imageCategory = category;
    }

    /**
        Return the image category (ICAT) for the image.
        <p>
        "This field shall contain a valid indicator of the specific category of image, raster or
        grid data. The specific category of an IS reveals its intended use or the nature of its collector. Valid
        categories include VIS for visible imagery, SL for side-looking radar, TI for thermal infrared, FL for
        forward looking infrared, RD for radar, EO for electro-optical, OP for optical, HR for high
        resolution radar, HS for hyperspectral, CP for color frame photography, BP for black/white frame
        photography, SAR for synthetic aperture radar, SARIQ for SAR radio hologram, IR for infrared,
        MS for multispectral, FP for fingerprints, MRI for magnetic resonance imagery, XRAY for x-rays,
        CAT for CAT scans, VD for video, BARO for barometric pressure, CURRENT for water current,
        DEPTH for water depth, and WIND for air wind charts. Valid categories for geographic products or
        geo-reference support data are MAP for raster maps, PAT for color patch, LEG for legends, DTEM for
        elevation models, MATR for other types of matrix data, and LOCG for location grids. This field
        should be used in conjunction with the ISUBCATn field to interpret the significance of each band in the
        image."

        @return the image category
    */
    public final ImageCategory getImageCategory() {
        return imageCategory;
    }

    /**
        Set actual bits-per-pixel per band (ABPP) for the image.
        <p>
        "This field shall contain the number of significant bits for the value in each band of each pixel without
        compression. Even when the image is compressed, ABPP contains the number of significant bits per
        pixel that were present in the image before compression. This field shall be less than or equal
        to Number of Bits Per Pixel (field NBPP). The number of adjacent bits within each NBPP is used
        to represent the value. These representation bits shall be left justified or right justified within the bits
        of the NBPP bits field, according to the value in the PJUST field. For example, if 11-bit pixels are
        stored in 16 bits, this field shall contain 11 and NBPP shall contain 16. The default number of
        significant bits to be used is the value contained in NBPP."

        @param abpb number of actual bits-per-pixel per band
    */
    public final void setActualBitsPerPixelPerBand(final int abpb) {
        actualBitsPerPixelPerBand = abpb;
    }

    /**
        Return the actual bits-per-pixel per band (ABPP) for the image.
        <p>
        "This field shall contain the number of significant bits for the value in each band of each pixel without
        compression. Even when the image is compressed, ABPP contains the number of significant bits per
        pixel that were present in the image before compression. This field shall be less than or equal
        to Number of Bits Per Pixel (field NBPP). The number of adjacent bits within each NBPP is used
        to represent the value. These representation bits shall be left justified or right justified within the bits
        of the NBPP bits field, according to the value in the PJUST field. For example, if 11-bit pixels are
        stored in 16 bits, this field shall contain 11 and NBPP shall contain 16. The default number of
        significant bits to be used is the value contained in NBPP."

        @return number of actual bits-per-pixel per band
    */
    public final int getActualBitsPerPixelPerBand() {
        return actualBitsPerPixelPerBand;
    }

    public final void setPixelJustification(final PixelJustification justification) {
        pixelJustification = justification;
    }

    public final PixelJustification getPixelJustification() {
        return pixelJustification;
    }

    public final void setImageCoordinatesRepresentation(final ImageCoordinatesRepresentation representation) {
        imageCoordinatesRepresentation = representation;
    }

    public final ImageCoordinatesRepresentation getImageCoordinatesRepresentation() {
        return imageCoordinatesRepresentation;
    }

    public final void addImageComment(final String imageComment) {
        imageComments.add(imageComment);
    }

    public final int getNumberOfImageComments() {
        return imageComments.size();
    }

    public final String getImageComment(final int commentNumber) {
        return getImageCommentZeroBase(commentNumber - 1);
    }

    public final String getImageCommentZeroBase(final int commentNumberZeroBase) {
        return imageComments.get(commentNumberZeroBase);
    }

    public final void setImageCompression(final ImageCompression compression) {
        imageCompression = compression;
    }

    public final ImageCompression getImageCompression() {
        return imageCompression;
    }

    public final void setCompressionRate(final String rate) {
        compressionRate = rate;
    }

    public final String getCompressionRate() {
        return compressionRate;
    }

    public final int getNumBands() {
        return imageBands.size();
    }

    public final void addImageBand(final NitfImageBand imageBand) {
        imageBands.add(imageBand);
    }
    public final NitfImageBand getImageBand(final int bandNumber) {
        return getImageBandZeroBase(bandNumber - 1);
    }

    public final NitfImageBand getImageBandZeroBase(final int bandNumberZeroBase) {
        return imageBands.get(bandNumberZeroBase);
    }

    public final void setImageMode(final ImageMode mode) {
        imageMode = mode;
    }

    public final ImageMode getImageMode() {
        return imageMode;
    }

    public final void setNumberOfBlocksPerRow(final int numberOfBlocksPerRow) {
        numBlocksPerRow = numberOfBlocksPerRow;
    }

    public final int getNumberOfBlocksPerRow() {
        return numBlocksPerRow;
    }

    public final void setNumberOfBlocksPerColumn(final int numberOfBlocksPerColumn) {
        numBlocksPerColumn = numberOfBlocksPerColumn;
    }

    public final int getNumberOfBlocksPerColumn() {
        return numBlocksPerColumn;
    }

    public final void setNumberOfPixelsPerBlockHorizontal(final int numberOfPixelsPerBlockHorizontal) {
        numPixelsPerBlockHorizontal = numberOfPixelsPerBlockHorizontal;
    }

    public final int getNumberOfPixelsPerBlockHorizontal() {
        return numPixelsPerBlockHorizontal;
    }

    public final void setNumberOfPixelsPerBlockVertical(final int numberOfPixelsPerBlockVertical) {
        numPixelsPerBlockVertical = numberOfPixelsPerBlockVertical;
    }

    public final int getNumberOfPixelsPerBlockVertical() {
        return numPixelsPerBlockVertical;
    }

    public final void setNumberOfBitsPerPixelPerBand(final int numberOfBitsPerPixelPerBand) {
        numBitsPerPixelPerBand = numberOfBitsPerPixelPerBand;
    }

    public final int getNumberOfBitsPerPixelPerBand() {
        return numBitsPerPixelPerBand;
    }

    public final void setImageDisplayLevel(final int displayLevel) {
        imageDisplayLevel = displayLevel;
    }

    public final int getImageDisplayLevel() {
        return imageDisplayLevel;
    }

    public final void setImageAttachmentLevel(final int attachmentLevel) {
        imageAttachmentLevel = attachmentLevel;
    }

    public final int getImageAttachmentLevel() {
        return imageAttachmentLevel;
    }

    public final void setImageLocationRow(final int locationRow) {
        imageLocationRow = locationRow;
    }

    public final int getImageLocationRow() {
        return imageLocationRow;
    }

    public final void setImageLocationColumn(final int locationColumn) {
        imageLocationColumn = locationColumn;
    }

    public final int getImageLocationColumn() {
        return imageLocationColumn;
    }

    public final void setImageMagnification(final String magnification) {
        imageMagnification = magnification;
    }

    public final String getImageMagnification() {
        return imageMagnification;
    }

    public final double getImageMagnificationAsDouble() {
        if (imageMagnification.startsWith("/")) {
            return 1.0 / Double.parseDouble(imageMagnification.substring("1".length()));
        } else {
            return Double.parseDouble(imageMagnification);
        }
    }

    public final void setImageCoordinates(final ImageCoordinates coordinates) {
        imageCoordinates = coordinates;
    }

    public final ImageCoordinates getImageCoordinates() {
        return imageCoordinates;
    }

    public final void setImageData(final byte[] imageData) {
        data = imageData;
    }

    public final byte[] getImageData() {
        return data;
    }
}
