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
package org.codice.imaging.nitf.core.label;

import org.codice.imaging.nitf.core.RGBColour;
import org.codice.imaging.nitf.core.common.FileType;
import org.codice.imaging.nitf.core.security.SecurityMetadataFactory;

/**
 * Factory class for creating new TextSegment instances.
 */
public final class LabelSegmentFactory {


    private LabelSegmentFactory() {
    }

    /**
     * Create a default NITF label segment, without data.
     *
     * This only applies to NITF 2.0 files, so that is assumed.
     *
     * Note that this will not set an identifier (LID) - it will be empty (space
     * filled on write). That may or may not be valid - it is application
     * dependent.
     *
     * Location (column and row) will be set 0 (no offset).
     * Cell height and cell width will have the default "0" value.
     * Attachment level will be set to 0 (not attached).
     * Text background colour will be set to black, and text foreground colour will be set to blue.
     *
     * Display level will be set to 0, which is not valid, and must be set prior to writing out the label segment.
     * @return default label segment, containing no text data.
     */
    public static LabelSegment getDefault() {
        LabelSegment labelSegment = new LabelSegmentImpl();
        labelSegment.setFileType(FileType.NITF_TWO_ZERO);
        labelSegment.setSecurityMetadata(SecurityMetadataFactory.getDefaultMetadata(FileType.NITF_TWO_ZERO));
        labelSegment.setIdentifier("");
        labelSegment.setAttachmentLevel(0);
        labelSegment.setLabelBackgroundColour(new RGBColour((byte) 0, (byte) 0, (byte) 0));
        labelSegment.setLabelTextColour(new RGBColour(RGBColour.CODICE_LOGO_RED_COMPONENT,
                RGBColour.CODICE_LOGO_GREEN_COMPONENT,
                RGBColour.CODICE_LOGO_BLUE_COMPONENT));
        return labelSegment;
    }
}
