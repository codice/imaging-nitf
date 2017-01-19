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
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import org.codice.imaging.nitf.core.image.ImageBand;
import org.codice.imaging.nitf.core.image.ImageRepresentation;
import org.codice.imaging.nitf.core.image.ImageSegment;
import org.codice.imaging.nitf.render.imagemode.ImageModeHandler;
import org.codice.imaging.nitf.render.imagemode.ImageModeHandlerFactory;
import org.codice.imaging.nitf.render.imagerep.ImageRepresentationHandler;
import org.codice.imaging.nitf.render.imagerep.ImageRepresentationHandlerFactory;

/**
 * Renderer for NITF files.
 */
public class NitfRenderer {

    private static final int BYTE_MASK = 0xFF;

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
    public final void render(final ImageSegment imageSegment, final Graphics2D targetGraphic) throws IOException {
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
        case JPEG2000:
            renderJPEG2k(imageSegment, targetGraphic);
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

    /**
     * Render the segment as a BufferedImage using a data model that matches the NITF data as close as possible.
     *
     * @param imageSegment the image segment header for the segment to be rendered
     * @return rendered image
     * @throws IOException if the source data could not be read from
     */
    public final BufferedImage renderToClosestDataModel(final ImageSegment imageSegment) throws IOException {
        ImageRepresentationHandler handler =
                ImageRepresentationHandlerFactory.forImageSegment(imageSegment);

        BufferedImage img = handler.createBufferedImage(imageSegment.getImageLocationColumn()
                        + (int) imageSegment.getNumberOfColumns(),
                imageSegment.getImageLocationRow()
                        + (int) imageSegment.getNumberOfRows());

        Graphics2D targetGraphic = img.createGraphics();

        render(imageSegment, targetGraphic);
        return img;
    }

    private void render(final BlockRenderer renderer, final ImageSegment imageSegment, final Graphics2D target) throws IOException {
        renderer.setImageSegment(imageSegment, imageSegment.getData());

        processBlocks(imageSegment, (rowIndex, columnIndex) -> {
            BufferedImage img = renderer.getImageBlock(rowIndex, columnIndex);
            target.drawImage(img,
                    imageSegment.getImageLocationColumn() + columnIndex
                            * (int) imageSegment.getNumberOfPixelsPerBlockHorizontal(),
                    imageSegment.getImageLocationRow()
                            + rowIndex * (int) imageSegment.getNumberOfPixelsPerBlockVertical(),
                    null);
        });
    }

    private void renderJPEG(final ImageSegment imageSegment, final Graphics2D targetGraphic, final ImageMask imageMask) throws IOException {
        ImageReader reader = getImageReader("image/jpeg");
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
                    columnIndex * (int) imageSegment.getNumberOfPixelsPerBlockHorizontal(),
                    rowIndex * (int) imageSegment.getNumberOfPixelsPerBlockVertical(),
                    null);
        });
    }

    private void renderJPEG2k(final ImageSegment imageSegment, final Graphics2D targetGraphic) throws IOException {
        final ImageReader reader = getImageReader("image/jp2");
        reader.setInput(imageSegment.getData(), true, true);
        final ImageReadParam param = reader.getDefaultReadParam();

        if (ImageRepresentation.MULTIBAND.equals(imageSegment.getImageRepresentation())) {
            final int[] sourceBands = getSourceBands(imageSegment);
            param.setSourceBands(sourceBands);
        }

        processBlocks(imageSegment, (r, c) -> {
                    Rectangle rect = new Rectangle((int) (c * imageSegment.getNumberOfPixelsPerBlockHorizontal()),
                            (int) (r * imageSegment.getNumberOfPixelsPerBlockVertical()),
                            (int) imageSegment.getNumberOfPixelsPerBlockHorizontal(),
                            (int) imageSegment.getNumberOfPixelsPerBlockVertical());
                    param.setSourceRegion(rect);

                    BufferedImage renderedBlock = reader.read(0, param);
                    param.setDestination(renderedBlock);
                    targetGraphic.drawImage(renderedBlock, (int) (c * imageSegment.getNumberOfPixelsPerBlockVertical()),
                            (int) (r * imageSegment.getNumberOfPixelsPerBlockHorizontal()),
                            null);

                }
        );
    }

    private int[] getSourceBands(final ImageSegment imageSegment) {
        List<Integer> imageBands = new ArrayList<Integer>();

        for (int i = 0; i < imageSegment.getNumBands(); i++) {
            ImageBand band = imageSegment.getImageBandZeroBase(i);

            if (null != band.getImageRepresentation()) {
                switch (band.getImageRepresentation()) {
                case "R":
                case "G":
                case "B":
                    imageBands.add(i);
                    break;
                default:
                    break;
                }
            }
        }

        int[] imageBandAry = new int[imageBands.size()];

        for (int i = 0; i < imageBands.size(); i++) {
            imageBandAry[i] = imageBands.get(i);
        }

        return imageBandAry;
    }

    private void processBlocks(final ImageSegment imageSegment, final BlockConsumer consumer)
            throws IOException {
        for (int rowIndex = 0; rowIndex < imageSegment.getNumberOfBlocksPerColumn(); ++rowIndex) {
            for (int columnIndex = 0; columnIndex < imageSegment.getNumberOfBlocksPerRow(); ++columnIndex) {
                consumer.acccept(rowIndex, columnIndex);
            }
        }
    }

    private void skipToMarker(final ImageInputStream imageInputStream, final JpegMarkerCode markerCode) throws IOException {
        imageInputStream.mark();
        byte fillByte = (byte) ((markerCode.getValue() >> Byte.SIZE) & BYTE_MASK);
        byte markerByte = (byte) (markerCode.getValue() & BYTE_MASK);

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


    private ImageReader getImageReader(final String mediaType) {
        Iterator<ImageReader> imageReaders = ImageIO.getImageReadersByMIMEType(mediaType);

        if (imageReaders == null || !imageReaders.hasNext()) {
            throw new UnsupportedOperationException(
                    String.format("NitfRenderer.render(): no ImageReader found for media type '%s'.", mediaType));
        }

        return imageReaders.next();
    }
}
