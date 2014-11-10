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

    @Override
    protected final void handleImageSegment(final NitfReader reader, final int i) throws ParseException {
        parseImageSegmentHeaderButSkipData(reader, i);
    }

    @Override
    protected final void handleSymbolSegment(final NitfReader reader, final int i) throws ParseException {
        parseSymbolSegmentHeaderButSkipData(reader, i);
    }

    @Override
    protected final void handleLabelSegment(final NitfReader reader, final int i) throws ParseException {
        parseLabelSegmentHeaderButSkipData(reader, i);
    }

    @Override
    protected final void handleGraphicSegment(final NitfReader reader, final int i) throws ParseException {
        parseGraphicSegmentHeaderButSkipData(reader, i);
    }

    @Override
    protected final void handleTextSegment(final NitfReader reader, final int i) throws ParseException {
        parseTextSegmentHeaderButSkipData(reader, i);
    }

    @Override
    protected final void handleDataExtensionSegment(final NitfReader reader, final int i) throws ParseException {
        parseDataExtensionSegmentHeaderButSkipData(reader, i);
    }


}
