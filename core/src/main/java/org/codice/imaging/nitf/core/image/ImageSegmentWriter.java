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
     * Write out the specified image segment.
     *
     * @param imageSegment the header content to write out
     * @param fileType the type of file (NITF version) to write the image header out for.
     * @throws IOException on write failure.
     * @throws ParseException on TRE parsing failure.
     */
    public final void writeImageSegment(final ImageSegment imageSegment, final FileType fileType) throws IOException, ParseException {
        writeFixedLengthString(IM, IM.length());
        writeFixedLengthString(imageSegment.getIdentifier(), IID1_LENGTH);
        writeDateTime(imageSegment.getImageDateTime());
        writeFixedLengthString(imageSegment.getImageTargetId().toString(), TGTID_LENGTH);
        writeFixedLengthString(imageSegment.getImageIdentifier2(), IID2_LENGTH);
        writeSecurityMetadata(imageSegment.getSecurityMetadata(), fileType);
        writeENCRYP();
        writeFixedLengthString(imageSegment.getImageSource(), ISORCE_LENGTH);
        writeFixedLengthNumber(imageSegment.getNumberOfRows(), NROWS_LENGTH);
        writeFixedLengthNumber(imageSegment.getNumberOfColumns(), NCOLS_LENGTH);
        writeFixedLengthString(imageSegment.getPixelValueType().getTextEquivalent(), PVTYPE_LENGTH);
        writeFixedLengthString(imageSegment.getImageRepresentation().getTextEquivalent(), IREP_LENGTH);
        writeFixedLengthString(imageSegment.getImageCategory().getTextEquivalent(), ICAT_LENGTH);
        writeFixedLengthNumber(imageSegment.getActualBitsPerPixelPerBand(), ABPP_LENGTH);
        writeFixedLengthString(imageSegment.getPixelJustification().getTextEquivalent(), PJUST_LENGTH);
        writeFixedLengthString(imageSegment.getImageCoordinatesRepresentation().getTextEquivalent(fileType), ICORDS_LENGTH);
        if (imageSegment.getImageCoordinatesRepresentation() != ImageCoordinatesRepresentation.NONE) {
            writeFixedLengthString(imageSegment.getImageCoordinates().getCoordinate00().getSourceFormat(),
                    IGEOLO_LENGTH / NUM_PARTS_IN_IGEOLO);
            writeFixedLengthString(imageSegment.getImageCoordinates().getCoordinate0MaxCol().getSourceFormat(),
                    IGEOLO_LENGTH / NUM_PARTS_IN_IGEOLO);
            writeFixedLengthString(imageSegment.getImageCoordinates().getCoordinateMaxRowMaxCol().getSourceFormat(),
                    IGEOLO_LENGTH / NUM_PARTS_IN_IGEOLO);
            writeFixedLengthString(imageSegment.getImageCoordinates().getCoordinateMaxRow0().getSourceFormat(),
                    IGEOLO_LENGTH / NUM_PARTS_IN_IGEOLO);
        }
        writeFixedLengthNumber(imageSegment.getImageComments().size(), NICOM_LENGTH);
        for (String comment : imageSegment.getImageComments()) {
            writeFixedLengthString(comment, ICOM_LENGTH);
        }
        writeFixedLengthString(imageSegment.getImageCompression().getTextEquivalent(), IC_LENGTH);
        if ((imageSegment.getImageCompression() != ImageCompression.NOTCOMPRESSED)
                && (imageSegment.getImageCompression() != ImageCompression.NOTCOMPRESSEDMASK)) {
            writeFixedLengthString(imageSegment.getCompressionRate(), COMRAT_LENGTH);
        }

        if (imageSegment.getNumBands() <= MAX_NUM_BANDS_IN_NBANDS_FIELD) {
            writeFixedLengthNumber(imageSegment.getNumBands(), NBANDS_LENGTH);
        } else {
            writeFixedLengthNumber(0, NBANDS_LENGTH);
            writeFixedLengthNumber(imageSegment.getNumBands(), XBANDS_LENGTH);
        }

        for (int i = 0; i < imageSegment.getNumBands(); ++i) {
            NitfImageBand band = imageSegment.getImageBandZeroBase(i);
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
        writeFixedLengthString(imageSegment.getImageMode().getTextEquivalent(), IMODE_LENGTH);
        writeFixedLengthNumber(imageSegment.getNumberOfBlocksPerRow(), NBPR_LENGTH);
        writeFixedLengthNumber(imageSegment.getNumberOfBlocksPerColumn(), NBPC_LENGTH);
        writeFixedLengthNumber(imageSegment.getNumberOfPixelsPerBlockHorizontal(), NPPBH_LENGTH);
        writeFixedLengthNumber(imageSegment.getNumberOfPixelsPerBlockVertical(), NPPBV_LENGTH);
        writeFixedLengthNumber(imageSegment.getNumberOfBitsPerPixelPerBand(), NBPP_LENGTH);
        writeFixedLengthNumber(imageSegment.getImageDisplayLevel(), IDLVL_LENGTH);
        writeFixedLengthNumber(imageSegment.getAttachmentLevel(), IALVL_LENGTH);
        writeFixedLengthNumber(imageSegment.getImageLocationRow(), ILOC_HALF_LENGTH);
        writeFixedLengthNumber(imageSegment.getImageLocationColumn(), ILOC_HALF_LENGTH);
        writeFixedLengthString(imageSegment.getImageMagnification(), IMAG_LENGTH);
        byte[] userDefinedImageData = mTreParser.getTREs(imageSegment, TreSource.UserDefinedImageData);
        int userDefinedImageDataLength = userDefinedImageData.length;
        if ((userDefinedImageDataLength > 0) || (imageSegment.getUserDefinedHeaderOverflow() != 0)) {
            userDefinedImageDataLength += UDOFL_LENGTH;
        }
        writeFixedLengthNumber(userDefinedImageDataLength, UDIDL_LENGTH);
        if (userDefinedImageDataLength > 0) {
            writeFixedLengthNumber(imageSegment.getUserDefinedHeaderOverflow(), UDOFL_LENGTH);
            mOutput.write(userDefinedImageData);
        }
        byte[] imageExtendedSubheaderData = mTreParser.getTREs(imageSegment, TreSource.ImageExtendedSubheaderData);
        int imageExtendedSubheaderDataLength = imageExtendedSubheaderData.length;
        if ((imageExtendedSubheaderDataLength > 0) || (imageSegment.getExtendedHeaderDataOverflow() != 0)) {
            imageExtendedSubheaderDataLength += IXSOFL_LENGTH;
        }
        writeFixedLengthNumber(imageExtendedSubheaderDataLength, IXSHDL_LENGTH);
        if (imageExtendedSubheaderDataLength > 0) {
            writeFixedLengthNumber(imageSegment.getExtendedHeaderDataOverflow(), IXSOFL_LENGTH);
            mOutput.write(imageExtendedSubheaderData);
        }

        writeSegmentData(imageSegment.getData());
    }

}
