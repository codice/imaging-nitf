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

import org.codice.imaging.nitf.core.image.ImageMode;
import org.junit.Test;

/**
 * Tests for ImageMode enumeration.
 */
public class ImageModeTest {

    public ImageModeTest() {
    }

    @Test
    public void checkUnknownConversion() {
        assertEquals(ImageMode.UNKNOWN, ImageMode.getEnumValue(""));
        assertEquals(ImageMode.UNKNOWN, ImageMode.getEnumValue(null));
        assertEquals(ImageMode.UNKNOWN, ImageMode.getEnumValue("xyzzy"));
    }

    @Test
    public void checkMotionImageryExtensions() {
        assertEquals(ImageMode.TEMPORALBANDSEQUENTIAL, ImageMode.getEnumValue("D"));
        assertEquals(ImageMode.TEMPORALINTERLEAVEBYSAMPLE, ImageMode.getEnumValue("E"));
        assertEquals(ImageMode.TEMPORALBANDBANDSEQUENTIALBYBLOCK, ImageMode.getEnumValue("F"));
        assertEquals(ImageMode.TEMPORALBANDINTERLEAVEBYBLOCK, ImageMode.getEnumValue("T"));
    }

    @Test
    public void checkStillImageModes() {
        assertEquals(ImageMode.BANDSEQUENTIAL, ImageMode.getEnumValue("S"));
        assertEquals(ImageMode.PIXELINTERLEVE, ImageMode.getEnumValue("P"));
        assertEquals(ImageMode.BLOCKINTERLEVE, ImageMode.getEnumValue("B"));
        assertEquals(ImageMode.ROWINTERLEVE, ImageMode.getEnumValue("R"));
    }
}
