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
package org.codice.nitf.filereader;

public class AbstractNitfSubSegment extends AbstractNitfSegment {

    private int extendedHeaderDataOverflow = 0;
    private String segmentIdentifier = null;
    private NitfSecurityMetadata securityMetadata = null;
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
        Return the extended subheader overflow index (IXSOFL/SXSOFL/LXSOFL/TXSOFL).
        <p>
        This is the (1-base) index of the TRE into which extended header data
        overflows.

        @return the extended header data overflow index
    */
    public final int getExtendedHeaderDataOverflow() {
        return extendedHeaderDataOverflow;
    }

    /**
        Set the identifier (IID1/SID/LID/TEXTID) for the segment.
        <p>
        This field shall contain a valid alphanumeric identification code associated with the
        segment. The valid codes are determined by the application.

         @param identifier the identifier for the segment
    */
    public final void setIdentifier(final String identifier) {
        segmentIdentifier = identifier;
    }

    /**
        Return the identifier (IID1/SID/LID/TEXTID) for the segment.
        <p>
        This field shall contain a valid alphanumeric identification code associated with the
        segment. The valid codes are determined by the application.

        @return the identifier
    */
    public final String getIdentifier() {
        return segmentIdentifier;
    }

    /**
        Set the security metadata elements for the segment.

        See NitfSecurityMetadata for the various elements, which differ slightly between NITF 2.0 and NITF 2.1/NSIF 1.0.

        @param nitfSecurityMetadata the security metadata values to set.
    */
    public final void setSecurityMetadata(final NitfSecurityMetadata nitfSecurityMetadata) {
        securityMetadata = nitfSecurityMetadata;
    }

    /**
        Return the security metadata for the segment.

        @return security metadata
    */
    public final NitfSecurityMetadata getSecurityMetadata() {
        return securityMetadata;
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
        Return the attachment level for the segment.
        <p>
        The valid values for this are zero (not attached) or the display level
        for an image, graphic, symbol or label (as appropriate to the NITF file type)
        in the file.

        @return the attachment level
    */
    public final int getAttachmentLevel() {
        return segmentAttachmentLevel;
    }

}
