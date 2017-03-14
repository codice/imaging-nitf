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
import static org.junit.Assert.*;

import org.codice.imaging.nitf.core.tre.Tre;
import org.codice.imaging.nitf.core.tre.TreCollection;
import org.codice.imaging.nitf.core.tre.TreSource;
import org.junit.Test;

/**
 * Tests for SENSRA TRE parsing.
 *
 * This TRE is described in STDI-0002 Annex E, Section 3.14.1.
 *
 */
public class SENSRA_Test {

    public SENSRA_Test() {
    }

    @Test
    public void SimpleSENSRA() throws Exception {
        InputStream inputStream = new ByteArrayInputStream("SENSRA00132                CA236    -10.391540+126.388075G+01630m01599-27.899-000.059+134.335+01.143-004.043072.4             00003m           ".getBytes());
        BufferedInputStream bufferedStream = new BufferedInputStream(inputStream);
        NitfReader nitfReader = new NitfInputStreamReader(bufferedStream);
        TreCollectionParser parser = new TreCollectionParser();
        TreCollection parseResult = parser.parse(nitfReader, 143, TreSource.ImageExtendedSubheaderData);
        assertEquals(1, parseResult.getTREs().size());
        Tre sensra = parseResult.getTREsWithName("SENSRA").get(0);
        assertNotNull(sensra);
        assertNull(sensra.getRawData());
        assertEquals(24, sensra.getEntries().size());
        assertEquals("        ", sensra.getFieldValue("REF_ROW"));
        assertEquals("        ", sensra.getFieldValue("REF_COL"));
        assertEquals("CA236 ", sensra.getFieldValue("SENSOR_MODEL"));
        assertEquals("   ", sensra.getFieldValue("SENSOR_MOUNT"));
        assertEquals("-10.391540+126.388075", sensra.getFieldValue("SENSOR_LOC"));
        assertEquals("G", sensra.getFieldValue("SENSOR_ALT_SOURCE"));
        assertEquals("+01630", sensra.getFieldValue("SENSOR_ALT"));
        assertEquals("m", sensra.getFieldValue("SENSOR_ALT_UNIT"));
        assertEquals("01599", sensra.getFieldValue("SENSOR_AGL"));
        assertEquals("-27.899", sensra.getFieldValue("SENSOR_PITCH"));
        assertEquals("-000.059", sensra.getFieldValue("SENSOR_ROLL"));
        assertEquals("+134.335", sensra.getFieldValue("SENSOR_YAW"));
        assertEquals("+01.143", sensra.getFieldValue("PLATFORM_PITCH"));
        assertEquals("-004.043", sensra.getFieldValue("PLATFORM_ROLL"));
        assertEquals("072.4", sensra.getFieldValue("PLATFORM_HDG"));
        assertEquals(" ", sensra.getFieldValue("GROUND_SPD_SOURCE"));
        assertEquals("      ", sensra.getFieldValue("GROUND_SPD"));
        assertEquals(" ", sensra.getFieldValue("GROUND_SPD_UNIT"));
        assertEquals("     ", sensra.getFieldValue("GROUND_TRACK"));
        assertEquals("00003", sensra.getFieldValue("VERT_VEL"));
        assertEquals("m", sensra.getFieldValue("VERT_VEL_UNIT"));
        assertEquals("    ", sensra.getFieldValue("SWATH_FRAMES"));
        assertEquals("    ", sensra.getFieldValue("N_SWATHS"));
        assertEquals("   ", sensra.getFieldValue("SPOT_NUM"));


    }
}
