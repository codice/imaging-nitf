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
 * Tests for PIATGB TRE parsing.
 *
 * This TRE is described in STDI-0002 Annex C, Section C.3.
 *
 */
public class PIATGB_Test extends SharedTreTest {
    
    public PIATGB_Test() {
    }

    @Test
    public void SimplePIATGB() throws Exception {
        String treTag = "PIATGB";
        String testData = "PIATGB0011755HFA9359093610ABCDEFGHIJUVWXYAS702XX351655S1490742EWGECanberra Hill                         057-35.30812 +149.12447 ";
        int expectedLength = treTag.length() + "00117".length() + 117;
        
        Tre piatgb = parseTRE(testData, expectedLength, treTag);
        assertEquals(10, piatgb.getEntries().size());
        assertEquals("55HFA9359093610", piatgb.getFieldValue("TGTUTM"));
        assertEquals("ABCDEFGHIJUVWXY", piatgb.getFieldValue("PIATGAID"));
        assertEquals("AS", piatgb.getFieldValue("PIACTRY"));
        assertEquals("702XX", piatgb.getFieldValue("PIACAT"));
        assertEquals("351655S1490742E", piatgb.getFieldValue("TGTGEO"));
        assertEquals("WGE", piatgb.getFieldValue("DATUM"));
        assertEquals("Canberra Hill                         ", piatgb.getFieldValue("TGTNAME"));
        assertEquals(57, piatgb.getIntValue("PERCOVER"));
        assertEquals("-35.30812 ", piatgb.getFieldValue("TGTLAT"));
        assertEquals("+149.12447 ", piatgb.getFieldValue("TGTLON"));
    }
}
