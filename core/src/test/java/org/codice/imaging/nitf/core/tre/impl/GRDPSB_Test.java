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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.codice.imaging.nitf.core.common.NitfFormatException;
import org.codice.imaging.nitf.core.tre.Tre;
import org.codice.imaging.nitf.core.tre.TreGroup;
import org.junit.Test;

/**
 * Tests for parsing GRDPSB.
 *
 * This is defined in STDI-0002 Appendix P Table P-4.
 */
public class GRDPSB_Test extends SharedTreTest {

    public GRDPSB_Test() {
    }

    @Test
    public void SimpleParse() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write("GRDPSB0006801+000027.81PIX_LATLON0000000000010000000000010000000000000000000000".getBytes(StandardCharsets.ISO_8859_1));
        Tre mtimsa = doParseAndCheckResults(baos);
    }

    private Tre doParseAndCheckResults(ByteArrayOutputStream baos) throws NitfFormatException, IOException {
        Tre grdpsb = parseTRE(baos.toByteArray(), baos.toByteArray().length, "GRDPSB");
        assertEquals(grdpsb.getIntValue("NUM_GRDS"), 1);
        assertEquals(grdpsb.getEntry("GRDS").getGroups().size(), 1);
        TreGroup group1 = grdpsb.getEntry("GRDS").getGroups().get(0);
        assertEquals(group1.getFieldValue("ZVL"), "+000027.81");
        assertEquals(group1.getDoubleValue("ZVL"), 27.81, 0.001);
        assertEquals(group1.getFieldValue("BAD"), "PIX_LATLON");
        assertEquals(group1.getFieldValue("LOD"), "000000000001");
        assertEquals(group1.getFieldValue("LAD"), "000000000001");
        assertEquals(group1.getFieldValue("LSO"), "00000000000");
        assertEquals(group1.getFieldValue("PSO"), "00000000000");
        return grdpsb;
    }
}
