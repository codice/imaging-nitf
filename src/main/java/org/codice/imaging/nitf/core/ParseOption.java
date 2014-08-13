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
package org.codice.imaging.nitf.core;

/**
    Potential parse-time settings for a NITF file.
    <p>
    This enumeration is intended to be passed as a variable
    of type Set to the main parse method.
    Constructing the Set from an EnumSet is likely to be the
    most useful method.
 */
public enum ParseOption {

    /**
        Extract the data associated with each image segment.
     */
    EXTRACT_IMAGE_SEGMENT_DATA,

    /**
        Extract the data associated with each graphic segment.
        <p>
        This is only meaningful for NITF 2.1 / NSIF 1.0 files,
        since NITF 2.0 files do not have graphic segments.
     */
    EXTRACT_GRAPHIC_SEGMENT_DATA,

    /**
        Extract the data associated with each symbol segment.
        <p>
        This is only meaningful for NITF 2.0 files, since
        NITF 2.1 / NSIF 1.0 files do not have symbol segments.
     */
    EXTRACT_SYMBOL_SEGMENT_DATA,

    /**
        Extract the data associated with each label segment.
        <p>
        This is only meaningful for NITF 2.0 files, since
        NITF 2.1 / NSIF 1.0 files do not have label segments.
     */
    EXTRACT_LABEL_SEGMENT_DATA,

    /**
        Extract the data associated with each label segment.
     */
    EXTRACT_TEXT_SEGMENT_DATA;
}
