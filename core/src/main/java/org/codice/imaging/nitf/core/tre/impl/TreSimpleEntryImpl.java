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

import org.codice.imaging.nitf.core.tre.TreSimpleEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Simple key-value entry within a TRE.
 */
public class TreSimpleEntryImpl extends TreEntryImpl implements TreSimpleEntry {

    // This is intentional - we want to log to the parent logger.
    private static final Logger LOG = LoggerFactory.getLogger(TreEntryImpl.class);

    private String value = null;
    private String dataType = null;

    /**
     * Construct a TRE entry with a specific field name, field value and parent.
     * <p>
     * This is the simple (not-repeating) TRE entry form.
     *
     * @param fieldName the field name of the new TRE entry.
     * @param fieldValue the field value for the new TRE entry.
     * @param fieldType the data type ("string", "real", "UINT", "integer") for the data
    */
    public TreSimpleEntryImpl(final String fieldName, final String fieldValue, final String fieldType) {
        setName(fieldName);
        value = fieldValue;
        dataType = fieldType;
    }

    /**
        Set the field value of the TRE entry.

        @param fieldValue the value of the TRE
    */
    public final void setFieldValue(final String fieldValue) {
        value = fieldValue;
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public final String getFieldValue() {
        return value;
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public final String getDataType() {
        return dataType;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void dump() {
        LOG.debug("\tName: " + getName());
        LOG.debug("\tValue: " + value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String toString() {
        if (value != null) {
            return getName() + ": " + value;
        } else {
            return getName();
        }
    }
}
