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

import org.codice.imaging.nitf.core.tre.Tre;
import org.codice.imaging.nitf.core.tre.TreEntry;
import org.codice.imaging.nitf.core.tre.TreGroup;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;

/**
 * Tests for ACCVTB TRE parsing.
 *
 * This TRE is described in STDI-0002-1 Appendix P: Table P-12.
 *
 */
public class ACCVTB_Test extends SharedTreTest {

    private final String treTag = "ACCVTB";

    public ACCVTB_Test() {
    }

    @Test
    public void SnipRipACCVTB() throws Exception {
        String testData = "ACCVTB0014101M  00095M  00095004+044.4130499724+33.69234401034+044.4945572008+33.67855217830+044.1731373448+32.79106350687+044.2538103407+32.77733592314";
        int expectedLength = treTag.length() + "00141".length() + 141;

        Tre accvtb = parseTRE(testData, expectedLength, treTag);
        assertEquals("01", accvtb.getFieldValue("NUM_ACVT"));
        assertEquals(1, accvtb.getIntValue("NUM_ACVT"));
        TreEntry targets = accvtb.getEntry("ACCVT");
        assertEquals(1, targets.getGroups().size());
        TreGroup group0 = targets.getGroups().get(0);
        assertNotNull(group0);
        assertEquals("M  ", group0.getFieldValue("UNIAAV"));
        assertEquals("00095", group0.getFieldValue("AAV"));
        assertEquals(95, group0.getIntValue("AAV"));
        assertEquals("M  ", group0.getFieldValue("UNIAPV"));
        assertEquals("00095", group0.getFieldValue("APV"));
        assertEquals(95, group0.getIntValue("APV"));
        assertEquals(4, group0.getIntValue("NUM_PTS"));
        TreEntry points = group0.getEntry("POINT");
        assertEquals(4, points.getGroups().size());
        TreGroup group0_0 = points.getGroups().get(0);
        assertNotNull(group0_0);
        assertEquals(44.4130499724, group0_0.getDoubleValue("LON"), 0.00000001);
        assertEquals(33.69234401034, group0_0.getDoubleValue("LAT"), 0.00000001);
        TreGroup group0_1 = points.getGroups().get(1);
        assertNotNull(group0_1);
        assertEquals(44.4945572008, group0_1.getDoubleValue("LON"), 0.00000001);
        assertEquals(33.67855217830, group0_1.getDoubleValue("LAT"), 0.00000001);
        TreGroup group0_2 = points.getGroups().get(2);
        assertNotNull(group0_2);
        assertEquals(44.1731373448, group0_2.getDoubleValue("LON"), 0.00000001);
        assertEquals(32.79106350687, group0_2.getDoubleValue("LAT"), 0.00000001);
        TreGroup group0_3 = points.getGroups().get(3);
        assertNotNull(group0_3);
        assertEquals(44.2538103407, group0_3.getDoubleValue("LON"), 0.00000001);
        assertEquals(32.77733592314, group0_3.getDoubleValue("LAT"), 0.00000001);
    }
}
