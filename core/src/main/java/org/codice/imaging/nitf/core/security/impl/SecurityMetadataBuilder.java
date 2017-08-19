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

/**
 * Builder for SecurityMetadata.
 *
 * This is the shared code between SecurityMetadataBuilder20 (which handles
 * the NITF 2.0 variant) and SecurityMetadataBuilder21 (which handles NSIF
 * 1.0 and NITF 2.1).
 */
abstract class SecurityMetadataBuilder<T extends SecurityMetadataBuilder> {

    protected FileType nitfFileType = FileType.UNKNOWN;

    private SecurityClassification securityClassification = SecurityClassification.UNKNOWN;
    private String nitfCodewords = "";
    private String controlAndHandling = "";
    private String nitfReleaseInstructions = "";
    private String nitfClassificationAuthority = "";
    private String nitfSecurityControlNumber = "";

    protected T instance;

    /**
     * Constructor.
     *
     * @param fileType the type of NITF file to generate security metadata for
     */
    SecurityMetadataBuilder(final FileType fileType) {
        nitfFileType = fileType;
    }

    /**
     * Copy builder.
     *
     * @param securityMetadata the metadata to initialise this builder from.
     */
    SecurityMetadataBuilder(final SecurityMetadata securityMetadata) {
        this.nitfFileType = securityMetadata.getFileType();
        this.nitfClassificationAuthority = securityMetadata.getClassificationAuthority();
        this.nitfCodewords = securityMetadata.getCodewords();
        this.controlAndHandling = securityMetadata.getControlAndHandling();
        this.securityClassification = securityMetadata.getSecurityClassification();
        this.nitfReleaseInstructions = securityMetadata.getReleaseInstructions();
        this.nitfSecurityControlNumber = securityMetadata.getSecurityControlNumber();
    }

    /**
     * Set the security classification.
     *
     * @param classification security classification
     * @return this builder, to support method chaining
     */
    public T securityClassification(final SecurityClassification classification) {
        this.securityClassification = classification;
        return instance;
    }

    /**
     * Set the security codewords.
     * <p>
     * "This field shall contain a valid indicator of the security compartments
     * associated with the file. Values include one or more of the digraphs
     * found table A-4. Multiple entries shall be separated by a single ECS
     * space (0x20): The selection of a relevant set of codewords is application
     * specific."
     * <p>
     * Note that the list in MIL-STD-2500C table A-4 includes digraphs that are
     * no longer used. Consult current guidance.
     * <p>
     * This field is valid for NITF 2.0 and NITF 2.1 / NSIF 1.0 files. The
     * maximum length is 11 characters for NITF 2.1 / NSIF 1.0; and 40
     * characters for NITF 2.0.
     *
     * @param codewords security codewords or an empty string if no codewords
     * apply
     * @return this builder, to support method chaining
     */
    public T codewords(final String codewords) {
        nitfCodewords = codewords;
        return instance;
    }

    /**
     * Set the security control and handling code instructions.
     * <p>
     * "This field shall contain valid additional security control and/or
     * handling instructions (caveats) associated with the file. Values include
     * digraphs found in table A-4. The digraph may indicate single or multiple
     * caveats. The selection of a relevant caveat(s) is application specific."
     * <p>
     * Note that the list in MIL-STD-2500C table A-4 includes digraphs that are
     * no longer used. Consult current guidance.
     * <p>
     * This field is valid for NITF 2.0 and NITF 2.1 / NSIF 1.0 files. The
     * maximum length is 2 characters for NITF 2.1 / NSIF 1.0; and 40 characters
     * for NITF 2.0.
     *
     * @param instructions security control and handling codes, or an empty
     * string if no codes apply.
     * @return this builder, to support method chaining
     */
    public T controlAndHandling(final String instructions) {
        controlAndHandling = instructions;
        return instance;
    }

    /**
     * Set the release instructions.
     * <p>
     * "This field shall contain a valid list of country and/or multilateral
     * entity codes to which countries and/or multilateral entities the file is
     * authorized for release. Valid items in the list are one or more country
     * codes as found in FIPS PUB 10-4 separated by a single ECS space (0x20)."
     * <p>
     * So the release instructions are the countries that this is "REL TO".
     * <p>
     * This field is valid for NITF 2.0 and NITF 2.1 / NSIF 1.0 files. The
     * maximum length is 20 characters for NITF 2.1 / NSIF 1.0; and 40
     * characters for NITF 2.0.
     *
     * @param releaseInstructions release instructions, or an empty string if no
     * release instructions apply.
     * @return this builder, to support method chaining
     */
    public T releaseInstructions(final String releaseInstructions) {
        nitfReleaseInstructions = releaseInstructions;
        return instance;
    }

    /**
     * Set the classification security authority.
     * <p>
     * "This field shall identify the classification authority for the file
     * dependent upon the value in Classification Authority Type. Values are
     * user defined free text which should contain the following information:
     * original classification authority name and position or personal
     * identifier if the value in Classification Authority Type is O; title of
     * the document or security classification guide used to classify the file
     * if the value in Classification Authority Type is D; and Derive-Multiple
     * if the file classification was derived from multiple sources and the
     * value of the Classification Authority Type field is M. In the latter
     * case, the file originator will maintain a record of the sources used in
     * accordance with existing security directives. One of the multiple sources
     * may also be identified in Classification Text if desired."
     * <p>
     * An empty string indicates that no file classification authority applies.
     * <p>
     * This field is valid for NITF 2.0 and NITF 2.1 / NSIF 1.0 files. The
     * maximum length is 40 characters for NITF 2.1 / NSIF 1.0; and 20
     * characters for NITF 2.0.
     *
     * @param classificationAuthority classification authority or an empty
     * string.
     * @return this builder, to support method chaining
     */
    public T classificationAuthority(final String classificationAuthority) {
        this.nitfClassificationAuthority = classificationAuthority;
        return instance;
    }

    /**
     * Set the security control number.
     * <p>
     * "This field shall contain a valid security control number associated with
     * the file. The format of the security control number shall be in
     * accordance with the regulations governing the appropriate security
     * channel(s)."
     * <p>
     * An empty string indicates that no file security control number applies.
     * <p>
     * This field is valid for NITF 2.0 and NITF 2.1 / NSIF 1.0 files. The
     * maximum length is 15 characters for NITF 2.1 / NSIF 1.0; and 20
     * characters for NITF 2.0.
     *
     * @param securityControlNumber the security control number, or an empty
     * string.
     * @return this builder, to support method chaining
     */
    public T securityControlNumber(final String securityControlNumber) {
        nitfSecurityControlNumber = securityControlNumber;
        return instance;
    }

    abstract void populateVersionSpecific(SecurityMetadataImpl securityMetadata);

    void populateCommon(final SecurityMetadataImpl securityMetadata) {
        securityMetadata.setFileType(nitfFileType);
        securityMetadata.setSecurityClassification(securityClassification);
        securityMetadata.setSecurityControlNumber(nitfSecurityControlNumber);
        securityMetadata.setClassificationAuthority(nitfClassificationAuthority);
        securityMetadata.setReleaseInstructions(nitfReleaseInstructions);
        securityMetadata.setControlAndHandling(controlAndHandling);
        securityMetadata.setCodewords(nitfCodewords);
    }

    /**
     * Build the Security Metadata based on data configured.
     *
     * @return security metadata object.
     */
    public SecurityMetadataImpl get() {
        SecurityMetadataImpl securityMetadata = new SecurityMetadataImpl();
        populateCommon(securityMetadata);
        populateVersionSpecific(securityMetadata);
        return securityMetadata;
    }
}
