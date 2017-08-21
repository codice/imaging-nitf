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
import java.util.List;
import java.util.function.Supplier;
import org.codice.imaging.nitf.core.tre.Tre;
import org.codice.imaging.nitf.core.tre.TreCollection;

/**
 * Builder for collections of TREs.
 */
public class TreCollectionBuilder implements Supplier<TreCollection> {

    private final List<Tre> treCollectionEntries = new ArrayList<>();

    /**
     * Constructor.
     */
    public TreCollectionBuilder() {
    }

    /**
     * Add a TRE to the collection.
     *
     * @param tre the TRE to add to the collection.
     * @return this builder, to support method chaining.
     */
    public final TreCollectionBuilder add(final Tre tre) {
        treCollectionEntries.add(tre);
        return this;
    }

    /**
     * Add multiple TREs to the collection.
     *
     * @param collectionToAdd the TREs to add to the collection.
     * @return this builder, to support method chaining.
     */
    public final TreCollectionBuilder add(final TreCollection collectionToAdd) {
        if (collectionToAdd != null) {
            treCollectionEntries.addAll(collectionToAdd.getTREs());
        }
        return this;
    }

    /**
     * Remove the specified TRE.
     *
     * @param tre the TRE to remove (must be from the list)
     * @return true if the TRE was removed, otherwise false.
     */
    public final boolean remove(final Tre tre) {
        return treCollectionEntries.remove(tre);
    }

    /**
     * Get an empty collection.
     *
     * @return empty collection of TREs.
     */
    public static TreCollection getEmptyCollection() {
        TreCollectionBuilder builder = new TreCollectionBuilder();
        return builder.get();
    }

    @Override
    public final TreCollection get() {
        return new TreCollectionImpl(treCollectionEntries);
    }

}
