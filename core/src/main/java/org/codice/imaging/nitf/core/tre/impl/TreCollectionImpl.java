/**
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
 **/
package org.codice.imaging.nitf.core.tre.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.codice.imaging.nitf.core.tre.Tre;
import org.codice.imaging.nitf.core.tre.TreCollection;
import org.codice.imaging.nitf.core.tre.TreSource;

/**
    Collection of TREs.
*/
class TreCollectionImpl implements TreCollection {
    private List<Tre> treCollectionEntries;

    TreCollectionImpl(final List<Tre> entries) {
        treCollectionEntries = Collections.unmodifiableList(entries);
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public final List<Tre> getTREs() {
        return treCollectionEntries;
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public final List<String> getUniqueNamesOfTRE() {
        List<String> treNames = new ArrayList<String>();
        for (Tre tre : treCollectionEntries) {
            String treName = tre.getName();
            if (!treNames.contains(treName)) {
                treNames.add(treName);
            }
        }
        return Collections.unmodifiableList(treNames);
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public final List<Tre> getTREsWithName(final String nameToMatch) {
        List<Tre> tres = new ArrayList<Tre>();
        for (Tre tre : treCollectionEntries) {
            String treName = tre.getName();
            if (treName.equals(nameToMatch)) {
                tres.add(tre);
            }
        }
        return Collections.unmodifiableList(tres);
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public final boolean hasTREs() {
        return !treCollectionEntries.isEmpty();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String toString() {
        return "TRE Collection";
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public final List<Tre> getTREsForSource(final TreSource source) {
        List<Tre> tres = new ArrayList<Tre>();
        for (Tre tre : treCollectionEntries) {
            if (tre.getSource() == source) {
                tres.add(tre);
            }
        }
        return Collections.unmodifiableList(tres);
    }
}
