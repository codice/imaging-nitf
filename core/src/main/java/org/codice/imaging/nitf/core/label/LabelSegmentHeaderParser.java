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
package org.codice.imaging.nitf.core.label;

import static org.codice.imaging.nitf.core.label.LabelConstants.LA;
import static org.codice.imaging.nitf.core.label.LabelConstants.LALVL_LENGTH;
import static org.codice.imaging.nitf.core.label.LabelConstants.LCH_LENGTH;
import static org.codice.imaging.nitf.core.label.LabelConstants.LCW_LENGTH;
import static org.codice.imaging.nitf.core.label.LabelConstants.LDLVL_LENGTH;
import static org.codice.imaging.nitf.core.label.LabelConstants.LFS_LENGTH;
import static org.codice.imaging.nitf.core.label.LabelConstants.LID_LENGTH;
import static org.codice.imaging.nitf.core.label.LabelConstants.LLOC_HALF_LENGTH;
import static org.codice.imaging.nitf.core.label.LabelConstants.LXSHDL_LENGTH;
import static org.codice.imaging.nitf.core.label.LabelConstants.LXSOFL_LENGTH;
import java.text.ParseException;
import org.codice.imaging.nitf.core.common.NitfParseStrategy;
import org.codice.imaging.nitf.core.common.NitfReader;
import org.codice.imaging.nitf.core.SecurityMetadata;
import org.codice.imaging.nitf.core.TreCollection;
import org.codice.imaging.nitf.core.common.AbstractNitfSegmentParser;

/**
    Parser for a label segment subheader in a NITF 2.0 file.
*/
public class LabelSegmentHeaderParser extends AbstractNitfSegmentParser {

    private int labelExtendedSubheaderLength = 0;

    private LabelSegmentHeaderImpl segment = null;

    /**
     * default constructor.
     */
    public LabelSegmentHeaderParser() {
    }

    /**
     *
     * @param nitfReader the reader to use to get the data
     * @param parseStrategy the parsing strategy to use to process the data
     * @return the parsed header
     * @throws ParseException on parse failure
     *
     */
    public final LabelSegmentHeader parse(final NitfReader nitfReader, final NitfParseStrategy parseStrategy) throws ParseException {

        reader = nitfReader;
        parsingStrategy = parseStrategy;
        segment = new LabelSegmentHeaderImpl();

        readLA();
        readLID();
        segment.setSecurityMetadata(new SecurityMetadata(reader));
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
        TreCollection extendedSubheaderTREs = parsingStrategy.parseTREs(reader, labelExtendedSubheaderLength - LXSOFL_LENGTH);
        segment.mergeTREs(extendedSubheaderTREs);
    }
}
