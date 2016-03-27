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
package org.codice.imaging.nitf.core.security;

import java.io.DataOutput;
import java.io.IOException;
import org.codice.imaging.nitf.core.common.AbstractSegmentWriter;
import org.codice.imaging.nitf.core.common.FileType;
import static org.codice.imaging.nitf.core.security.FileSecurityConstants.FSCOP_LENGTH;
import static org.codice.imaging.nitf.core.security.FileSecurityConstants.FSCPYS_LENGTH;
import static org.codice.imaging.nitf.core.security.SecurityConstants.XSCATP_LENGTH;
import static org.codice.imaging.nitf.core.security.SecurityConstants.XSCAUT20_LENGTH;
import static org.codice.imaging.nitf.core.security.SecurityConstants.XSCAUT_LENGTH;
import static org.codice.imaging.nitf.core.security.SecurityConstants.XSCLAS_LENGTH;
import static org.codice.imaging.nitf.core.security.SecurityConstants.XSCLSY_LENGTH;
import static org.codice.imaging.nitf.core.security.SecurityConstants.XSCLTX_LENGTH;
import static org.codice.imaging.nitf.core.security.SecurityConstants.XSCODE20_LENGTH;
import static org.codice.imaging.nitf.core.security.SecurityConstants.XSCODE_LENGTH;
import static org.codice.imaging.nitf.core.security.SecurityConstants.XSCRSN_LENGTH;
import static org.codice.imaging.nitf.core.security.SecurityConstants.XSCTLH20_LENGTH;
import static org.codice.imaging.nitf.core.security.SecurityConstants.XSCTLH_LENGTH;
import static org.codice.imaging.nitf.core.security.SecurityConstants.XSCTLN20_LENGTH;
import static org.codice.imaging.nitf.core.security.SecurityConstants.XSCTLN_LENGTH;
import static org.codice.imaging.nitf.core.security.SecurityConstants.XSDCDT_LENGTH;
import static org.codice.imaging.nitf.core.security.SecurityConstants.XSDCTP_LENGTH;
import static org.codice.imaging.nitf.core.security.SecurityConstants.XSDCXM_LENGTH;
import static org.codice.imaging.nitf.core.security.SecurityConstants.XSDEVT20_LENGTH;
import static org.codice.imaging.nitf.core.security.SecurityConstants.XSDGDT_LENGTH;
import static org.codice.imaging.nitf.core.security.SecurityConstants.XSDG_LENGTH;
import static org.codice.imaging.nitf.core.security.SecurityConstants.XSDWNG20_LENGTH;
import static org.codice.imaging.nitf.core.security.SecurityConstants.XSREL20_LENGTH;
import static org.codice.imaging.nitf.core.security.SecurityConstants.XSREL_LENGTH;
import static org.codice.imaging.nitf.core.security.SecurityConstants.XSSRDT_LENGTH;
import org.codice.imaging.nitf.core.tre.TreParser;

/**
 * Writer for security metadata.
 */
public class SecurityMetadataWriter extends AbstractSegmentWriter {

    /**
     * Constructor.
     *
     * @param mOutput the target to write the security metadata to.
     * @param mTreParser not used here, but kept for commonality.
     */
    public SecurityMetadataWriter(final DataOutput mOutput, final TreParser mTreParser) {
        super(mOutput, mTreParser);
    }

    /**
     * Write out normal segment-level security metadata.
     *
     * @param securityMetadata the metadata to write
     * @throws IOException on write failure.
     */
    public final void writeMetadata(final SecurityMetadata securityMetadata) throws IOException {
        if (securityMetadata.getFileType() == FileType.NITF_TWO_ZERO) {
            writeSecurityMetadata20(securityMetadata);
        } else {
            writeSecurityMetadata21(securityMetadata);
        }
    }

    private void writeSecurityMetadata20(final SecurityMetadata securityMetadata) throws IOException {
        writeFixedLengthString(securityMetadata.getSecurityClassification().getTextEquivalent(), XSCLAS_LENGTH);
        writeFixedLengthString(securityMetadata.getCodewords(), XSCODE20_LENGTH);
        writeFixedLengthString(securityMetadata.getControlAndHandling(), XSCTLH20_LENGTH);
        writeFixedLengthString(securityMetadata.getReleaseInstructions(), XSREL20_LENGTH);
        writeFixedLengthString(securityMetadata.getClassificationAuthority(), XSCAUT20_LENGTH);
        writeFixedLengthString(securityMetadata.getSecurityControlNumber(), XSCTLN20_LENGTH);
        writeFixedLengthString(securityMetadata.getDowngradeDateOrSpecialCase(), XSDWNG20_LENGTH);
        if (securityMetadata.hasDowngradeMagicValue()) {
            writeFixedLengthString(securityMetadata.getDowngradeEvent(), XSDEVT20_LENGTH);
        }
    }

    private void writeSecurityMetadata21(final SecurityMetadata securityMetadata) throws IOException {
        writeFixedLengthString(securityMetadata.getSecurityClassification().getTextEquivalent(), XSCLAS_LENGTH);
        writeFixedLengthString(securityMetadata.getSecurityClassificationSystem(), XSCLSY_LENGTH);
        writeFixedLengthString(securityMetadata.getCodewords(), XSCODE_LENGTH);
        writeFixedLengthString(securityMetadata.getControlAndHandling(), XSCTLH_LENGTH);
        writeFixedLengthString(securityMetadata.getReleaseInstructions(), XSREL_LENGTH);
        writeFixedLengthString(securityMetadata.getDeclassificationType(), XSDCTP_LENGTH);
        writeFixedLengthString(securityMetadata.getDeclassificationDate(), XSDCDT_LENGTH);
        writeFixedLengthString(securityMetadata.getDeclassificationExemption(), XSDCXM_LENGTH);
        writeFixedLengthString(securityMetadata.getDowngrade(), XSDG_LENGTH);
        writeFixedLengthString(securityMetadata.getDowngradeDate(), XSDGDT_LENGTH);
        writeFixedLengthString(securityMetadata.getClassificationText(), XSCLTX_LENGTH);
        writeFixedLengthString(securityMetadata.getClassificationAuthorityType(), XSCATP_LENGTH);
        writeFixedLengthString(securityMetadata.getClassificationAuthority(), XSCAUT_LENGTH);
        writeFixedLengthString(securityMetadata.getClassificationReason(), XSCRSN_LENGTH);
        writeFixedLengthString(securityMetadata.getSecuritySourceDate(), XSSRDT_LENGTH);
        writeFixedLengthString(securityMetadata.getSecurityControlNumber(), XSCTLN_LENGTH);
    }

    /**
     * Write file-header level security metadata.
     *
     * @param fsmeta the file-level security metadata to write out
     * @throws IOException on write failure.
     */
    public final void writeFileSecurityMetadata(final FileSecurityMetadata fsmeta) throws IOException {
        writeSecurityMetadata(fsmeta);
        writeFixedLengthString(fsmeta.getFileCopyNumber(), FSCOP_LENGTH);
        writeFixedLengthString(fsmeta.getFileNumberOfCopies(), FSCPYS_LENGTH);
    }
}
