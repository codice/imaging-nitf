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
package org.codice.imaging.nitf.core.graphic.impl;

import org.codice.imaging.nitf.core.common.FileType;
import org.codice.imaging.nitf.core.graphic.GraphicColour;
import org.codice.imaging.nitf.core.graphic.GraphicSegment;
import org.codice.imaging.nitf.core.security.impl.SecurityMetadataFactory;

/**
 * Factory class for creating new GraphicSegment instances.
 */
public final class GraphicSegmentFactory {

    private GraphicSegmentFactory() {
    }

    /**
     * Create a default NITF graphic segment, without data.
     *
     * Note that this will not set an identifier - it will be empty (space filled on write). That may or may not be
     * valid - it is application dependent.
     *
     * @param fileType the type (version) of NITF file this graphic segment is for
     * @return default graphic segment, containing no graphic data.
     */
    public static GraphicSegment getDefault(final FileType fileType) {
        GraphicSegment graphicSegment = new GraphicSegmentImpl();
        graphicSegment.setFileType(fileType);
        graphicSegment.setIdentifier("");
        graphicSegment.setAttachmentLevel(0);
        graphicSegment.setSecurityMetadata(SecurityMetadataFactory.getDefaultMetadata(fileType));
        graphicSegment.setGraphicColour(GraphicColour.COLOUR);
        return graphicSegment;
    }
}
