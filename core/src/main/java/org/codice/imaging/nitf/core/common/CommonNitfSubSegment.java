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
package org.codice.imaging.nitf.core.common;

/**
 Common data elements for NITF data segment subheaders.
 <p>
 This excludes the Data Extension Segment subheader.
 */
public interface CommonNitfSubSegment extends CommonNitfSegment {

    /**
     Return the extended subheader overflow index (IXSOFL/SXSOFL/LXSOFL/TXSOFL).
     <p>
     This is the (1-base) index of the TRE into which extended header data
     overflows.

     @return the extended header data overflow index
     */
    int getExtendedHeaderDataOverflow();

    /**
     Return the attachment level for the segment.
     <p>
     The valid values for this are zero (not attached) or the display level
     for an image, graphic, symbol or label (as appropriate to the NITF file type)
     in the file.

     @return the attachment level
     */
    int getAttachmentLevel();
}
