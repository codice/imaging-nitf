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
 * Field lookup for ACFTB SENSOR_ID fields values.
 *
 * This is a singleton, use getInstance().
 */
public final class ACFTBSensorId extends SimpleLookup {

    private static final Logger LOGGER = LoggerFactory.getLogger(ACFTBSensorId.class);

    private static final ACFTBSensorId INSTANCE = new ACFTBSensorId();

    /**
     * Private constructor for this lookup class.
     */
    private ACFTBSensorId() {
        try (InputStream inputStream = ACFTBSensorId.class.getResourceAsStream("/ACFTB_SENSOR_ID.xml")) {
            super.parseSimpleLookup(inputStream);
        } catch (IOException e) {
            //This will only occur when inputStream.close() throws an exception.
            LOGGER.warn(e.getMessage(), e);
        }
    }

    /**
     * Get a (shared) instance of this lookup.
     * @return instance of ACFTBSensorId.
     */
    public static ACFTBSensorId getInstance() {
        return INSTANCE;
    }

    /**
     * Add a identifier lookup to this lookup.
     *
     * If the identifier already exists, this will replace the existing entry.
     *
     * @param fieldValue field value that will be look up.
     * @param textDescription description corresponding to the field value.
     */
    public static void addLookupValue(final String fieldValue, final String textDescription) {
        INSTANCE.registerNewLookup(fieldValue, textDescription);
    }
}
