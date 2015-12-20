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

/**
    Common data elements for NITF segment subheaders.
*/
public abstract class AbstractCommonNitfSegment extends AbstractNitfSegment
        implements org.codice.imaging.nitf.core.common.CommonNitfSegment {

    private String segmentIdentifier = null;
    private NitfSecurityMetadata securityMetadata = null;

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
     * {@inheritDoc}
     */
    @Override
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
     * {@inheritDoc}
     */
    @Override
    public final NitfSecurityMetadata getSecurityMetadata() {
        return securityMetadata;
    }

}
