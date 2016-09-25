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
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import junit.framework.TestCase;
import static junit.framework.TestCase.assertTrue;
import org.codice.imaging.compare.Compare;
import org.codice.imaging.nitf.core.SlottedParseStrategy;
import org.codice.imaging.nitf.core.common.NitfFormatException;
import org.codice.imaging.nitf.core.common.NitfInputStreamReader;
import org.codice.imaging.nitf.core.common.NitfReader;
import org.codice.imaging.nitf.core.header.NitfParser;
import org.codice.imaging.nitf.core.image.ImageSegment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        String inputFileName = "/" + parentDirectory + "/" + testfile;
        System.out.println("================================== Testing :" + inputFileName);
        assertNotNull("Test file missing: " + inputFileName, getClass().getResource(inputFileName));
        NitfReader reader = new NitfInputStreamReader(getClass().getResourceAsStream(inputFileName));
        SlottedParseStrategy parseStrategy = new SlottedParseStrategy(SlottedParseStrategy.IMAGE_DATA);
        NitfParser.parse(reader, parseStrategy);
        for (int i = 0; i < parseStrategy.getDataSource().getImageSegments().size(); ++i) {
            ImageSegment imageSegment = parseStrategy.getDataSource().getImageSegments().get(i);
            NitfRenderer renderer = new NitfRenderer();
            BufferedImage img = renderer.render(imageSegment);
            // TODO: move to automated (perceptual?) comparison
            File targetPath = new File("target" + File.separator + testfile + "_" + i + ".png");
            ImageIO.write(img, "png", targetPath);
            String referencePath = "/" + parentDirectory + "/" + testfile + "_" + i + ".ref.png";
            if (getClass().getResource(referencePath) != null) {
                BufferedImage refImage = ImageIO.read(getClass().getResourceAsStream(referencePath));
                assertTrue(Compare.areIdentical(img, refImage));
                targetPath.delete();
            }
        }
    }
}
