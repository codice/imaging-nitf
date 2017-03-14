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
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import static javax.xml.bind.DatatypeConverter.parseHexBinary;
import org.codice.imaging.nitf.core.impl.SlottedParseStrategy;
import org.codice.imaging.nitf.core.common.NitfFormatException;
import org.codice.imaging.nitf.core.common.impl.NitfInputStreamReader;
import org.codice.imaging.nitf.core.common.NitfReader;
import org.codice.imaging.nitf.core.header.NitfHeader;
import org.codice.imaging.nitf.core.header.impl.NitfParser;
import org.codice.imaging.nitf.core.image.ImageSegment;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.codice.imaging.nitf.core.tre.Tre;
import org.codice.imaging.nitf.core.tre.TreGroup;
import org.codice.imaging.nitf.core.tre.TreSource;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * This checks the parsing of ENGRDA TRE.
 */
public class ENGRDA_Test extends SharedTreTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    public ENGRDA_Test() {
    }

    @Test
    public void testGreen2007Parse() throws NitfFormatException {
        SlottedParseStrategy parseStrategy = new SlottedParseStrategy();
        NitfReader reader = new NitfInputStreamReader(new BufferedInputStream(getInputStream()));
        NitfParser.parse(reader, parseStrategy);

        NitfHeader nitfHeader = parseStrategy.getNitfHeader();
        assertEquals(1, parseStrategy.getDataSource().getImageSegments().size());
        assertEquals(0, parseStrategy.getDataSource().getGraphicSegments().size());
        assertEquals(0, parseStrategy.getDataSource().getSymbolSegments().size());
        assertEquals(0, parseStrategy.getDataSource().getLabelSegments().size());
        assertEquals(0, parseStrategy.getDataSource().getTextSegments().size());
        assertEquals(0, parseStrategy.getDataSource().getDataExtensionSegments().size());

        assertEquals(0, nitfHeader.getTREsRawStructure().getUniqueNamesOfTRE().size());

        ImageSegment imageSegment = parseStrategy.getDataSource().getImageSegments().get(0);
        assertEquals(2, imageSegment.getTREsRawStructure().getTREs().size());

        List<Tre> engrdaTres = imageSegment.getTREsRawStructure().getTREsWithName("ENGRDA");
        assertEquals(1, engrdaTres.size());
        Tre engrdaTre = engrdaTres.get(0);
        // Check it parsed - raw data only used for unparsed TREs.
        assertNull(engrdaTre.getRawData());
        // Check values
        assertEquals("ENGRDA", engrdaTre.getName());
        assertEquals(3, engrdaTre.getEntries().size());
        assertEquals("LAIR                ", engrdaTre.getFieldValue("RESRC"));
        assertEquals(16, engrdaTre.getIntValue("RECNT"));
        assertEquals(16, engrdaTre.getEntry("RECORDS").getGroups().size());
        TreGroup group15 = engrdaTre.getEntry("RECORDS").getGroups().get(15);
        assertEquals("milliseconds", group15.getFieldValue("ENGLBL"));
        assertEquals("(Group)", group15.toString());
        assertEquals(3, group15.getIntValue("ENGMTXC"));
        assertEquals(1, group15.getIntValue("ENGMTXR"));
        assertEquals("A", group15.getFieldValue("ENGTYP"));
        assertEquals(1, group15.getIntValue("ENGDTS"));
        assertEquals("NA", group15.getFieldValue("ENGDATU"));
        assertEquals("170", group15.getFieldValue("ENGDATA"));
    }

    private InputStream getInputStream() {
        final String greenFile = "/Green2007/TimeStep103243.ntf.r0";

        assertNotNull("Test file missing", getClass().getResource(greenFile));

        return getClass().getResourceAsStream(greenFile);
    }

    @Test
    public void BuildENGRDA() throws NitfFormatException {
        Tre engrda = TreFactory.getDefault("ENGRDA", TreSource.UserDefinedHeaderData);
        assertNotNull(engrda);
        engrda.add(new TreEntryImpl("RESRC", "GEOMOS", "string"));
        engrda.add(new TreEntryImpl("RECNT", "1", "integer"));
        TreEntryImpl records = new TreEntryImpl("RECORDS");
        engrda.add(records);
        TreGroup record0 = new TreGroupImpl();
        record0.add(new TreEntryImpl("ENGLN", "11", "integer"));
        record0.add(new TreEntryImpl("ENGLBL", "TEMPERATURE", "string"));
        record0.add(new TreEntryImpl("ENGMTXC", "3", "integer"));
        record0.add(new TreEntryImpl("ENGMTXR", "1", "integer"));
        record0.add(new TreEntryImpl("ENGTYP", "A", "string"));
        record0.add(new TreEntryImpl("ENGDTS", "1", "integer"));
        record0.add(new TreEntryImpl("ENGDATU", "NA", "string"));
        record0.add(new TreEntryImpl("ENGDATC", "3", "integer"));
        record0.add(new TreEntryImpl("ENGDATA", "374", "string"));
        records.addGroup(record0);

        TreParser parser = new TreParser();
        byte[] serialisedTre = parser.serializeTRE(engrda);
        assertNotNull(serialisedTre);
        assertEquals("ENGRDA", engrda.getName());
        assertArrayEquals("GEOMOS              00111TEMPERATURE00030001A1NA00000003374".getBytes(), serialisedTre);
    }

    @Test
    public void AppNExample1() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write("ENGRDA00125YOUR_SENSOR_ID....  00305TEMP100010001I2tC00000001".getBytes(StandardCharsets.ISO_8859_1));
        baos.write(parseHexBinary("0125"));
        baos.write("05TEMP200010001R4tK00000001".getBytes(StandardCharsets.ISO_8859_1));
        baos.write(parseHexBinary("03271276"));
        baos.write("10TEMP3 Wall00100001A1NA0000001010.7 DEG C".getBytes(StandardCharsets.ISO_8859_1));

        Tre engrda = parseTRE(baos.toByteArray(), baos.toByteArray().length, "ENGRDA");
        assertEquals("YOUR_SENSOR_ID....  ", engrda.getFieldValue("RESRC"));
        assertEquals(3, engrda.getIntValue("RECNT"));
        assertEquals(3, engrda.getEntry("RECORDS").getGroups().size());
        TreGroup group0 = engrda.getEntry("RECORDS").getGroups().get(0);
        assertEquals(5, group0.getIntValue("ENGLN"));
        assertEquals("TEMP1", group0.getFieldValue("ENGLBL"));
        assertEquals(1, group0.getIntValue("ENGMTXC"));
        assertEquals(1, group0.getIntValue("ENGMTXR"));
        assertEquals("I", group0.getFieldValue("ENGTYP"));
        assertEquals(2, group0.getIntValue("ENGDTS"));
        assertEquals("tC", group0.getFieldValue("ENGDATU"));
        assertEquals(1, group0.getIntValue("ENGDATC"));
        assertEquals(1, group0.getEntry("ENGDATA").getGroups().size());
        assertEquals(1, group0.getEntry("ENGDATA").getGroups().get(0).getEntries().size());
        assertEquals(0x0125, group0.getEntry("ENGDATA").getGroups().get(0).getIntValue("ENGDATA"));

        TreGroup group1 = engrda.getEntry("RECORDS").getGroups().get(1);
        assertEquals(5, group1.getIntValue("ENGLN"));
        assertEquals("TEMP2", group1.getFieldValue("ENGLBL"));
        assertEquals(1, group1.getIntValue("ENGMTXC"));
        assertEquals(1, group1.getIntValue("ENGMTXR"));
        assertEquals("R", group1.getFieldValue("ENGTYP"));
        assertEquals(4, group1.getIntValue("ENGDTS"));
        assertEquals("tK", group1.getFieldValue("ENGDATU"));
        assertEquals(1, group1.getIntValue("ENGDATC"));
        assertEquals(1, group1.getEntry("ENGDATA").getGroups().size());
        assertEquals(1, group1.getEntry("ENGDATA").getGroups().get(0).getEntries().size());
        assertArrayEquals(parseHexBinary("03271276"), group1.getEntry("ENGDATA").getGroups().get(0).getFieldValue("ENGDATA").getBytes(StandardCharsets.ISO_8859_1));

        TreGroup group2 = engrda.getEntry("RECORDS").getGroups().get(2);
        assertEquals(10, group2.getIntValue("ENGLN"));
        assertEquals("TEMP3 Wall", group2.getFieldValue("ENGLBL"));
        assertEquals(10, group2.getIntValue("ENGMTXC"));
        assertEquals(1, group2.getIntValue("ENGMTXR"));
        assertEquals("A", group2.getFieldValue("ENGTYP"));
        assertEquals(1, group2.getIntValue("ENGDTS"));
        assertEquals("NA", group2.getFieldValue("ENGDATU"));
        assertEquals(10, group2.getIntValue("ENGDATC"));
        assertEquals("10.7 DEG C", group2.getFieldValue("ENGDATA"));
    }

    @Test
    public void AppNExample2() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write("ENGRDA00098YOUR_SENSOR_ID....  00211STB MTX 3x200030002I1NA00000006".getBytes(StandardCharsets.ISO_8859_1));
        baos.write(parseHexBinary("01"));
        baos.write(parseHexBinary("25"));
        baos.write(parseHexBinary("37"));
        baos.write(parseHexBinary("27"));
        baos.write(parseHexBinary("12"));
        baos.write(parseHexBinary("76"));
        baos.write("11temps a b c00030001I1tC00000003".getBytes(StandardCharsets.ISO_8859_1));
        baos.write(parseHexBinary("37"));
        baos.write(parseHexBinary("28"));
        baos.write(parseHexBinary("26"));

        Tre engrda = parseTRE(baos.toByteArray(), baos.toByteArray().length, "ENGRDA");
        assertEquals("YOUR_SENSOR_ID....  ", engrda.getFieldValue("RESRC"));
        assertEquals(2, engrda.getIntValue("RECNT"));
        assertEquals(2, engrda.getEntry("RECORDS").getGroups().size());
        TreGroup group0 = engrda.getEntry("RECORDS").getGroups().get(0);
        assertEquals(11, group0.getIntValue("ENGLN"));
        assertEquals("STB MTX 3x2", group0.getFieldValue("ENGLBL"));
        assertEquals(3, group0.getIntValue("ENGMTXC"));
        assertEquals(2, group0.getIntValue("ENGMTXR"));
        assertEquals("I", group0.getFieldValue("ENGTYP"));
        assertEquals(1, group0.getIntValue("ENGDTS"));
        assertEquals("NA", group0.getFieldValue("ENGDATU"));
        assertEquals(6, group0.getIntValue("ENGDATC"));
        assertEquals(6, group0.getEntry("ENGDATA").getGroups().size());
        assertEquals(1, group0.getEntry("ENGDATA").getGroups().get(0).getEntries().size());
        assertEquals(0x01, group0.getEntry("ENGDATA").getGroups().get(0).getIntValue("ENGDATA"));
        assertEquals(1, group0.getEntry("ENGDATA").getGroups().get(1).getEntries().size());
        assertEquals(0x25, group0.getEntry("ENGDATA").getGroups().get(1).getIntValue("ENGDATA"));
        assertEquals(1, group0.getEntry("ENGDATA").getGroups().get(2).getEntries().size());
        assertEquals(0x37, group0.getEntry("ENGDATA").getGroups().get(2).getIntValue("ENGDATA"));
        assertEquals(1, group0.getEntry("ENGDATA").getGroups().get(3).getEntries().size());
        assertEquals(0x27, group0.getEntry("ENGDATA").getGroups().get(3).getIntValue("ENGDATA"));
        assertEquals(1, group0.getEntry("ENGDATA").getGroups().get(4).getEntries().size());
        assertEquals(0x12, group0.getEntry("ENGDATA").getGroups().get(4).getIntValue("ENGDATA"));
        assertEquals(1, group0.getEntry("ENGDATA").getGroups().get(5).getEntries().size());
        assertEquals(0x76, group0.getEntry("ENGDATA").getGroups().get(5).getIntValue("ENGDATA"));
        TreGroup group1 = engrda.getEntry("RECORDS").getGroups().get(1);
        assertEquals(11, group1.getIntValue("ENGLN"));
        assertEquals("temps a b c", group1.getFieldValue("ENGLBL"));
        assertEquals(3, group1.getIntValue("ENGMTXC"));
        assertEquals(1, group1.getIntValue("ENGMTXR"));
        assertEquals("I", group1.getFieldValue("ENGTYP"));
        assertEquals(1, group1.getIntValue("ENGDTS"));
        assertEquals("tC", group1.getFieldValue("ENGDATU"));
        assertEquals(3, group1.getIntValue("ENGDATC"));
        assertEquals(3, group1.getEntry("ENGDATA").getGroups().size());
        assertEquals(1, group1.getEntry("ENGDATA").getGroups().get(0).getEntries().size());
        assertEquals(0x37, group1.getEntry("ENGDATA").getGroups().get(0).getIntValue("ENGDATA"));
        assertEquals(1, group1.getEntry("ENGDATA").getGroups().get(1).getEntries().size());
        assertEquals(0x28, group1.getEntry("ENGDATA").getGroups().get(1).getIntValue("ENGDATA"));
        assertEquals(1, group1.getEntry("ENGDATA").getGroups().get(2).getEntries().size());
        assertEquals(0x26, group1.getEntry("ENGDATA").getGroups().get(2).getIntValue("ENGDATA"));
    }

    @Test
    public void AppNExample2SignedModification() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write("ENGRDA00098YOUR_SENSOR_ID....  00211STB MTX 3x200030002S1NA00000006".getBytes(StandardCharsets.ISO_8859_1));
        baos.write(parseHexBinary("D1"));
        baos.write(parseHexBinary("E5"));
        baos.write(parseHexBinary("37"));
        baos.write(parseHexBinary("F7"));
        baos.write(parseHexBinary("12"));
        baos.write(parseHexBinary("76"));
        baos.write("11temps a b c00030001I1tC00000003".getBytes(StandardCharsets.ISO_8859_1));
        baos.write(parseHexBinary("37"));
        baos.write(parseHexBinary("28"));
        baos.write(parseHexBinary("26"));

        Tre engrda = parseTRE(baos.toByteArray(), baos.toByteArray().length, "ENGRDA");
        assertEquals("YOUR_SENSOR_ID....  ", engrda.getFieldValue("RESRC"));
        assertEquals(2, engrda.getIntValue("RECNT"));
        assertEquals(2, engrda.getEntry("RECORDS").getGroups().size());
        TreGroup group0 = engrda.getEntry("RECORDS").getGroups().get(0);
        assertEquals(11, group0.getIntValue("ENGLN"));
        assertEquals("STB MTX 3x2", group0.getFieldValue("ENGLBL"));
        assertEquals(3, group0.getIntValue("ENGMTXC"));
        assertEquals(2, group0.getIntValue("ENGMTXR"));
        assertEquals("S", group0.getFieldValue("ENGTYP"));
        assertEquals(1, group0.getIntValue("ENGDTS"));
        assertEquals("NA", group0.getFieldValue("ENGDATU"));
        assertEquals(6, group0.getIntValue("ENGDATC"));
        assertEquals(6, group0.getEntry("ENGDATA").getGroups().size());
        assertEquals(1, group0.getEntry("ENGDATA").getGroups().get(0).getEntries().size());
        assertEquals(0xD1, group0.getEntry("ENGDATA").getGroups().get(0).getIntValue("ENGDATA"));
        assertEquals(1, group0.getEntry("ENGDATA").getGroups().get(1).getEntries().size());
        assertEquals(0xE5, group0.getEntry("ENGDATA").getGroups().get(1).getIntValue("ENGDATA"));
        assertEquals(1, group0.getEntry("ENGDATA").getGroups().get(2).getEntries().size());
        assertEquals(0x37, group0.getEntry("ENGDATA").getGroups().get(2).getIntValue("ENGDATA"));
        assertEquals(1, group0.getEntry("ENGDATA").getGroups().get(3).getEntries().size());
        assertEquals(0xF7, group0.getEntry("ENGDATA").getGroups().get(3).getIntValue("ENGDATA"));
        assertEquals(1, group0.getEntry("ENGDATA").getGroups().get(4).getEntries().size());
        assertEquals(0x12, group0.getEntry("ENGDATA").getGroups().get(4).getIntValue("ENGDATA"));
        assertEquals(1, group0.getEntry("ENGDATA").getGroups().get(5).getEntries().size());
        assertEquals(0x76, group0.getEntry("ENGDATA").getGroups().get(5).getIntValue("ENGDATA"));
        TreGroup group1 = engrda.getEntry("RECORDS").getGroups().get(1);
        assertEquals(11, group1.getIntValue("ENGLN"));
        assertEquals("temps a b c", group1.getFieldValue("ENGLBL"));
        assertEquals(3, group1.getIntValue("ENGMTXC"));
        assertEquals(1, group1.getIntValue("ENGMTXR"));
        assertEquals("I", group1.getFieldValue("ENGTYP"));
        assertEquals(1, group1.getIntValue("ENGDTS"));
        assertEquals("tC", group1.getFieldValue("ENGDATU"));
        assertEquals(3, group1.getIntValue("ENGDATC"));
        assertEquals(3, group1.getEntry("ENGDATA").getGroups().size());
        assertEquals(1, group1.getEntry("ENGDATA").getGroups().get(0).getEntries().size());
        assertEquals(0x37, group1.getEntry("ENGDATA").getGroups().get(0).getIntValue("ENGDATA"));
        assertEquals(1, group1.getEntry("ENGDATA").getGroups().get(1).getEntries().size());
        assertEquals(0x28, group1.getEntry("ENGDATA").getGroups().get(1).getIntValue("ENGDATA"));
        assertEquals(1, group1.getEntry("ENGDATA").getGroups().get(2).getEntries().size());
        assertEquals(0x26, group1.getEntry("ENGDATA").getGroups().get(2).getIntValue("ENGDATA"));
    }

    @Test
    public void AppNExample3() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write("ENGRDA00079YOUR_SENSOR_ID....  00112Sta Temp 1-300220001A1tC00000022274.6, 327.65, 300.53\n".getBytes(StandardCharsets.ISO_8859_1));

        Tre engrda = parseTRE(baos.toByteArray(), baos.toByteArray().length, "ENGRDA");
        assertEquals("YOUR_SENSOR_ID....  ", engrda.getFieldValue("RESRC"));
        assertEquals(1, engrda.getIntValue("RECNT"));
        assertEquals(1, engrda.getEntry("RECORDS").getGroups().size());
        TreGroup group0 = engrda.getEntry("RECORDS").getGroups().get(0);
        assertEquals(12, group0.getIntValue("ENGLN"));
        assertEquals("Sta Temp 1-3", group0.getFieldValue("ENGLBL"));
        assertEquals(22, group0.getIntValue("ENGMTXC"));
        assertEquals(1, group0.getIntValue("ENGMTXR"));
        assertEquals("A", group0.getFieldValue("ENGTYP"));
        assertEquals(1, group0.getIntValue("ENGDTS"));
        assertEquals("tC", group0.getFieldValue("ENGDATU"));
        assertEquals(22, group0.getIntValue("ENGDATC"));
        assertEquals("274.6, 327.65, 300.53\n", group0.getFieldValue("ENGDATA"));
    }
}
