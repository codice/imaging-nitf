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

import org.codice.imaging.nitf.core.NitfDataExtensionSegment;
import org.openide.nodes.Children;
import org.openide.nodes.Sheet;

class NitfDataExtensionSegmentNode extends AbstractCommonSegmentNode {

    private final NitfDataExtensionSegment segment;

    public NitfDataExtensionSegmentNode(final NitfDataExtensionSegment nitfDataExtensionSegment) {
        super(Children.LEAF);
        segment = nitfDataExtensionSegment;
        setDisplayName("DES: " + segment.getIdentifier());
    }

   @Override
    protected Sheet createSheet() {
        Sheet sheet = Sheet.createDefault();
        Sheet.Set set = Sheet.createPropertiesSet();
        addCommonSegmentProperties(set, segment);
        sheet.put(set);
        return sheet;
    }
}
