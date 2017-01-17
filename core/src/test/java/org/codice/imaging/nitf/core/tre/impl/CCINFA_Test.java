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
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import javax.xml.bind.DatatypeConverter;
import org.codice.imaging.nitf.core.common.NitfReader;
import org.codice.imaging.nitf.core.common.impl.NitfInputStreamReader;
import org.codice.imaging.nitf.core.tre.Tre;
import org.codice.imaging.nitf.core.tre.TreCollection;
import org.codice.imaging.nitf.core.tre.TreGroup;
import org.codice.imaging.nitf.core.tre.TreSource;
import org.codice.imaging.nitf.core.tre.impl.TreCollectionParser;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for CCINFA TRE parsing.
 *
 * This TRE is described in STDI-0002 Annex AG (draft), Section AG.5.
 *
 */
public class CCINFA_Test {

    public CCINFA_Test() {
    }

    @Test
    public void TableAG5Partial() throws Exception {
        InputStream inputStream = new ByteArrayInputStream("CCINFA000620022RQ 17ge:GENC:3:3-5:PRI000002RQ 20as:ISO2:6:II-3:US-PR00000".getBytes());
        BufferedInputStream bufferedStream = new BufferedInputStream(inputStream);
        NitfReader nitfReader = new NitfInputStreamReader(bufferedStream);
        TreCollectionParser parser = new TreCollectionParser();
        TreCollection parseResult = parser.parse(nitfReader, 73, TreSource.ExtendedHeaderData);
        assertEquals(1, parseResult.getTREs().size());
        Tre ccinfa = parseResult.getTREsWithName("CCINFA").get(0);
        assertNotNull(ccinfa);
        assertNull(ccinfa.getRawData());
        assertEquals(2, ccinfa.getEntries().size());
        assertEquals(2, ccinfa.getIntValue("NUMCODE"));
        assertEquals(2, ccinfa.getEntry("CODES").getGroups().size());
        TreGroup group1 = ccinfa.getEntry("CODES").getGroups().get(0);
        assertEquals(2, group1.getIntValue("CODE_LEN"));
        assertEquals("RQ", group1.getFieldValue("CODE"));
        assertEquals(" ", group1.getFieldValue("EQTYPE"));
        assertEquals(17, group1.getIntValue("ESURN_LEN"));
        assertEquals("ge:GENC:3:3-5:PRI", group1.getFieldValue("ESURN"));
        assertEquals(0, group1.getIntValue("DETAIL_LEN"));
        TreGroup group2 = ccinfa.getEntry("CODES").getGroups().get(1);
        assertEquals(2, group2.getIntValue("CODE_LEN"));
        assertEquals("RQ", group2.getFieldValue("CODE"));
        assertEquals(" ", group2.getFieldValue("EQTYPE"));
        assertEquals(20, group2.getIntValue("ESURN_LEN"));
        assertEquals("as:ISO2:6:II-3:US-PR", group2.getFieldValue("ESURN"));
        assertEquals(0, group2.getIntValue("DETAIL_LEN"));
    }

    @Test
    public void TableAG5Full() throws Exception {
        String listingAG1
                = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                + "<genc:GeopoliticalEntityEntry \n"
                + "    xmlns:genc=\"http://api.nsgreg.nga.mil/schema/genc/3.0\" \n"
                + "    xmlns:genc-cmn=\"http://api.nsgreg.nga.mil/schema/genc/3.0/genc-cmn\" \n"
                + "    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" \n"
                + "    xsi:schemaLocation=\"http://api.nsgreg.nga.mil/schema/genc/3.0 http://api.nsgreg.nga.mil/schema/genc/3.0.0/genc.xsd\">\n"
                + "    <genc:encoding>\n"
                + "        <genc-cmn:char3Code>MMR</genc-cmn:char3Code>\n"
                + "        <genc-cmn:char3CodeURISet>\n"
                + "            <genc-cmn:codespaceURL>http://api.nsgreg.nga.mil/geo-political/GENC/3/3-5</genc-cmn:codespaceURL>\n"
                + "            <genc-cmn:codespaceURN>urn:us:gov:dod:nga:def:geo-political:GENC:3:3-5</genc-cmn:codespaceURN>\n"
                + "            <genc-cmn:codespaceURNBased>geo-political:GENC:3:3-5</genc-cmn:codespaceURNBased>\n"
                + "            <genc-cmn:codespaceURNBasedShort>ge:GENC:3:3-5</genc-cmn:codespaceURNBasedShort>\n"
                + "        </genc-cmn:char3CodeURISet>\n"
                + "        <genc-cmn:char2Code>MM</genc-cmn:char2Code>\n"
                + "        <genc-cmn:char2CodeURISet>\n"
                + "            <genc-cmn:codespaceURL>http://api.nsgreg.nga.mil/geo-political/GENC/2/3-5</genc-cmn:codespaceURL>\n"
                + "            <genc-cmn:codespaceURN>urn:us:gov:dod:nga:def:geo-political:GENC:2:3-5</genc-cmn:codespaceURN>\n"
                + "            <genc-cmn:codespaceURNBased>geo-political:GENC:2:3-5</genc-cmn:codespaceURNBased>\n"
                + "            <genc-cmn:codespaceURNBasedShort>ge:GENC:2:3-5</genc-cmn:codespaceURNBasedShort>\n"
                + "        </genc-cmn:char2CodeURISet>\n"
                + "        <genc-cmn:numericCode>104</genc-cmn:numericCode>\n"
                + "        <genc-cmn:numericCodeURISet>\n"
                + "            <genc-cmn:codespaceURL>http://api.nsgreg.nga.mil/geo-political/GENC/n/3-5</genc-cmn:codespaceURL>\n"
                + "            <genc-cmn:codespaceURN>urn:us:gov:dod:nga:def:geo-political:GENC:n:3-5</genc-cmn:codespaceURN>\n"
                + "            <genc-cmn:codespaceURNBased>geo-political:GENC:n:3-5</genc-cmn:codespaceURNBased>\n"
                + "            <genc-cmn:codespaceURNBasedShort>ge:GENC:n:3-5</genc-cmn:codespaceURNBasedShort>\n"
                + "        </genc-cmn:numericCodeURISet>\n"
                + "    </genc:encoding>\n"
                + "    <genc:name><![CDATA[BURMA]]></genc:name>\n"
                + "    <genc:shortName><![CDATA[Burma]]></genc:shortName>\n"
                + "    <genc:fullName><![CDATA[Union of Burma]]></genc:fullName>\n"
                + "    <genc:gencStatus>exception</genc:gencStatus>\n"
                + "    <genc:entryDate>2016-09-30</genc:entryDate>\n"
                + "    <genc:entryType>unchanged</genc:entryType>\n"
                + "    <genc:usRecognition>independent</genc:usRecognition>\n"
                + "    <genc:entryNotesOnNaming><![CDATA[\n"
                + "        The GENC Standard specifies the name \"BURMA\" where instead ISO 3166-1 specifies \"MYANMAR\"; GENC specifies the short name \"Burma\" where instead ISO 3166-1 specifies \"Myanmar\"; and GENC specifies the full name \"Union of Burma\" where instead ISO 3166-1 specifies \"the Republic of the Union of Myanmar\". The GENC Standard specifies the local short name for 'my'/'mya' as \"Myanma Naingngandaw\" where instead ISO 3166-1 specifies \"Myanma\".\n"
                + "        ]]></genc:entryNotesOnNaming>\n"
                + "    <genc:division codeSpace=\"as:GENC:6:3-5\">MM-01</genc:division>\n"
                + "    <genc:division codeSpace=\"as:GENC:6:3-5\">MM-02</genc:division>\n"
                + "    <genc:division codeSpace=\"as:GENC:6:3-5\">MM-03</genc:division>\n"
                + "    <genc:division codeSpace=\"as:GENC:6:3-5\">MM-04</genc:division>\n"
                + "    <genc:division codeSpace=\"as:GENC:6:3-5\">MM-05</genc:division>\n"
                + "    <genc:division codeSpace=\"as:GENC:6:3-5\">MM-06</genc:division>\n"
                + "    <genc:division codeSpace=\"as:GENC:6:3-5\">MM-07</genc:division>\n"
                + "    <genc:division codeSpace=\"as:GENC:6:3-5\">MM-11</genc:division>\n"
                + "    <genc:division codeSpace=\"as:GENC:6:3-5\">MM-12</genc:division>\n"
                + "    <genc:division codeSpace=\"as:GENC:6:3-5\">MM-13</genc:division>\n"
                + "    <genc:division codeSpace=\"as:GENC:6:3-5\">MM-14</genc:division>\n"
                + "    <genc:division codeSpace=\"as:GENC:6:3-5\">MM-15</genc:division>\n"
                + "    <genc:division codeSpace=\"as:GENC:6:3-5\">MM-16</genc:division>\n"
                + "    <genc:division codeSpace=\"as:GENC:6:3-5\">MM-17</genc:division>\n"
                + "    <genc:division codeSpace=\"as:GENC:6:3-5\">MM-18</genc:division>\n"
                + "    <genc:localShortName>\n"
                + "        <genc:name><![CDATA[Myanma Naingngandaw]]></genc:name>\n"
                + "        <genc:iso6393Char3Code>mya</genc:iso6393Char3Code>\n"
                + "    </genc:localShortName>\n"
                + "</genc:GeopoliticalEntityEntry>";
        String listingAG2 = "H4sIAAAAAAAAA72XXW+iQBSG7/dXsNzzMdDadqI0ru0mJuom0m520/RiFkacBAbCQK3/fgeQT4XFstZEEzjnfY7D+2aA8f275wpvOGTEpxMRyKooYGr5NqHORHx++i7divfGlzFhPpybP2Z+TKNw/5j8CF8E/uFyyiAvT8RtFAVQUVBAZMqcEDsydZDsEVdh1hZ7SHE4WdFlVeHtYlWeFM7QH2kly6PnzM81NdA7IwVjt9vJO132Q0fRVBUov5YLM2VIhLIIUQvnUkZgRl/4ForSi3jWdRB6d2f98juzRSOdnZqSm5WdSk/nq4PWFoX6zLexsVyux8qJ812i5/XcxFHZ0mjjHSxAFm9bGO1rcLAvBb5LImIhV+EJ0sFoJAFFV37O59JN9U9Vgf8eujLikMKY+++/Qdu3IZ8IbbyBtYmwmAh12DFx1WfiN8SwbXyMn2n7DjG3fhjxSQm+FzkTlG6e8LppZ91x7RCThlLrSol28ZRon54S7cIp6eYPSUkPcndKTtpZjqexh0NipYEA6lVFXK10Cy8aFvrpYaEXDks3f0hYepDbw9Ji6Fg5vh+lt6hiC3p0sYdpZEYoipnhbzbEIsh191PGiEOxnRFa2us87TzeifaSV1lPP2KroMJEHjbGX19mD9On6cvy93S1nK5fX40DIKmWzSy51Ku6Yo+oh8JCUbaUsk3sunVVtMXCGgfxH5dYgr8RkuNnyh9KkoMmspCXxJgusIPcw2oItXGA+Q+NMkW9XH0G4Y+DDyjChqaCkaTeSbqapyGvNLqf9gE2Ysq9oU5+WctC2WyTN5I8mgpJPs0knxMRsSTEGhzBOQ/xtchvW5IKMkbe/xGENhyhD0dcDUdcD0eMhiNuBiPAcFPBcFPBcFPBcFPBcFPBcFPBfzD1thXh8lco16zvdLUddcF3ixg5WJvxPd3w9uVuWq+0K/WDEh1L9RZpc1M+sYsX/fw70u9SUvaCVUw6qlTumc11Z2cbr9vGXwyTCxWrDwAA";
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write("CCINFA050440062RQ 17ge:GENC:3:3-5:PRI000002RQ 20as:ISO2:6:II-3:US-PR000002BM 17ge:GENC:3:3-5:MMR04108 ".getBytes(StandardCharsets.ISO_8859_1));
        baos.write(listingAG1.getBytes(StandardCharsets.ISO_8859_1));
        baos.write("3MMR 19ge:ISO1:3:VII-7:MMR00756G".getBytes(StandardCharsets.ISO_8859_1));
        byte[] gzipData = DatatypeConverter.parseBase64Binary(listingAG2);

        java.io.ByteArrayInputStream bytein = new java.io.ByteArrayInputStream(gzipData);
        java.util.zip.GZIPInputStream gzin = new java.util.zip.GZIPInputStream(bytein);
        java.io.ByteArrayOutputStream byteout = new java.io.ByteArrayOutputStream();

        int res = 0;
        byte buf[] = new byte[1024];
        while (res >= 0) {
            res = gzin.read(buf, 0, buf.length);
            if (res > 0) {
                byteout.write(buf, 0, res);
            }
        }
        byte uncompressed[] = byteout.toByteArray();
        String str = new String(uncompressed, StandardCharsets.ISO_8859_1);
        assertTrue(str.length() > 0);
        assertTrue(str.startsWith("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"));

        baos.write(gzipData);
        baos.write("2S1 19ge:GENC:3:3-alt:SCT000002YYC16gg:1059:2:ed9:3E00000".getBytes(StandardCharsets.ISO_8859_1));
        byte[] bytes = baos.toByteArray();
        InputStream inputStream = new ByteArrayInputStream(bytes);
        BufferedInputStream bufferedStream = new BufferedInputStream(inputStream);
        NitfReader nitfReader = new NitfInputStreamReader(bufferedStream);
        TreCollectionParser parser = new TreCollectionParser();
        TreCollection parseResult = parser.parse(nitfReader, 5055, TreSource.ExtendedHeaderData);
        assertEquals(1, parseResult.getTREs().size());
        Tre ccinfa = parseResult.getTREsWithName("CCINFA").get(0);
        assertNotNull(ccinfa);
        assertNull(ccinfa.getRawData());
        assertEquals(2, ccinfa.getEntries().size());
        assertEquals(6, ccinfa.getIntValue("NUMCODE"));
        assertEquals(6, ccinfa.getEntry("CODES").getGroups().size());

        TreGroup group1 = ccinfa.getEntry("CODES").getGroups().get(0);
        assertEquals(2, group1.getIntValue("CODE_LEN"));
        assertEquals("RQ", group1.getFieldValue("CODE"));
        assertEquals(" ", group1.getFieldValue("EQTYPE"));
        assertEquals(17, group1.getIntValue("ESURN_LEN"));
        assertEquals("ge:GENC:3:3-5:PRI", group1.getFieldValue("ESURN"));
        assertEquals(0, group1.getIntValue("DETAIL_LEN"));

        TreGroup group2 = ccinfa.getEntry("CODES").getGroups().get(1);
        assertEquals(2, group2.getIntValue("CODE_LEN"));
        assertEquals("RQ", group2.getFieldValue("CODE"));
        assertEquals(" ", group2.getFieldValue("EQTYPE"));
        assertEquals(20, group2.getIntValue("ESURN_LEN"));
        assertEquals("as:ISO2:6:II-3:US-PR", group2.getFieldValue("ESURN"));
        assertEquals(0, group2.getIntValue("DETAIL_LEN"));

        TreGroup group3 = ccinfa.getEntry("CODES").getGroups().get(2);
        assertEquals(2, group3.getIntValue("CODE_LEN"));
        assertEquals("BM", group3.getFieldValue("CODE"));
        assertEquals(" ", group3.getFieldValue("EQTYPE"));
        assertEquals(17, group3.getIntValue("ESURN_LEN"));
        assertEquals("ge:GENC:3:3-5:MMR", group3.getFieldValue("ESURN"));
        assertEquals(4108, group3.getIntValue("DETAIL_LEN"));
        assertEquals(" ", group3.getFieldValue("DETAIL_CMPR"));
        assertEquals(listingAG1, group3.getFieldValue("DETAIL"));

        TreGroup group4 = ccinfa.getEntry("CODES").getGroups().get(3);
        assertEquals(3, group4.getIntValue("CODE_LEN"));
        assertEquals("MMR", group4.getFieldValue("CODE"));
        assertEquals(" ", group4.getFieldValue("EQTYPE"));
        assertEquals(19, group4.getIntValue("ESURN_LEN"));
        assertEquals("ge:ISO1:3:VII-7:MMR", group4.getFieldValue("ESURN"));
        assertEquals(756, group4.getIntValue("DETAIL_LEN"));
        assertEquals("G", group4.getFieldValue("DETAIL_CMPR"));
        assertArrayEquals(gzipData, group4.getFieldValue("DETAIL").getBytes(StandardCharsets.ISO_8859_1));

        TreGroup group5 = ccinfa.getEntry("CODES").getGroups().get(4);
        assertEquals(2, group5.getIntValue("CODE_LEN"));
        assertEquals("S1", group5.getFieldValue("CODE"));
        assertEquals(" ", group5.getFieldValue("EQTYPE"));
        assertEquals(19, group5.getIntValue("ESURN_LEN"));
        assertEquals("ge:GENC:3:3-alt:SCT", group5.getFieldValue("ESURN"));
        assertEquals(0, group5.getIntValue("DETAIL_LEN"));

        TreGroup group6 = ccinfa.getEntry("CODES").getGroups().get(5);
        assertEquals(2, group6.getIntValue("CODE_LEN"));
        assertEquals("YY", group6.getFieldValue("CODE"));
        assertEquals("C", group6.getFieldValue("EQTYPE"));
        assertEquals(16, group6.getIntValue("ESURN_LEN"));
        assertEquals("gg:1059:2:ed9:3E", group6.getFieldValue("ESURN"));
        assertEquals(0, group6.getIntValue("DETAIL_LEN"));
    }

}
