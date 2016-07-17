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
package org.codice.imaging.nitf.core.tre;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
    Entry within a TRE.
    <p>
    This is a name and a value, or a name and a group of entries.
*/
public class TreEntry {

    private static final Logger LOG = LoggerFactory.getLogger(TreEntry.class);

    private String name = null;
    private String value = null;
    private String dataType = null;
    private List<TreGroup> groups = null;

    /**
     * Construct a TRE entry with a specific field name, field value and parent.
     * <p>
     * This is the simple (not-repeating) TRE entry form.
     *
     * @param fieldName the field name of the new TRE entry.
     * @param fieldValue the field value for the new TRE entry.
     * @param fieldType the data type ("string", "real", "UINT8", "integer") for the data
    */
    public TreEntry(final String fieldName, final String fieldValue, final String fieldType) {
        name = fieldName;
        value = fieldValue;
        dataType = fieldType;
    }

    /**
        Construct a TRE entry with a specific field name and parent.
        <p>
        This is the repeating TRE entry form of an entry, initialised with an empty group list.

        @param fieldName the field name of the new TRE entry.
    */
    public TreEntry(final String fieldName) {
        name = fieldName;
        initGroups();
    }

    /**
        Set the name of the TRE entry.

        @param fieldName the name of the TRE entry.
    */
    public final void setName(final String fieldName) {
        name = fieldName;
    }

    /**
        Return the name of the TRE entry.

        @return the name of the TRE entry
    */
    public final String getName() {
        return name;
    }

    /**
        Set the field value of the TRE entry.

        @param fieldValue the value of the TRE
    */
    public final void setFieldValue(final String fieldValue) {
        value = fieldValue;
    }

    /**
        Return the field value of the TRE entry.

        @return the value of the TRE
    */
    public final String getFieldValue() {
        return value;
    }

    /**
        Initialise the groups for this TRE entry.
    */
    public final void initGroups() {
        if (groups == null) {
            groups = new ArrayList<TreGroup>();
        }
    }

    /**
        Return the groups for this TRE entry.

        @return the groups for this TRE entry.
    */
    public final List<TreGroup> getGroups() {
        return groups;
    }

    /**
        Add a group to the groups in this TRE entry.

        @param group the group to add.
    */
    public final void addGroup(final TreGroup group) {
        groups.add(group);
    }

    /**
     * Return the data type for this TRE entry.
     *
     * This is only meaningful if it is a simple entry (not a nested group).
     *
     * @return the data type as a string, or null for a group.
     */
    public final String getDataType() {
        return dataType;
    }

    /**
        Debug dump of the TRE entry.
    */
    public final void dump() {
        LOG.debug("\tName: " + name);
        if (value != null) {
            LOG.debug("\tValue: " + value);
        } else if (groups != null) {
            for (TreGroup group : groups) {
                LOG.debug("\t--New Group--");
                group.dump();
                LOG.debug("\t--End Group--");
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String toString() {
        if (value != null) {
            return name + ": " + value;
        } else {
            return name;
        }
    }
}
