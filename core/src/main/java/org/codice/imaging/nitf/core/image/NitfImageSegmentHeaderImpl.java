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

import java.util.ArrayList;
import java.util.List;
import org.codice.imaging.nitf.core.AbstractNitfSubSegment;
import org.codice.imaging.nitf.core.common.NitfDateTime;
import org.codice.imaging.nitf.core.PixelJustification;
import org.codice.imaging.nitf.core.PixelValueType;

/**
    Image segment subheader information.
*/
class NitfImageSegmentHeaderImpl extends AbstractNitfSubSegment
        implements NitfImageSegmentHeader {

    private NitfDateTime imageDateTime = null;
    private TargetId imageTargetId = null;
    private String imageIdentifier2 = null;
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
    private final List<String> imageComments = new ArrayList<>();
    private ImageCompression imageCompression = ImageCompression.UNKNOWN;
    private String compressionRate = null;
    private final List<NitfImageBand> imageBands = new ArrayList<>();
    private ImageMode imageMode = ImageMode.UNKNOWN;
    private int numBlocksPerRow = 0;
    private int numBlocksPerColumn = 0;
    private int numPixelsPerBlockHorizontal = 0;
    private int numPixelsPerBlockVertical = 0;
    private int numBitsPerPixelPerBand = 0;
    private int imageDisplayLevel = 0;
    private int imageLocationRow = 0;
    private int imageLocationColumn = 0;
    private int imageUserDefinedHeaderOverflow = 0;
    private String imageMagnification = null;
    private long imageSegmentDataLength = 0;

    private static final int BITS_PER_BYTE = 8;

    /**
        Default constructor.
    */
    public NitfImageSegmentHeaderImpl() {
    }

    /**
        Set the date / time (IDATIM) for the image.
        <p>
        This is supposed to be the date and time that the image was captured.

        @param dateTime the date time for the image.
    */
    public final void setImageDateTime(final NitfDateTime dateTime) {
        imageDateTime = dateTime;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final NitfDateTime getImageDateTime() {
        return imageDateTime;
    }

    /**
        Set the target identifier (TGTID) for the image.
        <p>
        It is common for some or all of the identifier to be filled with default spaces.

        @param targetId the target identifier
    */
    public final void setImageTargetId(final TargetId targetId) {
        imageTargetId = targetId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final TargetId getImageTargetId() {
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
     * {@inheritDoc}
     */
    @Override
    public final String getImageIdentifier2() {
        return imageIdentifier2;
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
     * {@inheritDoc}
     */
    @Override
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
     * {@inheritDoc}
     */
    @Override
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
     * {@inheritDoc}
     */
    @Override
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
     * {@inheritDoc}
     */
    @Override
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
     * {@inheritDoc}
     */
    @Override
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
     * {@inheritDoc}
     */
    @Override
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
     * {@inheritDoc}
     */
    @Override
    public final int getActualBitsPerPixelPerBand() {
        return actualBitsPerPixelPerBand;
    }

    /**
        Set the pixel justification (PJUST) for the image.
        <p>
        "When ABPP is not equal to NBPP, this field indicates whether the significant bits are left justified (L)
        or right justified (R). Nonsignificant bits in each pixel shall contain the binary value 0. Right
        justification is recommended."

        @param justification the justification of the significant bits
    */
    public final void setPixelJustification(final PixelJustification justification) {
        pixelJustification = justification;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final PixelJustification getPixelJustification() {
        return pixelJustification;
    }

    /**
        Set the image coordinate representation (ICORDS) for the image.
        <p>
        For NITF 2.1 / NSIF 1.0: "This field shall contain a valid code indicating the type of
        coordinate representation used for providing an approximate location of the image in the Image
        Geographic Location field (IGEOLO). The valid values for this field are: U = UTM expressed in
        Military Grid Reference System (MGRS) form, N = UTM/UPS (Northern hemisphere), S = UTM/UPS
        (Southern hemisphere), G = GEOGRAPHIC, and D = Decimal degrees. (Choice between N and S is
        based on hemisphere of northernmost point.) The default Geodetic reference system is WGS84
        (appendix B, paragraph B.4.12 and figure B-1). If no coordinate system is identified, the space (BCS
        0x20) shall be used."
        <p>
        For NITF 2.0: "This field shall contain a valid code indicating the geo-referenced coordinate
        system for the image. The valid values for this field are: U=UTM, G=Geodetic (Geographic),
        C=Geocentric, N=None."
        <p>
        Note that those codes are translated into enumerated values to avoid the ambiguity associated with N,
        however the valid representations do differ between NITF 2.1 / NSIF 1.0 and NITF 2.0.

        @param representation the image coordinate representation
    */
    public final void setImageCoordinatesRepresentation(final ImageCoordinatesRepresentation representation) {
        imageCoordinatesRepresentation = representation;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final ImageCoordinatesRepresentation getImageCoordinatesRepresentation() {
        return imageCoordinatesRepresentation;
    }

    /**
        Add an image comment to the image.

        @param imageComment the image comment to add
    */
    public final void addImageComment(final String imageComment) {
        imageComments.add(imageComment);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final List<String> getImageComments() {
        return imageComments;
    }

    /**
        Set the image compression format (IC) for the image.
        <p>
        For NITF 2.1 / NSIF 1.0: "This field shall contain a valid
        code indicating the form of compression used in
        representing the image data. Valid values for this
        field are, C1 to represent bi-level, C3 to represent
        JPEG, C4 to represent Vector Quantization, C5 to
        represent lossless JPEG, I1 to represent down
        sampled JPEG, and NC to represent the image is
        not compressed. Also valid are M1, M3, M4, and
        M5 for compressed images, and NM for
        uncompressed images indicating an image that
        contains a block mask and/or a pad pixel mask. C6
        and M6 are reserved values that will represent a
        future correlated multicomponent compression
        algorithm. C7 and M7 are reserved values that will
        represent a future complex SAR compression. C8
        and M8 are the values for ISO standard
        compression JPEG 2000. The format of a mask
        image is identical to the format of its corresponding
        non-masked image except for the presence of an
        Image Data Mask at the beginning of the image data
        area. The format of the Image Data Mask is
        described in paragraph 5.4.3.2 and is shown in table
        A-3(A). The definitions of the compression
        schemes associated with codes C1/M1, C3/M3,
        C4/M4, and C5/M5 are given, respectively, in ITU-
        T T.4, AMD2, MIL-STD-188-198A, MIL-STD-
        188-199, and NGA N0106-97. C1 is found in ITU-
        T T.4 AMD2, C3 is found in MIL-STD-188-198A,
        C4 = is found in MIL-STD-188-199, and C5 and I1
        are found in NGA N0106-97. (NOTE: C2
        (ARIDPCM) is not valid in NITF 2.1.) The
        definition of the compression scheme associated
        with codes C8/M8 is found in ISO/IEC 15444-
        1:2000 (with amendments 1 and 2)."
        <p>
        For NITF 2.0: "This field shall contain a valid code indicating the form of compression used in
        representing the image data. Valid values for this field are C0, to mean
        compressed with a user specified algorithm, C1 to mean bi-level, C2 to mean
        ARIDPCM, C3 to mean JPEG, C4 to mean Vector Quantization and NC to mean
        the image is not compressed. Also valid are the codes M0, M3 and M4 for
        compressed images, and NM for uncompressed images, indicating a blocked
        image that contains a block mask and/or a transparent pixel mask. The format of
        a mask image is identical to the format of its corresponding non-masked image,
        except for the presence of an Image Data Mask Subheader at the beginning of
        the image data area. The format of the Image Data Mask Subheader is described
        in 5.5.1.5 and is shown in Table IV(A). The definitions of the compression
        schemes associated with codes C1, C2, C3, and C4 are given, respectively, in
        MIL-STD-188-196, MIL-STD-188-197A, MIL-STD-188-198A, and MIL-STD-
        188-199. This field shall not contain C1 or C2 if NBANDS > 1 or NBLOCKS > 1."
        <p>
        Obviously this needs to be consistent with the actual format of the data.

        @param compression the compression format used
    */
    public final void setImageCompression(final ImageCompression compression) {
        imageCompression = compression;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final ImageCompression getImageCompression() {
        return imageCompression;
    }

    /**
        Set the compression rate (COMRAT) for the image.
        <p>
        For NITF 2.1 / NSIF 1.0: "Compression Rate Code. If the IC field contains
        C1, C3, C4, C5, C8, M1, M3, M4, M5, M8, or I1,
        this field shall be present and contain a code
        indicating the compression rate for the image.
        If the value in IC is C1 or M1, the valid codes are
        1D, 2DS, and 2DH, where:
        1D represents One-dimensional Coding;
        2DS represents Two-dimensional Coding, Standard Vertical Resolution (K=2);
        2DH represents Two-dimensional Coding High Vertical Resolution (K=4);
        Explanation of these codes can be found in ITU-T T.4, AMD2.
        If the value in IC is C3, M3, C5, M5, or I1, the
        value of the field shall identify the embedded
        quantization table(s) used by the JPEG compression
        algorithm. In this case, the format of this field is
        XX.Y where XX is the image data type, and Y
        represents the quality level 1 to 5. The image data
        types are represented by:
        00 represents General Purpose;
        01 represents VIS;
        02 represents IR;
        03 represents SAR;
        04 represents Downsample (DS) JPEG;
        Explanation of the optimized tables can be found in
        MIL-STD-188-198A and NGA N0106-97. The
        value of Y shall be 0 if customized tables are used.
        It is optional but highly recommended that the value
        of XX still be used for the image type with
        customized tables.
        If the value of IC is C5 or M5, then the value of Y
        shall be 0. It is optional but highly recommended
        that the value of XX still be used for the image
        type.
        If the value in IC is C4 or M4, this field shall
        contain a value given in the form n.nn representing
        the number of bits-per-pixel for the compressed
        image. Explanation of the compression rate for
        vector quantization can be found in MIL-STD-188-
        199."
        <p>
        For NITF 2.0: "If the Image Compression (IC) field contains C0, C1, C2, C3, C4, M0, M3, or
        M4, this field shall be present and contain a code indicating the compression rate
        for the image. If the value in IC is C0 or M0, the code shall be user defined but
        shall not be all blanks. If the value in IC is C1 or M1, the valid codes are 1D,
        2DS, and 2DH, where: 1D means one Dimensional Coding; 2DS means two Dimensional Coding
        Standard Vertical Resolution, K=2; and 2DH means two Dimensional Coding High Vertical Resolution, K=4.
        Explanation of these codes can be found in MIL-STD-188-196. If the value in
        IC is C2 or M2, this field shall contain a value given in the form n.nn
        representing the number of bits-per-pixel for the compressed image. Explanation
        of the compression rate for vector quantization can be found in MIL-STD-188-199.
        Valid codes in this case are 0.75, 1.40, 2.30, and 4.50. Explanation of
        these codes can be found in MIL-STD-188-197A. If the value in IC is C3 or
        M3, this field is used to identify the default quantization table(s) used by the
        JPEG compression algorithm. In this case, the format of this field is XX.Y
        where XX is the image data type (00 = general purpose, 01 through 99 are
        reserved), and Y represents the quality level 1 through 5. Explanation of these
        codes can be found in MIL-STD-188-198A. If the value in IC is C4 or M4, this
        field shall contain a value given in the form n.nn representing the number of bits-
        per-pixel for the compressed image. Explanation of the compression rate for
        vector quantization can be found in MIL-STD-188-199. This field is omitted if
        the value in IC is NC or NM."

        @param rate string representation of the compression rate
    */
    public final void setCompressionRate(final String rate) {
        compressionRate = rate;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String getCompressionRate() {
        return compressionRate;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final int getNumBands() {
        return imageBands.size();
    }

    /**
        Add an image band to the image.

        @param imageBand the image band to add
    */
    public final void addImageBand(final NitfImageBand imageBand) {
        imageBands.add(imageBand);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final NitfImageBand getImageBand(final int bandNumber) {
        return getImageBandZeroBase(bandNumber - 1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final NitfImageBand getImageBandZeroBase(final int bandNumberZeroBase) {
        return imageBands.get(bandNumberZeroBase);
    }

    /**
        Set the image mode (IMODE) for the image.
        <p>
        "This field shall indicate how the
        Image Pixels are stored in the NITF file. Valid
        values are B, P, R, and S. The interpretation of
        IMODE is dependent on whether the image is JPEG
        compressed (IC = C3, C5, I1, M3, or M5), VQ
        compressed (IC = C4, or M4), or uncompressed (IC
        = NC or NM).
        a. Uncompressed. The value S indicates band
        sequential, where all blocks for the first band
        are followed by all blocks for the second band,
        and so on: [(block1, band1), (block2, band1), ...
        (blockM, band1)], [(block1, band2), (block2,
        band 2), ... (blockM, band2)] ... [(block1,
        bandN), (block2, bandN), ... (blockM,
        bandN)]. Note that, in each block, the pixels of
        the first line appears first, followed by the
        pixels of the second line, and so on.
        The value B indicates band interleaved by
        block. This implies that within each block, the
        bands follow one another: [(block1, band1),
        (block1, band2), ...(block1, bandN)], [(block2,
        band1), (block2, band2), ... (block2, bandN)],
        ... [(blockM, band1), (blockM, band2), ...
        (blockM, bandN)]. Note that, in each block,
        the pixels of the first line appears first and the
        pixels of the last line appears last.
        The value P indicates band interleaved by pixel
        within each block: such as, for each block, one
        after the other, the full pixel vector (all band
        values) appears for every pixel in the block, one
        pixel after another, the block column index
        varying faster than the block row index.
        The value R indicates band interleaved by row.
        The ordering mechanism for this case stores the
        pixel values of each band in row sequential
        order. Within each block, all pixel values of the
        first row of the first band are followed by pixel
        values of the first row of the second band
        continuing until all values of the first row are
        stored The remaining rows are stored in a similar
        fashion until the last row of values has been
        stored. Each block shall be zero-filled to the
        next octet boundary when necessary.
        If the value of the NBANDS field is 1, the cases
        B and S coincide. In this case, this field shall
        contain B. If the Number of Blocks is 1 (the
        NBPR field and the NBPC field contain 1), this
        field shall contain B for non-interleaved by
        pixel, and P for interleaved by pixel. The value
        S is only valid for images with multiple blocks
        and multiple bands.
        b. JPEG-compressed. The presence of B, P, or
        S implies specific ordering of data within the
        JPEG image data representation. For this case
        the interpretation of the various values of the
        IMODE field is specified in MIL-STD-188-
        198A. When IC contains C8, M8, or I1,
        IMODE contains B.
        c. Vector Quantization compressed. VQ
        compressed images are normally either RGB
        with a color look-up table or monochromatic. In
        either case, the image is single band, and the
        IMODE field defaults to B.
        d. Bi-Level Compressed. When the value of the
        IC field is C1 or M1, the value of the IMODE
        field is B."

        @param mode the image mode
    */
    public final void setImageMode(final ImageMode mode) {
        imageMode = mode;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final ImageMode getImageMode() {
        return imageMode;
    }

    /**
        Set the number of blocks per row (NBPR).
        <p>
        From MIL-STD-2500C: "This field shall
        contain the number of image blocks in a row of
        blocks (paragraph 5.4.2.2) in the horizontal
        direction. If the image consists of only a single
        block, this field shall contain the value one."

        @param numberOfBlocksPerRow the number of blocks per row.
    */
    public final void setNumberOfBlocksPerRow(final int numberOfBlocksPerRow) {
        numBlocksPerRow = numberOfBlocksPerRow;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final int getNumberOfBlocksPerRow() {
        return numBlocksPerRow;
    }

    /**
        Set the number of blocks per column (NBPC).
        <p>
        From MIL-STD-2500C: "This field shall
        contain the number of image blocks in a column of
        blocks (paragraph 5.4.2.2) in the vertical direction.
        If the image consists of only a single block, this
        field shall contain the value one."

        @param numberOfBlocksPerColumn the number of blocks per column.
    */
    public final void setNumberOfBlocksPerColumn(final int numberOfBlocksPerColumn) {
        numBlocksPerColumn = numberOfBlocksPerColumn;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final int getNumberOfBlocksPerColumn() {
        return numBlocksPerColumn;
    }

    /**
        Set the number of pixels per block - horizontal direction (NPPBH).
        <p>
        From MIL-STD-2500C: "This field
        shall contain the number of pixels horizontally in
        each block of the image. It shall be the case that the
        product of the values of the NBPR field and the
        NPPBH field is greater than or equal to the value of
        the NCOLS field (NBPR * NPPBH ≥ NCOLS).
        When NBPR is "0001", setting the NPPBH value to
        "0000" designates that the number of pixels
        horizontally is specified by the value in NCOLS."

        @param numberOfPixelsPerBlockHorizontal the number of pixels per block, horizontal.
    */
    public final void setNumberOfPixelsPerBlockHorizontal(final int numberOfPixelsPerBlockHorizontal) {
        numPixelsPerBlockHorizontal = numberOfPixelsPerBlockHorizontal;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final int getNumberOfPixelsPerBlockHorizontal() {
        return numPixelsPerBlockHorizontal;
    }

    /**
        Set the number of pixels per block - vertical direction (NPPBV).
        <p>
        From MIL-STD-2500C: "This field
        shall contain the number of pixels vertically in each
        block of the image. It shall be the case that the
        product of the values of the NBPC field and the
        NPPBV field is greater than or equal to the value of
        the NROWS field (NBPC * NPPBV ≥ NROWS).
        When NBPC is "0001", setting the NPPBV value to
        "0000" designates that the number of pixels
        vertically is specified by the value in NROWS."

        @param numberOfPixelsPerBlockVertical the number of pixels per block, vertical.
    */
    public final void setNumberOfPixelsPerBlockVertical(final int numberOfPixelsPerBlockVertical) {
        numPixelsPerBlockVertical = numberOfPixelsPerBlockVertical;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final int getNumberOfPixelsPerBlockVertical() {
        return numPixelsPerBlockVertical;
    }

    /**
        Set the number of bits per pixel per band (NBPP).
        <p>
        From MIL-STD-2500C: "If IC contains
        NC, NM, C4, or M4, this field shall contain the
        number of storage bits used for the value from each
        component of a pixel vector. The value in this field
        always shall be greater than or equal to Actual Bits
        Per Pixel (ABPP). For example, if 11-bit pixels are
        stored in 16 bits, this field shall contain 16 and
        Actual Bits Per Pixel shall contain 11. If IC = C3,
        M3, C5, M5, or I1 this field shall contain the value
        8 or the value 12. If IC = C1, this field shall
        contain the value 1. If IC = C8 or M8, this field
        shall contain the number of bits of precision (01-38)
        used in the JPEG 2000 compression of the data."

        @param numberOfBitsPerPixelPerBand the number of bits per pixel per band.
    */
    public final void setNumberOfBitsPerPixelPerBand(final int numberOfBitsPerPixelPerBand) {
        numBitsPerPixelPerBand = numberOfBitsPerPixelPerBand;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final int getNumberOfBitsPerPixelPerBand() {
        return numBitsPerPixelPerBand;
    }

    /**
        Set the image display level (IDLVL).
        <p>
        From MIL-STD-2500C: "This field shall contain a
        valid value that indicates the display level of the
        image relative to other displayed file components in
        a composite display. The valid values are 001 to
        999. The display level of each displayable segment
        (image or graphic) within a file shall be unique; that
        is, each number from 001 to 999 is the display level
        of, at most, one segment. Display level is discussed
        in paragraph 5.3.3. The image or graphic segment
        in the file having the minimum display level shall
        have attachment level 0 (ALVL000) (BCS zeros
        (code 0x30))."
        <p>
        Note that explanation mixes display level and attachment level.

        @param displayLevel the display level (integer format).
    */
    public final void setImageDisplayLevel(final int displayLevel) {
        imageDisplayLevel = displayLevel;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final int getImageDisplayLevel() {
        return imageDisplayLevel;
    }

    /**
        Set the image location row (part of ILOC).
        <p>
        From MIL-STD-2500C: "The image location is the location
        of the first pixel of the first line of the image. This
        field shall contain the image location offset from
        the ILOC or SLOC value of the segment to which
        the image is attached or from the origin of the CCS
        when the image is unattached (IALVL contains
        000). A row or column value of 00000 indicates no
        offset. Positive row and column values indicate
        offsets down and to the right while negative row
        and column values indicate offsets up and to the
        left."

        @param locationRow the image location row number
    */
    public final void setImageLocationRow(final int locationRow) {
        imageLocationRow = locationRow;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final int getImageLocationRow() {
        return imageLocationRow;
    }

    /**
        Set the image location column (part of ILOC).
        <p>
        From MIL-STD-2500C: "The image location is the location
        of the first pixel of the first line of the image. This
        field shall contain the image location offset from
        the ILOC or SLOC value of the segment to which
        the image is attached or from the origin of the CCS
        when the image is unattached (IALVL contains
        000). A row or column value of 00000 indicates no
        offset. Positive row and column values indicate
        offsets down and to the right while negative row
        and column values indicate offsets up and to the
        left."

        @param locationColumn the image location column number
    */
    public final void setImageLocationColumn(final int locationColumn) {
        imageLocationColumn = locationColumn;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final int getImageLocationColumn() {
        return imageLocationColumn;
    }

    /**
        Set the image magnification (IMAG).
        <p>
        From MIL-STD-2500C: "This field shall contain the
        magnification (or reduction) factor of the image
        relative to the original source image. Decimal
        values are used to indicate magnification, and
        decimal fraction values indicate reduction. For
        example, “2.30” indicates the original image has
        been magnified by a factor of “2.30,” while “0.5”
        indicates the original image has been reduced by a
        factor of 2. The default value is 1.0, indicating no
        magnification or reduction. In addition, the
        reductions can be represented as reciprocals of any
        non-negative integer: /2 (for 1/2), /3 (for 1/3), /4
        (for 1/4), /5 (for 1/5), through /999 (for 1/999). The
        values are left justified and BCS spaces (0x20)
        filled to the right."

        @param magnification the magnification level
    */
    public final void setImageMagnification(final String magnification) {
        imageMagnification = magnification;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String getImageMagnification() {
        return imageMagnification;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final double getImageMagnificationAsDouble() {
        if (imageMagnification.startsWith("/")) {
            return 1.0 / Double.parseDouble(imageMagnification.substring("1".length()));
        } else {
            return Double.parseDouble(imageMagnification);
        }
    }

    /**
        Set the image coordinates (IGEOLO) for the image.
        <p>
        From MIL-STD-2500C: "This field, when
        present, shall contain an approximate geographic
        location which is not intended for analytical
        purposes (e.g., targeting, mensuration, distance
        calculation); it is intended to support general user
        appreciation for the image location (e.g.,
        cataloguing)."

        @param coordinates the coordinates value.
    */
    public final void setImageCoordinates(final ImageCoordinates coordinates) {
        imageCoordinates = coordinates;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final ImageCoordinates getImageCoordinates() {
        return imageCoordinates;
    }

    /**
        Set the user defined header overflow (UDHOFL) for the image.
        <p>
        This is the (1-base) index of the TRE into which user defined header data
        overflows.

        @param overflow the user defined header overflow index
    */
    public final void setUserDefinedHeaderOverflow(final int overflow) {
        imageUserDefinedHeaderOverflow = overflow;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final int getUserDefinedHeaderOverflow() {
        return imageUserDefinedHeaderOverflow;
    }

    /**
        Set the image data length.
        <p>
        This is the length of the contents of the associated data segment.

        @param length the image data segment length, in bytes
    */
    public final void setImageSegmentDataLength(final long length) {
        imageSegmentDataLength = length;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final long getImageDataLength() {
        return imageSegmentDataLength;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final long getNumberOfBytesPerBlock() {
        long numberOfPixelsPerBlockPerBand = getNumberOfPixelsPerBlockHorizontal() * getNumberOfPixelsPerBlockVertical();
        long numberOfPixelsPerBlock = numberOfPixelsPerBlockPerBand * getNumBands();
        long numberOfBytesPerBlock = numberOfPixelsPerBlock * getNumberOfBitsPerPixelPerBand() / BITS_PER_BYTE;
        return numberOfBytesPerBlock;
    }
}
