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
package org.codice.imaging.nitf.core.security.impl;

import org.codice.imaging.nitf.core.common.NitfFormatException;
import org.codice.imaging.nitf.core.common.NitfReader;
import org.codice.imaging.nitf.core.security.SecurityClassification;
import org.codice.imaging.nitf.core.security.SecurityMetadata;

import static org.codice.imaging.nitf.core.security.impl.SecurityConstants.XSDEVT20_LENGTH;

/**
    Parser for security metadata.
*/
public class SecurityMetadataParser {
    /**
     * The nitfReader which supplies the data to be parsed.
     */
    protected NitfReader reader = null;
    private SecurityMetadataImpl metadata = null;

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

    private static final String DOWNGRADE_EVENT_MAGIC = "999998";

    /**
     * Default constructor.
     */
    public SecurityMetadataParser() {
        // Intentionally Empty
    }

    /**
     * Parses data from the given nitfReader and creates a SecurityMetadata object from that.
     *
     * @param nitfReader the NITF source data.
     * @return the fully populated SecurityMetaData object.
     * @throws NitfFormatException when the input isn't what was expected.
     */
    public final SecurityMetadata parseSecurityMetadata(final NitfReader nitfReader) throws NitfFormatException {
        this.metadata = new SecurityMetadataImpl();
        doParse(nitfReader, this.metadata);
        return this.metadata;
    }

    /**
     * Do low level parse.
     *
     * This is only exposed for re-use with a specific subclass, and is not general API.
     *
     * @param nitfReader the NITF source data.
     * @param securityMetadata the SecurityMetadataImpl class to be populated.
     * @throws NitfFormatException when the input isn't what was expected.
     *
     * @see parseSecurityMetadata for the real API.
     */
    protected final void doParse(final NitfReader nitfReader, final SecurityMetadataImpl securityMetadata) throws NitfFormatException {
        reader = nitfReader;
        metadata = securityMetadata;
        securityMetadata.setFileType(nitfReader.getFileType());

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
                throw new NitfFormatException("Need to set NITF version before reading metadata", (int) reader.getCurrentOffset());
         }
    }

    /**
     * reads the common security metadata elements.
     *
     * @throws NitfFormatException when the input isn't what was expected.
     */
    protected final void readCommonSecurityMetadata() throws NitfFormatException {
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

    /**
     * reads the Nitf 2.0 file security attributes.
     *
     * @throws NitfFormatException when the input isn't what was expected.
     */
    protected final void readNitf20FileSecurityItems() throws NitfFormatException {
        readXSCLAS();
        readXSCODE20();
        readXSCTLH20();
        readXSREL20();
        readXSCAUT20();
        readXSCTLN20();
        readXSDWNG20();
        readXSDEVT20();
    }

    private void readXSCLAS() throws NitfFormatException {
        String fsclas = reader.readBytes(XSCLAS_LENGTH);
        metadata.setSecurityClassification(SecurityClassification.getEnumValue(fsclas));
    }

    private void readXSCLSY() throws NitfFormatException {
        metadata.setSecurityClassificationSystem(reader.readTrimmedBytes(XSCLSY_LENGTH));
    }

    private void readXSCODE() throws NitfFormatException {
        metadata.setCodewords(reader.readTrimmedBytes(XSCODE_LENGTH));
    }

    private void readXSCTLH() throws NitfFormatException {
        metadata.setControlAndHandling(reader.readTrimmedBytes(XSCTLH_LENGTH));
    }

    private void readXSREL() throws NitfFormatException {
        metadata.setReleaseInstructions(reader.readTrimmedBytes(XSREL_LENGTH));
    }

    private void readXSDCTP() throws NitfFormatException {
        metadata.setDeclassificationType(reader.readTrimmedBytes(XSDCTP_LENGTH));
    }

    private void readXSDCDT() throws NitfFormatException {
        metadata.setDeclassificationDate(reader.readTrimmedBytes(XSDCDT_LENGTH));
    }

    private void readXSDCXM() throws NitfFormatException {
        metadata.setDeclassificationExemption(reader.readTrimmedBytes(XSDCXM_LENGTH));
    }

    private void readXSDG() throws NitfFormatException {
        metadata.setDowngrade(reader.readTrimmedBytes(XSDG_LENGTH));
    }

    private void readXSDGDT() throws NitfFormatException {
        metadata.setDowngradeDate(reader.readTrimmedBytes(XSDGDT_LENGTH));
    }

    private void readXSCLTX() throws NitfFormatException {
        metadata.setClassificationText(reader.readTrimmedBytes(XSCLTX_LENGTH));
    }

    private void readXSCATP() throws NitfFormatException {
        metadata.setClassificationAuthorityType(reader.readTrimmedBytes(XSCATP_LENGTH));
    }

    private void readXSCAUT() throws NitfFormatException {
        metadata.setClassificationAuthority(reader.readTrimmedBytes(XSCAUT_LENGTH));
    }

    private void readXSCRSN() throws NitfFormatException {
        metadata.setClassificationReason(reader.readTrimmedBytes(XSCRSN_LENGTH));
    }

    private void readXSSRDT() throws NitfFormatException {
        metadata.setSecuritySourceDate(reader.readTrimmedBytes(XSSRDT_LENGTH));
    }

    private void readXSCTLN() throws NitfFormatException {
        metadata.setSecurityControlNumber(reader.readTrimmedBytes(XSCTLN_LENGTH));
    }

    private void readXSCODE20() throws NitfFormatException {
        metadata.setCodewords(reader.readTrimmedBytes(XSCODE20_LENGTH));
    }

    private void readXSCTLH20() throws NitfFormatException {
        metadata.setControlAndHandling(reader.readTrimmedBytes(XSCTLH20_LENGTH));
    }

    private void readXSREL20() throws NitfFormatException {
        metadata.setReleaseInstructions(reader.readTrimmedBytes(XSREL20_LENGTH));
    }

    private void readXSCAUT20() throws NitfFormatException {
        metadata.setClassificationAuthority(reader.readTrimmedBytes(XSCAUT20_LENGTH));
    }

    private void readXSCTLN20() throws NitfFormatException {
        metadata.setSecurityControlNumber(reader.readTrimmedBytes(XSCTLN20_LENGTH));
    }

    private void readXSDWNG20() throws NitfFormatException {
        metadata.setDowngradeDateOrSpecialCase(reader.readBytes(XSDWNG20_LENGTH));
    }

    private void readXSDEVT20() throws NitfFormatException {
        if (DOWNGRADE_EVENT_MAGIC.equals(metadata.getDowngradeDateOrSpecialCase())) {
            metadata.setDowngradeEvent(reader.readTrimmedBytes(XSDEVT20_LENGTH));
        }
    }

}

