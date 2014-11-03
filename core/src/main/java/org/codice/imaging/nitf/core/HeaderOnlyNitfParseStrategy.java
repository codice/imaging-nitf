/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.codice.imaging.nitf.core;

import java.text.ParseException;

/**
 * Parse strategy that only extracts headers.
 */
public class HeaderOnlyNitfParseStrategy extends SlottedNitfParseStrategy {


    /**
     * Constructor.
     */
    public HeaderOnlyNitfParseStrategy() {
    }


    @Override
    public final void baseHeadersRead(final NitfReader reader) {
        try {
            readImageSegmentHeadersOnly(reader);
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
