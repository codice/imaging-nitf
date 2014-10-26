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

import org.codice.imaging.nitf.core.NitfTextSegment;
import org.openide.nodes.Children;
import org.openide.nodes.Sheet;

class NitfTextSegmentNode extends AbstractSegmentNode {

    private final NitfTextSegment segment;

    public NitfTextSegmentNode(final NitfTextSegment nitfTextSegment) {
        super(Children.LEAF);
        segment = nitfTextSegment;
        setDisplayName("Text Segment: " + segment.getIdentifier());
    }

   @Override
    protected Sheet createSheet() {
        Sheet sheet = Sheet.createDefault();
        Sheet.Set set = Sheet.createPropertiesSet();
        sheet.put(set);
        addSubSegmentProperties(set, segment);
        set.put(new DateProperty("textDateTime",
                "Text Date and Time",
                "Date and time of this origination of the text.",
                segment.getTextDateTime().toDate()));
        set.put(new StringProperty("textTitle",
                "Text Title",
                "The title of the text item",
                segment.getTextTitle()));
        set.put(new StringProperty("textFormat",
                "Text Format",
                "Three-character code indicating the format of type of text data.",
                segment.getTextFormat().toString()));
        return sheet;
    }
}
