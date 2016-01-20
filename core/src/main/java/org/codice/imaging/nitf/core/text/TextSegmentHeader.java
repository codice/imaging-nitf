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

import org.codice.imaging.nitf.core.common.CommonNitfSubSegment;
import org.codice.imaging.nitf.core.common.NitfDateTime;

/**
 * Represents a Nitf TextSegmentHeader.
 */
public interface TextSegmentHeader extends CommonNitfSubSegment {

    /**
     Return text date and time.
     <p>
     This field shall contain the time (UTC) (Zulu) of origination of the text.

     @return the date and time of the text.
     */
    NitfDateTime getTextDateTime();

    /**
     Return text title.
     <p>
     This field shall contain the title of the text item.

     @return text title
     */
    String getTextTitle();

    /**
     Return the text format indicator.
     <p>
     See TextFormat for the meaning of the enumerated values.

     @return the text format
     */
    TextFormat getTextFormat();

    /**
     Return the text data length.

     @return the text data segment length, in bytes
     */
    int getTextDataLength();

    /**
     Set the text segment data length.
     <p>
     This is the length of the contents of the associated data segment.

     @param dataLength the text data segment length, in bytes
     */
    void setTextSegmentDataLength(int dataLength);
}
