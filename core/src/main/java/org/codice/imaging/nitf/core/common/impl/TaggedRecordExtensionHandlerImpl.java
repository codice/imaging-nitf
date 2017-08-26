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
package org.codice.imaging.nitf.core.common.impl;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.codice.imaging.nitf.core.common.TaggedRecordExtensionHandler;
import org.codice.imaging.nitf.core.tre.Tre;
import org.codice.imaging.nitf.core.tre.TreCollection;
import org.codice.imaging.nitf.core.tre.TreEntry;
import org.codice.imaging.nitf.core.tre.TreGroup;
import org.codice.imaging.nitf.core.tre.TreGroupListEntry;
import org.codice.imaging.nitf.core.tre.TreSimpleEntry;
import org.codice.imaging.nitf.core.tre.impl.TreCollectionBuilder;

/**
 * Common data elements for NITF segments and file header.
 */
public abstract class TaggedRecordExtensionHandlerImpl implements TaggedRecordExtensionHandler {

    private final TreCollectionBuilder treCollectionBuilder = new TreCollectionBuilder();

    /**
     * {@inheritDoc}
     */
    @Override
    public final TreCollection getTREsRawStructure() {
        return treCollectionBuilder.get();
    }

    /**
     * Return the TREs for this segment, flattened into a key-value pair map.
     *
     * This will build appropriate namespaces for each entry.
     *
     * @deprecated Use the raw structure. Flattening out the TREs does not have a single correct solution.
     *
     * @return TRE map.
     */
    @Deprecated
    @Override
    public final Map<String, String> getTREsFlat() {
        Map<String, String> tresFlat = new TreeMap<>();
        for (String treName : treCollectionBuilder.get().getUniqueNamesOfTRE()) {
            List<Tre> tresWithName = treCollectionBuilder.get().getTREsWithName(treName);
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
     * Flatten out a single TRE.
     *
     * @param onlyTre  the TRE to flatten.
     * @param tresFlat the map to flatten the TRE into.
     */
    private void flattenOnlyTre(final Tre onlyTre, final Map<String, String> tresFlat) {
        List<TreEntry> treEntries = onlyTre.getEntries();
        for (TreEntry treEntry : treEntries) {
            String label = String.format("%s_%s", onlyTre.getName(), treEntry.getName());
            flattenOneTreEntry(tresFlat, treEntry, label);
        }
    }

    /**
     * Flatten out a single TRE, where other TREs have the same name.
     *
     * @param thisTre  the TRE to flatten.
     * @param i        the index of this TRE (zero base), required for namespacing.
     * @param tresFlat the map to flatten the TRE into.
     */
    private void flattenThisTre(final Tre thisTre, final int i, final Map<String, String> tresFlat) {
        List<TreEntry> treEntries = thisTre.getEntries();
        for (TreEntry treEntry : treEntries) {
            if (treEntry.isSimpleField()) {
                TreSimpleEntry simpleEntry = (TreSimpleEntry) treEntry;
                tresFlat.put(String.format("%s_%d_%s", thisTre.getName(), i, treEntry.getName()), simpleEntry.getFieldValue().trim());
            }
        }
    }

    /**
     * Flatten out a TreEntry.
     *
     * @param tresFlat the map to flatten into.
     * @param treEntry the TreEntry to flatten.
     * @param label    the name of the treEntry.
     */
    private void flattenOneTreEntry(final Map<String, String> tresFlat, final TreEntry treEntry, final String label) {
        if (treEntry.isSimpleField()) {
            TreSimpleEntry simpleEntry = (TreSimpleEntry) treEntry;
            addValueToMap(tresFlat, label, simpleEntry);
        } else if (treEntry.hasGroups()) {
            TreGroupListEntry groupListEntry = (TreGroupListEntry) treEntry;
            processTreGroups(groupListEntry, label, tresFlat);
        }
    }

    private void processTreGroups(final TreGroupListEntry treEntry, final String parentName, final Map<String, String> tresFlat) {
        int groupCounter = 0;
        for (TreGroup group : treEntry.getGroups()) {
            groupCounter++;
            if (group.getEntries().size() > 1) {
                for (int entryCounter = 0; entryCounter < group.getEntries().size(); ++entryCounter) {
                    TreEntry entryInGroup = group.getEntries().get(entryCounter);
                    String label = String.format("%s_%d_%s", parentName, groupCounter, entryInGroup.getName());
                    flattenOneTreEntry(tresFlat, entryInGroup, label);
                }
            } else {
                TreEntry entryInGroup = group.getEntries().get(0);
                String label = String.format("%s_%d", parentName, groupCounter);
                flattenOneTreEntry(tresFlat, entryInGroup, label);
            }
        }
    }

    private void addValueToMap(final Map<String, String> tresFlat, final String key, final TreSimpleEntry treSimpleEntry) {
        String value = treSimpleEntry.getFieldValue().trim();
        tresFlat.put(key, value);
    }

    /**
     * Merge a TreCollection into the TREs already associated with this segment.
     *
     * @param tresToAdd the TRE collection to add.
     */
    public final void mergeTREs(final TreCollection tresToAdd) {
        treCollectionBuilder.add(tresToAdd);
    }
}
