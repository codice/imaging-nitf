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
import org.codice.imaging.nitf.core.tre.Tre;
import org.codice.imaging.nitf.core.tre.TreEntry;
import org.codice.imaging.nitf.core.tre.TreSource;

/**
 * Builder for TREs (Tagged Record Extensions).
 */
public class TreBuilder {

    private final TreImpl treImpl;

    /**
     * Create a new builder.
     *
     * @param tag the name for the TRE.
     * @param source the TreSource associated with this TRE
     */
    public TreBuilder(final String tag, final TreSource source) {
        treImpl = new TreImpl(tag, source);
    };

    /**
     * Create a new builder from an existing TRE.
     *
     * @param tre the TRE to copy from.
     */
    public TreBuilder(final Tre tre) {
        treImpl = (TreImpl) tre;
    }

    /**
     * Set the metadata prefix format string.
     *
     * @param mdPrefix the metadata prefix.
     */
    public final void setPrefix(final String mdPrefix) {
        treImpl.setPrefix(mdPrefix);
    }

    /**
     * Set the raw data for this TRE.
     * <p>
     * This is only used for TREs that we couldn't parse.
     *
     * @param treDataRaw the raw bytes for the TRE.
     */
    public final void setRawData(final byte[] treDataRaw) {
        treImpl.setRawData(treDataRaw);
    }

    /**
     * Add an entry to the TRE.
     *
     * @param entry the entry to add
     */
    public final void add(final TreEntry entry) {
        treImpl.add(entry);
    }

    /**
     * Set the list of entries in the TRE.
     *
     * @param treEntries the new list of entries.
     */
    public final void setEntries(final List<TreEntry> treEntries) {
        treImpl.setEntries(treEntries);
    }

    /**
     * Get the list of entries in the TRE.
     *
     * @return the list of entries.
     */
    public final List<TreEntry> getEntries() {
        return treImpl.getEntries();
    }

    /**
     * Return the built TRE.
     *
     * @return the TRE that has been built.
     */
    public final Tre getTre() {
        return treImpl;
    }
}
