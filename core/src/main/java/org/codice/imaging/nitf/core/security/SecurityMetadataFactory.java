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

import org.codice.imaging.nitf.core.common.FileType;
import static org.codice.imaging.nitf.core.security.FileSecurityConstants.FSCOP_LENGTH;
import static org.codice.imaging.nitf.core.security.FileSecurityConstants.FSCPYS_LENGTH;
import static org.codice.imaging.nitf.core.security.SecurityConstants.XSCATP_LENGTH;
import static org.codice.imaging.nitf.core.security.SecurityConstants.XSCAUT20_LENGTH;
import static org.codice.imaging.nitf.core.security.SecurityConstants.XSCAUT_LENGTH;
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
import static org.codice.imaging.nitf.core.security.SecurityConstants.XSDGDT_LENGTH;
import static org.codice.imaging.nitf.core.security.SecurityConstants.XSDG_LENGTH;
import static org.codice.imaging.nitf.core.security.SecurityConstants.XSDWNG20_LENGTH;
import static org.codice.imaging.nitf.core.security.SecurityConstants.XSREL20_LENGTH;
import static org.codice.imaging.nitf.core.security.SecurityConstants.XSREL_LENGTH;
import static org.codice.imaging.nitf.core.security.SecurityConstants.XSSRDT_LENGTH;

/**
 * Factory for SecurityMetadata and FileSecurityMetadata instances.
 */
public final class SecurityMetadataFactory {

    private SecurityMetadataFactory() {
    }

    /**
     * Create default file-level security metadata.
     *
     * This is intended for making base (default) level security metadata, and some changes will probably be required
     * depending on application needs. In particular, you probably need to set the security classification system to
     * reflect your national needs if you care about these fields.
     *
     * @param fileType the type (version) of NITF file to build the security metadata for.
     * @return default unclassified security metadata
     */
    public static FileSecurityMetadata getDefaultFileSecurityMetadata(final FileType fileType) {
        FileSecurityMetadataImpl defaultMetadata = new FileSecurityMetadataImpl();
        fillDefaultMetadata(defaultMetadata, fileType);
        defaultMetadata.setFileCopyNumber(spaceFillForLength(FSCOP_LENGTH));
        defaultMetadata.setFileNumberOfCopies(spaceFillForLength(FSCPYS_LENGTH));
        return defaultMetadata;
    }

    /**
     * Create default security metadata structure.
     *
     * This is intended for making base (default) level security metadata, and some changes will probably be required
     * depending on application needs. In particular, you probably need to set the security classification system to
     * reflect your national needs if you care about these fields.
     *
     * @param fileType the type of NITF file to generate security metadata for
     * @return defaulted metadata structure
     */
    public static SecurityMetadata getDefaultMetadata(final FileType fileType) {
        SecurityMetadataImpl meta = new SecurityMetadataImpl();
        fillDefaultMetadata(meta, fileType);

        return meta;
    }

    private static void fillDefaultMetadata(final SecurityMetadataImpl meta, final FileType fileType) {
        meta.setSecurityClassification(SecurityClassification.UNCLASSIFIED);
        meta.setSecurityClassificationSystem(spaceFillForLength(XSCLSY_LENGTH));
        if (fileType.equals(FileType.NITF_TWO_ONE) || fileType.equals(FileType.NSIF_ONE_ZERO)) {
            meta.setCodewords(spaceFillForLength(XSCODE_LENGTH));
            meta.setControlAndHandling(spaceFillForLength(XSCTLH_LENGTH));
            meta.setReleaseInstructions(spaceFillForLength(XSREL_LENGTH));
            meta.setDeclassificationType(spaceFillForLength(XSDCTP_LENGTH));
            meta.setDeclassificationDate(spaceFillForLength(XSDCDT_LENGTH));
            meta.setDeclassificationExemption(spaceFillForLength(XSDCXM_LENGTH));
            meta.setDowngrade(spaceFillForLength(XSDG_LENGTH));
            meta.setDowngradeDate(spaceFillForLength(XSDGDT_LENGTH));
            meta.setClassificationText(spaceFillForLength(XSCLTX_LENGTH));
            meta.setClassificationAuthorityType(spaceFillForLength(XSCATP_LENGTH));
            meta.setClassificationAuthority(spaceFillForLength(XSCAUT_LENGTH));
            meta.setClassificationReason(spaceFillForLength(XSCRSN_LENGTH));
            meta.setSecuritySourceDate(spaceFillForLength(XSSRDT_LENGTH));
            meta.setSecurityControlNumber(spaceFillForLength(XSCTLN_LENGTH));
        } else if (fileType.equals(FileType.NITF_TWO_ZERO)) {
            meta.setCodewords(spaceFillForLength(XSCODE20_LENGTH));
            meta.setControlAndHandling(spaceFillForLength(XSCTLH20_LENGTH));
            meta.setReleaseInstructions(spaceFillForLength(XSREL20_LENGTH));
            meta.setClassificationAuthority(spaceFillForLength(XSCAUT20_LENGTH));
            meta.setSecurityControlNumber(spaceFillForLength(XSCTLN20_LENGTH));
            meta.setDowngradeDateOrSpecialCase(spaceFillForLength(XSDWNG20_LENGTH));
            // Do not need Security Downgrade here - its conditional on the previous field being "999998"
        }
    }

    /**
     * Create an empty (space-filled) string of specified length.
     *
     * @param length the length of the string to create
     * @return the space-filled string
     */
    private static String spaceFillForLength(final int length) {
        return String.format("%1$-" + length + "s", "");
    }
}
