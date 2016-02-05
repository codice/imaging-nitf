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
import javax.swing.tree.TreeModel;
import org.codice.imaging.nitf.core.dataextension.DataExtensionSegment;
import org.openide.nodes.Children;
import org.openide.nodes.Sheet;
import org.openide.util.Exceptions;

class NitfDataExtensionSegmentNode extends AbstractCommonSegmentNode {

    private final DataExtensionSegment header;
    private final ChildSegmentKey childKey;

    public NitfDataExtensionSegmentNode(final ChildSegmentKey key) throws ParseException {
        super(Children.LEAF);
        childKey = key;
        DeferredSegmentParseStrategy parseStrategy = childKey.getParseStrategy();
        header = parseStrategy.getDataExtensionSegment(childKey.getIndex());
        setDisplayName("DES: " + getFriendlyName());
    }

    @Override
    protected Sheet createSheet() {
        Sheet sheet = Sheet.createDefault();
        Sheet.Set set = Sheet.createPropertiesSet();
        addCommonSegmentProperties(set, header);
        set.put(new IntegerProperty("desVersion",
                "DES Version",
                "Version number of the Data Extension Segment type.",
                header.getDESVersion()));
        String desIdentifier = header.getIdentifier().trim();
        if (("TRE_OVERFLOW".equals(desIdentifier))
            || ("Registered Extensions".equals(desIdentifier))
            || ("Controlled Extensions".equals(desIdentifier))) {
            set.put(new StringProperty("headerTypeOverflowed",
                    "Overflowed Header Type",
                    "Indicator for the type of item that overflowed (e.g. file header, or the segment type).",
                    header.getOverflowedHeaderType()));
            set.put(new IntegerProperty("desItemOverflowed",
                    "Data Item Overflowed",
                    "The number of the data item that overflowed (000 for UDHD or XHD types).",
                    header.getItemOverflowed()));
        } else if ("CSATTA DES".equals(desIdentifier)) {
            set.put(new StringProperty("userDefinedSubHeaderFields",
                    "User Defined Subheader Fields",
                    "The user defined subheader fields.",
                    header.getUserDefinedSubheaderField()));
        } else if (header.getUserDefinedSubheaderField() != null) {
            set.put(new StringProperty("userDefinedSubHeaderFields",
                    "User Defined Subheader Fields",
                    "The user defined subheader fields.",
                    header.getUserDefinedSubheaderField()));
        }
        sheet.put(set);
        return sheet;
    }

    final String getFriendlyName() {
        if (!header.getIdentifier().trim().isEmpty()) {
            return header.getIdentifier().trim();
        }
        return "(no name)";
    }

    final String getText() {
        try {
            DeferredSegmentParseStrategy parseStrategy = childKey.getParseStrategy();
            if (header.isTreOverflowNitf21() || header.isTreOverflowNitf20()) {
                // TODO: convert to the raw / heirachical repr.
                StringBuilder sb = new StringBuilder();
                for (String key : header.getTREsFlat().keySet()) {
                    sb.append(key);
                    sb.append(" : ");
                    sb.append(header.getTREsFlat().get(key));
                    sb.append(System.lineSeparator());
                }
                return sb.toString();
            } else if ("XML_DATA_CONTENT".equals(header.getIdentifier().trim())) {
                return new String(parseStrategy.getDataExtensionSegment(childKey.getIndex()).getData());
            } else {
                return "[IMG-48] TODO";
            }
        } catch (ParseException ex) {
            Exceptions.printStackTrace(ex);
        }
        return "[Unable to get DES content]";
    }

    @Override
    public final Action[] getActions(final boolean popup) {
        return combineActions(new DataExtensionSegmentViewAction(this), super.getActions(popup));
    }

    boolean isTreOverflow() {
        return header.isTreOverflowNitf21() || header.isTreOverflowNitf20();
    }

    TreeModel getTreTreeModel() {
        DeferredSegmentParseStrategy parseStrategy = childKey.getParseStrategy();
        return new TreTreeModel(header.getTREsRawStructure());
    }
}
