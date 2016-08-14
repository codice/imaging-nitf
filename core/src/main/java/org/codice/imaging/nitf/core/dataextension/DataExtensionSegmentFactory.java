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
package org.codice.imaging.nitf.core.dataextension;

import java.io.IOException;
import org.codice.imaging.nitf.core.common.FileType;
import org.codice.imaging.nitf.core.common.NitfFormatException;
import org.codice.imaging.nitf.core.security.SecurityMetadataFactory;

/**
 * Factory class for creating new DataExtensionSegment instances.
 */
public final class DataExtensionSegmentFactory {

    private static final int NITF20_DES_OVERFLOW_VERSION = 99;

    private DataExtensionSegmentFactory() {
    }

    /**
     * Create a default NITF data extension segment (DES), without data.
     *
     * Note that this will not set an identifier - it will be empty (space filled on write). That probably is not what
     * you want, so you need to set it.
     *
     * @param fileType the type (version) of NITF file this data extension segment is for
     * @return default valid data extension segment, containing no data.
     */
    public static DataExtensionSegment getDefault(final FileType fileType) {
        DataExtensionSegment des = makeBasicDesImpl(fileType);
        des.setIdentifier("");
        return des;
    }

    /**
     * Create a user defined data extension segment (DES).
     * @param fileType the type (version) of NITF file this data extension segment is for
     * @param userDES the user DES to build this DES from.
     * @return user defined DES
     * @throws NitfFormatException if the DES could not be generated
     */
    public static DataExtensionSegment getUserDefinedDES(final FileType fileType, final UserDefinedDataExtensionSegment userDES)
            throws NitfFormatException {
        if (fileType.equals(FileType.UNKNOWN)) {
            throw new UnsupportedOperationException("Cannot make DES for unsupported FileType: " + fileType.toString());
        }
        DataExtensionSegment des = makeBasicDesImpl(fileType);
        des.setIdentifier(userDES.getTypeIdentifier());
        des.setDESVersion(userDES.getVersion());
        des.setUserDefinedSubheaderField(userDES.getUserDefinedSubheader());
        des.setData(userDES.getUserData());
        try {
            des.setDataLength(userDES.getUserData().length());
        } catch (IOException ex) {
            throw new NitfFormatException("Cannot get data length: " + ex.getMessage());
        }
        return des;
    }


    /**
     * Create a TRE Overflow NITF data extension segment (DES), without data.
     *
     * This will set the TRE_OVERFLOW identifier for NITF 2.1 / NSIF 1.0, and "Controlled Extensions" for NITF 2.0. If
     * you need "Registered Extensions" for NITF 2.0, you will need to set the identifier.
     *
     * @param fileType the type (version) of NITF file this data extension segment is for
     * @param segmentTypeOverflowed the segment type that this DES contains overflow for - one of "UDHD", "XHD", "UDID",
     * "IXSHD", "SXSHD", "LXSHD" or "TXSHD".
     * @param itemOverflowed the index count (1 based) of the segment that overflowed - 0 for UDHD or XHD.
     * @return default valid data extension segment, containing no data.
     */
    public static DataExtensionSegment getOverflow(final FileType fileType, final String segmentTypeOverflowed, final int itemOverflowed) {
        DataExtensionSegmentImpl des = makeBasicDesImpl(fileType);
        if (fileType.equals(FileType.NITF_TWO_ONE) || fileType.equals(FileType.NSIF_ONE_ZERO)) {
            des.setIdentifier(DataExtensionConstants.TRE_OVERFLOW);
            des.setDESVersion(1);
        } else if (fileType.equals(FileType.NITF_TWO_ZERO)) {
            if (segmentTypeOverflowed.equals("UDHD") || segmentTypeOverflowed.equals("UDID")) {
                des.setIdentifier(DataExtensionConstants.REGISTERED_EXTENSIONS);
                des.setDESVersion(NITF20_DES_OVERFLOW_VERSION);
            } else {
                des.setIdentifier(DataExtensionConstants.CONTROLLED_EXTENSIONS);
                des.setDESVersion(NITF20_DES_OVERFLOW_VERSION);
            }
        } else {
            throw new UnsupportedOperationException("Cannot make DES for unsupported FileType: " + fileType.toString());
        }
        des.setOverflowedHeaderType(segmentTypeOverflowed);
        if (segmentTypeOverflowed.equals("UDHD") || segmentTypeOverflowed.equals("XHD")) {
            des.setItemOverflowed(0);
        } else {
            des.setItemOverflowed(itemOverflowed);
        }
        return des;
    }

    private static DataExtensionSegmentImpl makeBasicDesImpl(final FileType fileType) {
        DataExtensionSegmentImpl des = new DataExtensionSegmentImpl(fileType);
        des.setDESVersion(0);
        des.setSecurityMetadata(SecurityMetadataFactory.getDefaultMetadata(fileType));
        des.setUserDefinedSubheaderField("");
        des.setDataLength(0);
        return des;
    }
}
