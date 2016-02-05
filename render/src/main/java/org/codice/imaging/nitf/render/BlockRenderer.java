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

import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.stream.ImageInputStream;
import org.codice.imaging.nitf.core.image.ImageSegment;

/**
 * Image renderer that operates block-by-block.
 */
public interface BlockRenderer {

    /**
     * Configure the renderer to support future image rendering.
     *
     * @param imageSegment the image segment to be read
     * @param imageInputStream the source data to be read from
     * @throws IOException if the imageInputStream is not readable
     */
    public void setImageSegment(ImageSegment imageSegment, ImageInputStream imageInputStream) throws IOException;

    /**
     * Render the next available image block.
     *
     * @return image for the specified block
     * @throws java.io.IOException if the data source is not readable
     */
    public BufferedImage getNextImageBlock() throws IOException;

    /**
     * Render a specific image block.
     *
     * If the source data (imageInputStream) is not seekable, this may require blocks to be read in row-major, column-minor order.
     *
     * @param rowIndex the row of the image block to be read
     * @param columnIndex the column of the image block to be read
     * @return image for the specified block
     * @throws java.io.IOException if the data source is not readable
     */
    public BufferedImage getImageBlock(int rowIndex, int columnIndex) throws IOException;
}
