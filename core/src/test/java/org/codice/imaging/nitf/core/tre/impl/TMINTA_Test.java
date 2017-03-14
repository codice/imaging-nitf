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

import org.codice.imaging.nitf.core.tre.Tre;
import org.junit.Test;

/**
 * Unit tests for TMINTA TRE.
 *
 * TMINTA is defined in MIE4NITF Version 1.1, Section 5.9.3.4 Time Interval Definition TRE (TMINTA) and Table 13.
 */
public class TMINTA_Test extends SharedTreTest {

    public TMINTA_Test() {
    }

    @Test
    public void SimpleTMINTA() throws Exception {
        String treTag = "TMINTA";
        String testData = "TMINTA00058000100000220160819201946.80225902620160819201948.728872709";
        int expectedLength = treTag.length() + "00058".length() + 58;

        Tre tminta = parseTRE(testData, expectedLength, treTag);
        assertEquals(2, tminta.getEntries().size());
        assertEquals(1, tminta.getIntValue("NUM_TIME_INT"));
        assertEquals(1, tminta.getEntry("TIME INTERVALS").getGroups().size());
        assertEquals(2, tminta.getEntry("TIME INTERVALS").getGroups().get(0).getIntValue("TIME_INTERVAL_INDEX"));
        assertEquals("20160819201946.802259026", tminta.getEntry("TIME INTERVALS").getGroups().get(0).getFieldValue("START_TIMESTAMP"));
        assertEquals("20160819201948.728872709", tminta.getEntry("TIME INTERVALS").getGroups().get(0).getFieldValue("END_TIMESTAMP"));
    }

    @Test
    public void MultiIntervalTMINTA() throws Exception {
        String treTag = "TMINTA";
        String testData = "TMINTA00112000200000320160819201946.80225902620160819201948.72887270900000420160819203319.69098424120160819203321.557468116";
        int expectedLength = treTag.length() + "00112".length() + 112;

        Tre tminta = parseTRE(testData, expectedLength, treTag);
        assertEquals(2, tminta.getEntries().size());
        assertEquals(2, tminta.getIntValue("NUM_TIME_INT"));
        assertEquals(2, tminta.getEntry("TIME INTERVALS").getGroups().size());
        assertEquals(3, tminta.getEntry("TIME INTERVALS").getGroups().get(0).getIntValue("TIME_INTERVAL_INDEX"));
        assertEquals("20160819201946.802259026", tminta.getEntry("TIME INTERVALS").getGroups().get(0).getFieldValue("START_TIMESTAMP"));
        assertEquals("20160819201948.728872709", tminta.getEntry("TIME INTERVALS").getGroups().get(0).getFieldValue("END_TIMESTAMP"));
        assertEquals(4, tminta.getEntry("TIME INTERVALS").getGroups().get(1).getIntValue("TIME_INTERVAL_INDEX"));
        assertEquals("20160819203319.690984241", tminta.getEntry("TIME INTERVALS").getGroups().get(1).getFieldValue("START_TIMESTAMP"));
        assertEquals("20160819203321.557468116", tminta.getEntry("TIME INTERVALS").getGroups().get(1).getFieldValue("END_TIMESTAMP"));
    }
}
