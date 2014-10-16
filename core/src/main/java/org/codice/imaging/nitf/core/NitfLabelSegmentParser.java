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
package org.codice.imaging.nitf.core;

import java.text.ParseException;
import java.util.Set;

/**
    Parser for a label segment subheader in a NITF 2.0 file.
*/
class NitfLabelSegmentParser extends AbstractNitfSegmentParser {

    private int lengthOfLabel = 0;

    private int labelExtendedSubheaderLength = 0;

    private boolean shouldParseLabelData = false;

    private NitfLabelSegment segment = null;

    NitfLabelSegmentParser(final NitfReader nitfReader,
                                    final int labelLength,
                                    final Set<ParseOption> parseOptions,
                                    final NitfLabelSegment labelSegment) throws ParseException {

        reader = nitfReader;
        lengthOfLabel = labelLength;
        segment = labelSegment;

        shouldParseLabelData = parseOptions.contains(ParseOption.EXTRACT_LABEL_SEGMENT_DATA);

        readLA();
        readLID();
        segment.setSecurityMetadata(new NitfSecurityMetadata(reader));
        readENCRYP();
        readLFS();
        readLCW();
        readLCH();
        readLDLVL();
        readLALVL();
        readLLOC();
        readLTC();
        readLBC();
        readLXSHDL();
        if (labelExtendedSubheaderLength > 0) {
            readLXSOFL();
            readLXSHD();
        }
        readLabelData();
    }

    private void readLA() throws ParseException {
        reader.verifyHeaderMagic(NitfConstants.LA);
    }

    private void readLID() throws ParseException {
        segment.setIdentifier(reader.readTrimmedBytes(NitfConstants.LID_LENGTH));
    }

    private void readLFS() throws ParseException {
        reader.skip(NitfConstants.LFS_LENGTH);
    }

    private void readLCW() throws ParseException {
        segment.setLabelCellWidth(reader.readBytesAsInteger(NitfConstants.LCW_LENGTH));
    }

    private void readLCH() throws ParseException {
        segment.setLabelCellHeight(reader.readBytesAsInteger(NitfConstants.LCH_LENGTH));
    }

    private void readLDLVL() throws ParseException {
        segment.setLabelDisplayLevel(reader.readBytesAsInteger(NitfConstants.LDLVL_LENGTH));
    }

    private void readLALVL() throws ParseException {
        segment.setAttachmentLevel(reader.readBytesAsInteger(NitfConstants.LALVL_LENGTH));
    }

    private void readLLOC() throws ParseException {
        segment.setLabelLocationRow(reader.readBytesAsInteger(NitfConstants.LLOC_HALF_LENGTH));
        segment.setLabelLocationColumn(reader.readBytesAsInteger(NitfConstants.LLOC_HALF_LENGTH));
    }

    private void readLTC() throws ParseException {
        segment.setLabelTextColour(readRGBColour());
    }

    private void readLBC() throws ParseException {
        segment.setLabelBackgroundColour(readRGBColour());
    }

    private void readLXSHDL() throws ParseException {
        labelExtendedSubheaderLength = reader.readBytesAsInteger(NitfConstants.LXSHDL_LENGTH);
    }

    private void readLXSOFL() throws ParseException {
        segment.setExtendedHeaderDataOverflow(reader.readBytesAsInteger(NitfConstants.LXSOFL_LENGTH));
    }

    private void readLXSHD() throws ParseException {
        TreParser treParser = new TreParser();
        TreCollection extendedSubheaderTREs = treParser.parse(reader, labelExtendedSubheaderLength - NitfConstants.LXSOFL_LENGTH);
        segment.mergeTREs(extendedSubheaderTREs);
    }

    private void readLabelData() throws ParseException {
        if (lengthOfLabel == 0) {
            return;
        }
        if (shouldParseLabelData) {
            segment.setLabelData(reader.readBytes(lengthOfLabel));
        } else {
            reader.skip(lengthOfLabel);
        }
    }
}
