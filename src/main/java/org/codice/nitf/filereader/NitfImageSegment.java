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

    public NitfImageSegment() {
    }

    public final void parse(final NitfReader nitfReader, final long imageLength) throws ParseException {
        new NitfImageSegmentParser(nitfReader, imageLength, this);
    }

    public final void setImageIdentifier1(final String identifier) {
        imageIdentifier1 = identifier;
    }

    public final String getImageIdentifier1() {
        return imageIdentifier1;
    }

    public final void setImageDateTime(final Date dateTime) {
        imageDateTime = dateTime;
    }

    public final Date getImageDateTime() {
        return imageDateTime;
    }

    public final void setImageTargetId(final String targetId) {
        imageTargetId = targetId;
    }

    public final String getImageTargetId() {
        return imageTargetId;
    }

    public final void setImageIdentifier2(final String identifier) {
        imageIdentifier2 = identifier;
    }

    public final String getImageIdentifier2() {
        return imageIdentifier2;
    }

    public final void setSecurityMetadata(final NitfSecurityMetadata nitfSecurityMetadata) {
        securityMetadata = nitfSecurityMetadata;
    }

    public final NitfSecurityMetadata getSecurityMetadata() {
        return securityMetadata;
    }

    public final void setImageSource(final String source) {
        imageSource = source;
    }

    public final String getImageSource() {
        return imageSource;
    }

    public final void setNumberOfRows(final long numberOfRows) {
        numRows = numberOfRows;
    }

    public final long getNumberOfRows() {
        return numRows;
    }

    public final void setNumberOfColumns(final long numberOfColumns) {
        numColumns = numberOfColumns;
    }

    public final long getNumberOfColumns() {
        return numColumns;
    }

    public final void setPixelValueType(final PixelValueType valueType) {
        pixelValueType = valueType;
    }

    public final PixelValueType getPixelValueType() {
        return pixelValueType;
    }

    public final void setImageRepresentation(final ImageRepresentation representation) {
        imageRepresentation = representation;
    }

    public final ImageRepresentation getImageRepresentation() {
        return imageRepresentation;
    }

    public final void setImageCategory(final ImageCategory category) {
        imageCategory = category;
    }

    public final ImageCategory getImageCategory() {
        return imageCategory;
    }

    public final void setActualBitsPerPixelPerBand(final int abpb) {
        actualBitsPerPixelPerBand = abpb;
    }

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
}
