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
package org.codice.imaging.nitf.trewrap.fields;

import java.io.IOException;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Lookup for sensor-dependent ACFTB MPLAN TRE field.
 *
 * This is a singleton, use getInstance().
 */
public final class ACFTBMissionPlanMode extends SensorLookup {

    private static final Logger LOGGER = LoggerFactory.getLogger(ACFTBMissionPlanMode.class);

    private static final ACFTBMissionPlanMode INSTANCE = new ACFTBMissionPlanMode();

    /**
     * Constructor for this lookup class.
     */
    private ACFTBMissionPlanMode() {
        try (InputStream inputStream = ACFTBMissionPlanMode.class.getResourceAsStream("/ACFTB_MPLAN_sensor.xml")) {
            super.parseSensorLookup(inputStream);
        } catch (IOException e) {
            //This will only occur when inputStream.close() throws an exception.
            LOGGER.warn(e.getMessage(), e);
        }
    }

    /**
     * Get a (shared) instance of this lookup.
     * @return instance of ACFTBMissionPlanMode.
     */
    public static ACFTBMissionPlanMode getInstance() {
        return INSTANCE;
    }

    /**
     * Add a sensor / identifier lookup to this lookup.
     *
     * If the sensor / identifier combination already exists, this will replace the existing entry.
     *
     * @param sensorId the sensor id that will be looked up.
     * @param fieldValue field value that will be look up.
     * @param textDescription description corresponding to the field value.
     */
    public static void addLookupValue(final String sensorId, final int fieldValue, final String textDescription) {
        INSTANCE.registerNewLookup(sensorId, String.format("%03d", fieldValue), textDescription);
    }
}
