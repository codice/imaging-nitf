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
package org.codice.imaging.nitf.core.header;

import org.codice.imaging.nitf.core.RGBColour;
import org.codice.imaging.nitf.core.common.FileType;
import org.codice.imaging.nitf.core.common.NitfDateTime;
import static org.codice.imaging.nitf.core.header.NitfHeaderConstants.LOWEST_COMPLEXITY_LEVEL;
import static org.codice.imaging.nitf.core.header.NitfHeaderConstants.STANDARD_TYPE_VAL;
import org.codice.imaging.nitf.core.security.SecurityMetadataFactory;

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
        NitfDateTime ndt = NitfDateTime.getNitfDateTimeForNow();
        nitfFileHeader.setFileDateTime(ndt);
        nitfFileHeader.setFileTitle("");
        nitfFileHeader.setFileSecurityMetadata(SecurityMetadataFactory.getDefaultFileSecurityMetadata(fileType));
        RGBColour backgroundColour = new RGBColour(RGBColour.CODICE_LOGO_RED_COMPONENT,
                RGBColour.CODICE_LOGO_GREEN_COMPONENT,
                RGBColour.CODICE_LOGO_BLUE_COMPONENT);
        nitfFileHeader.setFileBackgroundColour(backgroundColour);
        nitfFileHeader.setOriginatorsName("");
        nitfFileHeader.setOriginatorsPhoneNumber("");
        // The rest of this is made up of things that already work OK as defaults.
        return nitfFileHeader;
    }

}
