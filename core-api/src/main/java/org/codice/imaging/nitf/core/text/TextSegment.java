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
package org.codice.imaging.nitf.core.text;

import org.codice.imaging.nitf.core.common.DateTime;
import org.codice.imaging.nitf.core.common.CommonBasicSegment;

/**
 * Represents a NITF Text Segment.
 */
public interface TextSegment extends CommonBasicSegment {

    /**
     Return text date and time.
     <p>
     This field shall contain the time (UTC) (Zulu) of origination of the text.

     @return the date and time of the text.
     */
    DateTime getTextDateTime();

    /**
     * Set text date and time.
     *
     * This field shall contain the time (UTC) (Zulu) of origination of the text.
     *
     * @param dateTime the date and time of the text.
     */
    void setTextDateTime(final DateTime dateTime);

    /**
     Return text title.
     <p>
     This field shall contain the title of the text item.

     @return text title
     */
    String getTextTitle();

    /**
     * Set text title.
     * <p>
     * This field shall contain the title of the text item.
     *
     * @param title text title (80 characters maximum).
     */
    void setTextTitle(final String title);

    /**
     Return the text format indicator.
     <p>
     See TextFormat for the meaning of the enumerated values.

     @return the text format
     */
    TextFormat getTextFormat();

    /**
     * Set the text format indicator.
     * <p>
     * See TextFormat for the meaning of the enumerated values.
     *
     * @param format the text format
     */
    void setTextFormat(final TextFormat format);

    /**
     * Get the text segment data string.
     *
     * @return string containing text for this segment.
     *
     */
    String getData();

    /**
     * Set the text segment data string.
     *
     * @param text the text associated with this segment
     */
    void setData(String text);
}
