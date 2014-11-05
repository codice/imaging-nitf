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

import org.codice.imaging.nitf.core.NitfDataExtensionSegmentHeader;
import org.openide.nodes.Children;
import org.openide.nodes.Sheet;

class NitfDataExtensionSegmentNode extends AbstractCommonSegmentNode {

    private final NitfDataExtensionSegmentHeader segment;

    public NitfDataExtensionSegmentNode(final NitfDataExtensionSegmentHeader nitfDataExtensionSegment) {
        super(Children.LEAF);
        segment = nitfDataExtensionSegment;
        setDisplayName("DES: " + segment.getIdentifier());
    }

   @Override
    protected Sheet createSheet() {
        Sheet sheet = Sheet.createDefault();
        Sheet.Set set = Sheet.createPropertiesSet();
        addCommonSegmentProperties(set, segment);
        set.put(new IntegerProperty("desVersion",
                "DES Version",
                "Version number of the Data Extension Segment type.",
                segment.getDESVersion()));
        String desIdentifier = segment.getIdentifier().trim();
        if (("TRE_OVERFLOW".equals(desIdentifier))
            || ("Registered Extensions".equals(desIdentifier))
            || ("Controlled Extensions".equals(desIdentifier))) {
            set.put(new StringProperty("headerTypeOverflowed",
                    "Overflowed Header Type",
                    "Indicator for the type of item that overflowed (e.g. file header, or the segment type).",
                    segment.getOverflowedHeaderType()));
            set.put(new IntegerProperty("desItemOverflowed",
                    "Data Item Overflowed",
                    "The number of the data item that overflowed (000 for UDHD or XHD types).",
                    segment.getItemOverflowed()));
        }
        sheet.put(set);
        return sheet;
    }
}
