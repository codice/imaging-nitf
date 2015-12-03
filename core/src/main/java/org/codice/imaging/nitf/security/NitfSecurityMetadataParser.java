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
package org.codice.imaging.nitf.security;

import java.text.ParseException;

import org.codice.imaging.nitf.common.reader.NitfReader;
import org.codice.imaging.nitf.common.security.NitfSecurityClassification;

/**
    Parser for security metadata.
*/
class NitfSecurityMetadataParser {
    protected NitfReader reader = null;
    private NitfSecurityMetadataImpl metadata = null;

    private static final int XSCLAS_LENGTH = 1;
    private static final int XSCLSY_LENGTH = 2;
    private static final int XSCODE_LENGTH = 11;
    private static final int XSCTLH_LENGTH = 2;
    private static final int XSREL_LENGTH = 20;
    private static final int XSDCTP_LENGTH = 2;
    private static final int XSDCDT_LENGTH = 8;
    private static final int XSDCXM_LENGTH = 4;
    private static final int XSDG_LENGTH = 1;
    private static final int XSDGDT_LENGTH = 8;
    private static final int XSCLTX_LENGTH = 43;
    private static final int XSCATP_LENGTH = 1;
    private static final int XSCAUT_LENGTH = 40;
    private static final int XSCRSN_LENGTH = 1;
    private static final int XSSRDT_LENGTH = 8;
    private static final int XSCTLN_LENGTH = 15;

    // NITF 2.0 field lengths
    private static final int XSCODE20_LENGTH = 40;
    private static final int XSCTLH20_LENGTH = 40;
    private static final int XSREL20_LENGTH = 40;
    private static final int XSCAUT20_LENGTH = 20;
    private static final int XSCTLN20_LENGTH = 20;
    private static final int XSDWNG20_LENGTH = 6;
    private static final int XSDEVT20_LENGTH = 40;

    private static final String DOWNGRADE_EVENT_MAGIC = "999998";

    public void parse(final NitfReader nitfReader, final NitfSecurityMetadataImpl nitfSecurityMetadataImpl) throws ParseException {
        this.metadata = nitfSecurityMetadataImpl;
        doParse(nitfReader);
    }

    protected void doParse(final NitfReader nitfReader) throws ParseException {
        reader = nitfReader;

        switch (nitfReader.getFileType()) {
            case NITF_TWO_ZERO:
                readNitf20FileSecurityItems();
                break;
            case NITF_TWO_ONE:
            case NSIF_ONE_ZERO:
                readCommonSecurityMetadata();
                break;
            case UNKNOWN:
            default:
                throw new ParseException("Need to set NITF version before reading metadata", (int) reader.getCurrentOffset());
         }
    }

    protected final void readCommonSecurityMetadata() throws ParseException {
        readXSCLAS();
        readXSCLSY();
        readXSCODE();
        readXSCTLH();
        readXSREL();
        readXSDCTP();
        readXSDCDT();
        readXSDCXM();
        readXSDG();
        readXSDGDT();
        readXSCLTX();
        readXSCATP();
        readXSCAUT();
        readXSCRSN();
        readXSSRDT();
        readXSCTLN();
    }

    protected final void readNitf20FileSecurityItems() throws ParseException {
        readXSCLAS();
        readXSCODE20();
        readXSCTLH20();
        readXSREL20();
        readXSCAUT20();
        readXSCTLN20();
        readXSDWNG20();
        readXSDEVT20();
    }

    private void readXSCLAS() throws ParseException {
        String fsclas = reader.readBytes(XSCLAS_LENGTH);
        metadata.setSecurityClassification(NitfSecurityClassification.getEnumValue(fsclas));
    }

    private void readXSCLSY() throws ParseException {
        metadata.setSecurityClassificationSystem(reader.readTrimmedBytes(XSCLSY_LENGTH));
    }

    private void readXSCODE() throws ParseException {
        metadata.setCodewords(reader.readTrimmedBytes(XSCODE_LENGTH));
    }

    private void readXSCTLH() throws ParseException {
        metadata.setControlAndHandling(reader.readTrimmedBytes(XSCTLH_LENGTH));
    }

    private void readXSREL() throws ParseException {
        metadata.setReleaseInstructions(reader.readTrimmedBytes(XSREL_LENGTH));
    }

    private void readXSDCTP() throws ParseException {
        metadata.setDeclassificationType(reader.readTrimmedBytes(XSDCTP_LENGTH));
    }

    private void readXSDCDT() throws ParseException {
        metadata.setDeclassificationDate(reader.readTrimmedBytes(XSDCDT_LENGTH));
    }

    private void readXSDCXM() throws ParseException {
        metadata.setDeclassificationExemption(reader.readTrimmedBytes(XSDCXM_LENGTH));
    }

    private void readXSDG() throws ParseException {
        metadata.setDowngrade(reader.readTrimmedBytes(XSDG_LENGTH));
    }

    private void readXSDGDT() throws ParseException {
        metadata.setDowngradeDate(reader.readTrimmedBytes(XSDGDT_LENGTH));
    }

    private void readXSCLTX() throws ParseException {
        metadata.setClassificationText(reader.readTrimmedBytes(XSCLTX_LENGTH));
    }

    private void readXSCATP() throws ParseException {
        metadata.setClassificationAuthorityType(reader.readTrimmedBytes(XSCATP_LENGTH));
    }

    private void readXSCAUT() throws ParseException {
        metadata.setClassificationAuthority(reader.readTrimmedBytes(XSCAUT_LENGTH));
    }

    private void readXSCRSN() throws ParseException {
        metadata.setClassificationReason(reader.readTrimmedBytes(XSCRSN_LENGTH));
    }

    private void readXSSRDT() throws ParseException {
        metadata.setSecuritySourceDate(reader.readTrimmedBytes(XSSRDT_LENGTH));
    }

    private void readXSCTLN() throws ParseException {
        metadata.setSecurityControlNumber(reader.readTrimmedBytes(XSCTLN_LENGTH));
    }

    private void readXSCODE20() throws ParseException {
        metadata.setCodewords(reader.readTrimmedBytes(XSCODE20_LENGTH));
    }

    private void readXSCTLH20() throws ParseException {
        metadata.setControlAndHandling(reader.readTrimmedBytes(XSCTLH20_LENGTH));
    }

    private void readXSREL20() throws ParseException {
        metadata.setReleaseInstructions(reader.readTrimmedBytes(XSREL20_LENGTH));
    }

    private void readXSCAUT20() throws ParseException {
        metadata.setClassificationAuthority(reader.readTrimmedBytes(XSCAUT20_LENGTH));
    }

    private void readXSCTLN20() throws ParseException {
        metadata.setSecurityControlNumber(reader.readTrimmedBytes(XSCTLN20_LENGTH));
    }

    private void readXSDWNG20() throws ParseException {
        metadata.setDowngradeDateOrSpecialCase(reader.readBytes(XSDWNG20_LENGTH));
    }

    private void readXSDEVT20() throws ParseException {
        if (DOWNGRADE_EVENT_MAGIC.equals(metadata.getDowngradeDateOrSpecialCase())) {
            metadata.setDowngradeEvent(reader.readTrimmedBytes(XSDEVT20_LENGTH));
        }
    }

};

