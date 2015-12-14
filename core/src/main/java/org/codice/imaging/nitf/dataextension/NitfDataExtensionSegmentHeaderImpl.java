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
package org.codice.imaging.nitf.dataextension;

import org.codice.imaging.nitf.common.dataextension.NitfDataExtensionSegmentHeader;
import org.codice.imaging.nitf.common.segment.AbstractCommonNitfSegment;
import org.codice.imaging.nitf.common.FileType;

/**
    Data Extension Segment (DES) subheader and associated data.
*/
public class NitfDataExtensionSegmentHeaderImpl extends AbstractCommonNitfSegment
        implements NitfDataExtensionSegmentHeader {
    /**
     * Marker string for Tagged Record Overflow DES in NITF 2.1
     * <p>
     * See DESID in MIL-STD-2500C Table A-8(A).
     */
    protected static final String TRE_OVERFLOW = "TRE_OVERFLOW";

    private int desVersion = -1;
    private String overflowedHeaderType = null;
    private int desItemOverflowed = 0;
    private String userDefinedSubheaderField = null;
    private int dataExtensionSegmentDataLength = 0;

    /**
     * Marker string for Tagged Record Overflow DES in NITF 2.0
     * <p>
     * See MIL-STD-2500A.
     */
    protected static final String REGISTERED_EXTENSIONS = "Registered Extensions";

    /**
     * Marker string for Tagged Record Overflow DES in NITF 2.0
     * <p>
     * See MIL-STD-2500A.
     */
    protected static final String CONTROLLED_EXTENSIONS = "Controlled Extensions";

    /**
        Default constructor.
    */
    public NitfDataExtensionSegmentHeaderImpl() {
    }

    /**
     {@inheritDoc}
    */
    @Override
    public final void setDESVersion(final int version) {
        desVersion = version;
    }

    /**
     {@inheritDoc}
     */
    @Override
    public final int getDESVersion() {
        return desVersion;
    }

    /**
     {@inheritDoc}
     */
    @Override
    public final void setOverflowedHeaderType(final String headerType) {
        overflowedHeaderType = headerType;
    }

    /**
     {@inheritDoc}
     */
    @Override
    public final String getOverflowedHeaderType() {
        return overflowedHeaderType;
    }

    /**
     {@inheritDoc}
     */
    @Override
    public final void setItemOverflowed(final int itemOverflowed) {
        desItemOverflowed = itemOverflowed;
    }

    /**
     {@inheritDoc}
     */
    @Override
    public final int getItemOverflowed() {
        return desItemOverflowed;
    }

    /**
     {@inheritDoc}
     */
    @Override
    public final void setUserDefinedSubheaderField(final String data) {
        userDefinedSubheaderField = data;
    }

    /**
     {@inheritDoc}
     */
    @Override
    public final String getUserDefinedSubheaderField() {
        return userDefinedSubheaderField;
    }

    /**
     {@inheritDoc}
     */
    @Override
    public final void setDataExtensionSegmentDataLength(final int length) {
        dataExtensionSegmentDataLength = length;
    }

    /**
     {@inheritDoc}
     */
    @Override
    public final int getDataExtensionSegmentDataLength() {
        return dataExtensionSegmentDataLength;
    }

    /**
     {@inheritDoc}
     */
    @Override
    public final boolean isTreOverflowNitf21() {
        return getIdentifier().trim().equals(TRE_OVERFLOW);
    }

    /**
     {@inheritDoc}
     */
    @Override
    public final boolean isTreOverflowNitf20() {
        return getIdentifier().trim().equals(REGISTERED_EXTENSIONS)
                || getIdentifier().trim().equals(CONTROLLED_EXTENSIONS);
    }

    /**
     {@inheritDoc}
     */
    @Override
    public final boolean isTreOverflow(final FileType fileType) {
        if (fileType == FileType.NITF_TWO_ZERO) {
            return isTreOverflowNitf20();
        } else {
            return isTreOverflowNitf21();
        }
    }
}
