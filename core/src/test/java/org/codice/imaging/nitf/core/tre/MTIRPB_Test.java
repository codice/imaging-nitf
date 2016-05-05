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

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import org.codice.imaging.nitf.core.common.NitfInputStreamReader;
import org.codice.imaging.nitf.core.common.NitfReader;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for MTIRPB TRE parsing.
 *
 * This TRE is described in STDI-0002 Annex E, Section 3.10.
 *
 */
public class MTIRPB_Test {

    @Test
    public void SimpleMTIRPB() throws Exception {
        InputStream inputStream = new ByteArrayInputStream("MTIRPB00119120010100      20160504204234352820.03S1491287.65E022000f045L-43.209.99999001-35.3082123+149.1244456000.00-02301227011W".getBytes());
        BufferedInputStream bufferedStream = new BufferedInputStream(inputStream);
        NitfReader nitfReader = new NitfInputStreamReader(bufferedStream);
        TreCollectionParser parser = new TreCollectionParser();
        TreCollection parseResult = parser.parse(nitfReader, 130, TreSource.ImageExtendedSubheaderData);
        assertEquals(1, parseResult.getTREs().size());
        Tre mtirpb = parseResult.getTREsWithName("MTIRPB").get(0);
        assertNotNull(mtirpb);
        assertEquals(15, mtirpb.getEntries().size());
        assertEquals("12", mtirpb.getFieldValue("MTI_DP"));
        assertEquals(1, mtirpb.getIntValue("MTI_PACKET_ID"));
        assertEquals(100, mtirpb.getIntValue("PATCH_NO"));
        assertEquals("     ", mtirpb.getFieldValue("WAMTI_FRAME_NO"));
        assertEquals(" ", mtirpb.getFieldValue("WAMTI_BAR_NO"));
        assertEquals("20160504204234", mtirpb.getFieldValue("DATIME"));
        assertEquals("352820.03S1491287.65E", mtirpb.getFieldValue("ACFT_LOC"));
        assertEquals(22000, mtirpb.getIntValue("ACFT_ALT"));
        assertEquals("f", mtirpb.getFieldValue("ACFT_ALT_UNIT"));
        assertEquals(45, mtirpb.getIntValue("ACFT_HEADING"));
        assertEquals("L", mtirpb.getFieldValue("MTI_LR"));
        assertEquals("-43.20", mtirpb.getFieldValue("SQUINT_ANGLE"));
        assertEquals("9.99999", mtirpb.getFieldValue("COSGRZ"));
        assertEquals(1, mtirpb.getIntValue("NO_VALID_TARGETS"));
        TreEntry targets = mtirpb.getEntry("TARGETS");
        assertEquals(1, targets.getGroups().size());
        TreGroup group0 = targets.getGroups().get(0);
        assertNotNull(group0);
        assertEquals("-35.3082123+149.1244456", group0.getFieldValue("TGT_LOC"));
        assertEquals("000.00", group0.getFieldValue("TGT_LOC_ACCY"));
        assertEquals("-023", group0.getFieldValue("TGT_VEL_R"));
        assertEquals("012", group0.getFieldValue("TGT_SPEED"));
        assertEquals("270", group0.getFieldValue("TGT_HEADING"));
        assertEquals("11", group0.getFieldValue("TGT_AMPLITUDE"));
        assertEquals("W", group0.getFieldValue("TGT_CAT"));
    }
}
