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

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Field / description lookup for sensor specific TRE fields.
 */
public class SensorLookup {
    private final Map<String, Map<String, String>> sensorMap = new HashMap<>();
    private String mField;
    private String mTre;

    private static final Logger LOGGER = LoggerFactory.getLogger(SensorLookup.class);

    /**
     * Construct new lookup.
     *
     * @param xmlStream  stream of XML containing lookup entries.
     */
    public SensorLookup(final InputStream xmlStream) {
        parseSensorLookup(xmlStream);
    }

    private void parseSensorLookup(final InputStream xmlStream) {
        try {
            XMLStreamReader reader = XMLInputFactory.newInstance().createXMLStreamReader(xmlStream);
            reader.next();
            mTre = reader.getAttributeValue("", "tre");
            mField = reader.getAttributeValue("", "field");
            String sensor = null;
            while (reader.hasNext()) {
                reader.next();
                if (reader.getEventType() == XMLStreamConstants.START_ELEMENT) {
                    if ("Sensor".equals(reader.getLocalName())) {
                        sensor = reader.getAttributeValue("", "ident");
                        sensorMap.put(sensor, new HashMap<>());
                    }
                    if ("Value".equals(reader.getLocalName())) {
                        String value = reader.getAttributeValue("", "value");
                        String description = reader.getAttributeValue("", "description");
                        sensorMap.get(sensor).put(value, description);
                    }
                }
            }
        } catch (XMLStreamException ex) {
            LOGGER.warn(String.format("Problem parsing XML for %s:%s. %s", mTre, mField, ex.toString()));
        }
    }

    /**
     * Get the description for this sensor / field combination with default fallback.
     *
     * @param sensorId the sensor value
     * @param field the value to look up
     * @param defaultDescription default description to return if not found
     * @return the description corresponding to the field, or the default description if not found.
     */
    public final String lookupDescription(final String sensorId, final String field, final String defaultDescription) {
        String description = lookupDescription(sensorId, field);
        if (description == null) {
            return defaultDescription;
        } else {
            return description;
        }
    }

    /**
     * Get the description for this sensor / field combination.
     *
     * @param sensorId the sensor value
     * @param field the value to look up
     * @return the description corresponding to the field, or null if not found.
     */
    public final String lookupDescription(final String sensorId, final String field) {
        if (!sensorMap.containsKey(sensorId)) {
            return null;
        }
        Map<String, String> mapForSensor = sensorMap.get(sensorId);
        return mapForSensor.getOrDefault(field, null);
    }

    /**
     * Get the set of known sensors for this lookup.
     *
     * @return Set of String identifying each known sensor id.
     */
    public final Set<String> getKnownSensors() {
        return sensorMap.keySet();
    }

    /**
     * Add a sensor / identifier lookup to this lookup.
     *
     * If the sensor / identifier combination already exists, this will replace the existing entry.
     *
     * @param sensorId sensor identifier that will be looked up.
     * @param fieldValue field value that will be looked up.
     * @param textDescription description corresponding to the field value.
     */
    protected final void registerNewLookup(final String sensorId, final String fieldValue, final String textDescription) {
        if (!getKnownSensors().contains(sensorId)) {
            sensorMap.put(sensorId, new HashMap<>());
        }
        sensorMap.get(sensorId).put(fieldValue, textDescription);
    }
}
