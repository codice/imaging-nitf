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
import java.nio.charset.StandardCharsets;
import org.codice.imaging.nitf.core.common.NitfFormatException;
import org.codice.imaging.nitf.core.common.NitfInputStreamReader;
import org.codice.imaging.nitf.core.common.NitfReader;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * This checks the parsing of HISTOA TRE.
 */
public class HISTOA_Test {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    public HISTOA_Test() {
    }

    @Test
    public void basicHISTOA() throws NitfFormatException {
        InputStream inputStream = new ByteArrayInputStream("HISTOA00276WV02AA              NONE00000000NONE 000220160718085758DG        N001042216011INTNONE000000 0 00000011INTJ2VLC0000020160720173751NDL-W     NCL1204   1Transcode to EPJE+Quality Layer Parsing+Rset extraction/generation              11INTJ2VL000000 0 00100.031200011INTJ2VL000000".getBytes(StandardCharsets.ISO_8859_1));
        BufferedInputStream bufferedStream = new BufferedInputStream(inputStream);
        NitfReader nitfReader = new NitfInputStreamReader(bufferedStream);
        TreCollectionParser parser = new TreCollectionParser();
        TreCollection parseResult = parser.parse(nitfReader, 287, TreSource.ImageExtendedSubheaderData);
        assertEquals(1, parseResult.getTREs().size());
        Tre histoa = parseResult.getTREsWithName("HISTOA").get(0);
        assertNotNull(histoa);
        assertEquals("WV02AA", histoa.getFieldValue("SYSTYPE").trim());
        assertEquals("NONE00000000", histoa.getFieldValue("PC"));
        assertEquals("NONE", histoa.getFieldValue("PE"));
        assertEquals(" ", histoa.getFieldValue("REMAP_FLAG"));
        assertEquals("00", histoa.getFieldValue("LUTID"));
        assertEquals(2, histoa.getIntValue("NEVENTS"));
        TreGroup event1 = histoa.getEntry("EVENT").getGroups().get(0);
        assertEquals("20160718085758", event1.getFieldValue("PDATE"));
        assertEquals("DG", event1.getFieldValue("PSITE").trim());
        assertEquals("N001042216", event1.getFieldValue("PAS"));
        assertEquals(0, event1.getIntValue("NIPCOM"));
        assertEquals(11, event1.getIntValue("IBPP"));
        assertEquals("INT", event1.getFieldValue("IPVTYPE"));
        assertEquals("NONE000000", event1.getFieldValue("INBWC"));
        assertEquals(" ", event1.getFieldValue("DISP_FLAG"));
        assertEquals("0", event1.getFieldValue("ROT_FLAG"));
        assertEquals(" ", event1.getFieldValue("ASYM_FLAG"));
        assertEquals("0", event1.getFieldValue("PROJ_FLAG"));
        assertEquals("0", event1.getFieldValue("SHARP_FLAG"));
        assertEquals("0", event1.getFieldValue("MAG_FLAG"));
        assertEquals("0", event1.getFieldValue("DRA_FLAG"));
        assertEquals("0", event1.getFieldValue("TTC_FLAG"));
        assertEquals("0", event1.getFieldValue("DEVLUT_FLAG"));
        assertEquals(11, event1.getIntValue("OBPP"));
        assertEquals("INT", event1.getFieldValue("OPVTYPE"));
        assertEquals("J2VLC00000", event1.getFieldValue("OUTBWC"));

        TreGroup event2 = histoa.getEntry("EVENT").getGroups().get(1);
        assertEquals("20160720173751", event2.getFieldValue("PDATE"));
        assertEquals("NDL-W", event2.getFieldValue("PSITE").trim());
        assertEquals("NCL1204   ", event2.getFieldValue("PAS"));
        assertEquals(1, event2.getIntValue("NIPCOM"));
        TreGroup event2comment1 = event2.getEntry("IPCOM").getGroups().get(0);
        assertEquals("Transcode to EPJE+Quality Layer Parsing+Rset extraction/generation              ", event2comment1.getFieldValue("IPCOM"));
        assertEquals(11, event2.getIntValue("IBPP"));
        assertEquals("INT", event2.getFieldValue("IPVTYPE"));
        assertEquals("J2VL000000", event2.getFieldValue("INBWC"));
        assertEquals(" ", event2.getFieldValue("DISP_FLAG"));
        assertEquals("0", event2.getFieldValue("ROT_FLAG"));
        assertEquals(" ", event2.getFieldValue("ASYM_FLAG"));
        assertEquals("0", event2.getFieldValue("PROJ_FLAG"));
        assertEquals("0", event2.getFieldValue("SHARP_FLAG"));
        assertEquals("1", event2.getFieldValue("MAG_FLAG"));
        assertEquals(0.0312, event2.getDoubleValue("MAG_LEVEL"), 0.00001);
        assertEquals("0", event2.getFieldValue("DRA_FLAG"));
        assertEquals("0", event2.getFieldValue("TTC_FLAG"));
        assertEquals("0", event2.getFieldValue("DEVLUT_FLAG"));
        assertEquals(11, event2.getIntValue("OBPP"));
        assertEquals("INT", event2.getFieldValue("OPVTYPE"));
        assertEquals("J2VL000000", event2.getFieldValue("OUTBWC"));
    }

}
