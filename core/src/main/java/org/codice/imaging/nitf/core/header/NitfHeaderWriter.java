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
package org.codice.imaging.nitf.core.header;

import java.io.DataOutput;
import java.io.IOException;
import java.text.ParseException;
import org.codice.imaging.nitf.core.NitfDataSource;
import org.codice.imaging.nitf.core.common.AbstractSegmentWriter;
import org.codice.imaging.nitf.core.common.FileType;
import org.codice.imaging.nitf.core.dataextension.DataExtensionSegment;
import org.codice.imaging.nitf.core.security.SecurityMetadataWriter;
import org.codice.imaging.nitf.core.tre.TreParser;
import org.codice.imaging.nitf.core.tre.TreSource;

/**
 * Writer for the top level file header.
 */
public class NitfHeaderWriter extends AbstractSegmentWriter {

    private static final int BASIC_HEADER_LENGTH = 388;

    /**
     * Constructor.
     *
     * @param output the target to write the file header to.
     * @param treParser TreParser to use to serialise out the TREs.
     */
    public NitfHeaderWriter(final DataOutput output, final TreParser treParser) {
        super(output, treParser);
    }

    /**
     * Write out the file-level header.
     *
     * @param mDataSource the data source to take NITF structure from.
     * @throws IOException on read or write problems
     * @throws ParseException on TRE parsing problems
     */
    public final void writeFileHeader(final NitfDataSource mDataSource) throws IOException, ParseException {
        NitfHeader header = mDataSource.getNitfHeader();
        writeBytes(header.getFileType().getTextEquivalent(), NitfHeaderConstants.FHDR_LENGTH + NitfHeaderConstants.FVER_LENGTH);
        writeFixedLengthNumber(header.getComplexityLevel(), NitfHeaderConstants.CLEVEL_LENGTH);
        writeFixedLengthString(header.getStandardType(), NitfHeaderConstants.STYPE_LENGTH);
        writeFixedLengthString(header.getOriginatingStationId(), NitfHeaderConstants.OSTAID_LENGTH);
        writeDateTime(header.getFileDateTime());
        writeFixedLengthString(header.getFileTitle(), NitfHeaderConstants.FTITLE_LENGTH);
        SecurityMetadataWriter securityWriter = new SecurityMetadataWriter(mOutput, mTreParser);
        securityWriter.writeFileSecurityMetadata(header.getFileSecurityMetadata(), header.getFileType());
        writeENCRYP();
        if ((header.getFileType() == FileType.NITF_TWO_ONE) || (header.getFileType() == FileType.NSIF_ONE_ZERO)) {
            mOutput.writeByte(header.getFileBackgroundColour().getRed());
            mOutput.writeByte(header.getFileBackgroundColour().getGreen());
            mOutput.writeByte(header.getFileBackgroundColour().getBlue());
            writeFixedLengthString(header.getOriginatorsName(), NitfHeaderConstants.ONAME_LENGTH);
        } else {
            writeFixedLengthString(header.getOriginatorsName(), NitfHeaderConstants.ONAME20_LENGTH);
        }
        writeFixedLengthString(header.getOriginatorsPhoneNumber(), NitfHeaderConstants.OPHONE_LENGTH);
        int numberOfImageSegments = header.getImageSegmentSubHeaderLengths().size();
        int numberOfGraphicSegments = header.getGraphicSegmentSubHeaderLengths().size();
        int numberOfLabelSegments = header.getLabelSegmentSubHeaderLengths().size();
        int numberOfTextSegments = header.getTextSegmentSubHeaderLengths().size();
        byte[] userDefinedHeaderData = mTreParser.getTREs(header, TreSource.UserDefinedHeaderData);
        int userDefinedHeaderDataLength = userDefinedHeaderData.length;
        if ((userDefinedHeaderDataLength > 0) || (header.getUserDefinedHeaderOverflow() != 0)) {
            userDefinedHeaderDataLength += NitfHeaderConstants.UDHOFL_LENGTH;
        }
        byte[] extendedHeaderData = mTreParser.getTREs(header, TreSource.ExtendedHeaderData);
        int extendedHeaderDataLength = extendedHeaderData.length;
        if ((extendedHeaderDataLength > 0) || (header.getExtendedHeaderDataOverflow() != 0)) {
            extendedHeaderDataLength += NitfHeaderConstants.XHDLOFL_LENGTH;
        }
        int headerLength = BASIC_HEADER_LENGTH
                + numberOfImageSegments * (NitfHeaderConstants.LISH_LENGTH + NitfHeaderConstants.LI_LENGTH)
                + numberOfGraphicSegments * (NitfHeaderConstants.LSSH_LENGTH + NitfHeaderConstants.LS_LENGTH)
                + numberOfTextSegments * (NitfHeaderConstants.LTSH_LENGTH + NitfHeaderConstants.LT_LENGTH)
                + numberOfLabelSegments * (NitfHeaderConstants.LLSH_LENGTH + NitfHeaderConstants.LL_LENGTH)
                + userDefinedHeaderDataLength
                + extendedHeaderDataLength;

        headerLength += securityWriter.calculateExtraHeaderLength(header.getFileSecurityMetadata(),
                header.getFileType());

        int numberOfDataExtensionSegments = 0;
        for (DataExtensionSegment desHeader : mDataSource.getDataExtensionSegments()) {
            if (!desHeader.isStreamingMode()) {
                headerLength += NitfHeaderConstants.LDSH_LENGTH + NitfHeaderConstants.LD_LENGTH;
                numberOfDataExtensionSegments++;
            }
        }

        int fileLength = headerLength;
        for (int i = 0; i < numberOfImageSegments; ++i) {
            fileLength += header.getImageSegmentSubHeaderLengths().get(i);
            fileLength += header.getImageSegmentDataLengths().get(i);
        }
        // Also handles symbol segments in NITF 2.0 files.
        for (int i = 0; i < numberOfGraphicSegments; ++i) {
            fileLength += header.getGraphicSegmentSubHeaderLengths().get(i);
            fileLength += header.getGraphicSegmentDataLengths().get(i);
        }
        for (int i = 0; i < numberOfLabelSegments; ++i) {
            fileLength += header.getLabelSegmentSubHeaderLengths().get(i);
            fileLength += header.getLabelSegmentDataLengths().get(i);
        }
        for (int i = 0; i < numberOfTextSegments; ++i) {
            fileLength += header.getTextSegmentSubHeaderLengths().get(i);
            fileLength += header.getTextSegmentDataLengths().get(i);
        }
        for (int i = 0; i < numberOfDataExtensionSegments; ++i) {
            DataExtensionSegment desHeader = mDataSource.getDataExtensionSegments().get(i);
            if (!desHeader.isStreamingMode()) {
                fileLength += header.getDataExtensionSegmentSubHeaderLengths().get(i);
                fileLength += header.getDataExtensionSegmentDataLengths().get(i);
            }
        }
        writeFixedLengthNumber(fileLength, NitfHeaderConstants.FL_LENGTH);
        writeFixedLengthNumber(headerLength, NitfHeaderConstants.HL_LENGTH);
        writeFixedLengthNumber(numberOfImageSegments, NitfHeaderConstants.NUMI_LENGTH);
        for (int i = 0; i < numberOfImageSegments; ++i) {
            writeFixedLengthNumber(header.getImageSegmentSubHeaderLengths().get(i), NitfHeaderConstants.LISH_LENGTH);
            writeFixedLengthNumber(header.getImageSegmentDataLengths().get(i), NitfHeaderConstants.LI_LENGTH);
        }
        if ((header.getFileType() == FileType.NITF_TWO_ONE) || (header.getFileType() == FileType.NSIF_ONE_ZERO)) {
            writeFixedLengthNumber(numberOfGraphicSegments, NitfHeaderConstants.NUMS_LENGTH);
            for (int i = 0; i < numberOfGraphicSegments; ++i) {
                writeFixedLengthNumber(header.getGraphicSegmentSubHeaderLengths().get(i), NitfHeaderConstants.LSSH_LENGTH);
                writeFixedLengthNumber(header.getGraphicSegmentDataLengths().get(i), NitfHeaderConstants.LS_LENGTH);
            }
            writeFixedLengthNumber(0, NitfHeaderConstants.NUMX_LENGTH);
        } else {
            writeFixedLengthNumber(numberOfGraphicSegments, NitfHeaderConstants.NUMS_LENGTH);
            for (int i = 0; i < numberOfGraphicSegments; ++i) {
                writeFixedLengthNumber(header.getSymbolSegmentSubHeaderLengths().get(i), NitfHeaderConstants.LSSH_LENGTH);
                writeFixedLengthNumber(header.getSymbolSegmentDataLengths().get(i), NitfHeaderConstants.LS_LENGTH);
            }
            writeFixedLengthNumber(numberOfLabelSegments, NitfHeaderConstants.NUML20_LENGTH);
            for (int i = 0; i < numberOfLabelSegments; ++i) {
                writeFixedLengthNumber(header.getLabelSegmentSubHeaderLengths().get(i), NitfHeaderConstants.LLSH_LENGTH);
                writeFixedLengthNumber(header.getLabelSegmentDataLengths().get(i), NitfHeaderConstants.LL_LENGTH);
            }
        }
        writeFixedLengthNumber(numberOfTextSegments, NitfHeaderConstants.NUMT_LENGTH);
        for (int i = 0; i < numberOfTextSegments; ++i) {
            writeFixedLengthNumber(header.getTextSegmentSubHeaderLengths().get(i), NitfHeaderConstants.LTSH_LENGTH);
            writeFixedLengthNumber(header.getTextSegmentDataLengths().get(i), NitfHeaderConstants.LT_LENGTH);
        }
        writeFixedLengthNumber(numberOfDataExtensionSegments, NitfHeaderConstants.NUMDES_LENGTH);
        for (int i = 0; i < numberOfDataExtensionSegments; ++i) {
            DataExtensionSegment desHeader = mDataSource.getDataExtensionSegments().get(i);
            if (!desHeader.isStreamingMode()) {
                writeFixedLengthNumber(header.getDataExtensionSegmentSubHeaderLengths().get(i), NitfHeaderConstants.LDSH_LENGTH);
                writeFixedLengthNumber(header.getDataExtensionSegmentDataLengths().get(i), NitfHeaderConstants.LD_LENGTH);
            }
        }
        writeFixedLengthNumber(0, NitfHeaderConstants.NUMRES_LENGTH);
        writeFixedLengthNumber(userDefinedHeaderDataLength, NitfHeaderConstants.UDHDL_LENGTH);
        if (userDefinedHeaderDataLength > 0) {
            writeFixedLengthNumber(header.getUserDefinedHeaderOverflow(), NitfHeaderConstants.UDHOFL_LENGTH);
            writeBytes(userDefinedHeaderData, userDefinedHeaderDataLength - NitfHeaderConstants.UDHOFL_LENGTH);
        }
        writeFixedLengthNumber(extendedHeaderDataLength, NitfHeaderConstants.XHDL_LENGTH);
        if (extendedHeaderDataLength > 0) {
            writeFixedLengthNumber(header.getExtendedHeaderDataOverflow(), NitfHeaderConstants.XHDLOFL_LENGTH);
            writeBytes(extendedHeaderData, extendedHeaderDataLength - NitfHeaderConstants.XHDLOFL_LENGTH);
        }
    }


}
