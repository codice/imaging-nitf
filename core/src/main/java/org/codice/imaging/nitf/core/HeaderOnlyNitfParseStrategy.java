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
                parseTextSegmentHeaderButSkipData(reader, i);
            }
            for (int i = 0; i < nitfFileLevelHeader.getDataExtensionSegmentSubHeaderLengths().size(); ++i) {
                parseDataExtensionSegmentHeaderButSkipData(reader, i);
            }
        } catch (ParseException ex) {
            System.out.println("Exception should be logged: " + ex);
        }
    }



}
