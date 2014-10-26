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

import org.codice.imaging.nitf.core.NitfGraphicSegment;
import org.openide.nodes.Children;
import org.openide.nodes.Sheet;


class NitfGraphicSegmentNode extends AbstractSegmentNode {

    private static final String POINT_FORMATTER = "[%d, %d]";

    private final NitfGraphicSegment segment;

    public NitfGraphicSegmentNode(final NitfGraphicSegment nitfGraphicSegment) {
        super(Children.LEAF);
        segment = nitfGraphicSegment;
        setDisplayName("Graphic Segment: " + segment.getIdentifier());
    }

   @Override
    protected Sheet createSheet() {
        Sheet sheet = Sheet.createDefault();
        Sheet.Set set = Sheet.createPropertiesSet();
        sheet.put(set);
        addSubSegmentProperties(set, segment);
        set.put(new StringProperty("graphicName",
                "Graphic Name",
                "The alphanumeric name for the graphic",
                segment.getGraphicName()));
        set.put(new IntegerProperty("graphicDisplayLevel",
                "Graphic Display Level",
                "The display level of the graphic relative to other displayed file components.",
                segment.getGraphicDisplayLevel()));
        set.put(new StringProperty("graphicLocation",
                "Graphic Location",
                "The location of the graphic's origin point relative to the CCS, image or graphic to which it is attached.",
                String.format(POINT_FORMATTER, segment.getGraphicLocationColumn(), segment.getGraphicLocationRow())));
        set.put(new StringProperty("firstGraphicBoundLocation",
                "First Graphic Bound Location",
                "The upper left corner of the bounding box for the CGM graphic, "
                + "relative to the CCS, image or graphic to which the graphic is attached.",
                String.format(POINT_FORMATTER, segment.getBoundingBox1Column(), segment.getBoundingBox1Row())));
        set.put(new StringProperty("secondGraphicBoundLocation",
                "Second Graphic Bound Location",
                "The lower right corner of the bounding box for the CGM graphic, "
                + "relative to the CCS, image or graphic to which the graphic is attached.",
                String.format(POINT_FORMATTER, segment.getBoundingBox2Column(), segment.getBoundingBox2Row())));
        set.put(new StringProperty("graphicColour",
                "Graphic Colour",
                "Flag for whether the CGM graphic has any colour ('C') or is monochrome ('M').",
                segment.getGraphicColour().toString()));
        return sheet;
    }
}
