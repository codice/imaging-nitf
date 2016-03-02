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

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.util.function.Supplier;

/**
 * An ImageBlock represents a single block of a larger image.
 */
class ImageBlock {

    private final int row;
    private final int column;
    private final int numColumns;
    private final int width;
    private final int height;
    private final Supplier<BufferedImage> imageSupplier;
    private BufferedImage blockImage;

    /**
     *
     * @param row - the row position of this ImageBlock in the larger image.
     * @param column - the column position of this ImageBlock in the larger image.
     * @param numColumns the number of columns in the ImageBlock.
     */
    public ImageBlock(int row, int column, int numColumns, int width, int height,
            Supplier<BufferedImage> imageSupplier) {
        this.row = row;
        this.column = column;
        this.numColumns = numColumns;
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

    /**
     * Get the block index in standard rendering order.
     *
     * This is a zero-based index for the position of this block if the blocks were "linearised", such that the first
     * block (column 0) of each row appears at the end of the last block of the previous row. As an example, if you have
     * 3 rows of 4 columns, this will be a number between 0 and 11, where the first row has 0 to 3, the second row 4 to
     * 7, and the third row 8 to 11.
     *
     * @return block index.
     */
    public int getBlockIndex() {
        return (this.row * this.numColumns) + this.column;
    }
}
