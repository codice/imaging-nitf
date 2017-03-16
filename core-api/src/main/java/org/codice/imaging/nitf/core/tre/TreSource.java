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
package org.codice.imaging.nitf.core.tre;

/**
 * A source of a TRE.
 */
public enum TreSource {

    /**
     * TRE is associated with the file header "UDHD" user defined header data
     * field.
     */
    UserDefinedHeaderData,
    /**
     * TRE is associated with the file header "XHD" extended header data field.
     */
    ExtendedHeaderData,
    /**
     * TRE is associated with the Image Subheader "UDID" user defined image data
     * field.
     */
    UserDefinedImageData,
    /**
     * TRE is associated with the Image Subheader "IXSHD" extended subheader
     * data field.
     */
    ImageExtendedSubheaderData,
    /**
     * TRE is associated with the Graphic Subheader "SXSHD" extended subheader
     * data field.
     *
     * (Not NITF 2.0).
     */
    GraphicExtendedSubheaderData,
    /**
     * TRE is associated with the Symbol Subheader "SXSHD" extended subheader data field.
     *
     * (NITF 2.0 only).
     */
    SymbolExtendedSubheaderData,
    /**
     * TRE is associated with the Label Subheader "LXSHD" extended subheader data field.
     *
     * (NITF 2.0 only).
     */
    LabelExtendedSubheaderData,
    /**
     * TRE is associated with the Text Subheader "TXSHD" extended subheader data field.
     */
    TextExtendedSubheaderData,
    /**
     * TRE is associated with a TRE Overflow Data Extension Segment.
     */
    TreOverflowDES
}
