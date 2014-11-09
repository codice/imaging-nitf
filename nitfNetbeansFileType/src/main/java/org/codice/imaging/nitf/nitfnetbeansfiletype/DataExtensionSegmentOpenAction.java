/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.codice.imaging.nitf.nitfnetbeansfiletype;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import static javax.swing.Action.NAME;

class DataExtensionSegmentOpenAction extends AbstractAction {

    private final NitfDataExtensionSegmentNode associatedNode;

    public DataExtensionSegmentOpenAction(final NitfDataExtensionSegmentNode node) {
        putValue(NAME, "Open");
        associatedNode = node;
    }

    @Override
    public void actionPerformed(final ActionEvent event) {
        TextViewerPane viewer = new TextViewerPane();
        viewer.setDisplayName(associatedNode.getFriendlyName());
        viewer.setText(associatedNode.getText());
        viewer.open();
        viewer.toFront();
    }
}
