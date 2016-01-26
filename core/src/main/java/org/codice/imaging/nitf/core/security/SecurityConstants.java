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

final class SecurityConstants {

    private SecurityConstants() {
    }

    /**
     * Length of the "Security Classification" field used in the NITF file header and subheaders.
     * <p>
     * This field is called different things in different places. For example, in the file header
     * it starts with F, while in the image subheader it starts with I. To make it more general,
     * we use 'X' to represent these various prefixes.
     * See MIL-STD-2500C Annex A for the various uses.
     */
    protected static final int XSCLAS_LENGTH = 1;

    /**
     * Length of the "Security Classification System" field used in the NITF file header and subheaders.
     * <p>
     * This field is called different things in different places. For example, in the file header
     * it starts with F, while in the image subheader it starts with I. To make it more general,
     * we use 'X' to represent these various prefixes.
     * See MIL-STD-2500C Annex A for the various uses.
     */
    protected static final int XSCLSY_LENGTH = 2;

    /**
     * Length of the "Codewords" field used in the NITF file header and subheaders.
     * <p>
     * This field is called different things in different places. For example, in the file header
     * it starts with F, while in the image subheader it starts with I. To make it more general,
     * we use 'X' to represent these various prefixes.
     * See MIL-STD-2500C Annex A for the various uses.
     */

    protected static final int XSCODE_LENGTH = 11;

    /**
     * Length of the "Control and Handling" field used in the NITF file header and subheaders.
     * <p>
     * This field is called different things in different places. For example, in the file header
     * it starts with F, while in the image subheader it starts with I. To make it more general,
     * we use 'X' to represent these various prefixes.
     * See MIL-STD-2500C Annex A for the various uses.
     */
    protected static final int XSCTLH_LENGTH = 2;

    /**
     * Length of the "Releasing Instructions" field used in the NITF file header and subheaders.
     * <p>
     * This field is called different things in different places. For example, in the file header
     * it starts with F, while in the image subheader it starts with I. To make it more general,
     * we use 'X' to represent these various prefixes.
     * See MIL-STD-2500C Annex A for the various uses.
     */
    protected static final int XSREL_LENGTH = 20;

    /**
     * Length of the "Declassification Type" field used in the NITF file header and subheaders.
     * <p>
     * This field is called different things in different places. For example, in the file header
     * it starts with F, while in the image subheader it starts with I. To make it more general,
     * we use 'X' to represent these various prefixes.
     * See MIL-STD-2500C Annex A for the various uses.
     */
    protected static final int XSDCTP_LENGTH = 2;

    /**
     * Length of the "Declassification Date" field used in the NITF file header and subheaders.
     * <p>
     * This field is called different things in different places. For example, in the file header
     * it starts with F, while in the image subheader it starts with I. To make it more general,
     * we use 'X' to represent these various prefixes.
     * See MIL-STD-2500C Annex A for the various uses.
     */
    protected static final int XSDCDT_LENGTH = 8;

    /**
     * Length of the "Declassification Exemption" field used in the NITF file header and subheaders.
     * <p>
     * This field is called different things in different places. For example, in the file header
     * it starts with F, while in the image subheader it starts with I. To make it more general,
     * we use 'X' to represent these various prefixes.
     * See MIL-STD-2500C Annex A for the various uses.
     */
    protected static final int XSDCXM_LENGTH = 4;

    /**
     * Length of the "Downgrade" field used in the NITF file header and subheaders.
     * <p>
     * This field is called different things in different places. For example, in the file header
     * it starts with F, while in the image subheader it starts with I. To make it more general,
     * we use 'X' to represent these various prefixes.
     * See MIL-STD-2500C Annex A for the various uses.
     */
    protected static final int XSDG_LENGTH = 1;

    /**
     * Length of the "Downgrade Date" field used in the NITF file header and subheaders.
     * <p>
     * This field is called different things in different places. For example, in the file header
     * it starts with F, while in the image subheader it starts with I. To make it more general,
     * we use 'X' to represent these various prefixes.
     * See MIL-STD-2500C Annex A for the various uses.
     */
    protected static final int XSDGDT_LENGTH = 8;

    /**
     * Length of the "Classification Text" field used in the NITF file header and subheaders.
     * <p>
     * This field is called different things in different places. For example, in the file header
     * it starts with F, while in the image subheader it starts with I. To make it more general,
     * we use 'X' to represent these various prefixes.
     * See MIL-STD-2500C Annex A for the various uses.
     */
    protected static final int XSCLTX_LENGTH = 43;

    /**
     * Length of the "Classification Authority Type" field used in the NITF file header and subheaders.
     * <p>
     * This field is called different things in different places. For example, in the file header
     * it starts with F, while in the image subheader it starts with I. To make it more general,
     * we use 'X' to represent these various prefixes.
     * See MIL-STD-2500C Annex A for the various uses.
     */
    protected static final int XSCATP_LENGTH = 1;

    /**
     * Length of the "Classification Authority" field used in the NITF file header and subheaders.
     * <p>
     * This field is called different things in different places. For example, in the file header
     * it starts with F, while in the image subheader it starts with I. To make it more general,
     * we use 'X' to represent these various prefixes.
     * See MIL-STD-2500C Annex A for the various uses.
     */
    protected static final int XSCAUT_LENGTH = 40;

    /**
     * Length of the "Classification Reason" field used in the NITF file header and subheaders.
     * <p>
     * This field is called different things in different places. For example, in the file header
     * it starts with F, while in the image subheader it starts with I. To make it more general,
     * we use 'X' to represent these various prefixes.
     * See MIL-STD-2500C Annex A for the various uses.
     */
    protected static final int XSCRSN_LENGTH = 1;

    /**
     * Length of the "Security Source Date" field used in the NITF file header and subheaders.
     * <p>
     * This field is called different things in different places. For example, in the file header
     * it starts with F, while in the image subheader it starts with I. To make it more general,
     * we use 'X' to represent these various prefixes.
     * See MIL-STD-2500C Annex A for the various uses.
     */
    protected static final int XSSRDT_LENGTH = 8;

    /**
     * Length of the "Security Control Number" field used in the NITF file header and subheaders.
     * <p>
     * This field is called different things in different places. For example, in the file header
     * it starts with F, while in the image subheader it starts with I. To make it more general,
     * we use 'X' to represent these various prefixes.
     * See MIL-STD-2500C Annex A for the various uses.
     */
    protected static final int XSCTLN_LENGTH = 15;

    // NITF 2.0 field lengths
    /**
     * Length of the "Codewords" field used in the NITF 2.0 file header and subheaders.
     * <p>
     * This field is called different things in different places. For example, in the file header
     * it starts with F, while in the image subheader it starts with I. To make it more general,
     * we use 'X' to represent these various prefixes.
     * See MIL-STD-2500A Tables for the various uses.
     */
    protected static final int XSCODE20_LENGTH = 40;

    /**
     * Length of the "Control and Handling" field used in the NITF 2.0 file header and subheaders.
     * <p>
     * This field is called different things in different places. For example, in the file header
     * it starts with F, while in the image subheader it starts with I. To make it more general,
     * we use 'X' to represent these various prefixes.
     * See MIL-STD-2500A Tables for the various uses.
     */
    protected static final int XSCTLH20_LENGTH = 40;

    /**
     * Length of the "Releasing Instructions" field used in the NITF 2.0 file header and subheaders.
     * <p>
     * This field is called different things in different places. For example, in the file header
     * it starts with F, while in the image subheader it starts with I. To make it more general,
     * we use 'X' to represent these various prefixes.
     * See MIL-STD-2500A Tables for the various uses.
     */
    protected static final int XSREL20_LENGTH = 40;

    /**
     * Length of the "Classification Authority" field used in the NITF 2.0 file header and subheaders.
     * <p>
     * This field is called different things in different places. For example, in the file header
     * it starts with F, while in the image subheader it starts with I. To make it more general,
     * we use 'X' to represent these various prefixes.
     * See MIL-STD-2500A Tables for the various uses.
     */
    protected static final int XSCAUT20_LENGTH = 20;

    /**
     * Length of the "Control Number" field used in the NITF 2.0 file header and subheaders.
     * <p>
     * This field is called different things in different places. For example, in the file header
     * it starts with F, while in the image subheader it starts with I. To make it more general,
     * we use 'X' to represent these various prefixes.
     * See MIL-STD-2500A Tables for the various uses.
     */
    protected static final int XSCTLN20_LENGTH = 20;

    /**
     * Length of the "Security Downgrade" field used in the NITF 2.0 file header and subheaders.
     * <p>
     * This field is called different things in different places. For example, in the file header
     * it starts with F, while in the image subheader it starts with I. To make it more general,
     * we use 'X' to represent these various prefixes.
     * See MIL-STD-2500A Tables for the various uses.
     */
    protected static final int XSDWNG20_LENGTH = 6;

    /**
     * Length of the "Downgrade Event" field used in the NITF 2.0 file header and subheaders.
     * <p>
     * This field is called different things in different places. For example, in the file header
     * it starts with F, while in the image subheader it starts with I. To make it more general,
     * we use 'X' to represent these various prefixes.
     * See MIL-STD-2500A Tables for the various uses.
     */
    protected static final int XSDEVT20_LENGTH = 40;

    /**
     * Marker field used in the "Security Downgrade" field to indicate that the "Downgrade Event" field is present.
     * <p>
     * NITF 2.0 only. See MIL-STD-2500A Tables for usage.
     */
    public static final String DOWNGRADE_EVENT_MAGIC = "999998";
}
