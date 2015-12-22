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
package org.codice.imaging.nitf.core;

import org.codice.imaging.nitf.core.common.CommonNitfSubSegment;

/**
    Common data elements for NITF data segment subheaders.
    <p>
    This excludes the Data Extension Segment subheader.
*/
public abstract class AbstractNitfSubSegment extends AbstractCommonNitfSegment
        implements CommonNitfSubSegment {

    private int extendedHeaderDataOverflow = 0;
    private int segmentAttachmentLevel = 0;

    /**
        Set the extended subheader overflow index (IXSOFL/SXSOFL/LXSOFL/TXSOFL).
        <p>
        This is the (1-base) index of the TRE into which extended header data
        overflows.

        @param overflow the extended header data overflow index
    */
    public final void setExtendedHeaderDataOverflow(final int overflow) {
        extendedHeaderDataOverflow = overflow;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final int getExtendedHeaderDataOverflow() {
        return extendedHeaderDataOverflow;
    }

    /**
        Set the attachment level for the segment.
        <p>
        The valid values for this are zero (not attached) or the display level
        for an image, graphic, symbol or label (as appropriate to the NITF file type)
        in the file.

        @param attachmentLevel the attachment level
    */
    public final void setAttachmentLevel(final int attachmentLevel) {
        segmentAttachmentLevel = attachmentLevel;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final int getAttachmentLevel() {
        return segmentAttachmentLevel;
    }

}
