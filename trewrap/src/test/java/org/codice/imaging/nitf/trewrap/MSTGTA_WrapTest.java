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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 * Tests for the MSTGTA wrapper.
 */
public class MSTGTA_WrapTest extends SharedTreTestSupport {

    private final String mTestData =  "MSTGTA0010100006ABC123DEF456789XYZ654321POI002The Boss.   2018111623591490231084632Z+01634m+30.482261-086.503262";
    private final String mVertData =  "MSTGTA0010100006ABC123DEF456789XYZ654321POI002The Boss.   2018111623591090231084632Z+01634m302856.14N0863011.74W";
    private final String mFwrdData =  "MSTGTA0010100006ABC123DEF456789XYZ654321POI002The Boss.   2018111623592190231084632Z+01634m302856.14S0863011.74E";
    private final String mRghtData =  "MSTGTA0010100006ABC123DEF456789XYZ654321POI002The Boss.   2018111623590290231084632Z+01634m+30.482261-086.503262";
    private final String mLeftData =  "MSTGTA0010100006ABC123DEF456789XYZ654321POI002The Boss.   2018111623590390231084632Z+01634m+30.482261-086.503262";
    private final String mSpaceData = "MSTGTA0010100000                                                       8                                        ";

    public MSTGTA_WrapTest() {
    }

    @Test
    public final void testGetters() throws NitfFormatException {
        Tre tre = parseTRE(mTestData, "MSTGTA");
        MSTGTA mstgta = new MSTGTA(tre);
        assertTrue(mstgta.getValidity().isValid());

        assertEquals(6, mstgta.getTargetNumber());
        assertEquals("ABC123DEF456", mstgta.getTargetIdentifier());
        assertEquals("789XYZ654321POI", mstgta.getTargetBE());
        assertEquals("002", mstgta.getTargetPriority());
        assertEquals("The Boss.", mstgta.getTargetRequester());
        assertEquals(ZonedDateTime.of(2018, 11, 16, 23, 59, 0, 0, ZoneId.of("UTC")), mstgta.getLatestTimeInformationOfValue());
        assertEquals("1", mstgta.getPrePlannedTargetTypeEncoded());
        assertEquals("strip", mstgta.getPrePlannedTargetType());
        assertEquals(4, mstgta.getPrePlannedCollectionTechniqueEncoded());
        assertEquals("best possible", mstgta.getPrePlannedCollectionTechnique());
        assertEquals("90231", mstgta.getTargetFunctionalCategoryCode());
        assertEquals("084632Z", mstgta.getPlannedTimeAtTarget());
        assertEquals("+01634", mstgta.getTargetElevation());
        assertEquals("m", mstgta.getTargetElevationUnits());
        assertEquals("+30.482261-086.503262", mstgta.getTargetLocationRaw());
        assertEquals(30.482261, mstgta.getTargetLocationLatitude(), 0.000001);
        assertEquals(-86.503262, mstgta.getTargetLocationLongitude(), 0.000001);
    }

    @Test
    public final void testSpaces() throws NitfFormatException {
        Tre tre = parseTRE(mSpaceData, "MSTGTA");
        MSTGTA mstgta = new MSTGTA(tre);
        assertFalse(mstgta.getValidity().isValid());

        assertEquals(0, mstgta.getTargetNumber());
        assertEquals("", mstgta.getTargetIdentifier());
        assertEquals("", mstgta.getTargetBE());
        assertEquals("", mstgta.getTargetPriority());
        assertEquals("", mstgta.getTargetRequester());
        assertNull(mstgta.getLatestTimeInformationOfValue());
        assertEquals("", mstgta.getPrePlannedTargetTypeEncoded());
        assertEquals("unknown", mstgta.getPrePlannedTargetType());
        assertEquals(8, mstgta.getPrePlannedCollectionTechniqueEncoded());
        assertEquals("unknown", mstgta.getPrePlannedCollectionTechnique());
        assertEquals("", mstgta.getTargetFunctionalCategoryCode());
        assertEquals("", mstgta.getPlannedTimeAtTarget());
        assertEquals("", mstgta.getTargetElevation());
        assertEquals("", mstgta.getTargetElevationUnits());
        assertTrue(mstgta.getTargetLocationRaw().trim().isEmpty());
        assertEquals(0.0, mstgta.getTargetLocationLatitude(), 0.000001);
        assertEquals(0.0, mstgta.getTargetLocationLongitude(), 0.000001);
    }

    @Test
    public final void testVertical() throws NitfFormatException {
        Tre tre = parseTRE(mVertData, "MSTGTA");
        MSTGTA mstgta = new MSTGTA(tre);
        assertTrue(mstgta.getValidity().isValid());
        assertEquals("1", mstgta.getPrePlannedTargetTypeEncoded());
        assertEquals("strip", mstgta.getPrePlannedTargetType());
        assertEquals(0, mstgta.getPrePlannedCollectionTechniqueEncoded());
        assertEquals("vertical", mstgta.getPrePlannedCollectionTechnique());
        assertEquals("302856.14N0863011.74W", mstgta.getTargetLocationRaw());
        assertEquals(30.482261, mstgta.getTargetLocationLatitude(), 0.000001);
        assertEquals(-86.503262, mstgta.getTargetLocationLongitude(), 0.000001);
    }

    @Test
    public final void testForward() throws NitfFormatException {
        Tre tre = parseTRE(mFwrdData, "MSTGTA");
        MSTGTA mstgta = new MSTGTA(tre);
        assertTrue(mstgta.getValidity().isValid());
        assertEquals("2", mstgta.getPrePlannedTargetTypeEncoded());
        assertEquals("area", mstgta.getPrePlannedTargetType());
        assertEquals(1, mstgta.getPrePlannedCollectionTechniqueEncoded());
        assertEquals("forward oblique", mstgta.getPrePlannedCollectionTechnique());

        assertEquals("302856.14S0863011.74E", mstgta.getTargetLocationRaw());
        assertEquals(-30.482261, mstgta.getTargetLocationLatitude(), 0.000001);
        assertEquals(86.503262, mstgta.getTargetLocationLongitude(), 0.000001);
    }

    @Test
    public final void testRight() throws NitfFormatException {
        Tre tre = parseTRE(mRghtData, "MSTGTA");
        MSTGTA mstgta = new MSTGTA(tre);
        assertTrue(mstgta.getValidity().isValid());
        assertEquals("0", mstgta.getPrePlannedTargetTypeEncoded());
        assertEquals("point", mstgta.getPrePlannedTargetType());
        assertEquals(2, mstgta.getPrePlannedCollectionTechniqueEncoded());
        assertEquals("right oblique", mstgta.getPrePlannedCollectionTechnique());
    }

    @Test
    public final void testLeft() throws NitfFormatException {
        Tre tre = parseTRE(mLeftData, "MSTGTA");
        MSTGTA mstgta = new MSTGTA(tre);
        assertTrue(mstgta.getValidity().isValid());
        assertEquals("0", mstgta.getPrePlannedTargetTypeEncoded());
        assertEquals("point", mstgta.getPrePlannedTargetType());
        assertEquals(3, mstgta.getPrePlannedCollectionTechniqueEncoded());
        assertEquals("left oblique", mstgta.getPrePlannedCollectionTechnique());
    }
}
