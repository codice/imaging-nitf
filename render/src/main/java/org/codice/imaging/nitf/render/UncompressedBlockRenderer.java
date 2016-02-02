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
import java.awt.image.DataBufferInt;
import java.awt.image.DataBufferUShort;
import java.awt.image.IndexColorModel;
import java.awt.image.WritableRaster;
import java.io.IOException;
import javax.imageio.stream.ImageInputStream;
import org.codice.imaging.nitf.core.image.ImageCompression;
import org.codice.imaging.nitf.core.image.ImageMode;
import org.codice.imaging.nitf.core.image.ImageSegment;
import org.codice.imaging.nitf.core.image.NitfImageBand;
import org.codice.imaging.nitf.core.image.PixelJustification;
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
            case MONOCHROME:
                return getNextImageBlockMono();
            case RGBLUT:
                return getNextImageBlockRGBLUT();
            case RGBTRUECOLOUR:
                return getNextImageBlockRGBTrueColour();
            case MULTIBAND:
                return getNextImageBlockMultiband();
            default:
                throw new UnsupportedOperationException("Unhandled image representation:" + mImageSegment.getImageRepresentation());
        }
    }

    @Override
    public final BufferedImage getImageBlock(final int rowIndex, final int columnIndex) throws IOException {
        mImageData.seek(getOffsetForBlock(rowIndex, columnIndex) + mMaskOffset);
        return getNextImageBlock();
    }

    private BufferedImage getNextImageBlockMono() throws IOException {
        if ((mImageSegment.getImageMode() != ImageMode.BLOCKINTERLEVE) || (mImageSegment.getNumBands() != 1)) {
            throw new UnsupportedOperationException("Unsupported mode / band combination: " + mImageSegment.getImageMode() + ", " + mImageSegment.getNumBands());
        }
        // TODO: masked image
        switch (mImageSegment.getNumberOfBitsPerPixelPerBand()) {
            case 1:
                return getNextImageBlockMono1();
            case 8:
                return getNextImageBlockMono8();
            case 16:
                return getNextImageBlockMono16();
            default:
                if (mImageSegment.getNumberOfBitsPerPixelPerBand() < 16) {
                    return getNextImageBlockMonoArbitrary();
                } else {
                    throw new UnsupportedOperationException("Unhandled Mono bit depth:" + mImageSegment.getNumberOfBitsPerPixelPerBand());
                }
        }
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

    private BufferedImage getNextImageBlockRGBTrueColour() throws IOException {
        if (mImageSegment.getActualBitsPerPixelPerBand() == 8) {
            return getNextImageBlockRGB24();
        } else {
            throw new UnsupportedOperationException("Unhandled RGBTRUECOLOUR bit depth:" + mImageSegment.getNumberOfBitsPerPixelPerBand());
        }
    }

    private BufferedImage getNextImageBlockMultiband() throws IOException {
        if (mImageSegment.getActualBitsPerPixelPerBand() == 8) {
            return getNextImageBlockMultiband8();
        } else {
            throw new UnsupportedOperationException("Unhandled MULTIBAND bit depth:" + mImageSegment.getNumberOfBitsPerPixelPerBand());
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

    private BufferedImage getNextImageBlockMono1() throws IOException {
        BufferedImage img = new BufferedImage(mImageSegment.getNumberOfPixelsPerBlockHorizontal(),
                                              mImageSegment.getNumberOfPixelsPerBlockVertical(),
                                              BufferedImage.TYPE_BYTE_BINARY);
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

    private BufferedImage getNextImageBlockRGB24() throws IOException {
        BufferedImage img = new BufferedImage(mImageSegment.getNumberOfPixelsPerBlockHorizontal(),
                                              mImageSegment.getNumberOfPixelsPerBlockVertical(),
                                              BufferedImage.TYPE_INT_ARGB);
        int[] data = new int[mImageSegment.getNumberOfPixelsPerBlockHorizontal() * mImageSegment.getNumberOfPixelsPerBlockVertical()];
        if (mImageSegment.getImageMode() == ImageMode.PIXELINTERLEVE) {
            for (int i = 0; i < data.length; ++i) {
                for (int bandIndex = 0; bandIndex < mImageSegment.getNumBands(); ++bandIndex) {
                    data[i] = data[i] | (mImageData.read() << (8 * (2 - bandIndex)));
                }
                if ((mMask == null) || (!mMask.isPadPixel(data[i]))) {
                    data[i] = data[i] | 0xFF000000;
                }
            }
        } else if (mImageSegment.getImageMode() == ImageMode.ROWINTERLEVE) {
            for (int row = 0; row < mImageSegment.getNumberOfPixelsPerBlockVertical(); ++row) {
                for (int bandIndex = 0; bandIndex < mImageSegment.getNumBands(); ++ bandIndex) {
                    for (int column = 0; column < mImageSegment.getNumberOfPixelsPerBlockHorizontal(); ++column) {
                        int i = row * mImageSegment.getNumberOfPixelsPerBlockHorizontal() + column;
                        data[i] = data[i] | (mImageData.read() << (8 * (2 - bandIndex)));
                    }
                }
                for (int column = 0; column < mImageSegment.getNumberOfPixelsPerBlockHorizontal(); ++column) {
                    int i = row * mImageSegment.getNumberOfPixelsPerBlockHorizontal() + column;
                    if ((mMask == null) || (!mMask.isPadPixel(data[i]))) {
                        data[i] = data[i] | 0xFF000000;
                    }
                }
            }
        } else if (mImageSegment.getImageMode() == ImageMode.BLOCKINTERLEVE) {
            for (int bandIndex = 0; bandIndex < mImageSegment.getNumBands(); ++bandIndex) {
                for (int row = 0; row < mImageSegment.getNumberOfPixelsPerBlockVertical(); ++row) {
                    for (int column = 0; column < mImageSegment.getNumberOfPixelsPerBlockHorizontal(); ++column) {
                        int i = row * mImageSegment.getNumberOfPixelsPerBlockHorizontal() + column;
                        data[i] = data[i] | (mImageData.read() << (8 * (2 - bandIndex)));
                    }
                }
            }
            if (mMask != null) {
                for (int pixel = 0; pixel < data.length; ++pixel) {
                    data[pixel] = data[pixel] | 0xFF000000;
                    if (mMask.isPadPixel(data[pixel])) {
                        data[pixel] = 0x00000000;
                    }
                }
            } else {
                for (int pixel = 0; pixel < data.length; ++pixel) {
                    data[pixel] = data[pixel] | 0xFF000000;
                }
            }
        } else {
            throw new UnsupportedOperationException("Unhandled image mode for RGB24:" + mImageSegment.getImageMode());
        }

        int[] imgData = ((DataBufferInt)img.getRaster().getDataBuffer()).getData();
        System.arraycopy(data, 0, imgData, 0, data.length);
        return img;
    }

    private BufferedImage getNextImageBlockMono8() throws IOException {
        BufferedImage img = new BufferedImage(mImageSegment.getNumberOfPixelsPerBlockHorizontal(),
                                              mImageSegment.getNumberOfPixelsPerBlockVertical(),
                                              BufferedImage.TYPE_BYTE_GRAY);
        byte[] imgData = ((DataBufferByte)img.getRaster().getDataBuffer()).getData();
        mImageData.readFully(imgData);
        return img;
    }

    private BufferedImage getNextImageBlockMono16() throws IOException {
        BufferedImage img = new BufferedImage(mImageSegment.getNumberOfPixelsPerBlockHorizontal(),
                                              mImageSegment.getNumberOfPixelsPerBlockVertical(),
                                              BufferedImage.TYPE_USHORT_GRAY);
        short[] imgData = ((DataBufferUShort)img.getRaster().getDataBuffer()).getData();
        if ((mImageSegment.getNumberOfBitsPerPixelPerBand() == mImageSegment.getActualBitsPerPixelPerBand()) || (mImageSegment.getPixelJustification() == PixelJustification.LEFT)) {
            mImageData.readFully(imgData, 0, mImageSegment.getNumberOfPixelsPerBlockHorizontal() * mImageSegment.getNumberOfPixelsPerBlockVertical());
        } else {
            short[] data = new short[mImageSegment.getNumberOfPixelsPerBlockHorizontal() * mImageSegment.getNumberOfPixelsPerBlockVertical()];
            mImageData.readFully(data, 0, mImageSegment.getNumberOfPixelsPerBlockHorizontal() * mImageSegment.getNumberOfPixelsPerBlockVertical());
            for (int i = 0; i < data.length; ++i) {
                data[i] = (short) (data[i] << (mImageSegment.getNumberOfBitsPerPixelPerBand() - mImageSegment.getActualBitsPerPixelPerBand()));
            }
            System.arraycopy(data, 0, imgData, 0, data.length);
        }
        return img;
    }

    /*
        Allow rendering for any bit depth (up to 16 bit max), probably slow reading
    */
    private BufferedImage getNextImageBlockMonoArbitrary() throws IOException {
        BufferedImage img = new BufferedImage(mImageSegment.getNumberOfPixelsPerBlockHorizontal(),
                                              mImageSegment.getNumberOfPixelsPerBlockVertical(),
                                              BufferedImage.TYPE_USHORT_GRAY);

        short[] data = new short[mImageSegment.getNumberOfPixelsPerBlockHorizontal() * mImageSegment.getNumberOfPixelsPerBlockVertical()];
        for (int i = 0; i < data.length; ++i) {
            int pixelRawValue = (int)mImageData.readBits(mImageSegment.getNumberOfBitsPerPixelPerBand());
            if ((mImageSegment.getNumberOfBitsPerPixelPerBand() == mImageSegment.getActualBitsPerPixelPerBand()) || (mImageSegment.getPixelJustification() == PixelJustification.LEFT)) {
                data[i] = (short) (pixelRawValue << (img.getColorModel().getPixelSize() - mImageSegment.getNumberOfBitsPerPixelPerBand()));
            } else {
                data[i] = (short) (pixelRawValue << (img.getColorModel().getPixelSize() - mImageSegment.getActualBitsPerPixelPerBand()));
            }
        }

        short[] imgData = ((DataBufferUShort)img.getRaster().getDataBuffer()).getData();
        System.arraycopy(data, 0, imgData, 0, data.length);
        return img;
    }

    private BufferedImage getNextImageBlockMultiband8() throws IOException {
        // TODO: we need to determine the real representation first
        BufferedImage img = new BufferedImage(mImageSegment.getNumberOfPixelsPerBlockHorizontal(),
                                              mImageSegment.getNumberOfPixelsPerBlockVertical(),
                                              BufferedImage.TYPE_INT_ARGB);
        int[] data = new int[mImageSegment.getNumberOfPixelsPerBlockHorizontal() * mImageSegment.getNumberOfPixelsPerBlockVertical()];
        if (mImageSegment.getImageMode() == ImageMode.PIXELINTERLEVE) {
            for (int i = 0; i < data.length; ++i) {
                data[i] = 0;
                for (int bandIndex = 0; bandIndex < mImageSegment.getNumBands(); ++bandIndex) {
                    NitfImageBand band = mImageSegment.getImageBandZeroBase(bandIndex);
                    switch (mImageSegment.getImageBandZeroBase(bandIndex).getImageRepresentation()) {
                        case "R":
                            data[i] = data[i] | (mImageData.read() << 16);
                            break;
                        case "G":
                            data[i] = data[i] | (mImageData.read() << 8);
                            break;
                        case "B":
                            data[i] = data[i] | mImageData.read();
                            break;
                        case "":
                            mImageData.read();
                            break;
                        default:
                            throw new UnsupportedOperationException("unhandled image representation: " + mImageSegment.getImageBandZeroBase(bandIndex).getImageRepresentation());
                    }
                }
                if ((mMask == null) || (!mMask.isPadPixel(data[i]))) {
                    data[i] = data[i] | 0xFF000000;
                }
            }
        } else if (mImageSegment.getImageMode() == ImageMode.ROWINTERLEVE) {
            for (int row = 0; row < mImageSegment.getNumberOfPixelsPerBlockVertical(); ++row) {
                int rowStart = row * mImageSegment.getNumberOfPixelsPerBlockHorizontal();
                for (int bandIndex = 0; bandIndex < mImageSegment.getNumBands(); ++ bandIndex) {
                    switch (mImageSegment.getImageBandZeroBase(bandIndex).getImageRepresentation()) {
                        case "R":
                            for (int column = 0; column < mImageSegment.getNumberOfPixelsPerBlockHorizontal(); ++column) {
                                data[rowStart + column] = data[rowStart + column] | (mImageData.read() << 16);
                            }
                            break;
                        case "G":
                            for (int column = 0; column < mImageSegment.getNumberOfPixelsPerBlockHorizontal(); ++column) {
                                data[rowStart + column] = data[rowStart + column] | (mImageData.read() << 8);
                            }
                            break;
                        case "B":
                            for (int column = 0; column < mImageSegment.getNumberOfPixelsPerBlockHorizontal(); ++column) {
                                data[rowStart + column] = data[rowStart + column] | mImageData.read();
                            }
                            break;
                        case "":
                            mImageData.skipBytes(mImageSegment.getNumberOfPixelsPerBlockHorizontal());
                            break;
                        default:
                            throw new UnsupportedOperationException("unhandled image representation: " + mImageSegment.getImageBandZeroBase(bandIndex).getImageRepresentation());
                    }

                }
                for (int column = 0; column < mImageSegment.getNumberOfPixelsPerBlockHorizontal(); ++column) {
                    int i = row * mImageSegment.getNumberOfPixelsPerBlockHorizontal() + column;
                    if ((mMask == null) || (!mMask.isPadPixel(data[i]))) {
                        data[i] = data[i] | 0xFF000000;
                    }
                }
            }
        } else if (mImageSegment.getImageMode() == ImageMode.BLOCKINTERLEVE) {
            for (int bandIndex = 0; bandIndex < mImageSegment.getNumBands(); ++ bandIndex) {
                switch (mImageSegment.getImageBandZeroBase(bandIndex).getImageRepresentation()) {
                    case "R":
                        for (int row = 0; row < mImageSegment.getNumberOfPixelsPerBlockVertical(); ++row) {
                            int rowStart = row * mImageSegment.getNumberOfPixelsPerBlockHorizontal();
                            for (int column = 0; column < mImageSegment.getNumberOfPixelsPerBlockHorizontal(); ++column) {
                                data[rowStart + column] = data[rowStart + column] | (mImageData.read() << 16);
                            }
                        }
                        break;
                    case "G":
                        for (int row = 0; row < mImageSegment.getNumberOfPixelsPerBlockVertical(); ++row) {
                            int rowStart = row * mImageSegment.getNumberOfPixelsPerBlockHorizontal();
                            for (int column = 0; column < mImageSegment.getNumberOfPixelsPerBlockHorizontal(); ++column) {
                                data[rowStart + column] = data[rowStart + column] | (mImageData.read() << 8);
                            }
                        }
                        break;
                    case "B":
                        for (int row = 0; row < mImageSegment.getNumberOfPixelsPerBlockVertical(); ++row) {
                            int rowStart = row * mImageSegment.getNumberOfPixelsPerBlockHorizontal();
                            for (int column = 0; column < mImageSegment.getNumberOfPixelsPerBlockHorizontal(); ++column) {
                                data[rowStart + column] = data[rowStart + column] | mImageData.read();
                            }
                        }
                        break;
                    case "":
                        mImageData.skipBytes(mImageSegment.getNumberOfPixelsPerBlockHorizontal() * mImageSegment.getNumberOfPixelsPerBlockVertical());
                        break;
                    default:
                        throw new UnsupportedOperationException("unhandled image representation: " + mImageSegment.getImageBandZeroBase(bandIndex).getImageRepresentation());
                    }
                }
            for (int row = 0; row < mImageSegment.getNumberOfPixelsPerBlockVertical(); ++row) {
                for (int column = 0; column < mImageSegment.getNumberOfPixelsPerBlockHorizontal(); ++column) {
                    int i = row * mImageSegment.getNumberOfPixelsPerBlockHorizontal() + column;
                    if ((mMask == null) || (!mMask.isPadPixel(data[i]))) {
                        data[i] = data[i] | 0xFF000000;
                    }
                }
            }
        } else {
            throw new UnsupportedOperationException("Unhandled image mode for MULTIBAND:" + mImageSegment.getImageMode());
        }
        int[] imgData = ((DataBufferInt)img.getRaster().getDataBuffer()).getData();
        System.arraycopy(data, 0, imgData, 0, data.length);
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
