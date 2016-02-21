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
package org.codice.imaging.nitf.core.common;

import org.codice.imaging.nitf.core.security.SecurityMetadata;

/**
 * Implementation of common data elements for NITF segments.
 *
 * This includes image segments, symbol segments, graphic segments, label segments, text segments and data extension
 * segments.
 */
public class CommonSegmentImpl extends TaggedRecordExtensionHandlerImpl implements CommonSegment {

    private String segmentIdentifier;
    private SecurityMetadata securityMetadata = null;

    /**
     * {@inheritDoc}
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
     * {@inheritDoc}
     */
    @Override
    public final void setSecurityMetadata(final SecurityMetadata metaData) {
        this.securityMetadata = metaData;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final SecurityMetadata getSecurityMetadata() {
        return securityMetadata;
    }


}
