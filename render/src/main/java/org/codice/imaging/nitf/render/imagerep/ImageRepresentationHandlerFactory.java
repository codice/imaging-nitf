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
package org.codice.imaging.nitf.render.imagerep;

import org.codice.imaging.nitf.core.image.NitfImageSegmentHeader;

public class ImageRepresentationHandlerFactory {
    public static ImageRepresentationHandler forNitfImageSegment(NitfImageSegmentHeader header) {
        switch (header.getImageRepresentation()) {
        case RGBTRUECOLOUR: {
            return new Rgb24ImageRepresentationHandler();
        }
        //add other (more complex) cases here
        default:
            return null;
        }
    }
}

