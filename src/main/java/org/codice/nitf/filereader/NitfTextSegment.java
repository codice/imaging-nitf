/**
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
 **/
package org.codice.nitf.filereader;

import java.text.ParseException;
import java.util.Date;

public class NitfTextSegment extends AbstractNitfSegment {

    private String textIdentifier = null;
    private int textAttachmentLevel = 0;
    private Date textDateTime = null;
    private String textTitle = null;
    private NitfSecurityMetadata securityMetadata = null;
    private TextFormat textFormat = TextFormat.UNKNOWN;

    public NitfTextSegment() {
    }

    public final void parse(final NitfReader nitfReader, final int textLength) throws ParseException {
        new NitfTextSegmentParser(nitfReader, textLength, this);
    }

    public final void setTextIdentifier(final String identifier) {
        textIdentifier = identifier;
    }

    public final String getTextIdentifier() {
        return textIdentifier;
    }

    public final void setTextAttachmentLevel(final int attachmentLevel) {
        textAttachmentLevel = attachmentLevel;
    }

    public final int getTextAttachmentLevel() {
        return textAttachmentLevel;
    }

    public final void setTextDateTime(final Date dateTime) {
        textDateTime = dateTime;
    }

    public final Date getTextDateTime() {
        return textDateTime;
    }

    public final void setTextTitle(final String title) {
        textTitle = title;
    }

    public final String getTextTitle() {
        return textTitle;
    }

    public final void setSecurityMetadata(final NitfSecurityMetadata nitfSecurityMetadata) {
        securityMetadata = nitfSecurityMetadata;
    }

    public final NitfSecurityMetadata getSecurityMetadata() {
        return securityMetadata;
    }

    public final void setTextFormat(final TextFormat format) {
        textFormat = format;
    }

    public final TextFormat getTextFormat() {
        return textFormat;
    }
}
