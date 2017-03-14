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
import java.io.File;
import java.io.IOException;
import java.util.function.Function;
import javax.imageio.ImageIO;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.codice.imaging.compare.Compare;
import org.codice.imaging.nitf.core.impl.SlottedParseStrategy;
import org.codice.imaging.nitf.core.common.NitfFormatException;
import org.codice.imaging.nitf.core.common.impl.NitfInputStreamReader;
import org.codice.imaging.nitf.core.common.NitfReader;
import org.codice.imaging.nitf.core.header.impl.NitfParser;
import org.codice.imaging.nitf.core.image.ImageSegment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import junit.framework.TestCase;

/**
 * Shared code for rendering tests
 */
public class RenderTestSupport extends TestCase {

    private static final Logger LOGGER = LoggerFactory.getLogger(RenderTestSupport.class);

    public RenderTestSupport(String testName) {
        super(testName);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    protected void testOneFile(final String testfile, final String parentDirectory) throws IOException, NitfFormatException {
        testOneFileRenderToAGRB(testfile, parentDirectory);
        testOneFileRenderToClosestDataModel(testfile, parentDirectory);
    }

    protected void testOneFileRenderToAGRB(final String testfile, final String parentDirectory)
            throws IOException, NitfFormatException {
        testFile(testfile, parentDirectory, nitfRendererImageSegmentPair -> {
            try {
                return nitfRendererImageSegmentPair.getLeft()
                        .render(nitfRendererImageSegmentPair.getRight());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }, Pair::getRight,
                (actualImage, expectedImage, imageSegment) -> Compare.areIdentical(actualImage, expectedImage));
    }

    protected void testOneFileRenderToClosestDataModel(final String testfile,
            final String parentDirectory) throws IOException, NitfFormatException {

        testFile(testfile,
                parentDirectory,
                nitfRendererImageSegmentPair -> {
                    try {
                        return nitfRendererImageSegmentPair.getLeft()
                                .renderToClosestDataModel(nitfRendererImageSegmentPair.getRight());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                },
                imageSegmentBufferedImagePair -> convert2ARGB(imageSegmentBufferedImagePair.getLeft(),
                        imageSegmentBufferedImagePair.getRight()),
                (actualImage, expectedImage, imageSegment) -> Compare.areIdentical(actualImage, expectedImage, (int) imageSegment.getNumberOfColumns(),
                        (int) imageSegment.getNumberOfRows()));
    }

    private interface CompareImage {
        boolean compare(BufferedImage actualImage, BufferedImage expectedImage, ImageSegment imageSegment);
    }

    protected void testFile(final String testfile,
            final String parentDirectory, Function<Pair<NitfRenderer, ImageSegment>, BufferedImage> supplier,
            Function<Pair<ImageSegment, BufferedImage>, BufferedImage> function,
            CompareImage compareImage) throws IOException, NitfFormatException {
        String inputFileName = "/" + parentDirectory + "/" + testfile;
        System.out.println("================================== Testing :" + inputFileName);
        assertNotNull("Test file missing: " + inputFileName, getClass().getResource(inputFileName));
        NitfReader reader = new NitfInputStreamReader(getClass().getResourceAsStream(inputFileName));
        SlottedParseStrategy parseStrategy = new SlottedParseStrategy(SlottedParseStrategy.IMAGE_DATA);
        NitfParser.parse(reader, parseStrategy);
        for (int i = 0; i < parseStrategy.getDataSource().getImageSegments().size(); ++i) {
            ImageSegment imageSegment = parseStrategy.getDataSource().getImageSegments().get(i);
            NitfRenderer renderer = new NitfRenderer();

            BufferedImage img = supplier.apply(new ImmutablePair<>(renderer, imageSegment));

            img = function.apply(new ImmutablePair<>(imageSegment, img));

            // TODO: move to automated (perceptual?) comparison
            File targetPath = new File("target" + File.separator + testfile + "_" + i + ".png");
            ImageIO.write(img, "png", targetPath);
            String referencePath = "/" + parentDirectory + "/" + testfile + "_" + i + ".ref.png";
            if (getClass().getResource(referencePath) != null) {
                BufferedImage refImage = ImageIO.read(getClass().getResourceAsStream(referencePath));
                assertTrue(compareImage.compare(img, refImage, imageSegment));
                targetPath.delete();
            }
        }
    }

    private BufferedImage convert2ARGB(ImageSegment imageSegment, BufferedImage bufferedImage) {
        BufferedImage imgAGRB = new BufferedImage(
                imageSegment.getImageLocationColumn() + (int) imageSegment.getNumberOfColumns(),
                imageSegment.getImageLocationRow() + (int) imageSegment.getNumberOfRows(),
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D targetGraphic = imgAGRB.createGraphics();
        targetGraphic.drawImage(bufferedImage, 0, 0, null);
        return imgAGRB;
    }

}
