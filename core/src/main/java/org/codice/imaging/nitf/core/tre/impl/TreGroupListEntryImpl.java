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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.codice.imaging.nitf.core.tre.TreGroup;
import org.codice.imaging.nitf.core.tre.TreGroupListEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Group list entry within a TRE.
 *
 * This is a name and a group of entries.
*/
public class TreGroupListEntryImpl extends TreEntryImpl implements TreGroupListEntry {

    // This is intentional - we want to log to the parent logger.
    private static final Logger LOG = LoggerFactory.getLogger(TreEntryImpl.class);

    private List<TreGroup> groups = null;

    /**
        Construct a TRE entry with a specific field name and parent.
        <p>
        This is the repeating TRE entry form of an entry, initialised with an empty group list.

        @param fieldName the field name of the new TRE entry.
    */
    public TreGroupListEntryImpl(final String fieldName) {
        setName(fieldName);
        initGroups();
    }

    /**
     * Initialise the groups for this TRE entry.
     */
    public final void initGroups() {
        if (groups == null) {
            groups = new ArrayList<>();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final List<TreGroup> getGroups() {
        return Collections.unmodifiableList(groups);
    }

    /**
     * Add a group to the groups in this TRE entry.
     *
     * @param group the group to add.
     */
    final void addGroup(final TreGroup group) {
        groups.add(group);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void dump() {
        LOG.debug("\tName: " + getName());
        if (groups != null) {
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
        return getName();
    }
}
