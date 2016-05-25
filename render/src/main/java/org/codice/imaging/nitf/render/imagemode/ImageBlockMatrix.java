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

import java.awt.image.BufferedImage;
import java.util.function.Consumer;
import java.util.function.Supplier;
import org.codice.imaging.nitf.core.image.ImageSegment;

/**
 * The ImageMatrix represents image data stored in a rowcount x columncount matrix.
 */
class ImageBlockMatrix {
    private final ImageBlock[][] blocks;
    private final int matrixWidth;
    private final int matrixHeight;

    /**
     * Constructor.
     *
     * @param imageSegment the Image Segment that this matrix is being created
     * for.
     * @param imageSupplier the underlying buffered image to store the data.
     */
    ImageBlockMatrix(final ImageSegment imageSegment, final Supplier<BufferedImage> imageSupplier) {
        this.matrixWidth = (int) imageSegment.getNumberOfBlocksPerColumn();
        this.matrixHeight = (int) imageSegment.getNumberOfBlocksPerRow();
        int blockWidth = (int) imageSegment.getNumberOfPixelsPerBlockHorizontal();
        int blockHeight = (int) imageSegment.getNumberOfPixelsPerBlockVertical();

        blocks = new ImageBlock[matrixWidth][matrixHeight];

        for (int i = 0; i < this.getMatrixWidth(); i++) {
            for (int j = 0; j < this.getMatrixHeight(); j++) {
                blocks[i][j] = new ImageBlock(i, j, getMatrixWidth(), blockWidth, blockHeight, imageSupplier);
            }
        }
    }

    /**
     * Get a specific image block.
     *
     * @param row - the row to retrieve the ImageBlock from.
     * @param column - the column to retrieve the ImageBlock from.
     * @return - the ImageBlock referenced by (row, column).
     */
    public ImageBlock getImageBlock(final int row, final int column) {
        return blocks[row][column];
    }

    /**
     * Get the matrix width.
     *
     * @return the number of blocks in a row.
     */
    public int getMatrixWidth() {
        return matrixWidth;
    }

    /**
     * Get the matrix height.
     *
     * @return the number of blocks in a column.
     */
    public int getMatrixHeight() {
        return matrixHeight;
    }

    public void forEachBlock(final Consumer<ImageBlock> blockConsumer) {
        for (int i = 0; i < this.getMatrixWidth(); i++) {
            for (int j = 0; j < this.getMatrixHeight(); j++) {
                ImageBlock currentBlock = this.getImageBlock(i, j);
                blockConsumer.accept(currentBlock);
            }
        }
    }
}
