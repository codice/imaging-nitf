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
package org.codice.imaging.nitf.core.symbol.impl;

import org.codice.imaging.nitf.core.common.FileType;
import org.codice.imaging.nitf.core.security.impl.SecurityMetadataFactory;
import org.codice.imaging.nitf.core.symbol.SymbolColour;
import org.codice.imaging.nitf.core.symbol.SymbolSegment;
import org.codice.imaging.nitf.core.symbol.SymbolType;

/**
 * Factory class for creating new SymbolSegment instances.
 */
public final class SymbolSegmentFactory {

    private SymbolSegmentFactory() {
    }

    /**
     * Create a default NITF symbol segment, without data.
     *
     * The symbol segment will be configured with CGM defaults. Display level
     * will be set to 1, which will likely require modification for a more
     * complex file. Attachment level will be set to 0 (not attached).
     *
     * Note that this method will not set an identifier - it will be empty
     * (space filled on write). That may or may not be valid - it is application
     * dependent.
     *
     * Symbol segments are only valid in NITF 2.0, so that is assumed.
     *
     * @return default symbol segment, containing no data.
     */
    public static SymbolSegment getDefault() {
        SymbolSegment symbolSegment = new SymbolSegmentImpl();
        symbolSegment.setFileType(FileType.NITF_TWO_ZERO);
        symbolSegment.setIdentifier("");
        symbolSegment.setAttachmentLevel(0);
        symbolSegment.setSymbolDisplayLevel(1);
        symbolSegment.setSecurityMetadata(SecurityMetadataFactory.getDefaultMetadata(FileType.NITF_TWO_ZERO));
        symbolSegment.setSymbolType(SymbolType.CGM);
        symbolSegment.setSymbolColourFormat(SymbolColour.NOT_APPLICABLE);
        return symbolSegment;
    }
}
