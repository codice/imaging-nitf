<<<<<<< HEAD:render/src/main/java/org/codice/imaging/nitf/render/imagehandler/ImageModeHandler.java
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
package org.codice.imaging.nitf.render.imagehandler;
=======
package org.codice.imaging.nitf.render.imagemode;
>>>>>>> Refactored uncompressed rendering pattern:render/src/main/java/org/codice/imaging/nitf/render/imagemode/ImageModeHandler.java

import java.awt.*;
import java.io.IOException;

import javax.imageio.stream.ImageInputStream;

import org.codice.imaging.nitf.core.image.NitfImageSegmentHeader;
import org.codice.imaging.nitf.render.imagerep.ImageRepresentationHandler;

/**
 * An ImageModeHandler abstracts the processing of an ImageInputStream based on the Nitf Image Mode.
 * Pixel-by-pixel rendering is delegated to the supplied ImageRepresentationHandler.
 */
public interface ImageModeHandler {

    /**
     *
     * @param imageSegmentHeader - the NitfImageSegmentHeader for the image being rendered.
     * @param imageInputStream - the ImageInputStream containing the image data.
     * @param targetImage - the Graphic2D that the image will be rendered to.
     * @param imageRepresentationHandler - the ImageRepresentationHandler which will render a single pixel.
     * @throws IOException - propagated from the ImageInputStream.
     */
    void handleImage(NitfImageSegmentHeader imageSegmentHeader, ImageInputStream imageInputStream,
            Graphics2D targetImage, ImageRepresentationHandler imageRepresentationHandler)
            throws IOException;
}
