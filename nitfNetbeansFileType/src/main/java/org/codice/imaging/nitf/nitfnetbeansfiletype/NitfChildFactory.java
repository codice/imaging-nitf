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
import java.util.List;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;

class NitfChildFactory extends ChildFactory<ChildSegmentKey> {

    private final DeferredSegmentParseStrategy parseStrategy;

    public NitfChildFactory(final DeferredSegmentParseStrategy nitfData) {
        parseStrategy = nitfData;
    }

    @Override
    protected boolean createKeys(final List list) {
        for (int i = 0; i < parseStrategy.getImageSegmentHeaderOffsets().size(); ++i) {
            ChildSegmentKey key = new ChildSegmentKey();
            key.setSegmentType("Image");
            key.setParseStrategy(parseStrategy);
            key.setIndex(i);
            list.add(key);
        }
        for (int i = 0; i < parseStrategy.getGraphicSegmentHeaderOffsets().size(); ++i) {
            ChildSegmentKey key = new ChildSegmentKey();
            key.setSegmentType("Graphic");
            key.setParseStrategy(parseStrategy);
            key.setIndex(i);
            list.add(key);
        }
        for (int i = 0; i < parseStrategy.getSymbolSegmentHeaderOffsets().size(); ++i) {
            ChildSegmentKey key = new ChildSegmentKey();
            key.setSegmentType("Symbol");
            key.setParseStrategy(parseStrategy);
            key.setIndex(i);
            list.add(key);
        }
        for (int i = 0; i < parseStrategy.getLabelSegmentHeaderOffsets().size(); ++i) {
            ChildSegmentKey key = new ChildSegmentKey();
            key.setSegmentType("Label");
            key.setParseStrategy(parseStrategy);
            key.setIndex(i);
            list.add(key);
        }
        for (int i = 0; i < parseStrategy.getTextSegmentHeaderOffsets().size(); ++i) {
            ChildSegmentKey key = new ChildSegmentKey();
            key.setSegmentType("Text");
            key.setParseStrategy(parseStrategy);
            key.setIndex(i);
            list.add(key);
        }
        for (int i = 0; i < parseStrategy.getDataExtensionSegmentHeaderOffsets().size(); ++i) {
            ChildSegmentKey key = new ChildSegmentKey();
            key.setSegmentType("DES");
            key.setParseStrategy(parseStrategy);
            key.setIndex(i);
            list.add(key);
        }
        return true;
    }

    @Override
    protected Node createNodeForKey(final ChildSegmentKey key) {
        try {
            switch(key.getSegmentType()) {
                case "Image":
                    return new NitfImageSegmentNode(key);
                case "Graphic":
                    return new NitfGraphicSegmentNode(key);
                case "Symbol":
                    return new NitfSymbolSegmentNode(key);
                case "Label":
                    return new NitfLabelSegmentNode(key);
                case "Text":
                    return new NitfTextSegmentNode(key);
                case "DES":
                    return new NitfDataExtensionSegmentNode(key);
                default:
                    break;
            }
        } catch (ParseException ex) {
            Exceptions.printStackTrace(ex);
        }

        Node childNode = new AbstractNode(Children.LEAF);
        childNode.setDisplayName(key.getClass().getSimpleName() + " Not implemented : " + key.getSegmentType());
        return childNode;
    }
}
