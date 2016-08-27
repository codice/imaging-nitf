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

import java.time.ZoneId;
import java.time.ZonedDateTime;
import org.codice.imaging.nitf.core.common.NitfFormatException;
import org.codice.imaging.nitf.core.tre.Tre;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 * Tests for the MTIMFA wrapper.
 */
public class MTIMFA_WrapTest extends SharedTreTestSupport {

    private final String mTestData = "MTIMFA00138e6feb060-918c-4253-97e4-472e8756bd28003000004001dc4cb54e-1d97-4917-b13a-c25a880c702500120160827145527.89067263320160827145615.123184020007";
    private final String mSpaceFilledImageSegIndexData = "MTIMFA00138e6feb060-918c-4253-97e4-472e8756bd28003000004001dc4cb54e-1d97-4917-b13a-c25a880c702500120160827145527.89067263320160827145615.123184020   ";
    private final String mSpaceFilledEndTimestampData = "MTIMFA00138e6feb060-918c-4253-97e4-472e8756bd28003000004001dc4cb54e-1d97-4917-b13a-c25a880c702500120160827145527.890672633                           ";
    private final String mSpaceFilledStartTimestampData = "MTIMFA00138e6feb060-918c-4253-97e4-472e8756bd28003000004001dc4cb54e-1d97-4917-b13a-c25a880c7025001                                                   ";

    public MTIMFA_WrapTest() {
    }

    @Test
    public void testGetters() throws NitfFormatException {
        Tre tre = parseTRE(mTestData, "MTIMFA");
        MTIMFA mtimfa = new MTIMFA(tre);
        assertTrue(mtimfa.getValidity().isValid());

        assertEquals("e6feb060-918c-4253-97e4-472e8756bd28", mtimfa.getLayerId());
        assertEquals(3, mtimfa.getCameraSetIndex());
        assertEquals(4, mtimfa.getTimeIntervalIndex());
        assertEquals(1, mtimfa.getNumberOfCameras());
        assertEquals("dc4cb54e-1d97-4917-b13a-c25a880c7025", mtimfa.getCameraID(0));
        assertEquals(1, mtimfa.getNumTemporalBlocks(0));
        ZonedDateTime expectedStartTimestamp = ZonedDateTime.of(2016, 8, 27, 14, 55, 27, 890672633, ZoneId.of("UTC"));
        assertEquals(expectedStartTimestamp, mtimfa.getStartTimestamp(0, 0));
        ZonedDateTime expectedEndTimestamp = ZonedDateTime.of(2016, 8, 27, 14, 56, 15, 123184020, ZoneId.of("UTC"));
        assertEquals(expectedEndTimestamp, mtimfa.getEndTimestamp(0, 0));
        assertEquals(7, mtimfa.getImageSegmentIndex(0, 0));
        assertTrue(mtimfa.hasValidTimestamps(0, 0));
    }

    @Test
    public void testSpaceFill1() throws NitfFormatException {
        Tre tre = parseTRE(mSpaceFilledImageSegIndexData, "MTIMFA");
        MTIMFA mtimfa = new MTIMFA(tre);
        assertTrue(mtimfa.getValidity().isValid());

        assertFalse(mtimfa.hasValidTimestamps(0, 0));
    }

    @Test
    public void testSpaceFill2() throws NitfFormatException {
        Tre tre = parseTRE(mSpaceFilledEndTimestampData, "MTIMFA");
        MTIMFA mtimfa = new MTIMFA(tre);
        assertTrue(mtimfa.getValidity().isValid());

        assertFalse(mtimfa.hasValidTimestamps(0, 0));
    }

    @Test
    public void testSpaceFill3() throws NitfFormatException {
        Tre tre = parseTRE(mSpaceFilledStartTimestampData, "MTIMFA");
        MTIMFA mtimfa = new MTIMFA(tre);
        assertTrue(mtimfa.getValidity().isValid());

        assertFalse(mtimfa.hasValidTimestamps(0, 0));
    }
}
