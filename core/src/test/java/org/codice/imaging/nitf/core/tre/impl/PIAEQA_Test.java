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
 * Tests for PIAEQA TRE parsing.
 *
 * This TRE is described in STDI-0002 Annex C, Section C.6.
 *
 */
public class PIAEQA_Test extends SharedTreTest {
    
    public PIAEQA_Test() {
    }

    @Test
    public void SimplePIAEQA() throws Exception {
        String treTag = "PIAEQA";
        String testData = "PIAEQA00130A123456TECHNICAL(UNKNOWN)1234567890ABCDEFGHIJKMNOPQRSOME MANUFACTURER. Some padding for length and testing purposes.XABCYAZCBottom";
        int expectedLength = treTag.length() + "00130".length() + 130;
        
        Tre piaeqa = parseTRE(testData, expectedLength, treTag);
        assertEquals(8, piaeqa.getEntries().size());
        assertEquals("A123456", piaeqa.getFieldValue("EQPCODE"));
        assertEquals("TECHNICAL(UNKNOWN)1234567890ABCDEFGHIJKMNOPQR", piaeqa.getFieldValue("EQPNOMEN"));
        assertEquals("SOME MANUFACTURER. Some padding for length and testing purposes.", piaeqa.getFieldValue("EQPMAN"));
        assertEquals("X", piaeqa.getFieldValue("OBTYPE"));
        assertEquals("ABC", piaeqa.getFieldValue("ORDBAT"));
        assertEquals("YA", piaeqa.getFieldValue("CTRYPROD"));
        assertEquals("ZC", piaeqa.getFieldValue("CTRYDSN"));
        assertEquals("Bottom", piaeqa.getFieldValue("OBJVIEW"));
    }
}
