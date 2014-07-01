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

import java.io.BufferedInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class NitfImageSegment
{
    NitfReader reader = null;

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
    private ArrayList<String> imageComments = new ArrayList<String>();
    private ImageCompression imageCompression = ImageCompression.UNKNOWN;
    private String compressionRate = null;
    private int numBands = 0;
    private ArrayList<NitfImageBand> imageBands = new ArrayList<NitfImageBand>();
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
    private double imageMagnification = 0.0;
    private int userDefinedImageDataLength = 0;
    private int imageExtendedSubheaderDataLength = 0;
    private long lengthOfImage = 0L;

    private static final String IM = "IM";
    private static final int IM_LENGTH = 2;
    private static final int IID1_LENGTH = 10;
    private static final int TGTID_LENGTH = 17;
    private static final int IID2_LENGTH = 80;
    private static final int ISORCE_LENGTH = 42;
    private static final int NROWS_LENGTH = 8;
    private static final int NCOLS_LENGTH = 8;
    private static final int PVTYPE_LENGTH = 3;
    private static final int IREP_LENGTH = 8;
    private static final int ICAT_LENGTH = 8;
    private static final int ABPP_LENGTH = 2;
    private static final int PJUST_LENGTH = 1;
    private static final int ICORDS_LENGTH = 1;
    private static final int IGEOLO_LENGTH = 60;
    private static final int NICOM_LENGTH = 1;
    private static final int ICOM_LENGTH = 80;
    private static final int IC_LENGTH = 2;
    private static final int COMRAT_LENGTH = 4;
    private static final int NBANDS_LENGTH = 1;
    private static final int ISYNC_LENGTH = 1;
    private static final int IMODE_LENGTH = 1;
    private static final int NBPR_LENGTH = 4;
    private static final int NBPC_LENGTH = 4;
    private static final int NPPBH_LENGTH = 4;
    private static final int NPPBV_LENGTH = 4;
    private static final int NBPP_LENGTH = 2;
    private static final int IDLVL_LENGTH = 3;
    private static final int IALVL_LENGTH = 3;
    private static final int ILOC_HALF_LENGTH = 5;
    private static final int IMAG_LENGTH = 4;
    private static final int UDIDL_LENGTH = 5;
    private static final int IXSHDL_LENGTH = 5;

    public NitfImageSegment(NitfReader nitfReader, long imageLength) throws ParseException {
        reader = nitfReader;
        lengthOfImage = imageLength;
        readIM();
        readIID1();
        readIDATIM();
        readTGTID();
        readIID2();
        securityMetadata = new NitfSecurityMetadata(reader);
        reader.readENCRYP();
        readISORCE();
        readNROWS();
        readNCOLS();
        readPVTYPE();
        readIREP();
        readICAT();
        readABPP();
        readPJUST();
        readICORDS();
        if ((imageCoordinatesRepresentation != ImageCoordinatesRepresentation.UNKNOWN) &&
            (imageCoordinatesRepresentation != ImageCoordinatesRepresentation.NONE)) {
            readIGEOLO();
        }
        readNICOM();
        for (int i = 0; i < numImageComments; ++i) {
            imageComments.add(reader.readTrimmedBytes(ICOM_LENGTH));
        }
        readIC();
        if (hasCOMRAT()) {
            readCOMRAT();
        }
        readNBANDS();
        if (numBands == 0) {
            throw new UnsupportedOperationException("IMPLEMENT XBAND PARSING");
        }
        for (int i = 0; i < numBands; ++i) {
            imageBands.add(new NitfImageBand(reader));
        }
        readISYNC();
        readIMODE();
        readNBPR();
        readNBPC();
        readNPPBH();
        readNPPBV();
        readNBPP();
        readIDLVL();
        readIALVL();
        readILOC();
        readIMAG();
        readUDIDL();
        if (userDefinedImageDataLength > 0) {
            throw new UnsupportedOperationException("IMPLEMENT UDOFL / UDID PARSING");
        }
        readIXSHDL();
        if (imageExtendedSubheaderDataLength > 0) {
            throw new UnsupportedOperationException("IMPLEMENT IXSOFL / IXSHD PARSING");
        }
        readImageData();
    }

    public String getImageIdentifier1() {
        return imageIdentifier1;
    }

    public Date getImageDateTime() {
        return imageDateTime;
    }

    public String getImageTargetId() {
        return imageTargetId;
    }

    public String getImageIdentifier2() {
        return imageIdentifier2;
    }

    public NitfSecurityMetadata getSecurityMetadata() {
        return securityMetadata;
    }

    public String getImageSource() {
        return imageSource;
    }

    public long getNumberOfRows() {
        return numRows;
    }

    public long getNumberOfColumns() {
        return numColumns;
    }

    public PixelValueType getPixelValueType() {
        return pixelValueType;
    }

    public ImageRepresentation getImageRepresentation() {
        return imageRepresentation;
    }

    public ImageCategory getImageCategory() {
        return imageCategory;
    }

    public int getActualBitsPerPixelPerBand() {
        return actualBitsPerPixelPerBand;
    }

    public PixelJustification getPixelJustification() {
        return pixelJustification;
    }

    public ImageCoordinatesRepresentation getImageCoordinatesRepresentation() {
        return imageCoordinatesRepresentation;
    }

    public int getNumberOfImageComments() {
        return numImageComments;
    }

    public String getImageComment(int commentNumber) {
        return getImageCommentZeroBase(commentNumber - 1);
    }

    public String getImageCommentZeroBase(int commentNumberZeroBase) {
        return imageComments.get(commentNumberZeroBase);
    }

    public ImageCompression getImageCompression() {
        return imageCompression;
    }

    public String getCompressionRate() {
        return compressionRate;
    }

    public int getNumBands() {
        return numBands;
    }

    public NitfImageBand getImageBand(int bandNumber) {
        return getImageBandZeroBase(bandNumber - 1);
    }

    public NitfImageBand getImageBandZeroBase(int bandNumberZeroBase) {
        return imageBands.get(bandNumberZeroBase);
    }

    public ImageMode getImageMode() {
        return imageMode;
    }

    public int getNumberOfBlocksPerRow() {
        return numBlocksPerRow;
    }

    public int getNumberOfBlocksPerColumn() {
        return numBlocksPerColumn;
    }

    public int getNumberOfPixelsPerBlockHorizontal() {
        return numPixelsPerBlockHorizontal;
    }

    public int getNumberOfPixelsPerBlockVertical() {
        return numPixelsPerBlockVertical;
    }

    public int getNumberOfBitsPerPixelPerBand() {
        return numBitsPerPixelPerBand;
    }

    public int getImageDisplayLevel() {
        return imageDisplayLevel;
    }

    public int getImageAttachmentLevel() {
        return imageAttachmentLevel;
    }

    public int getImageLocationRow() {
        return imageLocationRow;
    }

    public int getImageLocationColumn() {
        return imageLocationColumn;
    }

    public double getImageMagnification() {
        return imageMagnification;
    }

    public int getUserDefinedImageDataLength() {
        return userDefinedImageDataLength;
    }

    public int getImageExtendedSubheaderDataLength() {
        return imageExtendedSubheaderDataLength;
    }

    public ImageCoordinates getImageCoordinates() {
        return imageCoordinates;
    }

    public long getLengthOfImage() {
        return lengthOfImage;
    }
    
    private Boolean hasCOMRAT() {
        return ((imageCompression == ImageCompression.BILEVEL) ||
                (imageCompression == ImageCompression.JPEG) ||
                (imageCompression == ImageCompression.VECTORQUANTIZATION) ||
                (imageCompression == ImageCompression.LOSSLESSJPEG) ||
                (imageCompression == ImageCompression.JPEG2000) ||
                (imageCompression == ImageCompression.BILEVELMASK) ||
                (imageCompression == ImageCompression.JPEGMASK) ||
                (imageCompression == ImageCompression.VECTORQUANTIZATIONMASK) ||
                (imageCompression == ImageCompression.LOSSLESSJPEGMASK) ||
                (imageCompression == ImageCompression.JPEG2000MASK));
    }

    private void readIM() throws ParseException {
       reader.verifyHeaderMagic(IM);
    }

    private void readIID1() throws ParseException {
        imageIdentifier1 = reader.readTrimmedBytes(IID1_LENGTH);
    }

    private void readIDATIM() throws ParseException {
        imageDateTime = reader.readNitfDateTime();
    }

    private void readTGTID() throws ParseException {
        imageTargetId = reader.readTrimmedBytes(TGTID_LENGTH);
    }

    private void readIID2() throws ParseException {
        imageIdentifier2 = reader.readTrimmedBytes(IID2_LENGTH);
    }

    private void readISORCE() throws ParseException {
        imageSource = reader.readTrimmedBytes(ISORCE_LENGTH);
    }

    private void readNROWS() throws ParseException {
        numRows = reader.readBytesAsLong(NROWS_LENGTH);
    }

    private void readNCOLS() throws ParseException {
        numColumns = reader.readBytesAsLong(NCOLS_LENGTH);
    }

    private void readPVTYPE() throws ParseException {
        String pvtype = reader.readTrimmedBytes(PVTYPE_LENGTH);
        pixelValueType = PixelValueType.getEnumValue(pvtype);
    }

    private void readIREP() throws ParseException {
        String irep = reader.readTrimmedBytes(IREP_LENGTH);
        imageRepresentation = ImageRepresentation.getEnumValue(irep);
    }

    private void readICAT() throws ParseException {
        String icat = reader.readTrimmedBytes(ICAT_LENGTH);
        imageCategory = ImageCategory.getEnumValue(icat);
    }

    private void readABPP() throws ParseException {
        actualBitsPerPixelPerBand = reader.readBytesAsInteger(ABPP_LENGTH);
    }

    private void readPJUST() throws ParseException {
        String pjust = reader.readTrimmedBytes(PJUST_LENGTH);
        pixelJustification = PixelJustification.getEnumValue(pjust);
    }

    private void readICORDS() throws ParseException {
        String icords = reader.readBytes(ICORDS_LENGTH);
        imageCoordinatesRepresentation = ImageCoordinatesRepresentation.getEnumValue(icords);
    }

    private void readIGEOLO() throws ParseException {
        // TODO: this really only handle the GEO and D cases, not the UTM / UPS representations.
        final int NUM_COORDS = 4;
        final int COORD_LENGTH = IGEOLO_LENGTH / NUM_COORDS;
        String igeolo = reader.readBytes(IGEOLO_LENGTH);
        ImageCoordinatePair[] coords = new ImageCoordinatePair[NUM_COORDS];
        for (int i = 0; i < NUM_COORDS; ++i) {
            coords[i] = new ImageCoordinatePair();
            String coordStr = igeolo.substring(i * COORD_LENGTH, (i + 1) * COORD_LENGTH);
            switch (imageCoordinatesRepresentation) {
                case GEOGRAPHIC:
                    coords[i].setFromDMS(coordStr);
                    break;
                case DECIMALDEGREES:
                    coords[i].setFromDecimalDegrees(coordStr);
                    break;
                default:
                    throw new UnsupportedOperationException("NEED TO IMPLEMENT OTHER COORDINATE REPRESENTATIONS");
            }
        }
        imageCoordinates = new ImageCoordinates(coords[0], coords[1], coords[2], coords[3]);
    }

    private void readNICOM() throws ParseException {
        numImageComments = reader.readBytesAsInteger(NICOM_LENGTH);
    }

    private void readIC() throws ParseException {
        String ic = reader.readBytes(IC_LENGTH);
        imageCompression = ImageCompression.getEnumValue(ic);
    }

    private void readNBANDS() throws ParseException {
        numBands = reader.readBytesAsInteger(NBANDS_LENGTH);
    }

    private void readISYNC() throws ParseException {
        reader.readBytes(ISYNC_LENGTH);
    }

    private void readIMODE() throws ParseException {
        String imode = reader.readBytes(IMODE_LENGTH);
        imageMode = ImageMode.getEnumValue(imode);
    }

    private void readNBPR() throws ParseException {
        numBlocksPerRow = reader.readBytesAsInteger(NBPR_LENGTH);
    }

    private void readNBPC() throws ParseException {
        numBlocksPerColumn = reader.readBytesAsInteger(NBPC_LENGTH);
    }

    private void readNPPBH() throws ParseException {
        numPixelsPerBlockHorizontal = reader.readBytesAsInteger(NPPBH_LENGTH);
    }

    private void readNPPBV() throws ParseException {
        numPixelsPerBlockVertical = reader.readBytesAsInteger(NPPBV_LENGTH);
    }

    private void readNBPP() throws ParseException {
        numBitsPerPixelPerBand = reader.readBytesAsInteger(NBPP_LENGTH);
    }

    private void readIDLVL() throws ParseException {
        imageDisplayLevel = reader.readBytesAsInteger(IDLVL_LENGTH);
    }

    private void readIALVL() throws ParseException {
        imageAttachmentLevel = reader.readBytesAsInteger(IALVL_LENGTH);
    }

    private void readILOC() throws ParseException {
        imageLocationRow = reader.readBytesAsInteger(ILOC_HALF_LENGTH);
        imageLocationColumn = reader.readBytesAsInteger(ILOC_HALF_LENGTH);
    }

    private void readIMAG() throws ParseException {
        imageMagnification = reader.readBytesAsDouble(IMAG_LENGTH);
    }

    private void readUDIDL() throws ParseException {
        userDefinedImageDataLength = reader.readBytesAsInteger(UDIDL_LENGTH);
    }

    private void readIXSHDL() throws ParseException {
        imageExtendedSubheaderDataLength = reader.readBytesAsInteger(IXSHDL_LENGTH);
    }

    private void readCOMRAT() throws ParseException {
        compressionRate = reader.readTrimmedBytes(COMRAT_LENGTH);
    }

    private void readImageData() throws ParseException {
        // TODO: we could use this if needed later
        reader.skip(lengthOfImage);
    }
}
