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
import java.util.Set;

public class NitfTextSegment extends AbstractNitfSubSegment {

    private Date textDateTime = null;
    private String textTitle = null;
    private TextFormat textFormat = TextFormat.UNKNOWN;

    private String data = null;

    public NitfTextSegment() {
    }

    public final void parse(final NitfReader nitfReader,
                            final int textLength,
                            final Set<ParseOption> parseOptions) throws ParseException {
        new NitfTextSegmentParser(nitfReader, textLength, parseOptions, this);
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

    public final void setTextFormat(final TextFormat format) {
        textFormat = format;
    }

    public final TextFormat getTextFormat() {
        return textFormat;
    }

    public final void setTextData(final String textData) {
        data = textData;
    }

    public final String getTextData() {
        return data;
    }
}
