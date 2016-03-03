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
 *
 * The concept is that the ImageBlock provides access to part of an underlying
 * (supplied) image. The ImageBlock is effectively a window into an existing
 * BufferedImage.
 */
class ImageBlock {

    private final int blockRowIndex;
    private final int blockColumnIndex;
    private final int blocksInOneRow;
    private final int blockWidth;
    private final int blockHeight;
    private final Supplier<BufferedImage> supplier;
    private BufferedImage blockImage;

    /**
     * Constructor.
     *
     * @param row the row position in blocks of this ImageBlock in the larger
     * image.
     * @param column the column position in blocks of this ImageBlock in the
     * larger image.
     * @param numColumns the number of columns in the larger image.
     * @param width the width in pixels of this ImageBlock.
     * @param height the height in pixels of this ImageBlock.
     * @param imageSupplier the underlying image source.
     */
    ImageBlock(final int row, final int column, final int numColumns, final int width, final int height,
            final Supplier<BufferedImage> imageSupplier) {
        this.blockRowIndex = row;
        this.blockColumnIndex = column;
        this.blocksInOneRow = numColumns;
        this.blockWidth = width;
        this.blockHeight = height;
        this.supplier = imageSupplier;
    }

    /**
     * Get the data buffer for this block.
     *
     * @return the DataBuffer that contains the data for this ImageBlock.
     */
    public DataBuffer getDataBuffer() {
        if (blockImage == null) {
            blockImage = supplier.get();
        }

        return blockImage.getRaster().getDataBuffer();
    }

    /**
     * Render this image block into a target image.
     *
     * @param targetImage the image to draw into.
     * @param disposeAfterRender set to true if this block should be disposed of
     * after the rendering is complete.
     */
    public void render(final Graphics2D targetImage, final boolean disposeAfterRender) {
        targetImage.drawImage(blockImage, this.blockColumnIndex * this.blockHeight, this.blockRowIndex * this.blockWidth, null);

        if (disposeAfterRender) {
            this.blockImage = null;
        }
    }

    /**
     * Get the width of this image block.
     *
     * @return the width (x dimension) of the block in pixels.
     */
    public int getWidth() {
        return blockWidth;
    }

    /**
     * Get the height of this image block.
     *
     * @return the height (y dimension) of the block in pixels.
     */
    public int getHeight() {
        return blockHeight;
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
        return (this.blockRowIndex * this.blocksInOneRow) + this.blockColumnIndex;
    }
}
