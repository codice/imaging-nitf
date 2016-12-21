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
package org.codice.imaging.nitf.core.common;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import org.codice.imaging.nitf.core.tre.Tre;
import org.codice.imaging.nitf.core.tre.TreCollection;
import org.codice.imaging.nitf.core.tre.TreCollectionParser;
import org.codice.imaging.nitf.core.tre.TreSource;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Test cases for TaggedRecordExtensionHandler.
 */
public class TaggedRecordExtensionHandlerTest {

    private class TaggedRecordExtensionHandlerTestClass extends TaggedRecordExtensionHandlerImpl {

    }

    public TaggedRecordExtensionHandlerTest() {
    }

    @Test
    public void testTreFlatteningHISTOA() throws NitfFormatException {
        InputStream inputStream = new ByteArrayInputStream("HISTOA00276WV02AA              NONE00000000NONE 000220160718085758DG        N001042216011INTNONE000000 0 00000011INTJ2VLC0000020160720173751NDL-W     NCL1204   1Transcode to EPJE+Quality Layer Parsing+Rset extraction/generation              11INTJ2VL000000 0 00100.031200011INTJ2VL000000".getBytes(StandardCharsets.ISO_8859_1));
        BufferedInputStream bufferedStream = new BufferedInputStream(inputStream);
        NitfReader nitfReader = new NitfInputStreamReader(bufferedStream);
        TreCollectionParser parser = new TreCollectionParser();
        TreCollection parseResult = parser.parse(nitfReader, 287, TreSource.ImageExtendedSubheaderData);
        assertEquals(1, parseResult.getTREs().size());
        Tre histoa = parseResult.getTREsWithName("HISTOA").get(0);
        TreCollection col = new TreCollection();
        col.add(histoa);

        TaggedRecordExtensionHandlerImpl treHandler = new TaggedRecordExtensionHandlerTestClass();

        treHandler.mergeTREs(col);

        Map<String, String> flatTres = treHandler.getTREsFlat();
        assertEquals("WV02AA", flatTres.get("HISTOA_SYSTYPE"));
        assertEquals("20160718085758", flatTres.get("HISTOA_EVENT_1_PDATE"));
        assertEquals("20160720173751", flatTres.get("HISTOA_EVENT_2_PDATE"));
        assertEquals("Transcode to EPJE+Quality Layer Parsing+Rset extraction/generation", flatTres.get("HISTOA_EVENT_2_IPCOM_1").trim());
    }

    @Test
    public void testTreFlatteningThreeTREs() throws NitfFormatException, IOException {
        String testData = "MSTGTA0010100006ABC123DEF456789XYZ654321POI002The Boss.   2018111623591490231084632Z+01634m+30.482261-086.503262PIAPEB00094Maxwell                     James                       Clerk                       18310613UKMSTGTA0010100000                                                       3                   +42.462679-071.281274";

        InputStream inputStream = new ByteArrayInputStream(testData.getBytes(StandardCharsets.ISO_8859_1));
        BufferedInputStream bufferedStream = new BufferedInputStream(inputStream);
        NitfReader nitfReader = new NitfInputStreamReader(bufferedStream);
        TreCollectionParser parser = new TreCollectionParser();
        TreCollection parseResult = parser.parse(nitfReader, 329, TreSource.ImageExtendedSubheaderData);
        assertEquals(3, parseResult.getTREs().size());

        TaggedRecordExtensionHandlerImpl treHandler = new TaggedRecordExtensionHandlerTestClass();

        treHandler.mergeTREs(parseResult);

        Map<String, String> flatTres = treHandler.getTREsFlat();
        assertEquals("789XYZ654321POI", flatTres.get("MSTGTA_0_TGT_BE"));
        assertEquals("", flatTres.get("MSTGTA_1_TGT_BE"));
        assertEquals("+30.482261-086.503262", flatTres.get("MSTGTA_0_TGT_LOC"));
        assertEquals("+42.462679-071.281274", flatTres.get("MSTGTA_1_TGT_LOC"));
        assertEquals("Maxwell", flatTres.get("PIAPEB_LASTNME").trim());
        assertEquals("James", flatTres.get("PIAPEB_FIRSTNME").trim());
        assertEquals("Clerk", flatTres.get("PIAPEB_MIDNME").trim());
        assertEquals("18310613", flatTres.get("PIAPEB_DOB"));
        assertEquals("UK", flatTres.get(("PIAPEB_ASSOCTRY")));
    }
}
