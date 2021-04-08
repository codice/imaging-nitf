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

import java.util.function.Supplier;
import org.codice.imaging.nitf.core.common.FileType;
import org.codice.imaging.nitf.core.security.SecurityMetadata;

/**
 * Builder for SecurityMetadata for NITF 2.0 files.
 *
 * This class implements a builder pattern, to produce a SecurityMetadata object
 * (which is immutable).
 *
 * A simple example of its use is:
 * <pre>{@code
 *      SecurityMetadataBuilder20 builder = SecurityMetadataBuilder20.newInstance();
 *      builder.securityClassification(SecurityClassification.UNCLASSIFIED);
 *      SecurityMetadata securityMetadata = builder.get();
 * }</pre>
 *
 * The API supports method chaining for a more fluent style, which is useful for
 * more involved metadata requirements:
 * <pre>{@code
 *      SecurityMetadataBuilder20 builder = SecurityMetadataBuilder20.newInstance();
 *      builder.securityClassification(SecurityClassification.UNCLASSIFIED)
 *              .downgradeDateOrSpecialCase("999998")
 *              .downgradeEvent("When its good.");
 *      SecurityMetadata securityMetadata = builder.get();
 * }</pre>
 */
public final class SecurityMetadataBuilder20 extends SecurityMetadataBuilder<SecurityMetadataBuilder20> implements Supplier<SecurityMetadata> {

    private String nitfDowngradeDateOrSpecialCase = null;
    private String nitfDowngradeEvent = null;

    private SecurityMetadataBuilder20() {
        super(FileType.NITF_TWO_ZERO);
    }

    private SecurityMetadataBuilder20(final SecurityMetadata securityMetadata) {
        super(securityMetadata);
    }

    /**
     * Constructor.
     *
     * @return new instance of a SecurityMetadataBuilder20
     */
    public static SecurityMetadataBuilder20 newInstance() {
        SecurityMetadataBuilder20 builder = new SecurityMetadataBuilder20();
        builder.instance = builder;
        return builder;
    }

    /**
     * Copy constructor.
     *
     * @param securityMetadata data to initialise from.
     * @return new instance of a SecurityMetadataBuilder20
     */
    public static SecurityMetadataBuilder20 newInstance(final SecurityMetadata securityMetadata) {
        SecurityMetadataBuilder20 builder = new SecurityMetadataBuilder20(securityMetadata);
        builder.instance = builder;
        builder.downgradeDateOrSpecialCase(securityMetadata.getDowngradeDateOrSpecialCase());
        builder.downgradeEvent(securityMetadata.getDowngradeEvent());
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
    public SecurityMetadataBuilder20 downgradeDateOrSpecialCase(final String dateOrSpecialCase) {
        this.nitfDowngradeDateOrSpecialCase = dateOrSpecialCase;
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
    public SecurityMetadataBuilder20 downgradeEvent(final String event) {
        this.nitfDowngradeEvent = event;
        return this;
    }

    @Override
    void populateVersionSpecific(final SecurityMetadataImpl securityMetadata) {
        securityMetadata.setDowngradeEvent(nitfDowngradeEvent);
        securityMetadata.setDowngradeDateOrSpecialCase(nitfDowngradeDateOrSpecialCase);
    }
}
