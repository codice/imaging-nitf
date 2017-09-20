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

import java.util.List;
import org.codice.imaging.nitf.core.tre.TreEntry;
import org.codice.imaging.nitf.core.tre.TreGroup;

/**
 * Builder for TreGroup.
 */
public class TreGroupBuilder {

    private final TreGroupImpl treGroup = new TreGroupImpl();

    /**
     * Constructor.
     */
    public TreGroupBuilder() { };

    /**
     * Add an entry to the group.
     *
     * @param entry the entry to add
     */
    public final void add(final TreEntry entry) {
        treGroup.add(entry);
    }

    /**
     * Add multiple entries to the group.
     *
     * @param group the group containing the entry or entries to add
     */
    public final void addAll(final TreGroup group) {
        treGroup.addAll(group);
    }

    /**
     * Set the list of entries.
     *
     * @param treEntries the new list of entries.
     */
    public final void setEntries(final List<TreEntry> treEntries) {
        treGroup.setEntries(treEntries);
    }

    /**
     * Get the built TreGroup.
     *
     * @return TreGroup from the builder operations.
     */
    public final TreGroup getTreGroup() {
        return treGroup;
    }
}
