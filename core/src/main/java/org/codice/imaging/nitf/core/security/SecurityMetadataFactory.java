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
        defaultMetadata.setFileCopyNumber("");
        defaultMetadata.setFileNumberOfCopies("");
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
        meta.setFileType(fileType);
        meta.setSecurityClassification(SecurityClassification.UNCLASSIFIED);
        meta.setSecurityClassificationSystem("");
        if (fileType.equals(FileType.NITF_TWO_ONE) || fileType.equals(FileType.NSIF_ONE_ZERO)) {
            meta.setCodewords("");
            meta.setControlAndHandling("");
            meta.setReleaseInstructions("");
            meta.setDeclassificationType("");
            meta.setDeclassificationDate("");
            meta.setDeclassificationExemption("");
            meta.setDowngrade("");
            meta.setDowngradeDate("");
            meta.setClassificationText("");
            meta.setClassificationAuthorityType("");
            meta.setClassificationAuthority("");
            meta.setClassificationReason("");
            meta.setSecuritySourceDate("");
            meta.setSecurityControlNumber("");
        } else if (fileType.equals(FileType.NITF_TWO_ZERO)) {
            meta.setSecurityClassificationSystem(null);
            meta.setCodewords("");
            meta.setControlAndHandling("");
            meta.setReleaseInstructions("");
            meta.setClassificationAuthority("");
            meta.setSecurityControlNumber("");
            meta.setDowngradeDateOrSpecialCase("");
            // Do not need Security Downgrade here - its conditional on the previous field being "999998"
        }
    }
}
