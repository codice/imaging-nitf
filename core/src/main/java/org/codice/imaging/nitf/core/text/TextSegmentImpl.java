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

import org.codice.imaging.nitf.core.AbstractSubSegment;
import org.codice.imaging.nitf.core.common.NitfDateTime;

/**
 * Text segment information.
 */
class TextSegmentImpl extends AbstractSubSegment implements TextSegment {

    private NitfDateTime textDateTime = null;
    private String textTitle = null;
    private TextFormat textFormat = TextFormat.UNKNOWN;
    private String textData = null;

    /**
     * Default constructor.
     */
    public TextSegmentImpl() {
    }

    /**
     * Set text date and time.
     *
     * This field shall contain the time (UTC) (Zulu) of origination of the text.
     *
     * @param dateTime the date and time of the text.
     */
    public final void setTextDateTime(final NitfDateTime dateTime) {
        textDateTime = dateTime;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final NitfDateTime getTextDateTime() {
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
     * {@inheritDoc}
     */
    @Override
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
     * {@inheritDoc}
     */
    @Override
    public final TextFormat getTextFormat() {
        return textFormat;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String getData() {
        return textData;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void setData(final String text) {
        textData = text;
    }
}
