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
 * Tests for EXOPTA TRE parsing.
 *
 * This TRE is described in STDI-0002 Annex E, Section 3.5.
 *
 */
public class EXOPTA_Test {
    
    public EXOPTA_Test() {
    }

    @Test
    public void SimpleEXOPTA() throws Exception {
        InputStream inputStream = new ByteArrayInputStream("EXOPTA00107134203.3100512       82.31-26.971234567890ABCDEFGHIJKLMNOPQ     023  0000001003023487            +67.3349.2".getBytes());
        BufferedInputStream bufferedStream = new BufferedInputStream(inputStream);
        NitfReader nitfReader = new NitfInputStreamReader(bufferedStream);
        TreCollectionParser parser = new TreCollectionParser();
        TreCollection parseResult = parser.parse(nitfReader, 118, TreSource.ImageExtendedSubheaderData);
        assertEquals(1, parseResult.getTREs().size());
        Tre exopta = parseResult.getTREsWithName("EXOPTA").get(0);
        assertNotNull(exopta);
        assertNull(exopta.getRawData());
        assertEquals(12, exopta.getEntries().size());
        assertEquals("134", exopta.getFieldValue("ANGLE_TO_NORTH"));
        assertEquals("203.3", exopta.getFieldValue("MEAN_GSD"));
        assertEquals("00512", exopta.getFieldValue("DYNAMIC_RANGE"));
        assertEquals("82.31", exopta.getFieldValue("OBL_ANG"));
        assertEquals("-26.97", exopta.getFieldValue("ROLL_ANG"));
        assertEquals("1234567890AB", exopta.getFieldValue("PRIME_ID"));
        assertEquals("CDEFGHIJKLMNOPQ", exopta.getFieldValue("PRIME_BE"));
        assertEquals(23, exopta.getIntValue("N_SEC"));
        assertEquals(3, exopta.getIntValue("N_SEG"));
        assertEquals("023487", exopta.getFieldValue("MAX_LP_SEG"));
        assertEquals("+67.3", exopta.getFieldValue("SUN_EL"));
        assertEquals("349.2", exopta.getFieldValue("SUN_AZ"));
    }
}
