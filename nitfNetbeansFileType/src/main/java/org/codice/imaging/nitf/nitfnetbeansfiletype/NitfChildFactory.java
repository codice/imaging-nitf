/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.codice.imaging.nitf.nitfnetbeansfiletype;

import java.util.List;
import org.codice.imaging.nitf.core.Nitf;
import org.codice.imaging.nitf.core.NitfGraphicSegment;
import org.codice.imaging.nitf.core.NitfImageSegment;
import org.codice.imaging.nitf.core.NitfTextSegment;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Children;
import org.openide.nodes.Node;

class NitfChildFactory extends ChildFactory<String> {

    private final Nitf nitf;

    public NitfChildFactory(Nitf nitfData) {
        nitf = nitfData;
    }

    @Override
    protected boolean createKeys(List list) {
        for (int imageSegmentCount = 0; imageSegmentCount < nitf.getNumberOfImageSegments(); ++imageSegmentCount) {
            NitfImageSegment imageSegment = nitf.getImageSegmentZeroBase(imageSegmentCount);
            list.add("Image segment " + imageSegmentCount + ": " + imageSegment.getImageIdentifier2());
        }
        for (int i = 0; i < nitf.getNumberOfGraphicSegments(); ++i) {
            NitfGraphicSegment graphicSegment = nitf.getGraphicSegmentZeroBase(i);
            list.add("Graphic segment " + i + ": " + graphicSegment.getGraphicName());
        }
        for (int i = 0; i < nitf.getNumberOfTextSegments(); ++i) {
            NitfTextSegment textSegment = nitf.getTextSegmentZeroBase(i);
            list.add("Text segment " + i + ": " + textSegment.getTextTitle());
        }
        return true;
    }

    @Override
    protected Node createNodeForKey(String key) {
        Node childNode = new AbstractNode(Children.LEAF);
        childNode.setDisplayName(key);
        return childNode;
    }
}
