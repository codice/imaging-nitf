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
 * Tests for MSTGTA TRE parsing.
 *
 * This TRE is described in STDI-0002-1 Appendix E: ASDE 2.1/CN1 Section E.3.9.
 *
 */
public class MSTGTA_Test extends SharedTreTest {

    private final String treTag = "MSTGTA";

    public MSTGTA_Test() {
    }

    @Test
    public void SimpleMSTGTA() throws Exception {
        String testData = "MSTGTA0010100001ABC123DEF456789XYZ654321POI002The Boss.   2018111623591490231084632Z+01634m+30.482261-086.503262";
        int expectedLength = treTag.length() + "00101".length() + 101;

        Tre mstgta = parseTRE(testData, expectedLength, treTag);
        assertEquals("00001", mstgta.getFieldValue("TGT_NUM"));
        assertEquals(1, mstgta.getIntValue("TGT_NUM"));
        assertEquals("ABC123DEF456", mstgta.getFieldValue("TGT_ID"));
        assertEquals("789XYZ654321POI", mstgta.getFieldValue("TGT_BE"));
        assertEquals("002", mstgta.getFieldValue("TGT_PRI"));
        assertEquals("The Boss.", mstgta.getFieldValue("TGT_REQ").trim());
        assertEquals("201811162359", mstgta.getFieldValue("TGT_LTIOV"));
        assertEquals("1", mstgta.getFieldValue("TGT_TYPE"));
        assertEquals("4", mstgta.getFieldValue("TGT_COLL"));
        assertEquals("90231", mstgta.getFieldValue("TGT_CAT"));
        assertEquals("084632Z", mstgta.getFieldValue("TGT_UTC"));
        assertEquals("+01634", mstgta.getFieldValue("TGT_ELEV"));
        assertEquals("m", mstgta.getFieldValue("TGT_ELEV_UNIT"));
        assertEquals("+30.482261-086.503262", mstgta.getFieldValue("TGT_LOC"));
    }

    @Test
    public void SpaceFilledMSTGTA() throws Exception {
        String testData = "MSTGTA0010100000                                                       3                   +42.462679-071.281274";
        int expectedLength = treTag.length() + "00101".length() + 101;

        Tre mstgta = parseTRE(testData, expectedLength, treTag);
        assertEquals("00000", mstgta.getFieldValue("TGT_NUM"));
        assertEquals(0, mstgta.getIntValue("TGT_NUM"));
        assertEquals("            ", mstgta.getFieldValue("TGT_ID"));
        assertEquals("               ", mstgta.getFieldValue("TGT_BE"));
        assertEquals("   ", mstgta.getFieldValue("TGT_PRI"));
        assertEquals("            ", mstgta.getFieldValue("TGT_REQ"));
        assertEquals("            ", mstgta.getFieldValue("TGT_LTIOV"));
        assertEquals(" ", mstgta.getFieldValue("TGT_TYPE"));
        assertEquals("3", mstgta.getFieldValue("TGT_COLL"));
        assertEquals("     ", mstgta.getFieldValue("TGT_CAT"));
        assertEquals("       ", mstgta.getFieldValue("TGT_UTC"));
        assertEquals("      ", mstgta.getFieldValue("TGT_ELEV"));
        assertEquals(" ", mstgta.getFieldValue("TGT_ELEV_UNIT"));
        assertEquals("+42.462679-071.281274", mstgta.getFieldValue("TGT_LOC"));
    }
}
