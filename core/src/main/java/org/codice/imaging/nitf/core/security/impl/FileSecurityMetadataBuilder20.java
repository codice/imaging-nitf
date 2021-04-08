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

import org.codice.imaging.nitf.core.security.FileSecurityMetadata;

import java.util.function.Supplier;

/**
 * Builder for FileSecurityMetadata for NITF 2.0 headers.
 *
 * This class implements a builder pattern, to produce a FileSecurityMetadata
 * object (which is immutable).
 *
 * A simple example of its use is:
 * <pre>{@code
 *      FileSecurityMetadataBuilder20 builder = FileSecurityMetadataBuilder20.newInstance();
 *      builder.securityClassification(SecurityClassification.UNCLASSIFIED);
 *      FileSecurityMetadata securityMetadata = builder.get();
 * }</pre>
 *
 * The API supports method chaining for a more fluent style, which is useful for
 * more involved metadata requirements:
 * <pre>{@code
 *      FileSecurityMetadataBuilder20 builder = FileSecurityMetadataBuilder20.newInstance();
 *      builder.securityClassification(SecurityClassification.RESTRICTED)
 *              .fileCopyNumber("00001")
 *              .codewords("AB CD")
 *              .controlAndHandling("WITH CARE")
 *              .downgradeDateOrSpecialCase("999998")
 *              .releaseInstructions("Great Height")
 *              .downgradeEvent("XyzA")
 *              .fileNumberOfCopies("00003")
 *              .classificationAuthority("SOME AUTH")
 *              .securityControlNumber("BOGUS");
 *      FileSecurityMetadata securityMetadata = builder.get();
 * }</pre>
 */
public final class FileSecurityMetadataBuilder20
        extends FileSecurityMetadataBuilder<FileSecurityMetadataBuilder20, SecurityMetadataBuilder20>
        implements Supplier<FileSecurityMetadata> {

    /**
     * Constructor.
     */
    private FileSecurityMetadataBuilder20() {
    }

    /**
     * Constructor method.
     *
     * @return new instance of this FileSecurityMetadataBuilder20
     */
    public static FileSecurityMetadataBuilder20 newInstance() {
        FileSecurityMetadataBuilder20 builder = new FileSecurityMetadataBuilder20();
        builder.instance = builder;
        builder.builderInstance = SecurityMetadataBuilder20.newInstance();
        return builder;
    }

    /**
     * Copy constructor method.
     *
     * @param securityMetadata base security metadata.
     * @return new instance of this FileSecurityMetadataBuilder20
     */
    public static FileSecurityMetadataBuilder20 newInstance(final FileSecurityMetadata securityMetadata) {
        FileSecurityMetadataBuilder20 builder = new FileSecurityMetadataBuilder20();
        builder.instance = builder;
        builder.builderInstance = SecurityMetadataBuilder20.newInstance(securityMetadata);
        builder.fileCopyNumber(securityMetadata.getFileCopyNumber());
        builder.fileNumberOfCopies(securityMetadata.getFileNumberOfCopies());
        return builder;
    }

    /**
     * Set the downgrade date or special case for this file.
     * <p>
     * This field is only valid for NITF 2.0 files.
     * <p>
     * The valid values are: (1) the calendar date in the format YYMMDD (2) the
     * code "999999" when the originating agency's determination is required
     * (OADR) (3) the code "999998" when a specific event determines at what
     * point declassification or downgrading is to take place.
     * <p>
     * If the third case (999998) is set, use downgradeEvent() to specify the
     * downgrade event.
     *
     * @param dateOrSpecialCase the date or special case
     * @return this builder, to support method chaining
     */
    public FileSecurityMetadataBuilder20 downgradeDateOrSpecialCase(final String dateOrSpecialCase) {
        builderInstance.downgradeDateOrSpecialCase(dateOrSpecialCase);
        return this;
    }

    /**
     * Set the specific downgrade event for this file.
     * <p>
     * This field is only valid for NITF 2.0 files.
     * <p>
     * This is only valid if getDowngradeDateOrSpecialCase() is equal to 999998.
     *
     * @param event the downgrade event, or an empty string if no downgrade
     * event applies
     * @return this builder, to support method chaining
     */
    public FileSecurityMetadataBuilder20 downgradeEvent(final String event) {
        builderInstance.downgradeEvent(event);
        return this;
    }
}
