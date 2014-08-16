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
package org.codice.imaging.nitf.core;

import java.util.Date;

/**
    Text segment subheader information.
*/
public class NitfTextSegment extends AbstractNitfSubSegment {

    private Date textDateTime = null;
    private String textTitle = null;
    private TextFormat textFormat = TextFormat.UNKNOWN;

    private String data = null;

    /**
        Default constructor.
    */
    public NitfTextSegment() {
    }

    /**
        Set text date and time.
        <p>
        This field shall contain the time (UTC) (Zulu) of origination of the text.

        @param dateTime the date and time of the text.
    */
    public final void setTextDateTime(final Date dateTime) {
        textDateTime = dateTime;
    }

    /**
        Return text date and time.
        <p>
        This field shall contain the time (UTC) (Zulu) of origination of the text.

        @return the date and time of the text.
    */
    public final Date getTextDateTime() {
        return textDateTime;
    }

    /**
        Set text title.
        <p>
        This field shall contain the title of the text item.

        @param title text title (80 characters maximum).
    */
    public final void setTextTitle(final String title) {
        textTitle = title;
    }

    /**
        Return text title.
        <p>
        This field shall contain the title of the text item.

        @return text title
    */
    public final String getTextTitle() {
        return textTitle;
    }

    /**
        Set the text format indicator.
        <p>
        See TextFormat for the meaning of the enumerated values.

        @param format the text format
    */
    public final void setTextFormat(final TextFormat format) {
        textFormat = format;
    }

    /**
        Return the text format indicator.
        <p>
        See TextFormat for the meaning of the enumerated values.

        @return the text format
    */
    public final TextFormat getTextFormat() {
        return textFormat;
    }

    /**
        Set the text data.
        <p>
        This is the data in the text data segment.

        @param textData the text data segment content
    */
    public final void setTextData(final String textData) {
        data = textData;
    }

    /**
        Return the text data.
        <p>
        This is the contents of the data segment. Depending on how the
        parsing was configured, this can be null.

        @return the text data
    */
    public final String getTextData() {
        return data;
    }
}
