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

import org.codice.imaging.nitf.core.common.FileType;
import org.codice.imaging.nitf.core.security.SecurityClassification;
import org.codice.imaging.nitf.core.security.SecurityMetadata;

import static org.codice.imaging.nitf.core.security.impl.SecurityConstants.DOWNGRADE_EVENT_MAGIC;
import static org.codice.imaging.nitf.core.security.impl.SecurityConstants.XSDEVT20_LENGTH;

/**
 * Security metadata for a NITF file header or segment subheader.
 */
class SecurityMetadataImpl implements SecurityMetadata {

    private FileType nitfFileType = FileType.NITF_TWO_ONE;

    private SecurityClassification securityClassification = SecurityClassification.UNKNOWN;
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

    /**
        Default constructor.
    */
    SecurityMetadataImpl() {
    }

    public void setFileType(final FileType fileType) {
        nitfFileType = fileType;
    }

    @Override
    public final FileType getFileType() {
        return nitfFileType;
    }

    /**
     * {@inheritDoc}
     */
    public final void setSecurityClassification(final SecurityClassification classification) {
        this.securityClassification = classification;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final SecurityClassification getSecurityClassification() {
        return securityClassification;
    }

    /**
     * {@inheritDoc}
     */
    public final void setSecurityClassificationSystem(final String securityClassificationSystem) {
        nitfSecurityClassificationSystem = securityClassificationSystem;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String getSecurityClassificationSystem() {
        return nitfSecurityClassificationSystem;
    }

    /**
     * {@inheritDoc}
     */
    public final void setCodewords(final String codewords) {
        nitfCodewords = codewords;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String getCodewords() {
        return nitfCodewords;
    }

    /**
     * {@inheritDoc}
     */
    public final void setControlAndHandling(final String instructions) {
        nitfControlAndHandling = instructions;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String getControlAndHandling() {
        return nitfControlAndHandling;
    }

    /**
     * {@inheritDoc}
     */
    public final void setReleaseInstructions(final String releaseInstructions) {
        nitfReleaseInstructions = releaseInstructions;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String getReleaseInstructions() {
        return nitfReleaseInstructions;
    }

    /**
     * {@inheritDoc}
     */
    public final void setDeclassificationType(final String declassificationType) {
        nitfDeclassificationType = declassificationType;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String getDeclassificationType() {
        return nitfDeclassificationType;
    }

    /**
     * {@inheritDoc}
     */
    public final void setDeclassificationDate(final String declassificationDate) {
        nitfDeclassificationDate = declassificationDate;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String getDeclassificationDate() {
        return nitfDeclassificationDate;
    }

    /**
     * {@inheritDoc}
     */
    public final void setDeclassificationExemption(final String declassificationExemption) {
        nitfDeclassificationExemption = declassificationExemption;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String getDeclassificationExemption() {
        return nitfDeclassificationExemption;
    }

    /**
     * {@inheritDoc}
     */
    public final void setDowngrade(final String downgrade) {
        nitfDowngrade = downgrade;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String getDowngrade() {
        return nitfDowngrade;
    }

    /**
     * {@inheritDoc}
     */
    public final void setDowngradeDate(final String downgradeDate) {
        nitfDowngradeDate = downgradeDate;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String getDowngradeDate() {
        return nitfDowngradeDate;
    }

    /**
     * {@inheritDoc}
     */
    public final void setDowngradeDateOrSpecialCase(final String dateOrSpecialCase) {
        downgradeDateOrSpecialCase = dateOrSpecialCase;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String getDowngradeDateOrSpecialCase() {
        return downgradeDateOrSpecialCase;
    }

    /**
     * {@inheritDoc}
     */
    public final void setDowngradeEvent(final String event) {
        downgradeEvent = event;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String getDowngradeEvent() {
        return downgradeEvent;
    }

    /**
     * {@inheritDoc}
     */
    public final void setClassificationText(final String classificationText) {
        nitfClassificationText = classificationText;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String getClassificationText() {
        return nitfClassificationText;
    }

    /**
     * {@inheritDoc}
     */
    public final void setClassificationAuthorityType(final String classificationAuthorityType) {
        nitfClassificationAuthorityType = classificationAuthorityType;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String getClassificationAuthorityType() {
        return nitfClassificationAuthorityType;
    }

    /**
     * {@inheritDoc}
     */
    public final void setClassificationAuthority(final String classificationAuthority) {
        nitfClassificationAuthority = classificationAuthority;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String getClassificationAuthority() {
        return nitfClassificationAuthority;
    }

    /**
     * {@inheritDoc}
     */
    public final void setClassificationReason(final String classificationReason) {
        nitfClassificationReason = classificationReason;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String getClassificationReason() {
        return nitfClassificationReason;
    }

    /**
     * {@inheritDoc}
     */
    public final void setSecuritySourceDate(final String securitySourceDate) {
        nitfSecuritySourceDate = securitySourceDate;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String getSecuritySourceDate() {
        return nitfSecuritySourceDate;
    }

    /**
     * {@inheritDoc}
     */
    public final void setSecurityControlNumber(final String securityControlNumber) {
        nitfSecurityControlNumber = securityControlNumber;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String getSecurityControlNumber() {
        return nitfSecurityControlNumber;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasDowngradeMagicValue() {
        return (DOWNGRADE_EVENT_MAGIC.equals(getDowngradeDateOrSpecialCase()));
    }

    @Override
    public long getSerialisedLength() {
        long len = SecurityConstants.XSCLAS_LENGTH + SecurityConstants.XSCLSY_LENGTH + SecurityConstants.XSCODE_LENGTH
                + SecurityConstants.XSCTLH_LENGTH + SecurityConstants.XSREL_LENGTH + SecurityConstants.XSDCTP_LENGTH
                + SecurityConstants.XSDCDT_LENGTH + SecurityConstants.XSDCXM_LENGTH + SecurityConstants.XSDG_LENGTH
                + SecurityConstants.XSDGDT_LENGTH + SecurityConstants.XSCLTX_LENGTH + SecurityConstants.XSCATP_LENGTH
                + SecurityConstants.XSCAUT_LENGTH + SecurityConstants.XSCRSN_LENGTH + SecurityConstants.XSSRDT_LENGTH
                + SecurityConstants.XSCTLN_LENGTH;
        len += calculateExtraHeaderLength();
        return len;
    }

    /**
     * Calculate the extra header length that can occur in NITF 2.0 files for downgrades.
     *
     * @return the number of additional bytes that will be required for special downgrade text.
     */
    private int calculateExtraHeaderLength() {
        if ((getFileType().equals(FileType.NITF_TWO_ZERO)) && hasDowngradeMagicValue()) {
            return XSDEVT20_LENGTH;
        }
        return 0;
    }
};
