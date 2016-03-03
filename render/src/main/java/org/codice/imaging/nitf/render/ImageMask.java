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

/**
 * Representation of the image mask.
 *
 * In NITF, there are two distinct image mask aspects - the concept that a block
 * might be missing, and the concept that a pixel value might indicate "no
 * data".
 *
 * Image masking is similar for the block, pixel and row interleve image modes.
 * Image masking for band-sequential images is handled slightly differently.
 */
public final class ImageMask {
    private static final Logger LOGGER = LoggerFactory.getLogger(ImageMask.class);

    private ImageSegment mImageSegment = null;

    private int[][] bmrnbndm = null;
    private final List<Integer> tmrnbndm = new ArrayList<>();
    private int tpxcd = -1;

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

    /**
     * Test if the specified block is not actually recorded in the file.
     *
     * This is always false if the image compression is not a masked variant.
     *
     * Image blocks are counted in row-major, column-minor order (e.g. if you
     * have three blocks across, and two blocks down, the block numbers are 0,
     * 1, 2 for the first row, and then 3, 4, 5 for the second row).
     *
     * @param blockNumber the block number to check for masking.
     * @param bandNumber the band to check for masking (only used for Band
     * Sequential).
     * @return true if the block is masked (not recorded in the file), otherwise
     * false.
     */
    public boolean isMaskedBlock(int blockNumber, int bandNumber) {
        if (bmrnbndm == null) {
            return false;
        }

        if (blockNumber >= bmrnbndm.length || bandNumber >= bmrnbndm[blockNumber].length) {
            return false;
        }

        return (BLOCK_NOT_RECORDED == bmrnbndm[blockNumber][bandNumber]);
    }

    /**
     * Test if the specified pixel value indicates "no data".
     *
     * Pad pixels are typically rendered transparent, and should not be included
     * in image calculations (e.g. statistics, averaging, exploitation).
     *
     * @param value the pixel value to test.
     * @return true if this is a pad ("no data") pixel value, otherwise false.
     */
    public boolean isPadPixel(int value) {
        if (tpxcd == -1) {
            return false;
        }
        return (tpxcd == value);
    }

    /**
     * Check whether this image mask has valid per-pixel masking.
     *
     * If this is false, isPadPixel() will always return false.
     *
     * @return true if there is valid per-pixel masking, otherwise false
     */
    public boolean hasPixelMasks() {
        return (tpxcd != -1);
    }

}
