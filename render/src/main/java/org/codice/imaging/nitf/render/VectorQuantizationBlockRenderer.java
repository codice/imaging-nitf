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
import java.awt.image.IndexColorModel;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.stream.ImageInputStream;
import org.codice.imaging.nitf.core.image.ImageCompression;
import org.codice.imaging.nitf.core.image.ImageRepresentation;
import org.codice.imaging.nitf.core.image.ImageSegment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class VectorQuantizationBlockRenderer implements BlockRenderer {

    private static final Logger LOGGER = LoggerFactory.getLogger(VectorQuantizationBlockRenderer.class);
    private ImageSegment mImageSegment = null;
    private ImageInputStream mImageData = null;
    private ImageMask mMask = null;

    private int mNumberOfImageRows = 0;
    private int mNumberOfImageCodesPerRow = 0;
    private int mImageCodeBitLength = 0;
    private int mCompressionAlgorithmId = 0;
    private int mNumberOfCompressionLookupOffsetRecords = 0;
    private int mNumberOfCompressionParameterOffsetRecords = 0;
    private int mCompressionLookupOffsetTableOffset = 0;
    private int mCompressionLookupTableOffsetRecordLength = 0;

    private int nextImageBlockToRead = 0;

    private final List<VQCompressionLookupOffsetRecord> mCompressionLookupOffsetRecords = new ArrayList<>();

    private int[][][] mCodebook;

    @Override
    public final void setImageSegment(final ImageSegment imageSegment, final ImageInputStream imageInputStream) throws IOException {
        mImageSegment = imageSegment;
        mImageData = imageInputStream;

        if (mImageSegment.getImageCompression() == ImageCompression.VECTORQUANTIZATIONMASK) {
            mMask = new ImageMask(mImageSegment, mImageData);
        }
        readVQHeader();
    }

    @Override
    public final BufferedImage getNextImageBlock() throws IOException {
        if ((mImageSegment.getActualBitsPerPixelPerBand() == Byte.SIZE)
                && (mImageSegment.getNumberOfBitsPerPixelPerBand() == Byte.SIZE)) {
            if (mImageSegment.getImageRepresentation() == ImageRepresentation.MONOCHROME) {
                if ((mMask != null) && (mMask.isMaskedBlock(nextImageBlockToRead++, 0))) {
                   return null;
                }
                return getNextImageBlockMono8();
            } else if (mImageSegment.getImageRepresentation() == ImageRepresentation.RGBLUT) {
                if ((mMask != null) && (mMask.isMaskedBlock(nextImageBlockToRead++, 0))) {
                    return null;
                }
                return getNextImageBlockRgbLut8();
            } else {
                throw new UnsupportedOperationException("Unhandled image representation:" + mImageSegment.getImageRepresentation());
            }
        } else {
            throw new UnsupportedOperationException("Unhandled BitsPerPixelPerBand: "
                    + mImageSegment.getActualBitsPerPixelPerBand() + " / " + mImageSegment.getNumberOfBitsPerPixelPerBand());
        }
    }

    @Override
    public final BufferedImage getImageBlock(final int rowIndex, final int columnIndex) throws IOException {
        // TODO: seek to image block location
        return getNextImageBlock();
    }

    private BufferedImage getNextImageBlockMono8() throws IOException {
        BufferedImage img = new BufferedImage((int) mImageSegment.getNumberOfPixelsPerBlockHorizontal(),
                (int) mImageSegment.getNumberOfPixelsPerBlockVertical(), BufferedImage.TYPE_BYTE_GRAY);
        renderToImage(img);
        return img;
    }

    private BufferedImage getNextImageBlockRgbLut8() throws IOException {
        IndexColorModel colourModel = new IndexColorModel(mImageSegment.getActualBitsPerPixelPerBand(),
                                                          mImageSegment.getImageBandZeroBase(0).getNumLUTEntries(),
                                                          mImageSegment.getImageBandZeroBase(0).getLUTZeroBase(0).getEntries(),
                                                          mImageSegment.getImageBandZeroBase(0).getLUTZeroBase(1).getEntries(),
                                                          mImageSegment.getImageBandZeroBase(0).getLUTZeroBase(2).getEntries());
        BufferedImage img = new BufferedImage((int) mImageSegment.getNumberOfColumns(),
                (int) mImageSegment.getNumberOfRows(), BufferedImage.TYPE_BYTE_INDEXED, colourModel);
        renderToImage(img);
        return img;
    }

    private void renderToImage(final BufferedImage img) throws IOException {
        WritableRaster imgRaster = img.getRaster();
        for (int r = 0; r < mNumberOfImageRows; ++r) {
            for (int c = 0; c < mNumberOfImageCodesPerRow; ++c) {
                int codebookEntry = (int) mImageData.readBits(mImageCodeBitLength);
                for (int tableIndex = 0; tableIndex < mNumberOfCompressionLookupOffsetRecords; ++tableIndex) {
                    VQCompressionLookupOffsetRecord record = mCompressionLookupOffsetRecords.get(tableIndex);
                    int[] codebookValues = mCodebook[tableIndex][codebookEntry];
                    imgRaster.setPixels(c * mNumberOfCompressionLookupOffsetRecords,
                            (r * record.getNumberOfValuesPerCompressionLookupRecord()) + tableIndex,
                            mNumberOfCompressionLookupOffsetRecords,
                            1,
                            codebookValues);
                }
            }
        }
    }

    private void readVQHeader() throws IOException {
        readImageDisplayParameterSubheader();
        readCompressionSection();
    }

    private void readImageDisplayParameterSubheader() throws IOException {
        mNumberOfImageRows = mImageData.readInt();
        LOGGER.debug("mNumberOfImageRows:" + mNumberOfImageRows);
        mNumberOfImageCodesPerRow = mImageData.readInt();
        LOGGER.debug("mNumberOfImageCodesPerRow: " + mNumberOfImageCodesPerRow);
        mImageCodeBitLength = mImageData.readUnsignedByte();
        LOGGER.debug("mImageCodeBitLength:" + mImageCodeBitLength);
    }

    private void readCompressionSection() throws IOException {
        readCompressionSectionSubheader();
        readCompressionLookupSubsection();
    }

    private void readCompressionSectionSubheader() throws IOException {
        mCompressionAlgorithmId = mImageData.readUnsignedShort();
        LOGGER.debug("mCompressionAlgorithmId:" + mCompressionAlgorithmId);
        mNumberOfCompressionLookupOffsetRecords = mImageData.readUnsignedShort();
        LOGGER.debug("mNumberOfCompressionLookupOffsetRecords:" + mNumberOfCompressionLookupOffsetRecords);
        mNumberOfCompressionParameterOffsetRecords = mImageData.readUnsignedShort();
        LOGGER.debug("mNumberOfCompressionParameterOffsetRecords:" + mNumberOfCompressionParameterOffsetRecords);
    }

    private void readCompressionLookupSubsection() throws IOException {
        mCompressionLookupOffsetTableOffset = mImageData.readInt();
        LOGGER.debug("mCompressionLookupOffsetTableOffset:" + mCompressionLookupOffsetTableOffset);
        mCompressionLookupTableOffsetRecordLength = mImageData.readUnsignedShort();
        LOGGER.debug("mCompressionLookupTableOffsetRecordLength:" + mCompressionLookupTableOffsetRecordLength);
        readCompressionLookupOffsetTable();
        readCompressionLookupTables();
    }

    private void readCompressionLookupOffsetTable() throws IOException {
        readCompressionLookupOffsetRecords();
    }

    private void readCompressionLookupOffsetRecords() throws IOException {
        LOGGER.debug("mNumberOfCompressionLookupOffsetRecords:" + mNumberOfCompressionLookupOffsetRecords);
        for (int i = 0; i < mNumberOfCompressionLookupOffsetRecords; ++i) {
            VQCompressionLookupOffsetRecord record = new VQCompressionLookupOffsetRecord();
            record.setCompressionLookupTableId(mImageData.readUnsignedShort());
            record.setNumberOfCompressionLookupRecords(mImageData.readInt());
            LOGGER.debug("numberOfCompressionLookupRecords:" + record.getNumberOfCompressionLookupRecords());
            record.setNumberOfValuesPerCompressionLookupRecord(mImageData.readUnsignedShort());
            LOGGER.debug("numberOfValuesPerCompressionLookupRecord:" + record.getNumberOfValuesPerCompressionLookupRecord());
            record.setCompressionLookupValueBitLength(mImageData.readUnsignedShort());
            LOGGER.debug("compressionLookupValueBitLength:" + record.getCompressionLookupValueBitLength());
            record.setCompressionLookupTableOffset(mImageData.readInt());
            mCompressionLookupOffsetRecords.add(record);
        }
    }

    private void readCompressionLookupTables() throws IOException {
        mCodebook = new int[mNumberOfCompressionLookupOffsetRecords][][];
        for (int compressionLookupTableIndex = 0;
                compressionLookupTableIndex < mNumberOfCompressionLookupOffsetRecords;
                ++compressionLookupTableIndex) {
            VQCompressionLookupOffsetRecord record = mCompressionLookupOffsetRecords.get(compressionLookupTableIndex);

            // Dimension this codebook entry based on the lookup offset record entry
            mCodebook[compressionLookupTableIndex]
                    = new int[record.getNumberOfCompressionLookupRecords()][record.getNumberOfValuesPerCompressionLookupRecord()];

            // Read record entries
            for (int compressionLookupRecordIndex = 0;
                    compressionLookupRecordIndex < record.getNumberOfCompressionLookupRecords();
                    ++compressionLookupRecordIndex) {
                // Read values for this record entry
                for (int compressionLookupValueIndex = 0;
                        compressionLookupValueIndex < record.getNumberOfValuesPerCompressionLookupRecord();
                        ++compressionLookupValueIndex) {
                    // Stuff it into the codebook.
                    mCodebook[compressionLookupTableIndex][compressionLookupRecordIndex][compressionLookupValueIndex] =
 (int) mImageData.readBits(record.getCompressionLookupValueBitLength());
                }
            }
        }
    }

}
