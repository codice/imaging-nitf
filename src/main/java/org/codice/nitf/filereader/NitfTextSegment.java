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
import java.util.Date;

public class NitfTextSegment {
    private NitfReader reader = null;
    private int lengthOfText = 0;
    private Date textDateTime = null;
    private NitfSecurityMetadata securityMetadata = null;
    private int textExtendedSubheaderLength = 0;

    private String textIdentifier = null;
    private int textAttachmentLevel = 0;
    private String textTitle = null;
    private TextFormat textFormat = TextFormat.UNKNOWN;

    private static final String TE = "TE";
    private static final int TEXTID_LENGTH = 7;
    private static final int TXTALVL_LENGTH = 3;
    private static final int TXTITL_LENGTH = 80;
    private static final int TXTFMT_LENGTH = 3;
    private static final int TXSHDL_LENGTH = 5;

    public NitfTextSegment(final NitfReader nitfReader, final int textLength) throws ParseException {
        reader = nitfReader;
        lengthOfText = textLength;

        readTE();
        readTEXTID();
        readTXTALVL();
        readTEXTDT();
        readTXTITL();
        securityMetadata = new NitfSecurityMetadata(reader);
        reader.readENCRYP();
        readTXTFMT();
        readTXSHDL();
        if (textExtendedSubheaderLength > 0) {
            // TODO: find a case that exercises this and implement it
            throw new UnsupportedOperationException("IMPLEMENT TXSOFL / TXSHD PARSING");
        }
        readTextData();
    }

    public final String getTextIdentifier() {
        return textIdentifier;
    }

    public final int getTextAttachmentLevel() {
        return textAttachmentLevel;
    }

    public final Date getTextDateTime() {
        return textDateTime;
    }

    public final String getTextTitle() {
        return textTitle;
    }

    public final NitfSecurityMetadata getSecurityMetadata() {
        return securityMetadata;
    }

    public final TextFormat getTextFormat() {
        return textFormat;
    }

    public final int getTextExtendedSubheaderLength() {
        return textExtendedSubheaderLength;
    }

    private void readTE() throws ParseException {
       reader.verifyHeaderMagic(TE);
    }

    private void readTEXTID() throws ParseException {
        textIdentifier = reader.readBytes(TEXTID_LENGTH);
    }

    private void readTXTALVL() throws ParseException {
        textAttachmentLevel = reader.readBytesAsInteger(TXTALVL_LENGTH);
    }

    private void readTEXTDT() throws ParseException {
        textDateTime = reader.readNitfDateTime();
    }

    private void readTXTITL() throws ParseException {
        textTitle = reader.readTrimmedBytes(TXTITL_LENGTH);
    }

    private void readTXTFMT() throws ParseException {
        String txtfmt = reader.readTrimmedBytes(TXTFMT_LENGTH);
        textFormat = TextFormat.getEnumValue(txtfmt);
    }

    private void readTXSHDL() throws ParseException {
        textExtendedSubheaderLength = reader.readBytesAsInteger(TXSHDL_LENGTH);
    }

    private void readTextData() throws ParseException {
        // TODO: we could use this if needed later
        reader.skip(lengthOfText);
    }
}
