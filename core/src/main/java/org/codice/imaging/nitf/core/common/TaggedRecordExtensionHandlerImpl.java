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
package org.codice.imaging.nitf.core.common;

import org.codice.imaging.nitf.core.tre.Tre;
import org.codice.imaging.nitf.core.tre.TreCollection;
import org.codice.imaging.nitf.core.tre.TreEntry;
import org.codice.imaging.nitf.core.tre.TreGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Common data elements for NITF segments and file header.
 */
public abstract class TaggedRecordExtensionHandlerImpl implements TaggedRecordExtensionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(TaggedRecordExtensionHandler.class);

    private final TreCollection treCollection = new TreCollection();

    /**
     * {@inheritDoc}
     */
    public final TreCollection getTREsRawStructure() {
        return treCollection;
    }

    /**
        Return the TREs for this segment, flattened into a key-value pair map.
        <p>
        This will build appropriate namespaces for each entry.

        @return TRE map.
    */
    public final Map<String, String> getTREsFlat() {
        Map<String, String> tresFlat = new TreeMap<String, String>();
        for (String treName : treCollection.getUniqueNamesOfTRE()) {
            List<Tre> tresWithName = treCollection.getTREsWithName(treName);
            if (tresWithName.size() == 1) {
                flattenOnlyTre(tresWithName.get(0), tresFlat);
            } else {
                for (int i = 0; i < tresWithName.size(); ++i) {
                    flattenThisTre(tresWithName.get(i), i, tresFlat);
                }
            }
        }
        return tresFlat;
    }

    /**
        Flatten out a single TRE.

        @param onlyTre the TRE to flatten.
        @param tresFlat the map to flatten the TRE into.
    */
    private void flattenOnlyTre(final Tre onlyTre, final Map<String, String> tresFlat) {
        List<TreEntry> treEntries = onlyTre.getEntries();
        for (TreEntry treEntry : treEntries) {
            flattenOneTreEntry(tresFlat, treEntry, onlyTre.getName());
        }
    }

    /**
        Flatten out a single TRE, where other TREs have the same name.

        @param thisTre the TRE to flatten.
        @param i the index of this TRE (zero base), required for namespacing.
        @param tresFlat the map to flatten the TRE into.
    */
    private void flattenThisTre(final Tre thisTre, final int i, final Map<String, String> tresFlat) {
        List<TreEntry> treEntries = thisTre.getEntries();
        for (TreEntry treEntry : treEntries) {
            tresFlat.put(String.format("%s_%d_%s", thisTre.getName(), i, treEntry.getName()), treEntry.getFieldValue().trim());
        }
    }

    /**
        Flatten out a TreEntry.

        @param tresFlat the map to flatten into.
        @param treEntry the TreEntry to flatten.
        @param parentName the name of the parent, required for namespacing.
    */
    private void flattenOneTreEntry(final Map<String, String> tresFlat, final TreEntry treEntry, final String parentName) {
        if ((treEntry.getName() != null) && (treEntry.getFieldValue() != null)) {
            String key = String.format("%s_%s", parentName, treEntry.getName());
            String value = treEntry.getFieldValue().trim();
            tresFlat.put(key, value);
        } else if (treEntry.getGroups() != null) {
            int groupCounter = 0;
            for (TreGroup group : treEntry.getGroups()) {
                groupCounter++;
                for (TreEntry entryInGroup : group.getEntries()) {
                    String key = String.format("%s_%s_%d", parentName, entryInGroup.getName(), groupCounter);
                    String value = entryInGroup.getFieldValue();

                    if (value != null) {
                        value = value.trim();
                    } else {
                        value = "";
                        LOGGER.warn("Value for %s attribute %s is missing. Substituting blank.", parentName, key);
                    }

                    tresFlat.put(key, value);
                }
            }
        }
    }

    /**
        Merge a TreCollection into the TREs already associated with this segment.

        @param tresToAdd the TRE collection to add.
    */
    public final void mergeTREs(final TreCollection tresToAdd) {
        treCollection.add(tresToAdd);
    }
}
