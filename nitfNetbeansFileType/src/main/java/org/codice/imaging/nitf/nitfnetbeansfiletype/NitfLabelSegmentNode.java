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
import javax.swing.Action;
import org.codice.imaging.nitf.core.label.LabelSegment;
import org.openide.nodes.Children;
import org.openide.nodes.Sheet;

class NitfLabelSegmentNode extends AbstractSegmentNode {

    private final ChildSegmentKey childKey;
    private final LabelSegment header;

    public NitfLabelSegmentNode(final ChildSegmentKey key) throws ParseException {
        super(Children.LEAF);
        childKey = key;
        DeferredSegmentParseStrategy parseStrategy = childKey.getParseStrategy();
        header = parseStrategy.getLabelSegmentHeader(childKey.getIndex());
        setDisplayName("Label Segment: " + getFriendlyName());
    }

    final String getFriendlyName() {
        if (!header.getIdentifier().trim().isEmpty()) {
            return header.getIdentifier().trim();
        }
        return "(no name)";
    }

    String getText() {
        return header.getData();
    }

    @Override
    protected Sheet createSheet() {
        Sheet sheet = Sheet.createDefault();
        Sheet.Set set = Sheet.createPropertiesSet();
        sheet.put(set);
        addSubSegmentProperties(set, header);
        set.put(new StringProperty("labelLocation",
                "Label Location",
                "The location of the label's origin point relative to the CCS, image or graphic to which it is attached.",
                String.format(POINT_FORMATTER, header.getLabelLocationColumn(), header.getLabelLocationRow())));
        set.put(new IntegerProperty("labelCellWidth",
                "Label Cell Width",
                "The width (in pixels) of one character cell. 0 indicates information not provided",
                header.getLabelCellWidth()));
        set.put(new IntegerProperty("labelCellHeight",
                "Label Cell Height",
                "The height (in pixels) of one character cell. 0 indicates information not provided",
                header.getLabelCellWidth()));
        set.put(new IntegerProperty("labelDisplayLevel",
                "Label Display Level",
                "The display level of the label relative to other displayed file components.",
                header.getLabelDisplayLevel()));
        set.put(new StringProperty("labelTextColour",
                "Label Text Colour",
                "The label text foreground colour.",
                header.getLabelTextColour().toString()));
        set.put(new StringProperty("labelBackgroundColour",
                "Label Background Colour",
                "The label background colour.",
                header.getLabelBackgroundColour().toString()));
        return sheet;
    }

    @Override
    public Action[] getActions(final boolean popup) {
        return combineActions(new LabelSegmentViewAction(this), super.getActions(popup));
    }

}
