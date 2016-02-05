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
package org.codice.imaging.nitf.nitfnetbeansfiletype;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Action;
import org.codice.imaging.cgm.AbstractElement;
import org.codice.imaging.cgm.CgmParser;
import org.codice.imaging.nitf.core.symbol.SymbolSegment;
import org.codice.imaging.nitf.core.symbol.SymbolType;
import org.openide.nodes.Children;
import org.openide.nodes.Sheet;
import org.openide.util.Exceptions;

class NitfSymbolSegmentNode extends AbstractSegmentNode {

    private final int symbolSegmentIndex;
    private final DeferredSegmentParseStrategy parseStrategy;
    private final SymbolSegment header;

    public NitfSymbolSegmentNode(final ChildSegmentKey childKey) throws ParseException {
        super(Children.LEAF);
        symbolSegmentIndex = childKey.getIndex();
        parseStrategy = childKey.getParseStrategy();
        header = parseStrategy.getSymbolSegmentHeader(symbolSegmentIndex);
        setDisplayName("Symbol Segment: " + getFriendlyName());
    }

    final String getFriendlyName() {
        if (!header.getSymbolName().trim().isEmpty()) {
            return header.getSymbolName().trim();
        }
        if (!header.getIdentifier().trim().isEmpty()) {
            return header.getIdentifier().trim();
        }
        return "(no name)";
    }

    List<AbstractElement> getCGMCommands() {
        try {
            CgmParser cgmParser = new CgmParser(parseStrategy.getSymbolSegmentDataReader(symbolSegmentIndex));
            cgmParser.buildCommandList();
            return cgmParser.getCommandList();
        } catch (ParseException | IOException ex) {
            Exceptions.printStackTrace(ex);
        }
        return new ArrayList<>();
    }

    @Override
    protected Sheet createSheet() {
        Sheet sheet = Sheet.createDefault();
        Sheet.Set set = Sheet.createPropertiesSet();
        sheet.put(set);
        addSubSegmentProperties(set, header);
        set.put(new StringProperty("symbolName",
                "Symbol Name",
                "The name of the symbol.",
                header.getSymbolName()));
        set.put(new StringProperty("symbolType",
                "Symbol Type",
                "The type of symbol.",
                header.getSymbolType().toString()));
        set.put(new StringProperty("symbolColour",
                "Symbol Colour",
                "The symbol colour.",
                header.getSymbolColour().toString()));
        set.put(new IntegerProperty("numberOfLinesPerSymbol",
                "Number of lines per symbol",
                "The number of lines (rows) per symbol. Always 0 for CGM.",
                header.getNumberOfLinesPerSymbol()));
        set.put(new IntegerProperty("numberOfPixelsPerLine",
                "Number of pixels per line",
                "The number of pixels per line. Always 0 for CGM.",
                header.getNumberOfPixelsPerLine()));
        set.put(new IntegerProperty("lineWidth",
                "Line width",
                "The line width. Always 0 for CGM and bitmapped symbols.",
                header.getLineWidth()));
        set.put(new IntegerProperty("bitsPerPixel",
                "Bits per pixel",
                "The number of bits per pixel. Always 0 for CGM. Always 1 for object symbols",
                header.getNumberOfBitsPerPixel()));
        set.put(new IntegerProperty("symbolDisplayLevel",
                "Symbol Display Level",
                "The display level of the symbol relative to other displayed file components.",
                header.getSymbolDisplayLevel()));
        set.put(new StringProperty("symbolLocation",
                "Symbol Location",
                "The location of the symbol's origin point relative to the CCS, image or graphic to which it is attached.",
                String.format(POINT_FORMATTER, header.getSymbolLocationColumn(), header.getSymbolLocationRow())));
        set.put(new StringProperty("symbolLocation2",
                "Second Symbol Location",
                "The second location for the symbol relative to the CCS, image or graphic to which it is attached.",
                String.format(POINT_FORMATTER, header.getSymbolLocation2Column(), header.getSymbolLocation2Row())));
        set.put(new StringProperty("symbolNumber",
                "Symbol Number",
                "The symbol number. Only non-zero for object symbols",
                header.getSymbolNumber()));
        set.put(new IntegerProperty("symbolRotation",
                "Symbol Rotation",
                "The symbol rotation angle, in counterclockwise direction. Only non-zero for object symbols.",
                header.getSymbolRotation()));
        return sheet;
    }

    @Override
    public Action[] getActions(final boolean popup) {
        Action[] actions = super.getActions(popup);
        if (header.getSymbolType() == SymbolType.CGM) {
            return combineActions(new SymbolSegmentViewAction(this), actions);
        } else {
            return actions;
        }
    }
}
