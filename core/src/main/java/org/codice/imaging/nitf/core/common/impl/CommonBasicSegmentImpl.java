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
package org.codice.imaging.nitf.core.common.impl;

import org.codice.imaging.nitf.core.common.CommonBasicSegment;

/**
 * Common data elements for NITF data segments.
 *
 * This excludes the Data Extension Segment.
 */
public abstract class CommonBasicSegmentImpl extends CommonSegmentImpl implements
        CommonBasicSegment {

    private int extendedHeaderDataOverflow = 0;
    private int segmentAttachmentLevel = 0;

    /**
     * {@inheritDoc}
     */
    @Override
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
     * {@inheritDoc}
     */
    @Override
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
