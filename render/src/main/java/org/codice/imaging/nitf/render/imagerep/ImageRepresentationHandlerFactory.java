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

import java.util.HashMap;
import java.util.Map;
import org.codice.imaging.nitf.core.image.ImageSegment;
import org.codice.imaging.nitf.core.image.NitfImageBand;
import static org.codice.imaging.nitf.render.imagerep.ImageRepresentationHandler.NOT_VISIBLE_MAPPED;

public class ImageRepresentationHandlerFactory {
    public static ImageRepresentationHandler forImageSegment(ImageSegment segment) {

        switch (segment.getImageRepresentation()) {
            case RGBTRUECOLOUR: {
                return getRgb24ImageRepresentationHandler(segment);
            }
            case MULTIBAND: {
                return getHandlerForMultiband(segment);
            }
            //add other (more complex) cases here
            default:
                return null;
        }
    }

    private static ImageRepresentationHandler getRgb24ImageRepresentationHandler(ImageSegment segment) {
        Map<Integer, Integer> bandMapping = getRgb24ImageRepresentationMapping(segment);
        return new Rgb24ImageRepresentationHandler(bandMapping);
    }

    private static Map<Integer, Integer> getRgb24ImageRepresentationMapping(final ImageSegment imageSegment) {
        Map<Integer, Integer> mapping = new HashMap<>();
        for (int bandIndex = 0; bandIndex < imageSegment.getNumBands(); bandIndex++) {
            int leftShift;
            switch (imageSegment.getImageBandZeroBase(bandIndex).getImageRepresentation()) {
                case "R":
                    leftShift = 16;
                    break;
                case "G":
                    leftShift = 8;
                    break;
                case "B":
                    leftShift = 0;
                    break;
                default:
                    leftShift = NOT_VISIBLE_MAPPED;
            }
            mapping.put(bandIndex, leftShift);
        }
        return mapping;
    }

    private static ImageRepresentationHandler getHandlerForMultiband(final ImageSegment segment) {
        if (irepbandsHasRgb(segment)) {
            return getRgb24ImageRepresentationHandler(segment);
        }
        // TODO: M case
        // TODO: LU case
        // TODO: no representation
        return null;
    }

    private static boolean irepbandsHasRgb(ImageSegment segment) {
        boolean hasR = false;
        boolean hasG = false;
        boolean hasB = false;
        for (int i = 0; i < segment.getNumBands(); i++) {
            NitfImageBand band = segment.getImageBandZeroBase(i);
            if (null != band.getImageRepresentation()) {
                switch (band.getImageRepresentation()) {
                    case "R":
                        hasR = true;
                        break;
                    case "G":
                        hasG = true;
                    case "B":
                        hasB = true;
                        break;
                }
            }
        }
        return (hasR && hasG && hasB);
    }
}
