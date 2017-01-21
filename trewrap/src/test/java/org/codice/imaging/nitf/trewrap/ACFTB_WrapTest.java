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

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import javax.xml.stream.XMLStreamException;
import org.codice.imaging.nitf.core.common.NitfFormatException;
import org.codice.imaging.nitf.core.tre.Tre;
import org.codice.imaging.nitf.trewrap.fields.ACFTBMissionPlanMode;
import org.codice.imaging.nitf.trewrap.fields.ACFTBSceneSource;
import org.codice.imaging.nitf.trewrap.fields.ACFTBSensorId;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import uk.org.lidalia.slf4jtest.LoggingEvent;
import uk.org.lidalia.slf4jtest.TestLogger;
import uk.org.lidalia.slf4jtest.TestLoggerFactory;

/**
 * Tests for the ACFTB wrapper.
 */
public class ACFTB_WrapTest extends SharedTreTestSupport {

    TestLogger LOGGER = TestLoggerFactory.getTestLogger(TreWrapper.class);

    private final String mTestData = "ACFTB 00207FOOLS_MATE          0000000001201411030748HHFRACESHY00000002014110300000000000005+33.36200000+044.35100000000.00+22555f+33.36500000+044.35100000+23555045.0000000000u0000000u999.990000010001.00201411030000000";
    private final String mAlternateData = "ACFTB 00207NOT AVAILABLE                             HHFRDPY-1  0000002014110300000000000005-33.36200000-044.35100000030.00+22555m-33.36500000-044.35200000+23555045.00012.0001f03.4567m123.450000010001.00201611031234321";
    private final String mEmptyLocations = "ACFTB 00207NOT AVAILABLE                             HHFRDPY-1  0000002014110300000000000005                         000.00       +33.36500000+044.35100000             0000000u0000000u999.99                     0000000";
    private final String mDMSLocations =   "ACFTB 00207NOT AVAILABLE                             HHFRDPY-1  0000002014110300000000000005332143.2000S0442103.6000W000.00+22555f+33.36500000+044.35100000+23555045.0000000000u0000000u999.990000010001.00201411030000000";
    private final String mNonstandardLookup = "ACFTB 00207NOT AVAILABLE                             HHFRTESTZZ30000002014110300000000000006-33.36200000-044.35100000030.00+22555m-33.36500000-044.35200000+23555045.00012.0001f03.4567m123.450000010001.00201611031234321";

    public ACFTB_WrapTest() {
    }

    @Test
    public void testGetters() throws NitfFormatException, XMLStreamException, IOException {
        Tre tre = parseTRE(mTestData, "ACFTB ");
        ACFTB acftb = new ACFTB(tre);
        assertTrue(acftb.getValidity().isValid());
        assertEquals("FOOLS_MATE", acftb.getAircraftMissionIdentification());
        assertEquals("0000000001", acftb.getAircraftTailNumber());
        ZonedDateTime expectedTakeOffTime = ZonedDateTime.of(2014, 11, 3, 7, 48, 0, 0, ZoneId.of("UTC"));
        ZonedDateTime actualTakeOffTime = acftb.getAircraftTakeoff();
        assertEquals(expectedTakeOffTime, actualTakeOffTime);
        assertEquals("HHFR", acftb.getSensorIdentificationType());
        assertEquals("ACESHY", acftb.getSensorIdentification());
        assertEquals("Airborne Cueing and Exploitation System - Hyperspectral", acftb.getSensorIdentificationDecoded());
        assertEquals("0", acftb.getSceneSource());
        assertEquals("Pre-Planned", acftb.getSceneSourceDecoded());
        assertEquals(0, acftb.getSceneNumber());
        LocalDate expectedProcessingDate = LocalDate.of(2014, 11, 3);
        LocalDate actualProcessingDate = acftb.getProcessingDate();
        assertEquals(expectedProcessingDate, actualProcessingDate);
        assertEquals(0, acftb.getImmediateSceneHost());
        assertEquals(0, acftb.getImmediateSceneRequestId());
        assertEquals(5, acftb.getMissionPlanMode());
        assertEquals("EO Point Target", acftb.getMissionPlanModeDecoded());
        assertTrue(acftb.entryPointHasData());
        assertEquals(33.36200000, acftb.getEntryLocationLatitude(), 0.00000001);
        assertEquals(44.35100000, acftb.getEntryLocationLongitude(), 0.00000001);
        assertFalse(acftb.locationAccuracyIsKnown());
        assertEquals(0.0, acftb.getLocationAccuracy(), 0.01);
        assertEquals((Integer)22555, acftb.getEntryElevation());
        assertEquals("f", acftb.getUnitOfElevation());
        assertTrue(acftb.exitPointHasData());
        assertEquals(33.36500000, acftb.getExitLocationLatitude(), 0.00000001);
        assertEquals(44.35100000, acftb.getExitLocationLongitude(), 0.00000001);
        assertEquals((Integer)23555, acftb.getExitElevation());
        assertEquals(45.000, acftb.getTrueMapAngle(), 0.001);
        assertTrue(acftb.hasTrueMapAngle());
        assertFalse(acftb.rowSpacingIsKnown());
        assertEquals(0.0, acftb.getRowSpacing(), 0.0001);
        assertEquals("u", acftb.getRowSpacingUnits());
        assertFalse(acftb.columnSpacingIsKnown());
        assertEquals(0.0, acftb.getColumnSpacing(), 0.0001);
        assertEquals("u", acftb.getColumnSpacingUnits());
        assertFalse(acftb.focalLengthIsAvailable());
        assertEquals(999.99, acftb.getFocalLength(), 0.01);
        assertTrue(acftb.sensorSerialNumberIsValid());
        assertEquals(1, acftb.getSensorSerialNumber());
        assertEquals("0001.00", acftb.getAirborneSoftwareVersion());
        LocalDate expectedCalibrationDate = LocalDate.of(2014, 11, 3);
        LocalDate actualCalibrationDate = acftb.getCalibrationDate();
        assertEquals(expectedCalibrationDate, actualCalibrationDate);
        assertEquals(0, acftb.getPatchTotal());
        assertEquals(0, acftb.getMTITotal());
    }

    @Test
    public void testGettersAlternate() throws NitfFormatException, XMLStreamException, IOException {
        Tre tre = parseTRE(mAlternateData, "ACFTB ");
        ACFTB acftb = new ACFTB(tre);
        assertTrue(acftb.getValidity().isValid());
        assertEquals("NOT AVAILABLE", acftb.getAircraftMissionIdentification());
        assertEquals("", acftb.getAircraftTailNumber());
        assertThat(LOGGER.getLoggingEvents().isEmpty(), is(true));
        ZonedDateTime actualTakeOffTime = acftb.getAircraftTakeoff();
        assertNull(actualTakeOffTime);
        assertThat(LOGGER.getLoggingEvents(), is(Arrays.asList(LoggingEvent.debug("Could not parse              as a zoned date/time: Text '            ' could not be parsed at index 0"))));
        assertEquals("DPY-1", acftb.getSensorIdentification());
        // Likely a typo in the source data
        assertEquals("Lxyn Block 30 Radar", acftb.getSensorIdentificationDecoded());
        assertEquals("", acftb.getSceneSource());
        assertEquals("Not specified", acftb.getSceneSourceDecoded());
        assertEquals(5, acftb.getMissionPlanMode());
        assertEquals("Geo Strip", acftb.getMissionPlanModeDecoded());
        assertTrue(acftb.entryPointHasData());
        assertEquals(-33.36200000, acftb.getEntryLocationLatitude(), 0.00000001);
        assertEquals(-44.35100000, acftb.getEntryLocationLongitude(), 0.00000001);
        assertTrue(acftb.locationAccuracyIsKnown());
        assertEquals(30.0, acftb.getLocationAccuracy(), 0.01);
        assertEquals("m", acftb.getUnitOfElevation());
        assertEquals(-33.36500000, acftb.getExitLocationLatitude(), 0.00000001);
        assertEquals(-44.35200000, acftb.getExitLocationLongitude(), 0.00000001);
        assertTrue(acftb.rowSpacingIsKnown());
        assertTrue(acftb.columnSpacingIsKnown());
        assertEquals(12.0001, acftb.getRowSpacing(), 0.0001);
        assertEquals("f", acftb.getRowSpacingUnits());
        assertEquals(3.4567, acftb.getColumnSpacing(), 0.0001);
        assertEquals("m", acftb.getColumnSpacingUnits());
        assertTrue(acftb.focalLengthIsAvailable());
        assertEquals(123.45, acftb.getFocalLength(), 0.01);
        LocalDate expectedCalibrationDate = LocalDate.of(2016, 11, 3);
        LocalDate actualCalibrationDate = acftb.getCalibrationDate();
        assertEquals(expectedCalibrationDate, actualCalibrationDate);
        assertEquals(1234, acftb.getPatchTotal());
        assertEquals(321, acftb.getMTITotal());
    }

    @Test
    public void testEmptyGetters() throws NitfFormatException, XMLStreamException, IOException {
        Tre tre = parseTRE(mEmptyLocations, "ACFTB ");
        ACFTB acftb = new ACFTB(tre);
        assertTrue(acftb.getValidity().isValid());
        assertFalse(acftb.entryPointHasData());
        assertEquals(0.0, acftb.getEntryLocationLatitude(), 0.00000001);
        assertEquals(0.0, acftb.getEntryLocationLongitude(), 0.00000001);
        assertNull(acftb.getEntryElevation());
        assertEquals(" ", acftb.getUnitOfElevation());
        assertNull(acftb.getExitElevation());
        assertFalse(acftb.hasTrueMapAngle());
        assertEquals(ACFTB.INVALID_TRUE_MAP_ANGLE, acftb.getTrueMapAngle(), 0.001);
        assertFalse(acftb.sensorSerialNumberIsValid());
        assertEquals(0, acftb.getSensorSerialNumber());
        assertEquals("", acftb.getAirborneSoftwareVersion());
        assertNull(acftb.getCalibrationDate());
    }

    @Test
    public void testGettersDMS() throws NitfFormatException, XMLStreamException, IOException {
        Tre tre = parseTRE(mDMSLocations, "ACFTB ");
        ACFTB acftb = new ACFTB(tre);
        assertTrue(acftb.getValidity().isValid());
        assertTrue(acftb.entryPointHasData());
        assertEquals(-33.36200000, acftb.getEntryLocationLatitude(), 0.00000001);
        assertEquals(-44.35100000, acftb.getEntryLocationLongitude(), 0.00000001);
    }

    @Test
    public void testGettersNonStandard() throws NitfFormatException, XMLStreamException, IOException {
        Tre tre = parseTRE(mNonstandardLookup, "ACFTB ");
        ACFTB acftb = new ACFTB(tre);
        assertTrue(acftb.getValidity().isValid());
        assertEquals("TESTZZ", acftb.getSensorIdentification());
        assertNull(acftb.getSensorIdentificationDecoded());
        ACFTBSensorId.addLookupValue("TESTZZ", "A test sensor identification value");
        assertEquals("A test sensor identification value", acftb.getSensorIdentificationDecoded());
        assertEquals(6, acftb.getMissionPlanMode());
        // This just picks up the default "General EO/IR" sensor mission modes
        assertEquals("EO Wide Area Search", acftb.getMissionPlanModeDecoded());
        ACFTBMissionPlanMode.addLookupValue("TESTZZ", 6, "Extra Mission Mode Test Case");
        assertEquals("Extra Mission Mode Test Case", acftb.getMissionPlanModeDecoded());
        assertEquals("3", acftb.getSceneSource());
        assertNull(acftb.getSceneSourceDecoded());
        ACFTBSceneSource.addLookupValue("TESTZZ", 3, "Some Test Source");
        assertEquals("Some Test Source", acftb.getSceneSourceDecoded());
    }
}
