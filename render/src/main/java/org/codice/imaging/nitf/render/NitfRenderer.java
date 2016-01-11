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
import org.codice.imaging.nitf.core.image.NitfImageSegmentHeader;

public class NitfRenderer {

    public final void render(final NitfImageSegmentHeader imageSegmentHeader,
            final ImageInputStream imageData, Graphics2D targetGraphic) throws IOException {
        switch (imageSegmentHeader.getImageCompression()) {
        case BILEVEL:
            renderBlocks(new BilevelBlockRenderer(), imageSegmentHeader, imageData, targetGraphic);
            break;
        case NOTCOMPRESSED:
        case NOTCOMPRESSEDMASK:
            renderBlocks(new UncompressedBlockRenderer(),
                    imageSegmentHeader,
                    imageData,
                    targetGraphic);
            break;
        case JPEG:
            renderJPEG(imageSegmentHeader, imageData, targetGraphic);
            break;
        case VECTORQUANTIZATION:
        case VECTORQUANTIZATIONMASK:
            renderBlocks(new VectorQuantizationBlockRenderer(),
                    imageSegmentHeader,
                    imageData,
                    targetGraphic);
            break;
        default:
            System.out.println("Unhandled image compression format: "
                    + imageSegmentHeader.getImageCompression());
            System.exit(0);
            break;
        }
    }

    public final BufferedImage render(final NitfImageSegmentHeader imageSegmentHeader,
            final ImageInputStream imageData) throws IOException {
        BufferedImage img = new BufferedImage(imageSegmentHeader.getImageLocationColumn()
                + (int) imageSegmentHeader.getNumberOfColumns(),
                imageSegmentHeader.getImageLocationRow()
                        + (int) imageSegmentHeader.getNumberOfRows(),
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D targetGraphic = img.createGraphics();

        render(imageSegmentHeader, imageData, targetGraphic);
        return img;
    }

    private void renderBlocks(BlockRenderer renderer, NitfImageSegmentHeader imageSegmentHeader,
            ImageInputStream imageInputStream, Graphics2D target) throws IOException {
        renderer.setImageSegment(imageSegmentHeader, imageInputStream);

        processBlocks(imageSegmentHeader, (rowIndex, columnIndex) -> {
            BufferedImage img = renderer.getImageBlock(rowIndex, columnIndex);
            target.drawImage(img,
                    imageSegmentHeader.getImageLocationColumn() + columnIndex
                            * imageSegmentHeader.getNumberOfPixelsPerBlockHorizontal(),
                    imageSegmentHeader.getImageLocationRow()
                            + rowIndex * imageSegmentHeader.getNumberOfPixelsPerBlockVertical(),
                    null);
        });
    }

    private void renderJPEG(NitfImageSegmentHeader imageSegmentHeader,
            ImageInputStream imageInputStream, Graphics2D targetGraphic) throws IOException {
        skipToMarker(imageInputStream, JpegMarkerCode.START_OF_IMAGE) ;
        ImageReader reader = getJPEGimageReader();
        reader.setInput(imageInputStream);

        processBlocks(imageSegmentHeader, (rowIndex, columnIndex) -> {
            BufferedImage img = reader.read(
                    columnIndex + rowIndex * imageSegmentHeader.getNumberOfBlocksPerColumn());

            targetGraphic.drawImage(img,
                    columnIndex * imageSegmentHeader.getNumberOfPixelsPerBlockHorizontal(),
                    rowIndex * imageSegmentHeader.getNumberOfPixelsPerBlockVertical(),
                    null);
        });
    }

    private void processBlocks(NitfImageSegmentHeader imageSegmentHeader, BlockConsumer consumer)
            throws IOException {
        for (int rowIndex = 0; rowIndex < imageSegmentHeader.getNumberOfBlocksPerColumn(); ++rowIndex) {
            for (int columnIndex = 0; columnIndex < imageSegmentHeader.getNumberOfBlocksPerRow(); ++columnIndex) {
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