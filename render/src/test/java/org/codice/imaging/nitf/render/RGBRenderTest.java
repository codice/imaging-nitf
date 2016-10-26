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

import java.io.IOException;
import org.codice.imaging.nitf.core.common.NitfFormatException;
import org.junit.Test;

/**
 * Additional tests for RGB rendering.
 *
 * There are some tests for RGB in various modes across the JITC samples. These supplement those tests for * additional cases.
 */
public class RGBRenderTest extends RenderTestSupport {

    public RGBRenderTest(String testname) {
        super(testname);
    }

    @Test
    public void testRectangular() throws IOException, NitfFormatException {
        testOneFile("merlionRGB.ntf", "Codice");
    }

    /**
     * Tests rendering of image with following features: RGB, 16 bits per pixel per band,
     * 16 actual pixels per band, and right pixel justification.
     */
    @Test
    public void test16BPPImage() throws IOException, NitfFormatException {
        testOneFile("rgb16.ntf", "fromGDAL");
    }

    /**
     * Tests rendering of image with following features: RGB, 16 bits per pixel per band,
     * 11 actual pixels per band, and right pixel justification.
     */
    @Test
    public void test11ABPPImage() throws IOException, NitfFormatException {
        testOneFile("rgb16_11ABPP.ntf", "fromGDAL");
    }

    /**
     * Tests rendering of image with following features: RGB, 16 bits per pixel per band,
     * 11 actual pixels per band, and left pixel justification.
     */
    @Test
    public void test11ABPPLeftImage() throws IOException, NitfFormatException {
        testOneFile("rgb16_11ABPPLeft.ntf", "fromGDAL");
    }

    /**
     * Tests rendering of image with following features: RGB, 16 bits per pixel per band,
     * 6 actual pixels per band, and left pixel justification.
     */
    @Test
    public void test6ABPPLeftImage() throws IOException, NitfFormatException {
        testOneFile("rgb16_6ABPPLeft.ntf", "fromGDAL");
    }

    /**
     * Tests rendering of image with following features: RGB, 16 bits per pixel per band,
     * 6 actual pixels per band, and right pixel justification.
     */
    @Test
    public void test6ABPPRightImage() throws IOException, NitfFormatException {
        testOneFile("rgb16_6ABPPRight.ntf", "fromGDAL");
    }
}
