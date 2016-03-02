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

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Iterator;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import org.codice.imaging.nitf.core.image.ImageSegment;
import org.codice.imaging.nitf.render.imagemode.ImageModeHandler;
import org.codice.imaging.nitf.render.imagemode.ImageModeHandlerFactory;

/**
 * Renderer for NITF files
 */
public class NitfRenderer {

    /**
     * Constructor.
     */
    public NitfRenderer() {
    }

    /**
     * Render to the specified Graphics2D target.
     *
     * @param imageSegment the segment to be rendered
     * @param targetGraphic the target to render to
     * @throws IOException if the source data could not be read from
     */
    public final void render(final ImageSegment imageSegment, Graphics2D targetGraphic) throws IOException {
        switch (imageSegment.getImageCompression()) {
        case BILEVEL:
            render(new BilevelBlockRenderer(), imageSegment, targetGraphic);
            break;
        case NOTCOMPRESSED:
        case NOTCOMPRESSEDMASK:
            ImageModeHandler modeHandler = ImageModeHandlerFactory.forImageSegment(imageSegment);

            if (modeHandler != null) {
                modeHandler.handleImage(imageSegment, targetGraphic);
            } else {
                throw new UnsupportedOperationException("Unhandled NC/NM format");
            }

            break;
        case DOWNSAMPLEDJPEG:
        case JPEG:
            skipToMarker(imageSegment.getData(), JpegMarkerCode.START_OF_IMAGE);
            renderJPEG(imageSegment, targetGraphic, null);
            break;
        case VECTORQUANTIZATION:
        case VECTORQUANTIZATIONMASK:
            render(new VectorQuantizationBlockRenderer(),
                    imageSegment,
                    targetGraphic);
            break;
        case JPEGMASK:
            ImageMask imageMask = new ImageMask(imageSegment, imageSegment.getData());
            renderJPEG(imageSegment, targetGraphic, imageMask);
            break;
        default:
            throw new UnsupportedOperationException("Unhandled image compression format: "
                    + imageSegment.getImageCompression());
        }
    }

    /**
     * Render the segment as a BufferedImage.
     *
     * @param imageSegment the image segment header for the segment to be rendered
     * @return rendered image
     * @throws IOException if the source data could not be read from
     */
    public final BufferedImage render(final ImageSegment imageSegment) throws IOException {
        BufferedImage img = new BufferedImage(imageSegment.getImageLocationColumn()
                + (int) imageSegment.getNumberOfColumns(),
                imageSegment.getImageLocationRow()
                        + (int) imageSegment.getNumberOfRows(),
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D targetGraphic = img.createGraphics();

        render(imageSegment, targetGraphic);
        return img;
    }

    private void render(BlockRenderer renderer, ImageSegment imageSegment, Graphics2D target) throws IOException {
        renderer.setImageSegment(imageSegment, imageSegment.getData());

        processBlocks(imageSegment, (rowIndex, columnIndex) -> {
            BufferedImage img = renderer.getImageBlock(rowIndex, columnIndex);
            target.drawImage(img,
                    imageSegment.getImageLocationColumn() + columnIndex
                            * imageSegment.getNumberOfPixelsPerBlockHorizontal(),
                    imageSegment.getImageLocationRow()
                            + rowIndex * imageSegment.getNumberOfPixelsPerBlockVertical(),
                    null);
        });
    }

    private void renderJPEG(ImageSegment imageSegment, Graphics2D targetGraphic, ImageMask imageMask) throws IOException {
        ImageReader reader = getJPEGimageReader();
        reader.setInput(imageSegment.getData());
        ThreadLocal<Integer> maskedBlocks = new ThreadLocal<Integer>();
        maskedBlocks.set(0);

        processBlocks(imageSegment, (rowIndex, columnIndex) -> {
            if (imageMask != null && imageMask.isMaskedBlock((rowIndex * imageSegment.getNumberOfBlocksPerRow() + columnIndex), 0)) {
                maskedBlocks.set(maskedBlocks.get() + 1);
                return;
            }

            BufferedImage img = reader.read(
                    (columnIndex + rowIndex * imageSegment.getNumberOfBlocksPerColumn()) - maskedBlocks.get());

            targetGraphic.drawImage(img,
                    columnIndex * imageSegment.getNumberOfPixelsPerBlockHorizontal(),
                    rowIndex * imageSegment.getNumberOfPixelsPerBlockVertical(),
                    null);
        });
    }

    private void processBlocks(ImageSegment imageSegment, BlockConsumer consumer)
            throws IOException {
        for (int rowIndex = 0; rowIndex < imageSegment.getNumberOfBlocksPerColumn(); ++rowIndex) {
            for (int columnIndex = 0; columnIndex < imageSegment.getNumberOfBlocksPerRow(); ++columnIndex) {
                consumer.acccept(rowIndex, columnIndex);
            }
        }
    }

    private void skipToMarker(ImageInputStream imageInputStream, JpegMarkerCode markerCode) throws IOException {
        imageInputStream.mark();
        byte fillByte = (byte) ((markerCode.getValue() >> 8) & 0xFF);
        byte markerByte = (byte) (markerCode.getValue() & 0xFF);

        int i = 0;
        byte a = imageInputStream.readByte();

        while (a == fillByte) {
            i++;
            a = imageInputStream.readByte();
        }

        imageInputStream.reset();

        if (a == markerByte) {
            imageInputStream.skipBytes(i - 1);
        }
    }

    private ImageReader getJPEGimageReader() {
        Iterator<ImageReader> imageReaders = ImageIO.getImageReadersByMIMEType("image/jpeg");
        return imageReaders.next();
    }
}
