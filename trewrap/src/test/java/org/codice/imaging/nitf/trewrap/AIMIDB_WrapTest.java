package org.codice.imaging.nitf.trewrap;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import org.codice.imaging.nitf.core.common.NitfFormatException;
import org.codice.imaging.nitf.core.tre.Tre;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

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

/**
 * Tests for the ACFTB wrapper.
 */
public class AIMIDB_WrapTest extends SharedTreTestSupport {

    private final String mTestData = "AIMIDB0008920160807212836PQ03NOT AVAIL.A2004AB05P06 00412345CD01934567AS    3520S14904E             ";

    public AIMIDB_WrapTest() {
    }

    @Test
    public void basicParse() throws NitfFormatException {
        Tre tre = parseTRE(mTestData, "AIMIDB");
        AIMIDB aimidb = new AIMIDB(tre);
        assertTrue(aimidb.getValidity().isValid());
        ZonedDateTime expectedAcquisitionDate = ZonedDateTime.of(2016, 8, 7, 21, 28, 36, 0, ZoneId.of("UTC"));
        assertEquals(expectedAcquisitionDate, aimidb.getAcquisitionDate());
        assertEquals("PQ03", aimidb.getMissionNumber());
        assertEquals("NOT AVAIL.", aimidb.getMissionIdentification());
        assertEquals("A2", aimidb.getFlightNumber());
        assertEquals(4, aimidb.getImageOperationNumber());
        assertEquals("AB", aimidb.getCurrentSegmentID());
        assertEquals(5, aimidb.getReprocessNumber());
        assertEquals("P06", aimidb.getReplay());
        assertEquals(4, aimidb.getStartingTileColumnNumber());
        assertEquals(12345, aimidb.getStartingTileRowNumber());
        assertEquals("CD", aimidb.getEndSegment());
        assertEquals(19, aimidb.getEndTileColumn());
        assertEquals(34567, aimidb.getEndTileRow());
        assertEquals("AS", aimidb.getCountryCode());
        assertEquals("3520S14904E", aimidb.getLocation());
    }
}
