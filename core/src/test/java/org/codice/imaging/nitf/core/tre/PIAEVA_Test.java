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
package org.codice.imaging.nitf.core.tre;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for PIAEVA TRE parsing.
 *
 * This TRE is described in STDI-0002 Annex C, Section C.5.
 *
 */
public class PIAEVA_Test extends SharedTreTest {
    
    public PIAEVA_Test() {
    }

    @Test
    public void SimplePIAEVA() throws Exception {
        String treTag = "PIAEVA";
        String testData = "PIAEVA00046Dawn at Ceres                         SPACE   ";
        int expectedLength = treTag.length() + "00046".length() + 46;
        
        Tre piaeva = parseTRE(testData, expectedLength, treTag);
        assertEquals(2, piaeva.getEntries().size());
        assertEquals("Dawn at Ceres                         ", piaeva.getFieldValue("EVENTNAME"));
        assertEquals("SPACE   ", piaeva.getFieldValue("EVENTTYPE"));
    }
}
