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
package org.codice.imaging.nitf.trewrap;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import static javax.xml.bind.DatatypeConverter.parseHexBinary;
import org.codice.imaging.nitf.core.common.NitfFormatException;
import org.codice.imaging.nitf.core.tre.Tre;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 * Tests for the MTIMSA TRE wrapper.
 */
public class MTIMSA_WrapTest extends SharedTreTestSupport {

    public MTIMSA_WrapTest() {
    }

    @Test
    public void basicParse() throws IOException, NitfFormatException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write("MTIMSA0015300199fa238862-73ed-41fc-8d52-bfc7a954428c00295cb5511-7350-479b-9c8a-f028aba01e840000030045.0000000E+0100000000620160716215756.012345678".getBytes(StandardCharsets.ISO_8859_1));
        baos.write(parseHexBinary("0000000001312D00"));
        baos.write(parseHexBinary("01"));
        baos.write(parseHexBinary("01020304"));
        baos.write(parseHexBinary("00000001"));
        baos.write(parseHexBinary("4e"));
        Tre tre = parseTRE(new ByteArrayInputStream(baos.toByteArray()), 164, "MTIMSA");
        MTIMSA mtimsa = new MTIMSA(tre);
        assertTrue(mtimsa.getValidity().isValid());
        assertEquals(1, mtimsa.getImageSegmentIndex());
        assertTrue(mtimsa.geoCoordinatesAreStatic());
        assertEquals("fa238862-73ed-41fc-8d52-bfc7a954428c", mtimsa.getLayerId());
        assertEquals(2, mtimsa.getCameraSetIndex());
        assertEquals("95cb5511-7350-479b-9c8a-f028aba01e84", mtimsa.getCameraIdentifier());
        assertEquals(3, mtimsa.getTimeIntervalIndex());
        assertEquals(4, mtimsa.getTemporalBlockIndex());
        assertEquals(50.0, mtimsa.getNominalFrameRate(), 0.00001);
        assertEquals(6, mtimsa.getReferenceFrameNumber());
        ZonedDateTime expectedBaseTimestamp = ZonedDateTime.of(2016, 7, 16, 21, 57, 56, 12345678, ZoneId.of("UTC"));
        ZonedDateTime actualBaseTimestamp = mtimsa.getBaseTimestamp();
        assertEquals(expectedBaseTimestamp, actualBaseTimestamp);
        assertEquals(20000000, mtimsa.getDeltaTimeMultiplier().intValueExact());
        assertEquals(16909060, mtimsa.getNumberOfFrames());
        assertEquals(1, mtimsa.getNumberOfDeltaTimes());
        assertEquals(BigInteger.valueOf(78L), mtimsa.getDeltaTime(0));
        ZonedDateTime expectedFrame1Time = ZonedDateTime.of(2016, 7, 16, 21, 57, 57, 572345678, ZoneId.of("UTC"));
        assertEquals(expectedFrame1Time, mtimsa.getFrameTime(1));
    }

    @Test
    public void basicNaNAltData() throws IOException, NitfFormatException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write("MTIMSA0016100199fa238862-73ed-41fc-8d52-bfc7a954428c00295cb5511-7350-479b-9c8a-f028aba01e84000003004NaN                   20160716215756.012345678".getBytes(StandardCharsets.ISO_8859_1));
        baos.write(parseHexBinary("0000000001312D00"));
        baos.write(parseHexBinary("03"));
        baos.write(parseHexBinary("01020304"));
        baos.write(parseHexBinary("00000003"));
        baos.write(parseHexBinary("4e0245"));
        baos.write(parseHexBinary("fe0245"));

        baos.write(parseHexBinary("3e0245"));
        Tre tre = parseTRE(new ByteArrayInputStream(baos.toByteArray()), 172, "MTIMSA");
        MTIMSA mtimsa = new MTIMSA(tre);
        assertFalse(mtimsa.getValidity().isValid());
        assertEquals(-1.0, mtimsa.getNominalFrameRate(), 0.00001);
        assertEquals(-1, mtimsa.getReferenceFrameNumber());

        ZonedDateTime expectedBaseTimestamp = ZonedDateTime.of(2016, 7, 16, 21, 57, 56, 12345678, ZoneId.of("UTC"));
        ZonedDateTime actualBaseTimestamp = mtimsa.getBaseTimestamp();
        assertEquals(expectedBaseTimestamp, actualBaseTimestamp);
        assertEquals(20000000, mtimsa.getDeltaTimeMultiplier().intValueExact());
        assertEquals(16909060, mtimsa.getNumberOfFrames());
        assertEquals(3, mtimsa.getNumberOfDeltaTimes());
        assertEquals(BigInteger.valueOf(5112389L), mtimsa.getDeltaTime(0));
        assertEquals(BigInteger.valueOf(16646725L), mtimsa.getDeltaTime(1));
        assertEquals(BigInteger.valueOf(4063813L), mtimsa.getDeltaTime(2));

        ZonedDateTime expectedFrame3Time = ZonedDateTime.of(2016, 7, 22, 21, 25, 34, 552345678, ZoneId.of("UTC"));
        assertEquals(expectedFrame3Time, mtimsa.getFrameTime(3));
    }
}
