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

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import static javax.swing.Action.NAME;
import org.openide.windows.TopComponent;

class DataExtensionSegmentViewAction extends AbstractAction {

    private final NitfDataExtensionSegmentNode associatedNode;

    public DataExtensionSegmentViewAction(final NitfDataExtensionSegmentNode node) {
        putValue(NAME, "View");
        associatedNode = node;
    }

    @Override
    public void actionPerformed(final ActionEvent event) {
        TopComponent tc;
        if (associatedNode.isTreOverflow()) {
            TreTreeViewerPane viewer = new TreTreeViewerPane();
            viewer.setTreeModel(associatedNode.getTreTreeModel());
            tc = viewer;
        } else {
            TextViewerPane viewer = new TextViewerPane();
            viewer.setText(associatedNode.getText());
            tc = viewer;
        }
        tc.setDisplayName(associatedNode.getFriendlyName());
        tc.open();
        tc.toFront();
        tc.requestActive();
    }
}
