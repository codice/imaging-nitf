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

public class NitfImageSegmentParser extends AbstractNitfSegmentParser {

    private int numImageComments = 0;
    private int numBands = 0;
    private int userDefinedImageDataLength = 0;
    private int userDefinedOverflow = 0;
    private int imageExtendedSubheaderDataLength = 0;
    private int imageExtendedSubheaderOverflow = 0;
    private long lengthOfImage = 0L;

    private static final String IM = "IM";
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
    private static final int XBANDS_LENGTH = 5;
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
    private static final int UDOFL_LENGTH = 3;
    private static final int IXSHDL_LENGTH = 5;
    private static final int IXSOFL_LENGTH = 3;

    private NitfImageSegment segment = null;

    public NitfImageSegmentParser(final NitfReader nitfReader, final long imageLength, final NitfImageSegment imageSegment) throws ParseException {
        reader = nitfReader;
        lengthOfImage = imageLength;
        segment = imageSegment;

        readIM();
        readIID1();
        readIDATIM();
        readTGTID();
        readIID2();
        segment.setSecurityMetadata(new NitfSecurityMetadata(reader));
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
        if ((segment.getImageCoordinatesRepresentation() != ImageCoordinatesRepresentation.UNKNOWN)
            && (segment.getImageCoordinatesRepresentation() != ImageCoordinatesRepresentation.NONE)) {
            readIGEOLO();
        }
        readNICOM();
        for (int i = 0; i < numImageComments; ++i) {
            segment.addImageComment(reader.readTrimmedBytes(ICOM_LENGTH));
        }
        readIC();
        if (hasCOMRAT()) {
            readCOMRAT();
        }
        readNBANDS();
        if (numBands == 0) {
            readXBANDS();
        }
        for (int i = 0; i < numBands; ++i) {
            segment.addImageBand(new NitfImageBand(reader));
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
            readUDOFL();
            readUDID();
        }
        readIXSHDL();
        if (imageExtendedSubheaderDataLength > 0) {
            readIXSOFL();
            readIXSHD();
        }
        readImageData();
    }

    private Boolean hasCOMRAT() {
        ImageCompression imageCompression = segment.getImageCompression();
        return (imageCompression == ImageCompression.BILEVEL)
                || (imageCompression == ImageCompression.JPEG)
                || (imageCompression == ImageCompression.VECTORQUANTIZATION)
                || (imageCompression == ImageCompression.LOSSLESSJPEG)
                || (imageCompression == ImageCompression.JPEG2000)
                || (imageCompression == ImageCompression.DOWNSAMPLEDJPEG)
                || (imageCompression == ImageCompression.BILEVELMASK)
                || (imageCompression == ImageCompression.JPEGMASK)
                || (imageCompression == ImageCompression.VECTORQUANTIZATIONMASK)
                || (imageCompression == ImageCompression.LOSSLESSJPEGMASK)
                || (imageCompression == ImageCompression.JPEG2000MASK);
    }

    private void readIM() throws ParseException {
       reader.verifyHeaderMagic(IM);
    }

    private void readIID1() throws ParseException {
        segment.setImageIdentifier1(reader.readTrimmedBytes(IID1_LENGTH));
    }

    private void readIDATIM() throws ParseException {
        segment.setImageDateTime(reader.readNitfDateTime());
    }

    private void readTGTID() throws ParseException {
        segment.setImageTargetId(reader.readTrimmedBytes(TGTID_LENGTH));
    }

    private void readIID2() throws ParseException {
        segment.setImageIdentifier2(reader.readTrimmedBytes(IID2_LENGTH));
    }

    private void readISORCE() throws ParseException {
        segment.setImageSource(reader.readTrimmedBytes(ISORCE_LENGTH));
    }

    private void readNROWS() throws ParseException {
        segment.setNumberOfRows(reader.readBytesAsLong(NROWS_LENGTH));
    }

    private void readNCOLS() throws ParseException {
        segment.setNumberOfColumns(reader.readBytesAsLong(NCOLS_LENGTH));
    }

    private void readPVTYPE() throws ParseException {
        String pvtype = reader.readTrimmedBytes(PVTYPE_LENGTH);
        segment.setPixelValueType(PixelValueType.getEnumValue(pvtype));
    }

    private void readIREP() throws ParseException {
        String irep = reader.readTrimmedBytes(IREP_LENGTH);
        segment.setImageRepresentation(ImageRepresentation.getEnumValue(irep));
    }

    private void readICAT() throws ParseException {
        String icat = reader.readTrimmedBytes(ICAT_LENGTH);
        segment.setImageCategory(ImageCategory.getEnumValue(icat));
    }

    private void readABPP() throws ParseException {
        segment.setActualBitsPerPixelPerBand(reader.readBytesAsInteger(ABPP_LENGTH));
    }

    private void readPJUST() throws ParseException {
        String pjust = reader.readTrimmedBytes(PJUST_LENGTH);
        segment.setPixelJustification(PixelJustification.getEnumValue(pjust));
    }

    private void readICORDS() throws ParseException {
        String icords = reader.readBytes(ICORDS_LENGTH);
        segment.setImageCoordinatesRepresentation(ImageCoordinatesRepresentation.getEnumValue(icords));
    }

    private void readIGEOLO() throws ParseException {
        // TODO: this really only handle the GEO and D cases, not the UTM / UPS representations.
        final int numCoordinates = 4;
        final int coordinatePairLength = IGEOLO_LENGTH / numCoordinates;
        String igeolo = reader.readBytes(IGEOLO_LENGTH);
        ImageCoordinatePair[] coords = new ImageCoordinatePair[numCoordinates];
        for (int i = 0; i < numCoordinates; ++i) {
            coords[i] = new ImageCoordinatePair();
            String coordStr = igeolo.substring(i * coordinatePairLength, (i + 1) * coordinatePairLength);
            switch (segment.getImageCoordinatesRepresentation()) {
                case GEOGRAPHIC:
                    coords[i].setFromDMS(coordStr);
                    break;
                case DECIMALDEGREES:
                    coords[i].setFromDecimalDegrees(coordStr);
                    break;
                case UTMUPSNORTH:
                    coords[i].setFromUTMUPSNorth(coordStr);
                    break;
                default:
                    throw new UnsupportedOperationException("NEED TO IMPLEMENT OTHER COORDINATE REPRESENTATIONS: "
                                                            + segment.getImageCoordinatesRepresentation());
            }
        }
        segment.setImageCoordinates(new ImageCoordinates(coords));
    }

    private void readNICOM() throws ParseException {
        numImageComments = reader.readBytesAsInteger(NICOM_LENGTH);
    }

    private void readIC() throws ParseException {
        String ic = reader.readBytes(IC_LENGTH);
        segment.setImageCompression(ImageCompression.getEnumValue(ic));
    }

    private void readNBANDS() throws ParseException {
        numBands = reader.readBytesAsInteger(NBANDS_LENGTH);
    }

    private void readXBANDS() throws ParseException {
        numBands = reader.readBytesAsInteger(XBANDS_LENGTH);
    }

    private void readISYNC() throws ParseException {
        reader.readBytes(ISYNC_LENGTH);
    }

    private void readIMODE() throws ParseException {
        String imode = reader.readBytes(IMODE_LENGTH);
        segment.setImageMode(ImageMode.getEnumValue(imode));
    }

    private void readNBPR() throws ParseException {
        segment.setNumberOfBlocksPerRow(reader.readBytesAsInteger(NBPR_LENGTH));
    }

    private void readNBPC() throws ParseException {
        segment.setNumberOfBlocksPerColumn(reader.readBytesAsInteger(NBPC_LENGTH));
    }

    private void readNPPBH() throws ParseException {
        segment.setNumberOfPixelsPerBlockHorizontal(reader.readBytesAsInteger(NPPBH_LENGTH));
    }

    private void readNPPBV() throws ParseException {
        segment.setNumberOfPixelsPerBlockVertical(reader.readBytesAsInteger(NPPBV_LENGTH));
    }

    private void readNBPP() throws ParseException {
        segment.setNumberOfBitsPerPixelPerBand(reader.readBytesAsInteger(NBPP_LENGTH));
    }

    private void readIDLVL() throws ParseException {
        segment.setImageDisplayLevel(reader.readBytesAsInteger(IDLVL_LENGTH));
    }

    private void readIALVL() throws ParseException {
        segment.setImageAttachmentLevel(reader.readBytesAsInteger(IALVL_LENGTH));
    }

    private void readILOC() throws ParseException {
        segment.setImageLocationRow(reader.readBytesAsInteger(ILOC_HALF_LENGTH));
        segment.setImageLocationColumn(reader.readBytesAsInteger(ILOC_HALF_LENGTH));
    }

    private void readIMAG() throws ParseException {
        segment.setImageMagnification(reader.readBytes(IMAG_LENGTH));
    }

    private void readUDIDL() throws ParseException {
        userDefinedImageDataLength = reader.readBytesAsInteger(UDIDL_LENGTH);
    }

    private void readUDOFL() throws ParseException {
        userDefinedOverflow = reader.readBytesAsInteger(UDOFL_LENGTH);
    }

    private void readUDID() throws ParseException {
        TreParser treParser = new TreParser();
        TreCollection userDefinedSubheaderTres = treParser.parse(reader, userDefinedImageDataLength - UDOFL_LENGTH);
        segment.mergeTREs(userDefinedSubheaderTres);
    }

    private void readIXSHDL() throws ParseException {
        imageExtendedSubheaderDataLength = reader.readBytesAsInteger(IXSHDL_LENGTH);
    }

    private void readIXSOFL() throws ParseException {
        imageExtendedSubheaderOverflow = reader.readBytesAsInteger(IXSOFL_LENGTH);
    }

    private void readIXSHD() throws ParseException {
        TreParser treParser = new TreParser();
        TreCollection extendedSubheaderTres = treParser.parse(reader, imageExtendedSubheaderDataLength - IXSOFL_LENGTH);
        segment.mergeTREs(extendedSubheaderTres);
    }

    private void readCOMRAT() throws ParseException {
        segment.setCompressionRate(reader.readTrimmedBytes(COMRAT_LENGTH));
    }

    private void readImageData() throws ParseException {
        // TODO: we could use this if needed later
        reader.skip(lengthOfImage);
    }
}
