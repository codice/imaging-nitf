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

package org.codice.imaging.nitf.render;

import java.io.IOException;

import java.awt.image.BufferedImage;
import javax.imageio.stream.ImageInputStream;

import org.codice.imaging.nitf.core.image.NitfImageSegmentHeader;

public interface BlockRenderer {

    public void setImageSegment(NitfImageSegmentHeader imageSegmentHeader, ImageInputStream imageInputStream) throws IOException;

    public BufferedImage getNextImageBlock() throws IOException;

    /**
     * Render a specific image block.
     *
     * If the source data (imageInputStream) is not seekable, this may require blocks to be read in row-major, column-minor order.
     *
     * @param rowIndex the row of the image block to be read
     * @param columnIndex the column of the image block to be read
     * @return image for the specified block
     */
    public BufferedImage getImageBlock(int rowIndex, int columnIndex) throws IOException;
}
