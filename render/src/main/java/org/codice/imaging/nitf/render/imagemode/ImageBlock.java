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
package org.codice.imaging.nitf.render.imagemode;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.util.function.Supplier;

/**
 * An ImageBlock represents a single block of a larger image.
 */
public class ImageBlock {
    private int row;
    private int column;
    private int width;
    private int height;
    private Supplier<BufferedImage> imageSupplier;
    private BufferedImage blockImage;

    /**
     *
     * @param row - the row position of this ImageBlock in the larger image.
     * @param column - the column position of this ImageBlock in the larger image.
     */
    public ImageBlock(int row, int column, int width, int height,
            Supplier<BufferedImage> imageSupplier) {
        this.row = row;
        this.column = column;
        this.width = width;
        this.height = height;
        this.imageSupplier = imageSupplier;
    }

    /**
     *
     * @return the DataBuffer that contains the data for this ImageBlock.
     */
    public DataBuffer getDataBuffer() {
        if (blockImage == null) {
            blockImage = imageSupplier.get();
        }

        return blockImage.getRaster().getDataBuffer();
    }

    public void render(Graphics2D targetImage, boolean disposeAfterRender) {
        targetImage.drawImage(blockImage, this.column * this.height, this.row * this.width, null);

        if (disposeAfterRender) {
            this.blockImage = null;
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

}
