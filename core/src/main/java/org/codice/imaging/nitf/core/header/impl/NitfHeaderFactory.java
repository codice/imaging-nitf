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
package org.codice.imaging.nitf.core.header.impl;

import org.codice.imaging.nitf.core.RGBColour;
import org.codice.imaging.nitf.core.common.DateTime;
import org.codice.imaging.nitf.core.common.impl.DateTimeImpl;
import org.codice.imaging.nitf.core.header.NitfHeader;
import org.codice.imaging.nitf.core.impl.RGBColourImpl;
import org.codice.imaging.nitf.core.common.FileType;

import static org.codice.imaging.nitf.core.header.impl.NitfHeaderConstants.LOWEST_COMPLEXITY_LEVEL;
import static org.codice.imaging.nitf.core.header.impl.NitfHeaderConstants.STANDARD_TYPE_VAL;
import org.codice.imaging.nitf.core.security.impl.SecurityMetadataFactory;

/**
 * Factory to create NitfHeader instances.
 */
public final class NitfHeaderFactory {

    private NitfHeaderFactory() {
    }

    /**
     * Create a default NITF file header.
     *
     * @param fileType the type (version) of NITF file to create
     * @return default valid header.
     */
    public static NitfHeader getDefault(final FileType fileType) {
        NitfHeaderImpl nitfFileHeader = new NitfHeaderImpl();
        nitfFileHeader.setFileType(fileType);
        nitfFileHeader.setComplexityLevel(LOWEST_COMPLEXITY_LEVEL);
        nitfFileHeader.setStandardType(STANDARD_TYPE_VAL);
        nitfFileHeader.setOriginatingStationId(NitfHeaderConstants.DEFAULT_ORIGINATING_STATION);
        DateTime ndt = DateTimeImpl.getNitfDateTimeForNow();
        nitfFileHeader.setFileDateTime(ndt);
        nitfFileHeader.setFileTitle("");
        nitfFileHeader.setFileSecurityMetadata(SecurityMetadataFactory.getDefaultFileSecurityMetadata(fileType));
        RGBColour backgroundColour = new RGBColourImpl(RGBColourImpl.CODICE_LOGO_RED_COMPONENT,
                RGBColourImpl.CODICE_LOGO_GREEN_COMPONENT,
                RGBColourImpl.CODICE_LOGO_BLUE_COMPONENT);
        nitfFileHeader.setFileBackgroundColour(backgroundColour);
        nitfFileHeader.setOriginatorsName("");
        nitfFileHeader.setOriginatorsPhoneNumber("");
        // The rest of this is made up of things that already work OK as defaults.
        return nitfFileHeader;
    }

}
