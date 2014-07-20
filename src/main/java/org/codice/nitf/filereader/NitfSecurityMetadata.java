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

    public NitfSecurityMetadata(NitfReader nitfReader) throws ParseException {
        reader = nitfReader;
        readCommonSecurityMetadata();
    }

    public NitfSecurityClassification getSecurityClassification() {
        return nitfSecurityClassification;
    }

    public String getSecurityClassificationSystem() {
        return nitfSecurityClassificationSystem;
    }

    public String getCodewords() {
        return nitfCodewords;
    }

    public String getControlAndHandling() {
        return nitfControlAndHandling;
    }

    public String getReleaseInstructions() {
        return nitfReleaseInstructions;
    }

    public String getDeclassificationType() {
        return nitfDeclassificationType;
    }

    public String getDeclassificationDate() {
        return nitfDeclassificationDate;
    }

    public String getDeclassificationExemption() {
        return nitfDeclassificationExemption;
    }

    public String getDowngrade() {
        return nitfDowngrade;
    }

    public String getDowngradeDate() {
        return nitfDowngradeDate;
    }

    public String getClassificationText() {
        return nitfClassificationText;
    }

    public String getClassificationAuthorityType() {
        return nitfClassificationAuthorityType;
    }

    public String getClassificationAuthority() {
        return nitfClassificationAuthority;
    }

    public String getClassificationReason() {
        return nitfClassificationReason;
    }

    public String getSecuritySourceDate() {
        return nitfSecuritySourceDate;
    }

    public String getSecurityControlNumber() {
        return nitfSecurityControlNumber;
    }

    protected void readCommonSecurityMetadata() throws ParseException {
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

};

