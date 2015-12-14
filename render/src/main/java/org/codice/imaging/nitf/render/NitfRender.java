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

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import org.codice.imaging.nitf.common.Nitf;
import org.codice.imaging.nitf.image.NitfImageSegmentHeader;

public class NitfRender {

    Nitf nitf = null;
    BufferedImage image;

    public NitfRender() {
    }
    
    public final void render(final NitfImageSegmentHeader imageSegmentHeader, final ImageInputStream imageData, final Graphics2D targetGraphic) throws IOException {
        
        switch (imageSegmentHeader.getImageCompression()) {
            case BILEVEL:
                render(new Bilevel(), imageSegmentHeader, imageData, targetGraphic);
                break;
            case NOTCOMPRESSED:
            case NOTCOMPRESSEDMASK:
                render(new Uncompressed(), imageSegmentHeader, imageData, targetGraphic);
                break;
            case JPEG:
                renderJPEG(imageSegmentHeader, imageData, targetGraphic);
                break;
            case VECTORQUANTIZATION:
            case VECTORQUANTIZATIONMASK:
                render(new VectorQuantization(), imageSegmentHeader, imageData, targetGraphic);
                break;
            default:
                System.out.println("Unhandled image compression format: " + imageSegmentHeader.getImageCompression());
                System.exit(0);
                break;
        }
    }

    private void renderJPEG(NitfImageSegmentHeader imageSegmentHeader, ImageInputStream imageInputStream, Graphics2D targetGraphic) throws IOException {
        ImageReader reader = getJPEGimageReader();

        reader.setInput(imageInputStream);
        for (int rowIndex = 0; rowIndex < imageSegmentHeader.getNumberOfBlocksPerRow(); ++rowIndex) {
            for (int columnIndex = 0; columnIndex < imageSegmentHeader.getNumberOfBlocksPerColumn(); ++columnIndex) {
                BufferedImage img = reader.read(columnIndex + rowIndex * imageSegmentHeader.getNumberOfBlocksPerColumn());
                targetGraphic.drawImage(img,
                        columnIndex * imageSegmentHeader.getNumberOfPixelsPerBlockHorizontal(),
                        rowIndex * imageSegmentHeader.getNumberOfPixelsPerBlockVertical(),
                        null);
            }
        }
    }

    private void render(BlockRenderer renderer, NitfImageSegmentHeader imageSegmentHeader, ImageInputStream imageInputStream, Graphics2D target) throws IOException {
        renderer.setImageSegment(imageSegmentHeader, imageInputStream);
        for (int rowIndex = 0; rowIndex < imageSegmentHeader.getNumberOfBlocksPerColumn(); ++rowIndex) {
            for (int columnIndex = 0; columnIndex < imageSegmentHeader.getNumberOfBlocksPerRow(); ++columnIndex) {
                BufferedImage img = renderer.getImageBlock(rowIndex, columnIndex);
                target.drawImage(img,
                        imageSegmentHeader.getImageLocationColumn() + columnIndex * imageSegmentHeader.getNumberOfPixelsPerBlockHorizontal(),
                        imageSegmentHeader.getImageLocationRow() + rowIndex * imageSegmentHeader.getNumberOfPixelsPerBlockVertical(), null);
            }
        }
    }

    private ImageReader getJPEGimageReader() {
        Iterator<ImageReader> imageReaders = ImageIO.getImageReadersByMIMEType("image/jpeg");
        return imageReaders.next();
    }
}
