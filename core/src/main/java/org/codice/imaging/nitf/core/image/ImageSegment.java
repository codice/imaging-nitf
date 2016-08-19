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

import java.util.List;
import javax.imageio.stream.ImageInputStream;
import org.codice.imaging.nitf.core.common.CommonBasicSegment;
import org.codice.imaging.nitf.core.common.DateTime;

/**
 Image segment information.
 */
public interface ImageSegment extends CommonBasicSegment {

    /**
     Return the date / time (IDATIM) for the image.
     <p>
     This is supposed to be the date and time that the image was captured.

     @return the date / time that the image was captured, or null if unknown.
     */
    DateTime getImageDateTime();

    /**
     Return the target identifier (TGTID) for the image.
     <p>
     It is common for some or all of the identifier to be filled with default spaces.

     @return the target identifier
     */
    TargetId getImageTargetId();

    /**
     Return the second image identifier (IID2) for the image.
     <p>
     "This field can contain the identification of additional information about the image."

     In NITF 2.0 files, this is the image title (ITITLE) field.

     @return the identifier
     */
    String getImageIdentifier2();

    /**
     Return the image source (ISORCE) for the image.
     <p>
     "This field shall contain a description of the source of the image. If the source of the data is
     classified, then the description shall be preceded by the classification, including codeword(s)
     contained in table A-4. If this field is all spaces (0x20), it shall imply that no image source
     data applies."

     @return the image source
     */
    String getImageSource();

    /**
     * Returns the number of significant rows (NROWS) in the image.
     *
     * <p>
     * "This field shall contain the total number of rows of significant pixels in the image. When the product of the
     * values of the NPPBV field and the NBPC field is greater than the value of the NROWS field (NPPBV * NBPC &gt;
     * NROWS), the rows indexed with the value of the NROWS field to (NPPBV * NBPC) minus 1 shall contain fill data.
     * NOTE: Only the rows indexed 0 to the value of the NROWS field minus 1 of the image contain significant data. The
     * pixel fill values are determined by the application."
     *
     * @return the number of significant rows
     */
    long getNumberOfRows();

    /**
     * Returns the number of significant columns (NCOLS) in the image.
     * <p>
     * "This field shall contain the total number of columns of significant pixels in the image. When the product of the
     * values of the NPPBH field and the NBPR field is greater than the NCOLS field (NPPBH * NBPR &gt; NCOLS), the
     * columns indexed with the value of the NCOLS field to (NPPBH * NBPR) minus 1 shall contain fill data. NOTE: Only
     * the columns indexed 0 to the value of the NCOLS field minus 1 of the image contain significant data. The pixel
     * fill values are determined by the application."
     *
     * @return the number of significant columns
     */
    long getNumberOfColumns();

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
    PixelValueType getPixelValueType();

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
    ImageRepresentation getImageRepresentation();

    /**
     * Return the image category (ICAT) for the image.
     * <p>
     * "This field shall contain a valid indicator of the specific category of image, raster or grid data. The specific
     * category of an IS reveals its intended use or the nature of its collector. Valid categories include VIS for
     * visible imagery, SL for side-looking radar, TI for thermal infrared, FL for forward looking infrared, RD for
     * radar, EO for electro-optical, OP for optical, HR for high resolution radar, HS for hyperspectral, CP for color
     * frame photography, BP for black/white frame photography, SAR for synthetic aperture radar, SARIQ for SAR radio
     * hologram, IR for infrared, MS for multispectral, FP for fingerprints, MRI for magnetic resonance imagery, XRAY
     * for x-rays, CAT for CAT scans, VD for video, BARO for barometric pressure, CURRENT for water current, DEPTH for
     * water depth, and WIND for air wind charts. Valid categories for geographic products or geo-reference support data
     * are MAP for raster maps, PAT for color patch, LEG for legends, DTEM for elevation models, MATR for other types of
     * matrix data, and LOCG for location grids. This field should be used in conjunction with the ISUBCATn field to
     * interpret the significance of each band in the image."
     *
     * @return the image category
     */
    ImageCategory getImageCategory();

    /**
     * Set the image category (ICAT) for the image.
     * <p>
     * "This field shall contain a valid indicator of the specific category of image, raster or grid data. The specific
     * category of an IS reveals its intended use or the nature of its collector. Valid categories include VIS for
     * visible imagery, SL for side-looking radar, TI for thermal infrared, FL for forward looking infrared, RD for
     * radar, EO for electro-optical, OP for optical, HR for high resolution radar, HS for hyperspectral, CP for color
     * frame photography, BP for black/white frame photography, SAR for synthetic aperture radar, SARIQ for SAR radio
     * hologram, IR for infrared, MS for multispectral, FP for fingerprints, MRI for magnetic resonance imagery, XRAY
     * for x-rays, CAT for CAT scans, VD for video, BARO for barometric pressure, CURRENT for water current, DEPTH for
     * water depth, and WIND for air wind charts. Valid categories for geographic products or geo-reference support data
     * are MAP for raster maps, PAT for color patch, LEG for legends, DTEM for elevation models, MATR for other types of
     * matrix data, and LOCG for location grids. This field should be used in conjunction with the ISUBCATn field to
     * interpret the significance of each band in the image."
     *
     * @param category the image category
     */
    void setImageCategory(final ImageCategory category);

    /**
     * Set actual bits-per-pixel per band (ABPP) for the image.
     * <p>
     * "This field shall contain the number of significant bits for the value in each band of each pixel without
     * compression. Even when the image is compressed, ABPP contains the number of significant bits per pixel that were
     * present in the image before compression. This field shall be less than or equal to Number of Bits Per Pixel
     * (field NBPP). The number of adjacent bits within each NBPP is used to represent the value. These representation
     * bits shall be left justified or right justified within the bits of the NBPP bits field, according to the value in
     * the PJUST field. For example, if 11-bit pixels are stored in 16 bits, this field shall contain 11 and NBPP shall
     * contain 16. The default number of significant bits to be used is the value contained in NBPP."
     *
     * @param abpb number of actual bits-per-pixel per band
     */
    void setActualBitsPerPixelPerBand(final int abpb);

    /**
     * Return the actual bits-per-pixel per band (ABPP) for the image.
     * <p>
     * "This field shall contain the number of significant bits for the value in each band of each pixel without
     * compression. Even when the image is compressed, ABPP contains the number of significant bits per pixel that were
     * present in the image before compression. This field shall be less than or equal to Number of Bits Per Pixel
     * (field NBPP). The number of adjacent bits within each NBPP is used to represent the value. These representation
     * bits shall be left justified or right justified within the bits of the NBPP bits field, according to the value in
     * the PJUST field. For example, if 11-bit pixels are stored in 16 bits, this field shall contain 11 and NBPP shall
     * contain 16. The default number of significant bits to be used is the value contained in NBPP."
     *
     * @return number of actual bits-per-pixel per band
     */
    int getActualBitsPerPixelPerBand();

    /**
     * Set the pixel justification (PJUST) for the image.
     * <p>
     * "When ABPP is not equal to NBPP, this field indicates whether the significant bits are left justified (L) or
     * right justified (R). Nonsignificant bits in each pixel shall contain the binary value 0. Right justification is
     * recommended."
     *
     * @param justification the justification of the significant bits
     */
    void setPixelJustification(final PixelJustification justification);

    /**
     * Return the pixel justification (PJUST) for the image.
     * <p>
     * "When ABPP is not equal to NBPP, this field indicates whether the significant bits are left justified (L) or
     * right justified (R). Nonsignificant bits in each pixel shall contain the binary value 0. Right justification is
     * recommended."
     *
     * @return the justification of the significant bits
     */
    PixelJustification getPixelJustification();

    /**
     * Set the image coordinate representation (ICORDS) for the image.
     * <p>
     * For NITF 2.1 / NSIF 1.0: "This field shall contain a valid code
     * indicating the type of coordinate representation used for providing an
     * approximate location of the image in the Image Geographic Location field
     * (IGEOLO). The valid values for this field are: U = UTM expressed in
     * Military Grid Reference System (MGRS) form, N = UTM (Northern
     * hemisphere), S = UTM (Southern hemisphere), P = UPS (north or south polar
     * regions), G = GEOGRAPHIC, and D = Decimal degrees. (Choice between N and
     * S is based on hemisphere of northernmost point.) The default Geodetic
     * reference system is WGS84 (appendix B, paragraph B.4.12 and figure B-1).
     * If no coordinate system is identified, the space (BCS 0x20) shall be
     * used."
     * <p>
     * For NITF 2.0: "This field shall contain a valid code indicating the
     * geo-referenced coordinate system for the image. The valid values for this
     * field are: U=UTM, G=Geodetic (Geographic), C=Geocentric, N=None."
     * <p>
     * Note that those codes are translated into enumerated values to avoid the
     * ambiguity associated with N, however the valid representations do differ
     * between NITF 2.1 / NSIF 1.0 and NITF 2.0.
     *
     * @param representation the image coordinate representation
     */
    void setImageCoordinatesRepresentation(final ImageCoordinatesRepresentation representation);

    /**
     * Return the image coordinate representation (ICORDS) for the image.
     * <p>
     * For NITF 2.1 / NSIF 1.0: "This field shall contain a valid code
     * indicating the type of coordinate representation used for providing an
     * approximate location of the image in the Image Geographic Location field
     * (IGEOLO). The valid values for this field are: U = UTM expressed in
     * Military Grid Reference System (MGRS) form, N = UTM (Northern
     * hemisphere), S = UTM (Southern hemisphere), P = UPS (north or south polar
     * regions), G = GEOGRAPHIC, and D = Decimal degrees. (Choice between N and
     * S is based on hemisphere of northernmost point.) The default Geodetic
     * reference system is WGS84 (appendix B, paragraph B.4.12 and figure B-1).
     * If no coordinate system is identified, the space (BCS 0x20) shall be
     * used."
     * <p>
     * For NITF 2.0: "This field shall contain a valid code indicating the
     * geo-referenced coordinate system for the image. The valid values for this
     * field are: U=UTM, G=Geodetic (Geographic), C=Geocentric, N=None."
     *
     * @return the image coordinate representation
     */
    ImageCoordinatesRepresentation getImageCoordinatesRepresentation();

    /**
     * Add an image comment to the image.
     *
     * @param imageComment the image comment to add
     */
    void addImageComment(final String imageComment);

    /**
     * Return the image comments attached to this image.
     *
     * @return the image comments
     */
    List<String> getImageComments();

    /**
     * Set the image compression format (IC) for the image.
     * <p>
     * For NITF 2.1 / NSIF 1.0: "This field shall contain a valid code indicating the form of compression used in
     * representing the image data. Valid values for this field are, C1 to represent bi-level, C3 to represent JPEG, C4
     * to represent Vector Quantization, C5 to represent lossless JPEG, I1 to represent down sampled JPEG, and NC to
     * represent the image is not compressed. Also valid are M1, M3, M4, and M5 for compressed images, and NM for
     * uncompressed images indicating an image that contains a block mask and/or a pad pixel mask. C6 and M6 are
     * reserved values that will represent a future correlated multicomponent compression algorithm. C7 and M7 are
     * reserved values that will represent a future complex SAR compression. C8 and M8 are the values for ISO standard
     * compression JPEG 2000. The format of a mask image is identical to the format of its corresponding non-masked
     * image except for the presence of an Image Data Mask at the beginning of the image data area. The format of the
     * Image Data Mask is described in paragraph 5.4.3.2 and is shown in table A-3(A). The definitions of the
     * compression schemes associated with codes C1/M1, C3/M3, C4/M4, and C5/M5 are given, respectively, in ITU-T T.4,
     * AMD2, MIL-STD-188-198A, MIL-STD-188-199, and NGA N0106-97. C1 is found in ITU-T T.4 AMD2, C3 is found in
     * MIL-STD-188-198A, C4 is found in MIL-STD-188-199, and C5 and I1 are found in NGA N0106-97. (NOTE: C2 (ARIDPCM) is
     * not valid in NITF 2.1.) The definition of the compression scheme associated with codes C8/M8 is found in ISO/IEC
     * 15444-1:2000 (with amendments 1 and 2)."
     * <p>
     * For NITF 2.0: "This field shall contain a valid code indicating the form of compression used in representing the
     * image data. Valid values for this field are C0, to mean compressed with a user specified algorithm, C1 to mean
     * bi-level, C2 to mean ARIDPCM, C3 to mean JPEG, C4 to mean Vector Quantization and NC to mean the image is not
     * compressed. Also valid are the codes M0, M3 and M4 for compressed images, and NM for uncompressed images,
     * indicating a blocked image that contains a block mask and/or a transparent pixel mask. The format of a mask image
     * is identical to the format of its corresponding non-masked image, except for the presence of an Image Data Mask
     * Subheader at the beginning of the image data area. The format of the Image Data Mask Subheader is described in
     * 5.5.1.5 and is shown in Table IV(A). The definitions of the compression schemes associated with codes C1, C2, C3,
     * and C4 are given, respectively, in MIL-STD-188-196, MIL-STD-188-197A, MIL-STD-188-198A, and MIL-STD-188-199. This
     * field shall not contain C1 or C2 if NBANDS &gt; 1 or NBLOCKS &gt; 1."
     *
     * <p>
     * Obviously this needs to be consistent with the actual format of the data.
     *
     * @param compression the compression format used
     */
    void setImageCompression(final ImageCompression compression);

    /**
     * Return the image compression format (IC) for the image.
     * <p>
     * For NITF 2.1 / NSIF 1.0: "This field shall contain a valid code indicating the form of compression used in
     * representing the image data. Valid values for this field are, C1 to represent bi-level, C3 to represent JPEG, C4
     * to represent Vector Quantization, C5 to represent lossless JPEG, I1 to represent down sampled JPEG, and NC to
     * represent the image is not compressed. Also valid are M1, M3, M4, and M5 for compressed images, and NM for
     * uncompressed images indicating an image that contains a block mask and/or a pad pixel mask. C6 and M6 are
     * reserved values that will represent a future correlated multicomponent compression algorithm. C7 and M7 are
     * reserved values that will represent a future complex SAR compression. C8 and M8 are the values for ISO standard
     * compression JPEG 2000. The format of a mask image is identical to the format of its corresponding non-masked
     * image except for the presence of an Image Data Mask at the beginning of the image data area. The format of the
     * Image Data Mask is described in paragraph 5.4.3.2 and is shown in table A-3(A). The definitions of the
     * compression schemes associated with codes C1/M1, C3/M3, C4/M4, and C5/M5 are given, respectively, in ITU-T T.4,
     * AMD2, MIL-STD-188-198A, MIL-STD-188-199, and NGA N0106-97. C1 is found in ITU-T T.4 AMD2, C3 is found in
     * MIL-STD-188-198A, C4 is found in MIL-STD-188-199, and C5 and I1 are found in NGA N0106-97. (NOTE: C2 (ARIDPCM) is
     * not valid in NITF 2.1.) The definition of the compression scheme associated with codes C8/M8 is found in ISO/IEC
     * 15444-1:2000 (with amendments 1 and 2)."
     * <p>
     * For NITF 2.0: "This field shall contain a valid code indicating the form of compression used in representing the
     * image data. Valid values for this field are C0, to mean compressed with a user specified algorithm, C1 to mean
     * bi-level, C2 to mean ARIDPCM, C3 to mean JPEG, C4 to mean Vector Quantization and NC to mean the image is not
     * compressed. Also valid are the codes M0, M3 and M4 for compressed images, and NM for uncompressed images,
     * indicating a blocked image that contains a block mask and/or a transparent pixel mask. The format of a mask image
     * is identical to the format of its corresponding non-masked image, except for the presence of an Image Data Mask
     * Subheader at the beginning of the image data area. The format of the Image Data Mask Subheader is described in
     * 5.5.1.5 and is shown in Table IV(A). The definitions of the compression schemes associated with codes C1, C2, C3,
     * and C4 are given, respectively, in MIL-STD-188-196, MIL-STD-188-197A, MIL-STD-188-198A, and MIL-STD-188-199. This
     * field shall not contain C1 or C2 if NBANDS &gt; 1 or NBLOCKS &gt; 1."
     *
     * @return the compression format used
     */
    ImageCompression getImageCompression();

    /**
     * Set the compression rate (COMRAT) for the image.
     * <p>
     * For NITF 2.1 / NSIF 1.0: "Compression Rate Code. If the IC field contains C1, C3, C4, C5, C8, M1, M3, M4, M5, M8,
     * or I1, this field shall be present and contain a code indicating the compression rate for the image. If the value
     * in IC is C1 or M1, the valid codes are 1D, 2DS, and 2DH, where: 1D represents One-dimensional Coding; 2DS
     * represents Two-dimensional Coding, Standard Vertical Resolution (K=2); 2DH represents Two-dimensional Coding High
     * Vertical Resolution (K=4); Explanation of these codes can be found in ITU-T T.4, AMD2.
     *
     * If the value in IC is C3, M3, C5, M5, or I1, then value of the field shall identify the embedded quantization
     * table(s) used by the JPEG compression algorithm. In this case, the format of this field is XX.Y where XX is the
     * image data type, and Y represents the quality level 1 to 5. The image data types are represented by: 00
     * represents General Purpose; 01 represents VIS; 02 represents IR; 03 represents SAR; 04 represents Downsample (DS)
     * JPEG; Explanation of the optimized tables can be found in MIL-STD-188-198A and NGA N0106-97. The value of Y shall
     * be 0 if customized tables are used. It is optional but highly recommended that the value of XX still be used for
     * the image type with customized tables.
     *
     * If the value of IC is C5 or M5, then the value of Y shall be 0. It is optional but highly recommended that the
     * value of XX still be used for the image type.
     *
     * If the value in IC is C4 or M4, this field shall contain a value given in the form n.nn representing the number
     * of bits-per-pixel for the compressed image. Explanation of the compression rate for vector quantization can be
     * found in MIL-STD-188-199."
     * <p>
     * For NITF 2.0: "If the Image Compression (IC) field contains C0, C1, C2, C3, C4, M0, M3, or M4, this field shall
     * be present and contain a code indicating the compression rate for the image. If the value in IC is C0 or M0, the
     * code shall be user defined but shall not be all blanks. If the value in IC is C1 or M1, the valid codes are 1D,
     * 2DS, and 2DH, where: 1D means one Dimensional Coding; 2DS means two Dimensional Coding Standard Vertical
     * Resolution, K=2; and 2DH means two Dimensional Coding High Vertical Resolution, K=4. Explanation of these codes
     * can be found in MIL-STD-188-196. If the value in IC is C2 or M2, this field shall contain a value given in the
     * form n.nn representing the number of bits-per-pixel for the compressed image. Explanation of the compression rate
     * for vector quantization can be found in MIL-STD-188-199. Valid codes in this case are 0.75, 1.40, 2.30, and 4.50.
     * Explanation of these codes can be found in MIL-STD-188-197A. If the value in IC is C3 or M3, this field is used
     * to identify the default quantization table(s) used by the JPEG compression algorithm. In this case, the format of
     * this field is XX.Y where XX is the image data type (00 = general purpose, 01 through 99 are reserved), and Y
     * represents the quality level 1 through 5. Explanation of these codes can be found in MIL-STD-188-198A. If the
     * value in IC is C4 or M4, this field shall contain a value given in the form n.nn representing the number of
     * bits-per-pixel for the compressed image. Explanation of the compression rate for vector quantization can be found
     * in MIL-STD-188-199. This field is omitted if the value in IC is NC or NM."
     *
     * @param rate string representation of the compression rate
     */
    void setCompressionRate(final String rate);

    /**
     * Return the compression rate (COMRAT) for the image.
     * <p>
     * For NITF 2.1 / NSIF 1.0: "Compression Rate Code. If the IC field contains C1, C3, C4, C5, C8, M1, M3, M4, M5, M8,
     * or I1, this field shall be present and contain a code indicating the compression rate for the image. If the value
     * in IC is C1 or M1, the valid codes are 1D, 2DS, and 2DH, where: 1D represents One-dimensional Coding; 2DS
     * represents Two-dimensional Coding, Standard Vertical Resolution (K=2); 2DH represents Two-dimensional Coding High
     * Vertical Resolution (K=4); Explanation of these codes can be found in ITU-T T.4, AMD2.
     *
     * If the value in IC is C3, M3, C5, M5, or I1, then value of the field shall identify the embedded quantization
     * table(s) used by the JPEG compression algorithm. In this case, the format of this field is XX.Y where XX is the
     * image data type, and Y represents the quality level 1 to 5. The image data types are represented by: 00
     * represents General Purpose; 01 represents VIS; 02 represents IR; 03 represents SAR; 04 represents Downsample (DS)
     * JPEG; Explanation of the optimized tables can be found in MIL-STD-188-198A and NGA N0106-97. The value of Y shall
     * be 0 if customized tables are used. It is optional but highly recommended that the value of XX still be used for
     * the image type with customized tables.
     *
     * If the value of IC is C5 or M5, then the value of Y shall be 0. It is optional but highly recommended that the
     * value of XX still be used for the image type.
     *
     * If the value in IC is C4 or M4, this field shall contain a value given in the form n.nn representing the number
     * of bits-per-pixel for the compressed image. Explanation of the compression rate for vector quantization can be
     * found in MIL-STD-188-199."
     * <p>
     * For NITF 2.0: "If the Image Compression (IC) field contains C0, C1, C2, C3, C4, M0, M3, or M4, this field shall
     * be present and contain a code indicating the compression rate for the image. If the value in IC is C0 or M0, the
     * code shall be user defined but shall not be all blanks. If the value in IC is C1 or M1, the valid codes are 1D,
     * 2DS, and 2DH, where: 1D means one Dimensional Coding; 2DS means two Dimensional Coding Standard Vertical
     * Resolution, K=2; and 2DH means two Dimensional Coding High Vertical Resolution, K=4. Explanation of these codes
     * can be found in MIL-STD-188-196. If the value in IC is C2 or M2, this field shall contain a value given in the
     * form n.nn representing the number of bits-per-pixel for the compressed image. Explanation of the compression rate
     * for vector quantization can be found in MIL-STD-188-199. Valid codes in this case are 0.75, 1.40, 2.30, and 4.50.
     * Explanation of these codes can be found in MIL-STD-188-197A. If the value in IC is C3 or M3, this field is used
     * to identify the default quantization table(s) used by the JPEG compression algorithm. In this case, the format of
     * this field is XX.Y where XX is the image data type (00 = general purpose, 01 through 99 are reserved), and Y
     * represents the quality level 1 through 5. Explanation of these codes can be found in MIL-STD-188-198A. If the
     * value in IC is C4 or M4, this field shall contain a value given in the form n.nn representing the number of
     * bits-per-pixel for the compressed image. Explanation of the compression rate for vector quantization can be found
     * in MIL-STD-188-199. This field is omitted if the value in IC is NC or NM."
     *
     * @return string representation of the compression rate
     */
    String getCompressionRate();

    /**
     * Add an image band to the image.
     *
     * @param imageBand the image band to add
     */
    void addImageBand(final ImageBand imageBand);

    /**
     * Return the number of bands (NBANDS/XBANDS) in the image.
     *
     * @return number of bands
     */
    int getNumBands();

    /**
     * Return the image band.
     * <p>
     * This method is 1-based, so the valid range is 1 through N, where N is the number of bands (see getNumBands() for
     * how to determine the number of bands).
     *
     * @param bandNumber the index of the band to return.
     * @return image band corresponding to the bandNumber index
     */
    ImageBand getImageBand(int bandNumber);

    /**
     * Return the image band (zero base).
     * <p>
     * This method is 0-based, so the valid range is 0 through N-1, where N is the number of bands.
     *
     * @param bandNumberZeroBase the index of the band to return (0 base).
     * @return image band corresponding to the bandNumberZeroBase index
     */
    ImageBand getImageBandZeroBase(int bandNumberZeroBase);

    /**
     * Set the image mode (IMODE) for the image.
     * <p>
     * "This field shall indicate how the Image Pixels are stored in the NITF file. Valid values are B, P, R, and S. The
     * interpretation of IMODE is dependent on whether the image is JPEG compressed (IC = C3, C5, I1, M3, or M5), VQ
     * compressed (IC = C4, or M4), or uncompressed (IC = NC or NM).
     *
     * a. Uncompressed. The value S indicates band sequential, where all blocks for the first band are followed by all
     * blocks for the second band, and so on: [(block1, band1), (block2, band1), ... (blockM, band1)], [(block1, band2),
     * (block2, band 2), ... (blockM, band2)] ... [(block1, bandN), (block2, bandN), ... (blockM, bandN)]. Note that, in
     * each block, the pixels of the first line appears first, followed by the pixels of the second line, and so on. The
     * value B indicates band interleaved by block. This implies that within each block, the bands follow one another:
     * [(block1, band1), (block1, band2), ...(block1, bandN)], [(block2, band1), (block2, band2), ... (block2, bandN)],
     * ... [(blockM, band1), (blockM, band2), ... (blockM, bandN)]. Note that, in each block, the pixels of the first
     * line appears first and the pixels of the last line appears last. The value P indicates band interleaved by pixel
     * within each block: such as, for each block, one after the other, the full pixel vector (all band values) appears
     * for every pixel in the block, one pixel after another, the block column index varying faster than the block row
     * index. The value R indicates band interleaved by row. The ordering mechanism for this case stores the pixel
     * values of each band in row sequential order. Within each block, all pixel values of the first row of the first
     * band are followed by pixel values of the first row of the second band continuing until all values of the first
     * row are stored The remaining rows are stored in a similar fashion until the last row of values has been stored.
     * Each block shall be zero-filled to the next octet boundary when necessary. If the value of the NBANDS field is 1,
     * the cases B and S coincide. In this case, this field shall contain B. If the Number of Blocks is 1 (the NBPR
     * field and the NBPC field contain 1), this field shall contain B for non-interleaved by pixel, and P for
     * interleaved by pixel. The value S is only valid for images with multiple blocks and multiple bands.
     *
     * b. JPEG-compressed. The presence of B, P, or S implies specific ordering of data within the JPEG image data
     * representation. For this case the interpretation of the various values of the IMODE field is specified in
     * MIL-STD-188-198A. When IC contains C8, M8, or I1, IMODE contains B.
     *
     * c. Vector Quantization compressed. VQ compressed images are normally either RGB with a color look-up table or
     * monochromatic. In either case, the image is single band, and the IMODE field defaults to B.
     *
     * d. Bi-Level Compressed. When the value of the IC field is C1 or M1, the value of the IMODE field is B."
     *
     * @param mode the image mode
     */
    void setImageMode(final ImageMode mode);

    /**
     * Return the image mode (IMODE) for the image.
     * <p>
     * See setImageMode() for image mode interpretation rules.
     *
     * @return the image mode
     */
    ImageMode getImageMode();

    /**
     * Set the number of blocks per row (NBPR).
     * <p>
     * From MIL-STD-2500C: "This field shall contain the number of image blocks in a row of blocks (paragraph 5.4.2.2)
     * in the horizontal direction. If the image consists of only a single block, this field shall contain the value
     * one."
     *
     * @param numberOfBlocksPerRow the number of blocks per row.
     */
    void setNumberOfBlocksPerRow(final int numberOfBlocksPerRow);

    /**
     * Return the number of blocks per row (NBPR).
     * <p>
     * From MIL-STD-2500C: "This field shall contain the number of image blocks in a row of blocks (paragraph 5.4.2.2)
     * in the horizontal direction. If the image consists of only a single block, this field shall contain the value
     * one."
     *
     * @return the number of blocks per row.
     */
    int getNumberOfBlocksPerRow();

    /**
     * Set the number of blocks per column (NBPC).
     * <p>
     * From MIL-STD-2500C: "This field shall contain the number of image blocks in a column of blocks (paragraph
     * 5.4.2.2) in the vertical direction. If the image consists of only a single block, this field shall contain the
     * value one."
     *
     * @param numberOfBlocksPerColumn the number of blocks per column.
     */
    void setNumberOfBlocksPerColumn(final int numberOfBlocksPerColumn);

    /**
     * Return the number of blocks per column (NBPC).
     * <p>
     * From MIL-STD-2500C: "This field shall contain the number of image blocks in a column of blocks (paragraph
     * 5.4.2.2) in the vertical direction. If the image consists of only a single block, this field shall contain the
     * value one."
     *
     * @return the number of blocks per column.
     */
    int getNumberOfBlocksPerColumn();

    /**
     * Set the number of pixels per block - horizontal direction (NPPBH).
     * <p>
     * From MIL-STD-2500C: "This field shall contain the number of pixels horizontally in each block of the image. It
     * shall be the case that the product of the values of the NBPR field and the NPPBH field is greater than or equal
     * to the value of the NCOLS field (NBPR * NPPBH ≥ NCOLS). When NBPR is "0001", setting the NPPBH value to "0000"
     * designates that the number of pixels horizontally is specified by the value in NCOLS."
     *
     * @param numberOfPixelsPerBlockHorizontal the number of pixels per block, horizontal.
     */
    void setNumberOfPixelsPerBlockHorizontalRaw(final int numberOfPixelsPerBlockHorizontal);

    /**
     * Return the number of pixels per block - horizontal direction (NPPBH).
     * <p>
     * From MIL-STD-2500C: "This field shall contain the number of pixels horizontally in each block of the image. It
     * shall be the case that the product of the values of the NBPR field and the NPPBH field is greater than or equal
     * to the value of the NCOLS field (NBPR * NPPBH ≥ NCOLS). When NBPR is "0001", setting the NPPBH value to "0000"
     * designates that the number of pixels horizontally is specified by the value in NCOLS."
     *
     * @return the number of pixels per block, horizontal.
     */
    int getNumberOfPixelsPerBlockHorizontalRaw();

    /**
     * Return the number of pixels per block - horizontal direction (NPPBH).
     * <p>
     * This is basically the same as getNumberOfPixelsPerBlockHorizontalRaw, except that it deals with the case where
     * NBPR == "0001" and NPPBH = "0000" and will return the actual number of pixels horizontally (i.e. the NCOLS
     * value) for that case.
     *
     * @return the number of pixels per block, horizontal.
     */
     long getNumberOfPixelsPerBlockHorizontal();

    /**
     * Set the number of pixels per block - vertical direction (NPPBV).
     * <p>
     * From MIL-STD-2500C: "This field shall contain the number of pixels vertically in each block of the image. It
     * shall be the case that the product of the values of the NBPC field and the NPPBV field is greater than or equal
     * to the value of the NROWS field (NBPC * NPPBV ≥ NROWS). When NBPC is "0001", setting the NPPBV value to "0000"
     * designates that the number of pixels vertically is specified by the value in NROWS."
     *
     * @param numberOfPixelsPerBlockVertical the number of pixels per block, vertical.
     */
    void setNumberOfPixelsPerBlockVerticalRaw(final int numberOfPixelsPerBlockVertical);

    /**
     * Return the number of pixels per block - vertical direction (NPPBV).
     * <p>
     * From MIL-STD-2500C: "This field shall contain the number of pixels vertically in each block of the image. It
     * shall be the case that the product of the values of the NBPC field and the NPPBV field is greater than or equal
     * to the value of the NROWS field (NBPC * NPPBV ≥ NROWS). When NBPC is "0001", setting the NPPBV value to "0000"
     * designates that the number of pixels vertically is specified by the value in NROWS."
     *
     * @return the number of pixels per block, vertical.
     */
    int getNumberOfPixelsPerBlockVerticalRaw();

    /**
     * Return the number of pixels per block - vertical direction (NPPBH).
     * <p>
     * This is basically the same as getNumberOfPixelsPerBlockVerticalRaw, except that it deals with the case where
     * NBPC == "0001" and NPPBV = "0000" and will return the actual number of pixels vertically (i.e. the NROWS
     * value) for that case.
     *
     * @return the number of pixels per block, vertical.
     */
    long getNumberOfPixelsPerBlockVertical();

    /**
     * Set the number of bits per pixel per band (NBPP).
     * <p>
     * From MIL-STD-2500C: "If IC contains NC, NM, C4, or M4, this field shall contain the number of storage bits used
     * for the value from each component of a pixel vector. The value in this field always shall be greater than or
     * equal to Actual Bits Per Pixel (ABPP). For example, if 11-bit pixels are stored in 16 bits, this field shall
     * contain 16 and Actual Bits Per Pixel shall contain 11. If IC = C3, M3, C5, M5, or I1 this field shall contain the
     * value 8 or the value 12. If IC = C1, this field shall contain the value 1. If IC = C8 or M8, this field shall
     * contain the number of bits of precision (01-38) used in the JPEG 2000 compression of the data."
     *
     * @param numberOfBitsPerPixelPerBand the number of bits per pixel per band.
     */
    void setNumberOfBitsPerPixelPerBand(final int numberOfBitsPerPixelPerBand);

    /**
     * Return the number of bits per pixel per band (NBPP).
     * <p>
     * From MIL-STD-2500C: "If IC contains NC, NM, C4, or M4, this field shall contain the number of storage bits used
     * for the value from each component of a pixel vector. The value in this field always shall be greater than or
     * equal to Actual Bits Per Pixel (ABPP). For example, if 11-bit pixels are stored in 16 bits, this field shall
     * contain 16 and Actual Bits Per Pixel shall contain 11. If IC = C3, M3, C5, M5, or I1 this field shall contain the
     * value 8 or the value 12. If IC = C1, this field shall contain the value 1. If IC = C8 or M8, this field shall
     * contain the number of bits of precision (01-38) used in the JPEG 2000 compression of the data."
     *
     * @return the number of bits per pixel per band.
     */
    int getNumberOfBitsPerPixelPerBand();

    /**
     * Set the image display level (IDLVL).
     * <p>
     * From MIL-STD-2500C: "This field shall contain a valid value that indicates the display level of the image
     * relative to other displayed file components in a composite display. The valid values are 001 to 999. The display
     * level of each displayable segment (image or graphic) within a file shall be unique; that is, each number from 001
     * to 999 is the display level of, at most, one segment. Display level is discussed in paragraph 5.3.3. The image or
     * graphic segment in the file having the minimum display level shall have attachment level 0 (ALVL000) (BCS zeros
     * (code 0x30))."
     * <p>
     * Note that explanation mixes display level and attachment level.
     *
     * @param displayLevel the display level (integer format).
     */
    void setImageDisplayLevel(final int displayLevel);

    /**
     * Return the image display level (IDLVL).
     * <p>
     * From MIL-STD-2500C: "This field shall contain a valid value that indicates the display level of the image
     * relative to other displayed file components in a composite display. The valid values are 001 to 999. The display
     * level of each displayable segment (image or graphic) within a file shall be unique; that is, each number from 001
     * to 999 is the display level of, at most, one segment. Display level is discussed in paragraph 5.3.3. The image or
     * graphic segment in the file having the minimum display level shall have attachment level 0 (ALVL000) (BCS zeros
     * (code 0x30))."
     * <p>
     * Note that explanation mixes display level and attachment level.
     *
     * @return the display level (integer format).
     */
    int getImageDisplayLevel();

    /**
     * Set the image location row (part of ILOC).
     * <p>
     * From MIL-STD-2500C: "The image location is the location of the first pixel of the first line of the image. This
     * field shall contain the image location offset from the ILOC or SLOC value of the segment to which the image is
     * attached or from the origin of the CCS when the image is unattached (IALVL contains 000). A row or column value
     * of 00000 indicates no offset. Positive row and column values indicate offsets down and to the right while
     * negative row and column values indicate offsets up and to the left."
     *
     * @param locationRow the image location row number
     */
    void setImageLocationRow(final int locationRow);

    /**
     * Return the image location row (part of ILOC).
     * <p>
     * From MIL-STD-2500C: "The image location is the location of the first pixel of the first line of the image. This
     * field shall contain the image location offset from the ILOC or SLOC value of the segment to which the image is
     * attached or from the origin of the CCS when the image is unattached (IALVL contains 000). A row or column value
     * of 00000 indicates no offset. Positive row and column values indicate offsets down and to the right while
     * negative row and column values indicate offsets up and to the left."
     *
     * @return the image location row number
     */
    int getImageLocationRow();

    /**
     * Set the image location column (part of ILOC).
     * <p>
     * From MIL-STD-2500C: "The image location is the location of the first pixel of the first line of the image. This
     * field shall contain the image location offset from the ILOC or SLOC value of the segment to which the image is
     * attached or from the origin of the CCS when the image is unattached (IALVL contains 000). A row or column value
     * of 00000 indicates no offset. Positive row and column values indicate offsets down and to the right while
     * negative row and column values indicate offsets up and to the left."
     *
     * @param locationColumn the image location column number
     */
    void setImageLocationColumn(final int locationColumn);

    /**
     * Return the image location column (part of ILOC).
     * <p>
     * From MIL-STD-2500C: "The image location is the location of the first pixel of the first line of the image. This
     * field shall contain the image location offset from the ILOC or SLOC value of the segment to which the image is
     * attached or from the origin of the CCS when the image is unattached (IALVL contains 000). A row or column value
     * of 00000 indicates no offset. Positive row and column values indicate offsets down and to the right while
     * negative row and column values indicate offsets up and to the left."
     *
     * @return the image location column number
     */
    int getImageLocationColumn();

    /**
     * Set the date / time (IDATIM) for the image.
     * <p>
     * This is supposed to be the date and time that the image was captured.
     *
     * @param dateTime the date time for the image.
     */
    void setImageDateTime(final DateTime dateTime);

    /**
     * Set the target identifier (TGTID) for the image.
     * <p>
     * It is common for some or all of the identifier to be filled with default spaces.
     *
     * @param targetId the target identifier
     */
    void setImageTargetId(final TargetId targetId);

    /**
     * Set the second image identifier (IID2) for the image.
     * <p>
     * "This field can contain the identification of additional information about the image." In NITF 2.0 files, this is
     * the image title (ITITLE) field.
     *
     * @param identifier the second image identifier for the image (80 character maximum)
     */
    void setImageIdentifier2(final String identifier);

    /**
     * Set the image source (ISORCE) for the image.
     * <p>
     * "This field shall contain a description of the source of the image. If the source of the data is classified, then
     * the description shall be preceded by the classification, including codeword(s) contained in table A-4. If this
     * field is all spaces (0x20), it shall imply that no image source data applies."
     *
     * @param source image source (42 characters maximum)
     */
    void setImageSource(final String source);

    /**
     * Set the number of significant columns (NCOLS) in the image.
     * <p>
     * "This field shall contain the total number of columns of significant pixels in the image. When the product of the
     * values of the NPPBH field and the NBPR field is greater than the NCOLS field (NPPBH * NBPR &gt; NCOLS), the columns
     * indexed with the value of the NCOLS field to (NPPBH * NBPR) minus 1 shall contain fill data. NOTE: Only the
     * columns indexed 0 to the value of the NCOLS field minus 1 of the image contain significant data. The pixel fill
     * values are determined by the application."
     *
     * @param numberOfColumns the number of significant columns
     */
    void setNumberOfColumns(final long numberOfColumns);

    /**
     * Set the number of significant rows (NROWS) in the image.
     * <p>
     * "This field shall contain the total number of rows of significant pixels in the image. When the product of the
     * values of the NPPBV field and the NBPC field is greater than the value of the NROWS field (NPPBV * NBPC &gt; NROWS),
     * the rows indexed with the value of the NROWS field to (NPPBV * NBPC) minus 1 shall contain fill data. NOTE: Only
     * the rows indexed 0 to the value of the NROWS field minus 1 of the image contain significant data. The pixel fill
     * values are determined by the application."
     *
     * @param numberOfRows the number of significant rows
     */
    void setNumberOfRows(final long numberOfRows);

    /**
     * Set the image magnification (IMAG).
     * <p>
     * From MIL-STD-2500C: "This field shall contain the magnification (or reduction) factor of the image relative to
     * the original source image. Decimal values are used to indicate magnification, and decimal fraction values
     * indicate reduction. For example, “2.30” indicates the original image has been magnified by a factor of “2.30,”
     * while “0.5” indicates the original image has been reduced by a factor of 2. The default value is 1.0, indicating
     * no magnification or reduction. In addition, the reductions can be represented as reciprocals of any non-negative
     * integer: /2 (for 1/2), /3 (for 1/3), /4 (for 1/4), /5 (for 1/5), through /999 (for 1/999). The values are left
     * justified and BCS spaces (0x20) filled to the right."
     *
     * @param magnification the magnification level
     */
    void setImageMagnification(final String magnification);

    /**
     * Set the pixel value type (PVTYPE) for the image.
     * <p>
     * "This field shall contain an indicator of the type of computer representation used for the value for each pixel
     * for each band in the image. Valid entries are INT for integer, B for bi-level, SI for 2’s complement signed
     * integer, R for real, and C for complex. The data bits of INT and SI values shall appear in the file in order of
     * significance, beginning with the MSB and ending with the LSB. Except when the data is JPEG 2000 compressed, INT
     * and SI data types shall be limited to 8, 12, 16, 32, or 64-bits (see field NBPP). R values shall be represented
     * according to IEEE 32 or 64-bit floating point representation (IEEE 754). C values shall be represented with the
     * Real and Imaginary parts, each represented in IEEE 32 or 64-bit floating point representation (IEEE 754) and
     * appearing in adjacent four or eight-byte blocks, first Real, then Imaginary. B (bi-level) pixel values shall be
     * represented as single bits with binary value 1 or 0."
     *
     * @param valueType the pixel value type
     */
    void setPixelValueType(final PixelValueType valueType);

    /**
     * Set the image representation (IREP) for the image.
     * <p>
     * "This field shall contain a valid indicator of the processing required in order to display an image. Valid
     * representation indicators are MONO for monochrome; RGB for red, green, or blue true color, RGB/LUT for mapped
     * color; MULTI for multiband imagery, NODISPLY for an image not intended for display, NVECTOR and POLAR for vectors
     * with Cartesian and polar coordinates respectively, and VPH for SAR video phase history. In addition, compressed
     * imagery can have this field set to YCbCr601 when compressed in the ITU-R Recommendation BT.601-5 color space
     * using JPEG (IC field = C3). This field should be used in conjunction with the IREPBANDn field to interpret the
     * processing required to display each band in the image."
     *
     * @param representation the image representation to apply
     */
    void setImageRepresentation(final ImageRepresentation representation);

    /**
     * Return the image magnification (IMAG).
     * <p>
     * From MIL-STD-2500C: "This field shall contain the magnification (or reduction) factor of the image relative to
     * the original source image. Decimal values are used to indicate magnification, and decimal fraction values
     * indicate reduction. For example, “2.30” indicates the original image has been magnified by a factor of “2.30,”
     * while “0.5” indicates the original image has been reduced by a factor of 2. The default value is 1.0, indicating
     * no magnification or reduction. In addition, the reductions can be represented as reciprocals of any non-negative
     * integer: /2 (for 1/2), /3 (for 1/3), /4 (for 1/4), /5 (for 1/5), through /999 (for 1/999). The values are left
     * justified and BCS spaces (0x20) filled to the right."
     *
     * @return the magnification level
     */
    String getImageMagnification();

    /**
     * Return the image magnification (IMAG) as a numerical value.
     * <p>
     * From MIL-STD-2500C: "This field shall contain the magnification (or reduction) factor of the image relative to
     * the original source image. Decimal values are used to indicate magnification, and decimal fraction values
     * indicate reduction. For example, “2.30” indicates the original image has been magnified by a factor of “2.30,”
     * while “0.5” indicates the original image has been reduced by a factor of 2. The default value is 1.0, indicating
     * no magnification or reduction. In addition, the reductions can be represented as reciprocals of any non-negative
     * integer: /2 (for 1/2), /3 (for 1/3), /4 (for 1/4), /5 (for 1/5), through /999 (for 1/999). The values are left
     * justified and BCS spaces (0x20) filled to the right."
     * <p>
     * This method converts the IMAG string to numerical value.
     *
     * @return the magnification level
     */
    double getImageMagnificationAsDouble();

    /**
     * Set the image coordinates (IGEOLO) for the image.
     * <p>
     * From MIL-STD-2500C: "This field, when present, shall contain an approximate geographic location which is not
     * intended for analytical purposes (e.g., targeting, mensuration, distance calculation); it is intended to support
     * general user appreciation for the image location (e.g., cataloguing)."
     *
     * @param coordinates the coordinates value.
     */
    void setImageCoordinates(final ImageCoordinates coordinates);

    /**
     * Return the image coordinates (IGEOLO) for the image.
     * <p>
     * From MIL-STD-2500C: "This field, when present, shall contain an approximate geographic location which is not
     * intended for analytical purposes (e.g., targeting, mensuration, distance calculation); it is intended to support
     * general user appreciation for the image location (e.g., cataloguing)."
     *
     * @return the coordinates value.
     */
    ImageCoordinates getImageCoordinates();

    /**
     * Return the user defined overflow (UDOFL) for the image.
     * <p>
     * This is the (1-base) index of the DES into which TRE data overflows.
     *
     * @return the user defined overflow index
     */
    int getUserDefinedHeaderOverflow();

    /**
     * Set the user defined header overflow (UDOFL) for the image.
     * <p>
     * This is the (1-base) index of the DES into which TRE data overflows.
     *
     * @param overflow the user defined header overflow index
     */
    void setUserDefinedHeaderOverflow(final int overflow);

    /**
     * Calculate the number of bytes per block, assuming uncompressed data.
     *
     * Use of this method on compressed images is probably a bad idea.
     *
     * @return number of bytes for image data in one block
     */
    long getNumberOfBytesPerBlock();

    /**
     * Get the data for this image segment.
     *
     * You may need to rewind this stream if it has been previously read.
     *
     * @return image input stream containing the data.
     */
    ImageInputStream getData();

    /**
     * Set the data for this image segment.
     *
     * @param data the data to set.
     */
    void setData(ImageInputStream data);

    /**
     * Get the length of the data for this segment.
     *
     * @return the number of bytes of data for this segment.
     */
    long getDataLength();

    /**
     * Set the length of data for this segment.
     *
     * @param length the number of bytes of data in this segment.
     */
    void setDataLength(final long length);

}
