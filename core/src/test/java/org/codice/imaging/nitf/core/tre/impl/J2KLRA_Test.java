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
import java.util.List;
import org.codice.imaging.nitf.core.common.impl.NitfInputStreamReader;
import org.codice.imaging.nitf.core.common.NitfReader;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.codice.imaging.nitf.core.tre.Tre;
import org.codice.imaging.nitf.core.tre.TreCollection;
import org.codice.imaging.nitf.core.tre.TreEntry;
import org.codice.imaging.nitf.core.tre.TreGroup;
import org.codice.imaging.nitf.core.tre.TreSource;
import org.junit.Test;

/**
 * Tests for J2KLRA TRE parsing.
 */
public class J2KLRA_Test {

    public J2KLRA_Test() {
    }

    @Test
    public void BasicJ2KLRA() throws Exception {
        InputStream inputStream = new ByteArrayInputStream("J2KLRA002390050000101900000.03125000100.06250000200.12500000300.25000000400.50000000500.60000000600.70000000700.80000000800.90000000901.00000001001.10000001101.20000001201.30000001301.50000001401.70000001502.00000001602.30000001703.50000001803.900000".getBytes());
        BufferedInputStream bufferedStream = new BufferedInputStream(inputStream);
        NitfReader nitfReader = new NitfInputStreamReader(bufferedStream);
        TreCollectionParser parser = new TreCollectionParser();
        TreCollection parseResult = parser.parse(nitfReader, 250, TreSource.ImageExtendedSubheaderData);
        assertEquals(1, parseResult.getTREs().size());
        Tre j2klra = parseResult.getTREsWithName("J2KLRA").get(0);
        assertNotNull(j2klra);
        assertEquals(5, j2klra.getEntries().size());
        assertEquals("0", j2klra.getFieldValue("ORIG"));
        assertEquals("05", j2klra.getFieldValue("NLEVELS_O"));
        assertEquals("00001", j2klra.getFieldValue("NBANDS_O"));
        assertEquals("019", j2klra.getFieldValue("NLAYERS_O"));

        TreEntry layer = j2klra.getEntry("LAYER");
        assertNotNull(layer);
        List<TreGroup> layerGroups = layer.getGroups();
        assertNotNull(layerGroups);
        assertEquals(19, layerGroups.size());

        TreGroup layerGroup0 = layerGroups.get(0);
        assertNotNull(layerGroup0);
        assertEquals("000", layerGroup0.getFieldValue("LAYER_ID"));
        assertEquals("00.031250", layerGroup0.getFieldValue("BITRATE"));

        TreGroup layerGroup1 = layerGroups.get(1);
        assertNotNull(layerGroup1);
        assertEquals("001", layerGroup1.getFieldValue("LAYER_ID"));
        assertEquals("00.062500", layerGroup1.getFieldValue("BITRATE"));

        TreGroup layerGroup10 = layerGroups.get(10);
        assertNotNull(layerGroup10);
        assertEquals("010", layerGroup10.getFieldValue("LAYER_ID"));
        assertEquals("01.100000", layerGroup10.getFieldValue("BITRATE"));

        TreGroup layerGroup17 = layerGroups.get(17);
        assertNotNull(layerGroup17);
        assertEquals("017", layerGroup17.getFieldValue("LAYER_ID"));
        assertEquals("03.500000", layerGroup17.getFieldValue("BITRATE"));

        TreGroup layerGroup18 = layerGroups.get(18);
        assertNotNull(layerGroup18);
        assertEquals("018", layerGroup18.getFieldValue("LAYER_ID"));
        assertEquals("03.900000", layerGroup18.getFieldValue("BITRATE"));
    }

    @Test
    public void ReprocessedJ2KLRA() throws Exception {
        InputStream inputStream = new ByteArrayInputStream("J2KLRA002493050000101900000.03125000100.06250000200.12500000300.25000000400.50000000500.60000000600.70000000700.80000000800.90000000901.00000001001.10000001101.20000001201.30000001301.50000001401.70000001502.00000001602.30000001703.50000001803.9000000000001011".getBytes());
        BufferedInputStream bufferedStream = new BufferedInputStream(inputStream);
        NitfReader nitfReader = new NitfInputStreamReader(bufferedStream);
        TreCollectionParser parser = new TreCollectionParser();
        TreCollection parseResult = parser.parse(nitfReader, 250, TreSource.ImageExtendedSubheaderData);
        assertEquals(1, parseResult.getTREs().size());
        Tre j2klra = parseResult.getTREsWithName("J2KLRA").get(0);
        assertNotNull(j2klra);
        assertEquals(8, j2klra.getEntries().size());
        assertEquals("3", j2klra.getFieldValue("ORIG"));
        assertEquals("05", j2klra.getFieldValue("NLEVELS_O"));
        assertEquals("00001", j2klra.getFieldValue("NBANDS_O"));
        assertEquals("019", j2klra.getFieldValue("NLAYERS_O"));

        TreEntry layer = j2klra.getEntry("LAYER");
        assertNotNull(layer);
        List<TreGroup> layerGroups = layer.getGroups();
        assertNotNull(layerGroups);
        assertEquals(19, layerGroups.size());

        TreGroup layerGroup0 = layerGroups.get(0);
        assertNotNull(layerGroup0);
        assertEquals("000", layerGroup0.getFieldValue("LAYER_ID"));
        assertEquals("00.031250", layerGroup0.getFieldValue("BITRATE"));

        TreGroup layerGroup1 = layerGroups.get(1);
        assertNotNull(layerGroup1);
        assertEquals("001", layerGroup1.getFieldValue("LAYER_ID"));
        assertEquals("00.062500", layerGroup1.getFieldValue("BITRATE"));

        TreGroup layerGroup10 = layerGroups.get(10);
        assertNotNull(layerGroup10);
        assertEquals("010", layerGroup10.getFieldValue("LAYER_ID"));
        assertEquals("01.100000", layerGroup10.getFieldValue("BITRATE"));

        TreGroup layerGroup17 = layerGroups.get(17);
        assertNotNull(layerGroup17);
        assertEquals("017", layerGroup17.getFieldValue("LAYER_ID"));
        assertEquals("03.500000", layerGroup17.getFieldValue("BITRATE"));

        TreGroup layerGroup18 = layerGroups.get(18);
        assertNotNull(layerGroup18);
        assertEquals("018", layerGroup18.getFieldValue("LAYER_ID"));
        assertEquals("03.900000", layerGroup18.getFieldValue("BITRATE"));

        assertEquals("00", j2klra.getFieldValue("NLEVELS_I"));
        assertEquals("00001", j2klra.getFieldValue("NBANDS_I"));
        assertEquals("011", j2klra.getFieldValue("NLAYERS_I"));
    }
}
