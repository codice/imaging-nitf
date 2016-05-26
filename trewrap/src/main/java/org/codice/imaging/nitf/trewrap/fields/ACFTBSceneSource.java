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

/**
 * Lookup for sensor-dependent ACFTB SCENE_SOURCE TRE field.
 *
 * This is a singleton, use getInstance().
 */
public final class ACFTBSceneSource extends SensorLookup {

    private static final ACFTBSceneSource INSTANCE = new ACFTBSceneSource();

    /**
     * Constructor for this lookup class.
     */
    private ACFTBSceneSource() {
        super(ACFTBSceneSource.class.getResourceAsStream("/ACFTB_SENSOR_SOURCE_sensor.xml"));
    }

    /**
     * Get a (shared) instance of this lookup.
     * @return instance of ACFTBSceneSource.
     */
    public static ACFTBSceneSource getInstance() {
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
        INSTANCE.registerNewLookup(sensorId, String.format("%d", fieldValue), textDescription);
    }
}
