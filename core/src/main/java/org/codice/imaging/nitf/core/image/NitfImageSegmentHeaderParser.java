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
package org.codice.imaging.nitf.core.image;

import static org.codice.imaging.nitf.core.image.ImageConstants.ABPP_LENGTH;
import static org.codice.imaging.nitf.core.image.ImageConstants.COMRAT_LENGTH;
import static org.codice.imaging.nitf.core.image.ImageConstants.IALVL_LENGTH;
import static org.codice.imaging.nitf.core.image.ImageConstants.ICAT_LENGTH;
import static org.codice.imaging.nitf.core.image.ImageConstants.ICOM_LENGTH;
import static org.codice.imaging.nitf.core.image.ImageConstants.ICORDS_LENGTH;
import static org.codice.imaging.nitf.core.image.ImageConstants.IC_LENGTH;
import static org.codice.imaging.nitf.core.image.ImageConstants.IDLVL_LENGTH;
import static org.codice.imaging.nitf.core.image.ImageConstants.IGEOLO_LENGTH;
import static org.codice.imaging.nitf.core.image.ImageConstants.IID1_LENGTH;
import static org.codice.imaging.nitf.core.image.ImageConstants.IID2_LENGTH;
import static org.codice.imaging.nitf.core.image.ImageConstants.ILOC_HALF_LENGTH;
import static org.codice.imaging.nitf.core.image.ImageConstants.IM;
import static org.codice.imaging.nitf.core.image.ImageConstants.IMAG_LENGTH;
import static org.codice.imaging.nitf.core.image.ImageConstants.IMODE_LENGTH;
import static org.codice.imaging.nitf.core.image.ImageConstants.IREP_LENGTH;
import static org.codice.imaging.nitf.core.image.ImageConstants.ISORCE_LENGTH;
import static org.codice.imaging.nitf.core.image.ImageConstants.ISYNC_LENGTH;
import static org.codice.imaging.nitf.core.image.ImageConstants.IXSHDL_LENGTH;
import static org.codice.imaging.nitf.core.image.ImageConstants.IXSOFL_LENGTH;
import static org.codice.imaging.nitf.core.image.ImageConstants.NBANDS_LENGTH;
import static org.codice.imaging.nitf.core.image.ImageConstants.NBPC_LENGTH;
import static org.codice.imaging.nitf.core.image.ImageConstants.NBPP_LENGTH;
import static org.codice.imaging.nitf.core.image.ImageConstants.NBPR_LENGTH;
import static org.codice.imaging.nitf.core.image.ImageConstants.NCOLS_LENGTH;
import static org.codice.imaging.nitf.core.image.ImageConstants.NICOM_LENGTH;
import static org.codice.imaging.nitf.core.image.ImageConstants.NPPBH_LENGTH;
import static org.codice.imaging.nitf.core.image.ImageConstants.NPPBV_LENGTH;
import static org.codice.imaging.nitf.core.image.ImageConstants.NROWS_LENGTH;
import static org.codice.imaging.nitf.core.image.ImageConstants.PJUST_LENGTH;
import static org.codice.imaging.nitf.core.image.ImageConstants.PVTYPE_LENGTH;
import static org.codice.imaging.nitf.core.image.ImageConstants.TGTID_LENGTH;
import static org.codice.imaging.nitf.core.image.ImageConstants.UDIDL_LENGTH;
import static org.codice.imaging.nitf.core.image.ImageConstants.UDOFL_LENGTH;
import static org.codice.imaging.nitf.core.image.ImageConstants.XBANDS_LENGTH;

import java.text.ParseException;
import java.util.EnumSet;
import java.util.Set;

import org.codice.imaging.nitf.core.common.AbstractNitfSegmentParser;
import org.codice.imaging.nitf.core.common.FileType;
import org.codice.imaging.nitf.core.common.NitfParseStrategy;
import org.codice.imaging.nitf.core.common.NitfReader;
import org.codice.imaging.nitf.core.security.SecurityMetadataParser;
import org.codice.imaging.nitf.core.tre.TreCollection;

/**
    Parser for an image segment subheader in a NITF file.
*/
public class NitfImageSegmentHeaderParser extends AbstractNitfSegmentParser {

    private int numImageComments = 0;
    private int numBands = 0;
    private int userDefinedImageDataLength = 0;
    private int imageExtendedSubheaderDataLength = 0;

    private static final Set<ImageCompression> HAS_COMRAT = EnumSet.of(ImageCompression.BILEVEL, ImageCompression.JPEG,
        ImageCompression.VECTORQUANTIZATION, ImageCompression.LOSSLESSJPEG, ImageCompression.JPEG2000, ImageCompression.DOWNSAMPLEDJPEG,
        ImageCompression.BILEVELMASK, ImageCompression.JPEGMASK, ImageCompression.VECTORQUANTIZATIONMASK,
        ImageCompression.LOSSLESSJPEGMASK, ImageCompression.JPEG2000MASK, ImageCompression.USERDEFINED, ImageCompression.USERDEFINEDMASK,
        ImageCompression.ARIDPCM, ImageCompression.ARIDPCMMASK);

    private NitfImageSegmentHeaderImpl segment = null;

    /**
     * Default constructor.
     */
    public NitfImageSegmentHeaderParser() {
    }

    /**
     * Parse the image segment header
     * <p>
     * This will return the image segment header, but it is not threadsafe. Please create a new parser for each header, or protect against
     * parallel runs.
     * @param nitfReader the reader to use to get the data
     * @param parseStrategy the parsing strategy to use to process the data
     * @return the parsed header
     * @throws ParseException on parse failure
     */
    public final NitfImageSegmentHeaderImpl parse(final NitfReader nitfReader, final NitfParseStrategy parseStrategy) throws ParseException {
        reader = nitfReader;
        segment = new NitfImageSegmentHeaderImpl();
        parsingStrategy = parseStrategy;

        readIM();
        readIID1();
        readIDATIM();
        readTGTID();
        readIID2();
        segment.setSecurityMetadata(new SecurityMetadataParser().parseSecurityMetadata(reader));
        readENCRYP();
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
        if ((reader.getFileType() != FileType.NITF_TWO_ZERO) && (numBands == 0)) {
            readXBANDS();
        }
        for (int i = 0; i < numBands; ++i) {
            NitfImageBand imageBand = new NitfImageBand();
            new NitfImageBandParser(reader, imageBand);
            segment.addImageBand(imageBand);
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
        return segment;
    }

    private Boolean hasCOMRAT() {
        return HAS_COMRAT.contains(segment.getImageCompression());
    }

    private void readIM() throws ParseException {
       reader.verifyHeaderMagic(IM);
    }

    private void readIID1() throws ParseException {
        segment.setIdentifier(reader.readTrimmedBytes(IID1_LENGTH));
    }

    private void readIDATIM() throws ParseException {
        segment.setImageDateTime(readNitfDateTime());
    }

    private void readTGTID() throws ParseException {
        segment.setImageTargetId(new TargetId(reader.readBytes(TGTID_LENGTH)));
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
        segment.setImageCoordinatesRepresentation(ImageCoordinatesRepresentation.getEnumValue(icords, reader.getFileType()));
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
                case GEOCENTRIC:
                    coords[i].setFromDMS(coordStr);
                    break;
                case MGRS:
                    coords[i].setFromMGRS(coordStr);
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
        segment.setAttachmentLevel(reader.readBytesAsInteger(IALVL_LENGTH));
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
        segment.setUserDefinedHeaderOverflow(reader.readBytesAsInteger(UDOFL_LENGTH));
    }

    private void readUDID() throws ParseException {
        TreCollection userDefinedSubheaderTres = parsingStrategy.parseTREs(reader, userDefinedImageDataLength - UDOFL_LENGTH);
        segment.mergeTREs(userDefinedSubheaderTres);
    }

    private void readIXSHDL() throws ParseException {
        imageExtendedSubheaderDataLength = reader.readBytesAsInteger(IXSHDL_LENGTH);
    }

    private void readIXSOFL() throws ParseException {
        segment.setExtendedHeaderDataOverflow(reader.readBytesAsInteger(IXSOFL_LENGTH));
    }

    private void readIXSHD() throws ParseException {
        TreCollection extendedSubheaderTres = parsingStrategy.parseTREs(reader, imageExtendedSubheaderDataLength - IXSOFL_LENGTH);
        segment.mergeTREs(extendedSubheaderTres);
    }

    private void readCOMRAT() throws ParseException {
        segment.setCompressionRate(reader.readTrimmedBytes(COMRAT_LENGTH));
    }
}
