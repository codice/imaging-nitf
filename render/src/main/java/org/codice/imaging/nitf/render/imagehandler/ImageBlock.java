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

import java.nio.IntBuffer;

/**
 * An ImageBlock represents a single block of a larger image.
 */
class ImageBlock {
    private int row;
    private int column;
    private IntBuffer data;

    /**
     *
     * @param row - the row position of this ImageBlock in the larger image.
     * @param column - the column position of this ImageBlock in the larger image.
     * @param blockSize - the size of this block.
     */
    public ImageBlock(int row, int column, int blockSize) {
        this.row = row;
        this.column = column;
        this.data = IntBuffer.allocate(blockSize);
    }

    /**
     *
     * @return the row position of this ImageBlock in the larger image.
     */
    public int getRow() {
        return row;
    }

    /**
     *
     * @return the column position of this ImageBlock in the larger image.
     */
    public int getColumn() {
        return column;
    }

    /**
     *
     * @return the IntBuffer that contains the data for this ImageBlock.
     */
    public IntBuffer getData() {
        return data;
    }
}
