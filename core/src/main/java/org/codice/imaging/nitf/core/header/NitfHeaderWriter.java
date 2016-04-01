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
import org.codice.imaging.nitf.core.NitfDataSource;
import org.codice.imaging.nitf.core.RGBColour;
import org.codice.imaging.nitf.core.common.AbstractSegmentWriter;
import org.codice.imaging.nitf.core.common.CommonConstants;
import org.codice.imaging.nitf.core.common.FileType;
import org.codice.imaging.nitf.core.common.NitfFormatException;
import org.codice.imaging.nitf.core.dataextension.DataExtensionSegment;
import org.codice.imaging.nitf.core.graphic.GraphicSegment;
import org.codice.imaging.nitf.core.image.ImageSegment;
import org.codice.imaging.nitf.core.label.LabelSegment;
import org.codice.imaging.nitf.core.security.SecurityMetadataWriter;
import org.codice.imaging.nitf.core.symbol.SymbolSegment;
import org.codice.imaging.nitf.core.text.TextSegment;
import org.codice.imaging.nitf.core.tre.TreParser;
import org.codice.imaging.nitf.core.tre.TreSource;

/**
 * Writer for the top level file header.
 */
public class NitfHeaderWriter extends AbstractSegmentWriter {

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
     * @param dataSource the data source to take NITF structure from.
     * @throws IOException on read or write problems
     * @throws NitfFormatException on TRE parsing problems
     */
    public final void writeFileHeader(final NitfDataSource dataSource) throws IOException, NitfFormatException {
        NitfHeader header = dataSource.getNitfHeader();
        writeBytes(header.getFileType().getTextEquivalent(), NitfHeaderConstants.FHDR_LENGTH + NitfHeaderConstants.FVER_LENGTH);
        writeFixedLengthNumber(header.getComplexityLevel(), NitfHeaderConstants.CLEVEL_LENGTH);
        writeFixedLengthString(header.getStandardType(), NitfHeaderConstants.STYPE_LENGTH);
        writeFixedLengthString(header.getOriginatingStationId(), NitfHeaderConstants.OSTAID_LENGTH);
        writeDateTime(header.getFileDateTime());
        writeFixedLengthString(header.getFileTitle(), NitfHeaderConstants.FTITLE_LENGTH);
        SecurityMetadataWriter securityWriter = new SecurityMetadataWriter(mOutput, mTreParser);
        securityWriter.writeFileSecurityMetadata(header.getFileSecurityMetadata());
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

        long headerLength = getBasicHeaderLength(header);

        int numberOfImageSegments = dataSource.getImageSegments().size();
        int numberOfGraphicSegments = dataSource.getGraphicSegments().size();
        int numberOfSymbolSegments = dataSource.getSymbolSegments().size();
        int numberOfLabelSegments = dataSource.getLabelSegments().size();
        int numberOfTextSegments = dataSource.getTextSegments().size();
        headerLength += numberOfImageSegments * (NitfHeaderConstants.LISH_LENGTH + NitfHeaderConstants.LI_LENGTH);
        headerLength += numberOfLabelSegments * (NitfHeaderConstants.LLSH_LENGTH + NitfHeaderConstants.LL_LENGTH);
        headerLength += numberOfGraphicSegments * (NitfHeaderConstants.LSSH_LENGTH + NitfHeaderConstants.LS_LENGTH);
        headerLength += numberOfSymbolSegments * (NitfHeaderConstants.LSSH_LENGTH + NitfHeaderConstants.LS_LENGTH);
        headerLength += numberOfTextSegments * (NitfHeaderConstants.LTSH_LENGTH + NitfHeaderConstants.LT_LENGTH);

        int numberOfDataExtensionSegments = 0;
        for (DataExtensionSegment desHeader : dataSource.getDataExtensionSegments()) {
            if (!desHeader.isStreamingMode()) {
                headerLength += NitfHeaderConstants.LDSH_LENGTH + NitfHeaderConstants.LD_LENGTH;
                numberOfDataExtensionSegments++;
            }
        }

        byte[] userDefinedHeaderData = mTreParser.getTREs(header, TreSource.UserDefinedHeaderData);
        int userDefinedHeaderDataLength = userDefinedHeaderData.length;
        if ((userDefinedHeaderDataLength > 0) || (header.getUserDefinedHeaderOverflow() != 0)) {
            userDefinedHeaderDataLength += NitfHeaderConstants.UDHOFL_LENGTH;
        }
        headerLength += userDefinedHeaderDataLength;

        byte[] extendedHeaderData = mTreParser.getTREs(header, TreSource.ExtendedHeaderData);
        int extendedHeaderDataLength = extendedHeaderData.length;
        if ((extendedHeaderDataLength > 0) || (header.getExtendedHeaderDataOverflow() != 0)) {
            extendedHeaderDataLength += NitfHeaderConstants.XHDLOFL_LENGTH;
        }
        headerLength += extendedHeaderDataLength;

        long fileLength = headerLength;
        for (ImageSegment imageSegment : dataSource.getImageSegments()) {
            fileLength += imageSegment.getHeaderLength();
            fileLength += imageSegment.getDataLength();
        }
        for (GraphicSegment graphicSegment : dataSource.getGraphicSegments()) {
            fileLength += graphicSegment.getHeaderLength();
            fileLength += graphicSegment.getDataLength();
        }
        for (SymbolSegment symbolSegment : dataSource.getSymbolSegments()) {
            fileLength += symbolSegment.getHeaderLength();
            fileLength += symbolSegment.getDataLength();
        }
        for (LabelSegment labelSegment : dataSource.getLabelSegments()) {
            fileLength += labelSegment.getHeaderLength();
            fileLength += labelSegment.getData().length();
        }
        for (TextSegment textSegment : dataSource.getTextSegments()) {
            fileLength += textSegment.getHeaderLength();
            fileLength += textSegment.getData().length();
        }
        for (int i = 0; i < numberOfDataExtensionSegments; ++i) {
            DataExtensionSegment desHeader = dataSource.getDataExtensionSegments().get(i);
            if (!desHeader.isStreamingMode()) {
                fileLength += dataSource.getDataExtensionSegments().get(i).getHeaderLength();
                fileLength += dataSource.getDataExtensionSegments().get(i).getDataLength();
            }
        }
        writeFixedLengthNumber(fileLength, NitfHeaderConstants.FL_LENGTH);
        writeFixedLengthNumber(headerLength, NitfHeaderConstants.HL_LENGTH);
        writeFixedLengthNumber(numberOfImageSegments, NitfHeaderConstants.NUMI_LENGTH);
        for (ImageSegment imageSegment : dataSource.getImageSegments()) {
            writeFixedLengthNumber(imageSegment.getHeaderLength(), NitfHeaderConstants.LISH_LENGTH);
            writeFixedLengthNumber(imageSegment.getDataLength(), NitfHeaderConstants.LI_LENGTH);
        }
        if ((header.getFileType() == FileType.NITF_TWO_ONE) || (header.getFileType() == FileType.NSIF_ONE_ZERO)) {
            writeFixedLengthNumber(numberOfGraphicSegments, NitfHeaderConstants.NUMS_LENGTH);
            for (GraphicSegment graphicSegment : dataSource.getGraphicSegments()) {
                writeFixedLengthNumber(graphicSegment.getHeaderLength(), NitfHeaderConstants.LSSH_LENGTH);
                writeFixedLengthNumber(graphicSegment.getDataLength(), NitfHeaderConstants.LS_LENGTH);
            }
            writeFixedLengthNumber(0, NitfHeaderConstants.NUMX_LENGTH);
        } else {
            writeFixedLengthNumber(numberOfSymbolSegments, NitfHeaderConstants.NUMS_LENGTH);
            for (SymbolSegment symbolSegment : dataSource.getSymbolSegments()) {
                writeFixedLengthNumber(symbolSegment.getHeaderLength(), NitfHeaderConstants.LSSH_LENGTH);
                writeFixedLengthNumber(symbolSegment.getDataLength(), NitfHeaderConstants.LS_LENGTH);
            }
            writeFixedLengthNumber(numberOfLabelSegments, NitfHeaderConstants.NUML20_LENGTH);
            for (LabelSegment labelSegment : dataSource.getLabelSegments()) {
                writeFixedLengthNumber(labelSegment.getHeaderLength(), NitfHeaderConstants.LLSH_LENGTH);
                writeFixedLengthNumber(labelSegment.getData().length(), NitfHeaderConstants.LL_LENGTH);
            }
        }
        writeFixedLengthNumber(numberOfTextSegments, NitfHeaderConstants.NUMT_LENGTH);
        for (TextSegment textSegment : dataSource.getTextSegments()) {
            writeFixedLengthNumber(textSegment.getHeaderLength(), NitfHeaderConstants.LTSH_LENGTH);
            writeFixedLengthNumber(textSegment.getData().length(), NitfHeaderConstants.LT_LENGTH);
        }
        writeFixedLengthNumber(numberOfDataExtensionSegments, NitfHeaderConstants.NUMDES_LENGTH);
        for (int i = 0; i < numberOfDataExtensionSegments; ++i) {
            DataExtensionSegment desHeader = dataSource.getDataExtensionSegments().get(i);
            if (!desHeader.isStreamingMode()) {
                writeFixedLengthNumber(dataSource.getDataExtensionSegments().get(i).getHeaderLength(), NitfHeaderConstants.LDSH_LENGTH);
                writeFixedLengthNumber(dataSource.getDataExtensionSegments().get(i).getDataLength(), NitfHeaderConstants.LD_LENGTH);
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

    private long getBasicHeaderLength(final NitfHeader header) {
        long headerLength = NitfHeaderConstants.FHDR_LENGTH
                + NitfHeaderConstants.FVER_LENGTH
                + NitfHeaderConstants.CLEVEL_LENGTH
                + NitfHeaderConstants.STYPE_LENGTH
                + NitfHeaderConstants.OSTAID_LENGTH
                + CommonConstants.STANDARD_DATE_TIME_LENGTH
                + NitfHeaderConstants.FTITLE_LENGTH
                + header.getFileSecurityMetadata().getSerialisedLength()
                + CommonConstants.ENCRYP_LENGTH
                + RGBColour.RGB_COLOUR_LENGTH
                + NitfHeaderConstants.ONAME_LENGTH
                + NitfHeaderConstants.OPHONE_LENGTH
                + NitfHeaderConstants.FL_LENGTH
                + NitfHeaderConstants.HL_LENGTH
                + NitfHeaderConstants.NUMI_LENGTH
                + NitfHeaderConstants.NUMS_LENGTH
                + NitfHeaderConstants.NUMX_LENGTH
                + NitfHeaderConstants.NUMT_LENGTH
                + NitfHeaderConstants.NUMDES_LENGTH
                + NitfHeaderConstants.NUMRES_LENGTH
                + NitfHeaderConstants.UDHDL_LENGTH
                + NitfHeaderConstants.XHDL_LENGTH;

        return headerLength;
    }


}
