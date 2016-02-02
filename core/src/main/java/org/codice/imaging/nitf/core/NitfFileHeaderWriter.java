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
package org.codice.imaging.nitf.core;

import org.codice.imaging.nitf.core.common.AbstractSegmentWriter;
import java.io.DataOutput;
import java.io.IOException;
import java.text.ParseException;
import org.codice.imaging.nitf.core.common.FileType;
import org.codice.imaging.nitf.core.dataextension.DataExtensionSegment;
import org.codice.imaging.nitf.core.security.SecurityMetadataWriter;
import org.codice.imaging.nitf.core.tre.TreParser;
import org.codice.imaging.nitf.core.tre.TreSource;

/**
 * Writer for the top level file header.
 */
public class NitfFileHeaderWriter extends AbstractSegmentWriter {

    private static final int BASIC_HEADER_LENGTH = 388;

    /**
     * Constructor.
     *
     * @param output the target to write the file header to.
     * @param treParser TreParser to use to serialise out the TREs.
     */
    public NitfFileHeaderWriter(final DataOutput output, final TreParser treParser) {
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
        NitfFileHeader header = mDataSource.getNitfHeader();
        mOutput.writeBytes(header.getFileType().getTextEquivalent());
        writeFixedLengthNumber(header.getComplexityLevel(), NitfConstants.CLEVEL_LENGTH);
        writeFixedLengthString(header.getStandardType(), NitfConstants.STYPE_LENGTH);
        writeFixedLengthString(header.getOriginatingStationId(), NitfConstants.OSTAID_LENGTH);
        mOutput.writeBytes(header.getFileDateTime().getSourceString());
        writeFixedLengthString(header.getFileTitle(), NitfConstants.FTITLE_LENGTH);
        SecurityMetadataWriter securityWriter = new SecurityMetadataWriter(mOutput, mTreParser);
        securityWriter.writeFileSecurityMetadata(header.getFileSecurityMetadata(), header.getFileType());
        writeENCRYP();
        if ((header.getFileType() == FileType.NITF_TWO_ONE) || (header.getFileType() == FileType.NSIF_ONE_ZERO)) {
            mOutput.writeByte(header.getFileBackgroundColour().getRed());
            mOutput.writeByte(header.getFileBackgroundColour().getGreen());
            mOutput.writeByte(header.getFileBackgroundColour().getBlue());
            writeFixedLengthString(header.getOriginatorsName(), NitfConstants.ONAME_LENGTH);
        } else {
            writeFixedLengthString(header.getOriginatorsName(), NitfConstants.ONAME20_LENGTH);
        }
        writeFixedLengthString(header.getOriginatorsPhoneNumber(), NitfConstants.OPHONE_LENGTH);
        int numberOfImageSegments = header.getImageSegmentSubHeaderLengths().size();
        int numberOfGraphicSegments = header.getGraphicSegmentSubHeaderLengths().size();
        int numberOfLabelSegments = header.getLabelSegmentSubHeaderLengths().size();
        int numberOfTextSegments = header.getTextSegmentSubHeaderLengths().size();
        byte[] userDefinedHeaderData = mTreParser.getTREs(header, TreSource.UserDefinedHeaderData);
        int userDefinedHeaderDataLength = userDefinedHeaderData.length;
        if ((userDefinedHeaderDataLength > 0) || (header.getUserDefinedHeaderOverflow() != 0)) {
            userDefinedHeaderDataLength += NitfConstants.UDHOFL_LENGTH;
        }
        byte[] extendedHeaderData = mTreParser.getTREs(header, TreSource.ExtendedHeaderData);
        int extendedHeaderDataLength = extendedHeaderData.length;
        if ((extendedHeaderDataLength > 0) || (header.getExtendedHeaderDataOverflow() != 0)) {
            extendedHeaderDataLength += NitfConstants.XHDLOFL_LENGTH;
        }
        int headerLength = BASIC_HEADER_LENGTH
                + numberOfImageSegments * (NitfConstants.LISH_LENGTH + NitfConstants.LI_LENGTH)
                + numberOfGraphicSegments * (NitfConstants.LSSH_LENGTH + NitfConstants.LS_LENGTH)
                + numberOfTextSegments * (NitfConstants.LTSH_LENGTH + NitfConstants.LT_LENGTH)
                + numberOfLabelSegments * (NitfConstants.LLSH_LENGTH + NitfConstants.LL_LENGTH)
                + userDefinedHeaderDataLength
                + extendedHeaderDataLength;

        headerLength += securityWriter.calculateExtraHeaderLength(header.getFileSecurityMetadata(),
                header.getFileType());

        int numberOfDataExtensionSegments = 0;
        for (DataExtensionSegment desHeader : mDataSource.getDataExtensionSegments()) {
            if (!desHeader.isStreamingMode()) {
                headerLength += NitfConstants.LDSH_LENGTH + NitfConstants.LD_LENGTH;
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
        writeFixedLengthNumber(fileLength, NitfConstants.FL_LENGTH);
        writeFixedLengthNumber(headerLength, NitfConstants.HL_LENGTH);
        writeFixedLengthNumber(numberOfImageSegments, NitfConstants.NUMI_LENGTH);
        for (int i = 0; i < numberOfImageSegments; ++i) {
            writeFixedLengthNumber(header.getImageSegmentSubHeaderLengths().get(i), NitfConstants.LISH_LENGTH);
            writeFixedLengthNumber(header.getImageSegmentDataLengths().get(i), NitfConstants.LI_LENGTH);
        }
        if ((header.getFileType() == FileType.NITF_TWO_ONE) || (header.getFileType() == FileType.NSIF_ONE_ZERO)) {
            writeFixedLengthNumber(numberOfGraphicSegments, NitfConstants.NUMS_LENGTH);
            for (int i = 0; i < numberOfGraphicSegments; ++i) {
                writeFixedLengthNumber(header.getGraphicSegmentSubHeaderLengths().get(i), NitfConstants.LSSH_LENGTH);
                writeFixedLengthNumber(header.getGraphicSegmentDataLengths().get(i), NitfConstants.LS_LENGTH);
            }
            writeFixedLengthNumber(0, NitfConstants.NUMX_LENGTH);
        } else {
            writeFixedLengthNumber(numberOfGraphicSegments, NitfConstants.NUMS_LENGTH);
            for (int i = 0; i < numberOfGraphicSegments; ++i) {
                writeFixedLengthNumber(header.getSymbolSegmentSubHeaderLengths().get(i), NitfConstants.LSSH_LENGTH);
                writeFixedLengthNumber(header.getSymbolSegmentDataLengths().get(i), NitfConstants.LS_LENGTH);
            }
            writeFixedLengthNumber(numberOfLabelSegments, NitfConstants.NUML20_LENGTH);
            for (int i = 0; i < numberOfLabelSegments; ++i) {
                writeFixedLengthNumber(header.getLabelSegmentSubHeaderLengths().get(i), NitfConstants.LLSH_LENGTH);
                writeFixedLengthNumber(header.getLabelSegmentDataLengths().get(i), NitfConstants.LL_LENGTH);
            }
        }
        writeFixedLengthNumber(numberOfTextSegments, NitfConstants.NUMT_LENGTH);
        for (int i = 0; i < numberOfTextSegments; ++i) {
            writeFixedLengthNumber(header.getTextSegmentSubHeaderLengths().get(i), NitfConstants.LTSH_LENGTH);
            writeFixedLengthNumber(header.getTextSegmentDataLengths().get(i), NitfConstants.LT_LENGTH);
        }
        writeFixedLengthNumber(numberOfDataExtensionSegments, NitfConstants.NUMDES_LENGTH);
        for (int i = 0; i < numberOfDataExtensionSegments; ++i) {
            DataExtensionSegment desHeader = mDataSource.getDataExtensionSegments().get(i);
            if (!desHeader.isStreamingMode()) {
                writeFixedLengthNumber(header.getDataExtensionSegmentSubHeaderLengths().get(i), NitfConstants.LDSH_LENGTH);
                writeFixedLengthNumber(header.getDataExtensionSegmentDataLengths().get(i), NitfConstants.LD_LENGTH);
            }
        }
        writeFixedLengthNumber(0, NitfConstants.NUMRES_LENGTH);
        writeFixedLengthNumber(userDefinedHeaderDataLength, NitfConstants.UDHDL_LENGTH);
        if (userDefinedHeaderDataLength > 0) {
            writeFixedLengthNumber(header.getUserDefinedHeaderOverflow(), NitfConstants.UDHOFL_LENGTH);
            mOutput.write(userDefinedHeaderData);
        }
        writeFixedLengthNumber(extendedHeaderDataLength, NitfConstants.XHDL_LENGTH);
        if (extendedHeaderDataLength > 0) {
            writeFixedLengthNumber(header.getExtendedHeaderDataOverflow(), NitfConstants.XHDLOFL_LENGTH);
            mOutput.write(extendedHeaderData);
        }
    }


}
