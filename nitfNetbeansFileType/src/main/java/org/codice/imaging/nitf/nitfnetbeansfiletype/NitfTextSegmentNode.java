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

import javax.swing.Action;
import org.codice.imaging.nitf.core.NitfTextSegment;
import org.openide.nodes.Children;
import org.openide.nodes.Sheet;

class NitfTextSegmentNode extends AbstractSegmentNode {

    private final NitfTextSegment segment;

    public NitfTextSegmentNode(final NitfTextSegment nitfTextSegment) {
        super(Children.LEAF);
        segment = nitfTextSegment;
        setDisplayName("Text Segment: " + getFriendlyName());
    }

    final String getFriendlyName() {
        if (!segment.getTextTitle().trim().isEmpty()) {
            return segment.getTextTitle().trim();
        }
        if (!segment.getIdentifier().trim().isEmpty()) {
            return segment.getIdentifier().trim();
        }
        return "(no name)";
    }

    String getText() {
        return segment.getTextData();
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

    @Override
    public Action[] getActions(final boolean popup) {
        return combineActions(new TextSegmentOpenAction(this), super.getActions(popup));
    }

    /**
     * Prepend an action to an existing array of actions.
     *
     * @param action the action to prepend
     * @param actions the existing actions
     * @return combined array of actions
     */
    protected Action[] combineActions(final Action action, final Action[] actions) {
        Action[] combinedActions = new Action[actions.length + 1];
        combinedActions[0] = action;
        for (int i = 1; i < combinedActions.length; ++i) {
            combinedActions[i] = actions[i - 1];
        }
        return combinedActions;
    }
}
