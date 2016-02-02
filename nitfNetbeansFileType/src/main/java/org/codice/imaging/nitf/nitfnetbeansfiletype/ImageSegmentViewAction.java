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
import java.io.IOException;
import javax.swing.AbstractAction;
import static javax.swing.Action.NAME;
import org.openide.util.Exceptions;

class ImageSegmentViewAction extends AbstractAction {

    private final NitfImageSegmentNode associatedNode;

    public ImageSegmentViewAction(final NitfImageSegmentNode node) {
        putValue(NAME, "View");
        associatedNode = node;
    }

    @Override
    public void actionPerformed(final ActionEvent event) {
        GraphicViewPane viewer = new GraphicViewPane();
        viewer.setBackground(associatedNode.getBackgroundColour());
        viewer.setDisplayName(associatedNode.getFriendlyName());
        try {
            viewer.setImage(associatedNode.getImageSegmentHeader());
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
        viewer.open();
        viewer.toFront();
        viewer.requestActive();
    }
}
