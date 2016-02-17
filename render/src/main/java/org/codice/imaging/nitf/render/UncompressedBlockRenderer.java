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
import java.awt.image.DataBufferByte;
import java.awt.image.IndexColorModel;
import java.awt.image.WritableRaster;
import java.io.IOException;
import javax.imageio.stream.ImageInputStream;
import org.codice.imaging.nitf.core.image.ImageCompression;
import org.codice.imaging.nitf.core.image.ImageMode;
import org.codice.imaging.nitf.core.image.ImageSegment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class UncompressedBlockRenderer implements BlockRenderer {

    private static final Logger LOGGER = LoggerFactory.getLogger(UncompressedBlockRenderer.class);
    private ImageSegment mImageSegment = null;
    private ImageInputStream mImageData = null;
    private ImageMask mMask = null;
    private long mMaskOffset = 0;


    /**
     * Set the image segment to read from
     *
     * @param imageSegment the image segment
     * @param imageInputStream the source to read the segment image data from
     * @throws IOException if the read fails
     */
    @Override
    public final void setImageSegment(ImageSegment imageSegment, ImageInputStream imageInputStream) throws IOException {
        mImageSegment = imageSegment;
        mImageData = imageInputStream;
        if (mImageSegment.getImageCompression() == ImageCompression.NOTCOMPRESSEDMASK) {
            mMask = new ImageMask(mImageSegment, mImageData);
            mMaskOffset = mImageData.getStreamPosition();
        } else {
            // We don't have a real mask, but we can create our own for an uncompressed image
            mMask = buildUncompressedImageMask();
        }
    }

    @Override
    public final BufferedImage getNextImageBlock() throws IOException {
        switch (mImageSegment.getImageRepresentation()) {
            case RGBLUT:
                return getNextImageBlockRGBLUT();
            default:
                throw new UnsupportedOperationException("Unhandled image representation:" + mImageSegment.getImageRepresentation());
        }
    }

    @Override
    public final BufferedImage getImageBlock(final int rowIndex, final int columnIndex) throws IOException {
        mImageData.seek(getOffsetForBlock(rowIndex, columnIndex) + mMaskOffset);
        return getNextImageBlock();
    }

    private BufferedImage getNextImageBlockRGBLUT() throws IOException {
        if ((mImageSegment.getImageMode() != ImageMode.BLOCKINTERLEVE) || (mImageSegment.getNumBands() != 1)) {
            throw new UnsupportedOperationException("Unsupported mode / band combination: " + mImageSegment.getImageMode() + ", " + mImageSegment.getNumBands());
        }
        // TODO: masked image
        switch (mImageSegment.getNumberOfBitsPerPixelPerBand()) {
            case 1:
                return getNextImageBlockRGBLUT1();
            case 8:
                return getNextImageBlockRGBLUT8();
            default:
                throw new UnsupportedOperationException("Unhandled RGBLUT bit depth:" + mImageSegment.getNumberOfBitsPerPixelPerBand());
        }
    }

    private BufferedImage getNextImageBlockRGBLUT1() throws IOException {
        IndexColorModel colourModel = new IndexColorModel(mImageSegment.getActualBitsPerPixelPerBand(),
                                                          mImageSegment.getImageBandZeroBase(0).getNumLUTEntries(),
                                                          mImageSegment.getImageBandZeroBase(0).getLUTZeroBase(0).getEntries(),
                                                          mImageSegment.getImageBandZeroBase(0).getLUTZeroBase(1).getEntries(),
                                                          mImageSegment.getImageBandZeroBase(0).getLUTZeroBase(2).getEntries());
        BufferedImage img = new BufferedImage(mImageSegment.getNumberOfPixelsPerBlockHorizontal(),
                                              mImageSegment.getNumberOfPixelsPerBlockVertical(),
                                              BufferedImage.TYPE_BYTE_BINARY, colourModel);
        copy1BitValuesIntoImage(img);
        return img;
    }

    private void copy1BitValuesIntoImage(final BufferedImage img) throws IOException {
        WritableRaster raster = img.getRaster();
        for (int rowIndex = 0; rowIndex < mImageSegment.getNumberOfPixelsPerBlockVertical(); ++rowIndex) {
            for (int columnIndex = 0; columnIndex < mImageSegment.getNumberOfPixelsPerBlockHorizontal(); ++columnIndex) {
                raster.setSample(columnIndex, rowIndex, 0, (int)mImageData.readBits(1));
            }
        }
    }

    private BufferedImage getNextImageBlockRGBLUT8() throws IOException {
        IndexColorModel colourModel = new IndexColorModel(mImageSegment.getActualBitsPerPixelPerBand(),
                                                          mImageSegment.getImageBandZeroBase(0).getNumLUTEntries(),
                                                          mImageSegment.getImageBandZeroBase(0).getLUTZeroBase(0).getEntries(),
                                                          mImageSegment.getImageBandZeroBase(0).getLUTZeroBase(1).getEntries(),
                                                          mImageSegment.getImageBandZeroBase(0).getLUTZeroBase(2).getEntries());
        BufferedImage img = new BufferedImage(mImageSegment.getNumberOfPixelsPerBlockHorizontal(),
                                              mImageSegment.getNumberOfPixelsPerBlockVertical(),
                                              BufferedImage.TYPE_BYTE_INDEXED, colourModel);
        byte[] imgData = ((DataBufferByte)img.getRaster().getDataBuffer()).getData();
        mImageData.readFully(imgData);
        return img;
    }


    private long getOffsetForBlock(int rowIndex, int columnIndex) {
        // TODO: this should handle mask blocks.
        int numberOfBlocks = rowIndex * mImageSegment.getNumberOfBlocksPerRow() + columnIndex;
        if (mMask.isMaskedBlock(numberOfBlocks, 0)) {
            LOGGER.debug("Some masked block");
        }
        return numberOfBlocks * mImageSegment.getNumberOfBytesPerBlock();
    }

    private ImageMask buildUncompressedImageMask() {
        return new ImageMask(mImageSegment);
    }
}
