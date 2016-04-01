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

import java.io.IOException;
import org.codice.imaging.nitf.core.common.CommonBasicSegmentImpl;
import org.codice.imaging.nitf.core.common.CommonConstants;
import static org.codice.imaging.nitf.core.common.CommonConstants.STANDARD_DATE_TIME_LENGTH;
import org.codice.imaging.nitf.core.common.NitfDateTime;
import org.codice.imaging.nitf.core.common.NitfFormatException;
import org.codice.imaging.nitf.core.tre.TreParser;
import org.codice.imaging.nitf.core.tre.TreSource;

/**
 * Text segment information.
 */
class TextSegmentImpl extends CommonBasicSegmentImpl implements TextSegment {

    private NitfDateTime textDateTime = null;
    private String textTitle = null;
    private TextFormat textFormat = TextFormat.UNKNOWN;
    private String textData = null;

    /**
     * Default constructor.
     */
    TextSegmentImpl() {
    }

    /**
     * Set text date and time.
     *
     * This field shall contain the time (UTC) (Zulu) of origination of the text.
     *
     * @param dateTime the date and time of the text.
     */
    @Override
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
     * {@inheritDoc}
     */
    @Override
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
     * {@inheritDoc}
     */
    @Override
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

    /**
     * {@inheritDoc}
     */
    @Override
    public final long getHeaderLength() throws NitfFormatException, IOException {
        long headerLength = TextConstants.TE.length()
                + TextConstants.TEXTID_LENGTH
                + TextConstants.TXTALVL_LENGTH
                + STANDARD_DATE_TIME_LENGTH
                + TextConstants.TXTITL_LENGTH
                + getSecurityMetadata().getSerialisedLength()
                + CommonConstants.ENCRYP_LENGTH
                + TextConstants.TXTFMT_LENGTH
                + TextConstants.TXSHDL_LENGTH;
        TreParser treParser = new TreParser();
        int extendedDataLength = treParser.getTREs(this, TreSource.TextExtendedSubheaderData).length;
        if (extendedDataLength > 0) {
            headerLength += TextConstants.TXSOFL_LENGTH;
            headerLength += extendedDataLength;
        }
        return headerLength;
    }
}
