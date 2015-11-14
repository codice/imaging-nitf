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
import org.codice.imaging.nitf.core.NitfTextSegmentHeader;
import org.openide.nodes.Children;
import org.openide.nodes.Sheet;
import org.openide.util.Exceptions;

class NitfTextSegmentNode extends AbstractSegmentNode {

    private final ChildSegmentKey childKey;
    private final NitfTextSegmentHeader header;

    public NitfTextSegmentNode(final ChildSegmentKey key) throws ParseException {
        super(Children.LEAF);
        childKey = key;
        DeferredSegmentParseStrategy parseStrategy = childKey.getParseStrategy();
        header = parseStrategy.getTextSegmentHeader(childKey.getIndex());
        setDisplayName("Text Segment: " + getFriendlyName());
    }

    final String getFriendlyName() {
        if (!header.getTextTitle().trim().isEmpty()) {
            return header.getTextTitle().trim();
        }
        if (!header.getIdentifier().trim().isEmpty()) {
            return header.getIdentifier().trim();
        }
        return "(no name)";
    }

    String getText() {
        try {
            DeferredSegmentParseStrategy parseStrategy = childKey.getParseStrategy();
            return parseStrategy.getTextSegmentData(header, childKey.getIndex());
        } catch (ParseException ex) {
            Exceptions.printStackTrace(ex);
        }
        return "";
    }

    @Override
    protected Sheet createSheet() {
        Sheet sheet = Sheet.createDefault();
        Sheet.Set set = Sheet.createPropertiesSet();
        sheet.put(set);
        addSubSegmentProperties(set, header);
        set.put(new DateProperty("textDateTime",
                "Text Date and Time",
                "Date and time of this origination of the text.",
                header.getTextDateTime().toDate()));
        set.put(new StringProperty("textTitle",
                "Text Title",
                "The title of the text item",
                header.getTextTitle()));
        set.put(new StringProperty("textFormat",
                "Text Format",
                "Three-character code indicating the format of type of text data.",
                header.getTextFormat().toString()));
        return sheet;
    }

    @Override
    public Action[] getActions(final boolean popup) {
        return combineActions(new TextSegmentViewAction(this), super.getActions(popup));
    }

}
