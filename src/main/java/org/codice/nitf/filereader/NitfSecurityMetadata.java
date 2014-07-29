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

public class NitfSecurityMetadata {
    protected NitfReader reader = null;

    private NitfSecurityClassification nitfSecurityClassification = NitfSecurityClassification.UNKNOWN;
    private String nitfSecurityClassificationSystem = null;
    private String nitfCodewords = null;
    private String nitfControlAndHandling = null;
    private String nitfReleaseInstructions = null;
    // Could be an enumerated type
    private String nitfDeclassificationType = null;
    // String instead of Date because its frequently just an empty string
    private String nitfDeclassificationDate = null;
    private String nitfDeclassificationExemption = null;
    private String nitfDowngrade = null;
    // String instead of Date because its frequently just an empty string
    private String nitfDowngradeDate = null;
    private String nitfClassificationText = null;
    // Could be an enumerated type
    private String nitfClassificationAuthorityType = null;
    private String nitfClassificationAuthority = null;
    private String nitfClassificationReason = null;
    private String nitfSecuritySourceDate = null;
    private String nitfSecurityControlNumber = null;

    // NITF 2.0 values
    private String downgradeDateOrSpecialCase = null;
    private String downgradeEvent = null;

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

    public NitfSecurityMetadata(final NitfReader nitfReader) throws ParseException {
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
                throw new ParseException("Need to set NITF version before reading metadata", reader.getNumBytesRead());
         }
    }

    public final NitfSecurityClassification getSecurityClassification() {
        return nitfSecurityClassification;
    }

    public final String getSecurityClassificationSystem() {
        return nitfSecurityClassificationSystem;
    }

    public final String getCodewords() {
        return nitfCodewords;
    }

    public final String getControlAndHandling() {
        return nitfControlAndHandling;
    }

    public final String getReleaseInstructions() {
        return nitfReleaseInstructions;
    }

    public final String getDeclassificationType() {
        return nitfDeclassificationType;
    }

    public final String getDeclassificationDate() {
        return nitfDeclassificationDate;
    }

    public final String getDeclassificationExemption() {
        return nitfDeclassificationExemption;
    }

    public final String getDowngrade() {
        return nitfDowngrade;
    }

    public final String getDowngradeDate() {
        return nitfDowngradeDate;
    }

    /**
      Get the downgrade date or special case for this file.

      This is not valid on NITF 2.1 or NSIF 1.0 files.

      The valid values are:
      (1) the calendar date in the format YYMMDD
      (2) the code "999999" when the originating agency's determination is required (OADR)
      (3) the code "999998" when a specific event determines at what point declassification or downgrading is to take place.

      If the third case (999998) occurs, use getDowngradeEvent() to determine the downgrade event.
    */
    public final String getDowngradeDateOrSpecialCase() {
        return downgradeDateOrSpecialCase;
    }

    /**
      Get the specific downgrade event for this file.

      This is not valid on NITF 2.1 or NSIF 1.0 files.

      This is only valid if getDowngradeDateOrSpecialCase() is equal to 999998.
     */
    public final String getDowngradeEvent() {
        return downgradeEvent;
    }

    public final String getClassificationText() {
        return nitfClassificationText;
    }

    public final String getClassificationAuthorityType() {
        return nitfClassificationAuthorityType;
    }

    public final String getClassificationAuthority() {
        return nitfClassificationAuthority;
    }

    public final String getClassificationReason() {
        return nitfClassificationReason;
    }

    public final String getSecuritySourceDate() {
        return nitfSecuritySourceDate;
    }

    public final String getSecurityControlNumber() {
        return nitfSecurityControlNumber;
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
        readFSCODE20();
        readFSCTLH20();
        readFSREL20();
        readFSCAUT20();
        readFSCTLN20();
        readFSDWNG20();
        readFSDEVT20();
    }

    private void readXSCLAS() throws ParseException {
        String fsclas = reader.readBytes(XSCLAS_LENGTH);
        nitfSecurityClassification = NitfSecurityClassification.getEnumValue(fsclas);
    }

    private void readXSCLSY() throws ParseException {
        nitfSecurityClassificationSystem = reader.readTrimmedBytes(XSCLSY_LENGTH);
    }

    private void readXSCODE() throws ParseException {
        nitfCodewords = reader.readTrimmedBytes(XSCODE_LENGTH);
    }

    private void readXSCTLH() throws ParseException {
        nitfControlAndHandling = reader.readTrimmedBytes(XSCTLH_LENGTH);
    }

    private void readXSREL() throws ParseException {
        nitfReleaseInstructions = reader.readTrimmedBytes(XSREL_LENGTH);
    }

    private void readXSDCTP() throws ParseException {
        nitfDeclassificationType = reader.readTrimmedBytes(XSDCTP_LENGTH);
    }

    private void readXSDCDT() throws ParseException {
        nitfDeclassificationDate = reader.readTrimmedBytes(XSDCDT_LENGTH);
    }

    private void readXSDCXM() throws ParseException {
        nitfDeclassificationExemption = reader.readTrimmedBytes(XSDCXM_LENGTH);
    }

    private void readXSDG() throws ParseException {
        nitfDowngrade = reader.readTrimmedBytes(XSDG_LENGTH);
    }

    private void readXSDGDT() throws ParseException {
        nitfDowngradeDate = reader.readTrimmedBytes(XSDGDT_LENGTH);
    }

    private void readXSCLTX() throws ParseException {
        nitfClassificationText = reader.readTrimmedBytes(XSCLTX_LENGTH);
    }

    private void readXSCATP() throws ParseException {
        nitfClassificationAuthorityType = reader.readTrimmedBytes(XSCATP_LENGTH);
    }

    private void readXSCAUT() throws ParseException {
        nitfClassificationAuthority = reader.readTrimmedBytes(XSCAUT_LENGTH);
    }

    private void readXSCRSN() throws ParseException {
        nitfClassificationReason = reader.readTrimmedBytes(XSCRSN_LENGTH);
    }

    private void readXSSRDT() throws ParseException {
        nitfSecuritySourceDate = reader.readTrimmedBytes(XSSRDT_LENGTH);
    }

    private void readXSCTLN() throws ParseException {
        nitfSecurityControlNumber = reader.readTrimmedBytes(XSCTLN_LENGTH);
    }

    private void readFSCODE20() throws ParseException {
        nitfCodewords = reader.readTrimmedBytes(XSCODE20_LENGTH);
    }

    private void readFSCTLH20() throws ParseException {
        nitfControlAndHandling = reader.readTrimmedBytes(XSCTLH20_LENGTH);
    }

    private void readFSREL20() throws ParseException {
        nitfReleaseInstructions = reader.readTrimmedBytes(XSREL20_LENGTH);
    }

    private void readFSCAUT20() throws ParseException {
        nitfClassificationAuthority = reader.readTrimmedBytes(XSCAUT20_LENGTH);
    }

    private void readFSCTLN20() throws ParseException {
        nitfSecurityControlNumber = reader.readTrimmedBytes(XSCTLN20_LENGTH);
    }

    private void readFSDWNG20() throws ParseException {
        downgradeDateOrSpecialCase = reader.readBytes(XSDWNG20_LENGTH);
    }

    private void readFSDEVT20() throws ParseException {
        if (DOWNGRADE_EVENT_MAGIC.equals(downgradeDateOrSpecialCase)) {
            downgradeEvent = reader.readTrimmedBytes(XSDEVT20_LENGTH);
        }
    }

};

