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

import java.io.DataOutput;
import java.io.IOException;
import java.text.ParseException;
import org.codice.imaging.nitf.core.common.AbstractSegmentWriter;
import org.codice.imaging.nitf.core.common.FileType;
import static org.codice.imaging.nitf.core.image.ImageConstants.ABPP_LENGTH;
import static org.codice.imaging.nitf.core.image.ImageConstants.COMRAT_LENGTH;
import static org.codice.imaging.nitf.core.image.ImageConstants.IALVL_LENGTH;
import static org.codice.imaging.nitf.core.image.ImageConstants.ICAT_LENGTH;
import static org.codice.imaging.nitf.core.image.ImageConstants.ICOM_LENGTH;
import static org.codice.imaging.nitf.core.image.ImageConstants.ICORDS_LENGTH;
import static org.codice.imaging.nitf.core.image.ImageConstants.IC_LENGTH;
import static org.codice.imaging.nitf.core.image.ImageConstants.IDLVL_LENGTH;
import static org.codice.imaging.nitf.core.image.ImageConstants.IFC_LENGTH;
import static org.codice.imaging.nitf.core.image.ImageConstants.IGEOLO_LENGTH;
import static org.codice.imaging.nitf.core.image.ImageConstants.IID1_LENGTH;
import static org.codice.imaging.nitf.core.image.ImageConstants.IID2_LENGTH;
import static org.codice.imaging.nitf.core.image.ImageConstants.ILOC_HALF_LENGTH;
import static org.codice.imaging.nitf.core.image.ImageConstants.IM;
import static org.codice.imaging.nitf.core.image.ImageConstants.IMAG_LENGTH;
import static org.codice.imaging.nitf.core.image.ImageConstants.IMFLT_LENGTH;
import static org.codice.imaging.nitf.core.image.ImageConstants.IMODE_LENGTH;
import static org.codice.imaging.nitf.core.image.ImageConstants.IREPBAND_LENGTH;
import static org.codice.imaging.nitf.core.image.ImageConstants.IREP_LENGTH;
import static org.codice.imaging.nitf.core.image.ImageConstants.ISORCE_LENGTH;
import static org.codice.imaging.nitf.core.image.ImageConstants.ISUBCAT_LENGTH;
import static org.codice.imaging.nitf.core.image.ImageConstants.ISYNC_LENGTH;
import static org.codice.imaging.nitf.core.image.ImageConstants.IXSHDL_LENGTH;
import static org.codice.imaging.nitf.core.image.ImageConstants.IXSOFL_LENGTH;
import static org.codice.imaging.nitf.core.image.ImageConstants.NBANDS_LENGTH;
import static org.codice.imaging.nitf.core.image.ImageConstants.NBPC_LENGTH;
import static org.codice.imaging.nitf.core.image.ImageConstants.NBPP_LENGTH;
import static org.codice.imaging.nitf.core.image.ImageConstants.NBPR_LENGTH;
import static org.codice.imaging.nitf.core.image.ImageConstants.NCOLS_LENGTH;
import static org.codice.imaging.nitf.core.image.ImageConstants.NELUT_LENGTH;
import static org.codice.imaging.nitf.core.image.ImageConstants.NICOM_LENGTH;
import static org.codice.imaging.nitf.core.image.ImageConstants.NLUTS_LENGTH;
import static org.codice.imaging.nitf.core.image.ImageConstants.NPPBH_LENGTH;
import static org.codice.imaging.nitf.core.image.ImageConstants.NPPBV_LENGTH;
import static org.codice.imaging.nitf.core.image.ImageConstants.NROWS_LENGTH;
import static org.codice.imaging.nitf.core.image.ImageConstants.PJUST_LENGTH;
import static org.codice.imaging.nitf.core.image.ImageConstants.PVTYPE_LENGTH;
import static org.codice.imaging.nitf.core.image.ImageConstants.TGTID_LENGTH;
import static org.codice.imaging.nitf.core.image.ImageConstants.UDIDL_LENGTH;
import static org.codice.imaging.nitf.core.image.ImageConstants.UDOFL_LENGTH;
import static org.codice.imaging.nitf.core.image.ImageConstants.XBANDS_LENGTH;
import org.codice.imaging.nitf.core.tre.TreParser;
import org.codice.imaging.nitf.core.tre.TreSource;

/**
 * Writer for Image Segments.
 */
public class ImageSegmentWriter extends AbstractSegmentWriter {

    private static final int NUM_PARTS_IN_IGEOLO = 4;
    private static final int MAX_NUM_BANDS_IN_NBANDS_FIELD = 9;

    /**
     * Constructor.
     *
     * @param output the target to write the image segment to.
     * @param treParser TreParser to use to serialise out the TREs.
     */
    public ImageSegmentWriter(final DataOutput output, final TreParser treParser) {
        super(output, treParser);
    }

    /**
     * Write out the subheader for the specified image segment.
     *
     * @param header the header content to write out
     * @param fileType the type of file (NITF version) to write the image header out for.
     * @throws IOException on write failure.
     * @throws ParseException on TRE parsing failure.
     */
    public final void writeImageHeader(final NitfImageSegmentHeader header, final FileType fileType) throws IOException, ParseException {
        writeFixedLengthString(IM, IM.length());
        writeFixedLengthString(header.getIdentifier(), IID1_LENGTH);
        writeDateTime(header.getImageDateTime());
        writeFixedLengthString(header.getImageTargetId().toString(), TGTID_LENGTH);
        writeFixedLengthString(header.getImageIdentifier2(), IID2_LENGTH);
        writeSecurityMetadata(header.getSecurityMetadata(), fileType);
        writeENCRYP();
        writeFixedLengthString(header.getImageSource(), ISORCE_LENGTH);
        writeFixedLengthNumber(header.getNumberOfRows(), NROWS_LENGTH);
        writeFixedLengthNumber(header.getNumberOfColumns(), NCOLS_LENGTH);
        writeFixedLengthString(header.getPixelValueType().getTextEquivalent(), PVTYPE_LENGTH);
        writeFixedLengthString(header.getImageRepresentation().getTextEquivalent(), IREP_LENGTH);
        writeFixedLengthString(header.getImageCategory().getTextEquivalent(), ICAT_LENGTH);
        writeFixedLengthNumber(header.getActualBitsPerPixelPerBand(), ABPP_LENGTH);
        writeFixedLengthString(header.getPixelJustification().getTextEquivalent(), PJUST_LENGTH);
        writeFixedLengthString(header.getImageCoordinatesRepresentation().getTextEquivalent(fileType), ICORDS_LENGTH);
        if (header.getImageCoordinatesRepresentation() != ImageCoordinatesRepresentation.NONE) {
            writeFixedLengthString(header.getImageCoordinates().getCoordinate00().getSourceFormat(),
                    IGEOLO_LENGTH / NUM_PARTS_IN_IGEOLO);
            writeFixedLengthString(header.getImageCoordinates().getCoordinate0MaxCol().getSourceFormat(),
                    IGEOLO_LENGTH / NUM_PARTS_IN_IGEOLO);
            writeFixedLengthString(header.getImageCoordinates().getCoordinateMaxRowMaxCol().getSourceFormat(),
                    IGEOLO_LENGTH / NUM_PARTS_IN_IGEOLO);
            writeFixedLengthString(header.getImageCoordinates().getCoordinateMaxRow0().getSourceFormat(),
                    IGEOLO_LENGTH / NUM_PARTS_IN_IGEOLO);
        }
        writeFixedLengthNumber(header.getImageComments().size(), NICOM_LENGTH);
        for (String comment : header.getImageComments()) {
            writeFixedLengthString(comment, ICOM_LENGTH);
        }
        writeFixedLengthString(header.getImageCompression().getTextEquivalent(), IC_LENGTH);
        if ((header.getImageCompression() != ImageCompression.NOTCOMPRESSED)
                && (header.getImageCompression() != ImageCompression.NOTCOMPRESSEDMASK)) {
            writeFixedLengthString(header.getCompressionRate(), COMRAT_LENGTH);
        }

        if (header.getNumBands() <= MAX_NUM_BANDS_IN_NBANDS_FIELD) {
            writeFixedLengthNumber(header.getNumBands(), NBANDS_LENGTH);
        } else {
            writeFixedLengthNumber(0, NBANDS_LENGTH);
            writeFixedLengthNumber(header.getNumBands(), XBANDS_LENGTH);
        }

        for (int i = 0; i < header.getNumBands(); ++i) {
            NitfImageBand band = header.getImageBandZeroBase(i);
            writeFixedLengthString(band.getImageRepresentation(), IREPBAND_LENGTH);
            writeFixedLengthString(band.getSubCategory(), ISUBCAT_LENGTH);
            writeFixedLengthString("N", IFC_LENGTH);
            writeFixedLengthString("", IMFLT_LENGTH); // space filled
            writeFixedLengthNumber(band.getNumLUTs(), NLUTS_LENGTH);
            if (band.getNumLUTs() != 0) {
                writeFixedLengthNumber(band.getNumLUTEntries(), NELUT_LENGTH);
                for (int j = 0; j < band.getNumLUTs(); ++j) {
                    NitfImageBandLUT lut = band.getLUTZeroBase(j);
                    mOutput.write(lut.getEntries());
                }
            }
        }
        writeFixedLengthNumber(0, ISYNC_LENGTH);
        writeFixedLengthString(header.getImageMode().getTextEquivalent(), IMODE_LENGTH);
        writeFixedLengthNumber(header.getNumberOfBlocksPerRow(), NBPR_LENGTH);
        writeFixedLengthNumber(header.getNumberOfBlocksPerColumn(), NBPC_LENGTH);
        writeFixedLengthNumber(header.getNumberOfPixelsPerBlockHorizontal(), NPPBH_LENGTH);
        writeFixedLengthNumber(header.getNumberOfPixelsPerBlockVertical(), NPPBV_LENGTH);
        writeFixedLengthNumber(header.getNumberOfBitsPerPixelPerBand(), NBPP_LENGTH);
        writeFixedLengthNumber(header.getImageDisplayLevel(), IDLVL_LENGTH);
        writeFixedLengthNumber(header.getAttachmentLevel(), IALVL_LENGTH);
        writeFixedLengthNumber(header.getImageLocationRow(), ILOC_HALF_LENGTH);
        writeFixedLengthNumber(header.getImageLocationColumn(), ILOC_HALF_LENGTH);
        writeFixedLengthString(header.getImageMagnification(), IMAG_LENGTH);
        byte[] userDefinedImageData = mTreParser.getTREs(header, TreSource.UserDefinedImageData);
        int userDefinedImageDataLength = userDefinedImageData.length;
        if ((userDefinedImageDataLength > 0) || (header.getUserDefinedHeaderOverflow() != 0)) {
            userDefinedImageDataLength += UDOFL_LENGTH;
        }
        writeFixedLengthNumber(userDefinedImageDataLength, UDIDL_LENGTH);
        if (userDefinedImageDataLength > 0) {
            writeFixedLengthNumber(header.getUserDefinedHeaderOverflow(), UDOFL_LENGTH);
            mOutput.write(userDefinedImageData);
        }
        byte[] imageExtendedSubheaderData = mTreParser.getTREs(header, TreSource.ImageExtendedSubheaderData);
        int imageExtendedSubheaderDataLength = imageExtendedSubheaderData.length;
        if ((imageExtendedSubheaderDataLength > 0) || (header.getExtendedHeaderDataOverflow() != 0)) {
            imageExtendedSubheaderDataLength += IXSOFL_LENGTH;
        }
        writeFixedLengthNumber(imageExtendedSubheaderDataLength, IXSHDL_LENGTH);
        if (imageExtendedSubheaderDataLength > 0) {
            writeFixedLengthNumber(header.getExtendedHeaderDataOverflow(), IXSOFL_LENGTH);
            mOutput.write(imageExtendedSubheaderData);
        }
    }

}
