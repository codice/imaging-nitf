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
package org.codice.imaging.nitf.label;

import java.text.ParseException;

import org.codice.imaging.nitf.common.reader.NitfReader;
import org.codice.imaging.nitf.parser.AbstractNitfSegmentParser;
import org.codice.imaging.nitf.security.NitfSecurityMetadataImpl;
import org.codice.imaging.nitf.parser.strategy.NitfParseStrategy;
import org.codice.imaging.nitf.tre.TreCollectionImpl;

/**
    Parser for a label segment subheader in a NITF 2.0 file.
*/
public class NitfLabelSegmentHeaderParser extends AbstractNitfSegmentParser {
    /**
     * Length of the "Extended Subheader Data Length" field in the NITF label header.
     * <p>
     * See MIL-STD-2500A Table XI and XII.
     */
    private static final int LXSHDL_LENGTH = 5;

    /**
     * Length of the "Extended Subheader Overflow" field in the NITF label header.
     * <p>
     * See MIL-STD-2500A Table XI and XII.
     */
    private static final int LXSOFL_LENGTH = 3;

    // label segment
    /**
     * Marker string for NITF Label subheader.
     * <p>
     * See MIL-STD-2500A Table XI and XII.
     */
    private static final String LA = "LA";

    /**
     * Length of the "Label Identifier" field in the NITF 2.0 Label Subheader.
     * <p>
     * See MIL-STD-2500A Table XI and XII.
     */
    private static final int LID_LENGTH = 10;

    /**
     * Length of the "Label Font Style" field in the NITF Label Subheader.
     * <p>
     * See MIL-STD-2500A Table XI and XII.
     */
    private static final int LFS_LENGTH = 1;

    /**
     * Length of the "Label Cell Width" field in the NITF Label Subheader.
     * <p>
     * See MIL-STD-2500A Table XI and XII.
     */
    private static final int LCW_LENGTH = 2;

    /**
     * Length of the "Label Cell Height" field in the NITF Label Subheader.
     * <p>
     * See MIL-STD-2500A Table XI and XII.
     */
    private static final int LCH_LENGTH = 2;

    /**
     * Length of the "Label Display Level" field in the NITF 2.0 label header.
     * <p>
     * See MIL-STD-2500A Table XI and XII.
     */
    private static final int LDLVL_LENGTH = 3;

    /**
     * Length of the "Label Attachment Level" field in the NITF label header.
     * <p>
     * See MIL-STD-2500A Table XI and XII.
     */
    private static final int LALVL_LENGTH = 3;

    /**
     * Length of half of the "Label Location" field in the NITF label Subheader.
     * <p>
     * See MIL-STD-2500A Table XI and XII.
     * <p>
     * The actual length is twice this, since the format is rrrrrccccc, but we parse rows then columns.
     */
    private static final int LLOC_HALF_LENGTH = 5;

    private int labelExtendedSubheaderLength = 0;

    private NitfLabelSegmentHeader segment = null;

    /**
     *
     * @param nitfReader - the NitfReader that streams the Nitf input.
     * @param parseStrategy - the strategy defines which fields to parse or skip.
     * @return - the NitfLabelSegmentHeader parsed from the NitfReader.
     * @throws ParseException - when an unexpected value is encountered in the input stream.
     */
    public final NitfLabelSegmentHeader parse(final NitfReader nitfReader,
            final NitfParseStrategy parseStrategy) throws ParseException {

        reader = nitfReader;
        parsingStrategy = parseStrategy;
        segment = new NitfLabelSegmentHeader();

        readLA();
        readLID();
        segment.setSecurityMetadata(new NitfSecurityMetadataImpl(reader));
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
        return segment;
    }

    private void readLA() throws ParseException {
        reader.verifyHeaderMagic(LA);
    }

    private void readLID() throws ParseException {
        segment.setIdentifier(reader.readTrimmedBytes(LID_LENGTH));
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
        segment.setAttachmentLevel(reader.readBytesAsInteger(LALVL_LENGTH));
    }

    private void readLLOC() throws ParseException {
        segment.setLabelLocationRow(reader.readBytesAsInteger(LLOC_HALF_LENGTH));
        segment.setLabelLocationColumn(reader.readBytesAsInteger(LLOC_HALF_LENGTH));
    }

    private void readLTC() throws ParseException {
        segment.setLabelTextColour(readRGBColour());
    }

    private void readLBC() throws ParseException {
        segment.setLabelBackgroundColour(readRGBColour());
    }

    private void readLXSHDL() throws ParseException {
        labelExtendedSubheaderLength = reader.readBytesAsInteger(LXSHDL_LENGTH);
    }

    private void readLXSOFL() throws ParseException {
        segment.setExtendedHeaderDataOverflow(reader.readBytesAsInteger(LXSOFL_LENGTH));
    }

    private void readLXSHD() throws ParseException {
        TreCollectionImpl extendedSubheaderTREs = parsingStrategy.parseTREs(reader, labelExtendedSubheaderLength - LXSOFL_LENGTH);
        segment.mergeTREs(extendedSubheaderTREs);
    }
}
