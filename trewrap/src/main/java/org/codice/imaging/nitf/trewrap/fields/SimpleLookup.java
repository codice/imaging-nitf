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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

/**
 * Basic field / description lookup for simple (non-sensor specific) TRE fields.
 */
public class SimpleLookup {

    private Map<String, String> fieldToDescriptionMap = new HashMap<>();
    private String mField;
    private String mTre;

    private static final Logger LOGGER = Logger.getLogger(SimpleLookup.class.getName());

    /**
     * Internal constructor.
     */
    protected SimpleLookup() {
    }

    /**
     * Build lookup map from XML representation.
     *
     * The lookup can be manually configured, but typically most of the data will be read from an existing source. This
     * method provides reading from XML in a specific format that is generated from the main registry (see NITF
     * Registry Values Parser module). The XML is stored as resources in the TRE Wrapper module.
     *
     * @param xmlStream the XML to read.
     */
    public SimpleLookup(final InputStream xmlStream) {
        parseSimpleLookup(xmlStream);
    }

    /**
     * Parses simple lookup data from an XML stream.
     *
     * @param xmlStream - an InputStream containing the simple lookup data.
     */
    protected final void parseSimpleLookup(final InputStream xmlStream) {
        try {
            XMLStreamReader reader = XMLInputFactory.newInstance().createXMLStreamReader(xmlStream);
            reader.next();
            mTre = reader.getAttributeValue("", "tre");
            mField = reader.getAttributeValue("", "field");
            while (reader.hasNext()) {
                reader.next();
                if (reader.getEventType() == XMLStreamConstants.START_ELEMENT) {
                    String value = reader.getAttributeValue("", "value");
                    String description = reader.getAttributeValue("", "description");
                    fieldToDescriptionMap.put(value, description);
                }
            }
        } catch (XMLStreamException ex) {
            LOGGER.log(Level.WARNING, String.format("Problem parsing XML for %s:%s. %s", mTre, mField, ex.toString()));
        }
    }

    /**
     * Get the description for this field.
     * @param fieldValue the value to look up
     * @return the description corresponding to the field, or null if not found.
     */
    public final String lookupDescription(final String fieldValue) {
        return fieldToDescriptionMap.getOrDefault(fieldValue, null);
    }

    /**
     * Add a identifier lookup to this lookup.
     *
     * If the identifier already exists, this will replace the existing entry.
     *
     * @param fieldValue field value that will be look up.
     * @param textDescription description corresponding to the field value.
     */
    protected final void registerNewLookup(final String fieldValue, final String textDescription) {
        fieldToDescriptionMap.put(fieldValue, textDescription);
    }

}
