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
package org.codice.imaging.nitf.core.text.impl;

import org.codice.imaging.nitf.core.common.FileType;
import org.codice.imaging.nitf.core.common.impl.DateTimeImpl;
import org.codice.imaging.nitf.core.security.impl.SecurityMetadataFactory;
import org.codice.imaging.nitf.core.text.TextFormat;
import org.codice.imaging.nitf.core.text.TextSegment;

/**
 * Factory class for creating new TextSegment instances.
 */
public final class TextSegmentFactory {

    private TextSegmentFactory() {
    }

    /**
     * Create a default NITF text segment, without data.
     *
     * Note that this will not set an identifier - it will be empty (space filled on write). That may or may not be
     * valid - it is application dependent.
     *
     * @param fileType the type (version) of NITF file this text segment is for
     * @return default valid text segment, containing no text data.
     */
    public static TextSegment getDefault(final FileType fileType) {
        TextSegment textSegment = new TextSegmentImpl();
        textSegment.setFileType(fileType);
        textSegment.setIdentifier("");
        textSegment.setAttachmentLevel(0);
        textSegment.setTextDateTime(DateTimeImpl.getNitfDateTimeForNow());
        textSegment.setTextTitle("");
        textSegment.setSecurityMetadata(SecurityMetadataFactory.getDefaultMetadata(fileType));
        textSegment.setTextFormat(TextFormat.UTF8SUBSET);
        return textSegment;
    }

}
