/*
 * Copyright (c) 2016, Codice
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package org.codice.imaging.compare;

import java.awt.image.BufferedImage;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 * Check identical images.
 */
public class CheckIdenticalTest {

    public CheckIdenticalTest() {
    }

    @Test
    public void checkIdentical() {
        BufferedImage referenceImage = new BufferedImage(100, 200, BufferedImage.TYPE_BYTE_GRAY);
        BufferedImage imageToTest = new BufferedImage(100, 200, BufferedImage.TYPE_BYTE_GRAY);
        assertTrue(Compare.areIdentical(imageToTest, referenceImage));
    }

    @Test
    public void checkNotIdenticalWidth() {
        BufferedImage referenceImage = new BufferedImage(100, 200, BufferedImage.TYPE_BYTE_GRAY);
        BufferedImage imageToTest = new BufferedImage(200, 100, BufferedImage.TYPE_BYTE_GRAY);
        assertFalse(Compare.areIdentical(imageToTest, referenceImage));
    }

    @Test
    public void checkNotIdenticalHeight() {
        BufferedImage referenceImage = new BufferedImage(100, 200, BufferedImage.TYPE_BYTE_GRAY);
        BufferedImage imageToTest = new BufferedImage(100, 150, BufferedImage.TYPE_BYTE_GRAY);
        assertFalse(Compare.areIdentical(imageToTest, referenceImage));
    }

    @Test
    public void checkNotIdenticalContent() {
        BufferedImage referenceImage = new BufferedImage(100, 200, BufferedImage.TYPE_BYTE_GRAY);
        referenceImage.setRGB(0, 0, 0xffffff);
        BufferedImage imageToTest = new BufferedImage(100, 200, BufferedImage.TYPE_BYTE_GRAY);
        imageToTest.setRGB(0, 0, 0);
        assertFalse(Compare.areIdentical(imageToTest, referenceImage));
    }

    @Test
    public void checkNotIdenticalContentRGB() {
        BufferedImage referenceImage = new BufferedImage(100, 200, BufferedImage.TYPE_INT_RGB);
        referenceImage.setRGB(0, 0, 0x000001);
        BufferedImage imageToTest = new BufferedImage(100, 200, BufferedImage.TYPE_INT_RGB);
        imageToTest.setRGB(0, 0, 0);
        assertFalse(Compare.areIdentical(imageToTest, referenceImage));
    }

    @Test
    public void checkNotIdenticalContentLast() {
        BufferedImage referenceImage = new BufferedImage(100, 200, BufferedImage.TYPE_BYTE_GRAY);
        referenceImage.setRGB(99, 199, 0xFFFFFF);
        BufferedImage imageToTest = new BufferedImage(100, 200, BufferedImage.TYPE_BYTE_GRAY);
        imageToTest.setRGB(99, 199, 0);
        assertFalse(Compare.areIdentical(imageToTest, referenceImage));
    }
}
