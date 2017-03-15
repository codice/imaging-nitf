/*
 * Copyright (C) 2016 Codice Foundation
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */
package org.codice.imaging.nitf.render;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.MemoryCacheImageInputStream;
import static javax.xml.bind.DatatypeConverter.parseHexBinary;
import org.codice.imaging.nitf.core.image.ImageCompression;
import org.codice.imaging.nitf.core.image.ImageSegment;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Unit tests for NitfRenderer class.
 */
public class JpegTest {

    public JpegTest() {
    }

    @Test
    public void checkJpegMaskRender() throws IOException, URISyntaxException {
        NitfRenderer renderer = new NitfRenderer();

        // Mock up a render source
        ImageSegment mockImageSegment = Mockito.mock(ImageSegment.class);
        Mockito.when(mockImageSegment.getImageCompression()).thenReturn(ImageCompression.JPEGMASK);
        Mockito.when(mockImageSegment.getNumberOfBlocksPerRow()).thenReturn(2);
        Mockito.when(mockImageSegment.getNumberOfBlocksPerColumn()).thenReturn(3);
        Mockito.when(mockImageSegment.getNumberOfPixelsPerBlockHorizontal()).thenReturn(256L);
        Mockito.when(mockImageSegment.getNumberOfPixelsPerBlockVertical()).thenReturn(256L);
        Mockito.when(mockImageSegment.getNumBands()).thenReturn(1);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write(parseHexBinary("00000044000400000000FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF00000000"));
        FileInputStream fis = new FileInputStream(new File(getClass().getResource("/256by256.jpg").toURI()));
        byte[] lastBlock = new byte[fis.available()];
        fis.read(lastBlock);
        baos.write(lastBlock, 0, lastBlock.length);
        ImageInputStream iis = new MemoryCacheImageInputStream(new ByteArrayInputStream(baos.toByteArray()));
        Mockito.when(mockImageSegment.getData()).thenReturn(iis);
        BufferedImage imgAGRB = new BufferedImage((int) (mockImageSegment.getNumberOfBlocksPerRow() * mockImageSegment.getNumberOfPixelsPerBlockHorizontal()),
                (int) (mockImageSegment.getNumberOfBlocksPerColumn() * mockImageSegment.getNumberOfPixelsPerBlockVertical()),
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D targetGraphic = imgAGRB.createGraphics();
        renderer.render(mockImageSegment, targetGraphic);
    }
    
    @Test
    public void checkJpegMaskRender2() throws IOException, URISyntaxException {
        NitfRenderer renderer = new NitfRenderer();

        // Mock up a render source
        ImageSegment mockImageSegment = Mockito.mock(ImageSegment.class);
        Mockito.when(mockImageSegment.getImageCompression()).thenReturn(ImageCompression.JPEGMASK);
        Mockito.when(mockImageSegment.getNumberOfBlocksPerRow()).thenReturn(3);
        Mockito.when(mockImageSegment.getNumberOfBlocksPerColumn()).thenReturn(2);
        Mockito.when(mockImageSegment.getNumberOfPixelsPerBlockHorizontal()).thenReturn(256L);
        Mockito.when(mockImageSegment.getNumberOfPixelsPerBlockVertical()).thenReturn(256L);
        Mockito.when(mockImageSegment.getNumBands()).thenReturn(1);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write(parseHexBinary("00000044000400000000FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF00000000"));
        FileInputStream fis = new FileInputStream(new File(getClass().getResource("/256by256.jpg").toURI()));
        byte[] lastBlock = new byte[fis.available()];
        fis.read(lastBlock);
        baos.write(lastBlock, 0, lastBlock.length);
        ImageInputStream iis = new MemoryCacheImageInputStream(new ByteArrayInputStream(baos.toByteArray()));
        Mockito.when(mockImageSegment.getData()).thenReturn(iis);
        BufferedImage imgAGRB = new BufferedImage((int) (mockImageSegment.getNumberOfBlocksPerRow() * mockImageSegment.getNumberOfPixelsPerBlockHorizontal()),
                (int) (mockImageSegment.getNumberOfBlocksPerColumn() * mockImageSegment.getNumberOfPixelsPerBlockVertical()),
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D targetGraphic = imgAGRB.createGraphics();
        renderer.render(mockImageSegment, targetGraphic);
    }

}
