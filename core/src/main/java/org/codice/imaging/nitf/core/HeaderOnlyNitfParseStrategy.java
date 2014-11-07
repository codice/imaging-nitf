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
            for (int i = 0; i < nitfFileLevelHeader.getNumberOfImageSegmentLengths(); ++i) {
                parseImageSegmentHeaderButSkipData(reader, i);
            }
            if (nitfFileLevelHeader.getFileType() == FileType.NITF_TWO_ZERO) {
                for (int i = 0; i < nitfFileLevelHeader.getNumberOfSymbolSegmentLengths(); ++i) {
                   parseSymbolSegmentHeaderButSkipData(reader, i);
                }
                for (int i = 0; i < nitfFileLevelHeader.getNumberOfLabelSegmentLengths(); ++i) {
                   parseLabelSegmentHeaderButSkipData(reader, i);
                }
            } else {
                for (int i = 0; i < nitfFileLevelHeader.getNumberOfGraphicSegmentLengths(); ++i) {
                   parseGraphicSegmentHeaderButSkipData(reader, i);
                }
            }
            for (int i = 0; i < nitfFileLevelHeader.getNumberOfTextSegmentLengths(); ++i) {
                parseTextSegmentHeaderButSkipData(reader, i);
            }
            readDataExtensionSegments(reader);
        } catch (ParseException ex) {
            System.out.println("Exception should be logged: " + ex);
        }
    }



}
