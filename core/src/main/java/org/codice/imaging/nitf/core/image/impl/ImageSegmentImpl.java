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
package org.codice.imaging.nitf.core.image.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import javax.imageio.stream.ImageInputStream;

import org.codice.imaging.nitf.core.common.DateTime;
import org.codice.imaging.nitf.core.common.impl.CommonBasicSegmentImpl;
import org.codice.imaging.nitf.core.common.impl.CommonConstants;
import org.codice.imaging.nitf.core.common.NitfFormatException;
import static org.codice.imaging.nitf.core.image.impl.ImageConstants.IFC_LENGTH;
import static org.codice.imaging.nitf.core.image.impl.ImageConstants.IMFLT_LENGTH;
import static org.codice.imaging.nitf.core.image.impl.ImageConstants.IREPBAND_LENGTH;
import static org.codice.imaging.nitf.core.image.impl.ImageConstants.ISUBCAT_LENGTH;
import static org.codice.imaging.nitf.core.image.impl.ImageConstants.NELUT_LENGTH;
import static org.codice.imaging.nitf.core.image.impl.ImageConstants.NLUTS_LENGTH;

import org.codice.imaging.nitf.core.image.ImageBand;
import org.codice.imaging.nitf.core.image.ImageCategory;
import org.codice.imaging.nitf.core.image.ImageCompression;
import org.codice.imaging.nitf.core.image.ImageCoordinates;
import org.codice.imaging.nitf.core.image.ImageCoordinatesRepresentation;
import org.codice.imaging.nitf.core.image.ImageMode;
import org.codice.imaging.nitf.core.image.ImageRepresentation;
import org.codice.imaging.nitf.core.image.ImageSegment;
import org.codice.imaging.nitf.core.image.PixelJustification;
import org.codice.imaging.nitf.core.image.PixelValueType;
import org.codice.imaging.nitf.core.image.TargetId;
import org.codice.imaging.nitf.core.tre.impl.TreParser;
import org.codice.imaging.nitf.core.tre.TreSource;

/**
    Image segment information.
*/
class ImageSegmentImpl extends CommonBasicSegmentImpl implements ImageSegment {

    private static final Set<ImageCompression> HAS_COMRAT = EnumSet.of(ImageCompression.BILEVEL, ImageCompression.JPEG,
            ImageCompression.VECTORQUANTIZATION, ImageCompression.LOSSLESSJPEG, ImageCompression.JPEG2000, ImageCompression.DOWNSAMPLEDJPEG,
            ImageCompression.BILEVELMASK, ImageCompression.JPEGMASK, ImageCompression.VECTORQUANTIZATIONMASK,
            ImageCompression.LOSSLESSJPEGMASK, ImageCompression.JPEG2000MASK, ImageCompression.USERDEFINED, ImageCompression.USERDEFINEDMASK,
            ImageCompression.ARIDPCM, ImageCompression.ARIDPCMMASK);

    private DateTime imageDateTime = null;
    private TargetId imageTargetId = null;
    private String imageIdentifier2 = "";
    private String imageSource = "";
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
    private final List<ImageBand> imageBands = new ArrayList<>();
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
    private String imageMagnification = "1.0 ";
    private ImageInputStream imageData = null;
    private long dataLength = 0;

    private static final int BITS_PER_BYTE = 8;

    /**
        Default constructor.
    */
    ImageSegmentImpl() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void setImageDateTime(final DateTime dateTime) {
        imageDateTime = dateTime;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final DateTime getImageDateTime() {
        return imageDateTime;
    }

    /**
     * {@inheritDoc}
     */
    @Override
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
     * {@inheritDoc}
     */
    @Override
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
     * {@inheritDoc}
     */
    @Override
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
     * {@inheritDoc}
     */
    @Override
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
     * {@inheritDoc}
     */
    @Override
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
     * {@inheritDoc}
     */
    @Override
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
     * {@inheritDoc}
     */
    @Override
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
     * {@inheritDoc}
     */
    @Override
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
     * {@inheritDoc}
     */
    @Override
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
     * {@inheritDoc}
     */
    @Override
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
     * {@inheritDoc}
     */
    @Override
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
     * {@inheritDoc}
     */
    @Override
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
     * {@inheritDoc}
     */
    @Override
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
     * {@inheritDoc}
     */
    @Override
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
     * {@inheritDoc}
     */
    @Override
    public final void addImageBand(final ImageBand imageBand) {
        imageBands.add(imageBand);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final ImageBand getImageBand(final int bandNumber) {
        return getImageBandZeroBase(bandNumber - 1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final ImageBand getImageBandZeroBase(final int bandNumberZeroBase) {
        return imageBands.get(bandNumberZeroBase);
    }

    /**
     * {@inheritDoc}
     */
    @Override
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
     * {@inheritDoc}
     */
    @Override
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
     * {@inheritDoc}
     */
    @Override
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
     * {@inheritDoc}
     */
    @Override
    public final void setNumberOfPixelsPerBlockHorizontalRaw(final int numberOfPixelsPerBlockHorizontal) {
        numPixelsPerBlockHorizontal = numberOfPixelsPerBlockHorizontal;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final int getNumberOfPixelsPerBlockHorizontalRaw() {
        return numPixelsPerBlockHorizontal;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final long getNumberOfPixelsPerBlockHorizontal() {
        if ((numPixelsPerBlockHorizontal == 0) && (numBlocksPerRow == 1)) {
            return numColumns;
        } else {
            return numPixelsPerBlockHorizontal;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void setNumberOfPixelsPerBlockVerticalRaw(final int numberOfPixelsPerBlockVertical) {
        numPixelsPerBlockVertical = numberOfPixelsPerBlockVertical;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final int getNumberOfPixelsPerBlockVerticalRaw() {
        return numPixelsPerBlockVertical;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final long getNumberOfPixelsPerBlockVertical() {
        if ((numPixelsPerBlockVertical == 0) && (numBlocksPerColumn == 1)) {
            return numRows;
        } else {
            return numPixelsPerBlockVertical;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
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
     * {@inheritDoc}
     */
    @Override
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
     * {@inheritDoc}
     */
    @Override
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
     * {@inheritDoc}
     */
    @Override
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
     * {@inheritDoc}
     */
    @Override
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
     * {@inheritDoc}
     */
    @Override
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
     * {@inheritDoc}
     */
    @Override
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
     * {@inheritDoc}
     */
    @Override
    public final long getNumberOfBytesPerBlock() {
        long numberOfPixelsPerBlockPerBand = getNumberOfPixelsPerBlockHorizontal() * getNumberOfPixelsPerBlockVertical();
        long numberOfPixelsPerBlock = numberOfPixelsPerBlockPerBand * getNumBands();
        long numberOfBytesPerBlock = numberOfPixelsPerBlock * getNumberOfBitsPerPixelPerBand() / BITS_PER_BYTE;
        return numberOfBytesPerBlock;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ImageInputStream getData() {
        return imageData;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setData(final ImageInputStream data) {
        imageData = data;
    }

    @Override
    public final long getDataLength() {
        return dataLength;
    }

    @Override
    public void setDataLength(final long length) {
        dataLength = length;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getHeaderLength() throws NitfFormatException, IOException {
        long len = ImageConstants.IM.length()
                + ImageConstants.IID1_LENGTH
                + CommonConstants.STANDARD_DATE_TIME_LENGTH
                + ImageConstants.TGTID_LENGTH
                + ImageConstants.IID2_LENGTH
                + getSecurityMetadata().getSerialisedLength()
                + CommonConstants.ENCRYP_LENGTH
                + ImageConstants.ISORCE_LENGTH
                + ImageConstants.NROWS_LENGTH
                + ImageConstants.NCOLS_LENGTH
                + ImageConstants.PVTYPE_LENGTH
                + ImageConstants.IREP_LENGTH
                + ImageConstants.ICAT_LENGTH
                + ImageConstants.ABPP_LENGTH
                + ImageConstants.PJUST_LENGTH
                + ImageConstants.ICORDS_LENGTH
                + ImageConstants.NICOM_LENGTH
                + imageComments.size() * ImageConstants.ICOM_LENGTH
                + ImageConstants.IC_LENGTH
                + ImageConstants.NBANDS_LENGTH
                + ImageConstants.ISYNC_LENGTH
                + ImageConstants.IMODE_LENGTH
                + ImageConstants.NBPR_LENGTH
                + ImageConstants.NBPC_LENGTH
                + ImageConstants.NPPBH_LENGTH
                + ImageConstants.NPPBV_LENGTH
                + ImageConstants.NBPP_LENGTH
                + ImageConstants.IDLVL_LENGTH
                + ImageConstants.IALVL_LENGTH
                + ImageConstants.ILOC_HALF_LENGTH * 2
                + ImageConstants.IMAG_LENGTH
                + ImageConstants.UDIDL_LENGTH
                + ImageConstants.IXSHDL_LENGTH;

        if ((getImageCoordinatesRepresentation() != ImageCoordinatesRepresentation.UNKNOWN)
                && (getImageCoordinatesRepresentation() != ImageCoordinatesRepresentation.NONE)) {
            len += ImageConstants.IGEOLO_LENGTH;
        }

        if (hasCOMRAT()) {
            len += ImageConstants.COMRAT_LENGTH;
        }

        if (getNumBands() > ImageConstants.MAX_NUM_BANDS_IN_NBANDS_FIELD) {
            len += ImageConstants.XBANDS_LENGTH;
        }
        for (int i = 0; i < getNumBands(); ++i) {
            ImageBand band = getImageBandZeroBase(i);
            len += IREPBAND_LENGTH;
            len += ISUBCAT_LENGTH;
            len += IFC_LENGTH;
            len += IMFLT_LENGTH;
            len += NLUTS_LENGTH;
            if (band.getNumLUTs() != 0) {
                len += NELUT_LENGTH;
                for (int j = 0; j < band.getNumLUTs(); ++j) {
                    len += band.getNumLUTEntries();
                }
            }
        }

        TreParser treParser = new TreParser();
        int userDefinedImageDataLength = treParser.getTREs(this, TreSource.UserDefinedImageData).length;
        if ((userDefinedImageDataLength > 0) || (getUserDefinedHeaderOverflow() != 0)) {
            len += ImageConstants.UDOFL_LENGTH;
            len += userDefinedImageDataLength;
        }
        int extendedDataLength = treParser.getTREs(this, TreSource.ImageExtendedSubheaderData).length;
        if ((extendedDataLength > 0) || (getExtendedHeaderDataOverflow() != 0)) {
            len += ImageConstants.IXSOFL_LENGTH;
            len += extendedDataLength;
        }
        return len;
    }

    public Boolean hasCOMRAT() {
        return HAS_COMRAT.contains(getImageCompression());
    }
}
