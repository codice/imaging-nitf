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
package org.codice.imaging.nitf.core;

import java.util.ArrayList;
import java.util.List;

public class TreCollection {
    private List<Tre> treCollectionEntries = new ArrayList<Tre>();

    public final List<Tre> getTREs() {
        return treCollectionEntries;
    }

    public final void add(final Tre tre) {
        treCollectionEntries.add(tre);
    }

    public final void add(final TreCollection collectionToAdd) {
        treCollectionEntries.addAll(collectionToAdd.getTREs());
    }

    public final List<String> getUniqueNamesOfTRE() {
        List<String> treNames = new ArrayList<String>();
        for (Tre tre : treCollectionEntries) {
            String treName = tre.getName();
            if (!treNames.contains(treName)) {
                treNames.add(treName);
            }
        }
        return treNames;
    }

    public final List<Tre> getTREsWithName(final String nameToMatch) {
        List<Tre> tres = new ArrayList<Tre>();
        for (Tre tre : treCollectionEntries) {
            String treName = tre.getName();
            if (treName.equals(nameToMatch)) {
                tres.add(tre);
            }
        }
        return tres;
    }

    public final boolean hasTREs() {
        return !treCollectionEntries.isEmpty();
    }
}
