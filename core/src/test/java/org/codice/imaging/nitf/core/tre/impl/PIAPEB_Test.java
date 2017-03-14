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

import org.codice.imaging.nitf.core.common.NitfReader;
import org.codice.imaging.nitf.core.tre.Tre;
import org.junit.Test;

/**
 * Tests for PIAPEB TRE parsing.
 *
 * This TRE is described in STDI-0002 Annex C, Section C.4.
 *
 */
public class PIAPEB_Test extends SharedTreTest {
    
    public PIAPEB_Test() {
    }

    @Test
    public void SimplePIAPEB() throws Exception {
        String treTag = "PIAPEB";
        String testData = "PIAPEB00094Maxwell                     James                       Clerk                       18310613UK";
        int expectedLength = treTag.length() + "00094".length() + 94;
        
        Tre piapeb = parseTRE(testData, expectedLength, treTag);
        assertEquals(5, piapeb.getEntries().size());
        assertEquals("Maxwell", piapeb.getFieldValue("LASTNME").trim());
        assertEquals("James", piapeb.getFieldValue("FIRSTNME").trim());
        assertEquals("Clerk", piapeb.getFieldValue("MIDNME").trim());
        assertEquals("18310613", piapeb.getFieldValue("DOB"));
        assertEquals("UK", piapeb.getFieldValue(("ASSOCTRY")));
    }

}
