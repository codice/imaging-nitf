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
package org.codice.imaging.nitf.core.tre.impl;

import static org.junit.Assert.assertEquals;

import org.codice.imaging.nitf.core.tre.Tre;
import org.junit.Test;

/**
 * Tests for MIMCSA TRE parsing.
 *
 * This TRE is described in MIE4NITF Version 1.1 Section 5.9.3.2.
 *
 */
public class MIMCSA_Test extends SharedTreTest {

    @Test
    public void SimpleMIMCSA() throws Exception {
        String treTag = "MIMCSA";
        String testData = "MIMCSA001211ad68fbf-d676-4702-9c40-8c264d13177b2.0000000E-011.9900000E-012.0100000E-0100NCNot applicable                      N/A   ";
        int expectedLength = treTag.length() + "00121".length() + 121;
        
        Tre mimcsa = parseTRE(testData, expectedLength, treTag);
        assertEquals("1ad68fbf-d676-4702-9c40-8c264d13177b", mimcsa.getFieldValue("LAYER_ID"));
        assertEquals("2.0000000E-01", mimcsa.getFieldValue("NOMINAL_FRAME_RATE"));
        assertEquals("1.9900000E-01", mimcsa.getFieldValue("MIN_FRAME_RATE"));
        assertEquals("2.0100000E-01", mimcsa.getFieldValue("MAX_FRAME_RATE"));
        assertEquals(0, mimcsa.getIntValue("T_RSET"));
        assertEquals("NC", mimcsa.getFieldValue("MI_REQ_DECODER"));
        assertEquals("Not applicable", mimcsa.getFieldValue("MI_REQ_PROFILE").trim());
        assertEquals("N/A", mimcsa.getFieldValue("MI_REQ_LEVEL").trim());
    }

    @Test
    public void NaN() throws Exception {
        String treTag = "MIMCSA";
        String testData = "MIMCSA001211ad68fbf-d676-4702-9c40-8c264d13177b2.0000000E-01NaN          NaN          00NCNot applicable                      N/A   ";
        int expectedLength = treTag.length() + "00121".length() + 121;

        Tre mimcsa = parseTRE(testData, expectedLength, treTag);
        assertEquals("1ad68fbf-d676-4702-9c40-8c264d13177b", mimcsa.getFieldValue("LAYER_ID"));
        assertEquals("2.0000000E-01", mimcsa.getFieldValue("NOMINAL_FRAME_RATE"));
        assertEquals("NaN          ", mimcsa.getFieldValue("MIN_FRAME_RATE"));
        assertEquals("NaN          ", mimcsa.getFieldValue("MAX_FRAME_RATE"));
        assertEquals(0, mimcsa.getIntValue("T_RSET"));
        assertEquals("NC", mimcsa.getFieldValue("MI_REQ_DECODER"));
        assertEquals("Not applicable", mimcsa.getFieldValue("MI_REQ_PROFILE").trim());
        assertEquals("N/A", mimcsa.getFieldValue("MI_REQ_LEVEL").trim());
    }
}
