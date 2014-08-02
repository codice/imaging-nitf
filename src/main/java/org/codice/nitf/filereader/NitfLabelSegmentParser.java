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
import java.util.EnumSet;

public class NitfLabelSegmentParser extends AbstractNitfSegmentParser {

    private int lengthOfLabel = 0;

    private int labelExtendedSubheaderLength = 0;
    private int labelExtendedSubheaderOverflow = 0;

    private static final String LA = "LA";
    private static final int LID_LENGTH = 10;
    private static final int LFS_LENGTH = 1;
    private static final int LCW_LENGTH = 2;
    private static final int LCH_LENGTH = 2;
    private static final int LDLVL_LENGTH = 3;
    private static final int LALVL_LENGTH = 3;
    private static final int LLOC_HALF_LENGTH = 5;
    private static final int LTC_LENGTH = 3;
    private static final int LBC_LENGTH = 3;
    private static final int LXSHDL_LENGTH = 5;
    private static final int LXSOFL_LENGTH = 3;

    private boolean shouldParseLabelData = false;

    private NitfLabelSegment segment = null;

    public NitfLabelSegmentParser(final NitfReader nitfReader,
                                    final int labelLength,
                                    final EnumSet<ParseOption> parseOptions,
                                    final NitfLabelSegment labelSegment) throws ParseException {

        reader = nitfReader;
        lengthOfLabel = labelLength;
        segment = labelSegment;

        shouldParseLabelData = parseOptions.contains(ParseOption.ExtractLabelSegmentData);

        readLA();
        readLID();
        segment.setSecurityMetadata(new NitfSecurityMetadata(reader));
        reader.readENCRYP();
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
        reader.verifyHeaderMagic(LA);
    }

    private void readLID() throws ParseException {
        segment.setLabelIdentifier(reader.readTrimmedBytes(LID_LENGTH));
    }

    private void readLFS() throws ParseException {
        reader.skip(LFS_LENGTH);
    }

    private void readLCW() throws ParseException {
        segment.setLabelCellWidth(reader.readBytesAsInteger(LCW_LENGTH));
    }

    private void readLCH() throws ParseException {
        segment.setLabelCellHeight(reader.readBytesAsInteger(LCH_LENGTH));
    }

    private void readLDLVL() throws ParseException {
        segment.setLabelDisplayLevel(reader.readBytesAsInteger(LDLVL_LENGTH));
    }

    private void readLALVL() throws ParseException {
        segment.setLabelAttachmentLevel(reader.readBytesAsInteger(LALVL_LENGTH));
    }

    private void readLLOC() throws ParseException {
        segment.setLabelLocationRow(reader.readBytesAsInteger(LLOC_HALF_LENGTH));
        segment.setLabelLocationColumn(reader.readBytesAsInteger(LLOC_HALF_LENGTH));
    }

    private void readLTC() throws ParseException {
        segment.setLabelTextColour(reader.readRGBColour());
    }

    private void readLBC() throws ParseException {
        segment.setLabelBackgroundColour(reader.readRGBColour());
    }

    private void readLXSHDL() throws ParseException {
        labelExtendedSubheaderLength = reader.readBytesAsInteger(LXSHDL_LENGTH);
    }

    private void readLXSOFL() throws ParseException {
        labelExtendedSubheaderOverflow = reader.readBytesAsInteger(LXSOFL_LENGTH);
    }

    private void readLXSHD() throws ParseException {
        TreParser treParser = new TreParser();
        TreCollection extendedSubheaderTREs = treParser.parse(reader, labelExtendedSubheaderLength - LXSOFL_LENGTH);
        segment.mergeTREs(extendedSubheaderTREs);
    }

    private void readLabelData() throws ParseException {
        if (lengthOfLabel == 0) {
            return;
        }
        if (shouldParseLabelData) {
            segment.setLabelData(reader.readBytesRaw(lengthOfLabel));
        } else {
            reader.skip(lengthOfLabel);
        }
    }
}
