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
package org.codice.imaging.nitf.core.image;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 * Tests for ImageCategory enumeration.
 */
public class ImageCategoryTest {

    public ImageCategoryTest() {
    }

    @Test
    public void checkUnknownConversion() {
        assertEquals(ImageCategory.UNKNOWN, ImageCategory.getEnumValue(""));
        assertEquals(ImageCategory.UNKNOWN, ImageCategory.getEnumValue(null));
        assertEquals(ImageCategory.UNKNOWN, ImageCategory.getEnumValue("xyzzy"));
    }

    @Test
    public void checkMotionImagery() {
        assertEquals(ImageCategory.VISUAL, ImageCategory.getEnumValue("VIS"));
        assertFalse(ImageCategory.VISUAL.isMotionImagery());
        assertEquals(ImageCategory.VISUAL_MOTION, ImageCategory.getEnumValue("VIS.M"));
        assertTrue(ImageCategory.VISUAL_MOTION.isMotionImagery());
    }

    @Test
    public void checkMaxLength() {
        for (ImageCategory icat : ImageCategory.values()) {
            assertTrue(icat.getTextEquivalent().length() <= ImageConstants.ICAT_LENGTH);
        }
    }
}
