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

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import org.codice.imaging.nitf.core.common.impl.NitfInputStreamReader;
import org.codice.imaging.nitf.core.common.NitfReader;
import org.codice.imaging.nitf.core.tre.Tre;
import org.codice.imaging.nitf.core.tre.TreCollection;
import org.codice.imaging.nitf.core.tre.TreSource;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for SECTGA TRE parsing.
 *
 * This TRE is described in STDI-0002 Annex E, Section 3.13.
 *
 */
public class SECTGA_Test {
    
    public SECTGA_Test() {
    }

    @Test
    public void SimpleSECTGA() throws Exception {
        InputStream inputStream = new ByteArrayInputStream("SECTGA000281234567890ABCDEFGHIJKLMNOPQ0".getBytes());
        BufferedInputStream bufferedStream = new BufferedInputStream(inputStream);
        NitfReader nitfReader = new NitfInputStreamReader(bufferedStream);
        TreCollectionParser parser = new TreCollectionParser();
        TreCollection parseResult = parser.parse(nitfReader, 39, TreSource.ImageExtendedSubheaderData);
        assertEquals(1, parseResult.getTREs().size());
        Tre sectga = parseResult.getTREsWithName("SECTGA").get(0);
        assertNotNull(sectga);
        assertNull(sectga.getRawData());
        assertEquals(2, sectga.getEntries().size());
        assertEquals("1234567890AB", sectga.getFieldValue("SEC_ID"));
        assertEquals("CDEFGHIJKLMNOPQ", sectga.getFieldValue("SEC_BE"));
    }
}
