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

import java.text.ParseException;
import org.codice.imaging.nitf.core.NitfGraphicSegmentHeader;
import org.openide.nodes.Children;
import org.openide.nodes.Sheet;


class NitfGraphicSegmentNode extends AbstractSegmentNode {

    private static final String BOUNDING_BOX_POSITION_DESCRIPTION =
                    "bounding box for the CGM graphic, relative to the CCS, image or graphic to which the graphic is attached.";

    private final ChildSegmentKey childKey;
    private final NitfGraphicSegmentHeader header;

    public NitfGraphicSegmentNode(final ChildSegmentKey key) throws ParseException {
        super(Children.LEAF);
        childKey = key;
        DeferredSegmentParseStrategy parseStrategy = childKey.getParseStrategy();
        header = parseStrategy.getGraphicSegmentHeader(childKey.getIndex());
        setDisplayName("Graphic Segment: " + header.getIdentifier());
    }

   @Override
    protected Sheet createSheet() {
        Sheet sheet = Sheet.createDefault();
        Sheet.Set set = Sheet.createPropertiesSet();
        sheet.put(set);
        addSubSegmentProperties(set, header);
        set.put(new StringProperty("graphicName",
                "Graphic Name",
                "The alphanumeric name for the graphic",
                header.getGraphicName()));
        set.put(new IntegerProperty("graphicDisplayLevel",
                "Graphic Display Level",
                "The display level of the graphic relative to other displayed file components.",
                header.getGraphicDisplayLevel()));
        set.put(new StringProperty("graphicLocation",
                "Graphic Location",
                "The location of the graphic's origin point relative to the CCS, image or graphic to which it is attached.",
                String.format(POINT_FORMATTER, header.getGraphicLocationColumn(), header.getGraphicLocationRow())));
        set.put(new StringProperty("firstGraphicBoundLocation",
                "First Graphic Bound Location",
                "The upper left corner of the " + BOUNDING_BOX_POSITION_DESCRIPTION,
                String.format(POINT_FORMATTER, header.getBoundingBox1Column(), header.getBoundingBox1Row())));
        set.put(new StringProperty("secondGraphicBoundLocation",
                "Second Graphic Bound Location",
                "The lower right corner of the " + BOUNDING_BOX_POSITION_DESCRIPTION,
                String.format(POINT_FORMATTER, header.getBoundingBox2Column(), header.getBoundingBox2Row())));
        set.put(new StringProperty("graphicColour",
                "Graphic Colour",
                "Flag for whether the CGM graphic has any colour ('C') or is monochrome ('M').",
                header.getGraphicColour().toString()));
        return sheet;
    }

}
