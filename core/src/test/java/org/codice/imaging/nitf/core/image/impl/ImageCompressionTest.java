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
package org.codice.imaging.nitf.core.image.impl;

import static org.junit.Assert.assertEquals;

import org.codice.imaging.nitf.core.image.ImageCompression;
import org.junit.Test;

/**
 * Tests for ImageCompression enumeration.
 */
public class ImageCompressionTest {

    public ImageCompressionTest() {
    }

    @Test
    public void checkUnknownConversion() {
        assertEquals(ImageCompression.UNKNOWN, ImageCompression.getEnumValue(""));
        assertEquals(ImageCompression.UNKNOWN, ImageCompression.getEnumValue(null));
        assertEquals(ImageCompression.UNKNOWN, ImageCompression.getEnumValue("xyzzy"));
    }

    @Test
    public void checkMotionImageryExtensions() {
        assertEquals(ImageCompression.H264, ImageCompression.getEnumValue("C9"));
        assertEquals(ImageCompression.H265, ImageCompression.getEnumValue("CA"));
        assertEquals(ImageCompression.H264MASK, ImageCompression.getEnumValue("M9"));
        assertEquals(ImageCompression.H265MASK, ImageCompression.getEnumValue("MA"));
        assertEquals(ImageCompression.JPEG2000MASKTIME, ImageCompression.getEnumValue("MB"));
        assertEquals(ImageCompression.JPEG2000TIME, ImageCompression.getEnumValue("CB"));
    }

    @Test
    public void checkJPEG2000() {
        assertEquals(ImageCompression.JPEG2000, ImageCompression.getEnumValue("C8"));
        assertEquals(ImageCompression.JPEG2000MASK, ImageCompression.getEnumValue("M8"));
        assertEquals("C8", ImageCompression.JPEG2000.getTextEquivalent());
        assertEquals("M8", ImageCompression.JPEG2000MASK.getTextEquivalent());
    }

    @Test
    public void checkUncompressed() {
        assertEquals(ImageCompression.NOTCOMPRESSED, ImageCompression.getEnumValue("NC"));
        assertEquals(ImageCompression.NOTCOMPRESSEDMASK, ImageCompression.getEnumValue("NM"));
        assertEquals("NC", ImageCompression.NOTCOMPRESSED.getTextEquivalent());
        assertEquals("NM", ImageCompression.NOTCOMPRESSEDMASK.getTextEquivalent());
    }
}
