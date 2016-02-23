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
import java.util.ArrayList;
import java.util.List;
import javax.imageio.stream.ImageInputStream;
import org.codice.imaging.nitf.core.image.ImageMode;
import org.codice.imaging.nitf.core.image.ImageSegment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ImageMask {
    private static final Logger LOGGER = LoggerFactory.getLogger(ImageMask.class);

    private ImageSegment mImageSegment = null;

    private int[][] bmrnbndm = null;
    private final List<Integer> tmrnbndm = new ArrayList<>();
    int tpxcd = -1;

    private static final int BLOCK_NOT_RECORDED = 0xFFFFFFFF;

    /**
     * Create an image mask based on reading from an image segment and associated stream.
     *
     * @param imageSegment the image segment
     * @param imageStream the associated image stream (data)
     * @throws IOException if image mask parsing fails
     */
    public ImageMask(final ImageSegment imageSegment, final ImageInputStream imageStream) throws IOException {
        mImageSegment = imageSegment;
        readImageMask(imageStream);
    }

    /**
     * Create an image mask based on a regular number of bytes per block.
     *
     * This is essentially a way to build an image mask for images that don't have one, but where the block layout is
     * predictable (i.e. there is no compression).
     *
     * @param imageSegment the image segment that specifies the image characteristics
     */
    public ImageMask(final ImageSegment imageSegment) {
        mImageSegment = imageSegment;
        bmrnbndm = new int[mImageSegment.getNumberOfBlocksPerRow() * mImageSegment.getNumberOfBlocksPerColumn()][mImageSegment.getNumBands()];
        int numBandsToRead = 1;
        if (mImageSegment.getImageMode() == ImageMode.BANDSEQUENTIAL) {
            numBandsToRead = mImageSegment.getNumBands();
        }
        int bytesPerPixel = (int)mImageSegment.getNumberOfBytesPerBlock();
        int blockCounter = 0;
        for (int m = 0; m < numBandsToRead; ++m) {
            for (int n = 0; n < mImageSegment.getNumberOfBlocksPerRow() * mImageSegment.getNumberOfBlocksPerColumn(); ++n) {
                bmrnbndm[n][m] = bytesPerPixel * blockCounter;
                blockCounter++;
            }
        }
    }

    private void readImageMask(final ImageInputStream imageInputStream) throws IOException {
        int imdatoff = imageInputStream.readInt();
        int bmrlnth = imageInputStream.readShort();
        int tmrlnth = imageInputStream.readShort();
        int tpxcdlnth = imageInputStream.readShort();
        LOGGER.debug(String.format("Blocked image data offset: 0x%08x", imdatoff));
        LOGGER.debug(String.format("Block mask record length: 0x%04x", bmrlnth));
        LOGGER.debug(String.format("Pad Pixel Mask Record Length: 0x%04x", tmrlnth));
        LOGGER.debug(String.format("Pad Output pixel code length: 0x%04x", tpxcdlnth));
        if (tpxcdlnth > 0) {
            tpxcd = 0;
            int numBytesToRead = (tpxcdlnth + 7) / 8;
            LOGGER.debug("Reading TPXCD at length:" + numBytesToRead);
            int bandBits = (int)imageInputStream.readBits(numBytesToRead * 8);
            for (int i = 0; i < mImageSegment.getNumBands(); ++i) {
                tpxcd = tpxcd | (bandBits << (8*i));
            }
            LOGGER.debug(String.format("Pad Output pixel code : 0x%08x", tpxcd));
        }
        int numBandsToRead = 1;
        if (mImageSegment.getImageMode() == ImageMode.BANDSEQUENTIAL) {
            numBandsToRead = mImageSegment.getNumBands();
        }
        if (bmrlnth > 0) {
            bmrnbndm = new int[mImageSegment.getNumberOfBlocksPerRow() * mImageSegment.getNumberOfBlocksPerColumn()][mImageSegment.getNumBands()];
            for (int m = 0; m < numBandsToRead; ++m) {
                for (int n = 0; n < mImageSegment.getNumberOfBlocksPerRow() * mImageSegment.getNumberOfBlocksPerColumn(); ++n) {
                    bmrnbndm[n][m] = imageInputStream.readInt();
                    LOGGER.debug(String.format("mask blocks (band %d) %d: 0x%08x", m, n, bmrnbndm[n][m]));
                }
            }
        }
        if (tmrlnth > 0) {
            for (int m = 0; m < numBandsToRead; ++m) {
                for (int n = 0; n < mImageSegment.getNumberOfBlocksPerRow() * mImageSegment.getNumberOfBlocksPerColumn(); ++n) {
                    int val = imageInputStream.readInt();
                    tmrnbndm.add(val);
                    LOGGER.debug(String.format("mask pixel (band %d) %d: 0x%08x", m, n, val));
                }
            }
        }
    }

    public boolean isMaskedBlock(int blockNumber, int bandNumber) {
        if (bmrnbndm == null) {
            return false;
        }

        if (blockNumber >= bmrnbndm.length || bandNumber >= bmrnbndm[blockNumber].length) {
            return false;
        }

        return (BLOCK_NOT_RECORDED == bmrnbndm[blockNumber][bandNumber]);
    }

    public boolean isPadPixel(int value) {
        if (tpxcd == -1) {
            return false;
        }
        return (tpxcd == value);
    }

}
