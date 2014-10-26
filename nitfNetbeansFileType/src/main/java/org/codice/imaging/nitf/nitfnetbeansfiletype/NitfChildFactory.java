/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.codice.imaging.nitf.nitfnetbeansfiletype;

import java.util.List;
import org.codice.imaging.nitf.core.AbstractNitfSubSegment;
import org.codice.imaging.nitf.core.Nitf;
import org.codice.imaging.nitf.core.NitfGraphicSegment;
import org.codice.imaging.nitf.core.NitfImageSegment;
import org.codice.imaging.nitf.core.NitfTextSegment;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Children;
import org.openide.nodes.Node;

class NitfChildFactory extends ChildFactory<AbstractNitfSubSegment> {

    private final Nitf nitf;

    public NitfChildFactory(final Nitf nitfData) {
        nitf = nitfData;
    }

    @Override
    protected boolean createKeys(final List list) {
        list.addAll(nitf.getImageSegments());
        list.addAll(nitf.getGraphicSegments());
        list.addAll(nitf.getTextSegments());
        list.addAll(nitf.getDataExtensionSegments());
        return true;
    }

    @Override
    protected Node createNodeForKey(final AbstractNitfSubSegment key) {
        if (key instanceof NitfImageSegment) {
            return new NitfImageSegmentNode((NitfImageSegment) key);
        } else if (key instanceof NitfGraphicSegment) {
            return new NitfGraphicSegmentNode((NitfGraphicSegment) key);
        } else if (key instanceof NitfTextSegment) {
            return new NitfTextSegmentNode((NitfTextSegment) key);
        } else {
            Node childNode = new AbstractNode(Children.LEAF);
            childNode.setDisplayName(key.getClass().getSimpleName() + " : " + key.getIdentifier());
            return childNode;
        }
    }
}
