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
package org.codice.imaging.nitf.common.segment;

import org.codice.imaging.nitf.common.security.NitfSecurityMetadata;

/**
    Common data elements for NITF segment subheaders.
*/
public abstract class AbstractCommonNitfSegment extends AbstractNitfSegment
        implements CommonNitfSegment {

    private String segmentIdentifier = null;
    private NitfSecurityMetadata securityMetadata = null;

    /**
     {@inheritDoc}
     */
    @Override
    public final void setIdentifier(final String identifier) {
        segmentIdentifier = identifier;
    }

    /**
     {@inheritDoc}
     */
    @Override
    public final String getIdentifier() {
        return segmentIdentifier;
    }

    /**
     {@inheritDoc}
     */
    @Override
    public final void setSecurityMetadata(final NitfSecurityMetadata nitfSecurityMetadata) {
        securityMetadata = nitfSecurityMetadata;
    }

    /**
     {@inheritDoc}
     */
    @Override
    public final NitfSecurityMetadata getSecurityMetadata() {
        return securityMetadata;
    }

}
