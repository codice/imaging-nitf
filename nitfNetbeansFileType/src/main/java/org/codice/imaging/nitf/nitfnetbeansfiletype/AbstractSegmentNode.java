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

import org.codice.imaging.nitf.core.common.CommonBasicSegment;
import org.openide.nodes.Children;
import org.openide.nodes.Sheet;

abstract class AbstractSegmentNode extends AbstractCommonSegmentNode {
    protected static final String POINT_FORMATTER = "[%d, %d]";

    public AbstractSegmentNode(final Children children) {
        super(children);
    }

    protected void addSubSegmentProperties(final Sheet.Set set, final CommonBasicSegment segment) {
        addCommonSegmentProperties(set, segment);
        set.put(new IntegerProperty("attachmentLevel",
                "Attachment Level",
                "The attachment level for this segment.",
                segment.getAttachmentLevel()));
    }

}
