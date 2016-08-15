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
package org.codice.imaging.nitf.trewrap;

import org.codice.imaging.nitf.core.common.NitfFormatException;
import org.codice.imaging.nitf.core.image.ImageCompression;
import org.codice.imaging.nitf.core.tre.Tre;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 * Tests for the MIMCSA wrapper.
 */
public class MIMCSA_WrapTest extends SharedTreTestSupport {

    private final String mTestData = "MIMCSA001211ad68fbf-d676-4702-9c40-8c264d13177b2.0000000e-011.9900000e-012.0100000e-0100NCNot Applicable                      N/A   ";
    private final String mNaNData = "MIMCSA001211ad68fbf-d676-4702-9c40-8c264d13177bNaN          NaN          NaN          00NCNot Applicable                      N/A   ";

    public MIMCSA_WrapTest() {};


    @Test
    public final void testGetters() throws NitfFormatException {
        Tre tre = parseTRE(mTestData, "MIMCSA");
        MIMCSA mimcsa = new MIMCSA(tre);
        assertTrue(mimcsa.getValidity().isValid());

        assertEquals("1ad68fbf-d676-4702-9c40-8c264d13177b", mimcsa.getLayerId());

        assertEquals(0.2, mimcsa.getNominalFrameRate(), 0.00001);
        assertEquals(0.199, mimcsa.getMinimumFrameRate(), 0.00001);
        assertEquals(0.201, mimcsa.getMaximumFrameRate(), 0.00001);
        assertEquals(0, mimcsa.getTemporalRSET());
        assertEquals("NC", mimcsa.getRequiredDecoder());
        assertEquals(ImageCompression.NOTCOMPRESSED, mimcsa.getRequiredDecoderValue());
        assertEquals("Not Applicable", mimcsa.getRequiredProfile().trim());
        assertEquals("N/A", mimcsa.getRequiredLevel().trim());
    }

    @Test
    public final void testNaN() throws NitfFormatException {
        Tre tre = parseTRE(mNaNData, "MIMCSA");
        MIMCSA mimcsa = new MIMCSA(tre);
        assertTrue(mimcsa.getValidity().isValid());

        assertEquals("1ad68fbf-d676-4702-9c40-8c264d13177b", mimcsa.getLayerId());

        assertEquals(Double.NaN, mimcsa.getNominalFrameRate(), 0.00001);
        assertEquals(Double.NaN, mimcsa.getMinimumFrameRate(), 0.00001);
        assertEquals(Double.NaN, mimcsa.getMaximumFrameRate(), 0.00001);
        assertEquals(0, mimcsa.getTemporalRSET());
        assertEquals("NC", mimcsa.getRequiredDecoder());
        assertEquals(ImageCompression.NOTCOMPRESSED, mimcsa.getRequiredDecoderValue());
    }
}
