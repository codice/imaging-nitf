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
import static javax.xml.bind.DatatypeConverter.parseHexBinary;
import org.codice.imaging.nitf.core.common.NitfFormatException;
import org.codice.imaging.nitf.core.tre.Tre;
import org.junit.Test;

/**
 * Tests for parsing BANDSB.
 *
 * This is defined in STDI-0002 Appendix X. CN2 has important clarifications and
 * corrections.
 */
public class BANDSB_Test extends SharedTreTest {

    public BANDSB_Test() {
    }

    @Test
    public void SimpleParse() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write("BANDSB00123".getBytes(StandardCharsets.ISO_8859_1));
        createStandardBANDSBparts(baos);
        baos.write(parseHexBinary("00000000"));
        baos.write("W".getBytes(StandardCharsets.ISO_8859_1));
        Tre bandsb = doParseAndCheckResults(baos);
        assertEquals(17, bandsb.getEntries().size());
        assertEquals(0, bandsb.getIntValue("EXISTENCE_MASK"));
    }

    @Test
    public void ParseWithB31() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write("BANDSB00151".getBytes(StandardCharsets.ISO_8859_1));
        createStandardBANDSBparts(baos);
        baos.write(parseHexBinary("80000000"));
        baos.write("DETECTOR                ".getBytes(StandardCharsets.ISO_8859_1));
        baos.write(parseHexBinary("7f800001"));
        baos.write("W".getBytes(StandardCharsets.ISO_8859_1));

        Tre bandsb = doParseAndCheckResults(baos);
        assertEquals(19, bandsb.getEntries().size());
        assertEquals(0x80000000L, bandsb.getLongValue("EXISTENCE_MASK"));
        assertEquals("DETECTOR                ", bandsb.getFieldValue("RADIOMETRIC_ADJUSTMENT_SURFACE"));
        assertEquals(Double.NaN, bandsb.getDoubleValue("ATMOSPHERIC_ADJUSTMENT_ALTITUDE"), 0.0);
    }

    @Test
    public void ParseWithB30() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write("BANDSB00130".getBytes(StandardCharsets.ISO_8859_1));
        createStandardBANDSBparts(baos);
        baos.write(parseHexBinary("40000000"));
        baos.write("0000.75".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("W".getBytes(StandardCharsets.ISO_8859_1));

        Tre bandsb = doParseAndCheckResults(baos);
        assertEquals(18, bandsb.getEntries().size());
        assertEquals(0x40000000L, bandsb.getLongValue("EXISTENCE_MASK"));
        assertEquals(0.75, bandsb.getDoubleValue("DIAMETER"), 0.000001);
    }

    @Test
    public void ParseWithB29() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write("BANDSB00155".getBytes(StandardCharsets.ISO_8859_1));
        createStandardBANDSBparts(baos);
        baos.write(parseHexBinary("20000000"));
        baos.write("01234567890123456789012345678901".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("W".getBytes(StandardCharsets.ISO_8859_1));

        Tre bandsb = doParseAndCheckResults(baos);
        assertEquals(18, bandsb.getEntries().size());
        assertEquals(0x20000000L, bandsb.getLongValue("EXISTENCE_MASK"));
        assertEquals("01234567890123456789012345678901", bandsb.getFieldValue("DATA_FLD_2"));
    }

    @Test
    public void ParseWithB28() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write("BANDSB00273".getBytes(StandardCharsets.ISO_8859_1));
        addThreeBandHeaderParts(baos);
        baos.write(parseHexBinary("10000000"));
        baos.write("U".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("FIRST BAND ID                                     ".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("SECOND BAND ID                                    ".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("FINAL BAND ID                                     ".getBytes(StandardCharsets.ISO_8859_1));
        Tre bandsb = parseTRE(baos.toByteArray(), baos.toByteArray().length, "BANDSB");
        checkThreeBandHeaderParts(bandsb);
        assertEquals(0x10000000L, bandsb.getLongValue("EXISTENCE_MASK"));
        assertEquals("FIRST BAND ID                                     ", bandsb.getEntry("BANDS").getGroups().get(0).getFieldValue("BANDID"));
        assertEquals("SECOND BAND ID                                    ", bandsb.getEntry("BANDS").getGroups().get(1).getFieldValue("BANDID"));
        assertEquals("FINAL BAND ID                                     ", bandsb.getEntry("BANDS").getGroups().get(2).getFieldValue("BANDID"));
    }

    @Test
    public void ParseWithB27() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write("BANDSB00126".getBytes(StandardCharsets.ISO_8859_1));
        addThreeBandHeaderParts(baos);
        baos.write(parseHexBinary("08000000"));
        baos.write("U".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("1".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("0".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("1".getBytes(StandardCharsets.ISO_8859_1));
        Tre bandsb = parseTRE(baos.toByteArray(), baos.toByteArray().length, "BANDSB");
        checkThreeBandHeaderParts(bandsb);
        assertEquals(0x08000000L, bandsb.getLongValue("EXISTENCE_MASK"));
        assertEquals(1, bandsb.getEntry("BANDS").getGroups().get(0).getIntValue("BAD_BAND"));
        assertEquals(0, bandsb.getEntry("BANDS").getGroups().get(1).getIntValue("BAD_BAND"));
        assertEquals("1", bandsb.getEntry("BANDS").getGroups().get(2).getFieldValue("BAD_BAND"));
    }

    @Test
    public void ParseWithB28andB26() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write("BANDSB00282".getBytes(StandardCharsets.ISO_8859_1));
        addThreeBandHeaderParts(baos);
        baos.write(parseHexBinary("14000000"));
        baos.write("U".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("FIRST BAND ID                                     ".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("8.0".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("SECOND BAND ID                                    ".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("7.6".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("FINAL BAND ID                                     ".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("3.3".getBytes(StandardCharsets.ISO_8859_1));
        Tre bandsb = parseTRE(baos.toByteArray(), baos.toByteArray().length, "BANDSB");
        checkThreeBandHeaderParts(bandsb);
        assertEquals(0x14000000L, bandsb.getLongValue("EXISTENCE_MASK"));
        assertEquals("FIRST BAND ID                                     ", bandsb.getEntry("BANDS").getGroups().get(0).getFieldValue("BANDID"));
        assertEquals(8.0, bandsb.getEntry("BANDS").getGroups().get(0).getDoubleValue("NIIRS"), 0.001);
        assertEquals("SECOND BAND ID                                    ", bandsb.getEntry("BANDS").getGroups().get(1).getFieldValue("BANDID"));
        assertEquals(7.6, bandsb.getEntry("BANDS").getGroups().get(1).getDoubleValue("NIIRS"), 0.001);
        assertEquals("FINAL BAND ID                                     ", bandsb.getEntry("BANDS").getGroups().get(2).getFieldValue("BANDID"));
        assertEquals(3.3, bandsb.getEntry("BANDS").getGroups().get(2).getDoubleValue("NIIRS"), 0.001);
    }

    @Test
    public void ParseWithB25() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write("BANDSB00138".getBytes(StandardCharsets.ISO_8859_1));
        addThreeBandHeaderParts(baos);
        baos.write(parseHexBinary("02000000"));
        baos.write("U".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("01000".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("00057".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("00035".getBytes(StandardCharsets.ISO_8859_1));
        Tre bandsb = parseTRE(baos.toByteArray(), baos.toByteArray().length, "BANDSB");
        checkThreeBandHeaderParts(bandsb);
        assertEquals(0x02000000L, bandsb.getLongValue("EXISTENCE_MASK"));
        assertEquals(1000, bandsb.getEntry("BANDS").getGroups().get(0).getIntValue("FOCAL_LEN"));
        assertEquals(57, bandsb.getEntry("BANDS").getGroups().get(1).getIntValue("FOCAL_LEN"));
        assertEquals(35, bandsb.getEntry("BANDS").getGroups().get(2).getIntValue("FOCAL_LEN"));
    }

    @Test
    public void ParseWithB24() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write("BANDSB00144".getBytes(StandardCharsets.ISO_8859_1));
        addThreeBandHeaderParts(baos);
        baos.write(parseHexBinary("01000000"));
        baos.write("U".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("0.70000".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("0.40000".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("1.06700".getBytes(StandardCharsets.ISO_8859_1));
        Tre bandsb = parseTRE(baos.toByteArray(), baos.toByteArray().length, "BANDSB");
        checkThreeBandHeaderParts(bandsb);
        assertEquals(0x01000000L, bandsb.getLongValue("EXISTENCE_MASK"));
        assertEquals(0.7, bandsb.getEntry("BANDS").getGroups().get(0).getDoubleValue("CWAVE"), 0.00001);
        assertEquals(0.4, bandsb.getEntry("BANDS").getGroups().get(1).getDoubleValue("CWAVE"), 0.00001);
        assertEquals(1.067, bandsb.getEntry("BANDS").getGroups().get(2).getDoubleValue("CWAVE"), 0.00001);
    }

    @Test
    public void ParseWithB23() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write("BANDSB00144".getBytes(StandardCharsets.ISO_8859_1));
        addThreeBandHeaderParts(baos);
        baos.write(parseHexBinary("00800000"));
        baos.write("U".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("0.70000".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("0.40000".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("1.06700".getBytes(StandardCharsets.ISO_8859_1));
        Tre bandsb = parseTRE(baos.toByteArray(), baos.toByteArray().length, "BANDSB");
        checkThreeBandHeaderParts(bandsb);
        assertEquals(0x00800000L, bandsb.getLongValue("EXISTENCE_MASK"));
        assertEquals(0.7, bandsb.getEntry("BANDS").getGroups().get(0).getDoubleValue("FWHM"), 0.00001);
        assertEquals(0.4, bandsb.getEntry("BANDS").getGroups().get(1).getDoubleValue("FWHM"), 0.00001);
        assertEquals(1.067, bandsb.getEntry("BANDS").getGroups().get(2).getDoubleValue("FWHM"), 0.00001);
    }

    @Test
    public void ParseWithB23andB22() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write("BANDSB00165".getBytes(StandardCharsets.ISO_8859_1));
        addThreeBandHeaderParts(baos);
        baos.write(parseHexBinary("00C00000"));
        baos.write("U".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("0.70000".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("0.00020".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("0.40000".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("0.00040".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("1.06700".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("0.00200".getBytes(StandardCharsets.ISO_8859_1));
        Tre bandsb = parseTRE(baos.toByteArray(), baos.toByteArray().length, "BANDSB");
        checkThreeBandHeaderParts(bandsb);
        assertEquals(0x00c00000L, bandsb.getLongValue("EXISTENCE_MASK"));
        assertEquals(0.7, bandsb.getEntry("BANDS").getGroups().get(0).getDoubleValue("FWHM"), 0.00001);
        assertEquals(0.0002, bandsb.getEntry("BANDS").getGroups().get(0).getDoubleValue("FWHM_UNC"), 0.00001);
        assertEquals(0.4, bandsb.getEntry("BANDS").getGroups().get(1).getDoubleValue("FWHM"), 0.00001);
        assertEquals(0.0004, bandsb.getEntry("BANDS").getGroups().get(1).getDoubleValue("FWHM_UNC"), 0.00001);
        assertEquals(1.067, bandsb.getEntry("BANDS").getGroups().get(2).getDoubleValue("FWHM"), 0.00001);
        assertEquals(0.002, bandsb.getEntry("BANDS").getGroups().get(2).getDoubleValue("FWHM_UNC"), 0.00001);
    }

    @Test
    public void ParseWithB21() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write("BANDSB00144".getBytes(StandardCharsets.ISO_8859_1));
        addThreeBandHeaderParts(baos);
        baos.write(parseHexBinary("00200000"));
        baos.write("U".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("0.70000".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("0.40000".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("1.06700".getBytes(StandardCharsets.ISO_8859_1));
        Tre bandsb = parseTRE(baos.toByteArray(), baos.toByteArray().length, "BANDSB");
        checkThreeBandHeaderParts(bandsb);
        assertEquals(0x00200000L, bandsb.getLongValue("EXISTENCE_MASK"));
        assertEquals(0.7, bandsb.getEntry("BANDS").getGroups().get(0).getDoubleValue("NOM_WAVE"), 0.00001);
        assertEquals(0.4, bandsb.getEntry("BANDS").getGroups().get(1).getDoubleValue("NOM_WAVE"), 0.00001);
        assertEquals(1.067, bandsb.getEntry("BANDS").getGroups().get(2).getDoubleValue("NOM_WAVE"), 0.00001);
    }

    @Test
    public void ParseWithB21andB20() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write("BANDSB00165".getBytes(StandardCharsets.ISO_8859_1));
        addThreeBandHeaderParts(baos);
        baos.write(parseHexBinary("00300000"));
        baos.write("U".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("0.70000".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("0.00020".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("0.40000".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("0.00040".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("1.06700".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("0.00200".getBytes(StandardCharsets.ISO_8859_1));
        Tre bandsb = parseTRE(baos.toByteArray(), baos.toByteArray().length, "BANDSB");
        checkThreeBandHeaderParts(bandsb);
        assertEquals(0x00300000L, bandsb.getLongValue("EXISTENCE_MASK"));
        assertEquals(0.7, bandsb.getEntry("BANDS").getGroups().get(0).getDoubleValue("NOM_WAVE"), 0.00001);
        assertEquals(0.0002, bandsb.getEntry("BANDS").getGroups().get(0).getDoubleValue("NOM_WAVE_UNC"), 0.00001);
        assertEquals(0.4, bandsb.getEntry("BANDS").getGroups().get(1).getDoubleValue("NOM_WAVE"), 0.00001);
        assertEquals(0.0004, bandsb.getEntry("BANDS").getGroups().get(1).getDoubleValue("NOM_WAVE_UNC"), 0.00001);
        assertEquals(1.067, bandsb.getEntry("BANDS").getGroups().get(2).getDoubleValue("NOM_WAVE"), 0.00001);
        assertEquals(0.002, bandsb.getEntry("BANDS").getGroups().get(2).getDoubleValue("NOM_WAVE_UNC"), 0.00001);
    }

    @Test
    public void ParseWithB19() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write("BANDSB00165".getBytes(StandardCharsets.ISO_8859_1));
        addThreeBandHeaderParts(baos);
        baos.write(parseHexBinary("00080000"));
        baos.write("U".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("0.70000".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("0.80000".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("0.40000".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("0.55000".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("1.02000".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("1.50000".getBytes(StandardCharsets.ISO_8859_1));
        Tre bandsb = parseTRE(baos.toByteArray(), baos.toByteArray().length, "BANDSB");
        checkThreeBandHeaderParts(bandsb);
        assertEquals(0x00080000L, bandsb.getLongValue("EXISTENCE_MASK"));
        assertEquals(0.7, bandsb.getEntry("BANDS").getGroups().get(0).getDoubleValue("LBOUND"), 0.00001);
        assertEquals(0.8, bandsb.getEntry("BANDS").getGroups().get(0).getDoubleValue("UBOUND"), 0.00001);
        assertEquals(0.4, bandsb.getEntry("BANDS").getGroups().get(1).getDoubleValue("LBOUND"), 0.00001);
        assertEquals(0.55, bandsb.getEntry("BANDS").getGroups().get(1).getDoubleValue("UBOUND"), 0.00001);
        assertEquals(1.02, bandsb.getEntry("BANDS").getGroups().get(2).getDoubleValue("LBOUND"), 0.00001);
        assertEquals(1.50, bandsb.getEntry("BANDS").getGroups().get(2).getDoubleValue("UBOUND"), 0.00001);
    }

    @Test
    public void ParseWithB18() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write("BANDSB00147".getBytes(StandardCharsets.ISO_8859_1));
        addThreeBandHeaderParts(baos);
        baos.write(parseHexBinary("00040000"));
        baos.write("U".getBytes(StandardCharsets.ISO_8859_1));
        baos.write(parseHexBinary("40266666"));
        baos.write(parseHexBinary("40600000"));
        baos.write(parseHexBinary("410b3333"));
        baos.write(parseHexBinary("3e4ccccd"));
        baos.write(parseHexBinary("4114cccd"));
        baos.write(parseHexBinary("42ae999a"));
        Tre bandsb = parseTRE(baos.toByteArray(), baos.toByteArray().length, "BANDSB");
        checkThreeBandHeaderParts(bandsb);
        assertEquals(0x00040000L, bandsb.getLongValue("EXISTENCE_MASK"));
        assertEquals(2.6, bandsb.getEntry("BANDS").getGroups().get(0).getDoubleValue("SCALE_FACTOR"), 0.00001);
        assertEquals(3.5, bandsb.getEntry("BANDS").getGroups().get(0).getDoubleValue("ADDITIVE_FACTOR"), 0.00001);
        assertEquals(8.7, bandsb.getEntry("BANDS").getGroups().get(1).getDoubleValue("SCALE_FACTOR"), 0.00001);
        assertEquals(0.2, bandsb.getEntry("BANDS").getGroups().get(1).getDoubleValue("ADDITIVE_FACTOR"), 0.00001);
        assertEquals(9.3, bandsb.getEntry("BANDS").getGroups().get(2).getDoubleValue("SCALE_FACTOR"), 0.00001);
        assertEquals(87.3, bandsb.getEntry("BANDS").getGroups().get(2).getDoubleValue("ADDITIVE_FACTOR"), 0.00001);
    }

    @Test
    public void ParseWithB17() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write("BANDSB00171".getBytes(StandardCharsets.ISO_8859_1));
        addThreeBandHeaderParts(baos);
        baos.write(parseHexBinary("00020000"));
        baos.write("U".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("191020082354.123".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("191020082354.345".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("191020082354.567".getBytes(StandardCharsets.ISO_8859_1));
        Tre bandsb = parseTRE(baos.toByteArray(), baos.toByteArray().length, "BANDSB");
        checkThreeBandHeaderParts(bandsb);
        assertEquals(0x00020000L, bandsb.getLongValue("EXISTENCE_MASK"));
        assertEquals("191020082354.123", bandsb.getEntry("BANDS").getGroups().get(0).getFieldValue("START_TIME"));
        assertEquals("191020082354.345", bandsb.getEntry("BANDS").getGroups().get(1).getFieldValue("START_TIME"));
        assertEquals("191020082354.567", bandsb.getEntry("BANDS").getGroups().get(2).getFieldValue("START_TIME"));
    }

    @Test
    public void ParseWithB16() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write("BANDSB00141".getBytes(StandardCharsets.ISO_8859_1));
        addThreeBandHeaderParts(baos);
        baos.write(parseHexBinary("00010000"));
        baos.write("U".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("0.0001".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("0.2002".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("3.2350".getBytes(StandardCharsets.ISO_8859_1));
        Tre bandsb = parseTRE(baos.toByteArray(), baos.toByteArray().length, "BANDSB");
        checkThreeBandHeaderParts(bandsb);
        assertEquals(0x00010000L, bandsb.getLongValue("EXISTENCE_MASK"));
        assertEquals(0.0001, bandsb.getEntry("BANDS").getGroups().get(0).getDoubleValue("INT_TIME"), 0.00001);
        assertEquals(0.2002, bandsb.getEntry("BANDS").getGroups().get(1).getDoubleValue("INT_TIME"), 0.00001);
        assertEquals(3.2350, bandsb.getEntry("BANDS").getGroups().get(2).getDoubleValue("INT_TIME"), 0.00001);
    }

    @Test
    public void ParseWithB15() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write("BANDSB00156".getBytes(StandardCharsets.ISO_8859_1));
        addThreeBandHeaderParts(baos);
        baos.write(parseHexBinary("00008000"));
        baos.write("U".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("0.0201".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("0.001".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("0.2002".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("0.007".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("3.2350".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("0.123".getBytes(StandardCharsets.ISO_8859_1));
        Tre bandsb = parseTRE(baos.toByteArray(), baos.toByteArray().length, "BANDSB");
        checkThreeBandHeaderParts(bandsb);
        assertEquals(0x00008000L, bandsb.getLongValue("EXISTENCE_MASK"));
        assertEquals(0.0201, bandsb.getEntry("BANDS").getGroups().get(0).getDoubleValue("CALDRK"), 0.00001);
        assertEquals(0.001, bandsb.getEntry("BANDS").getGroups().get(0).getDoubleValue("CALIBRATION_SENSITIVITY"), 0.0001);
        assertEquals(0.2002, bandsb.getEntry("BANDS").getGroups().get(1).getDoubleValue("CALDRK"), 0.00001);
        assertEquals(0.007, bandsb.getEntry("BANDS").getGroups().get(1).getDoubleValue("CALIBRATION_SENSITIVITY"), 0.0001);
        assertEquals(3.2350, bandsb.getEntry("BANDS").getGroups().get(2).getDoubleValue("CALDRK"), 0.00001);
        assertEquals(0.123, bandsb.getEntry("BANDS").getGroups().get(2).getDoubleValue("CALIBRATION_SENSITIVITY"), 0.0001);
    }

    @Test
    public void ParseWithB14() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write("BANDSB00171".getBytes(StandardCharsets.ISO_8859_1));
        addThreeBandHeaderParts(baos);
        baos.write(parseHexBinary("00004000"));
        baos.write("U".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("15.0000".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("M".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("5.20000".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("R".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("16.0000".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("M".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("5.70000".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("R".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("17.0000".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("M".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("5.30000".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("R".getBytes(StandardCharsets.ISO_8859_1));
        Tre bandsb = parseTRE(baos.toByteArray(), baos.toByteArray().length, "BANDSB");
        checkThreeBandHeaderParts(bandsb);
        assertEquals(0x00004000L, bandsb.getLongValue("EXISTENCE_MASK"));
        assertEquals(15.0, bandsb.getEntry("BANDS").getGroups().get(0).getDoubleValue("ROW_GSD"), 0.00001);
        assertEquals("M", bandsb.getEntry("BANDS").getGroups().get(0).getFieldValue("ROW_GSD_UNIT"));
        assertEquals(5.2, bandsb.getEntry("BANDS").getGroups().get(0).getDoubleValue("COL_GSD"), 0.00001);
        assertEquals("R", bandsb.getEntry("BANDS").getGroups().get(0).getFieldValue("COL_GSD_UNIT"));
        assertEquals(16.0, bandsb.getEntry("BANDS").getGroups().get(1).getDoubleValue("ROW_GSD"), 0.00001);
        assertEquals("M", bandsb.getEntry("BANDS").getGroups().get(1).getFieldValue("ROW_GSD_UNIT"));
        assertEquals(5.7, bandsb.getEntry("BANDS").getGroups().get(1).getDoubleValue("COL_GSD"), 0.00001);
        assertEquals("R", bandsb.getEntry("BANDS").getGroups().get(1).getFieldValue("COL_GSD_UNIT"));
        assertEquals(17.0, bandsb.getEntry("BANDS").getGroups().get(2).getDoubleValue("ROW_GSD"), 0.00001);
        assertEquals("M", bandsb.getEntry("BANDS").getGroups().get(2).getFieldValue("ROW_GSD_UNIT"));
        assertEquals(5.3, bandsb.getEntry("BANDS").getGroups().get(2).getDoubleValue("COL_GSD"), 0.00001);
        assertEquals("R", bandsb.getEntry("BANDS").getGroups().get(2).getFieldValue("COL_GSD_UNIT"));
    }

    @Test
    public void ParseWithB14andB13() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write("BANDSB00213".getBytes(StandardCharsets.ISO_8859_1));
        addThreeBandHeaderParts(baos);
        baos.write(parseHexBinary("00006000"));
        baos.write("U".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("15.0000".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("1.00000".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("M".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("5.20000".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("0.90000".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("R".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("16.0000".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("1.90000".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("M".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("5.70000".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("0.20000".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("R".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("17.0000".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("0.30000".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("M".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("5.30000".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("0.40000".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("R".getBytes(StandardCharsets.ISO_8859_1));
        Tre bandsb = parseTRE(baos.toByteArray(), baos.toByteArray().length, "BANDSB");
        checkThreeBandHeaderParts(bandsb);
        assertEquals(0x00006000L, bandsb.getLongValue("EXISTENCE_MASK"));
        assertEquals(15.0, bandsb.getEntry("BANDS").getGroups().get(0).getDoubleValue("ROW_GSD"), 0.00001);
        assertEquals(1.0, bandsb.getEntry("BANDS").getGroups().get(0).getDoubleValue("ROW_GSD_UNC"), 0.00001);
        assertEquals("M", bandsb.getEntry("BANDS").getGroups().get(0).getFieldValue("ROW_GSD_UNIT"));
        assertEquals(5.2, bandsb.getEntry("BANDS").getGroups().get(0).getDoubleValue("COL_GSD"), 0.00001);
        assertEquals(0.9, bandsb.getEntry("BANDS").getGroups().get(0).getDoubleValue("COL_GSD_UNC"), 0.00001);
        assertEquals("R", bandsb.getEntry("BANDS").getGroups().get(0).getFieldValue("COL_GSD_UNIT"));
        assertEquals(16.0, bandsb.getEntry("BANDS").getGroups().get(1).getDoubleValue("ROW_GSD"), 0.00001);
        assertEquals(1.9, bandsb.getEntry("BANDS").getGroups().get(1).getDoubleValue("ROW_GSD_UNC"), 0.00001);
        assertEquals("M", bandsb.getEntry("BANDS").getGroups().get(1).getFieldValue("ROW_GSD_UNIT"));
        assertEquals(5.7, bandsb.getEntry("BANDS").getGroups().get(1).getDoubleValue("COL_GSD"), 0.00001);
        assertEquals(0.2, bandsb.getEntry("BANDS").getGroups().get(1).getDoubleValue("COL_GSD_UNC"), 0.00001);
        assertEquals("R", bandsb.getEntry("BANDS").getGroups().get(1).getFieldValue("COL_GSD_UNIT"));
        assertEquals(17.0, bandsb.getEntry("BANDS").getGroups().get(2).getDoubleValue("ROW_GSD"), 0.00001);
        assertEquals(0.3, bandsb.getEntry("BANDS").getGroups().get(2).getDoubleValue("ROW_GSD_UNC"), 0.00001);
        assertEquals("M", bandsb.getEntry("BANDS").getGroups().get(2).getFieldValue("ROW_GSD_UNIT"));
        assertEquals(5.3, bandsb.getEntry("BANDS").getGroups().get(2).getDoubleValue("COL_GSD"), 0.00001);
        assertEquals(0.4, bandsb.getEntry("BANDS").getGroups().get(2).getDoubleValue("COL_GSD_UNC"), 0.00001);
        assertEquals("R", bandsb.getEntry("BANDS").getGroups().get(2).getFieldValue("COL_GSD_UNIT"));
    }

    @Test
    public void ParseWithB12() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write("BANDSB00153".getBytes(StandardCharsets.ISO_8859_1));
        addThreeBandHeaderParts(baos);
        baos.write(parseHexBinary("00001000"));
        baos.write("U".getBytes(StandardCharsets.ISO_8859_1));
        baos.write(".0201".getBytes(StandardCharsets.ISO_8859_1));
        baos.write(".0010".getBytes(StandardCharsets.ISO_8859_1));
        baos.write(".2002".getBytes(StandardCharsets.ISO_8859_1));
        baos.write(".0070".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("3.235".getBytes(StandardCharsets.ISO_8859_1));
        baos.write(".1230".getBytes(StandardCharsets.ISO_8859_1));
        Tre bandsb = parseTRE(baos.toByteArray(), baos.toByteArray().length, "BANDSB");
        checkThreeBandHeaderParts(bandsb);
        assertEquals(0x00001000L, bandsb.getLongValue("EXISTENCE_MASK"));
        assertEquals(0.0201, bandsb.getEntry("BANDS").getGroups().get(0).getDoubleValue("BKNOISE"), 0.00001);
        assertEquals(0.001, bandsb.getEntry("BANDS").getGroups().get(0).getDoubleValue("SCNNOISE"), 0.0001);
        assertEquals(0.2002, bandsb.getEntry("BANDS").getGroups().get(1).getDoubleValue("BKNOISE"), 0.00001);
        assertEquals(0.007, bandsb.getEntry("BANDS").getGroups().get(1).getDoubleValue("SCNNOISE"), 0.0001);
        assertEquals(3.2350, bandsb.getEntry("BANDS").getGroups().get(2).getDoubleValue("BKNOISE"), 0.00001);
        assertEquals(0.123, bandsb.getEntry("BANDS").getGroups().get(2).getDoubleValue("SCNNOISE"), 0.0001);
    }

    @Test
    public void ParseWithB11() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write("BANDSB00171".getBytes(StandardCharsets.ISO_8859_1));
        addThreeBandHeaderParts(baos);
        baos.write(parseHexBinary("00000800"));
        baos.write("U".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("15.0000".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("M".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("5.20000".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("R".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("16.0000".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("M".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("5.70000".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("R".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("17.0000".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("M".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("5.30000".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("R".getBytes(StandardCharsets.ISO_8859_1));
        Tre bandsb = parseTRE(baos.toByteArray(), baos.toByteArray().length, "BANDSB");
        checkThreeBandHeaderParts(bandsb);
        assertEquals(0x00000800L, bandsb.getLongValue("EXISTENCE_MASK"));
        assertEquals(15.0, bandsb.getEntry("BANDS").getGroups().get(0).getDoubleValue("SPT_RESP_FUNCTION_ROW"), 0.00001);
        assertEquals("M", bandsb.getEntry("BANDS").getGroups().get(0).getFieldValue("SPT_RESP_UNIT_ROW"));
        assertEquals(5.2, bandsb.getEntry("BANDS").getGroups().get(0).getDoubleValue("SPT_RESP_FUNCTION_COL"), 0.00001);
        assertEquals("R", bandsb.getEntry("BANDS").getGroups().get(0).getFieldValue("SPT_RESP_UNIT_COL"));
        assertEquals(16.0, bandsb.getEntry("BANDS").getGroups().get(1).getDoubleValue("SPT_RESP_FUNCTION_ROW"), 0.00001);
        assertEquals("M", bandsb.getEntry("BANDS").getGroups().get(1).getFieldValue("SPT_RESP_UNIT_ROW"));
        assertEquals(5.7, bandsb.getEntry("BANDS").getGroups().get(1).getDoubleValue("SPT_RESP_FUNCTION_COL"), 0.00001);
        assertEquals("R", bandsb.getEntry("BANDS").getGroups().get(1).getFieldValue("SPT_RESP_UNIT_COL"));
        assertEquals(17.0, bandsb.getEntry("BANDS").getGroups().get(2).getDoubleValue("SPT_RESP_FUNCTION_ROW"), 0.00001);
        assertEquals("M", bandsb.getEntry("BANDS").getGroups().get(2).getFieldValue("SPT_RESP_UNIT_ROW"));
        assertEquals(5.3, bandsb.getEntry("BANDS").getGroups().get(2).getDoubleValue("SPT_RESP_FUNCTION_COL"), 0.00001);
        assertEquals("R", bandsb.getEntry("BANDS").getGroups().get(2).getFieldValue("SPT_RESP_UNIT_COL"));
    }

    @Test
    public void ParseWithB11andB10() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write("BANDSB00213".getBytes(StandardCharsets.ISO_8859_1));
        addThreeBandHeaderParts(baos);
        baos.write(parseHexBinary("00000c00"));
        baos.write("U".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("15.0000".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("1.00000".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("M".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("5.20000".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("0.90000".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("R".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("16.0000".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("1.90000".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("M".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("5.70000".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("0.20000".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("R".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("17.0000".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("0.30000".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("M".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("5.30000".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("0.40000".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("R".getBytes(StandardCharsets.ISO_8859_1));
        Tre bandsb = parseTRE(baos.toByteArray(), baos.toByteArray().length, "BANDSB");
        checkThreeBandHeaderParts(bandsb);
        assertEquals(0x00000C00L, bandsb.getLongValue("EXISTENCE_MASK"));
        assertEquals(15.0, bandsb.getEntry("BANDS").getGroups().get(0).getDoubleValue("SPT_RESP_FUNCTION_ROW"), 0.00001);
        assertEquals(1.0, bandsb.getEntry("BANDS").getGroups().get(0).getDoubleValue("SPT_RESP_UNC_ROW"), 0.00001);
        assertEquals("M", bandsb.getEntry("BANDS").getGroups().get(0).getFieldValue("SPT_RESP_UNIT_ROW"));
        assertEquals(5.2, bandsb.getEntry("BANDS").getGroups().get(0).getDoubleValue("SPT_RESP_FUNCTION_COL"), 0.00001);
        assertEquals(0.9, bandsb.getEntry("BANDS").getGroups().get(0).getDoubleValue("SPT_RESP_UNC_COL"), 0.00001);
        assertEquals("R", bandsb.getEntry("BANDS").getGroups().get(0).getFieldValue("SPT_RESP_UNIT_COL"));
        assertEquals(16.0, bandsb.getEntry("BANDS").getGroups().get(1).getDoubleValue("SPT_RESP_FUNCTION_ROW"), 0.00001);
        assertEquals(1.9, bandsb.getEntry("BANDS").getGroups().get(1).getDoubleValue("SPT_RESP_UNC_ROW"), 0.00001);
        assertEquals("M", bandsb.getEntry("BANDS").getGroups().get(1).getFieldValue("SPT_RESP_UNIT_ROW"));
        assertEquals(5.7, bandsb.getEntry("BANDS").getGroups().get(1).getDoubleValue("SPT_RESP_FUNCTION_COL"), 0.00001);
        assertEquals(0.2, bandsb.getEntry("BANDS").getGroups().get(1).getDoubleValue("SPT_RESP_UNC_COL"), 0.00001);
        assertEquals("R", bandsb.getEntry("BANDS").getGroups().get(1).getFieldValue("SPT_RESP_UNIT_COL"));
        assertEquals(17.0, bandsb.getEntry("BANDS").getGroups().get(2).getDoubleValue("SPT_RESP_FUNCTION_ROW"), 0.00001);
        assertEquals(0.3, bandsb.getEntry("BANDS").getGroups().get(2).getDoubleValue("SPT_RESP_UNC_ROW"), 0.00001);
        assertEquals("M", bandsb.getEntry("BANDS").getGroups().get(2).getFieldValue("SPT_RESP_UNIT_ROW"));
        assertEquals(5.3, bandsb.getEntry("BANDS").getGroups().get(2).getDoubleValue("SPT_RESP_FUNCTION_COL"), 0.00001);
        assertEquals(0.4, bandsb.getEntry("BANDS").getGroups().get(2).getDoubleValue("SPT_RESP_UNC_COL"), 0.00001);
        assertEquals("R", bandsb.getEntry("BANDS").getGroups().get(2).getFieldValue("SPT_RESP_UNIT_COL"));
    }

    @Test
    public void ParseWithB0() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write("BANDSB00253".getBytes(StandardCharsets.ISO_8859_1));
        createStandardBANDSBparts(baos);
        baos.write(parseHexBinary("00000001"));
        baos.write("W".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("02".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("04".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("R".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("UNIT   ".getBytes(StandardCharsets.ISO_8859_1));
        baos.write(parseHexBinary("40800000"));
        baos.write("A".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("%      ".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("Words, words, more .".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("A".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("HPA    ".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("AbcdeFghijKlmnoPqrst".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("I".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("MBAR   ".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("1234568790".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("R".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("UM     ".getBytes(StandardCharsets.ISO_8859_1));
        baos.write(parseHexBinary("40600000"));
        baos.write("A".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("M/S    ".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("     abcde     fghij".getBytes(StandardCharsets.ISO_8859_1));
        Tre bandsb = doParseAndCheckResults(baos);
        assertEquals(21, bandsb.getEntries().size());
        assertEquals(0x00000001L, bandsb.getLongValue("EXISTENCE_MASK"));
        assertEquals(2, bandsb.getIntValue("NUM_AUX_B"));
        assertEquals(2, bandsb.getEntry("BAND_AUX_PARAMS").getGroups().size());
        assertEquals("R", bandsb.getEntry("BAND_AUX_PARAMS").getGroups().get(0).getFieldValue("BAPF"));
        assertEquals("UNIT   ", bandsb.getEntry("BAND_AUX_PARAMS").getGroups().get(0).getFieldValue("UBAP"));
        assertEquals(4.0, bandsb.getEntry("BAND_AUX_PARAMS").getGroups().get(0).getEntry("BANDS_AUX").getGroups().get(0).getDoubleValue("APR"), 0.00001);
        assertEquals("A", bandsb.getEntry("BAND_AUX_PARAMS").getGroups().get(1).getFieldValue("BAPF"));
        assertEquals("%      ", bandsb.getEntry("BAND_AUX_PARAMS").getGroups().get(1).getFieldValue("UBAP"));
        assertEquals("Words, words, more .", bandsb.getEntry("BAND_AUX_PARAMS").getGroups().get(1).getEntry("BANDS_AUX").getGroups().get(0).getFieldValue("APA"));
        assertEquals(4, bandsb.getIntValue("NUM_AUX_C"));
        assertEquals(4, bandsb.getEntry("CUBE_AUX_PARAMS").getGroups().size());
        assertEquals(3, bandsb.getEntry("CUBE_AUX_PARAMS").getGroups().get(0).getEntries().size());
        assertEquals("A", bandsb.getEntry("CUBE_AUX_PARAMS").getGroups().get(0).getFieldValue("CAPF"));
        assertEquals("HPA    ", bandsb.getEntry("CUBE_AUX_PARAMS").getGroups().get(0).getFieldValue("UCAP"));
        assertEquals("AbcdeFghijKlmnoPqrst", bandsb.getEntry("CUBE_AUX_PARAMS").getGroups().get(0).getFieldValue("APA"));
        assertEquals(3, bandsb.getEntry("CUBE_AUX_PARAMS").getGroups().get(1).getEntries().size());
        assertEquals("I", bandsb.getEntry("CUBE_AUX_PARAMS").getGroups().get(1).getFieldValue("CAPF"));
        assertEquals("MBAR   ", bandsb.getEntry("CUBE_AUX_PARAMS").getGroups().get(1).getFieldValue("UCAP"));
        assertEquals(1234568790, bandsb.getEntry("CUBE_AUX_PARAMS").getGroups().get(1).getLongValue("APN"));
        assertEquals(3, bandsb.getEntry("CUBE_AUX_PARAMS").getGroups().get(2).getEntries().size());
        assertEquals("R", bandsb.getEntry("CUBE_AUX_PARAMS").getGroups().get(2).getFieldValue("CAPF"));
        assertEquals("UM     ", bandsb.getEntry("CUBE_AUX_PARAMS").getGroups().get(2).getFieldValue("UCAP"));
        assertEquals(3.5, bandsb.getEntry("CUBE_AUX_PARAMS").getGroups().get(2).getDoubleValue("APR"), 0.00001);
        assertEquals(3, bandsb.getEntry("CUBE_AUX_PARAMS").getGroups().get(3).getEntries().size());
        assertEquals("A", bandsb.getEntry("CUBE_AUX_PARAMS").getGroups().get(3).getFieldValue("CAPF"));
        assertEquals("M/S    ", bandsb.getEntry("CUBE_AUX_PARAMS").getGroups().get(3).getFieldValue("UCAP"));
        assertEquals("     abcde     fghij", bandsb.getEntry("CUBE_AUX_PARAMS").getGroups().get(3).getFieldValue("APA"));
    }

    @Test
    public void ParseWithB0multiband() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write("BANDSB00469".getBytes(StandardCharsets.ISO_8859_1));
        addThreeBandHeaderParts(baos);
        baos.write(parseHexBinary("10000001"));
        baos.write("U".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("FIRST BAND ID                                     ".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("SECOND BAND ID                                    ".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("FINAL BAND ID                                     ".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("02".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("04".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("I".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("FM     ".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("1234567891".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("1234567892".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("1234567893".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("A".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("KM/H   ".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("Words for Band 1    ".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("Words for Band 2    ".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("Words for Band 3    ".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("A".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("S      ".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("AbcdeFghijKlmnoPqrst".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("I".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("H      ".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("1234568790".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("R".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("KM2    ".getBytes(StandardCharsets.ISO_8859_1));
        baos.write(parseHexBinary("40600000"));
        baos.write("A".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("M2     ".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("     abcde     fghij".getBytes(StandardCharsets.ISO_8859_1));
        Tre bandsb = parseTRE(baos.toByteArray(), baos.toByteArray().length, "BANDSB");
        assertEquals(21, bandsb.getEntries().size());
        assertEquals("00003", bandsb.getFieldValue("COUNT"));
        assertEquals(3, bandsb.getIntValue("COUNT"));
        assertEquals("EMISSIVITY              ", bandsb.getFieldValue("RADIOMETRIC_QUANTITY"));
        assertEquals("P", bandsb.getFieldValue("RADIOMETRIC_QUANTITY_UNIT"));
        assertEquals(4.0, bandsb.getDoubleValue("SCALE_FACTOR"), 0.000001);
        assertEquals(2.0, bandsb.getDoubleValue("ADDITIVE_FACTOR"), 0.000001);
        assertEquals(30.0, bandsb.getDoubleValue("ROW_GSD"), 0.000001);
        assertEquals("M", bandsb.getFieldValue("ROW_GSD_UNIT"));
        assertEquals(20.0, bandsb.getDoubleValue("COL_GSD"), 0.000001);
        assertEquals("R", bandsb.getFieldValue("COL_GSD_UNIT"));
        assertEquals(1.01, bandsb.getDoubleValue("SPT_RESP_ROW"), 0.000001);
        assertEquals("R", bandsb.getFieldValue("SPT_RESP_ROW_UNIT"));
        assertEquals(2.1, bandsb.getDoubleValue("SPT_RESP_COL"), 0.000001);
        assertEquals("M", bandsb.getFieldValue("SPT_RESP_COL_UNIT"));
        assertEquals("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUV", bandsb.getFieldValue("DATA_FLD_1"));
        assertEquals("U", bandsb.getFieldValue("WAVE_LENGTH_UNIT"));
        assertEquals(3, bandsb.getEntry("BANDS").getGroups().size());
        assertEquals(0x10000001L, bandsb.getLongValue("EXISTENCE_MASK"));
        assertEquals("FIRST BAND ID                                     ", bandsb.getEntry("BANDS").getGroups().get(0).getFieldValue("BANDID"));
        assertEquals("SECOND BAND ID                                    ", bandsb.getEntry("BANDS").getGroups().get(1).getFieldValue("BANDID"));
        assertEquals("FINAL BAND ID                                     ", bandsb.getEntry("BANDS").getGroups().get(2).getFieldValue("BANDID"));
        assertEquals(0x10000001L, bandsb.getLongValue("EXISTENCE_MASK"));
        assertEquals(2, bandsb.getIntValue("NUM_AUX_B"));
        assertEquals(2, bandsb.getEntry("BAND_AUX_PARAMS").getGroups().size());
        assertEquals("I", bandsb.getEntry("BAND_AUX_PARAMS").getGroups().get(0).getFieldValue("BAPF"));
        assertEquals("FM     ", bandsb.getEntry("BAND_AUX_PARAMS").getGroups().get(0).getFieldValue("UBAP"));
        assertEquals(1234567891, bandsb.getEntry("BAND_AUX_PARAMS").getGroups().get(0).getEntry("BANDS_AUX").getGroups().get(0).getLongValue("APN"));
        assertEquals(1234567892, bandsb.getEntry("BAND_AUX_PARAMS").getGroups().get(0).getEntry("BANDS_AUX").getGroups().get(1).getLongValue("APN"));
        assertEquals(1234567893, bandsb.getEntry("BAND_AUX_PARAMS").getGroups().get(0).getEntry("BANDS_AUX").getGroups().get(2).getLongValue("APN"));
        assertEquals("A", bandsb.getEntry("BAND_AUX_PARAMS").getGroups().get(1).getFieldValue("BAPF"));
        assertEquals("KM/H   ", bandsb.getEntry("BAND_AUX_PARAMS").getGroups().get(1).getFieldValue("UBAP"));
        assertEquals("Words for Band 1    ", bandsb.getEntry("BAND_AUX_PARAMS").getGroups().get(1).getEntry("BANDS_AUX").getGroups().get(0).getFieldValue("APA"));
        assertEquals("Words for Band 2    ", bandsb.getEntry("BAND_AUX_PARAMS").getGroups().get(1).getEntry("BANDS_AUX").getGroups().get(1).getFieldValue("APA"));
        assertEquals("Words for Band 3    ", bandsb.getEntry("BAND_AUX_PARAMS").getGroups().get(1).getEntry("BANDS_AUX").getGroups().get(2).getFieldValue("APA"));
        assertEquals(4, bandsb.getIntValue("NUM_AUX_C"));
        assertEquals(4, bandsb.getEntry("CUBE_AUX_PARAMS").getGroups().size());
        assertEquals(3, bandsb.getEntry("CUBE_AUX_PARAMS").getGroups().get(0).getEntries().size());
        assertEquals("A", bandsb.getEntry("CUBE_AUX_PARAMS").getGroups().get(0).getFieldValue("CAPF"));
        assertEquals("S      ", bandsb.getEntry("CUBE_AUX_PARAMS").getGroups().get(0).getFieldValue("UCAP"));
        assertEquals("AbcdeFghijKlmnoPqrst", bandsb.getEntry("CUBE_AUX_PARAMS").getGroups().get(0).getFieldValue("APA"));
        assertEquals(3, bandsb.getEntry("CUBE_AUX_PARAMS").getGroups().get(1).getEntries().size());
        assertEquals("I", bandsb.getEntry("CUBE_AUX_PARAMS").getGroups().get(1).getFieldValue("CAPF"));
        assertEquals("H      ", bandsb.getEntry("CUBE_AUX_PARAMS").getGroups().get(1).getFieldValue("UCAP"));
        assertEquals(1234568790, bandsb.getEntry("CUBE_AUX_PARAMS").getGroups().get(1).getLongValue("APN"));
        assertEquals(3, bandsb.getEntry("CUBE_AUX_PARAMS").getGroups().get(2).getEntries().size());
        assertEquals("R", bandsb.getEntry("CUBE_AUX_PARAMS").getGroups().get(2).getFieldValue("CAPF"));
        assertEquals("KM2    ", bandsb.getEntry("CUBE_AUX_PARAMS").getGroups().get(2).getFieldValue("UCAP"));
        assertEquals(3.5, bandsb.getEntry("CUBE_AUX_PARAMS").getGroups().get(2).getDoubleValue("APR"), 0.00001);
        assertEquals(3, bandsb.getEntry("CUBE_AUX_PARAMS").getGroups().get(3).getEntries().size());
        assertEquals("A", bandsb.getEntry("CUBE_AUX_PARAMS").getGroups().get(3).getFieldValue("CAPF"));
        assertEquals("M2     ", bandsb.getEntry("CUBE_AUX_PARAMS").getGroups().get(3).getFieldValue("UCAP"));
        assertEquals("     abcde     fghij", bandsb.getEntry("CUBE_AUX_PARAMS").getGroups().get(3).getFieldValue("APA"));
    }

    private void addThreeBandHeaderParts(ByteArrayOutputStream baos) throws IOException {
        baos.write("00003EMISSIVITY              P".getBytes(StandardCharsets.ISO_8859_1));
        baos.write(parseHexBinary("40800000"));
        baos.write(parseHexBinary("40000000"));
        baos.write("0030.00M".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("0020.00R".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("0001.01R".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("0002.10M".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUV".getBytes(StandardCharsets.ISO_8859_1));
    }

    private void checkThreeBandHeaderParts(Tre bandsb) throws NitfFormatException {
        assertEquals(17, bandsb.getEntries().size());
        assertEquals("00003", bandsb.getFieldValue("COUNT"));
        assertEquals(3, bandsb.getIntValue("COUNT"));
        assertEquals("EMISSIVITY              ", bandsb.getFieldValue("RADIOMETRIC_QUANTITY"));
        assertEquals("P", bandsb.getFieldValue("RADIOMETRIC_QUANTITY_UNIT"));
        assertEquals(4.0, bandsb.getDoubleValue("SCALE_FACTOR"), 0.000001);
        assertEquals(2.0, bandsb.getDoubleValue("ADDITIVE_FACTOR"), 0.000001);
        assertEquals(30.0, bandsb.getDoubleValue("ROW_GSD"), 0.000001);
        assertEquals("M", bandsb.getFieldValue("ROW_GSD_UNIT"));
        assertEquals(20.0, bandsb.getDoubleValue("COL_GSD"), 0.000001);
        assertEquals("R", bandsb.getFieldValue("COL_GSD_UNIT"));
        assertEquals(1.01, bandsb.getDoubleValue("SPT_RESP_ROW"), 0.000001);
        assertEquals("R", bandsb.getFieldValue("SPT_RESP_ROW_UNIT"));
        assertEquals(2.1, bandsb.getDoubleValue("SPT_RESP_COL"), 0.000001);
        assertEquals("M", bandsb.getFieldValue("SPT_RESP_COL_UNIT"));
        assertEquals("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUV", bandsb.getFieldValue("DATA_FLD_1"));
        assertEquals("U", bandsb.getFieldValue("WAVE_LENGTH_UNIT"));
        assertEquals(3, bandsb.getEntry("BANDS").getGroups().size());
    }

    private void createStandardBANDSBparts(ByteArrayOutputStream baos) throws IOException {
        baos.write("00001EMISSIVITY              P".getBytes(StandardCharsets.ISO_8859_1));
        baos.write(parseHexBinary("40800000"));
        baos.write(parseHexBinary("40000000"));
        baos.write("0030.00M".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("0020.00R".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("0001.01R".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("0002.10M".getBytes(StandardCharsets.ISO_8859_1));
        baos.write("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUV".getBytes(StandardCharsets.ISO_8859_1));
    }

    private Tre doParseAndCheckResults(ByteArrayOutputStream baos) throws NitfFormatException, IOException {
        Tre bandsb = parseTRE(baos.toByteArray(), baos.toByteArray().length, "BANDSB");
        assertEquals("00001", bandsb.getFieldValue("COUNT"));
        assertEquals(1, bandsb.getIntValue("COUNT"));
        assertEquals("EMISSIVITY              ", bandsb.getFieldValue("RADIOMETRIC_QUANTITY"));
        assertEquals("P", bandsb.getFieldValue("RADIOMETRIC_QUANTITY_UNIT"));
        assertEquals(4.0, bandsb.getDoubleValue("SCALE_FACTOR"), 0.000001);
        assertEquals(2.0, bandsb.getDoubleValue("ADDITIVE_FACTOR"), 0.000001);
        assertEquals(30.0, bandsb.getDoubleValue("ROW_GSD"), 0.000001);
        assertEquals("M", bandsb.getFieldValue("ROW_GSD_UNIT"));
        assertEquals(20.0, bandsb.getDoubleValue("COL_GSD"), 0.000001);
        assertEquals("R", bandsb.getFieldValue("COL_GSD_UNIT"));
        assertEquals(1.01, bandsb.getDoubleValue("SPT_RESP_ROW"), 0.000001);
        assertEquals("R", bandsb.getFieldValue("SPT_RESP_ROW_UNIT"));
        assertEquals(2.1, bandsb.getDoubleValue("SPT_RESP_COL"), 0.000001);
        assertEquals("M", bandsb.getFieldValue("SPT_RESP_COL_UNIT"));
        assertEquals("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUV", bandsb.getFieldValue("DATA_FLD_1"));
        assertEquals("W", bandsb.getFieldValue("WAVE_LENGTH_UNIT"));
        return bandsb;
    }
}
