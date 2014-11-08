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

/**
 * Parse strategy that just extracts headers and the text data.
 */
public class TextDataExtractionParseStrategy extends SlottedNitfParseStrategy {

    @Override
    public final void baseHeadersRead(final NitfReader reader) {
        try {
            for (int i = 0; i < nitfFileLevelHeader.getImageSegmentSubHeaderLengths().size(); ++i) {
                parseImageSegmentHeaderButSkipData(reader, i);
            }
            if (nitfFileLevelHeader.getFileType() == FileType.NITF_TWO_ZERO) {
                for (int i = 0; i < nitfFileLevelHeader.getSymbolSegmentSubHeaderLengths().size(); ++i) {
                    parseSymbolSegmentHeaderButSkipData(reader, i);
                }
                for (int i = 0; i < nitfFileLevelHeader.getLabelSegmentSubHeaderLengths().size(); ++i) {
                   parseLabelSegmentHeaderButSkipData(reader, i);
                }
            } else {
                for (int i = 0; i < nitfFileLevelHeader.getGraphicSegmentSubHeaderLengths().size(); ++i) {
                   parseGraphicSegmentHeaderButSkipData(reader, i);
                }
            }
            for (int i = 0; i < nitfFileLevelHeader.getTextSegmentSubHeaderLengths().size(); ++i) {
                parseTextSegmentHeaderAndData(reader, i);
            }
            for (int i = 0; i < nitfFileLevelHeader.getDataExtensionSegmentSubHeaderLengths().size(); ++i) {
                parseDataExtensionSegmentHeaderButSkipData(reader, i);
            }
        } catch (ParseException ex) {
            System.out.println("Exception should be logged: " + ex);
        }
    }

}
