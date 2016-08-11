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
import java.nio.charset.StandardCharsets;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import org.codice.imaging.nitf.core.common.NitfFormatException;
import org.codice.imaging.nitf.core.tre.Tre;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 * Tests for the TMINTA TRE wrapper.
 */
public class TMINTA_WrapTest extends SharedTreTestSupport {

    public TMINTA_WrapTest() {
    }

    @Test
    public void basicParse() throws IOException, NitfFormatException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write("TMINTA00112000200000320160819201946.80225902620160819201948.72887270900000420160819203319.69098424120160819203321.557468116".getBytes(StandardCharsets.ISO_8859_1));
        Tre tre = parseTRE(new ByteArrayInputStream(baos.toByteArray()), 123, "TMINTA");
        TMINTA tminta = new TMINTA(tre);
        assertTrue(tminta.getValidity().isValid());
        assertEquals(2, tminta.getNumberOfTimeIntervals());
        assertEquals(3, tminta.getTimeIntervalIndex(0));
        ZonedDateTime expectedStartTimeStamp0 = ZonedDateTime.of(2016, 8, 19, 20, 19, 46, 802259026, ZoneId.of("UTC"));
        assertEquals(expectedStartTimeStamp0, tminta.getStartTimeStamp(0));
        ZonedDateTime expectedEndTimeStamp0 = ZonedDateTime.of(2016, 8, 19, 20, 19, 48, 728872709, ZoneId.of("UTC"));
        assertEquals(expectedEndTimeStamp0, tminta.getEndTimeStamp(0));
        assertEquals(4, tminta.getTimeIntervalIndex(1));
        ZonedDateTime expectedStartTimeStamp1 = ZonedDateTime.of(2016, 8, 19, 20, 33, 19, 690984241, ZoneId.of("UTC"));
        assertEquals(expectedStartTimeStamp1, tminta.getStartTimeStamp(1));
        ZonedDateTime expectedEndTimeStamp1 = ZonedDateTime.of(2016, 8, 19, 20, 33, 21, 557468116, ZoneId.of("UTC"));
        assertEquals(expectedEndTimeStamp1, tminta.getEndTimeStamp(1));
    }
}
