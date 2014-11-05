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

import java.text.ParseException;

// TODO: make public if we keep this
class ImageDataExtractionParseStrategy extends SlottedNitfParseStrategy {

    @Override
    public final void baseHeadersRead(final NitfReader reader) {
        try {
            for (int i = 0; i < nitfFileLevelHeader.getNumberOfImageSegmentLengths(); ++i) {
                parseImageSegmentHeaderAndData(reader, i);
            }
            if (nitfFileLevelHeader.getFileType() == FileType.NITF_TWO_ZERO) {
                readSymbolSegmentHeadersOnly(reader);
                readLabelSegmentHeadersOnly(reader);
            } else {
                readGraphicSegmentHeadersOnly(reader);
            }
            readTextSegmentHeadersOnly(reader);
            readDataExtensionSegments(reader);
        } catch (ParseException ex) {
            System.out.println("Exception should be logged: " + ex);
        }
    }

}
